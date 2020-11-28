package io;

import io.DataTransferable;
import io.Datagram;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BlockingClient implements DataTransferable {
    private String host;
    private int port;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public BlockingClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void close() {
        try {
            if(oos != null) { oos.flush(); oos.close(); }
            if(ois != null) ois.close();
//            if(socket != null && !socket.isClosed()) socket.close();
//            socket.close(); // 서버쪽에서 처리하여 CLOSE_WAIT가 되지 않도록 함
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            socket = new Socket(host, port);
//            socket.close(); // 서버쪽에서 처리하여 CLOSE_WAIT가 되지 않도록 함
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Datagram readData() throws IOException, ClassNotFoundException {
        ois = new ObjectInputStream(socket.getInputStream());
        Datagram datagram = (Datagram) ois.readObject();
        return datagram;
    }

    @Override
    public void sendData(Datagram datagram) throws IOException {
        oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(datagram);
//        oos.close();
    }
}
