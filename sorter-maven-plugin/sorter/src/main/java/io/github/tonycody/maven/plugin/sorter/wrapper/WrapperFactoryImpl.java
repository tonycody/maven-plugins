package io.github.tonycody.maven.plugin.sorter.wrapper;

import static io.github.tonycody.maven.plugin.sorter.XmlProcessor.DISALLOW_DOCTYPE_DECL;

import io.github.tonycody.maven.plugin.sorter.content.IgnoreSectionToken;
import io.github.tonycody.maven.plugin.sorter.exception.FailureException;
import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import io.github.tonycody.maven.plugin.sorter.util.FileHelper;
import io.github.tonycody.maven.plugin.sorter.wrapper.content.UnsortedWrapper;
import io.github.tonycody.maven.plugin.sorter.wrapper.content.Wrapper;
import io.github.tonycody.maven.plugin.sorter.wrapper.operation.HierarchyRootWrapper;
import io.github.tonycody.maven.plugin.sorter.wrapper.operation.WrapperFactory;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

/**
 * Concrete implementation of a wrapper factory that sorts xml according to sort order from
 * fileUtil.
 *
 * <p>Thank you Christian Haelg for your sortProperties patch.
 *
 * @author Bjorn Ekryd
 */
public class WrapperFactoryImpl implements WrapperFactory {

    /** How much the sort order index should increase for each element type */
    private static final int SORT_ORDER_INCREMENT = 100;

    /** Start value for sort order index. */
    private static final int SORT_ORDER_BASE = 1000;

    private final FileHelper fileUtil;

    private final ElementSortOrderMap elementSortOrderMap = new ElementSortOrderMap();
    private final ElementWrapperCreator elementWrapperCreator = new ElementWrapperCreator(elementSortOrderMap);
    private final TextWrapperCreator textWrapperCreator = new TextWrapperCreator();

    /**
     * Instantiates a new wrapper factory impl.
     *
     * @param fileUtil the file util
     */
    public WrapperFactoryImpl(final FileHelper fileUtil) {
        this.fileUtil = fileUtil;
    }

    /** Initializes the class with sortpom parameters. */
    public void setup(PluginParameters pluginParameters) {
        elementWrapperCreator.setup(pluginParameters);
        textWrapperCreator.setup(pluginParameters);
    }

    /**
     * @see WrapperFactory#createFromRootElement(org.dom4j.Element)
     */
    public HierarchyRootWrapper createFromRootElement(final Element rootElement) {
        initializeSortOrderMap();
        return new HierarchyRootWrapper(create(rootElement));
    }

    /** Creates sort order map from chosen sort order. */
    private void initializeSortOrderMap() {
        try {
            Document document = createDocumentFromDefaultSortOrderFile();
            addElementsToSortOrderMap(document.getRootElement(), SORT_ORDER_BASE);
        } catch (IOException | DocumentException | SAXException e) {
            throw new FailureException(e.getMessage(), e);
        }
    }

    Document createDocumentFromDefaultSortOrderFile() throws IOException, DocumentException, SAXException {
        try (Reader reader = new StringReader(fileUtil.getDefaultSortOrderXml())) {
            SAXReader parser = new SAXReader();
            parser.setFeature(DISALLOW_DOCTYPE_DECL, true);
            return parser.read(reader);
        }
    }

    /** Processes the chosen sort order. Adds sort order element and sort index to a map. */
    private void addElementsToSortOrderMap(final Element element, int baseSortOrder) {
        elementSortOrderMap.addElement(element, baseSortOrder);
        final List<Element> castToChildElementList = element.elements();
        // Increments the sort order index for each element
        int sortOrder = baseSortOrder;
        for (Element child : castToChildElementList) {
            sortOrder += SORT_ORDER_INCREMENT;
            addElementsToSortOrderMap(child, sortOrder);
        }
    }

    /**
     * @see WrapperFactory#create(org.dom4j.Node)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends Node> Wrapper<T> create(final T content) {
        if (content instanceof Element) {
            return (Wrapper<T>) elementWrapperCreator.createWrapper((Element) content);
        }
        if (content instanceof Comment) {
            return new UnsortedWrapper<>(content);
        }
        if (content instanceof Text) {
            return (Wrapper<T>) textWrapperCreator.createWrapper((Text) content);
        }
        if (content instanceof ProcessingInstruction && "sortpom".equals(content.getName())) {
            return (Wrapper<T>) new UnsortedWrapper<>(IgnoreSectionToken.from((ProcessingInstruction) content));
        }
        return new UnsortedWrapper<>(content);
    }
}
