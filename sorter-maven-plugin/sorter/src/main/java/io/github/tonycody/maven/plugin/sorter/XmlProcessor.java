package io.github.tonycody.maven.plugin.sorter;

import io.github.tonycody.maven.plugin.sorter.util.XmlOrderedResult;
import io.github.tonycody.maven.plugin.sorter.verify.ElementComparator;
import io.github.tonycody.maven.plugin.sorter.wrapper.operation.HierarchyRootWrapper;
import io.github.tonycody.maven.plugin.sorter.wrapper.operation.WrapperFactory;
import java.io.InputStream;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

/**
 * Creates xml structure and sorts it.
 *
 * @author Bjorn Ekryd
 */
public class XmlProcessor {
    // https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html
    public static final String DISALLOW_DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";
    private final WrapperFactory factory;

    private Document originalDocument;
    private Document newDocument;

    public XmlProcessor(WrapperFactory factory) {
        this.factory = factory;
    }

    /**
     * Sets the original xml that should be sorted. Builds a dom document of the xml.
     *
     * @param originalXml the new original xml
     */
    public void setOriginalXml(final InputStream originalXml) throws DocumentException, SAXException {
        SAXReader parser = new SAXReader();
        parser.setFeature(DISALLOW_DOCTYPE_DECL, true);
        parser.setMergeAdjacentText(true);
        originalDocument = parser.read(originalXml);
    }

    /** Creates a new dom document that contains the sorted xml. */
    public void sortXml() {
        newDocument = (Document) originalDocument.clone();
        final Element rootElement = originalDocument.getRootElement().createCopy();

        HierarchyRootWrapper rootWrapper = factory.createFromRootElement(rootElement);

        rootWrapper.createWrappedStructure(factory);
        rootWrapper.detachStructure();
        rootWrapper.sortStructureAttributes();
        rootWrapper.sortStructureElements();
        rootWrapper.connectXmlStructure();

        replaceRootElementInNewDocument(rootWrapper.getElementContent().getContent());
    }

    /** Setting root element directly on the document will clear other content */
    private void replaceRootElementInNewDocument(Element newElement) {
        Element rootElement = newDocument.getRootElement();
        List<Node> content = newDocument.content();

        newDocument.clearContent();

        for (Node node : content) {
            if (node == rootElement) {
                newDocument.add(newElement);
            } else {
                newDocument.add(node);
            }
        }
    }

    public Document getNewDocument() {
        return newDocument;
    }

    public XmlOrderedResult isXmlOrdered() {
        ElementComparator elementComparator =
                new ElementComparator(originalDocument.getRootElement(), newDocument.getRootElement());
        return elementComparator.isElementOrdered();
    }
}
