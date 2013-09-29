package org.javapickling.core;

import java.lang.reflect.Field;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * FieldReflector uses reflection to infer the Pickler for a Field (java.lang.reflect.Field).
 * @param <PF>
 */
public class FieldReflector<PF> {

    private static Type[] getInterfaceTypeParams(Class<?> clazz, Type[] interfaces) {

        for (Type interfaze : interfaces) {
            if (clazz.isAssignableFrom(interfaze.getClass())) {
                final ParameterizedType paramType = (ParameterizedType)interfaze;
                return paramType.getActualTypeArguments();
            }
        }

        throw new PicklerException("Could not find an interface matching " + clazz);
    }

    private final PicklerCore<PF> core;

    public FieldReflector(PicklerCore<PF> core) {
        this.core = core;
    }

    /**
     * Infer the Pickler for a Field (java.lang.reflect.Field).
     * @param field represents the field of a class.
     * @param <T>
     * @return
     */
    public <T> Pickler<T, PF> inferPickler(Field field) {
        return inferPickler(field.getGenericType());
    }

    /**
     * Infer the Pickler for a Type.
     * @param type
     * @param <T>
     * @return
     */
    public <T> Pickler<T, PF> inferPickler(Type type) {
        if (type instanceof Class) {
            return inferPickler((Class)type);
        } else if (type instanceof ParameterizedType) {
            return inferPickler((ParameterizedType)type);
        }

        throw new PicklerException("Unable to infer a pickler for field Type " + type);
    }

    /**
     * Infer the Pickler for a ParameterizedType - i.e. for a generic type.
     * @param type
     * @param <T>
     * @return
     */
    public <T> Pickler<T, PF> inferPickler(ParameterizedType type) {

        final Type rawType = type.getRawType();

        if (rawType instanceof Class) {
            final Class clazz = (Class)rawType;
            final MetaType.TypeKind typeKind = MetaType.typeKindOf(clazz);
            switch (typeKind) {
                case MAP: {
                    final Type[] typeParams = type.getActualTypeArguments();
                    return inferMapPickler(clazz, typeParams);
                }
                case LIST: {
                    final Type[] typeParams = type.getActualTypeArguments();
                    return inferListPickler(clazz, typeParams);
                }
                case SET: {
                    final Type[] typeParams = type.getActualTypeArguments();
                    return inferSetPickler(clazz, typeParams);
                }
            }
        }

        final Type[] typeArgs = type.getActualTypeArguments();
        final Pickler[] picklers = new Pickler[typeArgs.length];
        for (int i = 0; i < typeArgs.length; ++i) {
            picklers[i] = inferPickler(typeArgs[i]);
        }

        return generic_p((Class)type.getRawType(), picklers);
    }

    /**
     * Infer the Pickler for a type described by a Class object.
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Pickler<T, PF> inferPickler(Class<T> clazz) {

        final MetaType.TypeKind typeKind = MetaType.typeKindOf(clazz);

        switch (typeKind) {
            case NULL:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case CLASS:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
            case STRING:
                return (Pickler<T, PF>)typeKind.pickler(core, clazz);
            case MAP: {
                final Type[] typeParams = getInterfaceTypeParams(Map.class, clazz.getGenericInterfaces());
                return inferMapPickler(clazz, typeParams);
            }
            case LIST: {
                final Type[] typeParams = getInterfaceTypeParams(List.class, clazz.getGenericInterfaces());
                return inferListPickler(clazz, typeParams);
            }
            case SET: {
                final Type[] typeParams = getInterfaceTypeParams(Set.class, clazz.getGenericInterfaces());
                return inferSetPickler(clazz, typeParams);
            }
            case ENUM:
                return enum_p(clazz);
            case OBJECT: {
                final boolean isFinal = Modifier.isFinal((clazz.getModifiers()));
                if (isFinal) {
                    return core.object_p(clazz);
                } else {
                    return core.d_object_p(clazz);
                }
            }
            case ARRAY: {
                final Class<?> compClass = clazz.getComponentType();
                return this.array_p(inferPickler((compClass)), compClass);
            }
        }

        throw new PicklerException("Unable to infer a pickler for field Class " + clazz);
    }

    private <T> Pickler<T, PF> inferMapPickler(Class<?> clazz, Type[] typeParams) {
        if (typeParams.length != 2) {
            throw new PicklerException("Map interface on " + clazz + " expected to have 2 type parameters, not " + typeParams.length);
        } else if (typeParams[0].equals(String.class)) {
            return map_p(inferPickler(typeParams[1]));

        } else {
            return map_p(inferPickler(typeParams[0]), inferPickler(typeParams[1]));
        }
    }

    private <T> Pickler<T, PF> inferListPickler(Class<?> clazz, Type[] typeParams) {
        if (typeParams.length != 1) {
            throw new PicklerException("List interface on " + clazz + " expected to have 1 type parameters, not " + typeParams.length);
        } else {
            return list_p(inferPickler(typeParams[0]));
        }
    }

    private <T> Pickler<T, PF> inferSetPickler(Class<?> clazz, Type[] typeParams) {
        if (typeParams.length != 1) {
            throw new PicklerException("Set interface on " + clazz + " expected to have 1 type parameters, not " + typeParams.length);
        } else {
            return set_p(inferPickler(typeParams[0]));
        }
    }

    private Pickler array_p(final Pickler elemPickler, final Class elemClass) {
        return core.array_p(elemPickler, elemClass);
    }

    private Pickler enum_p(final Class enumClass) {
        return core.enum_p(enumClass);
    }

    private Pickler list_p(final Pickler elemPickler) {
        return core.list_p(elemPickler);
    }

    private Pickler map_p(final Pickler valuePickler) {
        return core.map_p(valuePickler);
    }

    private Pickler map_p(final Pickler keyPickler, final Pickler valuePickler) {
        return core.map_p(keyPickler, valuePickler);
    }

    private Pickler set_p(final Pickler elemPickler) {
        return core.set_p(elemPickler);
    }

    private Pickler generic_p(final Class clazz, Pickler... picklers) {
        return core.generic_p(clazz, picklers);
    }
}
