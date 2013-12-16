package org.javapickling.core;

import com.google.common.base.Optional;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.TypeVariable;
import java.util.*;

/**
 * Base class for PicklerCore implementations.
 * @param <PF> the pickle format.
 */
public abstract class PicklerCoreBase<PF> implements PicklerCore<PF> {

    /**
     * Utility function to call Class.newInstance and rethrow exceptions.
     */
    protected static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException ex) {
            throw new PicklerException("Can not create class " + clazz.getName(), ex);
        } catch (IllegalAccessException ex) {
            throw new PicklerException("Can not create class " + clazz.getName(), ex);
        }
    }

    /**
     * AbstractFieldPickler adds a helper method to simplify calling FieldPickler.field().
     */
    protected abstract class AbstractFieldPickler implements FieldPickler<PF> {

        protected final PF target;

        public AbstractFieldPickler(PF target) {
            this.target = target;
        }

        @Override
        public <T> void field(Field<T, PF> field, T value) throws Exception {
            field(field.name, value, field.pickler);
        }
    }

    /**
     * AbstractFieldPickler adds a helper method to simplify calling FieldUnpickler.field().
     */
    protected abstract class AbstractFieldUnpickler implements FieldUnpickler<PF> {

        protected final PF source;

        public AbstractFieldUnpickler(PF source) {
            this.source = source;
        }

        @Override
        public <T> T field(Field<T, PF> field) throws Exception {
            return field(field.name, field.pickler);
        }
    }

    /**
     * A cache of picklers by class name.
     */
    protected final Map<String, Class<Pickler<?, PF>>> picklerClassRegistry = Maps.newConcurrentMap();

    /**
     * A cache of picklers by class name.
     */
    protected final Map<String, Pickler<?, PF>> picklerCache = Maps.newConcurrentMap();

    /**
     * A registry of constructors for PicklerClasses.
     */
    protected final Map<String, List<GenericPicklerCtor<?, PF>>> genericPicklerClassRegistry = Maps.newConcurrentMap();

    /**
     * A map of class names to short names.
     */
    protected final BiMap<String, String> classShortNameMap = HashBiMap.create();

    protected final FieldReflector fieldReflector = new FieldReflector(this);

    public PicklerCoreBase() {
    }

    /**
     * Initialisation has to be performed outside the constructor
     * to avoid calling virtual methods from the constructors.
     */
    protected void initialise() {
        register(Boolean.class, boolean_p());
        register(Byte.class, byte_p());
        register(Character.class, char_p());
        register(String.class, string_p());
        register(Integer.class, integer_p());
        register(Short.class, short_p());
        register(Long.class, long_p());
        register(Float.class, float_p());
        register(Double.class, double_p());

        register(Class.class, class_p());

        register(boolean.class, boolean_p());
        register(byte.class, byte_p());
        register(char.class, char_p());
        register(int.class, integer_p());
        register(short.class, short_p());
        register(long.class, long_p());
        register(float.class, float_p());
        register(double.class, double_p());
    }

    /**
     * Register a Pickler for the specified value Class type.
     * @param valueClass value class.
     * @param pickler the pickler.
     * @param <T> value type.
     */
    protected <T, S extends T> void register(Class<S> valueClass, Pickler<T, PF> pickler) {
        picklerCache.put(valueClass.getName(), pickler);
    }

    /**
     * Register a Pickler by class, for the specified value Class type.
     * @param valueClass value class.
     * @param picklerClass class for the Pickler.
     * @param <T> value type.
     * @param <P> pickler type.
     */
    public <T, P extends Pickler<T, PF>> void register(Class<T> valueClass, Class<P> picklerClass) {
        register(valueClass, picklerClass, false);
    }

    /**
     * Register a Pickler by class, for the specified value Class type.
     * @param valueClass value class.
     * @param picklerClass class for the Pickler.
     * @param <T> value type.
     * @param <P> pickler type.
     * @param lazy create pickler instance from class on demand.
     */
    public <T, P extends Pickler<T, PF>> void register(Class<T> valueClass, Class<P> picklerClass, boolean lazy) {

        final TypeVariable<Class<P>>[] tps = picklerClass.getTypeParameters();
        if (tps.length > 0) {
            if (!tps[0].getName().equals("PF") || tps.length > 1) {
                registerGeneric(valueClass, picklerClass);
            }
        }

        registerPicklerClass(valueClass, picklerClass);

        if (!lazy) {
            getPickler(valueClass);
        }
    }

    public void registerClassShortName(Class<?> clazz, String shortName) {
        final String clazzName = clazz.getName();
        if (classShortNameMap.containsValue(shortName)) {
            final String existClazzName = classShortNameMap.inverse().get(shortName);
            if (!clazzName.equals(existClazzName)) {
                throw new PicklerException("Can not register short name '" + shortName + "' for " + clazz.getName() +
                        " as it has already been registered to " + clazzName);
            }
        } else {
            classShortNameMap.put(clazz.getName(), shortName);
        }
    }

    public void registerClassShortName(Class<?> clazz) {
        registerClassShortName(clazz, clazz.getSimpleName());
    }

    public String classToName(Class<?> clazz) {

        final String clazzName = clazz.getName();

        String name = classShortNameMap.get(clazzName);
        if (name != null) {
            return name;
        } else {
            return clazzName;
        }
    }

    public Class<?> nameToClass(String name) throws ClassNotFoundException {

        String clazzName = classShortNameMap.inverse().get(name);
        if (clazzName == null) {
            clazzName = name;
        }

        return Class.forName(clazzName);
    }

    private <P> void registerPicklerClass(Class<?> valueClass, Class<P> picklerClass) {
        picklerClassRegistry.put(valueClass.getName(), this.<Pickler<?, PF>>castPicklerClass(picklerClass));
    }

    private <T> Class<Pickler<T, PF>> getPicklerClass(Class<T> valueClass) {
        return castPicklerClass(picklerClassRegistry.get(valueClass.getName()));
    }

    private <T> Class<T> castPicklerClass(Class<?> picklerClass) {
        return (Class<T>)picklerClass;
    }

    interface GenericPicklerCtor<T, PF> {
        int picklerCount();
        Pickler<T, PF> create(Pickler<?, PF>[] picklerArgs);
    }

    protected <T, P extends Pickler<T, PF>> void registerGeneric(final Class<T> valueClass, final Class<P> picklerClass) {

        final TypeVariable<Class<T>>[] valueTps = valueClass.getTypeParameters();
        final TypeVariable<Class<P>>[] picklerTps = picklerClass.getTypeParameters();

        // We expect the first type parameter to be PF unless the Pickler is specialised for a specific format.
        final int picklerCount;
        if (picklerTps[0].getName().equals("PF")) {
            picklerCount = picklerTps.length - 1;
        } else {
            picklerCount = picklerTps.length;
        }

        if (valueTps.length < picklerCount) {
            throw new PicklerException("Value class " + valueClass.getName() +
                    "expected to have " + picklerCount + " type parameters");
        }

        final List<GenericPicklerCtor<?, PF>> genPicklerCtors = Lists.newArrayList();
        final Constructor<?>[] ctors = picklerClass.getConstructors();
        for (final Constructor<?> ctor : ctors) {
            // The first pickler ctor args is always PicklerCore.
            final int ctorPicklerCount = ctor.getParameterTypes().length - 1;

            // Register a generic pickler constructor.
            genPicklerCtors.add(new GenericPicklerCtor<T, PF>() {
                @Override public int picklerCount() {
                    return ctorPicklerCount;
                }

                @Override
                public Pickler<T, PF> create(Pickler<?, PF>[] picklerArgs) {
                    if (picklerArgs.length != picklerCount) {
                        throw new PicklerException("The constructor for " + picklerClass.getName() +
                                " expects " + picklerCount + " pickler arguments");
                    }

                    return createPickler(picklerClass.getName(), ctor, picklerArgs);
                }
            });

            // Register a generic pickler which pickles its arguments dynamically.
            final Object[] args = new Object[ctorPicklerCount];
            for (int i = 0; i < ctorPicklerCount; ++i) {
                args[i] = d_object_p();
            }

            register(valueClass, (Pickler<T,PF>)createPickler(picklerClass.getName(), ctor, args));
        }

        genericPicklerClassRegistry.put(valueClass.getName(), genPicklerCtors);
    }

    private <T> Pickler<T, PF> createPickler(String name, Constructor ctor, Object[] args) {

        final int ctorParamCount = ctor.getParameterTypes().length;
        final Class<?>[] ctorParamTypes = ctor.getParameterTypes();

        final Object[] ctorArgs = new Object[ctorParamCount];
        ctorArgs[0] = ctorParamTypes[0].cast(this);
        for (int i = 1; i < ctorParamCount; ++i) {
            ctorArgs[i] = ctorParamTypes[i].cast(args[i - 1]);
        }

        try {
            return (Pickler<T, PF>)ctor.newInstance(ctorArgs);
        } catch (InvocationTargetException ex) {
            throw new PicklerException("Failed to call constructor for Pickler class " + name, ex);
        } catch (InstantiationException ex) {
            throw new PicklerException("Failed to call constructor for Pickler class " + name, ex);
        } catch (IllegalAccessException ex) {
            throw new PicklerException("Failed to call constructor for Pickler class " + name, ex);
        }
    }

    protected <T, P extends Pickler<T, PF>> P getPickler(Class<T> valueClass) {

        final String key = valueClass.getName();

        P pickler = (P)picklerCache.get(key);
        if (pickler != null)
            return pickler;

        Class<P> picklerClass = (Class<P>)getPicklerClass(valueClass);
        if (picklerClass == null) {
            if (valueClass.isAnnotationPresent(DefaultPickler.class)) {
                final DefaultPickler defPickAnn = valueClass.getAnnotation(DefaultPickler.class);
                picklerClass = (Class<P>)defPickAnn.value();
                register(valueClass, picklerClass, true);
            } else {
                final Class<?> superClass = valueClass.getSuperclass();
                if (superClass != null) {
                    return (P)getPickler(superClass);
                } else {
                    throw new PicklerException("No Pickler class registered for " + valueClass.getName());
                }
            }
        }

        try {
            // Acquire a picklerClass constructor which takes a PicklerCore and a class.
            final Constructor<P> ctor = picklerClass.getConstructor(PicklerCore.class);

            // Invoke the constructor to get a new instance of the Pickler.
            pickler = ctor.newInstance(this);

            // Register the Pickler.
            register(valueClass, pickler);

            return pickler;
        } catch (NoSuchMethodException ex) {
            throw new PicklerException(
                    "Pickler class " + picklerClass.getName() +
                            " must have a public constructor which takes a PicklerCore and a class", ex);
        } catch (InvocationTargetException ex) {
            throw new PicklerException("Failed to call constructor for Pickler class " + picklerClass.getName(), ex);
        } catch (InstantiationException ex) {
            throw new PicklerException("Failed to call constructor for Pickler class " + picklerClass.getName(), ex);
        } catch (IllegalAccessException ex) {
            throw new PicklerException("Failed to call constructor for Pickler class " + picklerClass.getName(), ex);
        }
    }

    protected <T, P extends Pickler<T, PF>> Pickler<T, PF> getGenericPickler(final Class<T> valueClass, Pickler<?, PF>... picklers) {

        List<GenericPicklerCtor<?, PF>> picklerCtors = genericPicklerClassRegistry.get(valueClass.getName());
        if (picklerCtors == null) {
            if (valueClass.isAnnotationPresent(DefaultPickler.class)) {
                final DefaultPickler defPickAnn = valueClass.getAnnotation(DefaultPickler.class);
                final Class<P> picklerClass = (Class<P>)defPickAnn.value();
                register(valueClass, (Class<Pickler<T,PF>>) picklerClass, true);
                picklerCtors = genericPicklerClassRegistry.get(valueClass.getName());
            }
        }

        if (picklerCtors == null) {
            throw new PicklerException("No Generic Pickler registered for " + valueClass.getName());
        }

        for (GenericPicklerCtor<?, PF> picklerCtor : picklerCtors) {
            if (picklerCtor.picklerCount() == picklers.length) {
                return (P)picklerCtor.create(picklers);
            }
        }

        throw new PicklerException("No Generic Pickler found for " + valueClass.getName() + " which accepts " + picklers.length + " picklers");
    }

    @Override
    public <T> Pickler<T, PF> object_p(Class<T> clazz) {
        return getPickler(clazz);
    }

    @Override
    public <T> Pickler<Class<T>, PF> class_p() {

        return new Pickler<Class<T>, PF>() {

            @Override
            public PF pickle(Class<T> clazz, PF target) throws Exception {
                return string_p().pickle(classToName(clazz), target);
            }

            @Override
            public Class<T> unpickle(PF source) throws Exception {
                final String name = string_p().unpickle(source);
                try {
                    return (Class<T>)nameToClass(name);
                } catch (ClassNotFoundException ex) {
                    throw new IOException("Can not create class from name '" + name + "'", ex);
                }
            }
        };
    }

    @Override
    public <T> Pickler<Set<T>, PF> set_p(final Pickler<T, PF> elemPickler) {
        return set_p(elemPickler, HashSet.class);
    }

    @Override
    public <T> Pickler<List<T>, PF> list_p(final Pickler<T, PF> elemPickler) {
        return list_p(elemPickler, ArrayList.class);
    }

    @Override
    public <T> Pickler<Map<String, T>, PF> map_p(final Pickler<T, PF> valuePickler) {
        return map_p(valuePickler, TreeMap.class);
    }

    @Override
    public <K, V> Pickler<Map<K, V>, PF> map_p(
            final Pickler<K, PF> keyPickler,
            final Pickler<V, PF> valuePickler) {
        return map_p(keyPickler, valuePickler, HashMap.class);
    }

    @Override
    public <T, S extends T> Pickler<S, PF> generic_p(final Class<T> clazz, Pickler<?, PF>... picklers) {
        return (Pickler<S, PF>)getGenericPickler(clazz, picklers);
    }

    @Override
    public Pickler<Object, PF> d_object_p() {
        // Create this object on the fly to avoid construction-order dependency issues.
        return new DynamicObjectPickler<Object, PF>(this, Object.class);
    }

    @Override
    public <T, S extends T> Pickler<S, PF> d_object_p(Class<T> clazz) {
        return new DynamicObjectPickler<S, PF>(this, clazz);
    }

    @Override
    public <T> Field<T, PF> field(String name, Pickler<T, PF> pickler) {
        return new Field<T, PF>(name, pickler);
    }

    @Override
    public <T> Field<T, PF> field(Class<?> clazz, String name) {
        final java.lang.reflect.Field field;
        try {
            field = clazz.getField(name);
        } catch (NoSuchFieldException ex) {
            throw new PicklerException(clazz.getName() + " has no field called " + name);
        }
        return new Field<T, PF>(field.getName(), fieldReflector.inferPickler(field));
    }

    @Override
    public <T> Field<T, PF> null_field(String name, Pickler<T, PF> pickler) {
        return new Field<T, PF>(name, nullable(pickler));
    }

    @Override
    public <T> Field<T, PF> null_field(Class<?> clazz, String name) {
        final java.lang.reflect.Field field;
        try {
            field = clazz.getField(name);
        } catch (NoSuchFieldException ex) {
            throw new PicklerException(clazz.getName() + " has no field called " + name);
        }
        return new Field<T, PF>(field.getName(), nullable(fieldReflector.inferPickler(field)));
    }
}
