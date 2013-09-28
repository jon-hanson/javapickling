package org.javapickling.core;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.TypeVariable;
import java.util.Map;

/**
 * Base class for PicklerCore implementations.
 * @param <PF>
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
    protected final Map<String, Class<Pickler<?, PF>>> picklerClassRegistry = Maps.newTreeMap();

    /**
     * A cache of picklers by class name.
     */
    protected final Map<String, Pickler<?, PF>> picklerCache = Maps.newTreeMap();

    /**
     * A registry of constructors for PicklerClasses.
     */
    protected final Map<String, GenericPicklerCtor<?, PF>> genericPicklerClassRegistry = Maps.newTreeMap();

    public PicklerCoreBase() {
    }

    public void initialise() {
        register(Boolean.class, boolean_p());
        register(Byte.class, byte_p());
        register(Character.class, char_p());
        register(String.class, string_p());
        register(Integer.class, integer_p());
        register(Short.class, short_p());
        register(Long.class, long_p());
        register(Float.class, float_p());
        register(Double.class, double_p());
        register(Class.class, this.class_p());
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

    private <P> void registerPicklerClass(Class<?> valueClass, Class<P> picklerClass) {
        picklerClassRegistry.put(valueClass.getName(), this.<P, Pickler<?, PF>>castPicklerClass(picklerClass));
    }

    private <T> Class<Pickler<T, PF>> getPicklerClass(Class<T> valueClass) {
        return castPicklerClass(picklerClassRegistry.get(valueClass.getName()));
    }

    private <S, T> Class<T> castPicklerClass(Class<S> picklerClass) {
        return (Class<T>)picklerClass;
    }

    interface GenericPicklerCtor<T, PF> {
        Pickler<T, PF> create(Pickler<?, PF>[] picklerArgs);
    }

    protected <T, P extends Pickler<T, PF>> void registerGeneric(final Class<T> valueClass, final Class<P> picklerClass) {

        final TypeVariable<Class<T>>[] valueTps = valueClass.getTypeParameters();
        final TypeVariable<Class<P>>[] picklerTps = picklerClass.getTypeParameters();

        final int picklerCount;
        final boolean firstParamIsPF = picklerTps[0].getName().equals("PF");
        if (firstParamIsPF) {
            picklerCount = picklerTps.length - 1;
        } else {
            picklerCount = picklerTps.length;
        }

        if (valueTps.length < picklerCount) {
            throw new PicklerException("Value class " + valueClass.getName() +
                    "expected to have " + picklerCount + " type parameters");
        }

        final Constructor<?>[] ctors = picklerClass.getConstructors();
        for (final Constructor<?> ctor : ctors) {
            if (ctor.getParameterTypes().length == picklerCount + 1) {
                // Register a generic pickler constructor.
                genericPicklerClassRegistry.put(valueClass.getName(), new GenericPicklerCtor<T, PF>() {

                    @Override
                    public Pickler<T, PF> create(Pickler<?, PF>[] picklerArgs) {
                        if (picklerArgs.length != picklerCount) {
                            throw new PicklerException("The constructor for " + picklerClass.getName() + " expects " + picklerCount + " pickler arguments");
                        }

                        return createPickler(picklerClass.getName(), ctor, picklerArgs);
                    }
                });

                // Register a generic pickler which pickles it arguments dynamically.
                final Object[] args = new Object[picklerCount];
                for (int i = 0; i < picklerCount; ++i) {
                    args[i] = d_object_p();
                }

                register(valueClass, (Pickler<T,PF>)createPickler(picklerClass.getName(), ctor, args));

                break;
            }
        }
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
                throw new PicklerException("No Pickler class registered for " + valueClass.getName());
            }
        }

        try {
            // Acquire a picklerClass constructor which takes a PicklerCore.
            final Constructor<P> ctor = picklerClass.getConstructor(PicklerCore.class);

            // Invoke the constructor to get a new instance of the Pickler.
            pickler = ctor.newInstance(this);

            // Register the Pickler.
            register(valueClass, pickler);

            return pickler;
        } catch (NoSuchMethodException ex) {
            throw new PicklerException(
                    "Pickler class " + picklerClass.getName() +
                            " must have a public constructor which takes a PicklerCore", ex);
        } catch (InvocationTargetException ex) {
            throw new PicklerException("Failed to call constructor for Pickler class " + picklerClass.getName(), ex);
        } catch (InstantiationException ex) {
            throw new PicklerException("Failed to call constructor for Pickler class " + picklerClass.getName(), ex);
        } catch (IllegalAccessException ex) {
            throw new PicklerException("Failed to call constructor for Pickler class " + picklerClass.getName(), ex);
        }
    }

    protected <T, P extends Pickler<T, PF>> Pickler<T, PF> getGenericPickler(final Class<T> valueClass, Pickler<?, PF>... picklers) {

        GenericPicklerCtor<?, PF> picklerCtor = genericPicklerClassRegistry.get(valueClass.getName());
        if (picklerCtor == null) {
            if (valueClass.isAnnotationPresent(DefaultPickler.class)) {
                final DefaultPickler defPickAnn = valueClass.getAnnotation(DefaultPickler.class);
                final Class<P> picklerClass = (Class<P>)defPickAnn.value();
                register(valueClass, (Class<Pickler<T,PF>>) picklerClass, true);
                picklerCtor = genericPicklerClassRegistry.get(valueClass.getName());
            }
        }

        if (picklerCtor == null) {
            throw new PicklerException("No Generic Pickler registered for " + valueClass.getName());
        }

        return (P)picklerCtor.create(picklers);
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
                return string_p().pickle(clazz.getName(), target);
            }

            @Override
            public Class<T> unpickle(PF source) throws Exception {
                final String name = string_p().unpickle(source);
                try {
                    return (Class<T>)Class.forName(name);
                } catch (ClassNotFoundException ex) {
                    throw new IOException("Can not create class from name '" + name + "'", ex);
                }
            }
        };
    }

    @Override
    public <T, S extends T> Pickler<Class<S>, PF> class_p(Class<T> clazz) {

        return new Pickler<Class<S>, PF>() {

            @Override
            public PF pickle(Class<S> clazz, PF target) throws Exception {
                return string_p().pickle(clazz.getName(), target);
            }

            @Override
            public Class<S> unpickle(PF source) throws Exception {
                final String name = string_p().unpickle(source);
                try {
                    return (Class<S>)Class.forName(name);
                } catch (ClassNotFoundException ex) {
                    throw new IOException("Can not create class from name '" + name + "'", ex);
                }
            }
        };
    }

    @Override
    public <T, S extends T> Pickler<S, PF> generic_p(final Class<T> clazz, Pickler<?, PF>... picklers) {
        return (Pickler<S, PF>)getGenericPickler(clazz, picklers);
    }

    @Override
    public Pickler<Object, PF> d_object_p() {
        // Create this object on the fly to avoid construction order dependency issues.
        return new DynamicObjectPickler<PF, Object>(this);
    }

    @Override
    public <T, S extends T> Pickler<S, PF> d_object_p(Class<T> clazz) {
        return new DynamicObjectPickler<PF, S>(this);
    }

    @Override
    public <T> Field<T, PF> field(String name, Pickler<T, PF> pickler) {
        return new Field<T, PF>(name, pickler);
    }

    @Override
    public <T> Field<T, PF> null_field(String name, Pickler<T, PF> pickler) {
        return new Field<T, PF>(name, nullable(pickler));
    }

    public <T> Pickler<T, PF> nullable(final Pickler<T, PF> pickler) {

        return new Pickler<T, PF>() {

            @Override
            public PF pickle(T t, PF target) throws Exception {
                final PF target2 = boolean_p().pickle(t != null, target);
                if (t != null) {
                    return pickler.pickle(t, target2);
                } else {
                    return target2;
                }
            }

            @Override
            public T unpickle(PF source) throws Exception {
                if (boolean_p().unpickle(source)) {
                    return pickler.unpickle(source);
                } else {
                    return null;
                }
            }
        };
    }
}
