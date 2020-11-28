package io;

import utils.ByteSerializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client implements DataTransferable {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_TCP_PORT = 10055;
    private static SocketChannel client;
    private static ByteBuffer buffer;
    private int minBufferSize;

    public Client(int bufferSize) {
        minBufferSize = bufferSize;
        buffer = ByteBuffer.allocate(bufferSize);
    }

    public void stop() throws IOException {
        client.close();
        buffer = null;
    }

    public void connect() {
        connect(DEFAULT_HOST, DEFAULT_TCP_PORT);
    }

    @Override
    public Datagram readData() {
        Datagram datagram = null;
        try {
            buffer = ByteBuffer.allocate(minBufferSize);
            client.read(buffer);
            datagram = (Datagram) ByteSerializer.deserialize(buffer.array());
            if (datagram.getHeader() == DatagramHeader.ONE_SECONDS_RES) {
            } else {
                throw new IOException("Invalid TCP Response");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            buffer.clear();
        }
        return datagram;
    }

    @Override
    public void sendData(Datagram datagram) {
        try {
            byte[] bytes = ByteSerializer.serialize(datagram);
            buffer = ByteBuffer.allocate(bytes.length);
            buffer = ByteBuffer.wrap(bytes);
            client.write(buffer);
            buffer.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void connect(String host, int port) {
        try {
            client = SocketChannel.open(new InetSocketAddress(host, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
