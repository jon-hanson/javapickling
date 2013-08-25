package org.javapickling.json;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.javapickling.common.SimpleClass;
import org.javapickling.core.*;
import org.javapickling.example.Utils;
import org.junit.Test;

import java.io.IOException;

public class JsonPicklerTest {

    private static final JsonPicklerCore jsonPickler = new JsonPicklerCore();

    static {
        jsonPickler.register(SimpleClass.class, SimpleClass.Pickler.class);
    }

    @Test
    public void testPickle() throws IOException, ClassNotFoundException {

        final SimpleClass simple = SimpleClass.createInstance(true);

        final org.javapickling.common.Utils.RoundTrip jsonTimeMs = roundTripViaJson(simple);
        System.out.println(jsonTimeMs);

        Utils.roundTripViaJavaSer(simple);
        final org.javapickling.common.Utils.RoundTrip javaSerTimeMs = Utils.roundTripViaJavaSer(simple);
        System.out.println(javaSerTimeMs);
    }

    private static org.javapickling.common.Utils.RoundTrip roundTripViaJson(SimpleClass simple) throws IOException {

        final long startTime1 = System.nanoTime();

        final Pickler<SimpleClass, JsonNode> pickler = jsonPickler.object_p(SimpleClass.class);

        final JsonNode node = pickler.pickle(simple, null);

        final long endTime1 = System.nanoTime();

        System.out.println("JSON=");
        System.out.println(node);
        final int size = node.toString().getBytes().length;

        final long startTime2 = System.nanoTime();

        final SimpleClass simple2 = pickler.unpickle(node);

        final long endTime2 = System.nanoTime();

        Assert.assertEquals(simple, simple2);

        return new org.javapickling.common.Utils.RoundTrip("JsonPickler", endTime1 - startTime1, endTime2 - startTime2, size);
    }
}
