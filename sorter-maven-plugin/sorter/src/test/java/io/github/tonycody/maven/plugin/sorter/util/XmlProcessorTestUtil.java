package io.github.tonycody.maven.plugin.sorter.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.tonycody.maven.plugin.sorter.XmlProcessor;
import io.github.tonycody.maven.plugin.sorter.output.XmlOutputGenerator;
import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import io.github.tonycody.maven.plugin.sorter.wrapper.WrapperFactoryImpl;
import io.github.tonycody.maven.plugin.sorter.wrapper.content.AlphabeticalSortedWrapper;
import io.github.tonycody.maven.plugin.sorter.wrapper.content.UnsortedWrapper;
import io.github.tonycody.maven.plugin.sorter.wrapper.content.Wrapper;
import io.github.tonycody.maven.plugin.sorter.wrapper.operation.HierarchyRootWrapper;
import io.github.tonycody.maven.plugin.sorter.wrapper.operation.WrapperFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import org.dom4j.Element;
import org.dom4j.Node;
import refutils.ReflectionHelper;

/** Test utility */
public class XmlProcessorTestUtil {
    private boolean sortAlphabeticalOnly = false;
    private boolean keepBlankLines = false;
    private boolean indentBlankLines = false;
    private String predefinedSortOrder = "recommended_2008_06";
    private boolean expandEmptyElements = true;
    private String lineSeparator = "\r\n";

    private XmlProcessor xmlProcessor;
    private XmlOutputGenerator xmlOutputGenerator;
    private boolean spaceBeforeCloseEmptyElement = true;
    private boolean sortModules = false;
    private String sortDependencies;
    private String sortDependencyManagement;
    private String sortPlugins;
    private boolean sortProperties = false;

    private XmlProcessorTestUtil() {}

    public static XmlProcessorTestUtil create() {
        return new XmlProcessorTestUtil();
    }

    public void testInputAndExpected(final String inputFileName, final String expectedFileName) throws Exception {
        String actual = sortXmlAndReturnResult(inputFileName);

        final String expected =
                new String(new FileInputStream(expectedFileName).readAllBytes(), StandardCharsets.UTF_8);

        assertEquals(expected, actual);
    }

    public String sortXmlAndReturnResult(String inputFileName) throws Exception {
        setup(inputFileName);
        xmlProcessor.sortXml();
        return xmlOutputGenerator.getSortedXml(xmlProcessor.getNewDocument());
    }

    public void testVerifyXmlIsOrdered(final String inputFileName) throws Exception {
        setup(inputFileName);
        xmlProcessor.sortXml();
        assertTrue(xmlProcessor.isXmlOrdered().isOrdered());
    }

    public void testVerifyXmlIsNotOrdered(final String inputFileName, String infoMessage) throws Exception {
        setup(inputFileName);
        xmlProcessor.sortXml();
        XmlOrderedResult xmlOrdered = xmlProcessor.isXmlOrdered();
        assertFalse(xmlOrdered.isOrdered());
        assertEquals(infoMessage, xmlOrdered.getErrorMessage());
    }

    private void setup(String inputFileName) throws Exception {
        PluginParameters pluginParameters = PluginParameters.builder()
                .setOriginalPom(null)
                .setFileOutput(false, new File(""), null, false)
                .setEncoding("UTF-8")
                .setFormatting(lineSeparator, expandEmptyElements, spaceBeforeCloseEmptyElement, keepBlankLines)
                .setIndent(2, indentBlankLines, false)
                .setSortOrder(predefinedSortOrder + ".xml", null)
                .setSortEntities(
                        sortDependencies, "", sortDependencyManagement, sortPlugins, sortProperties, sortModules, false)
                .build();
        final String xml = new String(new FileInputStream(inputFileName).readAllBytes(), StandardCharsets.UTF_8);

        final FileHelper fileUtil = new FileHelper();
        fileUtil.setup(pluginParameters);

        WrapperFactory wrapperFactory = new WrapperFactoryImpl(fileUtil);
        ((WrapperFactoryImpl) wrapperFactory).setup(pluginParameters);

        xmlProcessor = new XmlProcessor(wrapperFactory);

        xmlOutputGenerator = new XmlOutputGenerator();
        xmlOutputGenerator.setup(pluginParameters);

        if (sortAlphabeticalOnly) {
            wrapperFactory = new WrapperFactory() {

                @Override
                public HierarchyRootWrapper createFromRootElement(final Element rootElement) {
                    return new HierarchyRootWrapper(new AlphabeticalSortedWrapper(rootElement));
                }

                @SuppressWarnings("unchecked")
                @Override
                public <T extends Node> Wrapper<T> create(final T content) {
                    if (content instanceof Element) {
                        Element element = (Element) content;
                        return (Wrapper<T>) new AlphabeticalSortedWrapper(element);
                    }
                    return new UnsortedWrapper<>(content);
                }
            };
        } else {
            new ReflectionHelper(wrapperFactory).setField(fileUtil);
        }
        new ReflectionHelper(xmlProcessor).setField(wrapperFactory);
        xmlProcessor.setOriginalXml(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    public XmlProcessorTestUtil sortAlphabeticalOnly() {
        sortAlphabeticalOnly = true;
        return this;
    }

    public XmlProcessorTestUtil keepBlankLines() {
        keepBlankLines = true;
        return this;
    }

    public XmlProcessorTestUtil lineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
        return this;
    }

    public XmlProcessorTestUtil indentBlankLines() {
        indentBlankLines = true;
        return this;
    }

    public XmlProcessorTestUtil predefinedSortOrder(String predefinedSortOrder) {
        this.predefinedSortOrder = predefinedSortOrder;
        return this;
    }

    public XmlProcessorTestUtil sortModules() {
        this.sortModules = true;
        return this;
    }

    public XmlProcessorTestUtil sortDependencies(String sortDependencies) {
        this.sortDependencies = sortDependencies;
        return this;
    }

    public XmlProcessorTestUtil sortDependencyManagement(String sortDependencyManagement) {
        this.sortDependencyManagement = sortDependencyManagement;
        return this;
    }

    public XmlProcessorTestUtil sortPlugins(String sortPlugins) {
        this.sortPlugins = sortPlugins;
        return this;
    }

    public XmlProcessorTestUtil noSpaceBeforeCloseEmptyElement() {
        this.spaceBeforeCloseEmptyElement = false;
        return this;
    }

    public XmlProcessorTestUtil sortProperties() {
        this.sortProperties = true;
        return this;
    }

    public XmlProcessorTestUtil expandEmptyElements(boolean expand) {
        this.expandEmptyElements = expand;
        return this;
    }
}
