package org.javapickling.common;

import com.google.common.base.Optional;
import org.javapickling.core.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A pickler for the ComplexClass.
 * @param <PF>
 */
public class ComplexClassPickler<PF> extends PicklerBase<ComplexClass, PF> {

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
    private final Field<double[][], PF>             dblArrF     = field("dblArrF");
    private final Field<IdWrapper[], PF>            idWrapArrF  = field("idWrapArrF");
    private final Field<Interface[], PF>            intfArrF    = field("intfArrF");
    private final Field<Optional<IdWrapper>, PF>    optionalF   = field("optionalF");

    public ComplexClassPickler(PicklerCore<PF> core) {
        super(core, ComplexClass.class);
    }

    @Override
    public PF pickle(ComplexClass sc, PF target) throws Exception {
        final FieldPickler<PF> fp = object_map().pickler(target);
        fp.field(booleanF,      sc.booleanF);
        fp.field(byteF,         sc.byteF);
        fp.field(charF,         sc.charF);
        fp.field(shortF,        sc.shortF);
        fp.field(longF,         sc.longF);
        fp.field(intF,          sc.intF);
        fp.field(floatF,        sc.floatF);
        fp.field(doubleF,       sc.doubleF);
        fp.field(enumF,         sc.enumF);
        fp.field(stringF,       sc.stringF);
        fp.field(strDblMapF,    sc.strDblMapF);
        fp.field(intEnumMapF,   sc.intEnumMapF);
        fp.field(objObjMapF,    sc.objObjMapF);
        fp.field(strSetF,       sc.strSetF);
        fp.field(objSetF,       sc.objSetF);
        fp.field(strListF,      sc.strListF);
        fp.field(objListF,      sc.objListF);
        fp.field(genericF,      sc.genericF);
        fp.field(generic2F,     sc.generic2F);
        fp.field(strArrF,       sc.strArrF);
        fp.field(dblArrF,       sc.dblArrF);
        fp.field(idWrapArrF,    sc.idWrapArrF);
        fp.field(intfArrF,      sc.intfArrF);
        fp.field(optionalF,     sc.optionalF);
        return fp.pickle(target);
    }

    @Override
    public ComplexClass unpickle(PF source) throws Exception {
        final FieldUnpickler<PF> fu = object_map().unpickler(source);
        return new ComplexClass(
                fu.field(booleanF),
                fu.field(byteF),
                fu.field(charF),
                fu.field(shortF),
                fu.field(longF),
                fu.field(intF),
                fu.field(floatF),
                fu.field(doubleF),
                fu.field(enumF),
                fu.field(stringF),
                fu.field(strDblMapF),
                fu.field(intEnumMapF),
                fu.field(objObjMapF),
                fu.field(strSetF),
                fu.field(objSetF),
                fu.field(strListF),
                fu.field(objListF),
                fu.field(genericF),
                fu.field(generic2F),
                fu.field(strArrF),
                fu.field(dblArrF),
                fu.field(idWrapArrF),
                fu.field(intfArrF),
                fu.field(optionalF));
    }

    public static class IdWrapperPickler<PF> extends PicklerBase<IdWrapper, PF> {

        public IdWrapperPickler(PicklerCore core) {
            super(core, IdWrapper.class);
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
            super(core, Generic.class);
            valueF = field("value", valuePickler);
        }

        @Override
        public PF pickle(Generic<T> generic, PF target) throws Exception {
            final FieldPickler<PF> fp = object_map().pickler(target);
            fp.field(valueF, generic.value);
            return fp.pickle(target);
        }

        @Override
        public Generic<T> unpickle(PF source) throws Exception {
            final FieldUnpickler<PF> fu = object_map().unpickler(source);
            return new Generic<T>(fu.field(valueF));
        }
    }
}
