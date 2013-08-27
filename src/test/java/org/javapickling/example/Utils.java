package org.javapickling.example;

import java.io.*;

public abstract class Utils {

    public static <T> org.javapickling.common.Utils.RoundTrip roundTripViaJavaSer(T value) throws IOException, ClassNotFoundException {


        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);

        final long startTime1 = System.nanoTime();
        oos.writeObject(value);
        final long endTime1 = System.nanoTime();

        final byte[] ba = baos.toByteArray();
        final int size = ba.length;

        final ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        final ObjectInputStream ois = new ObjectInputStream(bais);

        final long startTime2 = System.nanoTime();
        final T value2 = (T)ois.readObject();
        final long endTime2 = System.nanoTime();

        return new org.javapickling.common.Utils.RoundTrip("JavaSer", endTime1 - startTime1, endTime2 - startTime2, size);
    }
}
