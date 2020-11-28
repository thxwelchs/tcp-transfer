package io;

import domain.Source;
import service.SourceService;
import utils.ByteSerializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_TCP_PORT = 10055;
    private static final Queue<Source> queue = new ConcurrentLinkedQueue<>();
    private static final Map<Integer, Boolean> map = new ConcurrentHashMap<>();

    private Integer port;
    private SourceService sourceService;
    private ServerSocketChannel serverSocket;
    private ByteBuffer buffer = ByteBuffer.allocate(5000);
    private Selector selector;

    public Server(int port, SourceService sourceService) {
        this.port = port;
        this.sourceService = sourceService;
    }

    public Server(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    private void readData(ByteBuffer buffer, SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        try {
            buffer = ByteBuffer.allocate(5000);
            client.read(buffer);
            Datagram datagram = (Datagram) ByteSerializer.deserialize(buffer.array());
            System.out.println(datagram.getHeader());

            switch (datagram.getHeader()) {
                case ONE_SECONDS_REQ:
                    List<Source> sourceList = sourceService.findByCreatedTime(LocalDateTime.now().minusSeconds(1));
                    buffer.flip();
                    client.write(ByteBuffer.wrap(ByteSerializer.serialize(new Datagram(DatagramHeader.ONE_SECONDS_RES, sourceList))));
                    break;
                default:
                    throw new IOException("Invalid TCP Request");
            }

            buffer.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerAndSend(Selector selector, ServerSocketChannel serverSocket, Object object)
            throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        byte[] bytes = ByteSerializer.serialize(object);
        buffer = ByteBuffer.allocate(bytes.length);
        client.write(ByteBuffer.wrap(bytes));
        client.register(selector, SelectionKey.OP_READ);
        buffer.clear();
    }

    private void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
//        byte[] bytes = ByteSerializer.serialize(object);
//        buffer = ByteBuffer.allocate(bytes.length);
//        client.write(ByteBuffer.wrap(bytes));
        client.register(selector, SelectionKey.OP_READ);
//        buffer.clear();
    }

    public void start() {
        try {
            start(DEFAULT_HOST, port != null ? port : DEFAULT_TCP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(String host, int port) throws IOException {
        System.out.println("Master Server Starting...");
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(host, port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Master Server On " + host + ":" + port);

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();

                if (key.isAcceptable()) {
                    register(selector, serverSocket);
                }

                if (key.isReadable()) {
                    readData(buffer, key);
                }

                iter.remove();
            }
        }
    }
}
