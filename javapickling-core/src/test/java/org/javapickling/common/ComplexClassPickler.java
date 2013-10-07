package org.javapickling.common;

import org.javapickling.core.*;

import java.util.*;

/**
 * A pickler for the ComplexClass.
 * @param <PF>
 */
public class ComplexClassPickler<PF> extends PicklerBase<ComplexClass, PF> {

    // Utility method for creating fields.
    private <T> Field<T, PF> field(String name) {
        return field(ComplexClass.class, name);
    }
    private <T> Field<T, PF> field2(String name) {
        return field(ComplexClass.class, name);
    }

    // Utility method for creating nullable fields.
    private <T> Field<T, PF> null_field(String name) {
        return null_field(ComplexClass.class, name);
    }

    private final Field<Boolean, PF>                booleanF    = field("booleanF");
    private final Field<Byte, PF>                   byteF       = field("byteF");
    private final Field<Character, PF>              charF       = field("charF");
    private final Field<Short, PF>                  shortF      = field("shortF");
    private final Field<Long, PF>                   longF       = field("longF");
    private final Field<Integer, PF>                intF        = field("intF");
    private final Field<Float, PF>                  floatF      = field("floatF");
    private final Field<Double, PF>                 doubleF     = field("doubleF");
    private final Field<Colour, PF>                 enumF       = field("enumF");
    private final Field<String, PF>                 stringF     = field("stringF");
    private final Field<Map<String, Double>, PF>    strDblMapF  = field("strDblMapF");
    private final Field<Map<Integer, Colour>, PF>   intEnumMapF = field("intEnumMapF");
    private final Field<Map<Object, Object>, PF>    objObjMapF  = field("objObjMapF");
    private final Field<Set<String>, PF>            strSetF     = field("strSetF");
    private final Field<Set<Object>, PF>            objSetF     = field("objSetF");
    private final Field<List<String>, PF>           strListF    = field("strListF");
    private final Field<List<Object>, PF>           objListF    = null_field("objListF");
    private final Field<Generic<IdWrapper>, PF>     genericF    = field("genericF");
    private final Field<Generic<Interface>, PF>     generic2F   = field("generic2F");
    private final Field<String[], PF>               strArrF     = field("strArrF");
    private final Field<double[][], PF>             dblArrF     = field2("dblArrF");
    private final Field<IdWrapper[], PF>            idWrapArrF  = field("idWrapArrF");
    private final Field<Interface[], PF>            intfArrF    = field("intfArrF");

    public ComplexClassPickler(PicklerCore<PF> core) {
        super(core);
    }

    @Override
    public PF pickle(ComplexClass sc, PF target) throws Exception {
        final FieldPickler<PF> mp = object_map().pickler(target);
        mp.field(booleanF,      sc.booleanF);
        mp.field(byteF,         sc.byteF);
        mp.field(charF,         sc.charF);
        mp.field(shortF,        sc.shortF);
        mp.field(longF,         sc.longF);
        mp.field(intF,          sc.intF);
        mp.field(floatF,        sc.floatF);
        mp.field(doubleF,       sc.doubleF);
        mp.field(enumF,         sc.enumF);
        mp.field(stringF,       sc.stringF);
        mp.field(strDblMapF,    sc.strDblMapF);
        mp.field(intEnumMapF,   sc.intEnumMapF);
        mp.field(objObjMapF,    sc.objObjMapF);
        mp.field(strSetF,       sc.strSetF);
        mp.field(objSetF,       sc.objSetF);
        mp.field(strListF,      sc.strListF);
        mp.field(objListF,      sc.objListF);
        mp.field(genericF,      sc.genericF);
        mp.field(generic2F,     sc.generic2F);
        mp.field(strArrF,       sc.strArrF);
        mp.field(dblArrF,       sc.dblArrF);
        mp.field(idWrapArrF,    sc.idWrapArrF);
        mp.field(intfArrF,      sc.intfArrF);
        return mp.pickle(target);
    }

    @Override
    public ComplexClass unpickle(PF source) throws Exception {
        final FieldUnpickler<PF> mu = object_map().unpickler(source);
        return new ComplexClass(
                mu.field(booleanF),
                mu.field(byteF),
                mu.field(charF),
                mu.field(shortF),
                mu.field(longF),
                mu.field(intF),
                mu.field(floatF),
                mu.field(doubleF),
                mu.field(enumF),
                mu.field(stringF),
                mu.field(strDblMapF),
                mu.field(intEnumMapF),
                mu.field(objObjMapF),
                mu.field(strSetF),
                mu.field(objSetF),
                mu.field(strListF),
                mu.field(objListF),
                mu.field(genericF),
                mu.field(generic2F),
                mu.field(strArrF),
                mu.field(dblArrF),
                mu.field(idWrapArrF),
                mu.field(intfArrF));
    }

    public static class IdWrapperPickler<PF> extends PicklerBase<IdWrapper, PF> {

        public IdWrapperPickler(PicklerCore core) {
            super(core);
        }

        @Override
        public PF pickle(IdWrapper idw, PF target) throws Exception {
            return string_p().pickle(idw.id, target);
        }

        @Override
        public IdWrapper unpickle(PF source) throws Exception {
            return new IdWrapper(string_p().unpickle(source));
        }
    }

    public static class GenericPickler<PF, T extends Interface> extends PicklerBase<Generic<T>, PF> {

        private final Field<T, PF> valueF;

        public GenericPickler(PicklerCore<PF> core, Pickler<T, PF> valuePickler) {
            super(core);
            valueF = field("value", valuePickler);
        }

        @Override
        public PF pickle(Generic<T> generic, PF target) throws Exception {
            final FieldPickler<PF> mp = object_map().pickler(target);
            mp.field(valueF, generic.value);
            return mp.pickle(target);
        }

        @Override
        public Generic<T> unpickle(PF source) throws Exception {
            final FieldUnpickler<PF> mu = object_map().unpickler(source);
            return new Generic<T>(mu.field(valueF));
        }
    }
}
