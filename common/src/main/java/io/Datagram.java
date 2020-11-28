package io;

import java.io.Serializable;

public class Datagram implements Serializable {
    private static final long serialVersionUID = 10L;

    private DatagramHeader header;
    private Object payload;

    public Datagram(DatagramHeader header, Object payload) {
        this.header = header;
        this.payload = payload;
    }

    public DatagramHeader getHeader() {
        return header;
    }

    public Object getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Datagram{" +
                "header='" + header + '\'' +
                ", payload=" + payload +
                '}';
    }
}
