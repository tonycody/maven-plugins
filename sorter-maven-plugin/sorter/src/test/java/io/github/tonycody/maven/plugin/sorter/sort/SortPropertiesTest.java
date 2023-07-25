package io.github.tonycody.maven.plugin.sorter.sort;

import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import org.junit.jupiter.api.Test;

class SortPropertiesTest {

    @Test
    final void namedParametersInSortFileShouldSortThemFirst() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
                .lineSeparator("\n")
                .testFiles("/SortedProperties_input.xml", "/SortedProperties_output.xml");
    }

    @Test
    final void sortPropertyParameterShouldSortAlphabetically() throws Exception {
        SortPomImplUtil.create()
                .sortProperties()
                .lineSeparator("\n")
                .predefinedSortOrder("custom_1")
                .testFiles("/SortedProperties_input.xml", "/SortedProperties_output_alfa.xml");
    }

    @Test
    final void testBothNamedParametersInSortFileAndSortPropertyParameterTest() throws Exception {
        SortPomImplUtil.create()
                .lineSeparator("\n")
                .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
                .sortProperties()
                .testFiles("/SortedProperties_input.xml", "/SortedProperties_output_alfa2.xml");
    }

    @Test
    final void sortingOfFullPomFileShouldWork() throws Exception {
        SortPomImplUtil.create()
                .sortProperties()
                .sortPlugins("groupId,artifactId")
                .sortDependencies("groupId,artifactId")
                .lineSeparator("\n")
                .testFiles("/SortProp_input.xml", "/SortProp_expected.xml");
    }

    @Test
    final void duplicatePropertiesShouldNotTriggerSorting() throws Exception {
        SortPomImplUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .sortProperties()
                .sortPlugins("groupId,artifactId")
                .sortDependencies("groupId,artifactId")
                .lineSeparator("\n")
                .testNoSorting("/SortProp_input_duplicate.xml");
    }
}
