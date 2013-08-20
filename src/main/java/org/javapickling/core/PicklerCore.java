package org.javapickling.core;

import java.util.List;
import java.util.Map;

/**
 * PicklerCore models the core set of picklers an implementation must provide.
 * @param <PF> The pickle format.
 */
public interface PicklerCore<PF> {

    /**
     * @return a Pickler for Booleans.
     */
    Pickler<Boolean, PF> boolean_p();

    /**
     * @return a Pickler for Booleans.
     */
    Pickler<Byte, PF> byte_p();

    /**
     * @return a Pickler for Character.
     */
    Pickler<Character, PF> char_p();

    /**
     * @return a Pickler for String.
     */
    Pickler<String, PF> string_p();

    /**
     * @return a Pickler for Integer.
     */
    Pickler<Integer, PF> integer_p();

    /**
     * @return a Pickler for Short.
     */
    Pickler<Short, PF> short_p();

    /**
     * @return a Pickler for Long.
     */
    Pickler<Long, PF> long_p();

    /**
     * @return a Pickler for Float.
     */
    Pickler<Float, PF> float_p();

    /**
     * @return a Pickler for Double.
     */
    Pickler<Double, PF> double_p();

    /**
     * @param <T>
     * @return a Pickler for an enum.
     */
    <T extends Enum<T>> Pickler<T, PF> enum_p(final Class<T> enumClass);

    /**
     * Provide a Pickler for an array.
     * @param elemPickler a Pickler for the array element type.
     * @param <T>
     * @return a Pickler for an array.
     */
    <T> Pickler<T[], PF> array_p(final Pickler<T, PF> elemPickler);

    /**
     * Provide a Pickler for an List.
     * @param elemPickler a Pickler for the List element type.
     * @param <T>
     * @return a Pickler for an List.
     */
    <T> Pickler<List<T>, PF> list_p(final Pickler<T, PF> elemPickler);

    /**
     * Provide a Pickler for a homogeneous Map.
     * @param elemPickler a Pickler for the Map value type.
     * @param <T>
     * @return a Pickler for an Map.
     */
    <T> Pickler<Map<String, T>, PF> map_p(final Pickler<T, PF> elemPickler);

    /**
     * Provide a Pickler corresponding to the specified Class.
     * @param clazz class specifying the required Pickler type.
     * @param <T>
     * @return a Pickler for the specified Class.
     */
    <T> Pickler<T, PF> object_p(final Class<T> clazz);

    Pickler<Object, PF> unknown_p();

    /**
     * Provide a proxy for pickling heterogeneous maps (including objects).
     * @return a Pickler for a Map.
     */
    MapPickler<PF> object_map();
}
