package io.github.tonycody.maven.plugin.sorter.sort;

import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import org.junit.jupiter.api.Test;

class IgnoreSectionsTest {
    @Test
    void forceDependencyToTopTrickShouldWork() throws Exception {
        SortPomImplUtil.create()
                .sortDependencies("scope,groupId,artifactId")
                .lineSeparator("\n")
                .testFiles("/DependencyToTop_input.xml", "/DependencyToTop_expected.xml");
    }
}
