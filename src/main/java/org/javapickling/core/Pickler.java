package org.javapickling.core;

import java.io.IOException;

/**
 * Interface for Pickler classes, which implement both the pickling and unpickling of a single type.
 * @param <T> The type to be pickled.
 * @param <PF> The pickle format.
 */
public interface Pickler<T, PF> {
    /**
     * Pickle a value into the specified pickle format.
     * @param t The value to be pickled.
     * @param target The pickle target. May be null if the pickled result can be encapsulated in the return value.
     * @return The pickled result.
     * @throws IOException
     */
    PF pickle(T t, PF target) throws IOException;

    /**
     * Unpickle a value from the specified pickle source.
     * @param source The pickle source.
     * @return The unpickled value.
     * @throws IOException
     */
    T unpickle(PF source) throws IOException;
}
