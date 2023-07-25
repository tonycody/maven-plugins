package io.github.tonycody.maven.plugin.sorter.sort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import io.github.tonycody.maven.plugin.sorter.util.XmlProcessorTestUtil;
import org.junit.jupiter.api.Test;

class KeepBlankLinesTest {
    @Test
    final void emptyRowsInSimplePomShouldBePreserved() throws Exception {
        XmlProcessorTestUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .keepBlankLines()
                .testInputAndExpected(
                        "src/test/resources/EmptyRow_input.xml", "src/test/resources/EmptyRow_expected.xml");
    }

    @Test
    final void emptyRowsInLargePomShouldBePreserved1() throws Exception {
        XmlProcessorTestUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .keepBlankLines()
                .testInputAndExpected(
                        "src/test/resources/Real1_input.xml", "src/test/resources/Real1_expected_keepBlankLines.xml");
    }

    @Test
    final void emptyRowsInLargePomShouldBePreservedAndIndented1() throws Exception {
        XmlProcessorTestUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .keepBlankLines()
                .indentBlankLines()
                .testInputAndExpected(
                        "src/test/resources/Real1_input.xml",
                        "src/test/resources/Real1_expected_keepBlankLines_indented.xml");
    }

    @Test
    final void emptyRowsInLargePomShouldBePreserved2() throws Exception {
        SortPomImplUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .testFiles("/Real1_input.xml", "/Real1_expected_keepBlankLines.xml");
    }

    @Test
    final void emptyRowsInLargePomShouldBePreservedAndIndented2() throws Exception {
        SortPomImplUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .indentBLankLines()
                .testFiles("/Real1_input.xml", "/Real1_expected_keepBlankLines_indented.xml");
    }

    @Test
    final void simpleLineBreaksShouldNotBePreserved() throws Exception {
        XmlProcessorTestUtil.create()
                .keepBlankLines()
                .testInputAndExpected(
                        "src/test/resources/LineBreak_input.xml", "src/test/resources/Character_expected.xml");
    }

    @Test
    final void allLineBreaksInXmlShouldBeNewlines() throws Exception {
        String actual = XmlProcessorTestUtil.create()
                .keepBlankLines()
                .lineSeparator("\n")
                .sortXmlAndReturnResult("src/test/resources/LineBreak_N_input.xml");

        assertThat(actual, containsString("</groupId>\n  <artifactId>whitespace-test</artifactId>\n  <version>"));
        assertThat(actual, containsString(",\nembedded,\nand"));
    }

    @Test
    final void allLineBreaksInXmlShouldBeCarriageReturnNewlines() throws Exception {
        String actual = XmlProcessorTestUtil.create()
                .keepBlankLines()
                .lineSeparator("\r\n")
                .sortXmlAndReturnResult("src/test/resources/LineBreak_N_input.xml");

        assertThat(actual, containsString("</groupId>\r\n  <artifactId>whitespace-test</artifactId>\r\n  <version>"));
        assertThat(actual, containsString(",\r\nembedded,\r\nand"));
    }

    @Test
    final void allLineBreaksInXmlShouldBeCarriageReturn() throws Exception {
        String actual = XmlProcessorTestUtil.create()
                .keepBlankLines()
                .lineSeparator("\r")
                .sortXmlAndReturnResult("src/test/resources/LineBreak_N_input.xml");

        assertThat(actual, containsString("</groupId>\r  <artifactId>whitespace-test</artifactId>\r  <version>"));
        assertThat(actual, containsString(",\rembedded,\rand"));
    }

    @Test
    final void allLineBreaksInXmlShouldBeNewlines2() throws Exception {
        String actual = XmlProcessorTestUtil.create()
                .keepBlankLines()
                .lineSeparator("\n")
                .sortXmlAndReturnResult("src/test/resources/LineBreak_RN_input.xml");

        assertThat(actual, containsString("</groupId>\n  <artifactId>whitespace-test</artifactId>\n  <version>"));
        assertThat(actual, containsString(",\nembedded,\nand"));
    }

    @Test
    final void allLineBreaksInXmlShouldBeCarriageReturnNewlines2() throws Exception {
        String actual = XmlProcessorTestUtil.create()
                .keepBlankLines()
                .lineSeparator("\r\n")
                .sortXmlAndReturnResult("src/test/resources/LineBreak_RN_input.xml");

        assertThat(actual, containsString("</groupId>\r\n  <artifactId>whitespace-test</artifactId>\r\n  <version>"));
        assertThat(actual, containsString(",\r\nembedded,\r\nand"));
    }

    @Test
    final void allLineBreaksInXmlShouldBeCarriageReturn2() throws Exception {
        String actual = XmlProcessorTestUtil.create()
                .keepBlankLines()
                .lineSeparator("\r")
                .sortXmlAndReturnResult("src/test/resources/LineBreak_RN_input.xml");

        assertThat(actual, containsString("</groupId>\r  <artifactId>whitespace-test</artifactId>\r  <version>"));
        assertThat(actual, containsString(",\rembedded,\rand"));
    }

    @Test
    final void artifactIdsWithLineBreaksShouldBeTrimmedBeforeSorting() throws Exception {
        XmlProcessorTestUtil.create()
                .lineSeparator("\n")
                .keepBlankLines()
                .expandEmptyElements(false)
                .sortModules()
                .sortDependencies("scope,groupId,artifactId")
                .sortProperties()
                .sortPlugins("groupId,artifactId")
                .noSpaceBeforeCloseEmptyElement()
                .testInputAndExpected(
                        "src/test/resources/LineBreak_input3.xml", "src/test/resources/LineBreak_expected3.xml");
    }
}
