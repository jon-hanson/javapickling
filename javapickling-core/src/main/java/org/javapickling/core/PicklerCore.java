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
     * @return a Pickler for Class objects.
     */
    <T> Pickler<Class<T>, PF> class_p();

    /**
     * @return a Pickler for Class objects.
     */
    <T, S extends T> Pickler<Class<S>, PF> class_p(Class<T> clazz);

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
     * Provide a Pickler for an List.
     * @param elemPickler a Pickler for the List element type.
     * @param <T>
     * @return a Pickler for an List.
     */
    <T> Pickler<List<T>, PF> list_p(final Pickler<T, PF> elemPickler);

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
     * Provide a Pickler for a homogeneous String to value Map.
     * @param valuePickler a Pickler for the Map value type.
     * @param <T>
     * @return a Pickler for an Map.
     */
    <T> Pickler<Map<String, T>, PF> map_p(final Pickler<T, PF> valuePickler);

    /**
     * Provide a Pickler for a homogeneous Map.
     * @param keyPickler a Pickler for the Map key type.
     * @param valuePickler a Pickler for the Map value type.
     * @param mapClass the Map implementation to use.
     * @param <K>
     * @param <V>
     * @return a Pickler for a homogeneous Map
     */
    <K, V> Pickler<Map<K, V>, PF> map_p(
            final Pickler<K, PF> keyPickler,
            final Pickler<V, PF> valuePickler,
            final Class<? extends Map> mapClass);

    /**
     * Provide a Pickler for a homogeneous Map.
     * @param keyPickler a Pickler for the Map key type.
     * @param valuePickler a Pickler for the Map value type.
     * @param <K>
     * @param <V>
     * @return a Pickler for a homogeneous Map
     */
    <K, V> Pickler<Map<K, V>, PF> map_p(
            final Pickler<K, PF> keyPickler,
            final Pickler<V, PF> valuePickler);

    /**
     * Provide a Pickler for a homogeneous Set.
     * @param elemPickler a Pickler for the Set value type.
     * @param setClass class type for Set.
     * @param <T>
     * @return a Pickler for an Map.
     */
    <T> Pickler<Set<T>, PF> set_p(
            final Pickler<T, PF> elemPickler,
            final Class<? extends Set> setClass);

    /**
     * Provide a Pickler for a homogeneous Set.
     * @param elemPickler a Pickler for the Set value type.
     * @param <T>
     * @return a Pickler for an Map.
     */
    <T> Pickler<Set<T>, PF> set_p(final Pickler<T, PF> elemPickler);

    /**
     * Provide a Pickler corresponding to the specified Class.
     * @param clazz class specifying the required Pickler type.
     * @param <T>
     * @return a Pickler for the specified Class.
     */
    <T> Pickler<T, PF> object_p(final Class<T> clazz);

    /**
     * Provide a Pickler corresponding to the specified Generic Class.
     * @param clazz class specifying the required Pickler type.
     * @param picklers a list of picklers corresponding to the value type parameters to T.
     * @param <T>
     * @return a Pickler for the specified Class.
     */
    <T, S extends T> Pickler<S, PF> generic_p(final Class<T> clazz, Pickler<?, PF>... picklers);

    /**
     * Provide a Pickler for objects where the static type is unknown.
     * @return a Pickler for weakly-typed objects.
     */
    Pickler<Object, PF> d_object_p();

    /**
     * Provide a Pickler for objects where the static type unknown but is a subclass of T.
     * @return a Pickler for weakly-typed objects.
     */
    <T, S extends T> Pickler<S, PF> d_object_p(final Class<T> clazz);

    /**
     * Provide a proxy for pickling heterogeneous maps of strings to a static type.
     * Generally used for defining picklers for objects.
     * @return a Pickler for a object/Map.
     */
    ObjectPickler<PF> object_map();

    /**
     * Create a field handler.
     * @param name the name of the field.
     * @param pickler the pickler for the field value.
     * @param <T>
     * @return a field handler
     */
    <T> Field<T, PF> field(String name, Pickler<T, PF> pickler);

    /**
     * Create a field handler using reflection to create the field pickler.
     * @param clazz the containing class.
     * @param name the field name.
     * @param <T>
     * @return a field handler
     */
    <T> Field<T, PF> field(Class<?> clazz, String name);

    /**
     * Create a field handler for a nullable field.
     * @param name the name of the field.
     * @param pickler the pickler for the field value.
     * @param <T>
     * @return a field handler
     */
    <T> Field<T, PF> null_field(String name, Pickler<T, PF> pickler);

    /**
     * Create a field handler for a nullable field.
     * @param clazz the containing class.
     * @param name the field name.
     * @param <T>
     * @return a field handler
     */
    <T> Field<T, PF> null_field(Class<?> clazz, String name);

    /**
     * Convert a pickler into one that checks for nulls.
     * @param pickler
     * @param <T>
     * @return a pickler
     */
    <T> Pickler<T, PF> nullable(final Pickler<T, PF> pickler);

}
