package org.javapickling.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.javapickling.core.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@DefaultPickler(pickler=ComplexClass.Pickler.class)
public class ComplexClass implements Serializable {

    enum Colour {
        RED, GREEN, BLUE
    }

    public static class Pickler<PF> extends PicklerBase<ComplexClass, PF> {

        final Field<Boolean, PF>                booleanF =      field("bool",       boolean_p());
        final Field<Byte, PF>                   byteF =         field("byte",       byte_p());
        final Field<Character, PF>              charF =         field("char",       char_p());
        final Field<Short, PF>                  shortF =        field("short",      short_p());
        final Field<Long, PF>                   longF =         field("long",       long_p());
        final Field<Integer, PF>                intF =          field("int",        integer_p());
        final Field<Float, PF>                  floatF =        field("float",      float_p());
        final Field<Double, PF>                 doubleF =       field("double",     double_p());
        final Field<Colour, PF>                 enumF =         field("enum",       enum_p(Colour.class));
        final Field<String, PF>                 stringF =       field("string",     string_p());
        final Field<Map<String, Double>, PF>    strDblMapF =    field("strDblMap",  map_p(double_p(), TreeMap.class));
        final Field<Map<Integer, Colour>, PF>   intEnumMapF =   field("intEnumMap", map_p(integer_p(), enum_p(Colour.class), TreeMap.class));
        final Field<Map<Object, Object>, PF>    objObjMapF =    field("objObjMap",  map_p(d_object_p(), d_object_p(), TreeMap.class));
        final Field<Set<String>, PF>            strSetF =       field("strSet",     set_p(string_p(), TreeSet.class));
        final Field<Set<Object>, PF>            objSetF =       field("objSet",     set_p(d_object_p(), TreeSet.class));
        final Field<List<String>, PF>           listStrF =      field("listStr",    list_p(string_p(), ArrayList.class));
        final Field<List<Object>, PF>           listObjF =      null_field("listObj", list_p(d_object_p(), ArrayList.class));

