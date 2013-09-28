package org.javapickling.core;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class FieldReflector<PF> {

    private final PicklerCore<PF> core;

    public FieldReflector(PicklerCore<PF> core) {
        this.core = core;
    }

    public <T> Pickler<T, PF> inferPickler(Field field) {
        final Type type = field.getType();
        if (type instanceof Class) {
            return inferPickler((Class)type);
        } else {
            throw new PicklerException("Unable to comprehend field type " + type);
        }
    }

    public <T> Pickler inferPickler(Class<T> clazz) {
        if (clazz.isArray()) {
            final Class<?> compClass = clazz.getComponentType();
            return this.array_p(inferPickler((compClass)), compClass);
        } else {
            final MetaType.TypeKind typeKind = MetaType.typeKindOf(clazz);

        }

        throw new PicklerException("Unable to comprehend field class " + clazz);
    }

    private Pickler array_p(final Pickler elemPickler, final Class elemClass) {
        return core.array_p(elemPickler, elemClass);
    }


}
