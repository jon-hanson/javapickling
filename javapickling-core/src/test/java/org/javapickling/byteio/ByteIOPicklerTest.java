package org.javapickling.byteio;

import org.javapickling.common.ComplexClass;
import org.javapickling.common.RoundTrip;
import org.junit.Assert;
import org.javapickling.core.Pickler;
import org.junit.Test;

import java.io.*;

public class ByteIOPicklerTest {

    private static final ByteIOPicklerCore byteIOPickler = ByteIOPicklerCore.create();

    @Test
    public void testPickle() throws Exception {

        final ComplexClass simple = ComplexClass.createInstance(true);

        final RoundTrip byteIOTimeMs = roundTripViaByteIO(simple);
        System.out.println(byteIOTimeMs);

        org.javapickling.common.Utils.roundTripViaJavaSer(simple);
        final RoundTrip javaSerTimeMs = org.javapickling.common.Utils.roundTripViaJavaSer(simple);
        System.out.println(javaSerTimeMs);
    }

    private static RoundTrip roundTripViaByteIO(ComplexClass complex) throws Exception {

        final Pickler<ComplexClass, ByteIO> pickler = byteIOPickler.object_p(ComplexClass.class);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ByteIO byteOutput = new ByteIO(new DataOutputStream(baos));

        final long startTime1 = System.nanoTime();
        pickler.pickle(complex, byteOutput);
        final long endTime1 = System.nanoTime();

        final byte[] ba = baos.toByteArray();
        final int size = ba.length;

        final ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        final ByteIO byteInput = new ByteIO(new DataInputStream(bais));

        final long startTime2 = System.nanoTime();
        final ComplexClass complex2 = pickler.unpickle(byteInput);
        final long endTime2 = System.nanoTime();

        Assert.assertEquals(complex, complex2);

        return new RoundTrip("ByteIOPickler", endTime1 - startTime1, endTime2 - startTime2, size);
    }
}
