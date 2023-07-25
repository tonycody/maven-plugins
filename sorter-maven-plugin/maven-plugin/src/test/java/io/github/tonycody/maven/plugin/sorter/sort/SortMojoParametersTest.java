package io.github.tonycody.maven.plugin.sorter.sort;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import io.github.tonycody.maven.plugin.sorter.SortMojo;
import io.github.tonycody.maven.plugin.sorter.Sorter;
import io.github.tonycody.maven.plugin.sorter.SorterHandler;
import io.github.tonycody.maven.plugin.sorter.logger.SorterLogger;
import io.github.tonycody.maven.plugin.sorter.output.XmlOutputGenerator;
import io.github.tonycody.maven.plugin.sorter.processinstruction.XmlProcessingInstructionParser;
import io.github.tonycody.maven.plugin.sorter.util.FileHelper;
import io.github.tonycody.maven.plugin.sorter.wrapper.ElementWrapperCreator;
import io.github.tonycody.maven.plugin.sorter.wrapper.TextWrapperCreator;
import io.github.tonycody.maven.plugin.sorter.wrapper.WrapperFactoryImpl;
import java.io.File;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import refutils.ReflectionHelper;

class SortMojoParametersTest {
    private final File pomFile = mock(File.class);

    private SorterHandler sortPomImpl;
    private Sorter sortPomService;
    private FileHelper fileUtil;
    private SortMojo sortMojo;
    private ElementWrapperCreator elementWrapperCreator;
    private TextWrapperCreator textWrapperCreator;
    private XmlOutputGenerator xmlOutputGenerator;
    private XmlProcessingInstructionParser xmlProcessingInstructionParser;

    @BeforeEach
    void setup() throws SecurityException, IllegalArgumentException {
        sortMojo = new SortMojo();
        new ReflectionHelper(sortMojo).setField("lineSeparator", "\n");
        new ReflectionHelper(sortMojo).setField("encoding", "UTF-8");

        sortPomImpl = new ReflectionHelper(sortMojo).getField(SorterHandler.class);
        sortPomService = new ReflectionHelper(sortPomImpl).getField(Sorter.class);
        ReflectionHelper sortPomServiceHelper = new ReflectionHelper(sortPomService);
        fileUtil = sortPomServiceHelper.getField(FileHelper.class);
        xmlOutputGenerator = sortPomServiceHelper.getField(XmlOutputGenerator.class);
        WrapperFactoryImpl wrapperFactoryImpl = sortPomServiceHelper.getField(WrapperFactoryImpl.class);
        xmlProcessingInstructionParser = sortPomServiceHelper.getField(XmlProcessingInstructionParser.class);

        elementWrapperCreator = new ReflectionHelper(wrapperFactoryImpl).getField(ElementWrapperCreator.class);
        textWrapperCreator = new ReflectionHelper(wrapperFactoryImpl).getField(TextWrapperCreator.class);
    }

    @Test
    void pomFileParameter() {
        assertParameterMoveFromMojoToRestOfApplication("pomFile", pomFile, sortPomImpl, fileUtil);
    }

    @Test
    void createBackupFileParameter() {
        assertParameterMoveFromMojoToRestOfApplicationForBoolean("createBackupFile", sortPomService);
    }

    @Test
    void keepTimestampParameter() {
        assertParameterMoveFromMojoToRestOfApplicationForBoolean("keepTimestamp", fileUtil);
    }

    @Test
    void backupFileExtensionParameter() {
        assertParameterMoveFromMojoToRestOfApplication("backupFileExtension", ".gurka", sortPomService, fileUtil);
    }

    @Test
    void encodingParameter() {
        assertParameterMoveFromMojoToRestOfApplication(
                "encoding", "GURKA-2000", fileUtil, sortPomService, xmlOutputGenerator);
    }

    @Test
    void lineSeparatorParameter() {
        assertParameterMoveFromMojoToRestOfApplication("lineSeparator", "\r");

        final String lineSeparator = new ReflectionHelper(xmlOutputGenerator)
                .getField("lineSeparator")
                .toString();

        assertThat(lineSeparator, is(equalTo("\r")));
    }

    @Test
    void parameterNrOfIndentSpaceShouldEndUpInXmlProcessor() {
        assertParameterMoveFromMojoToRestOfApplication("nrOfIndentSpace", 6);

        final Object indentCharacters = new ReflectionHelper(xmlOutputGenerator).getField("indentCharacters");

        assertThat(indentCharacters, is(equalTo("      ")));
    }

    @Test
    void expandEmptyElementsParameter() {
        assertParameterMoveFromMojoToRestOfApplicationForBoolean("expandEmptyElements", xmlOutputGenerator);
    }

    @Test
    void spaceBeforeCloseEmptyElementParameter() {
        assertParameterMoveFromMojoToRestOfApplicationForBoolean("spaceBeforeCloseEmptyElement", xmlOutputGenerator);
    }

    @Test
    void predefinedSortOrderParameter() {
        assertParameterMoveFromMojoToRestOfApplication("predefinedSortOrder", "tomatoSort", fileUtil);
    }

