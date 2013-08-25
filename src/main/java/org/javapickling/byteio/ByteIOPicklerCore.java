package org.javapickling.byteio;

import org.javapickling.core.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class ByteIOPicklerCore extends PicklerCoreBase<ByteIO> {

    protected final Pickler<Object, ByteIO> nullP = new Pickler<Object, ByteIO>() {

        @Override
        public ByteIO pickle(Object obj, ByteIO target) throws IOException {
            return target;
        }

        @Override
        public Object unpickle(ByteIO source) throws IOException {
            return null;
        }
    };

    protected final Pickler<Boolean, ByteIO> boolP = new Pickler<Boolean, ByteIO>() {

        @Override
        public ByteIO pickle(Boolean b, ByteIO target) throws IOException {
            target.output.writeBoolean(b);
            return target;
        }

        @Override
        public Boolean unpickle(ByteIO source) throws IOException {
            return source.input.readBoolean();
        }
    };

    protected final Pickler<Byte, ByteIO> byteP = new Pickler<Byte, ByteIO>() {

        @Override
        public ByteIO pickle(Byte b, ByteIO target) throws IOException {
            target.output.writeByte(b);
            return target;
        }

        @Override
        public Byte unpickle(ByteIO source) throws IOException {
            return source.input.readByte();
        }
    };

    protected final Pickler<Character, ByteIO> charP = new Pickler<Character, ByteIO>() {

        @Override
        public ByteIO pickle(Character c, ByteIO target) throws IOException {
            target.output.writeChar(c);
            return target;
        }

        @Override
        public Character unpickle(ByteIO source) throws IOException {
            return source.input.readChar();
        }
    };

    protected final Pickler<String, ByteIO> stringP = new Pickler<String, ByteIO>() {

        @Override
        public ByteIO pickle(String s, ByteIO target) throws IOException {
            target.writeString(s);
            return target;
        }

        @Override
        public String unpickle(ByteIO source) throws IOException {
            return source.readString();
        }
    };

    protected final Pickler<Integer, ByteIO> integerP = new Pickler<Integer, ByteIO>() {

        @Override
        public ByteIO pickle(Integer i, ByteIO target) throws IOException {
            target.output.writeInt(i);
            return target;
        }

        @Override
        public Integer unpickle(ByteIO source) throws IOException {
            return source.input.readInt();
        }
    };

    protected final Pickler<Short, ByteIO> shortP = new Pickler<Short, ByteIO>() {

        @Override
        public ByteIO pickle(Short s, ByteIO target) throws IOException {
            target.output.writeShort(s);
            return target;
        }

        @Override
        public Short unpickle(ByteIO source) throws IOException {
            return source.input.readShort();
        }
    };

    protected final Pickler<Long, ByteIO> longP = new Pickler<Long, ByteIO>() {

        @Override
        public ByteIO pickle(Long l, ByteIO target) throws IOException {
            target.output.writeLong(l);
            return target;
        }

        @Override
        public Long unpickle(ByteIO source) throws IOException {
            return source.input.readLong();
        }
    };

    protected final Pickler<Float, ByteIO> floatP = new Pickler<Float, ByteIO>() {

        @Override
        public ByteIO pickle(Float f, ByteIO target) throws IOException {
            target.output.writeFloat(f);
            return target;
        }

        @Override
        public Float unpickle(ByteIO source) throws IOException {
            return source.input.readFloat();
        }
    };

    protected final Pickler<Double, ByteIO> doubleP = new Pickler<Double, ByteIO>() {

        @Override
        public ByteIO pickle(Double d, ByteIO target) throws IOException {
            target.output.writeDouble(d);
            return target;
        }

        @Override
        public Double unpickle(ByteIO source) throws IOException {
            return source.input.readDouble();
        }
    };

    protected final MapPickler<ByteIO> mapP = new MapPickler<ByteIO>() {

        @Override
        public FieldPickler<ByteIO> pickler(final ByteIO target) {

            return new FieldPicklerBase(target) {

                @Override
                public <T> void field(final String name, final T value, final Pickler<T, ByteIO> pickler) throws IOException {
                    pickler.pickle(value, target);
                }

                @Override
                public <T> void field(Field<T, ByteIO> field, T value) throws IOException {
                    field.pickler.pickle(value, target);
                }

                @Override
                public ByteIO pickle(ByteIO target) throws IOException {
                    return target;
                }
            };
        }

        @Override
        public FieldUnpickler<ByteIO> unpickler(final ByteIO source) {

            return new FieldUnpicklerBase(source) {

                @Override
                public <T> T field(String name, Pickler<T, ByteIO> pickler) throws IOException {
                    return pickler.unpickle(source);
                }

                @Override
                public <T> T field(Field<T, ByteIO> field) throws IOException {
                    return field.pickler.unpickle(source);
                }
            };
        }
    };

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
    public <T extends Enum<T>> Pickler<T, ByteIO> enum_p(final Class<T> enumClass) {

        return new Pickler<T, ByteIO>() {

            @Override
            public ByteIO pickle(T t, ByteIO target) throws IOException {
                target.writeString(t.name());
                return target;
            }

            @Override
            public T unpickle(ByteIO source) throws IOException {
                return T.valueOf(enumClass, source.readString());
            }
        };
    }

    @Override
    public <T> Pickler<T[], ByteIO> array_p(final Pickler<T, ByteIO> elemPickler, final Class<T> elemClass) {

        return new Pickler<T[], ByteIO>() {

            @Override
            public ByteIO pickle(T[] arr, ByteIO target) throws IOException {

                target.output.writeInt(arr.length);

                for (T elem : arr) {
                    elemPickler.pickle(elem, target);
                }

                return target;
            }

            @Override
            public T[] unpickle(ByteIO source) throws IOException {

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
            public ByteIO pickle(List<T> list, ByteIO target) throws IOException {

                target.output.writeInt(list.size());

                for (T elem : list) {
                    elemPickler.pickle(elem, target);
                }

                return target;
            }

            @Override
            public List<T> unpickle(ByteIO source) throws IOException {

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
            public ByteIO pickle(Map<String, T> map, ByteIO target) throws IOException {

                target.output.writeInt(map.size());

                for (Map.Entry<String, T> entry : map.entrySet()) {
                    target.writeString(entry.getKey());
                    valuePickler.pickle(entry.getValue(), target);
                }

                return target;
            }

            @Override
            public Map<String, T> unpickle(ByteIO source) throws IOException {

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
            public ByteIO pickle(Map<K, V> map, ByteIO target) throws IOException {

                target.output.writeInt(map.size());

                for (Map.Entry<K, V> entry : map.entrySet()) {
                    keyPickler.pickle(entry.getKey(), target);
                    valuePickler.pickle(entry.getValue(), target);
                }

                return target;
            }

            @Override
            public Map<K, V> unpickle(ByteIO source) throws IOException {

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
            public ByteIO pickle(Set<T> set, ByteIO target) throws IOException {

                target.output.writeInt(set.size());

                for (T elem : set) {
                    elemPickler.pickle(elem, target);
                }

                return target;
            }

            @Override
            public Set<T> unpickle(ByteIO source) throws IOException {

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
    public MapPickler<ByteIO> object_map() {
        return mapP;
    }
}
