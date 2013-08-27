package org.javapickling.byteio;

import org.javapickling.common.ComplexClass;
import org.junit.Assert;
import org.javapickling.core.Pickler;
import org.javapickling.example.Utils;
import org.junit.Test;

import java.io.*;

public class ByteIOPicklerTest {

    private static final ByteIOPicklerCore byteIOPickler = new ByteIOPicklerCore();

    @Test
    public void testPickle() throws IOException, ClassNotFoundException {

        final ComplexClass simple = ComplexClass.createInstance(true);

        final org.javapickling.common.Utils.RoundTrip byteIOTimeMs = roundTripViaByteIO(simple);
        System.out.println(byteIOTimeMs);

        Utils.roundTripViaJavaSer(simple);
        final org.javapickling.common.Utils.RoundTrip javaSerTimeMs = Utils.roundTripViaJavaSer(simple);
        System.out.println(javaSerTimeMs);
    }

    private static org.javapickling.common.Utils.RoundTrip roundTripViaByteIO(ComplexClass simple) throws IOException {

        final Pickler<ComplexClass, ByteIO> pickler = byteIOPickler.object_p(ComplexClass.class);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ByteIO byteOutput = new ByteIO(new DataOutputStream(baos));

        final long startTime1 = System.nanoTime();
        pickler.pickle(simple, byteOutput);
        final long endTime1 = System.nanoTime();

        final byte[] ba = baos.toByteArray();
        final int size = ba.length;

        final ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        final ByteIO byteInput = new ByteIO(new DataInputStream(bais));

        final long startTime2 = System.nanoTime();
        final ComplexClass simple2 = pickler.unpickle(byteInput);
        final long endTime2 = System.nanoTime();

        Assert.assertEquals(simple, simple2);

        return new org.javapickling.common.Utils.RoundTrip("ByteIOPickler", endTime1 - startTime1, endTime2 - startTime2, size);
    }
}
