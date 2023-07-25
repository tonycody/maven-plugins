package io.github.tonycody.maven.plugin.sorter;

import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Sorts the pom.xml for a Maven project.
 *
 * @author Bjorn Ekryd
 */
@Mojo(name = "sort", threadSafe = true, defaultPhase = LifecyclePhase.VALIDATE)
@SuppressWarnings({"UnusedDeclaration"})
public class SortMojo extends AbstractParentMojo {

    /**
     * 建立参数
     *
     * @return {@link PluginParameters}
     */
    @Override
    protected PluginParameters buildParameters() {
        return PluginParameters.builder()
                .setOriginalPom(pom)
                .setFileOutput(backup, outputDirectory, null, keepTimestamp)
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
                .build();
    }

    /**
     *
     */
    @Override
    protected void invoke(SorterHandler sorterHandler) {
        sorterHandler.sort();
    }
}
