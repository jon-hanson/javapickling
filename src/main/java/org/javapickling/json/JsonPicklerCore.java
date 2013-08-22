package org.javapickling.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.javapickling.core.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonPicklerCore extends PicklerCoreBase<JsonNode> {

    private final JsonNodeFactory nodeFactory;

    JsonPicklerCore() {
        this(JsonNodeFactory.instance);
    }

    JsonPicklerCore(JsonNodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    protected final Pickler<Object, JsonNode> nullP = new Pickler<Object, JsonNode>() {

        @Override
        public JsonNode pickle(Object obj, JsonNode unused) {
            return nodeFactory.nullNode();
        }

        @Override
        public Object unpickle(JsonNode node) {
            return null;
        }
    };

    protected final Pickler<Boolean, JsonNode> booleanP = new Pickler<Boolean, JsonNode>() {

        @Override
        public JsonNode pickle(Boolean b, JsonNode unused) {
            return nodeFactory.booleanNode(b);
        }

        @Override
        public Boolean unpickle(JsonNode node) {
            return node.asBoolean();
        }
    };

    protected final Pickler<Byte, JsonNode> byteP = new Pickler<Byte, JsonNode>() {

        @Override
        public JsonNode pickle(Byte b, JsonNode unused) {
            return nodeFactory.numberNode(b);
        }

        @Override
        public Byte unpickle(JsonNode node) {
            return (byte)node.asInt();
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
        public JsonNode pickle(String s, JsonNode unused) {
            return nodeFactory.textNode(s);
        }

        @Override
        public String unpickle(JsonNode source) {
            return source.asText();
        }
    };

    protected final Pickler<Integer, JsonNode> integerP = new Pickler<Integer, JsonNode>() {

        @Override
        public JsonNode pickle(Integer i, JsonNode unused) {
            return nodeFactory.numberNode(i);
        }

        @Override
        public Integer unpickle(JsonNode source) {
            return source.asInt();
        }
    };

    protected final Pickler<Short, JsonNode> shortP = new Pickler<Short, JsonNode>() {

        @Override
        public JsonNode pickle(Short s, JsonNode unused) {
            return nodeFactory.numberNode(s);
        }

        @Override
        public Short unpickle(JsonNode source) {
            return source.shortValue();
        }
    };
    protected final Pickler<Long, JsonNode> longP = new Pickler<Long, JsonNode>() {

        @Override
        public JsonNode pickle(Long l, JsonNode unused) {
            return nodeFactory.numberNode(l);
        }

        @Override
        public Long unpickle(JsonNode source) {
            return source.asLong();
        }
    };

    protected final Pickler<Float, JsonNode> floatP = new Pickler<Float, JsonNode>() {

        @Override
        public JsonNode pickle(Float d, JsonNode unused) {
            return nodeFactory.numberNode(d);
        }

        @Override
        public Float unpickle(JsonNode source) {
            return source.floatValue();
        }
    };

    protected final Pickler<Double, JsonNode> doubleP = new Pickler<Double, JsonNode>() {

        @Override
        public JsonNode pickle(Double d, JsonNode unused) {
            return nodeFactory.numberNode(d);
        }

        @Override
        public Double unpickle(JsonNode source) {
            return source.asDouble();
        }
    };

    protected final MapPickler<JsonNode> mapP = new MapPickler<JsonNode>() {

        @Override
        public FieldPickler<JsonNode> pickler(final JsonNode unused) {

            return new FieldPicklerBase() {

                private ObjectNode objectNode = nodeFactory.objectNode();

                @Override
                public <T> void field(String name, T value, Pickler<T, JsonNode> pickler) throws IOException {
                    objectNode.put(name, pickler.pickle(value, unused));
                }

                @Override
                public JsonNode pickle(JsonNode node) {
                    return objectNode;
                }
            };
        }

        @Override
        public FieldUnpickler<JsonNode> unpickler(final JsonNode node) {

            return new FieldUnpicklerBase() {

                @Override
                public <T> T field(String name, JsonNode pf, Pickler<T, JsonNode> pickler) throws IOException {
                    return pickler.unpickle(node.get(name));
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
    public <T extends Enum<T>> Pickler<T, JsonNode> enum_p(final Class<T> enumClass, final T[] values) {

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
    public <T> Pickler<T[], JsonNode> array_p(final Pickler<T, JsonNode> elemPickler, final Class<T> clazz) {

        return new Pickler<T[], JsonNode>() {

            @Override
            public JsonNode pickle(T[] arr, JsonNode unused) throws IOException {

                final ArrayNode result = nodeFactory.arrayNode();

                for (T elem : arr) {
                    result.add(elemPickler.pickle(elem, result));
                }

                return result;
            }

            @Override
            public T[] unpickle(JsonNode source) throws IOException {

                if (!source.isArray())
                    throw new PicklerException("Can not unpickle a " + source.getNodeType() + " into a List");

                final ArrayNode contNode = (ArrayNode)source;

                final T[] result = (T[]) Array.newInstance(clazz, contNode.size());

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
    public <T> Pickler<List<T>, JsonNode> list_p(final Pickler<T, JsonNode> elemPickler) {

        return new Pickler<List<T>, JsonNode>() {

            @Override
            public JsonNode pickle(List<T> list, JsonNode unused) throws IOException {

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

                final List<T> result = Lists.newArrayListWithCapacity(contNode.size());

                for (JsonNode elem : contNode) {
                    result.add(elemPickler.unpickle(elem));
                }

                return result;
            }
        };
    }

    @Override
    public <T> Pickler<Map<String, T>, JsonNode> map_p(final Pickler<T, JsonNode> elemPickler) {

        return new Pickler<Map<String, T>, JsonNode>() {

            @Override
            public JsonNode pickle(Map<String, T> map, JsonNode unused) throws IOException {

                final ObjectNode result = nodeFactory.objectNode();

                for (Map.Entry<String, T> entry : map.entrySet()) {
                    result.put(entry.getKey(), elemPickler.pickle(entry.getValue(), result));
                }

                return result;
            }

            @Override
            public Map<String, T> unpickle(JsonNode source) throws IOException {
                if (!source.isObject())
                    throw new PicklerException("Can not unpickle a " + source.getNodeType() + " into a Map");

                final ObjectNode objectNode = (ObjectNode)source;

                final Map<String, T> result = Maps.newTreeMap();

                for (Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields(); iter.hasNext();) {
                    final Map.Entry<String, JsonNode> entry = iter.next();
                    result.put(entry.getKey(), elemPickler.unpickle(entry.getValue()));
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
