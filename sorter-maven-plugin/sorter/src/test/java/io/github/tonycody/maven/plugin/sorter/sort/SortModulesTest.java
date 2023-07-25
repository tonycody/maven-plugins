package io.github.tonycody.maven.plugin.sorter.sort;

import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import org.junit.jupiter.api.Test;

class SortModulesTest {
    @Test
    final void sortingOfPomFileWithSubmodulesShouldWork() throws Exception {
        SortPomImplUtil.create()
                .sortProperties()
                .sortPlugins("groupId,artifactId")
                .sortModules()
                .sortDependencies("groupId,artifactId")
                .lineSeparator("\n")
                .testFiles("/SortModules_input.xml", "/SortModules_expected.xml");
    }

    @Test
    final void sortingOfPomFileWithSubmodulesAndExtraElementsShouldWork() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("sortOrderFiles/extra_dummy_tags.xml")
                .sortProperties()
                .sortPlugins("groupId,artifactId")
                .sortModules()
                .sortDependencies("groupId,artifactId")
                .lineSeparator("\n")
                .testFiles("/SortModules_input_extra_elements.xml", "/SortModules_expected_extra_elements.xml");
    }

    @Test
    final void sortingOfPomFileWithSubmodulesNotEnabled() throws Exception {
        SortPomImplUtil.create()
                .sortProperties()
                .sortPlugins("groupId,artifactId")
                .sortDependencies("groupId,artifactId")
                .lineSeparator("\n")
                .testFiles("/SortModules_input.xml", "/SortModules_expected_notsorted.xml");
    }
}
