package io.github.tonycody.maven.plugin.sorter;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

import io.github.tonycody.maven.plugin.sorter.logger.MavenLogger;
import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

/** Common parent for both SortMojo and VerifyMojo */
@SuppressWarnings("jol")
abstract class AbstractParentMojo extends AbstractMojo {

    /** 排序处理程序 */
    private final SorterHandler SORTER_HANDLER = SorterHandler.getInstance();

    /** This is the File instance that refers to the location of the pom that should be sorted. */
    @Parameter(property = "maven.sorter.pom", defaultValue = "${project.file}")
    File pom;
    /** Should a backup copy be created for the sorted pom. */
    @Parameter(property = "maven.sorter.backup", defaultValue = "true")
    boolean backup;

    @Parameter(property = "maven.sorter.outputDirectory", defaultValue = "${project.build.directory}/.sorted")
    File outputDirectory;
    /** Encoding for the files. */
    @Parameter(property = "maven.sorter.encoding", defaultValue = "${project.build.sourceEncoding}")
    String encoding;
    /** Line separator for sorted pom. Can be either \n, \r or \r\n */
    @Parameter(property = "maven.sorter.lineSeparator", defaultValue = "${line.separator}")
    String lineSeparator;
    /**
     * Should an empty xml element be expanded with start and end tag, or be written as an
     * empty-element tag.
     */
    @Parameter(property = "maven.sorter.expandEmptyElements", defaultValue = "true")
    boolean expandEmptyElements;
    /** Should a non-expanded empty-element tag have a space before the closing slash. */
    @Parameter(property = "maven.sorter.spaceBeforeCloseEmptyElement", defaultValue = "false")
    boolean spaceBeforeCloseEmptyElement;
    /**
     * Should blank lines in the pom-file be preserved. A maximum of one line is preserved between
     * each tag.
     */
    @Parameter(property = "maven.sorter.keepBlankLines", defaultValue = "true")
    boolean keepBlankLines;
    /**
     * Number of space characters to use as indentation. A value of -1 indicates that tab character
     * should be used instead.
     */
    @Parameter(property = "maven.sorter.nrOfIndentSpace", defaultValue = "4")
    int nrOfIndentSpace;
    /** Ignore line separators when comparing current POM with sorted one */
    @Parameter(property = "maven.sorter.ignoreLineSeparators", defaultValue = "true")
    boolean ignoreLineSeparators;
    /** Should blank lines (if preserved) have indentation. */
    @Parameter(property = "maven.sorter.indentBlankLines", defaultValue = "false")
    boolean indentBlankLines;
    /**
     * Should the schema location attribute of project (top level xml element) be placed on a new
     * line. The attribute will be indented (2 * nrOfIndentSpace + 1 space) characters.
     */
    @Parameter(property = "maven.sorter.indentSchemaLocation", defaultValue = "false")
    boolean indentSchemaLocation;
    /** Choose between a number of predefined sort order files. */
    @Parameter(property = "maven.sorter.predefinedSortOrder", defaultValue = "recommended_2008_06")
    String predefinedSortOrder;
    /** Custom sort order file. */
    @Parameter(property = "maven.sorter.sortOrderFile")
    String sortOrderFile;
    /**
     * Comma-separated ordered list how dependencies should be sorted. Example:
     * scope,groupId,artifactId. If scope is specified in the list then the scope ranking is IMPORT,
     * COMPILE, PROVIDED, SYSTEM, RUNTIME and TEST. The list can be separated by ",;:"
     */
    @Parameter(property = "maven.sorter.sortDependencies")
    String sortDependencies;
    /**
     * Comma-separated ordered list how exclusions, for dependencies, should be sorted. Example:
     * groupId,artifactId The list can be separated by ",;:"
     */
    @Parameter(property = "maven.sorter.sortDependencyExclusions")
    String sortDependencyExclusions;
    /**
     * Comma-separated ordered list how dependencies in dependency management should be sorted.
     * Example: scope,groupId,artifactId. If scope is specified in the list then the scope ranking is
     * IMPORT, COMPILE, PROVIDED, SYSTEM, RUNTIME and TEST. The list can be separated by ",;:". It
     * would take precedence if present and would fall back to {@link #sortDependencies} if not
     * present. The value NONE can be used to avoid sorting dependency management at all.
     */
    @Parameter(property = "maven.sorter.sortDependencyManagement")
    String sortDependencyManagement;
    /**
     * Comma-separated ordered list how plugins should be sorted. Example: groupId,artifactId The list
     * can be separated by ",;:"
     */
    @Parameter(property = "maven.sorter.sortPlugins")
    String sortPlugins;
    /**
     * Should the Maven pom properties be sorted alphabetically. Affects both project/properties and
     * project/profiles/profile/properties
     */
    @Parameter(property = "maven.sorter.sortProperties", defaultValue = "false")
    boolean sortProperties;
    /** Should the Maven pom sub modules be sorted alphabetically. */
    @Parameter(property = "maven.sorter.sortModules", defaultValue = "false")
    boolean sortModules;
    /** Should the Maven pom execution sections be sorted by phase and then alphabetically. */
    @Parameter(property = "maven.sorter.sortExecutions", defaultValue = "false")
    boolean sortExecutions;
    /** Whether to keep the file timestamps of old POM file when creating new POM file. */
    @Parameter(property = "maven.sorter.keepTimestamp", defaultValue = "false")
    boolean keepTimestamp;
    /** Set this to 'true' to bypass sortpom plugin */
    @Parameter(property = "maven.sorter.skip", defaultValue = "false")
    private boolean skip;

    /**执行
     * Execute plugin.
     *
     * @see org.apache.maven.plugin.Mojo#execute()
     * @throws MojoFailureException 魔力衰竭例外
     */
    @Override
    public void execute() throws MojoFailureException {
        CheckedRunnable runnable = Match(skip)
                .of(Case($(true), () -> getLog().info("Skipping sort")), Case($(false), () -> {
                    PluginParameters pluginParameters = buildParameters();
                    SORTER_HANDLER.initialize(new MavenLogger(getLog()), pluginParameters);
                    invoke(SORTER_HANDLER);
                }));
        Try.run(runnable);
    }

    /**
     * 调用
     */
    protected abstract void invoke(SorterHandler sorterHandler);

    /**
     * 建立参数
     *
     * @return {@link PluginParameters}
     */
    protected abstract PluginParameters buildParameters();
}
