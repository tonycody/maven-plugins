package io.github.tonycody.maven.plugin.sorter.wrapper.operation;

import io.github.tonycody.maven.plugin.sorter.wrapper.content.Wrapper;
import org.dom4j.Element;

/**
 * Exposes the methods that are available to the root of the wrapper hierarchy. This also means that
 * we always have an element as root element.
 */
public class HierarchyRootWrapper extends HierarchyWrapper {
    public HierarchyRootWrapper(Wrapper<Element> wrapper) {
        super(wrapper);
    }

    @Override
    public void createWrappedStructure(WrapperFactory factory) {
        super.createWrappedStructure(factory);
    }

    /** Sorts the attributes of the xml elements */
    public void sortStructureAttributes() {
        processOperation(new SortAttributesOperation());
    }

    /** Sorts all xml elements */
    public void sortStructureElements() {
        processOperation(new SortChildrenOperation());
    }

    /** Creates a fresh xml structure */
    public void connectXmlStructure() {
        processOperation(new GetContentStructureOperation());
    }

    /** Detaches all xml elements */
    public void detachStructure() {
        processOperation(new DetachOperation());
    }

    @Override
    public Wrapper<Element> getElementContent() {
        return super.getElementContent();
    }
}
