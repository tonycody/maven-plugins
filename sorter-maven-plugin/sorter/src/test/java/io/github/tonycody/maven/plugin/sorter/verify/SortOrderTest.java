package io.github.tonycody.maven.plugin.sorter.verify;

import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import org.junit.jupiter.api.Test;

class SortOrderTest {

    @Test
    final void testSortDifferentClassPathShouldNotAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("difforder/differentOrder.xml")
                .testVerifyXmlIsOrdered("/full_differentorder_expected.xml");
    }

    @Test
    final void testSortXmlCharacterShouldNotAffectVerify() throws Exception {
        SortPomImplUtil.create().testVerifyXmlIsOrdered("/Character_expected.xml");
    }

    @Test
    final void testSortXmlComplexShouldNotAffectVerify() throws Exception {
        SortPomImplUtil.create().predefinedSortOrder("default_0_4_0").testVerifyXmlIsOrdered("/Complex_expected.xml");
    }

    @Test
    final void testSortXmlFullFromAlphabeticalOrderShouldNotAffectVerify() throws Exception {
        SortPomImplUtil.create().predefinedSortOrder("default_0_4_0").testVerifyXmlIsOrdered("/full_expected.xml");
    }

    @Test
    final void testSortXmlReal1ShouldNotAffectVerify() throws Exception {
        SortPomImplUtil.create().testVerifyXmlIsOrdered("/Real1_expected.xml");
    }

    @Test
    final void testSortXmlSimpleShouldNotAffectVerify() throws Exception {
        SortPomImplUtil.create().testVerifyXmlIsOrdered("/Simple_expected.xml");
    }

    @Test
    final void testSortWithIndentShouldNotAffectVerify() throws Exception {
        SortPomImplUtil.create().nrOfIndentSpace(42).testVerifyXmlIsOrdered("/Simple_expected_indent.xml");
    }

    @Test
    final void testSortWithDependencySortSimpleShouldNotAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .sortDependencies("groupId,artifactId")
                .sortPlugins("groupId,artifactId")
                .testVerifyXmlIsOrdered("/Simple_expected_sortDep.xml");
    }

    @Test
    final void testSortWithDependencySortFullShouldNotAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .sortDependencies("groupId,artifactId")
                .sortPlugins("groupId,artifactId")
                .testVerifyXmlIsOrdered("/SortDep_expected.xml");
    }

    @Test
    final void testSortDifferentClassPathShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("difforder/differentOrder.xml")
                .testVerifyXmlIsNotOrdered(
                        "/full_unsorted_input.xml", "The xml element <modelVersion> should be placed before <parent>");
    }

    @Test
    final void testSortXmlCharacterShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .testVerifyXmlIsNotOrdered(
                        "/Character_input.xml", "The xml element <modelVersion> should be placed before <artifactId>");
    }

    @Test
    final void testSortXmlComplexShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .testVerifyXmlIsNotOrdered(
                        "/Complex_input.xml", "The xml element <modelVersion> should be placed before <artifactId>");
    }

    @Test
    final void testSortXmlFullFromAlphabeticalOrderShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .testVerifyXmlIsNotOrdered(
                        "/full_alfa_input.xml", "The xml element <modelVersion> should be placed before <artifactId>");
    }

    @Test
    final void testSortXmlFullShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .testVerifyXmlIsNotOrdered(
                        "/full_unsorted_input.xml", "The xml element <modelVersion> should be placed before <parent>");
    }

    @Test
    final void testSortXmlReal1ShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .testVerifyXmlIsNotOrdered(
                        "/Real1_input.xml", "The xml element <version> should be placed before <name>");
    }

    @Test
    final void testSortXmlSimpleShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .testVerifyXmlIsNotOrdered(
                        "/Simple_input.xml", "The xml element <modelVersion> should be placed before <artifactId>");
    }

    @Test
    final void sortedDependenciesWithDifferentChildrenShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .sortDependencies("groupId,artifactId")
                .testVerifyXmlIsNotOrdered(
                        "/SortDepSimple_input.xml",
                        "The xml element <groupId>cglib</groupId> should be placed before <groupId>junit</groupId>");
    }

    @Test
    final void sortedDependenciesWithDifferentNamesShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .sortDependencies("groupId,artifactId")
                .testVerifyXmlIsNotOrdered(
                        "/SortDepSimple2_input.xml",
                        "The xml element <groupId>cglib</groupId> should be placed before <groupId>junit</groupId>");
    }

    @Test
    final void sortedDependenciesWithSameNameShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .sortDependencies("groupId,artifactId")
                .testVerifyXmlIsNotOrdered(
                        "/SortDepSimple3_input.xml",
                        "The xml element <dependency> with 2 child elements should be placed before element <dependency> with 4 child elements");
    }

    @Test
    final void testSortWithDependencySortFullShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .sortDependencies("groupId,artifactId")
                .sortPlugins("groupId,artifactId")
                .testVerifyXmlIsNotOrdered(
                        "/SortDep_input.xml",
                        "The xml element <groupId>cheesymock</groupId> should be placed before <groupId>junit</groupId>");
    }
}
