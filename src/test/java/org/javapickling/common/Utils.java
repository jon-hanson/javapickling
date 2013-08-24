package org.javapickling.common;

import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;

public abstract class Utils {

    public static class RoundTrip {
        public final String name;
        public final long pickleTimeNs;
        public final long unpickleTimeNs;
        public final int size;

        public RoundTrip(String name, long pickleTimeNs, long unpickleTimeNs, int size) {
            this.name = name;
            this.pickleTimeNs = pickleTimeNs;
            this.unpickleTimeNs = unpickleTimeNs;
            this.size = size;
        }

        @Override
        public String toString() {
            final long pickleTime = pickleTimeNs / 1000;
            final long unpickleTime = unpickleTimeNs / 1000;
            final long total = (pickleTimeNs + unpickleTimeNs) / 1000;
            return "Round-trip via " + name + " serialisation took " +
                    (pickleTime / 1000.0) + " + " +
                    (unpickleTime / 1000.0) + " = " +
                    (total / 1000.0) + "ms. Pickled size was " + size + " bytes.";
        }
    }

    public static Person<Long> createPerson(String name, int age, boolean female, int i, Long test, Object obj) {
        final List<Long> longs = Lists.newLinkedList();
        longs.add(0l);
        if (i > 0) longs.add(1l);
        if (i > 1) longs.add(2l);
        if (i > 2) longs.add(3l);
        return new Person(name, age, female, longs, test, obj);
    }

    public static List<Person<Long>> persons() {
        final List<Person<Long>> persons = Lists.newArrayListWithCapacity(3);
        String[] names = {"Zap", "Bang"};
        persons.add(createPerson("tom", 10, false, 0, 12l, 69.69));
        persons.add(createPerson("diane", 20, true, 1, 34l, names));
        persons.add(createPerson("harry", 30, false, 2, -999l, null));
        return persons;
    }

    public static House house(int i) {
        if (i == 0)
            return new House(House.Type.DETACHED, 1.23, persons());
        else if (i == 1)
            return new House(House.Type.SEMI_DETACHED, 2.34, persons());
        else
            return new House(House.Type.FLAT, 3.45, persons());
    }

    public static RoundTrip roundTripViaJavaSer(House house) throws IOException, ClassNotFoundException {

        final long startTime1 = System.nanoTime();

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(house);

        final long endTime1 = System.nanoTime();

        final byte[] ba = baos.toByteArray();
        final int size = ba.length;

        final long startTime2 = System.nanoTime();

        final ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        final ObjectInputStream ois = new ObjectInputStream(bais);
        final House house2 = (House)ois.readObject();

        final long endTime2 = System.nanoTime();

        return new RoundTrip("JavaSer", endTime1 - startTime1, endTime2 - startTime2, size);
    }
}
