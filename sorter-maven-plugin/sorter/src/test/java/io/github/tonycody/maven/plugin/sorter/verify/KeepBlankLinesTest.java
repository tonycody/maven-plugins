package io.github.tonycody.maven.plugin.sorter.verify;

import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import io.github.tonycody.maven.plugin.sorter.util.XmlProcessorTestUtil;
import org.junit.jupiter.api.Test;

class KeepBlankLinesTest {
    @Test
    final void emptyLinesInXmlShouldNotAffectVerify() throws Exception {
        XmlProcessorTestUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .testVerifyXmlIsOrdered("src/test/resources/EmptyRow_input2.xml");
    }

    @Test
    final void emptyLinesInXmlShouldNotAffectVerify2() throws Exception {
        XmlProcessorTestUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .keepBlankLines()
                .testVerifyXmlIsOrdered("src/test/resources/EmptyRow_input2.xml");
    }

    @Test
    final void emptyLinesInXmlAndIndentParameterShouldNotAffectVerify() throws Exception {
        XmlProcessorTestUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .keepBlankLines()
                .indentBlankLines()
                .testVerifyXmlIsOrdered("src/test/resources/EmptyRow_input2.xml");
    }

    @Test
    final void emptyLinesInXmlShouldNotAffectVerify3() throws Exception {
        SortPomImplUtil.create().predefinedSortOrder("default_0_4_0").testVerifyXmlIsOrdered("/EmptyRow_input2.xml");
    }

    @Test
    final void emptyLinesInXmlAndIndentParameterShouldNotAffectVerify2() throws Exception {
        SortPomImplUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .indentBLankLines()
                .testVerifyXmlIsOrdered("/EmptyRow_input2.xml");
    }

    @Test
    final void unsortedXmlAndIndentParameterShouldAffectVerify() throws Exception {
        SortPomImplUtil.create()
                .indentBLankLines()
                .testVerifyXmlIsNotOrdered(
                        "/EmptyRow_input.xml", "The xml element <modelVersion> should be placed before <artifactId>");
    }

    @Test
    final void simpleLineBreaksShouldNotAffectVerify() throws Exception {
        XmlProcessorTestUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .keepBlankLines()
                .testVerifyXmlIsOrdered("src/test/resources/LineBreak_input2.xml");
    }

    @Test
    final void unsortedXmlShouldAffectVerify() throws Exception {
        XmlProcessorTestUtil.create()
                .keepBlankLines()
                .testVerifyXmlIsNotOrdered(
                        "src/test/resources/LineBreak_input.xml",
                        "The xml element <modelVersion> should be placed before <artifactId>");
    }
}
