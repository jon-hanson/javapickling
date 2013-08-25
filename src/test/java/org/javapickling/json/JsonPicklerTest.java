package org.javapickling.json;

import com.fasterxml.jackson.databind.JsonNode;
import org.javapickling.common.ComplexClass;
import org.junit.Assert;
import org.javapickling.core.*;
import org.javapickling.example.Utils;
import org.junit.Test;

import java.io.IOException;

public class JsonPicklerTest {

    private static final JsonPicklerCore jsonPickler = new JsonPicklerCore();

    static {
        jsonPickler.register(ComplexClass.class, ComplexClass.Pickler.class);
    }

    @Test
    public void testPickle() throws IOException, ClassNotFoundException {

        final ComplexClass simple = ComplexClass.createInstance(true);

        final org.javapickling.common.Utils.RoundTrip jsonTimeMs = roundTripViaJson(simple);
        System.out.println(jsonTimeMs);

        Utils.roundTripViaJavaSer(simple);
        final org.javapickling.common.Utils.RoundTrip javaSerTimeMs = Utils.roundTripViaJavaSer(simple);
        System.out.println(javaSerTimeMs);
    }

    private static org.javapickling.common.Utils.RoundTrip roundTripViaJson(ComplexClass simple) throws IOException {

        final long startTime1 = System.nanoTime();

        final Pickler<ComplexClass, JsonNode> pickler = jsonPickler.object_p(ComplexClass.class);

        final JsonNode node = pickler.pickle(simple, null);

        final long endTime1 = System.nanoTime();

        System.out.println("JSON=");
        System.out.println(node);
        final int size = node.toString().getBytes().length;

        final long startTime2 = System.nanoTime();

        final ComplexClass simple2 = pickler.unpickle(node);

        final long endTime2 = System.nanoTime();

        Assert.assertEquals(simple, simple2);

        return new org.javapickling.common.Utils.RoundTrip("JsonPickler", endTime1 - startTime1, endTime2 - startTime2, size);
    }
}
