package io.github.tonycody.maven.plugin.sorter;

import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Clear the backup files generated by the sorter plug-in
 *
 * @author XS <tonycody@qq.com>
 * @version 1.0
 * @date 2023/7/5 16:53
 */
@Mojo(name = "clean", threadSafe = true, defaultPhase = LifecyclePhase.CLEAN)
public class CleanMojo extends AbstractParentMojo {
    @Parameter(property = "maven.sorter.clean", defaultValue = "false")
    private boolean clean;

    /**
     * 调用
     *
     * @param sorterHandler
     */
    @Override
    protected void invoke(SorterHandler sorterHandler) {
        if (clean) {
            sorterHandler.cleanup();
        }
    }

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
}
