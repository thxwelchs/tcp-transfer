package utils;

import java.io.*;

public class ByteSerializer {
    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        Object obj;
        ByteArrayInputStream bytesIn;
        ObjectInputStream ois = null;
        bytesIn = new ByteArrayInputStream(bytes);
        ois = new ObjectInputStream(bytesIn);
        obj = ois.readObject();
//        bytesIn.close();
//        ois.close();

        return obj;
    }


    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bytesOut);
        oos.writeObject(obj);
        oos.flush();
        byte[] bytes = bytesOut.toByteArray();
//        System.out.println("bytes length: " + bytes.length);
        bytesOut.close();
        oos.close();
        return bytes;
    }
}