        public Pickler(PicklerCore<PF> core) {
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
                    mu.field(listObjF)
            );
        }
    }

    public static ComplexClass createInstance(boolean first) {
        if (first) {
            final boolean booleanF = true;
            final byte byteF = 12;
            final char charF = 'A';
            final short shortF = 1234;
            final long longF = 23456789;
            final int intF = 456789;
            final float floatF = 123.456f;
            final double doubleF = Math.PI;
            final Colour enumF = Colour.RED;
            final String stringF = "first";

            final Map<String, Double> strDblMapF = Maps.newTreeMap();
            strDblMapF.put("Abc", 12.34);
            strDblMapF.put("Bcd", 34.56);
            final Map<Integer, Colour> intEnumMapF = Maps.newTreeMap();
            intEnumMapF.put(12, Colour.GREEN);
            intEnumMapF.put(45, Colour.BLUE);
            final Map<Object, Object> objObjMapF = new TreeMap<Object, Object>();
            objObjMapF.put(Colour.RED, "RED");
            objObjMapF.put(Colour.GREEN, createInstance(false));

            final Set<String> strSetF = Sets.newTreeSet();
            strSetF.add("a12");
            strSetF.add("b23");
            final Set<Object> strObjF = new TreeSet<Object>();
            strObjF.add(33);
            strObjF.add(44);

            final List<String> listStrF = Lists.newArrayList("anna", "betty");
            final List<Object> listObjF = Lists.newArrayList((Object)Colour.BLUE, Colour.RED);

            return new ComplexClass(
                    booleanF,
                    byteF,
                    charF,
                    shortF,
                    longF,
                    intF,
                    floatF,
                    doubleF,
                    enumF,
                    stringF,
                    strDblMapF,
                    intEnumMapF,
                    objObjMapF,
                    strSetF,
                    strObjF,
                    listStrF,
                    listObjF
            );
        } else {
            final boolean booleanF = true;
            final byte byteF = 56;
            final char charF = 'Z';
            final short shortF = -1234;
            final long longF = -23456789;
            final int intF = -456789;
            final float floatF = -123.456f;
            final double doubleF = -Math.PI;
            final Colour enumF = Colour.GREEN;
            final String stringF = "second";

            final Map<String, Double> strDblMapF = Maps.newTreeMap();
            final Map<Integer, Colour> intEnumMapF = Maps.newTreeMap();
            final Map<Object, Object> objObjMapF = new TreeMap<Object, Object>();
            final Set<String> strSetF = Sets.newTreeSet();
            final Set<Object> strObjF = new TreeSet<Object>();
            final List<String> listStrF = Lists.newArrayList();
            final List<Object> listObjF = null;

            return new ComplexClass(
                    booleanF,
                    byteF,
                    charF,
                    shortF,
                    longF,
                    intF,
                    floatF,
                    doubleF,
                    enumF,
                    stringF,
                    strDblMapF,
                    intEnumMapF,
                    objObjMapF,
                    strSetF,
                    strObjF,
                    listStrF,
                    listObjF
            );
        }
    }

    public final boolean booleanF;
    public final byte byteF;
    public final char charF;
    public final short shortF;
    public final long longF;
    public final int intF;
    public final float floatF;
    public final double doubleF;
    public final Colour enumF;
    public final String stringF;

    public final Map<String, Double> strDblMapF;
    public final Map<Integer, Colour> intEnumMapF;
    public final Map<Object, Object> objObjMapF;

    public final Set<String> strSetF;
    public final Set<Object> objSetF;

    public final List<String> listStrF;
    public final List<Object> listObjF;

    public ComplexClass(
            boolean booleanF,
            byte byteF,
            char charF,
            short shortF,
            long longF,
            int intF,
            float floatF,
            double doubleF,
            Colour enumF,
            String stringF,
            Map<String, Double> strDblMapF,
            Map<Integer, Colour> intEnumMapF,
            Map<Object, Object> objObjMapF,
            Set<String> strSetF,
            Set<Object> objSetF,
            List<String> listStrF,
            List<Object> listObjF) {
        this.booleanF = booleanF;
        this.byteF = byteF;
        this.charF = charF;
        this.shortF = shortF;
        this.longF = longF;
        this.intF = intF;
        this.floatF = floatF;
        this.doubleF = doubleF;
        this.enumF = enumF;
        this.stringF = stringF;
        this.strDblMapF = strDblMapF;
        this.intEnumMapF = intEnumMapF;
        this.objObjMapF = objObjMapF;
        this.strSetF = strSetF;
        this.objSetF = objSetF;
        this.listStrF = listStrF;
        this.listObjF = listObjF;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComplexClass that = (ComplexClass) o;

        if (booleanF != that.booleanF) return false;
        if (byteF != that.byteF) return false;
        if (charF != that.charF) return false;
        if (Double.compare(that.doubleF, doubleF) != 0) return false;
        if (Float.compare(that.floatF, floatF) != 0) return false;
        if (intF != that.intF) return false;
        if (longF != that.longF) return false;
        if (shortF != that.shortF) return false;
        if (enumF != that.enumF) return false;
        if (intEnumMapF != null ? !intEnumMapF.equals(that.intEnumMapF) : that.intEnumMapF != null) return false;
        if (listObjF != null ? !listObjF.equals(that.listObjF) : that.listObjF != null) return false;
        if (listStrF != null ? !listStrF.equals(that.listStrF) : that.listStrF != null) return false;
        if (objObjMapF != null ? !objObjMapF.equals(that.objObjMapF) : that.objObjMapF != null) return false;
        if (strDblMapF != null ? !strDblMapF.equals(that.strDblMapF) : that.strDblMapF != null) return false;
        if (objSetF != null ? !objSetF.equals(that.objSetF) : that.objSetF != null) return false;
        if (strSetF != null ? !strSetF.equals(that.strSetF) : that.strSetF != null) return false;
        if (stringF != null ? !stringF.equals(that.stringF) : that.stringF != null) return false;

        return true;
    }
}
