package org.javapickling.xml;

import org.javapickling.core.MetaType;
import org.javapickling.core.PicklerBase;
import org.javapickling.core.PicklerCore;
import org.javapickling.core.PicklerException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DynamicObjectXmlNodePickler<T> extends PicklerBase<T, Node> {

    private final XmlNodePicklerCore xmlCore = (XmlNodePicklerCore)core;

    private final String typeName = "type";
    private final String className = "class";

    public DynamicObjectXmlNodePickler(PicklerCore<Node> core, Class<? super T> clazz) {
        super(core, clazz);
    }

    @Override
    public Node pickle(T obj, Node target) throws Exception {

        final Element targetElem = (Element)target;

        final MetaType metaType = MetaType.ofObject(obj);

        xmlCore.addAttribute(targetElem, typeName, metaType.name());

        if (metaType.typeKind == MetaType.TypeKind.ENUM || metaType.typeKind == MetaType.TypeKind.OBJECT) {
            xmlCore.addAttribute(targetElem, className, core.classToName(metaType.clazz));
        }

        if (metaType.typeKind != MetaType.TypeKind.NULL) {
            metaType.pickler(core).pickle(obj, target);
        }

        return target;
    }

    @Override
    public T unpickle(Node source) throws Exception {

        final Element sourceElem = (Element)source;

        MetaType metaType = MetaType.ofName(xmlCore.getChildAttrValue(sourceElem, typeName));

        if (metaType.typeKind == MetaType.TypeKind.ENUM || metaType.typeKind == MetaType.TypeKind.OBJECT) {
            try {
                final Class<?> clazz = core.nameToClass(xmlCore.getChildAttrValue(sourceElem, className));
                metaType = new MetaType(metaType.typeKind, clazz, metaType.arrayDepth);
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
