package org.javapickling.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.javapickling.core.*;

import java.io.Serializable;
import java.util.*;

/**
 * A (pathologically) complex class for illustration purposes.
 */
@DefaultPickler(ComplexClassPickler.class)
public class ComplexClass implements Serializable {

    public static ComplexClass createInstance(boolean first) {
        if (first) {
            final boolean booleanF = true;
            final byte byteF = 12;
            final char charF = 'A';
            final short shortF = 1234;
            final long longF = 234567890;
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
            objObjMapF.put(Colour.RED, "OrDead");
            objObjMapF.put(Colour.GREEN, createInstance(false));

            final Set<String> strSetF = Sets.newTreeSet();
            strSetF.add("a12");
            strSetF.add("b23");
            final Set<Object> objStrF = new HashSet<Object>();
            objStrF.add(-1.23);
            objStrF.add(-1);
            objStrF.add(-1.0);
            objStrF.add(0);
            objStrF.add(0.0);
            objStrF.add(1);
            objStrF.add(1.0);
            objStrF.add(1.23);

            final List<String> strListF = Lists.newArrayList("anna", "betty");
            final List<Object> objListF = Lists.newArrayList((Object)Colour.BLUE, Colour.RED);

            final Generic<IdWrapper> genericF = new Generic<IdWrapper>(new IdWrapper("Dan"));
            final Generic<Interface> generic2F = new Generic<Interface>(new IdWrapper("John"));

            final String[] strArrF = {"Plato", "Socrates", "Aristotle"};
            final double[][] dblArrF = {{-1.1, -1.0, -0.1, 0.0, 0.1, 1.0, 1.1}, {-9.9, -9.0, -0.9, 0.0, 0.9, 9.0, 9.9}};
            final IdWrapper[] idWrapArrF = {new IdWrapper("Constantine")};
            final Interface[] intfArrF = {new IdWrapper("Augustus")};

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
                    objStrF,
                    strListF,
                    objListF,
                    genericF,
                    generic2F,
                    strArrF,
                    dblArrF,
                    idWrapArrF,
                    intfArrF);
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
            final Set<Object> objStrF = new HashSet<Object>();
            final List<String> strListF = Lists.newArrayList();
            final List<Object> objListF = null;
            final Generic<IdWrapper> genericF = new Generic<IdWrapper>(new IdWrapper("Ackroyd"));
            final Generic<Interface> generic2F = new Generic<Interface>(new IdWrapper("Belushi"));

            final String[] strArrF = {};
            final double[][] dblArrF = {};
            final IdWrapper[] idWrapArrF = {};
            final Interface[] intfArrF = {};

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
                    objStrF,
                    strListF,
                    objListF,
                    genericF,
                    generic2F,
                    strArrF,
                    dblArrF,
                    idWrapArrF,
                    intfArrF);
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

    public final List<String> strListF;
    public final List<Object> objListF;

    public final Generic<IdWrapper> genericF;
    public final Generic<Interface> generic2F;

    public final String[] strArrF;
    public final double[][] dblArrF;
    public final IdWrapper[] idWrapArrF;
    public final Interface[] intfArrF;

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
            List<String> strListF,
            List<Object> objListF,
            Generic<IdWrapper> genericF,
            Generic<Interface> generic2F,
            String[] strArrF,
            double[][] dblArrF,
            IdWrapper[] idWrapArrF,
            Interface[] intfArrF) {
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
        this.strListF = strListF;
        this.objListF = objListF;
        this.genericF = genericF;
        this.generic2F = generic2F;
        this.strArrF = strArrF;
        this.dblArrF = dblArrF;
        this.idWrapArrF = idWrapArrF;
        this.intfArrF = intfArrF;
    }

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
        if (objListF != null ? !objListF.equals(that.objListF) : that.objListF != null) return false;
        if (strListF != null ? !strListF.equals(that.strListF) : that.strListF != null) return false;
        if (objObjMapF != null ? !objObjMapF.equals(that.objObjMapF) : that.objObjMapF != null) return false;
        if (strDblMapF != null ? !strDblMapF.equals(that.strDblMapF) : that.strDblMapF != null) return false;
        if (objSetF != null ? !objSetF.equals(that.objSetF) : that.objSetF != null) return false;
        if (strSetF != null ? !strSetF.equals(that.strSetF) : that.strSetF != null) return false;
        if (stringF != null ? !stringF.equals(that.stringF) : that.stringF != null) return false;
        if (genericF != null ? !genericF.equals(that.genericF) : that.genericF != null) return false;
        if (generic2F != null ? !generic2F.equals(that.generic2F) : that.generic2F != null) return false;
        if (strArrF != null ? !Arrays.deepEquals(strArrF, that.strArrF) : that.strArrF != null) return false;
        if (dblArrF != null ? !Arrays.deepEquals(dblArrF, that.dblArrF) : that.dblArrF != null) return false;
        if (idWrapArrF != null ? !Arrays.deepEquals(idWrapArrF, that.idWrapArrF) : that.idWrapArrF != null) return false;
        if (intfArrF != null ? !Arrays.deepEquals(intfArrF, that.intfArrF) : that.intfArrF != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "ComplexClass{" +
                "\n booleanF=" + booleanF +
                ",\n byteF=" + byteF +
                ",\n charF=" + charF +
                ",\n shortF=" + shortF +
                ",\n longF=" + longF +
                ",\n intF=" + intF +
                ",\n floatF=" + floatF +
                ",\n doubleF=" + doubleF +
                ",\n enumF=" + enumF +
                ",\n stringF='" + stringF + '\'' +
                ",\n strDblMapF=" + strDblMapF +
                ",\n intEnumMapF=" + intEnumMapF +
                ",\n objObjMapF=" + objObjMapF +
                ",\n strSetF=" + strSetF +
                ",\n objSetF=" + objSetF +
                ",\n strListF=" + strListF +
                ",\n objListF=" + objListF +
                ",\n genericF=" + genericF +
                ",\n generic2F=" + generic2F +
                ",\n strArrF=" + Arrays.toString(strArrF) +
                ",\n dblArrF=" + Arrays.deepToString(dblArrF) +
                ",\n idWrapArrF=" + Arrays.toString(idWrapArrF) +
                ",\n intfArrF=" + Arrays.toString(intfArrF) +
                '}';
    }
}
