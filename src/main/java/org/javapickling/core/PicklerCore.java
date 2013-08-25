package org.javapickling.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * PicklerCore models the core set of picklers an implementation must provide.
 * @param <PF> The pickle format.
 */
public interface PicklerCore<PF> {

    /**
     * @return a Pickler for null.
     */
    Pickler<Object, PF> null_p();

    /**
     * @return a Pickler for Boolean.
     */
    Pickler<Boolean, PF> boolean_p();

    /**
     * @return a Pickler for Byte.
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
     * @param elemClass element class.
     * @param <T>
     * @return a Pickler for an array.
     */
    <T> Pickler<T[], PF> array_p(final Pickler<T, PF> elemPickler, final Class<T> elemClass);

    /**
     * Provide a Pickler for an List.
     * @param elemPickler a Pickler for the List element type.
     * @param listClass class type for List.
     * @param <T>
     * @return a Pickler for an List.
     */
    <T> Pickler<List<T>, PF> list_p(
            final Pickler<T, PF> elemPickler,
            Class<? extends List> listClass);

    /**
     * Provide a Pickler for a homogeneous String to value Map.
     * @param valuePickler a Pickler for the Map value type.
     * @param <T>
     * @return a Pickler for an Map.
     */
    <T> Pickler<Map<String, T>, PF> map_p(
            final Pickler<T, PF> valuePickler,
            final Class<? extends Map> mapClass);

    /**
     * Provide a Pickler for a homogeneous Map.
     * @param keyPickler a Pickler for the Map key type.
     * @param valuePickler a Pickler for the Map value type.
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V> Pickler<Map<K, V>, PF> map_p(
            final Pickler<K, PF> keyPickler,
            final Pickler<V, PF> valuePickler,
            final Class<?  extends Map> mapClass);

    /**
     * Provide a Pickler for a homogeneous Set.
     * @param elemPickler a Pickler for the Set value type.
     * @param setClass class type for Set.
     * @param <T>
     * @return a Pickler for an Map.
     */
    <T> Pickler<Set<T>, PF> set_p(
            final Pickler<T, PF> elemPickler,
            final Class<?  extends Set> setClass);

    /**
     * Provide a Pickler corresponding to the specified Class.
     * @param clazz class specifying the required Pickler type.
     * @param <T>
     * @return a Pickler for the specified Class.
     */
    <T> Pickler<T, PF> object_p(final Class<T> clazz);

    /**
     * Provide a Pickler for objects where the static type is unknown.
     * @return a Pickler for weakly-typed objects.
     */
    Pickler<Object, PF> object_p();

    /**
     * Provide a proxy for pickling heterogeneous maps of strings to a static type.
     * Generally used for pickling objects.
     * @return a Pickler for a object/Map.
     */
    MapPickler<PF> object_map();
}
