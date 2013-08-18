package org.javapickling.byteio;

import org.javapickling.common.*;
import org.javapickling.core.Pickler;
import org.junit.Test;

import java.io.*;

public class ByteIOPicklerTest {

    private static final ByteIOPicklerCore byteIOPickler = new ByteIOPicklerCore();

    static {
        byteIOPickler.register(Person.class, PersonPickler.class);
        byteIOPickler.register(House.class, HousePickler.class);
    }

    @Test
    public void testPickle() throws IOException, ClassNotFoundException {

        final House house = Utils.house();

        final Utils.RoundTrip byteIOTimeMs = roundTripViaByteIO(house);
        System.out.println(byteIOTimeMs);

        final Utils.RoundTrip javaSerTimeMs = Utils.roundTripViaJavaSer(house);
        System.out.println(javaSerTimeMs);
    }

    private static Utils.RoundTrip roundTripViaByteIO(House house) throws IOException {

        final long startTime1 = System.nanoTime();

        final Pickler<House, ByteIO> pickler = byteIOPickler.object(House.class);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ByteIO byteOutput = new ByteIO(new DataOutputStream(baos));

        pickler.pickle(house, byteOutput);
        final long endTime1 = System.nanoTime();

        final byte[] ba = baos.toByteArray();
        final int size = ba.length;

        final long startTime2 = System.nanoTime();

        final ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        final ByteIO byteInput = new ByteIO(new DataInputStream(bais));
        final House house2 = pickler.unpickle(byteInput);

        final long endTime2 = System.nanoTime();

        return new Utils.RoundTrip("ByteIOPickler", endTime1 - startTime1, endTime2 - startTime2, size);
    }
}
