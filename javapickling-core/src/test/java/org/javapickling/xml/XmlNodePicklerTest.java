package org.javapickling.xml;

import org.javapickling.common.ComplexClass;
import org.javapickling.common.RoundTrip;
import org.javapickling.core.Pickler;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlNodePicklerTest {

    private static final XmlNodePicklerCore xmlPickler = XmlNodePicklerCore.create();

    @Test
    public void testPickle() throws Exception {

        final ComplexClass simple = ComplexClass.createInstance(true);

        final RoundTrip jsonTimeMs = roundTripViaJson(simple);
        System.out.println(jsonTimeMs);

        org.javapickling.common.Utils.roundTripViaJavaSer(simple);
        final RoundTrip javaSerTimeMs = org.javapickling.common.Utils.roundTripViaJavaSer(simple);
        System.out.println(javaSerTimeMs);
    }

    private static RoundTrip roundTripViaJson(ComplexClass complex) throws Exception {

        final Pickler<ComplexClass, Node> pickler = xmlPickler.object_p(ComplexClass.class);

        final Element rootNode = xmlPickler.doc.createElement("complex");
        xmlPickler.doc.appendChild(rootNode);
        final long startTime1 = System.nanoTime();
        final Node node = pickler.pickle(complex, rootNode);
        final long endTime1 = System.nanoTime();

        final String xml = XmlNodePicklerCore.nodeToString(xmlPickler.doc, true);
        System.out.println("XML=");
        System.out.println(xml);

        final int size = xml.length();

        final long startTime2 = System.nanoTime();
        final ComplexClass complex2 = pickler.unpickle(node);
        final long endTime2 = System.nanoTime();

        Assert.assertEquals(complex, complex2);

        return new RoundTrip("XmlNodePickler", endTime1 - startTime1, endTime2 - startTime2, size);
    }
}
