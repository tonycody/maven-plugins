package io.github.tonycody.maven.plugin.sorter;

import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Verifies that the pom.xml is sorted. If the verification fails then the pom.xml is sorted.
 *
 * @author Bjorn Ekryd
 */
@Mojo(name = "check", threadSafe = true, defaultPhase = LifecyclePhase.VALIDATE)
@SuppressWarnings({"UnusedDeclaration"})
public class CheckMojo extends AbstractParentMojo {

    /** What should happen if verification fails. Can be either 'sort', 'warn' or 'stop' */
    @Parameter(property = "maven.sorter.verifyFail", defaultValue = "sort")
    private String verifyFail;

    /**
     * What kind of differences should trigger verify failure. Can be either 'xmlElements' or
     * 'strict'. Can be combined with ignoreLineSeparators
     */
    @Parameter(property = "maven.sorter.verifyFailOn", defaultValue = "xmlElements")
    private String verifyFailOn;

    /**
     * Saves the verification failure to an external xml file, recommended filename is
     * 'target/sortpom_reports/violation.xml'.
     */
    @Parameter(property = "maven.sorter.violationFilename")
    private String violationFilename;

    /**
     * 建立参数
     *
     * @return {@link PluginParameters}
     */
    @Override
    protected PluginParameters buildParameters() {
        return PluginParameters.builder()
                .setOriginalPom(pom)
                .setFileOutput(backup, outputDirectory, violationFilename, keepTimestamp)
                .setEncoding(encoding)
                .setFormatting(lineSeparator, expandEmptyElements, spaceBeforeCloseEmptyElement, keepBlankLines)
                .setIndent(nrOfIndentSpace, indentBlankLines, indentSchemaLocation)
                .setSortOrder(sortOrderFile, predefinedSortOrder)
                .setSortEntities(
                        sortDependencies,
                        sortDependencyExclusions,
                        sortDependencyManagement,
                        sortPlugins,
                        sortProperties,
                        sortModules,
                        sortExecutions)
                .setIgnoreLineSeparators(ignoreLineSeparators)
                .setVerifyFail(verifyFail, verifyFailOn)
                .build();
    }

    /**
     *
     */
    @Override
    protected void invoke(SorterHandler sorterHandler) {
        sorterHandler.check();
    }
}
