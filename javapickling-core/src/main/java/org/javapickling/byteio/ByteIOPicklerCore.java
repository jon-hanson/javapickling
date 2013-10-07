package org.javapickling.byteio;

import org.javapickling.core.*;

import java.lang.reflect.Array;
import java.util.*;

/**
 * PicklerCore implementation which pickles objects to byte[] data, via the ByteIO wrapper.
 */
public class ByteIOPicklerCore extends PicklerCoreBase<ByteIO> {

    public static ByteIOPicklerCore create() {
        final ByteIOPicklerCore core = new ByteIOPicklerCore();
        core.initialise();
        return core;
    }

    protected final Pickler<Object, ByteIO> nullP = new Pickler<Object, ByteIO>() {

        @Override
        public ByteIO pickle(Object obj, ByteIO target) throws Exception {
            return target;
        }

        @Override
        public Object unpickle(ByteIO source) throws Exception {
            return null;
        }
    };

    protected final Pickler<Boolean, ByteIO> boolP = new Pickler<Boolean, ByteIO>() {

        @Override
        public ByteIO pickle(Boolean b, ByteIO target) throws Exception {
            target.output.writeBoolean(b);
            return target;
        }

        @Override
        public Boolean unpickle(ByteIO source) throws Exception {
            return source.input.readBoolean();
        }
    };

    protected final Pickler<Byte, ByteIO> byteP = new Pickler<Byte, ByteIO>() {

        @Override
        public ByteIO pickle(Byte b, ByteIO target) throws Exception {
            target.output.writeByte(b);
            return target;
        }

        @Override
        public Byte unpickle(ByteIO source) throws Exception {
            return source.input.readByte();
        }
    };

    protected final Pickler<Character, ByteIO> charP = new Pickler<Character, ByteIO>() {

        @Override
        public ByteIO pickle(Character c, ByteIO target) throws Exception {
            target.output.writeChar(c);
            return target;
        }

        @Override
        public Character unpickle(ByteIO source) throws Exception {
            return source.input.readChar();
        }
    };

    protected final Pickler<String, ByteIO> stringP = new Pickler<String, ByteIO>() {

        @Override
        public ByteIO pickle(String s, ByteIO target) throws Exception {
            target.writeString(s);
            return target;
        }

        @Override
        public String unpickle(ByteIO source) throws Exception {
            return source.readString();
        }
    };

    protected final Pickler<Integer, ByteIO> integerP = new Pickler<Integer, ByteIO>() {

        @Override
        public ByteIO pickle(Integer i, ByteIO target) throws Exception {
            target.output.writeInt(i);
            return target;
        }

        @Override
        public Integer unpickle(ByteIO source) throws Exception {
            return source.input.readInt();
        }
    };

    protected final Pickler<Short, ByteIO> shortP = new Pickler<Short, ByteIO>() {

        @Override
        public ByteIO pickle(Short s, ByteIO target) throws Exception {
            target.output.writeShort(s);
            return target;
        }

        @Override
        public Short unpickle(ByteIO source) throws Exception {
            return source.input.readShort();
        }
    };

    protected final Pickler<Long, ByteIO> longP = new Pickler<Long, ByteIO>() {

        @Override
        public ByteIO pickle(Long l, ByteIO target) throws Exception {
            target.output.writeLong(l);
            return target;
        }

        @Override
        public Long unpickle(ByteIO source) throws Exception {
            return source.input.readLong();
        }
    };

    protected final Pickler<Float, ByteIO> floatP = new Pickler<Float, ByteIO>() {

        @Override
        public ByteIO pickle(Float f, ByteIO target) throws Exception {
            target.output.writeFloat(f);
            return target;
        }

        @Override
        public Float unpickle(ByteIO source) throws Exception {
            return source.input.readFloat();
        }
    };

    protected final Pickler<Double, ByteIO> doubleP = new Pickler<Double, ByteIO>() {

        @Override
        public ByteIO pickle(Double d, ByteIO target) throws Exception {
            target.output.writeDouble(d);
            return target;
        }

        @Override
        public Double unpickle(ByteIO source) throws Exception {
            return source.input.readDouble();
        }
    };

