package io.github.tonycody.maven.plugin.sorter.output;

import io.github.tonycody.maven.plugin.sorter.exception.FailureException;
import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import io.github.tonycody.maven.plugin.sorter.util.StringLineSeparatorWriter;
import java.io.IOException;
import java.io.StringWriter;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/** Handles all generation of xml. */
public class XmlOutputGenerator {

    private String encoding;
    private String indentCharacters;
    private boolean expandEmptyElements;
    private boolean indentBlankLines;
    private boolean indentSchemaLocation;
    private boolean spaceBeforeCloseEmptyElement;
    private String lineSeparator;

    /** Setup default configuration */
    public void setup(PluginParameters pluginParameters) {
        this.indentCharacters = pluginParameters.indentCharacters;
        this.encoding = pluginParameters.encoding;
        this.expandEmptyElements = pluginParameters.expandEmptyElements;
        this.indentBlankLines = pluginParameters.indentBlankLines;
        this.indentSchemaLocation = pluginParameters.indentSchemaLocation;
        this.spaceBeforeCloseEmptyElement = pluginParameters.spaceBeforeCloseEmptyElement;
        lineSeparator = pluginParameters.lineSeparatorUtil.toString();
    }

    /**
     * Returns the sorted xml as an OutputStream.
     *
     * @return the sorted xml
     */
    public String getSortedXml(Document newDocument) {
        try (StringWriter out = new StringWriter();
                StringLineSeparatorWriter writer = new StringLineSeparatorWriter(out, lineSeparator)) {
            XMLWriter xmlWriter = new PatchedXMLWriter(
                    writer, createPrettyFormat(), spaceBeforeCloseEmptyElement, indentBlankLines, indentSchemaLocation);
            xmlWriter.write(newDocument);
            writer.writeDelayedNewline();
            return writer.toString();
        } catch (IOException ioex) {
            throw new FailureException("Could not format pom files content", ioex);
        }
    }

    private OutputFormat createPrettyFormat() {
        OutputFormat outputFormat = new OutputFormat(indentCharacters);
        outputFormat.setNewlines(true);
        outputFormat.setExpandEmptyElements(expandEmptyElements);
        outputFormat.setNewLineAfterDeclaration(false);
        outputFormat.setEncoding(encoding);
        return outputFormat;
    }
}
