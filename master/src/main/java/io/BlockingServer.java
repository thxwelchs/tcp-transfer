package io;

import domain.Source;
import domain.TransferableDataQueue;
import service.SourceService;
import io.Datagram;
import io.DatagramHeader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

public class BlockingServer {
    private Socket socket;
    private ServerSocket serverSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private int port;
    private SourceService sourceService;
    private TransferableDataQueue<Source> queue;

    public BlockingServer(int port, SourceService sourceService, TransferableDataQueue<Source> queue) {
        this.port = port;
        this.sourceService = sourceService;
        this.queue = queue;
    }

    public void close() {
        try {
            if(oos != null) { oos.flush(); oos.close(); }
            if(ois != null) ois.close();
            if(socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Master Blocking Server Starting");
        System.out.println("Master Blocking Server On " + port);
        try {
            serverSocket = new ServerSocket(port);
            while(true) {
                socket = serverSocket.accept();
                ois = new ObjectInputStream(socket.getInputStream());

                Datagram datagram = (Datagram) ois.readObject();
                System.out.println(datagram.getHeader());
                switch (datagram.getHeader()) {
                    case TEN_SECONDS_REQ:
                        List<Source> suspenseSourceList = sourceService.findByCreatedTimeLessThan(LocalDateTime.now().minusSeconds(10));
                        queue.enqueueAll(suspenseSourceList);
                        System.out.println("getProcessedQueueSize : " + queue.getProcessedQueueSize());
//                        System.out.println("cdcd: " + suspenseSourceList.size());
//                        System.out.println("asdfasdf: " + queue.getData().size());
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(new Datagram(DatagramHeader.TEN_SECONDS_RES, queue.getDatas()));
                        break;
                    default:
                        throw new IOException("Invalid TCP Request");
                }
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
}