    protected final Pickler<boolean[], ByteIO> booleanArrayP = new Pickler<boolean[], ByteIO>() {

        final Pickler<Boolean, ByteIO> elemPickler = boolean_p();

        @Override
        public ByteIO pickle(boolean[] arr, ByteIO target) throws Exception {

            target.output.writeInt(arr.length);

            for (boolean elem : arr) {
                elemPickler.pickle(elem, target);
            }

            return target;
        }

        @Override
        public boolean[] unpickle(ByteIO source) throws Exception {

            int size = source.input.readInt();
            final boolean[] result = new boolean[size];

            for (int i = 0; i < size; ++i) {
                result[i] = elemPickler.unpickle(source);
            }

            return result;
        }
    };

    protected final Pickler<byte[], ByteIO> byteArrayP = new Pickler<byte[], ByteIO>() {

        final Pickler<Byte, ByteIO> elemPickler = byte_p();

        @Override
        public ByteIO pickle(byte[] arr, ByteIO target) throws Exception {

            target.output.writeInt(arr.length);

            for (byte elem : arr) {
                elemPickler.pickle(elem, target);
            }

            return target;
        }

        @Override
        public byte[] unpickle(ByteIO source) throws Exception {

            int size = source.input.readInt();
            final byte[] result = new byte[size];

            for (int i = 0; i < size; ++i) {
                result[i] = elemPickler.unpickle(source);
            }

            return result;
        }
    };

    protected final Pickler<char[], ByteIO> charArrayP = new Pickler<char[], ByteIO>() {

        final Pickler<Character, ByteIO> elemPickler =char_p();

        @Override
        public ByteIO pickle(char[] arr, ByteIO target) throws Exception {

            target.output.writeInt(arr.length);

            for (char elem : arr) {
                elemPickler.pickle(elem, target);
            }

            return target;
        }

        @Override
        public char[] unpickle(ByteIO source) throws Exception {

            int size = source.input.readInt();
            final char[] result = new char[size];

            for (int i = 0; i < size; ++i) {
                result[i] = elemPickler.unpickle(source);
            }

            return result;
        }
    };

    protected final Pickler<int[], ByteIO> integerArrayP = new Pickler<int[], ByteIO>() {

        final Pickler<Integer, ByteIO> elemPickler = integer_p();

        @Override
        public ByteIO pickle(int[] arr, ByteIO target) throws Exception {

            target.output.writeInt(arr.length);

            for (int elem : arr) {
                elemPickler.pickle(elem, target);
            }

            return target;
        }

        @Override
        public int[] unpickle(ByteIO source) throws Exception {

            int size = source.input.readInt();
            final int[] result = new int[size];

            for (int i = 0; i < size; ++i) {
                result[i] = elemPickler.unpickle(source);
            }

            return result;
        }
    };

    protected final Pickler<short[], ByteIO> shortArrayP = new Pickler<short[], ByteIO>() {

        final Pickler<Short, ByteIO> elemPickler = short_p();

        @Override
        public ByteIO pickle(short[] arr, ByteIO target) throws Exception {

            target.output.writeInt(arr.length);

            for (short elem : arr) {
                elemPickler.pickle(elem, target);
            }

            return target;
        }

        @Override
        public short[] unpickle(ByteIO source) throws Exception {

            int size = source.input.readInt();
            final short[] result = new short[size];

            for (int i = 0; i < size; ++i) {
                result[i] = elemPickler.unpickle(source);
            }

            return result;
        }
    };

    protected final Pickler<long[], ByteIO> longArrayP = new Pickler<long[], ByteIO>() {

        final Pickler<Long, ByteIO> elemPickler = long_p();

        @Override
        public ByteIO pickle(long[] arr, ByteIO target) throws Exception {

            target.output.writeInt(arr.length);

            for (long elem : arr) {
                elemPickler.pickle(elem, target);
            }

            return target;
        }

        @Override
        public long[] unpickle(ByteIO source) throws Exception {

            int size = source.input.readInt();
            final long[] result = new long[size];

            for (int i = 0; i < size; ++i) {
                result[i] = elemPickler.unpickle(source);
            }

            return result;
        }
    };

    protected final Pickler<float[], ByteIO> floatArrayP = new Pickler<float[], ByteIO>() {

        final Pickler<Float, ByteIO> elemPickler = float_p();

        @Override
        public ByteIO pickle(float[] arr, ByteIO target) throws Exception {

            target.output.writeInt(arr.length);

            for (float elem : arr) {
                elemPickler.pickle(elem, target);
            }

            return target;
        }

        @Override
        public float[] unpickle(ByteIO source) throws Exception {

            int size = source.input.readInt();
            final float[] result = new float[size];

            for (int i = 0; i < size; ++i) {
                result[i] = elemPickler.unpickle(source);
            }

            return result;
        }
    };

