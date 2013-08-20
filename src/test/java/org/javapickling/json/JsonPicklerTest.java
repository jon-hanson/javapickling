package org.javapickling.json;

import com.fasterxml.jackson.databind.JsonNode;
import org.javapickling.common.*;
import org.javapickling.core.*;
import org.junit.Test;

import java.io.IOException;

public class JsonPicklerTest {

    private static final JsonPicklerCore jsonPickler = new JsonPicklerCore();

    static {
        jsonPickler.register(Person.class, PersonPickler.class);
        jsonPickler.register(House.class, HousePickler.class);
    }

    @Test
    public void testPickle() throws IOException, ClassNotFoundException {

        final House house = Utils.house(0);

        final Utils.RoundTrip jsonTimeMs = roundTripViaJson(house);
        System.out.println(jsonTimeMs);

        final Utils.RoundTrip javaSerTimeMs = Utils.roundTripViaJavaSer(house);
        System.out.println(javaSerTimeMs);
    }

    private static Utils.RoundTrip roundTripViaJson(House house) throws IOException {

        final long startTime1 = System.nanoTime();

        final Pickler<House, JsonNode> pickler = jsonPickler.object_p(House.class);

        final JsonNode node = pickler.pickle(house, null);

        final long endTime1 = System.nanoTime();

        System.out.println("JSON=");
        System.out.println(node);
        final int size = node.toString().length();

        final long startTime2 = System.nanoTime();

        final House house2 = pickler.unpickle(node);

        final long endTime2 = System.nanoTime();

        return new Utils.RoundTrip("JsonPickler", endTime1 - startTime1, endTime2 - startTime2, size);
    }
}
