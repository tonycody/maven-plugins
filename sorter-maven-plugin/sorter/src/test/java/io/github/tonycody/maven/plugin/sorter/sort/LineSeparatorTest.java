package io.github.tonycody.maven.plugin.sorter.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.tonycody.maven.plugin.sorter.output.XmlOutputGenerator;
import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LineSeparatorTest {
    @ParameterizedTest
    @ValueSource(strings = {"\n", "\r", "\r\n"})
    void formattingXmlWithLineEndingsShouldResultInOneLineBreakAtEnd(String lineSeparator) {
        XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator();
        xmlOutputGenerator.setup(PluginParameters.builder()
                .setEncoding("UTF-8")
                .setFormatting(lineSeparator, false, true, false)
                .setIndent(2, false, false)
                .build());

        String actual = xmlOutputGenerator.getSortedXml(XmlFragment.createXmlFragment());
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + lineSeparator + "<Gurka />" + lineSeparator, actual);
    }

    @Test
    void linesInContentShouldBePreserved() throws Exception {
        SortPomImplUtil.create()
                .lineSeparator("\r\n")
                .testFiles("/MultilineContent_input.xml", "/MultilineContent_expected.xml");
    }
}