    protected final Pickler<double[], ByteIO> doubleArrayP = new Pickler<double[], ByteIO>() {

        final Pickler<Double, ByteIO> elemPickler = double_p();

        @Override
        public ByteIO pickle(double[] arr, ByteIO target) throws Exception {

            target.output.writeInt(arr.length);

            for (double elem : arr) {
                elemPickler.pickle(elem, target);
            }

            return target;
        }

        @Override
        public double[] unpickle(ByteIO source) throws Exception {

            int size = source.input.readInt();
            final double[] result = new double[size];

            for (int i = 0; i < size; ++i) {
                result[i] = elemPickler.unpickle(source);
            }

            return result;
        }
    };

    protected final ObjectPickler<ByteIO> objectMapP = new ObjectPickler<ByteIO>() {

        @Override
        public FieldPickler<ByteIO> pickler(final ByteIO target) {

            return new AbstractFieldPickler(target) {

                @Override
                public <T> void field(final String name, final T value, final Pickler<T, ByteIO> pickler) throws Exception {
                    pickler.pickle(value, target);
                }

                @Override
                public <T> void field(Field<T, ByteIO> field, T value) throws Exception {
                    field.pickler.pickle(value, target);
                }

                @Override
                public ByteIO pickle(ByteIO target) throws Exception {
                    return target;
                }
            };
        }

        @Override
        public FieldUnpickler<ByteIO> unpickler(final ByteIO source) {

            return new AbstractFieldUnpickler(source) {

                @Override
                public <T> T field(String name, Pickler<T, ByteIO> pickler) throws Exception {
                    return pickler.unpickle(source);
                }

                @Override
                public <T> T field(Field<T, ByteIO> field) throws Exception {
                    return field.pickler.unpickle(source);
                }
            };
        }
    };

    private ByteIOPicklerCore() {
    }

    @Override
    public Pickler<Object, ByteIO> null_p() {
        return nullP;
    }

    @Override
    public Pickler<Boolean, ByteIO> boolean_p() {
        return boolP;
    }

    @Override
    public Pickler<Byte, ByteIO> byte_p() {
        return byteP;
    }

    @Override
    public Pickler<Character, ByteIO> char_p() {
        return charP;
    }

    @Override
    public Pickler<String, ByteIO> string_p() {
        return stringP;
    }

    @Override
    public Pickler<Integer, ByteIO> integer_p() {
        return integerP;
    }

    @Override
    public Pickler<Short, ByteIO> short_p() {
        return shortP;
    }

    @Override
    public Pickler<Long, ByteIO> long_p() {
        return longP;
    }

    @Override
    public Pickler<Float, ByteIO> float_p() {
        return floatP;
    }

    @Override
    public Pickler<Double, ByteIO> double_p() {
        return doubleP;
    }

    @Override
    public Pickler<boolean[], ByteIO> boolean_array_p() {

        return booleanArrayP;
    }

    @Override
    public Pickler<byte[], ByteIO> byte_array_p() {
        return byteArrayP;
    }

    @Override
    public Pickler<char[], ByteIO> char_array_p() {
        return charArrayP;
    }

    @Override
    public Pickler<int[], ByteIO> integer_array_p() {
        return integerArrayP;
    }

    @Override
    public Pickler<short[], ByteIO> short_array_p() {
        return shortArrayP;
    }

    @Override
    public Pickler<long[], ByteIO> long_array_p() {
        return longArrayP;
    }

    @Override
    public Pickler<float[], ByteIO> float_array_p() {
        return floatArrayP;
    }

    @Override
    public Pickler<double[], ByteIO> double_array_p() {
        return doubleArrayP;
    }

    @Override
    public <T extends Enum<T>> Pickler<T, ByteIO> enum_p(final Class<T> enumClass) {

        return new Pickler<T, ByteIO>() {

            @Override
            public ByteIO pickle(T t, ByteIO target) throws Exception {
                target.writeString(t.name());
                return target;
            }

            @Override
            public T unpickle(ByteIO source) throws Exception {
                return T.valueOf(enumClass, source.readString());
            }
        };
    }

