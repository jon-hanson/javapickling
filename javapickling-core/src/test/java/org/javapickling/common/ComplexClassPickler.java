package org.javapickling.common;

import org.javapickling.core.*;

import java.util.*;

public class ComplexClassPickler<PF> extends PicklerBase<ComplexClass, PF> {

    private <T> Field<T, PF> field(String name) {
        return field(ComplexClass.class, name);
    }

    private <T> Field<T, PF> null_field(String name) {
        return null_field(ComplexClass.class, name);
    }

    final Field<Boolean, PF>                booleanF =      field("booleanF");
    final Field<Byte, PF>                   byteF =         field("byteF");
    final Field<Character, PF>              charF =         field("charF");
    final Field<Short, PF>                  shortF =        field("shortF");
    final Field<Long, PF>                   longF =         field("longF");
    final Field<Integer, PF>                intF =          field("intF");
    final Field<Float, PF>                  floatF =        field("floatF");
    final Field<Double, PF>                 doubleF =       field("doubleF");
    final Field<ComplexClass.Colour, PF>    enumF =         field("enumF");
    final Field<String, PF>                 stringF =       field("stringF");
    final Field<Map<String, Double>, PF>    strDblMapF =    field("strDblMapF");
    final Field<Map<Integer, ComplexClass.Colour>, PF>   intEnumMapF =   field("intEnumMapF");
    final Field<Map<Object, Object>, PF>    objObjMapF =    field("objObjMapF");
    final Field<Set<String>, PF>            strSetF =       field("strSetF");
    final Field<Set<Object>, PF>            objSetF =       field("objSetF");
    final Field<List<String>, PF>           strListF =      field("strListF");
    final Field<List<Object>, PF>           objListF =      null_field("objListF");

    final Field<ComplexClass.Generic<ComplexClass.IdWrapper>, PF>     genericF =      field("genericF");
    final Field<ComplexClass.Generic<ComplexClass.Interface>, PF>     generic2F =     field("generic2F");

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
        mp.field(generic2F,      sc.generic2F);
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
                mu.field(generic2F)
        );
    }

    public static class IdWrapperPickler<PF> extends PicklerBase<ComplexClass.IdWrapper, PF> {

        public IdWrapperPickler(PicklerCore core) {
            super(core);
        }

        @Override
        public PF pickle(ComplexClass.IdWrapper idw, PF target) throws Exception {
            return string_p().pickle(idw.id, target);
        }

        @Override
        public ComplexClass.IdWrapper unpickle(PF source) throws Exception {
            return new ComplexClass.IdWrapper(string_p().unpickle(source));
        }
    }

    public static class GenericPickler<PF, T extends ComplexClass.Interface> extends PicklerBase<ComplexClass.Generic<T>, PF> {

        private final Field<T, PF> valueF;

        public GenericPickler(PicklerCore<PF> core, Pickler<T, PF> valuePickler) {
            super(core);
            valueF = field("value", valuePickler);
        }

        @Override
        public PF pickle(ComplexClass.Generic<T> generic, PF target) throws Exception {
            final FieldPickler<PF> mp = core.object_map().pickler(target);
            mp.field(valueF, generic.value);
            return mp.pickle(target);
        }

        @Override
        public ComplexClass.Generic<T> unpickle(PF source) throws Exception {
            final FieldUnpickler<PF> mu = core.object_map().unpickler(source);
            return new ComplexClass.Generic<T>(mu.field(valueF));
        }
    }
}
