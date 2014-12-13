package org.javafp.javapickling.core;

/**
 * An intermediate interface though which objects composed of fields can be unpickled.
 * @param <PF> the pickle format.
 */
public interface FieldUnpickler<PF> {

    /**
     * Unpickle a field.
     * @param name Field name
     * @param pickler Field pickler
     * @param <T> Field type
     * @return The unpickled value
     * @throws Exception
     */
    <T> T field(String name, Pickler<T, PF> pickler) throws Exception;

    /**
     * Unpickle a field.
     * @param field Field name and pickler.
     * @param <T> Field type.
     * @return The unpickled value
     * @throws Exception
     */
    <T> T field(Field<T, PF> field) throws Exception;
}
