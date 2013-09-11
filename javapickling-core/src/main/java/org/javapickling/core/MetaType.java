package org.javapickling.core;

import com.google.common.collect.Maps;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A class which describes types.
 */
public class MetaType {

    private static final String ARRAY_SUFFIX = "[]";

    /**
     * An enum for what we consider to be the base types.
     */
    public enum TypeKind {
        NULL(Object.class),
        BOOLEAN(Boolean.class),
        BYTE(Byte.class),
        CHAR(Character.class),
        CLASS(Class.class),
        DOUBLE(Double.class),
        ENUM(String.class),
        FLOAT(Float.class),
        INT(Integer.class),
        LIST(List.class),
        LONG(Long.class),
        MAP(Map.class),
        OBJECT(Object.class),
        SET(Set.class),
        SHORT(Short.class),
        STRING(String.class);

        public final Class<?> clazz;

        TypeKind(Class<?> clazz) {
            this.clazz = clazz;
        }

        public <PF> Pickler<?, PF> pickler(PicklerCore<PF> core, Class<?> clazz) {
            switch (this) {
                case NULL:      return core.null_p();
                case BOOLEAN:   return core.boolean_p();
                case BYTE:      return core.byte_p();
                case CHAR:      return core.char_p();
                case CLASS:     return core.class_p();
                case DOUBLE:    return core.double_p();
                case ENUM:
                    // TODO: Remove the use of TypeKind from the following line once we upgrade to Java 7.
                    return core.enum_p(MetaType.<TypeKind>castEnumClass(clazz));
                case FLOAT:     return core.float_p();
                case INT:       return core.integer_p();
                case LIST:      return core.list_p(core.d_object_p(), (Class<List<Object>>)clazz);
                case LONG:      return core.long_p();
                case MAP:       return core.map_p(core.d_object_p(), core.d_object_p(), (Class<Map<Object, Object>>)clazz);
                case OBJECT:    return core.object_p(clazz);
                case SET:       return core.set_p(core.d_object_p(), (Class<Set<Object>>)clazz);
                case SHORT:     return core.short_p();
                case STRING:    return core.string_p();
                default:        throw new PicklerException("Unexpected TypeKind value - " + name());
            }
        }
    }

    private static <T extends Enum<T>> Class<T> castEnumClass(Class<?> clazz) {
        return (Class<T>)clazz;
    }

    private static final Map<String, TypeKind> classTypeMap = Maps.newTreeMap();

    static {
        for (TypeKind typeKind : TypeKind.values()) {
            register(typeKind);
        }
    }

    private static void register(TypeKind typeKind) {
        classTypeMap.put(typeKind.clazz.getName(), typeKind);
    }

    public static MetaType ofObject(Object obj) {

        if (obj == null)
            return new MetaType((TypeKind.NULL));

        Class<?> clazz = obj.getClass();

        int arrayDepth = 0;
        while (clazz.isArray()) {
            ++arrayDepth;
            clazz = clazz.getComponentType();
        }

        if (clazz.isEnum()) {
            return new MetaType(TypeKind.ENUM, clazz, arrayDepth);
        } else {
            TypeKind typeKind = classTypeMap.get(clazz.getName());
            if (typeKind != null) {
                return new MetaType(typeKind, arrayDepth);
            } else {
                return new MetaType(TypeKind.OBJECT, clazz, arrayDepth);
            }
        }
    }

    public static MetaType ofName(String name) {

        int arrayDepth = 0;
        while (name.endsWith(ARRAY_SUFFIX)) {
            ++arrayDepth;
            name = name.substring(0, name.length() - 2);
        }

        final TypeKind typeKind = TypeKind.valueOf(name);
        return new MetaType(typeKind, arrayDepth);
    }

    public final TypeKind typeKind;
    public Class<?> clazz;
    public final int arrayDepth;

    public MetaType(TypeKind typeKind, Class<?> clazz, int arrayDepth) {
        this.typeKind = typeKind;
        this.clazz = clazz;
        this.arrayDepth = arrayDepth;
    }

    public MetaType(TypeKind typeKind, Class<?> clazz) {
        this(typeKind, clazz, 0);
    }

    public MetaType(TypeKind typeKind, int arrayDepth) {
        this(typeKind, null, arrayDepth);
    }

    public MetaType(TypeKind typeKind) {
        this(typeKind, null, 0);
    }

    public <PF> Pickler<Object, PF> pickler(PicklerCore<PF> core) {
        Pickler<?, PF> pickler = typeKind.pickler(core, clazz);
        Class<?> arrClazz = clazz == null ? typeKind.clazz : clazz;
        for (int i = 0; i < arrayDepth; ++i) {
            pickler = core.array_p((Pickler<Object, PF>)pickler, (Class<Object>)arrClazz);
            arrClazz = Array.newInstance(arrClazz, 0).getClass();
        }

        return (Pickler<Object, PF>)pickler;
    }

    public String name() {
        final StringBuilder sb = new StringBuilder(typeKind.name());
        for (int i = 0; i < arrayDepth; ++i) {
            sb.append(ARRAY_SUFFIX);
        }
        return sb.toString();
    }
}
