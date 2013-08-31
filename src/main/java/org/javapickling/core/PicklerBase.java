package org.javapickling.core;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base class for Pickler implementations.
 * Also acts as a proxy for core by implementing the PicklerCore interface.
 * @param <T>
 * @param <PF>
 */
public abstract class PicklerBase<T, PF> implements Pickler<T, PF>, PicklerCore<PF> {

    protected final PicklerCore<PF> core;

    protected PicklerBase(PicklerCore<PF> core) {
        this.core = core;
    }

    @Override
    public Pickler<Object, PF> null_p() {
        return core.null_p();
    }

    @Override
    public Pickler<Boolean, PF> boolean_p() {
        return core.boolean_p();
    }

    @Override
    public Pickler<Byte, PF> byte_p() {
        return core.byte_p();
    }

    @Override
    public Pickler<Character, PF> char_p() {
        return core.char_p();
    }

    @Override
    public Pickler<String, PF> string_p() {
        return core.string_p();
    }

    @Override
    public Pickler<Integer, PF> integer_p() {
        return core.integer_p();
    }

    @Override
    public Pickler<Short, PF> short_p() {
        return core.short_p();
    }

    @Override
    public Pickler<Long, PF> long_p() {
        return core.long_p();
    }

    @Override
    public Pickler<Float, PF> float_p() {
        return core.float_p();
    }

    @Override
    public Pickler<Double, PF> double_p() {
        return core.double_p();
    }

    @Override
    public <T extends Enum<T>> Pickler<T, PF> enum_p(Class<T> enumClass) {
        return core.enum_p(enumClass);
    }

    @Override
    public <T> Pickler<Class<T>, PF> class_p() {
        return core.class_p();
    }

    @Override
    public <T, S extends T> Pickler<Class<S>, PF> class_p(Class<T> clazz) {
        return core.class_p(clazz);
    }

    @Override
    public <T> Pickler<T[], PF> array_p(Pickler<T, PF> elemPickler, Class<T> elemClass) {
        return core.array_p(elemPickler, elemClass);
    }

    @Override
    public <T> Pickler<List<T>, PF> list_p(Pickler<T, PF> elemPickler, Class<? extends List> listClass) {
        return core.list_p(elemPickler, listClass);
    }

    @Override
    public <T> Pickler<Map<String, T>, PF> map_p(Pickler<T, PF> valuePickler, Class<? extends Map> mapClass) {
        return core.map_p(valuePickler, mapClass);
    }

    @Override
    public <K, V> Pickler<Map<K, V>, PF> map_p(Pickler<K, PF> keyPickler, Pickler<V, PF> valuePickler, Class<? extends Map> mapClass) {
        return core.map_p(keyPickler, valuePickler, mapClass);
    }

    @Override
    public <T> Pickler<Set<T>, PF> set_p(Pickler<T, PF> elemPickler, Class<? extends Set> setClass) {
        return core.set_p(elemPickler, setClass);
    }

    @Override
    public <T> Pickler<T, PF> object_p(Class<T> clazz) {
        return core.object_p(clazz);
    }

    @Override
    public Pickler<Object, PF> d_object_p() {
        return core.d_object_p();
    }

    @Override
    public <T, S extends T> Pickler<S, PF> d_object_p(Class<T> clazz) {
        return core.d_object_p(clazz);
    }

    @Override
    public MapPickler<PF> object_map() {
        return core.object_map();
    }

    @Override
    public <T> Field<T, PF> field(String name, Pickler<T, PF> pickler) {
        return core.field(name, pickler);
    }

    @Override
    public <T> Field<T, PF> null_field(String name, Pickler<T, PF> pickler) {
        return core.null_field(name, pickler);
    }

    @Override
    public <T> Pickler<T, PF> nullable(final Pickler<T, PF> pickler) {
        return core.nullable(pickler);
    }
}
