package org.javapickling.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javapickling.core.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class JsonPicklerCore extends PicklerCoreBase<JsonNode> {

    private final JsonNodeFactory nodeFactory;

    public JsonPicklerCore() {
        this(JsonNodeFactory.instance);
    }

    public JsonPicklerCore(JsonNodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    protected final Pickler<Object, JsonNode> nullP = new Pickler<Object, JsonNode>() {

        @Override
        public JsonNode pickle(Object obj, JsonNode target) {
            return nodeFactory.nullNode();
        }

        @Override
        public Object unpickle(JsonNode source) {
            return null;
        }
    };

    protected final Pickler<Boolean, JsonNode> booleanP = new Pickler<Boolean, JsonNode>() {

        @Override
        public JsonNode pickle(Boolean b, JsonNode target) {
            return nodeFactory.booleanNode(b);
        }

        @Override
        public Boolean unpickle(JsonNode source) {
            return source.asBoolean();
        }
    };

    protected final Pickler<Byte, JsonNode> byteP = new Pickler<Byte, JsonNode>() {

        @Override
        public JsonNode pickle(Byte b, JsonNode target) {
            return nodeFactory.numberNode(b);
        }

        @Override
        public Byte unpickle(JsonNode source) {
            return (byte)source.asInt();
        }
    };

    protected final Pickler<Character, JsonNode> charP = new Pickler<Character, JsonNode>() {

        @Override
        public JsonNode pickle(Character c, JsonNode target) throws IOException {
            return nodeFactory.textNode(String.valueOf(c));
        }

        @Override
        public Character unpickle(JsonNode source) throws IOException {
            return source.asText().charAt(0);
        }
    };

    protected final Pickler<String, JsonNode> stringP = new Pickler<String, JsonNode>() {

        @Override
        public JsonNode pickle(String s, JsonNode target) {
            return nodeFactory.textNode(s);
        }

        @Override
        public String unpickle(JsonNode source) {
            return source.asText();
        }
    };

    protected final Pickler<Integer, JsonNode> integerP = new Pickler<Integer, JsonNode>() {

        @Override
        public JsonNode pickle(Integer i, JsonNode target) {
            return nodeFactory.numberNode(i);
        }

        @Override
        public Integer unpickle(JsonNode source) {
            return source.asInt();
        }
    };

    protected final Pickler<Short, JsonNode> shortP = new Pickler<Short, JsonNode>() {

        @Override
        public JsonNode pickle(Short s, JsonNode target) {
            return nodeFactory.numberNode(s);
        }

        @Override
        public Short unpickle(JsonNode source) {
            return source.shortValue();
        }
    };
    protected final Pickler<Long, JsonNode> longP = new Pickler<Long, JsonNode>() {

        @Override
        public JsonNode pickle(Long l, JsonNode target) {
            return nodeFactory.numberNode(l);
        }

        @Override
        public Long unpickle(JsonNode source) {
            return source.asLong();
        }
    };

    protected final Pickler<Float, JsonNode> floatP = new Pickler<Float, JsonNode>() {

        @Override
        public JsonNode pickle(Float d, JsonNode target) {
            return nodeFactory.numberNode(d);
        }

        @Override
        public Float unpickle(JsonNode source) {
            return source.floatValue();
        }
    };

    protected final Pickler<Double, JsonNode> doubleP = new Pickler<Double, JsonNode>() {

        @Override
        public JsonNode pickle(Double d, JsonNode target) {
            return nodeFactory.numberNode(d);
        }

        @Override
        public Double unpickle(JsonNode source) {
            return source.asDouble();
        }
    };

    protected final MapPickler<JsonNode> mapP = new MapPickler<JsonNode>() {

        @Override
        public FieldPickler<JsonNode> pickler(final JsonNode target) {

            return new FieldPicklerBase(target) {

                private ObjectNode objectNode = nodeFactory.objectNode();

                @Override
                public <T> void field(String name, T value, Pickler<T, JsonNode> pickler) throws IOException {
                    objectNode.put(name, pickler.pickle(value, target));
                }

                @Override
                public JsonNode pickle(JsonNode source) {
                    return objectNode;
                }
            };
        }

        @Override
        public FieldUnpickler<JsonNode> unpickler(final JsonNode source) {

            return new FieldUnpicklerBase(source) {

                @Override
                public <T> T field(String name, Pickler<T, JsonNode> pickler) throws IOException {
                    return pickler.unpickle(source.get(name));
                }
            };
        }
    };

    @Override
    public Pickler<Object, JsonNode> null_p() {
        return nullP;
    }

    @Override
    public Pickler<Boolean, JsonNode> boolean_p() {
        return booleanP;
    }

    @Override
    public Pickler<Byte, JsonNode> byte_p() {
        return byteP;
    }

    @Override
    public Pickler<Character, JsonNode> char_p() {
        return charP;
    }

    @Override
    public Pickler<String, JsonNode> string_p() {
        return stringP;
    }

    @Override
    public Pickler<Integer, JsonNode> integer_p() {
        return integerP;
    }

    @Override
    public Pickler<Short, JsonNode> short_p() {
        return shortP;
    }

    @Override
    public Pickler<Long, JsonNode> long_p() {
        return longP;
    }

    @Override
    public Pickler<Float, JsonNode> float_p() {
        return floatP;
    }

    @Override
    public Pickler<Double, JsonNode> double_p() {
        return doubleP;
    }

    @Override
    public <T extends Enum<T>> Pickler<T, JsonNode> enum_p(final Class<T> enumClass) {

        return new Pickler<T, JsonNode>() {

            @Override
            public JsonNode pickle(T t, JsonNode target) throws IOException {
                return nodeFactory.textNode(t.name());
            }

            @Override
            public T unpickle(JsonNode source) throws IOException {
                return T.valueOf(enumClass, source.asText());
            }
        };
    }

    @Override
    public <T> Pickler<T[], JsonNode> array_p(
            final Pickler<T, JsonNode> elemPickler,
            final Class<T> elemClass) {

        return new Pickler<T[], JsonNode>() {

            @Override
            public JsonNode pickle(T[] arr, JsonNode target) throws IOException {

                final ArrayNode result = nodeFactory.arrayNode();

                for (T elem : arr) {
                    result.add(elemPickler.pickle(elem, result));
                }

                return result;
            }

            @Override
            public T[] unpickle(JsonNode source) throws IOException {

                if (!source.isArray())
                    throw new PicklerException("Can not unpickle a " + source.getNodeType() + " into an array");

                final ArrayNode contNode = (ArrayNode)source;

                final T[] result = (T[]) Array.newInstance(elemClass, contNode.size());

                int i = 0;
                for (JsonNode elem : contNode) {
                    result[i] = elemPickler.unpickle(elem);
                    ++i;
                }

                return result;
            }
        };
    }

    @Override
    public <T> Pickler<List<T>, JsonNode> list_p(
            final Pickler<T, JsonNode> elemPickler,
            final Class<? extends List> listClass) {

        return new Pickler<List<T>, JsonNode>() {

            @Override
            public JsonNode pickle(List<T> list, JsonNode target) throws IOException {

                final ArrayNode result = nodeFactory.arrayNode();

                for (T elem : list) {
                    result.add(elemPickler.pickle(elem, result));
                }

                return result;
            }

            @Override
            public List<T> unpickle(JsonNode source) throws IOException {

                if (!source.isArray())
                    throw new PicklerException("Can not unpickle a " + source.getNodeType() + " into a List");

                final ArrayNode contNode = (ArrayNode)source;

                final List<T> result = newInstance(listClass);

                for (JsonNode elem : contNode) {
                    result.add(elemPickler.unpickle(elem));
                }

                return result;
            }
        };
    }

    @Override
    public <T> Pickler<Map<String, T>, JsonNode> map_p(
            final Pickler<T, JsonNode> valuePickler,
            final Class<? extends Map> mapClass) {

        return new Pickler<Map<String, T>, JsonNode>() {

            @Override
            public JsonNode pickle(Map<String, T> map, JsonNode target) throws IOException {

                final ObjectNode result = nodeFactory.objectNode();

                for (Map.Entry<String, T> entry : map.entrySet()) {
                    result.put(entry.getKey(), valuePickler.pickle(entry.getValue(), result));
                }

                return result;
            }

            @Override
            public Map<String, T> unpickle(JsonNode source) throws IOException {

                if (!source.isObject())
                    throw new PicklerException("Can not unpickle a " + source.getNodeType() + " into a Map");

                final ObjectNode objectNode = (ObjectNode)source;

                final Map<String, T> result = newInstance(mapClass);

                for (Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields(); iter.hasNext();) {
                    final Map.Entry<String, JsonNode> entry = iter.next();
                    result.put(entry.getKey(), valuePickler.unpickle(entry.getValue()));
                }

                return result;
            }
        };
    }

    @Override
    public <K, V> Pickler<Map<K, V>, JsonNode> map_p(
            final Pickler<K, JsonNode> keyPickler,
            final Pickler<V, JsonNode> valuePickler,
            final Class<?  extends Map> mapClass) {

        return new Pickler<Map<K, V>, JsonNode>() {

            private static final String keyF = "key";
            private static final String valueF = "value";

            @Override
            public JsonNode pickle(Map<K, V> map, JsonNode target) throws IOException {

                final ArrayNode result = nodeFactory.arrayNode();

                for (Map.Entry<K, V> entry : map.entrySet()) {
                    final ObjectNode elem = nodeFactory.objectNode();
                    elem.put(keyF, keyPickler.pickle(entry.getKey(), result));
                    elem.put(valueF, valuePickler.pickle(entry.getValue(), result));
                    result.add(elem);
                }

                return result;
            }

            @Override
            public Map<K, V> unpickle(JsonNode source) throws IOException {

                if (!source.isArray())
                    throw new PicklerException("Can not unpickle a " + source.getNodeType() + " into a Map");

                final ArrayNode arrayNode = (ArrayNode)source;

                final Map<K, V> result;
                try {
                    result = mapClass.newInstance();
                } catch (InstantiationException ex) {
                    throw new PicklerException("Can not create map class", ex);
                } catch (IllegalAccessException ex) {
                    throw new PicklerException("Can not create map class", ex);
                }

                for (JsonNode child : arrayNode) {
                    final ObjectNode objectNode = (ObjectNode)child;
                    final K key = keyPickler.unpickle(objectNode.get(keyF));
                    final V value = valuePickler.unpickle(objectNode.get(valueF));
                    result.put(key, value);
                }

                return result;
            }
        };
    }

    @Override
    public <T> Pickler<Set<T>, JsonNode> set_p(
            final Pickler<T, JsonNode> elemPickler,
            final Class<? extends Set> setClass) {

        return new Pickler<Set<T>, JsonNode>() {

            @Override
            public JsonNode pickle(Set<T> set, JsonNode target) throws IOException {

                final ArrayNode result = nodeFactory.arrayNode();

                for (T elem : set) {
                    result.add(elemPickler.pickle(elem, result));
                }

                return result;
            }

            @Override
            public Set<T> unpickle(JsonNode source) throws IOException {

                if (!source.isArray())
                    throw new PicklerException("Can not unpickle a " + source.getNodeType() + " into a Set");

                final ArrayNode contNode = (ArrayNode)source;

                final Set<T> result = newInstance(setClass);

                for (JsonNode elem : contNode) {
                    result.add(elemPickler.unpickle(elem));
                }

                return result;
            }
        };
    }

    @Override
    public MapPickler<JsonNode> object_map() {
        return mapP;
    }
}
