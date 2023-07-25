package io.github.tonycody.maven.plugin.sorter.sort;

import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import org.junit.jupiter.api.Test;

class ReportPluginsTest {

    @Test
    final void sortReportPluginsByArtifactIdWithCustomSortOrderFileShouldWork() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("sortOrderFiles/custom_report_plugins.xml")
                .lineSeparator("\r\n")
                .sortPlugins("artifactId,groupId")
                .testFiles("/ReportPlugins_input.xml", "/ReportPlugins_expected.xml");
    }

    @Test
    final void sortReportPluginsByGroupIdWithCustomSortOrderFileShouldWork() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("sortOrderFiles/custom_report_plugins.xml")
                .lineSeparator("\r\n")
                .sortPlugins("groupId,artifactId")
                .testFiles("/ReportPlugins_input.xml", "/ReportPlugins_expected2.xml");
    }
}
