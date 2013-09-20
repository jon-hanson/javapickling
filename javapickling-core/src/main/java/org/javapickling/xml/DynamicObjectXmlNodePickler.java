package org.javapickling.xml;

import org.javapickling.core.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DynamicObjectXmlNodePickler<T> extends PicklerBase<T, Node> {

    private final XmlNodePicklerCore xmlCore = (XmlNodePicklerCore)core;

    private final String typeName = "type";
    private final String className = "class";

    public DynamicObjectXmlNodePickler(PicklerCore<Node> core) {
        super(core);
    }

    @Override
    public Node pickle(T obj, Node target) throws Exception {

        final Element targetElem = (Element)target;

        final MetaType metaType = MetaType.ofObject(obj);

        xmlCore.addAttribute(targetElem, typeName, metaType.name());

        if (metaType.typeKind == MetaType.TypeKind.ENUM || metaType.typeKind == MetaType.TypeKind.OBJECT) {
            xmlCore.addAttribute(targetElem, className, metaType.clazz.getName());
        }

        if (metaType.typeKind != MetaType.TypeKind.NULL) {
            metaType.pickler(core).pickle(obj, target);
        }

        return target;
    }

    @Override
    public T unpickle(Node source) throws Exception {

        final Element sourceElem = (Element)source;

        final MetaType metaType = MetaType.ofName(xmlCore.getChildAttrValue(sourceElem, typeName));

        if (metaType.typeKind == MetaType.TypeKind.ENUM || metaType.typeKind == MetaType.TypeKind.OBJECT) {
            try {
                metaType.clazz = Class.forName(xmlCore.getChildAttrValue(sourceElem, className));
            } catch (ClassNotFoundException ex) {
                throw new PicklerException("Can not construct class", ex);
            }
        }

        if (metaType.typeKind != MetaType.TypeKind.NULL) {
            return (T)metaType.pickler(core).unpickle(source);
        } else {
            return null;
        }
    }
}