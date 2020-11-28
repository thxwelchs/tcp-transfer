package io;

import java.io.IOException;

public interface DataTransferable {
    Datagram readData() throws IOException, ClassNotFoundException;
    void sendData(Datagram datagram) throws IOException;
}
