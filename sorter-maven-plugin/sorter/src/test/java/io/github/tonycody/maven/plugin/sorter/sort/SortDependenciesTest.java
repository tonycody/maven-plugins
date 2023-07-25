package io.github.tonycody.maven.plugin.sorter.sort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.tonycody.maven.plugin.sorter.exception.FailureException;
import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class SortDependenciesTest {

    @Test
    final void scopeInSortDependenciesShouldSortByScope() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("custom_1.xml")
                .sortDependencies("scope,GROUPID,artifactId")
                .lineSeparator("\r\n")
                .testFiles("/SortDep_input_simpleWithScope.xml", "/SortDep_expected_simpleWithScope2.xml");
    }

    /**
     * This is an odd test since we add an EXTRA tag in the SORT ORDER FILE (under plugins and
     * dependencies) so that it will be sorted beside dependency and plugin tags.
     *
     * <p>The extra tag does not play well with the pom xml validation. Also unknown tags should be
     * placed last.
     */
    @Test
    final void extraTagInDependenciesPluginAndExclusionsShouldBeSortedFirst() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("sortOrderFiles/extra_dummy_tags.xml")
                .sortDependencies("scope,groupId,artifactId")
                .sortDependencyExclusions("groupId,artifactId")
                .sortPlugins("groupId,artifactId")
                .lineSeparator("\r\n")
                .testFiles("/Extra_tags_dep_and_plugin.xml", "/Extra_tags_dep_and_plugin_expected.xml");
    }

    @Test
    final void defaultGroupIdForPluginsShouldWork() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("custom_1.xml")
                .sortPlugins("groupId,artifactId")
                .lineSeparator("\n")
                .nrOfIndentSpace(4)
                .testFiles("/PluginDefaultName_input.xml", "/PluginDefaultName_expect.xml");
    }

    @Test
    final void deprecatedSortPluginsTrueMessageShouldWork() {
        Executable testMethod = () -> SortPomImplUtil.create()
                .customSortOrderFile("custom_1.xml")
                .sortPlugins("true")
                .lineSeparator("\n")
                .nrOfIndentSpace(4)
                .testFiles("/PluginDefaultName_input.xml", "/PluginDefaultName_expect.xml");

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(
                thrown.getMessage(),
                is(
                        "The 'true' value in sortPlugins is not supported anymore, please use value 'groupId,artifactId' instead."));
    }

    @Test
    final void deprecatedSortPluginsFalseMessageShouldWork() {
        Executable testMethod = () -> SortPomImplUtil.create()
                .sortPlugins("false")
                .testFiles("/full_unsorted_input.xml", "/full_expected.xml");

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(
                thrown.getMessage(),
                is(
                        "The 'false' value in sortPlugins is not supported anymore, please use empty value '' or omit sortPlugins instead."));
    }

    @Test
    final void deprecatedSortDependenciesTrueMessageShouldWork() {
        Executable testMethod = () -> SortPomImplUtil.create()
                .sortDependencies("true")
                .sortPlugins("true")
                .testFiles("/Simple_input.xml", "/Simple_expected_sortDep.xml");

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(
                thrown.getMessage(),
                is(
                        "The 'true' value in sortDependencies is not supported anymore, please use value 'groupId,artifactId' instead."));
    }

    @Test
    final void deprecatedSortDependenciesFalseMessageShouldWork() {
        Executable testMethod = () -> SortPomImplUtil.create()
                .sortDependencies("false")
                .testFiles("/full_unsorted_input.xml", "/full_expected.xml");

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(
                thrown.getMessage(),
                is(
                        "The 'false' value in sortDependencies is not supported anymore, please use empty value '' or omit sortDependencies instead."));
    }
}
