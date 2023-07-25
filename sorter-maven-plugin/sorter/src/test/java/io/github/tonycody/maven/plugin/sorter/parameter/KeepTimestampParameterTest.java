package io.github.tonycody.maven.plugin.sorter.parameter;

import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import org.junit.jupiter.api.Test;

class KeepTimestampParameterTest {

    @Test
    final void whenKeepTimestampNotSetTimestampsShouldDiffer() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("difforder/differentOrder.xml")
                .lineSeparator("\n")
                .keepTimestamp(false)
                .testFilesWithTimestamp("/full_unsorted_input.xml", "/sortOrderFiles/sorted_differentOrder.xml");
    }

    @Test
    final void whenKeepTimestampIsSetTimestampsShouldRemain() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("difforder/differentOrder.xml")
                .lineSeparator("\n")
                .keepTimestamp(true)
                .testFilesWithTimestamp("/full_unsorted_input.xml", "/sortOrderFiles/sorted_differentOrder.xml");
    }
}