    @Test
    void parameterSortOrderFileShouldEndUpInFileUtil() {
        assertParameterMoveFromMojoToRestOfApplication("sortOrderFile", "sortOrderFile.gurka");

        Object actual = new ReflectionHelper(fileUtil).getField("customSortOrderFile");

        assertThat(actual, is(equalTo("sortOrderFile.gurka")));
    }

    @Test
    void parameterSortDependenciesShouldEndUpInElementWrapperCreator() {
        assertParameterMoveFromMojoToRestOfApplication("sortDependencies", "groupId,scope");

        Object sortDependencies = new ReflectionHelper(elementWrapperCreator).getField("sortDependencies");
        assertThat(sortDependencies.toString(), is("DependencySortOrder{childElementNames=[groupId, scope]}"));
    }

    @Test
    void parameterSortDependencyManagementShouldEndUpInElementWrapperCreator() {
        assertParameterMoveFromMojoToRestOfApplication("sortDependencyManagement", "scope,groupId");

        Object sortDependencies = new ReflectionHelper(elementWrapperCreator).getField("sortDependencyManagement");
        assertThat(sortDependencies.toString(), is("DependencySortOrder{childElementNames=[scope, groupId]}"));
    }

    @Test
    void parameterSortDependencyExclusionsShouldEndUpInElementWrapperCreator() {
        assertParameterMoveFromMojoToRestOfApplication("sortDependencyExclusions", "groupId,scope");

        Object sortDependencyExclusions =
                new ReflectionHelper(elementWrapperCreator).getField("sortDependencyExclusions");
        assertThat(sortDependencyExclusions.toString(), is("DependencySortOrder{childElementNames=[groupId, scope]}"));
    }

    @Test
    void parameterSortPluginsShouldEndUpInWrapperFactoryImpl() {
        assertParameterMoveFromMojoToRestOfApplication("sortPlugins", "alfa,beta");

        Object sortPlugins = new ReflectionHelper(elementWrapperCreator).getField("sortPlugins");
        assertThat(sortPlugins.toString(), is("DependencySortOrder{childElementNames=[alfa, beta]}"));
    }

    @Test
    void parameterSortModulesShouldEndUpInWrapperFactoryImpl() {
        assertParameterMoveFromMojoToRestOfApplicationForBoolean("sortModules", elementWrapperCreator);
    }

    @Test
    void parameterSortExecutionsShouldEndUpInWrapperFactoryImpl() {
        assertParameterMoveFromMojoToRestOfApplicationForBoolean("sortExecutions", elementWrapperCreator);
    }

    @Test
    void parameterSortPropertiesShouldEndUpInWrapperFactoryImpl() {
        assertParameterMoveFromMojoToRestOfApplicationForBoolean("sortProperties", elementWrapperCreator);
    }

    @Test
    void parameterKeepBlankLineShouldEndUpInXmlProcessor() {
        assertParameterMoveFromMojoToRestOfApplicationForBoolean("keepBlankLines", textWrapperCreator);
    }

    @Test
    void parameterIndentBlankLineShouldEndUpInXmlProcessor() {
        assertParameterMoveFromMojoToRestOfApplicationForBoolean("indentBlankLines", xmlOutputGenerator);
    }

    @Test
    void parameterIndentSchemaLocationShouldEndUpInXmlProcessor() {
        assertParameterMoveFromMojoToRestOfApplicationForBoolean("indentSchemaLocation", xmlOutputGenerator);
    }

    @Test
    void xmlProcessingInstructionParserShouldGetLogger() throws MojoFailureException {
        sortMojo.execute();
        SorterLogger logger = new ReflectionHelper(xmlProcessingInstructionParser).getField(SorterLogger.class);
        SorterLogger expectedLogger = new ReflectionHelper(sortPomService).getField(SorterLogger.class);
        assertThat(logger, not(nullValue()));
        assertThat(logger, sameInstance(expectedLogger));
    }

    private void assertParameterMoveFromMojoToRestOfApplication(
            String parameterName, Object parameterValue, Object... whereParameterCanBeFound) {
        new ReflectionHelper(sortMojo).setField(parameterName, parameterValue);

        try {
            sortMojo.execute();
        } catch (MojoFailureException e) {
            throw new RuntimeException(e);
        }

        for (Object someInstanceThatContainParameter : whereParameterCanBeFound) {
            Object actual = new ReflectionHelper(someInstanceThatContainParameter).getField(parameterName);

            assertThat(actual, is(equalTo(parameterValue)));
        }
    }

    private void assertParameterMoveFromMojoToRestOfApplicationForBoolean(
            String parameterName, Object... whereParameterCanBeFound) {
        new ReflectionHelper(sortMojo).setField(parameterName, true);

        try {
            sortMojo.execute();
        } catch (MojoFailureException e) {
            throw new RuntimeException(e);
        }

        for (Object someInstanceThatContainParameter : whereParameterCanBeFound) {
            boolean actual = (boolean) new ReflectionHelper(someInstanceThatContainParameter).getField(parameterName);

            assertThat(actual, is(equalTo(true)));
        }
    }
}
