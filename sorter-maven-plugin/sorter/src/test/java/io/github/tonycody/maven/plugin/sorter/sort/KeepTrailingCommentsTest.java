package io.github.tonycody.maven.plugin.sorter.sort;

import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import org.junit.jupiter.api.Test;

class KeepTrailingCommentsTest {
    @Test
    final void commentsInIgnoreSectionShouldNotBeFormatted() throws Exception {
        SortPomImplUtil.create()
                .sortDependencies("scope,groupId,artifactId")
                .lineSeparator("\r\n")
                .testFiles("/TrailingComment_input.xml", "/TrailingComment_expected.xml");
    }
}
