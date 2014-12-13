package org.javafp.javapickling.core;

/**
 * An intermediate interface though which objects composed of fields can be pickled.
 * @param <PF> the pickle format.
 */
public interface FieldPickler<PF> {

    /**
     * Pickle a field.
     * @param name Field name
     * @param value Field value
     * @param pickler Field pickler
     * @param <T> Field type
     * @throws Exception
     */
    <T> void field(String name, T value, Pickler<T, PF> pickler) throws Exception;

    /**
     * Pickle a field.
     * @param field Field name and pickler.
     * @param value Field value
     * @param <T> Field type.
     * @throws Exception
     */
    <T> void field(Field<T, PF> field, T value) throws Exception;

    /**
     * Once all field have been pickled this method will pickle the enclosing object.
     * @param pf
     * @return
     * @throws Exception
     */
    PF pickle(PF pf) throws Exception;
}
