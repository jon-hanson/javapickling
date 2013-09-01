package org.javapickling.common;

import org.javapickling.core.*;

import java.io.IOException;
import java.util.*;

public class ComplexClassPickler<PF> extends PicklerBase<ComplexClass, PF> {

    private Pickler<ComplexClass.Generic<ComplexClass.IdWrapper>, PF> genIdWrapperPickler() {
        return generic_p(ComplexClass.Generic.class, object_p(ComplexClass.IdWrapper.class));
    }

    private Pickler<ComplexClass.Generic<ComplexClass.Interface>, PF> genInterfacePickler() {
        return generic_p(ComplexClass.Generic.class, d_object_p());
    }

    final Field<Boolean, PF> booleanF =      field("bool",       boolean_p());
    final Field<Byte, PF>                   byteF =         field("byte",       byte_p());
    final Field<Character, PF>              charF =         field("char",       char_p());
    final Field<Short, PF>                  shortF =        field("short",      short_p());
    final Field<Long, PF>                   longF =         field("long",       long_p());
    final Field<Integer, PF>                intF =          field("int",        integer_p());
    final Field<Float, PF>                  floatF =        field("float",      float_p());
    final Field<Double, PF>                 doubleF =       field("double",     double_p());
    final Field<ComplexClass.Colour, PF>                 enumF =         field("enum",       enum_p(ComplexClass.Colour.class));
    final Field<String, PF>                 stringF =       field("string",     string_p());
    final Field<Map<String, Double>, PF>    strDblMapF =    field("strDblMap",  map_p(double_p(), TreeMap.class));
    final Field<Map<Integer, ComplexClass.Colour>, PF>   intEnumMapF =   field("intEnumMap", map_p(integer_p(), enum_p(ComplexClass.Colour.class), TreeMap.class));
    final Field<Map<Object, Object>, PF>    objObjMapF =    field("objObjMap",  map_p(d_object_p(), d_object_p(), TreeMap.class));
    final Field<Set<String>, PF>            strSetF =       field("strSet",     set_p(string_p(), TreeSet.class));
    final Field<Set<Object>, PF>            objSetF =       field("objSet",     set_p(d_object_p(), TreeSet.class));
    final Field<List<String>, PF>           listStrF =      field("listStr",    list_p(string_p(), ArrayList.class));
    final Field<List<Object>, PF>           listObjF =      null_field("listObj", list_p(d_object_p(), ArrayList.class));

    final Field<ComplexClass.Generic<ComplexClass.IdWrapper>, PF>     genericF =      field("generic",    genIdWrapperPickler());
    final Field<ComplexClass.Generic<ComplexClass.Interface>, PF>     generic2F =     field("generic2",   genInterfacePickler());

    public ComplexClassPickler(PicklerCore<PF> core) {
        super(core);
    }

    @Override
    public PF pickle(ComplexClass sc, PF target) throws IOException {
        final FieldPickler<PF> mp = core.object_map().pickler(target);
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
        mp.field(listStrF,      sc.listStrF);
        mp.field(listObjF,      sc.listObjF);
        mp.field(genericF,      sc.genericF);
        mp.field(generic2F,      sc.generic2F);
        return mp.pickle(target);
    }

    @Override
    public ComplexClass unpickle(PF source) throws IOException {
        final FieldUnpickler<PF> mu = core.object_map().unpickler(source);
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
                mu.field(listStrF),
                mu.field(listObjF),
                mu.field(genericF),
                mu.field(generic2F)
        );
    }

    public static class IdWrapperPickler<PF> extends PicklerBase<ComplexClass.IdWrapper, PF> {

        public IdWrapperPickler(PicklerCore core) {
            super(core);
        }

        @Override
        public PF pickle(ComplexClass.IdWrapper idw, PF target) throws IOException {
            return string_p().pickle(idw.id, target);
        }

        @Override
        public ComplexClass.IdWrapper unpickle(PF source) throws IOException {
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
        public PF pickle(ComplexClass.Generic<T> generic, PF target) throws IOException {
            final FieldPickler<PF> mp = core.object_map().pickler(target);
            mp.field(valueF, generic.value);
            return mp.pickle(target);
        }

        @Override
        public ComplexClass.Generic<T> unpickle(PF source) throws IOException {
            final FieldUnpickler<PF> mu = core.object_map().unpickler(source);
            return new ComplexClass.Generic<T>(mu.field(valueF));
        }
    }
}
