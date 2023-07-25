package io.github.tonycody.maven.plugin.sorter.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import io.github.tonycody.maven.plugin.sorter.util.FileHelper;
import io.github.tonycody.maven.plugin.sorter.wrapper.operation.HierarchyRootWrapper;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;

class ElementToStringTest {
    @Test
    void testToString() throws Exception {
        String expected = new String(
                new FileInputStream("src/test/resources/Real1_expected_toString.txt").readAllBytes(),
                StandardCharsets.UTF_8);
        assertEquals(expected, getToStringOnRootElementWrapper());
    }

    private String getToStringOnRootElementWrapper() throws IOException, DocumentException {
        PluginParameters pluginParameters = PluginParameters.builder()
                .setOriginalPom(null)
                .setFileOutput(false, new File(""), null, false)
                .setEncoding("UTF-8")
                .setFormatting("\r\n", true, true, true)
                .setIndent(2, false, false)
                .setSortOrder("default_0_4_0.xml", null)
                .setSortEntities(
                        "scope,groupId,artifactId",
                        "groupId,artifactId",
                        "scope,groupId,artifactId",
                        "groupId,artifactId",
                        true,
                        true,
                        true)
                .build();

        FileHelper fileUtil = new FileHelper();
        fileUtil.setup(pluginParameters);

        String xml = new String(
                new FileInputStream("src/test/resources/" + "Real1_input.xml").readAllBytes(), StandardCharsets.UTF_8);
        SAXReader parser = new SAXReader();
        Document document = parser.read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        WrapperFactoryImpl wrapperFactory = new WrapperFactoryImpl(fileUtil);
        wrapperFactory.setup(pluginParameters);
        HierarchyRootWrapper rootWrapper = wrapperFactory.createFromRootElement(document.getRootElement());
        rootWrapper.createWrappedStructure(wrapperFactory);

        return rootWrapper.toString();
    }
}
