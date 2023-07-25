package io.github.tonycody.maven.plugin.sorter.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import org.junit.jupiter.api.Test;

/**
 * @author bjorn
 * @since 2013-08-16
 */
class FileUtilTest {
    @Test
    void defaultSortOrderFromFileShouldWork() throws Exception {
        FileHelper fileUtil = createFileUtil("Attribute_expected.xml");

        String defaultSortOrderXml = fileUtil.getDefaultSortOrderXml();
        assertThat(defaultSortOrderXml, startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<projec"));
    }

    @Test
    void defaultSortOrderFromNonExistingShouldThrowException() {
        FileHelper fileUtil = createFileUtil("zzz_Attribute_expected.xml");

        final IOException thrown = assertThrows(IOException.class, fileUtil::getDefaultSortOrderXml);

        assertThat(thrown.getMessage(), startsWith("Could not find"));
        assertThat(thrown.getMessage(), endsWith("or zzz_Attribute_expected.xml in classpath"));
    }

    @Test
    void defaultSortOrderFromUrlShouldWork() throws IOException {
        FileHelper fileUtil = createFileUtil("https://google.com");

        try {
            String defaultSortOrderXml = fileUtil.getDefaultSortOrderXml();
            assertThat(defaultSortOrderXml, containsString("google"));
        } catch (UnknownHostException e) {
            // This is ok, we were not online when the test was performed
            // Which actually makes this test a bit pointless :-(
        }
    }

    @Test
    void defaultSortOrderFromNonExistingHostShouldThrowException() {
        FileHelper fileUtil = createFileUtil("http://jgerwzuujy.fjrmzaxklj.zfgmqavbhp/licenses/BSD-3-Clause");

        final UnknownHostException thrown = assertThrows(UnknownHostException.class, fileUtil::getDefaultSortOrderXml);

        assertThat(thrown.getMessage(), is("jgerwzuujy.fjrmzaxklj.zfgmqavbhp"));
    }

    @Test
    void defaultSortOrderFromNonExistingPageShouldThrowException() throws IOException {
        FileHelper fileUtil = createFileUtil("https://github.com/Ekryd/sortpom/where_are_the_donations");

        try {
            fileUtil.getDefaultSortOrderXml();
            fail();
        } catch (UnknownHostException e) {
            // This is ok, we were not online when the test was performed
        } catch (FileNotFoundException e) {
            assertThat(e.getMessage(), is("https://github.com/Ekryd/sortpom/where_are_the_donations"));
        }
    }

    private FileHelper createFileUtil(String customSortOrderFile) {
        FileHelper fileUtil = new FileHelper();
        PluginParameters pluginParameters = PluginParameters.builder()
                .setSortOrder(customSortOrderFile, null)
                .setEncoding("UTF-8")
                .build();
        fileUtil.setup(pluginParameters);
        return fileUtil;
    }
}
