package io.github.tonycody.maven.plugin.sorter;

import java.io.File;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;
import org.dom4j.tree.DefaultDocument;

/**
 * Used to store an external violation file
 *
 * @author bjorn
 * @since 2017-03-24
 */
class ViolationXmlProcessor {
    Document createViolationXmlContent(File pomFileLocation, String violationMessage) {
        Element violationElement = new BaseElement("violation");
        violationElement.setText(violationMessage);

        BaseElement fileElement = new BaseElement("file");
        fileElement.add(violationElement);
        fileElement.addAttribute("filename", pomFileLocation.getAbsolutePath());

        Element rootElement = new BaseElement("sortpom");
        rootElement.add(fileElement);

        return new DefaultDocument(rootElement);
    }
}
