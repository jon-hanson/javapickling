package org.javapickling.core;

import com.google.common.collect.Maps;
import org.javapickling.byteio.ByteIO;

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

        protected final PF target;

        public FieldPicklerBase(PF target) {
            this.target = target;
        }

        @Override
        public void boolean_f(String name, Boolean value) throws IOException {
            field(name, value, PicklerCoreBase.this.boolean_p());
        }

        @Override
        public void byte_f(String name, Byte value) throws IOException {
            field(name, value, PicklerCoreBase.this.byte_p());
        }

        @Override
        public void char_f(String name, Character value) throws IOException {
            field(name, value, PicklerCoreBase.this.char_p());
        }

        @Override
        public void short_f(String name, Short value) throws IOException {
            field(name, value, PicklerCoreBase.this.short_p());
        }

        @Override
        public void long_f(String name, Long value) throws IOException {
            field(name, value, PicklerCoreBase.this.long_p());
        }

        @Override
        public void integer_f(String name, Integer value) throws IOException {
            field(name, value, PicklerCoreBase.this.integer_p());
        }

        @Override
        public void float_f(String name, Float value) throws IOException {
            field(name, value, PicklerCoreBase.this.float_p());
        }

        @Override
        public void double_f(String name, Double value) throws IOException {
            field(name, value, PicklerCoreBase.this.double_p());
        }

        @Override
        public <T extends Enum<T>> void enum_f(String name, T value, Class<T> clazz) throws IOException {
            field(name, value, PicklerCoreBase.this.enum_p(clazz));
        }

        @Override
        public void string_f(String name, String value) throws IOException {
            field(name, value, PicklerCoreBase.this.string_p());
        }
    }

    /**
     * FieldUnpicklerBase adds helper methods to simplify calling FieldUnpickler.field().
     */
    protected abstract class FieldUnpicklerBase implements FieldUnpickler<PF> {

        protected final PF source;

        public FieldUnpicklerBase(PF source) {
            this.source = source;
        }

        @Override
        public Boolean boolean_f(String name) throws IOException {
            return field(name, PicklerCoreBase.this.boolean_p());
        }

        @Override
        public Byte byte_f(String name) throws IOException {
            return field(name, PicklerCoreBase.this.byte_p());
        }

        @Override
        public Character char_f(String name) throws IOException {
            return field(name, PicklerCoreBase.this.char_p());
        }

        @Override
        public Short short_f(String name) throws IOException {
            return field(name, PicklerCoreBase.this.short_p());
        }

        @Override
        public Long long_f(String name) throws IOException {
            return field(name, PicklerCoreBase.this.long_p());
        }

        @Override
        public Integer integer_f(String name) throws IOException {
            return field(name, PicklerCoreBase.this.integer_p());
        }

        @Override
        public Float float_f(String name) throws IOException {
            return field(name, PicklerCoreBase.this.float_p());
        }

        @Override
        public Double double_f(String name) throws IOException {
            return field(name, PicklerCoreBase.this.double_p());
        }

        @Override
        public <T extends Enum<T>> T enum_f(String name, Class<T> clazz) throws IOException {
            return field(name, PicklerCoreBase.this.enum_p(clazz));
        }

        @Override
        public String string_f(String name) throws IOException {
            return field(name, PicklerCoreBase.this.string_p());
        }
    }

    /**
     * A registry of picklers by class name.
     */
    private final Map<String, Pickler<?, PF>> picklerRegistry = Maps.newTreeMap();

    public PicklerCoreBase() {
        register(Boolean.class, boolean_p());
        register(Byte.class, byte_p());
        register(Character.class, char_p());
        register(String.class, string_p());
        register(Integer.class, integer_p());
        register(Short.class, short_p());
        register(Long.class, long_p());
        register(Float.class, float_p());
        register(Double.class, double_p());
        register(Object.class, object_p());
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

    public <T> Pickler<T, PF> object_p(Class<T> clazz) {
        final Pickler<T, PF> result = (Pickler<T, PF>)picklerRegistry.get(clazz.getName());
        if (result == null)
            throw new PicklerException("No pickler registered for class " + clazz);
        return result;
    }

    public Pickler<Object, PF> object_p() {
        return new DynamicObjectPickler<PF>(this);
    }
}
