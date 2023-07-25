package io.github.tonycody.maven.plugin.sorter.sort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.tonycody.maven.plugin.sorter.exception.FailureException;
import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class SortDependencyExclusionsTest {

    @Test
    final void deprecatedSortPluginsTrueMessageShouldWork() {
        Executable testMethod = () -> SortPomImplUtil.create()
                .customSortOrderFile("custom_1.xml")
                .sortDependencyExclusions("true")
                .lineSeparator("\n")
                .nrOfIndentSpace(4)
                .testFiles("/PluginDefaultName_input.xml", "/PluginDefaultName_expect.xml");

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(
                thrown.getMessage(),
                is(
                        "The 'true' value in sortDependencyExclusions is not supported, please use value 'groupId,artifactId' instead."));
    }

    @Test
    final void deprecatedSortPluginsFalseMessageShouldWork() {
        Executable testMethod = () -> SortPomImplUtil.create()
                .customSortOrderFile("custom_1.xml")
                .sortDependencyExclusions("false")
                .lineSeparator("\n")
                .nrOfIndentSpace(4)
                .testFiles("/PluginDefaultName_input.xml", "/PluginDefaultName_expect.xml");

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(
                thrown.getMessage(),
                is(
                        "The 'false' value in sortDependencyExclusions is not supported, please use empty value '' or omit sortDependencyExclusions instead."));
    }

    @Test
    final void sortGroupIdForExclusionsShouldWork() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("custom_1.xml")
                .sortDependencyExclusions("groupId")
                .lineSeparator("\n")
                .nrOfIndentSpace(2)
                .testFiles("/SortDepExclusions_input.xml", "/SortDepExclusions_group_expected.xml");
    }

    @Test
    final void sortArtifactIdForExclusionsShouldWork() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("custom_1.xml")
                .sortDependencyExclusions("artifactId")
                .lineSeparator("\n")
                .nrOfIndentSpace(2)
                .testFiles("/SortDepExclusions_input.xml", "/SortDepExclusions_artifact_expected.xml");
    }

    @Test
    final void sortGroupIdAndArtifactIdForExclusionsShouldWork() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("custom_1.xml")
                .sortDependencyExclusions("groupId,artifactId")
                .lineSeparator("\n")
                .nrOfIndentSpace(2)
                .testFiles("/SortDepExclusions_input.xml", "/SortDepExclusions_group_artifact_expected.xml");
    }
}
