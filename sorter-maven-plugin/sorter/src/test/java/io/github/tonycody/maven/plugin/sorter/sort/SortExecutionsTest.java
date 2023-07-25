package io.github.tonycody.maven.plugin.sorter.sort;

import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import org.junit.jupiter.api.Test;

class SortExecutionsTest {

    @Test
    final void sortPhaseAndThenId() throws Exception {
        SortPomImplUtil.create()
                .sortExecutions()
                .lineSeparator("\r\n")
                .testFiles("/SortExec_input_simpleWithPhase.xml", "/SortExec_expect_simpleWithPhase.xml");
    }

    @Test
    final void doNotSortIfPhaseAndIdIsSame() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("sortOrderFiles/extra_dummy_tags.xml")
                .sortExecutions()
                .lineSeparator("\r\n")
                .testFiles("/SortExec_input_complexWithPhase.xml", "/SortExec_expect_complexWithPhase.xml");
    }
}
