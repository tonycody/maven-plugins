package io.github.tonycody.maven.plugin.sorter.verify;

import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import org.junit.jupiter.api.Test;

class SortPropertiesTest {

    @Test
    final void namedParametersInSortFileShouldNotAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
                .lineSeparator("\n")
                .testVerifyXmlIsOrdered("/SortedProperties_output.xml");
    }

    @Test
    final void sortPropertyParameterShouldNotAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .sortProperties()
                .lineSeparator("\n")
                .predefinedSortOrder("custom_1")
                .testVerifyXmlIsOrdered("/SortedProperties_output_alfa.xml");
    }

    @Test
    final void testBothNamedParametersInSortFileAndSortPropertyParameterTestNotAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .lineSeparator("\n")
                .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
                .sortProperties()
                .testVerifyXmlIsOrdered("/SortedProperties_output_alfa2.xml");
    }

    @Test
    final void sortingOfFullPomFileShouldNotAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .sortProperties()
                .sortPlugins("groupId,artifactId")
                .sortDependencies("groupId,artifactId")
                .lineSeparator("\n")
                .testVerifyXmlIsOrdered("/SortProp_expected.xml");
    }

    @Test
    final void namedParametersInSortFileShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
                .lineSeparator("\n")
                .testVerifyXmlIsNotOrdered(
                        "/SortedProperties_input.xml",
                        "The xml element <project.build.sourceEncoding> should be placed before <other>");
    }

    @Test
    final void sortPropertyParameterShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .sortProperties()
                .lineSeparator("\n")
                .predefinedSortOrder("custom_1")
                .testVerifyXmlIsNotOrdered(
                        "/SortedProperties_input.xml", "The xml element <another> should be placed before <other>");
    }

    @Test
    final void testBothNamedParametersInSortFileAndSortPropertyParameterTestAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .lineSeparator("\n")
                .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
                .sortProperties()
                .testVerifyXmlIsNotOrdered(
                        "/SortedProperties_input.xml",
                        "The xml element <project.build.sourceEncoding> should be placed before <other>");
    }

    @Test
    final void sortingOfFullPomFileShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .sortProperties()
                .predefinedSortOrder("default_0_4_0")
                .sortPlugins("groupId,artifactId")
                .sortDependencies("groupId,artifactId")
                .lineSeparator("\n")
                .testVerifyXmlIsNotOrdered(
                        "/SortProp_input.xml",
                        "The xml element <commons.beanutils.version> should be placed before <commons.io.version>");
    }
}
