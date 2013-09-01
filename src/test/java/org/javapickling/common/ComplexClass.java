package org.javapickling.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.javapickling.core.*;

import java.io.Serializable;
import java.util.*;

@DefaultPickler(ComplexClassPickler.class)
public class ComplexClass implements Serializable {

    enum Colour {
        RED, GREEN, BLUE
    }

    interface Interface {
        boolean equals(Object rhs);
    }

    @DefaultPickler(ComplexClassPickler.IdWrapperPickler.class)
    public static class IdWrapper implements Interface, Serializable {
        public final String id;

        IdWrapper(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            final IdWrapper rhs = (IdWrapper)obj;
            return id.equals(rhs.id);
        }
    }

    @DefaultPickler(ComplexClassPickler.GenericPickler.class)
    public static class Generic<T extends Interface> implements Serializable {

        public final T value;

        public Generic(T value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            final Generic<T> rhs = (Generic<T>)obj;
            return value.equals(rhs.value);
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

            final Generic<IdWrapper> genericF = new Generic<IdWrapper>(new IdWrapper("Dan"));
            final Generic<Interface> generic2F = new Generic<Interface>(new IdWrapper("John"));

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
                    listObjF,
                    genericF,
                    generic2F
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
            final Generic<IdWrapper> genericF = new Generic<IdWrapper>(new IdWrapper("Ackroyd"));
            final Generic<Interface> generic2F = new Generic<Interface>(new IdWrapper("Belushi"));

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
                    listObjF,
                    genericF,
                    generic2F
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

    public final Generic<IdWrapper> genericF;
    public final Generic<Interface> generic2F;

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
            List<Object> listObjF,
            Generic<IdWrapper> genericF,
            Generic<Interface> generic2F) {
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
        this.genericF = genericF;
        this.generic2F = generic2F;
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
        if (genericF != null ? !genericF.equals(that.genericF) : that.genericF != null) return false;
        if (generic2F != null ? !generic2F.equals(that.generic2F) : that.generic2F != null) return false;

        return true;
    }
}
