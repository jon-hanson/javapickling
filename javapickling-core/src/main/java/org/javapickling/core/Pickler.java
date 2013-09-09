package org.javapickling.core;

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
     * @throws Exception
     */
    PF pickle(T t, PF target) throws Exception;

    /**
     * Unpickle a value from the specified pickle source.
     * @param source The pickle source.
     * @return The unpickled value.
     * @throws Exception
     */
    T unpickle(PF source) throws Exception;
}
