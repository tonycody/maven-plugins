package io.github.tonycody.maven.plugin.sorter;

import io.github.tonycody.maven.plugin.sorter.exception.FailureException;
import io.github.tonycody.maven.plugin.sorter.logger.SorterLogger;
import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import io.github.tonycody.maven.plugin.sorter.parameter.VerifyFailOnType;
import io.github.tonycody.maven.plugin.sorter.parameter.VerifyFailType;
import io.github.tonycody.maven.plugin.sorter.util.XmlOrderedResult;
import java.io.File;

/** The implementation of the Mojo (Maven plugin) that sorts the pom file for a Maven project. */
public class SorterHandler {
    private static final String TEXT_FILE_NOT_SORTED = "The file %s is not sorted";
    private final Sorter sorter;
    private SorterLogger log;
    private File pomFile;
    private VerifyFailType verifyFailType;
    private VerifyFailOnType verifyFailOn;

    public SorterHandler() {
        this.sorter = new Sorter();
    }

    /**
     * 获得实例
     *
     * @return {@link SorterHandler}
     */
    public static SorterHandler getInstance() {
        return new SorterHandler();
    }

    public void initialize(SorterLogger log, PluginParameters pluginParameters) {
        this.log = log;
        this.pomFile = pluginParameters.originalPom;
        this.verifyFailType = pluginParameters.verifyFailType;
        this.verifyFailOn = pluginParameters.verifyFailOn;

        sorter.initialize(log, pluginParameters);

        warnAboutDeprecatedArguments(pluginParameters);
    }

    private void warnAboutDeprecatedArguments(PluginParameters pluginParameters) {
        if (pluginParameters.sortDependencies.isDeprecatedValueTrue()) {
            throw new FailureException(
                    "The 'true' value in sortDependencies is not supported anymore, please use value 'groupId,artifactId' instead.");
        }
        if (pluginParameters.sortDependencies.isDeprecatedValueFalse()) {
            throw new FailureException(
                    "The 'false' value in sortDependencies is not supported anymore, please use empty value '' or omit sortDependencies instead.");
        }
        if (pluginParameters.sortDependencyExclusions.isDeprecatedValueTrue()) {
            throw new FailureException(
                    "The 'true' value in sortDependencyExclusions is not supported, please use value 'groupId,artifactId' instead.");
        }
        if (pluginParameters.sortDependencyExclusions.isDeprecatedValueFalse()) {
            throw new FailureException(
                    "The 'false' value in sortDependencyExclusions is not supported, please use empty value '' or omit sortDependencyExclusions instead.");
        }
        if (pluginParameters.sortPlugins.isDeprecatedValueTrue()) {
            throw new FailureException(
                    "The 'true' value in sortPlugins is not supported anymore, please use value 'groupId,artifactId' instead.");
        }
        if (pluginParameters.sortPlugins.isDeprecatedValueFalse()) {
            throw new FailureException(
                    "The 'false' value in sortPlugins is not supported anymore, please use empty value '' or omit sortPlugins instead.");
        }
    }

    /** Sorts the pom file. */
    public void sort() {
        log.info("Sorting file " + pomFile.getAbsolutePath());

        sorter.sortOriginalXml();
        sorter.generateSortedXml();
        if (sorter.isOriginalXmlStringSorted().isOrdered()) {
            log.info("Pom file is already sorted, exiting");
            return;
        }
        sorter.createBackupFile();
        sorter.saveGeneratedXml();
        log.info("Saved sorted pom file to " + pomFile.getAbsolutePath());
    }

    /** Verify that the pom-file is sorted regardless of formatting */
    public void check() {
        XmlOrderedResult xmlOrderedResult = getVerificationResult();
        performVerificationResult(xmlOrderedResult);
    }

    /**
     * clean
     *
     */
    public void cleanup() {
        sorter.cleanup();
    }

    private XmlOrderedResult getVerificationResult() {
        log.info("Verifying file " + pomFile.getAbsolutePath());

        sorter.sortOriginalXml();

        XmlOrderedResult xmlOrderedResult;
        if (verifyFailOn == VerifyFailOnType.XMLELEMENTS) {
            xmlOrderedResult = sorter.isOriginalXmlElementsSorted();
        } else {
            sorter.generateSortedXml();
            xmlOrderedResult = sorter.isOriginalXmlStringSorted();
        }
        return xmlOrderedResult;
    }

    private void performVerificationResult(XmlOrderedResult xmlOrderedResult) {
        if (!xmlOrderedResult.isOrdered()) {
            switch (verifyFailType) {
                case WARN:
                    log.warn(xmlOrderedResult.getErrorMessage());
                    sorter.saveViolationFile(xmlOrderedResult);
                    log.warn(String.format(TEXT_FILE_NOT_SORTED, pomFile.getAbsolutePath()));
                    break;
                case SORT:
                    log.info(xmlOrderedResult.getErrorMessage());
                    sorter.saveViolationFile(xmlOrderedResult);
                    log.info(String.format(TEXT_FILE_NOT_SORTED, pomFile.getAbsolutePath()));
                    log.info("Sorting file " + pomFile.getAbsolutePath());
                    sorter.generateSortedXml();
                    sorter.createBackupFile();
                    sorter.saveGeneratedXml();
                    log.info("Saved sorted pom file to " + pomFile.getAbsolutePath());
                    break;
                case STOP:
                    log.error(xmlOrderedResult.getErrorMessage());
                    sorter.saveViolationFile(xmlOrderedResult);
                    log.error(String.format(TEXT_FILE_NOT_SORTED, pomFile.getAbsolutePath()));
                    throw new FailureException(String.format(TEXT_FILE_NOT_SORTED, pomFile.getAbsolutePath()));
            }
        }
    }
}
