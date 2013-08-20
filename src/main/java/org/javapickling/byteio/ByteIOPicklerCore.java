package org.javapickling.byteio;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.javapickling.core.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ByteIOPicklerCore extends PicklerCoreBase<ByteIO> {

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

            return new FieldPicklerBase() {

                @Override
                public <T> void field(final String name, final T value, final Pickler<T, ByteIO> pickler) throws IOException {
                    target.writeString(name);
                    pickler.pickle(value, target);
                }

                @Override
                public ByteIO pickle(ByteIO target) throws IOException {
                    return target;
                }
            };
        }

        @Override
        public FieldUnpickler<ByteIO> unpickler(final ByteIO source) {

            return new FieldUnpicklerBase() {

                @Override
                public <T> T field(String name, ByteIO source, Pickler<T, ByteIO> pickler) throws IOException {
                    final String s = source.readString();
                    if (!s.equals(name))
                        throw new PicklerException("Expecting field name '" + name + "' but got '" + s + "'");
                    return pickler.unpickle(source);
                }
            };
        }
    };

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
    public <T> Pickler<T[], ByteIO> array_p(final Pickler<T, ByteIO> elemPickler) {

        return new Pickler<T[], ByteIO>() {

            @Override
            public ByteIO pickle(T[] list, ByteIO target) throws IOException {

                target.output.writeInt(list.length);

                for (T elem : list) {
                    elemPickler.pickle(elem, target);
                }

                return target;
            }

            @Override
            public T[] unpickle(ByteIO source) throws IOException {

                int size = source.input.readInt();
                final T[] result = (T[]) new Object[size];

                for (int i = 0; i < size; ++i) {
                    result[i] = elemPickler.unpickle(source);
                }

                return result;
            }
        };
    }

    @Override
    public <T> Pickler<List<T>, ByteIO> list_p(final Pickler<T, ByteIO> elemPickler) {

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
                final List<T> result = Lists.newArrayListWithCapacity(size);

                for (int i = 0; i < size; ++i) {
                    result.add(elemPickler.unpickle(source));
                }

                return result;
            }
        };
    }

    @Override
    public <T> Pickler<Map<String, T>, ByteIO> map_p(final Pickler<T, ByteIO> elemPickler) {

        return new Pickler<Map<String, T>, ByteIO>() {

            @Override
            public ByteIO pickle(Map<String, T> map, ByteIO target) throws IOException {

                target.output.writeInt(map.size());

                for (Map.Entry<String, T> entry : map.entrySet()) {
                    target.writeString(entry.getKey());
                    elemPickler.pickle(entry.getValue(), target);
                }

                return target;
            }

            @Override
            public Map<String, T> unpickle(ByteIO source) throws IOException {

                final int size = source.input.readInt();

                final Map<String, T> result = Maps.newTreeMap();
                for (int i = 0; i < size; ++i) {
                    final String key = source.readString();
                    result.put(key, elemPickler.unpickle(source));
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
