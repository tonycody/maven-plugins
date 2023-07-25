package io.github.tonycody.maven.plugin.sorter.wrapper.operation;

import io.github.tonycody.maven.plugin.sorter.wrapper.content.Wrapper;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * Xml hierarchy operation that detaches a xml child from its parent. Used by Used in
 * HierarchyWrapper.processOperation(HierarchyWrapperOperation operation)
 *
 * @author bjorn
 * @since 2013-11-01
 */
class DetachOperation implements HierarchyWrapperOperation {
    /** Detach each 'other content' */
    @Override
    public void processOtherContent(Wrapper<Node> contentWrapper) {
        contentWrapper.getContent().detach();
    }

    /** Detach each xml element */
    @Override
    public void processElement(Wrapper<Element> elementWrapper) {
        Element content = elementWrapper.getContent();
        content.detach();
        List<Node> contents = new ArrayList<>(content.content());
        contents.forEach(content::remove);
    }
}
