package org.javapickling.core;

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
    public <U extends Enum<U>> Pickler<U, PF> enum_p(Class<U> enumClass) {
        return core.enum_p(enumClass);
    }

    @Override
    public <U> Pickler<Class<U>, PF> class_p() {
        return core.class_p();
    }

    @Override
    public <U, V extends U> Pickler<Class<V>, PF> class_p(Class<U> clazz) {
        return core.class_p(clazz);
    }

    @Override
    public <U> Pickler<U[], PF> array_p(Pickler<U, PF> elemPickler, Class<U> elemClass) {
        return core.array_p(elemPickler, elemClass);
    }

    @Override
    public <U> Pickler<List<U>, PF> list_p(Pickler<U, PF> elemPickler, Class<? extends List> listClass) {
        return core.list_p(elemPickler, listClass);
    }

    @Override
    public <U> Pickler<Map<String, U>, PF> map_p(Pickler<U, PF> valuePickler, Class<? extends Map> mapClass) {
        return core.map_p(valuePickler, mapClass);
    }

    @Override
    public <K, V> Pickler<Map<K, V>, PF> map_p(Pickler<K, PF> keyPickler, Pickler<V, PF> valuePickler, Class<? extends Map> mapClass) {
        return core.map_p(keyPickler, valuePickler, mapClass);
    }

    @Override
    public <U> Pickler<Set<U>, PF> set_p(Pickler<U, PF> elemPickler, Class<? extends Set> setClass) {
        return core.set_p(elemPickler, setClass);
    }

    @Override
    public <U> Pickler<U, PF> object_p(Class<U> clazz) {
        return core.object_p(clazz);
    }

    @Override
    public <U, V extends U> Pickler<V, PF> generic_p(final Class<U> clazz, Pickler<?, PF>... picklers) {
        return core.generic_p(clazz, picklers);
    }

    @Override
    public Pickler<Object, PF> d_object_p() {
        return core.d_object_p();
    }

    @Override
    public <U, V extends U> Pickler<V, PF> d_object_p(Class<U> clazz) {
        return core.d_object_p(clazz);
    }

    @Override
    public ObjectMapPickler<PF> object_map() {
        return core.object_map();
    }

    @Override
    public <U> Field<U, PF> field(String name, Pickler<U, PF> pickler) {
        return core.field(name, pickler);
    }

    @Override
    public <U> Field<U, PF> null_field(String name, Pickler<U, PF> pickler) {
        return core.null_field(name, pickler);
    }

    @Override
    public <U> Pickler<U, PF> nullable(final Pickler<U, PF> pickler) {
        return core.nullable(pickler);
    }
}
