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
    Pickler<Boolean, PF> bool();

    /**
     * @return a Pickler for Character.
     */
    Pickler<Character, PF> chr();

    /**
     * @return a Pickler for String.
     */
    Pickler<String, PF> str();

    /**
     * @return a Pickler for Integer.
     */
    Pickler<Integer, PF> integer();

    /**
     * @return a Pickler for Long.
     */
    Pickler<Long, PF> lng();

    /**
     * @return a Pickler for Double.
     */
    Pickler<Double, PF> dbl();

    /**
     * @param <T>
     * @return a Piclker for an enum.
     */
    <T extends Enum<T>> Pickler<T, PF> enm(final Class<T> enumClass);

    /**
     * Provide a Pickler for an array.
     * @param elemPickler a Pickler for the array element type.
     * @param <T>
     * @return a Pickler for an array.
     */
    <T> Pickler<T[], PF> array(final Pickler<T, PF> elemPickler);

    /**
     * Provide a Pickler for an List.
     * @param elemPickler a Pickler for the List element type.
     * @param <T>
     * @return a Pickler for an List.
     */
    <T> Pickler<List<T>, PF> list(final Pickler<T, PF> elemPickler);

    /**
     * Provide a Pickler for a homogeneous Map.
     * @param elemPickler a Pickler for the Map value type.
     * @param <T>
     * @return a Pickler for an Map.
     */
    <T> Pickler<Map<String, T>, PF> map(final Pickler<T, PF> elemPickler);

    /**
     * Provide a proxy for pickling heterogeneous maps (including objects).
     * @return a Pickler for a Map.
     */
    MapPickler<PF> map();

    /**
     * Provide a Pickler corresponding to the specified Class.
     * @param clazz class specifing the required Pickler type.
     * @param <T>
     * @return a Pickler for the specified Class.
     */
    <T> Pickler<T, PF> object(final Class<T> clazz);
}
