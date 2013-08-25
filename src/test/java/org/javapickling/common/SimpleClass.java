package org.javapickling.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.javapickling.core.FieldPickler;
import org.javapickling.core.FieldUnpickler;
import org.javapickling.core.PicklerBase;
import org.javapickling.core.PicklerCore;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class SimpleClass implements Serializable {

    public static class Pickler<PF> extends PicklerBase<SimpleClass, PF> {

        public Pickler(PicklerCore<PF> core) {
            super(core);
        }

        @Override
        public PF pickle(SimpleClass sc, PF target) throws IOException {
            final FieldPickler<PF> mp = core.object_map().pickler(target);
            mp.boolean_f("boolean", sc.booleanF);
            mp.byte_f("byte", sc.byteF);
            mp.char_f("char", sc.charF);
            mp.short_f("short", sc.shortF);
            mp.long_f("long", sc.longF);
            mp.integer_f("int", sc.intF);
            mp.float_f("float", sc.floatF);
            mp.double_f("double", sc.doubleF);
            mp.enum_f("enum", sc.enumF, Colour.class);
            mp.string_f("string", sc.stringF);
            mp.field("strDblMap", sc.strDblMapF, core.map_p(core.double_p()));
            mp.field("intEnumMap", sc.intEnumMapF, core.map_p(core.integer_p(), core.enum_p(Colour.class), TreeMap.class));
            mp.field("objObjMap", sc.objObjMapF, core.map_p(core.object_p(), core.object_p(), TreeMap.class));
            mp.field("strSet", sc.strSetF, core.set_p(core.string_p()));
            mp.field("strObj", sc.strObjF, core.set_p(core.object_p()));
            mp.field("listStr", sc.listStrF, core.list_p(core.string_p()));
            mp.field("listObj", sc.listObjF, core.list_p(core.object_p()));
            return mp.pickle(target);
        }

        @Override
        public SimpleClass unpickle(PF source) throws IOException {
            final FieldUnpickler<PF> mu = core.object_map().unpickler(source);
            final boolean booleanF = mu.boolean_f("boolean");
            final byte byteF = mu.byte_f("byte");
            final char charF = mu.char_f("char");
            final short shortF = mu.short_f("short");
            final long longF = mu.long_f("long");
            final int intF = mu.integer_f("int");
            final float floatF = mu.float_f("float");
            final double doubleF = mu.double_f("double");
            final Colour enumF = mu.enum_f("enum", Colour.class);
            final String stringF = mu.string_f("string");
            final Map<String, Double> strDblMapF = mu.field("strDblMap", core.map_p(core.double_p()));
                final Map<Integer, Colour> intEnumMapF = (Map<Integer, Colour>)mu.field("intEnumMap", core.map_p(core.integer_p(), core.enum_p(Colour.class), TreeMap.class));
            final Map<Object, Object> objObjMapF = (Map<Object, Object>)mu.field("objObjMap", core.map_p(core.object_p(), core.object_p(), TreeMap.class));
            final Set<String> strSetF = mu.field("strSet", core.set_p(core.string_p()));
            final Set<Object> strObjF = mu.field("strObj", core.set_p(core.object_p()));
            final List<String> listStrF = mu.field("listStr", core.list_p(core.string_p()));
            final List<Object> listObjF = mu.field("listObj", core.list_p(core.object_p()));
            return new SimpleClass(
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

    enum Colour {
        RED, GREEN, BLUE
    }

    public static SimpleClass createInstance(boolean first) {
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

            return new SimpleClass(
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
            final List<Object> listObjF = Lists.newArrayList();

            return new SimpleClass(
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
    public final Set<Object> strObjF;

    public final List<String> listStrF;
    public final List<Object> listObjF;

    public SimpleClass(
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
            Set<Object> strObjF,
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
        this.strObjF = strObjF;
        this.listStrF = listStrF;
        this.listObjF = listObjF;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleClass that = (SimpleClass) o;

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
        if (strObjF != null ? !strObjF.equals(that.strObjF) : that.strObjF != null) return false;
        if (strSetF != null ? !strSetF.equals(that.strSetF) : that.strSetF != null) return false;
        if (stringF != null ? !stringF.equals(that.stringF) : that.stringF != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (booleanF ? 1 : 0);
        result = 31 * result + (int) byteF;
        result = 31 * result + (int) charF;
        result = 31 * result + (int) shortF;
        result = 31 * result + (int) (longF ^ (longF >>> 32));
        result = 31 * result + intF;
        result = 31 * result + (floatF != +0.0f ? Float.floatToIntBits(floatF) : 0);
        temp = Double.doubleToLongBits(doubleF);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (enumF != null ? enumF.hashCode() : 0);
        result = 31 * result + (stringF != null ? stringF.hashCode() : 0);
        result = 31 * result + (strDblMapF != null ? strDblMapF.hashCode() : 0);
        result = 31 * result + (intEnumMapF != null ? intEnumMapF.hashCode() : 0);
        result = 31 * result + (objObjMapF != null ? objObjMapF.hashCode() : 0);
        result = 31 * result + (strSetF != null ? strSetF.hashCode() : 0);
        result = 31 * result + (strObjF != null ? strObjF.hashCode() : 0);
        result = 31 * result + (listStrF != null ? listStrF.hashCode() : 0);
        result = 31 * result + (listObjF != null ? listObjF.hashCode() : 0);
        return result;
    }
}
