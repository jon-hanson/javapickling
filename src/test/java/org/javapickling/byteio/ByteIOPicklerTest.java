package org.javapickling.byteio;

import org.junit.Assert;
import org.javapickling.common.SimpleClass;
import org.javapickling.core.Pickler;
import org.javapickling.example.Utils;
import org.junit.Test;

import java.io.*;

public class ByteIOPicklerTest {

    private static final ByteIOPicklerCore byteIOPickler = new ByteIOPicklerCore();

    static {
        byteIOPickler.register(SimpleClass.class, SimpleClass.Pickler.class);
    }

    @Test
    public void testPickle() throws IOException, ClassNotFoundException {

        final SimpleClass simple = SimpleClass.createInstance(true);

        final org.javapickling.common.Utils.RoundTrip byteIOTimeMs = roundTripViaByteIO(simple);
        System.out.println(byteIOTimeMs);

        Utils.roundTripViaJavaSer(simple);
        final org.javapickling.common.Utils.RoundTrip javaSerTimeMs = Utils.roundTripViaJavaSer(simple);
        System.out.println(javaSerTimeMs);
    }

    private static org.javapickling.common.Utils.RoundTrip roundTripViaByteIO(SimpleClass simple) throws IOException {

        final long startTime1 = System.nanoTime();

        final Pickler<SimpleClass, ByteIO> pickler = byteIOPickler.object_p(SimpleClass.class);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ByteIO byteOutput = new ByteIO(new DataOutputStream(baos));

        pickler.pickle(simple, byteOutput);
        final long endTime1 = System.nanoTime();

        final byte[] ba = baos.toByteArray();
        final int size = ba.length;

        final long startTime2 = System.nanoTime();

        final ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        final ByteIO byteInput = new ByteIO(new DataInputStream(bais));
        final SimpleClass simple2 = pickler.unpickle(byteInput);

        final long endTime2 = System.nanoTime();

        Assert.assertEquals(simple, simple2);

        return new org.javapickling.common.Utils.RoundTrip("ByteIOPickler", endTime1 - startTime1, endTime2 - startTime2, size);
    }
}
