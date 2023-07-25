package io.github.tonycody.maven.plugin.sorter.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import io.github.tonycody.maven.plugin.sorter.util.FileHelper;
import io.github.tonycody.maven.plugin.sorter.wrapper.operation.HierarchyRootWrapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

class CustomSortOrderFileTest {
    @Test
    void compareDefaultSortOrderFileToString() throws Exception {
        String expected = new String(
                new FileInputStream("src/test/resources/sortOrderFiles/with_newline_tagsToString.txt").readAllBytes(),
                StandardCharsets.UTF_8);
        assertEquals(expected, getToStringOnCustomSortOrderFile());
    }

    private String getToStringOnCustomSortOrderFile() throws IOException, DocumentException, SAXException {
        PluginParameters pluginParameters = PluginParameters.builder()
                .setOriginalPom(null)
                .setFileOutput(false, new File(""), null, false)
                .setEncoding("UTF-8")
                .setFormatting("\r\n", true, true, true)
                .setIndent(2, false, false)
                .setSortOrder("src/test/resources/sortOrderFiles/with_newline_tags.xml", null)
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

        WrapperFactoryImpl wrapperFactory = new WrapperFactoryImpl(fileUtil);

        Document documentFromDefaultSortOrderFile = wrapperFactory.createDocumentFromDefaultSortOrderFile();
        new HierarchyRootWrapper(wrapperFactory.create(documentFromDefaultSortOrderFile.getRootElement()));

        HierarchyRootWrapper rootWrapper =
                new HierarchyRootWrapper(wrapperFactory.create(documentFromDefaultSortOrderFile.getRootElement()));
        rootWrapper.createWrappedStructure(wrapperFactory);

        return rootWrapper.toString();
    }
}
