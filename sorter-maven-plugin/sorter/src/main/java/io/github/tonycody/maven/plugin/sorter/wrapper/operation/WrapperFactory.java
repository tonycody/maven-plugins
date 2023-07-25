package io.github.tonycody.maven.plugin.sorter.wrapper.operation;

import io.github.tonycody.maven.plugin.sorter.wrapper.content.Wrapper;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * Creates wrappers around xml fragments.
 *
 * @author Bjorn Ekryd
 */
public interface WrapperFactory {

    /** Creates wrapper around a root element. */
    HierarchyRootWrapper createFromRootElement(final Element rootElement);

    /** Creates wrapper around xml content. */
    <T extends Node> Wrapper<T> create(final T content);
}
