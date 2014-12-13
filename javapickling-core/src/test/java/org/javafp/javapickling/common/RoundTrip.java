package org.javafp.javapickling.common;

public class RoundTrip {
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
