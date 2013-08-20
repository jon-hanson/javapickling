package org.javapickling.core;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Base class for PicklerCore implementations.
 * @param <PF>
 */
public abstract class PicklerCoreBase<PF> implements PicklerCore<PF> {

    /**
     * FieldPicklerBase adds helper methods to simplify calling FieldPickler.field().
     */
    protected abstract class FieldPicklerBase implements FieldPickler<PF> {

        @Override
        public void bool(String name, Boolean value) throws IOException {
            field(name, value, PicklerCoreBase.this.bool());
        }

        @Override
        public void chr(String name, Character value) throws IOException {
            field(name, value, PicklerCoreBase.this.chr());
        }

        @Override
        public void str(String name, String value) throws IOException {
            field(name, value, PicklerCoreBase.this.str());
        }

        @Override
        public void integer(String name, Integer value) throws IOException {
            field(name, value, PicklerCoreBase.this.integer());
        }

        @Override
        public void lng(String name, Long value) throws IOException {
            field(name, value, PicklerCoreBase.this.lng());
        }

        @Override
        public void dbl(String name, Double value) throws IOException {
            field(name, value, PicklerCoreBase.this.dbl());
        }
    }

    /**
     * FieldUnpicklerBase adds helper methods to simplify calling FieldUnpickler.field().
     */
    protected abstract class FieldUnpicklerBase implements FieldUnpickler<PF> {

        @Override
        public Boolean bool(String name, PF pf) throws IOException {
            return field(name, pf, PicklerCoreBase.this.bool());
        }

        @Override
        public Character chr(String name, PF pf) throws IOException {
            return field(name, pf, PicklerCoreBase.this.chr());
        }

        @Override
        public String str(String name, PF pf) throws IOException {
            return field(name, pf, PicklerCoreBase.this.str());
        }

        @Override
        public Integer integer(String name, PF pf) throws IOException {
            return field(name, pf, PicklerCoreBase.this.integer());
        }

        @Override
        public Long lng(String name, PF pf) throws IOException {
            return field(name, pf, PicklerCoreBase.this.lng());
        }

        @Override
        public Double dbl(String name, PF pf) throws IOException {
            return field(name, pf, PicklerCoreBase.this.dbl());
        }
    }

    /**
     * A registry of picklers by class name.
     */
    private final Map<String, Pickler<?, PF>> picklerRegistry = Maps.newTreeMap();

    public PicklerCoreBase() {
        register(Boolean.class, bool());
        register(Integer.class, integer());
        register(Double.class, dbl());
        register(Long.class, lng());
        register(String.class, str());
    }

    /**
     * Register a Pickler for the specified value Class type.
     * @param valueClass value class.
     * @param pickler the pickler.
     * @param <T> value type.
     */
    protected <T> void register(Class<T> valueClass, Pickler<T, PF> pickler) {
        picklerRegistry.put(valueClass.getName(), pickler);
    }

    /**
     * Register a Pickler by class, for the specified value Class type.
     * @param valueClass value class.
     * @param picklerClass class for the Pickler.
     * @param <T> value type.
     * @param <P> pickler type.
     */
    public <T, P extends Pickler<T, PF>> void register(Class<T> valueClass, Class<P> picklerClass) {
        try {
            // Acquire a picklerClass constructor which takes a PicklerCore.
            final Constructor<P> ctor = picklerClass.getConstructor(PicklerCore.class);

            // Invoke the constructor to get a new instance of the Pickler.
            final Pickler<T, PF> pickler = ctor.newInstance(this);

            // Register the Pickler.
            register(valueClass, pickler);
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

    public <T> Pickler<T, PF> object(Class<T> clazz) {
        final Pickler<T, PF> result = (Pickler<T, PF>)picklerRegistry.get(clazz.getName());
        if (result == null)
            throw new PicklerException("No pickler registered for class " + clazz);
        return result;
    }
}
