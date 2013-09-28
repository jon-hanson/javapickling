package org.javapickling.xml;

import org.javapickling.core.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * PicklerCore implementation which pickles objects to a DOM Document.
 */
public class XmlNodePicklerCore extends PicklerCoreBase<Node> {

    public static XmlNodePicklerCore create() {
        final XmlNodePicklerCore core = new XmlNodePicklerCore();
        core.initialise();
        return core;
    }

    public static XmlNodePicklerCore create(DocumentBuilder docBuilder) {
        final XmlNodePicklerCore core = new XmlNodePicklerCore(docBuilder);
        core.initialise();
        return core;
    }

    public static String nodeToString(Document xml, boolean pretty) throws Exception {

        final Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        if (pretty) {
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        }

        final Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        return out.toString();
    }

    private static DocumentBuilder createDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new PicklerException("Failed to construct DocumentBuilder", ex);
        }
    }

    protected static Element getChildElement(Node parent, String name)
    {
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Element && name.equals(child.getNodeName()))
                return (Element)child;
        }
        return null;
    }

    protected static Text getChildText(Node parent) {
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Text)
                return (Text)child;
        }
        return null;
    }

    protected static String getChildAttrValue(Element parent, String name) {
        return parent.getAttribute(name);
    }

    protected static void addAttribute(Element parent, String name, String value) {
        parent.setAttribute(name, value);
    }

    public DocumentBuilder docBuilder;

    public Document doc;

    private XmlNodePicklerCore() {
        this(createDocumentBuilder());
    }

    private XmlNodePicklerCore(DocumentBuilder docBuilder) {
        this.docBuilder = docBuilder;
        this.doc = docBuilder.newDocument();
    }

    public void setDocumentBuilder(DocumentBuilder docBuilder) {
        this.docBuilder = docBuilder;
    }

    public void setDocument(Document doc) {
        this.doc = doc;
    }

    protected final Pickler<Object, Node> nullP = new Pickler<Object, Node>() {

        @Override
        public Node pickle(Object obj, Node target) throws Exception {
            return target.appendChild(doc.createTextNode(""));
        }

        @Override
        public Object unpickle(Node source) throws Exception {
            return null;
        }
    };

    protected final Pickler<Boolean, Node> booleanP = new Pickler<Boolean, Node>() {

        @Override
        public Node pickle(Boolean b, Node target) throws Exception {
            return target.appendChild(doc.createTextNode(b.toString()));
        }

        @Override
        public Boolean unpickle(Node source) throws Exception {
            final Text text = getChildText(source);
            return Boolean.parseBoolean(text.getWholeText());
        }
    };

    protected final Pickler<Byte, Node> byteP = new Pickler<Byte, Node>() {

        @Override
        public Node pickle(Byte b, Node target) throws Exception {
            return target.appendChild(doc.createTextNode(b.toString()));
        }

        @Override
        public Byte unpickle(Node source) throws Exception {
            final Text text = getChildText(source);
            return Byte.valueOf(text.getWholeText());
        }
    };

    protected final Pickler<Character, Node> charP = new Pickler<Character, Node>() {

        @Override
        public Node pickle(Character c, Node target) throws Exception {
            return target.appendChild(doc.createTextNode(c.toString()));
        }

        @Override
        public Character unpickle(Node source) throws Exception {
            final Text text = getChildText(source);
            return Character.valueOf(text.getWholeText().charAt(0));
        }
    };

    protected final Pickler<String, Node> stringP = new Pickler<String, Node>() {

        @Override
        public Node pickle(String s, Node target) throws Exception {
            return target.appendChild(doc.createTextNode(s));
        }

        @Override
        public String unpickle(Node source) throws Exception {
            final Text text = getChildText(source);
            return text.getWholeText();
        }
    };

    protected final Pickler<Integer, Node> integerP = new Pickler<Integer, Node>() {

        @Override
        public Node pickle(Integer i, Node target) throws Exception {
            return target.appendChild(doc.createTextNode(i.toString()));
        }

        @Override
        public Integer unpickle(Node source) throws Exception {
            final Text text = getChildText(source);
            return Integer.valueOf(text.getWholeText());
        }
    };

    protected final Pickler<Short, Node> shortP = new Pickler<Short, Node>() {

        @Override
        public Node pickle(Short s, Node target) throws Exception {
            return target.appendChild(doc.createTextNode(s.toString()));
        }

        @Override
        public Short unpickle(Node source) throws Exception {
            final Text text = getChildText(source);
            return Short.valueOf(text.getWholeText());
        }
    };

    protected final Pickler<Long, Node> longP = new Pickler<Long, Node>() {

        @Override
        public Node pickle(Long l, Node target) throws Exception {
            return target.appendChild(doc.createTextNode(l.toString()));
        }

        @Override
        public Long unpickle(Node source) throws Exception {
            final Text text = getChildText(source);
            return Long.valueOf(text.getWholeText());
        }
    };

    protected final Pickler<Float, Node> floatP = new Pickler<Float, Node>() {

        @Override
        public Node pickle(Float f, Node target) throws Exception {
            return target.appendChild(doc.createTextNode(f.toString()));
        }

        @Override
        public Float unpickle(Node source) throws Exception {
            final Text text = getChildText(source);
            return Float.valueOf(text.getWholeText());
        }
    };

    protected final Pickler<Double, Node> doubleP = new Pickler<Double, Node>() {

        @Override
        public Node pickle(Double d, Node target) throws Exception {
            return target.appendChild(doc.createTextNode(d.toString()));
        }

        @Override
        public Double unpickle(Node source) throws Exception {
            final Text text = getChildText(source);
            return Double.valueOf(text.getWholeText());
        }
    };

    protected final ObjectMapPickler<Node> objectMapP = new ObjectMapPickler<Node>() {

        @Override
        public FieldPickler<Node> pickler(final Node target) {

            return new AbstractFieldPickler(target) {

                private final Element elem = (Element)target;

                @Override
                public <T> void field(String name, T value, Pickler<T, Node> pickler) throws Exception {
                    final Element node = doc.createElement(name);
                    if (pickler.pickle(value, node) != null) {
                        target.appendChild(node);
                    }
                }


                @Override
                public Node pickle(Node source) {
                    return elem;
                }
            };
        }

        @Override
        public FieldUnpickler<Node> unpickler(final Node source) {

            return new AbstractFieldUnpickler(source) {

                private final Element elem = (Element)source;

                @Override
                public <T> T field(String name, Pickler<T, Node> pickler) throws Exception {
                    return pickler.unpickle(getChildElement(elem, name));
                }
            };
        }
    };

    protected final Pickler<Object, Node> dynObjectP = new DynamicObjectXmlNodePickler<Object>(this);

    @Override
    public Pickler<Object, Node> null_p() {
        return nullP;
    }

    @Override
    public Pickler<Boolean, Node> boolean_p() {
        return booleanP;
    }

    @Override
    public Pickler<Byte, Node> byte_p() {
        return byteP;
    }

    @Override
    public Pickler<Character, Node> char_p() {
        return charP;
    }

    @Override
    public Pickler<String, Node> string_p() {
        return stringP;
    }

    @Override
    public Pickler<Integer, Node> integer_p() {
        return integerP;
    }

    @Override
    public Pickler<Short, Node> short_p() {
        return shortP;
    }

    @Override
    public Pickler<Long, Node> long_p() {
        return longP;
    }

    @Override
    public Pickler<Float, Node> float_p() {
        return floatP;
    }

    @Override
    public Pickler<Double, Node> double_p() {
        return doubleP;
    }

    @Override
    public <T extends Enum<T>> Pickler<T, Node> enum_p(final Class<T> enumClass) {

        return new Pickler<T, Node>() {

            @Override
            public Node pickle(T t, Node target) throws Exception {
                return target.appendChild(doc.createTextNode(t.name()));
            }

            @Override
            public T unpickle(Node source) throws Exception {
                final Text text = getChildText(source);
                return T.valueOf(enumClass, text.getWholeText());
            }
        };
    }

    @Override
    public <T> Pickler<T[], Node> array_p(
            final Pickler<T, Node> elemPickler,
            final Class<T> elemClass) {

        return new Pickler<T[], Node>() {

            @Override
            public Node pickle(T[] arr, Node target) throws Exception {

                for (int i = 0; i < arr.length; ++i) {
                    final Node node = doc.createElement("_" + Integer.toString(i));
                    elemPickler.pickle(arr[i], node);
                    target.appendChild(node);
                }

                return target;
            }

            @Override
            public T[] unpickle(Node source) throws Exception {

                final NodeList nodes = source.getChildNodes();
                final int l = nodes.getLength();
                final T[] result = (T[]) Array.newInstance(elemClass, l);

                for (int i = 0; i < l; ++i) {
                    result[i] = elemPickler.unpickle(nodes.item(i));
                }

                return result;
            }
        };
    }

    @Override
    public <T> Pickler<List<T>, Node> list_p(
            final Pickler<T, Node> elemPickler,
            final Class<? extends List> listClass) {

        return new Pickler<List<T>, Node>() {

            @Override
            public Node pickle(List<T> list, Node target) throws Exception {

                int i = 0;
                for (T elem : list) {
                    final Node node = doc.createElement("_" + Integer.toString(i));
                    elemPickler.pickle(elem, node);
                    target.appendChild(node);
                    ++i;
                }

                return target;
            }

            @Override
            public List<T> unpickle(Node source) throws Exception {

                final NodeList nodes = source.getChildNodes();
                final int l = nodes.getLength();
                final List<T> result = newInstance(listClass);

                for (int i = 0; i < l; ++i) {
                    result.add(elemPickler.unpickle(nodes.item(i)));
                }

                return result;
            }
        };
    }

    @Override
    public <T> Pickler<Map<String, T>, Node> map_p(
            final Pickler<T, Node> valuePickler,
            final Class<? extends Map> mapClass) {

        return new Pickler<Map<String, T>, Node>() {

            @Override
            public Node pickle(Map<String, T> map, Node target) throws Exception {

                for (Map.Entry<String, T> entry : map.entrySet()) {
                    final Node node = doc.createElement(entry.getKey());
                    valuePickler.pickle(entry.getValue(), node);
                    target.appendChild(node);
                }

                return target;
            }

            @Override
            public Map<String, T> unpickle(Node source) throws Exception {

                final NodeList nodes = source.getChildNodes();
                final int l = nodes.getLength();
                final Map<String, T> result = newInstance(mapClass);

                for (int i = 0; i < l; ++i) {
                    final Node node = nodes.item(i);
                    result.put(node.getNodeName(), valuePickler.unpickle(nodes.item(i)));
                }

                return result;
            }
        };
    }

    @Override
    public <K, V> Pickler<Map<K, V>, Node> map_p(
            final Pickler<K, Node> keyPickler,
            final Pickler<V, Node> valuePickler,
            final Class<?  extends Map> mapClass) {

        return new Pickler<Map<K, V>, Node>() {

            private static final String keyF = "key";
            private static final String valueF = "value";

            @Override
            public Node pickle(Map<K, V> map, Node target) throws Exception {

                int i = 0;
                for (Map.Entry<K, V> entry : map.entrySet()) {
                    final Node node = doc.createElement("_" + Integer.toString(i));

                    final Node keyNode = doc.createElement(keyF);
                    keyPickler.pickle(entry.getKey(), keyNode);
                    node.appendChild(keyNode);

                    final Node valueNode = doc.createElement(valueF);
                    valuePickler.pickle(entry.getValue(), valueNode);
                    node.appendChild(valueNode);

                    target.appendChild(node);
                    ++i;
                }

                return target;
            }

            @Override
            public Map<K, V> unpickle(Node source) throws Exception {

                final NodeList nodes = source.getChildNodes();
                final int l = nodes.getLength();

                final Map<K, V> result;
                try {
                    result = mapClass.newInstance();
                } catch (InstantiationException ex) {
                    throw new PicklerException("Can not create map class", ex);
                } catch (IllegalAccessException ex) {
                    throw new PicklerException("Can not create map class", ex);
                }

                for (int i = 0; i < l; ++i) {
                    final Element node = (Element)nodes.item(i);
                    final K key = keyPickler.unpickle(getChildElement(node, keyF));
                    final V value = valuePickler.unpickle(getChildElement(node, valueF));
                    result.put(key, value);
                }

                return result;
            }
        };
    }

    @Override
    public <T> Pickler<Set<T>, Node> set_p(
            final Pickler<T, Node> elemPickler,
            final Class<? extends Set> setClass) {

        return new Pickler<Set<T>, Node>() {

            @Override
            public Node pickle(Set<T> set, Node target) throws Exception {

                int i = 0;
                for (T elem : set) {
                    final Node node = doc.createElement("_" + Integer.toString(i));
                    elemPickler.pickle(elem, node);
                    target.appendChild(node);
                    ++i;
                }

                return target;
            }

            @Override
            public Set<T> unpickle(Node source) throws Exception {

                final NodeList nodes = source.getChildNodes();
                final int l = nodes.getLength();
                final Set<T> result = newInstance(setClass);

                for (int i = 0; i < l; ++i) {
                    result.add(elemPickler.unpickle(nodes.item(i)));
                }

                return result;
            }
        };
    }

    @Override
    public Pickler<Object, Node> d_object_p() {
        return dynObjectP;
    }

    @Override
    public <T, S extends T> Pickler<S, Node> d_object_p(Class<T> clazz) {
        return new DynamicObjectXmlNodePickler<S>(this);
    }

    @Override
    public ObjectMapPickler<Node> object_map() {
        return objectMapP;
    }

    @Override
    public <T> Pickler<T, Node> nullable(final Pickler<T, Node> pickler) {
        return new Pickler<T, Node>() {

            @Override
            public Node pickle(T t, Node target) throws Exception {
                if (t == null) {
                    return null;
                } else {
                    return pickler.pickle(t, target);
                }
            }

            @Override
            public T unpickle(Node source) throws Exception {
                if (source == null) {
                    return null;
                } else {
                    return pickler.unpickle(source);
                }
            }
        };
    }
}
