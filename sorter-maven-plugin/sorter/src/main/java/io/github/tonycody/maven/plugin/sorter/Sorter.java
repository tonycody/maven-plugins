package io.github.tonycody.maven.plugin.sorter;

import io.github.tonycody.maven.plugin.sorter.exception.FailureException;
import io.github.tonycody.maven.plugin.sorter.logger.SorterLogger;
import io.github.tonycody.maven.plugin.sorter.output.XmlOutputGenerator;
import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import io.github.tonycody.maven.plugin.sorter.processinstruction.XmlProcessingInstructionParser;
import io.github.tonycody.maven.plugin.sorter.util.FileHelper;
import io.github.tonycody.maven.plugin.sorter.util.XmlOrderedResult;
import io.github.tonycody.maven.plugin.sorter.wrapper.WrapperFactoryImpl;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

/**
 * The implementation of the Mojo (Maven plugin) that sorts the pom file for a maven project.
 *
 * @author Bjorn Ekryd
 */
public class Sorter {
    private final FileHelper fileUtil;
    private final XmlProcessor xmlProcessor;
    private final WrapperFactoryImpl wrapperFactory;
    private final XmlProcessingInstructionParser xmlProcessingInstructionParser;
    private final XmlOutputGenerator xmlOutputGenerator;

    private SorterLogger log;
    private File pomFile;
    private String encoding;
    private boolean ignoreLineSeparators;
    private String violationFilename;
    private boolean backup;

    private String originalXml;
    private String sortedXml;

    /** Instantiates a new sort pom mojo and initiates dependencies to other classes. */
    public Sorter() {
        this.fileUtil = new FileHelper();
        this.wrapperFactory = new WrapperFactoryImpl(fileUtil);
        this.xmlProcessor = new XmlProcessor(wrapperFactory);
        this.xmlProcessingInstructionParser = new XmlProcessingInstructionParser();
        this.xmlOutputGenerator = new XmlOutputGenerator();
    }

    public void initialize(SorterLogger log, PluginParameters pluginParameters) {
        this.log = log;
        fileUtil.setup(pluginParameters);
        wrapperFactory.setup(pluginParameters);
        xmlProcessingInstructionParser.setup(log);
        xmlOutputGenerator.setup(pluginParameters);

        this.pomFile = pluginParameters.originalPom;
        this.encoding = pluginParameters.encoding;
        this.ignoreLineSeparators = pluginParameters.ignoreLineSeparators;
        this.violationFilename = pluginParameters.violationFilename;
        this.backup = pluginParameters.backup;
    }

    /** Fetches and sorts the original xml. */
    void sortOriginalXml() {
        originalXml = fileUtil.getPomFileContent();
        xmlProcessingInstructionParser.scanForIgnoredSections(originalXml);
        String xml = xmlProcessingInstructionParser.replaceIgnoredSections();

        try (ByteArrayInputStream originalXmlInputStream = new ByteArrayInputStream(xml.getBytes(encoding))) {
            xmlProcessor.setOriginalXml(originalXmlInputStream);
        } catch (DocumentException | IOException | SAXException e) {
            throw new FailureException("Could not sort " + pomFile.getAbsolutePath() + " content: " + xml, e);
        }
        xmlProcessor.sortXml();
    }

    /** Generates the sorted XML */
    void generateSortedXml() {
        if (sortedXml != null) {
            return;
        }
        sortedXml = xmlOutputGenerator.getSortedXml(xmlProcessor.getNewDocument());
        if (xmlProcessingInstructionParser.existsIgnoredSections()) {
            sortedXml = xmlProcessingInstructionParser.revertIgnoredSections(sortedXml);
        }
    }

    /** Creates the backup file for pom. */
    void createBackupFile() {
        if (!backup) {
            return;
        }
        fileUtil.backupFile();
        log.info(String.format("Saved backup of %s to %s", pomFile.getAbsolutePath(), pomFile.getAbsolutePath()));
    }

    void saveGeneratedXml() {
        fileUtil.savePomFile(sortedXml);
    }

    XmlOrderedResult isOriginalXmlStringSorted() {
        try (BufferedReader originalXmlReader = new BufferedReader(new StringReader(originalXml));
                BufferedReader sortedXmlReader = new BufferedReader(new StringReader(sortedXml))) {
            String originalXmlLine = originalXmlReader.readLine();
            String sortedXmlLine = sortedXmlReader.readLine();
            int line = 1;

            while (originalXmlLine != null && sortedXmlLine != null) {
                if (!originalXmlLine.equals(sortedXmlLine)) {
                    return XmlOrderedResult.lineDiffers(line, "'" + sortedXmlLine + "'");
                }
                line++;
                originalXmlLine = originalXmlReader.readLine();
                sortedXmlLine = sortedXmlReader.readLine();
            }
            if (originalXmlLine != null || sortedXmlLine != null) {
                return XmlOrderedResult.lineDiffers(line, sortedXmlLine == null ? "empty" : "'" + sortedXmlLine + "'");
            }
        } catch (IOException ioex) {
            throw new FailureException(ioex.getMessage(), ioex);
        }
        if (ignoreLineSeparators || originalXml.equals(sortedXml)) {
            return XmlOrderedResult.ordered();
        }
        return XmlOrderedResult.lineSeparatorCharactersDiffer();
    }

    XmlOrderedResult isOriginalXmlElementsSorted() {
        return xmlProcessor.isXmlOrdered();
    }

    void saveViolationFile(XmlOrderedResult xmlOrderedResult) {
        if (violationFilename != null) {
            log.info("Saving violation report to " + new File(violationFilename).getAbsolutePath());
            ViolationXmlProcessor violationXmlProcessor = new ViolationXmlProcessor();
            Document document =
                    violationXmlProcessor.createViolationXmlContent(pomFile, xmlOrderedResult.getErrorMessage());
            String violationXmlString = xmlOutputGenerator.getSortedXml(document);
            fileUtil.saveViolationFile(violationXmlString);
        }
    }

    /**
     * clean
     *
     */
    void cleanup() {
        fileUtil.cleanup().ifPresent(file -> log.info("Deleted " + file));
    }
}
