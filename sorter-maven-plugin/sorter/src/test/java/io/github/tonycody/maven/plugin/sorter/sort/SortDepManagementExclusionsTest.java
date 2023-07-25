package io.github.tonycody.maven.plugin.sorter.sort;

import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import org.junit.jupiter.api.Test;

class SortDepManagementExclusionsTest {

    @Test
    final void sortGroupIdForExclusionsShouldWork() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("custom_1.xml")
                .sortDependencyExclusions("groupId")
                .lineSeparator("\n")
                .nrOfIndentSpace(2)
                .testFiles("/SortDepManagementExclusions_input.xml", "/SortDepManagementExclusions_group_expected.xml");
    }

    @Test
    final void sortArtifactIdForExclusionsShouldWork() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("custom_1.xml")
                .sortDependencyExclusions("artifactId")
                .lineSeparator("\n")
                .nrOfIndentSpace(2)
                .testFiles(
                        "/SortDepManagementExclusions_input.xml", "/SortDepManagementExclusions_artifact_expected.xml");
    }

    @Test
    final void sortGroupIdAndArtifactIdForExclusionsShouldWork() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("custom_1.xml")
                .sortDependencyExclusions("groupId,artifactId")
                .lineSeparator("\n")
                .nrOfIndentSpace(2)
                .testFiles(
                        "/SortDepManagementExclusions_input.xml",
                        "/SortDepManagementExclusions_group_artifact_expected.xml");
    }
}