    @Override
    public <T> Pickler<T[], ByteIO> array_p(final Pickler<T, ByteIO> elemPickler, final Class<T> elemClass) {

        return new Pickler<T[], ByteIO>() {

            @Override
            public ByteIO pickle(T[] arr, ByteIO target) throws Exception {

                target.output.writeInt(arr.length);

                for (T elem : arr) {
                    elemPickler.pickle(elem, target);
                }

                return target;
            }

            @Override
            public T[] unpickle(ByteIO source) throws Exception {

                int size = source.input.readInt();
                final T[] result = (T[])Array.newInstance(elemClass, size);

                for (int i = 0; i < size; ++i) {
                    result[i] = elemPickler.unpickle(source);
                }

                return result;
            }
        };
    }

    @Override
    public <T> Pickler<List<T>, ByteIO> list_p(
            final Pickler<T, ByteIO> elemPickler,
            final Class<? extends List> listClass) {

        return new Pickler<List<T>, ByteIO>() {

            @Override
            public ByteIO pickle(List<T> list, ByteIO target) throws Exception {

                target.output.writeInt(list.size());

                for (T elem : list) {
                    elemPickler.pickle(elem, target);
                }

                return target;
            }

            @Override
            public List<T> unpickle(ByteIO source) throws Exception {

                int size = source.input.readInt();
                final List<T> result = newInstance(listClass);

                for (int i = 0; i < size; ++i) {
                    result.add(elemPickler.unpickle(source));
                }

                return result;
            }
        };
    }

    @Override
    public <T> Pickler<Map<String, T>, ByteIO> map_p(
            final Pickler<T, ByteIO> valuePickler,
            final Class<? extends Map> mapClass) {

        return new Pickler<Map<String, T>, ByteIO>() {

            @Override
            public ByteIO pickle(Map<String, T> map, ByteIO target) throws Exception {

                target.output.writeInt(map.size());

                for (Map.Entry<String, T> entry : map.entrySet()) {
                    target.writeString(entry.getKey());
                    valuePickler.pickle(entry.getValue(), target);
                }

                return target;
            }

            @Override
            public Map<String, T> unpickle(ByteIO source) throws Exception {

                final int size = source.input.readInt();

                final Map<String, T> result = newInstance(mapClass);

                for (int i = 0; i < size; ++i) {
                    final String key = source.readString();
                    result.put(key, valuePickler.unpickle(source));
                }

                return result;
            }
        };
    }

    @Override
    public <K, V> Pickler<Map<K, V>, ByteIO> map_p(
            final Pickler<K, ByteIO> keyPickler,
            final Pickler<V, ByteIO> valuePickler,
            final Class<?  extends Map> mapClass) {

        return new Pickler<Map<K, V>, ByteIO>() {

            @Override
            public ByteIO pickle(Map<K, V> map, ByteIO target) throws Exception {

                target.output.writeInt(map.size());

                for (Map.Entry<K, V> entry : map.entrySet()) {
                    keyPickler.pickle(entry.getKey(), target);
                    valuePickler.pickle(entry.getValue(), target);
                }

                return target;
            }

            @Override
            public Map<K, V> unpickle(ByteIO source) throws Exception {

                final Map<K, V> result = newInstance(mapClass);

                final int size = source.input.readInt();
                for (int i = 0; i < size; ++i) {
                    final K key = keyPickler.unpickle(source);
                    final V value = valuePickler.unpickle(source);
                    result.put(key, value);
                }

                return result;
            }
        };
    }

    @Override
    public <T> Pickler<Set<T>, ByteIO> set_p(
            final Pickler<T, ByteIO> elemPickler,
            final Class<? extends Set> setClass) {

        return new Pickler<Set<T>, ByteIO>() {

            @Override
            public ByteIO pickle(Set<T> set, ByteIO target) throws Exception {

                target.output.writeInt(set.size());

                for (T elem : set) {
                    elemPickler.pickle(elem, target);
                }

                return target;
            }

            @Override
            public Set<T> unpickle(ByteIO source) throws Exception {

                final Set<T> result = newInstance(setClass);

                int size = source.input.readInt();
                for (int i = 0; i < size; ++i) {
                    result.add(elemPickler.unpickle(source));
                }

                return result;
            }
        };
    }

    @Override
    public ObjectPickler<ByteIO> object_map() {
        return objectMapP;
    }
}
