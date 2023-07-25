package io.github.tonycody.maven.plugin.sorter.sort;

import static io.github.tonycody.maven.plugin.sorter.sort.XmlFragment.createXmlFragment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import io.github.tonycody.maven.plugin.sorter.output.XmlOutputGenerator;
import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ExpandEmptyElementTest {
    private static Stream<Arguments> testValues() {
        return Stream.of(
                // expandEmptyElements, spaceBeforeCloseEmptyElement, expected
                arguments("true", "true", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka></Gurka>\n"),
                arguments("false", "true", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka />\n"),
                arguments("false", "false", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka/>\n"),

                // The spaceBeforeCloseEmptyElement does not affect expanded elements
                arguments("true", "false", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka></Gurka>\n"));
    }

    @ParameterizedTest
    @MethodSource("testValues")
    void expandEmptyElementsAndKeepSpaceShouldAffectOutputXml(
            boolean expandEmptyElements, boolean spaceBeforeCloseEmptyElement, String expectedValue) {
        XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator();
        xmlOutputGenerator.setup(PluginParameters.builder()
                .setEncoding("UTF-8")
                .setFormatting("\n", expandEmptyElements, spaceBeforeCloseEmptyElement, false)
                .setIndent(2, false, false)
                .build());

        String actual = xmlOutputGenerator.getSortedXml(createXmlFragment());
        assertEquals(expectedValue, actual);
    }
}
