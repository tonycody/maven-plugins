package io.github.tonycody.maven.plugin.sorter.parameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.tonycody.maven.plugin.sorter.exception.FailureException;
import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import java.io.File;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class ViolationFileParameterTest {

    private static final String FILENAME_WITH_DIRECTORIES = "target/sortpom_reports/1/violation.xml";
    private static final String FILENAME_WITHOUT_DIRECTORIES = "target/violation.xml";

    @BeforeEach
    void setUp() {
        //noinspection ResultOfMethodCallIgnored
        new File(FILENAME_WITHOUT_DIRECTORIES).delete();
    }

    @Test
    final void violationFileCanBeOverwritten() throws Exception {
        File tempFile = File.createTempFile("violation", ".xml", new File("target"));
        SortPomImplUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .violationFile(tempFile.getAbsolutePath())
                .testVerifySort(
                        "/full_unsorted_input.xml",
                        "/full_expected.xml",
                        "[INFO] The xml element <modelVersion> should be placed before <parent>",
                        true);
    }

    @Test
    final void readOnlyViolationFileShouldReportError() throws Exception {
        File tempFile = File.createTempFile("violation", ".xml", new File("target"));
        assertTrue(tempFile.setReadOnly());

        final Executable testMethod = () -> SortPomImplUtil.create()
                .violationFile(tempFile.getAbsolutePath())
                .testVerifySort(
                        "/full_unsorted_input.xml",
                        "/full_expected.xml",
                        "[INFO] The xml element <modelVersion> should be placed before <parent>",
                        true);

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("Could not save violation file: " + tempFile.getAbsolutePath())));
    }

    @Test
    final void violationFileShouldBeCreatedOnVerificationStop() {
        SortPomImplUtil.create()
                .verifyFail("Stop")
                .violationFile(FILENAME_WITHOUT_DIRECTORIES)
                .testVerifyFail(
                        "/full_unsorted_input.xml",
                        FailureException.class,
                        "[ERROR] The xml element <modelVersion> should be placed before <parent>",
                        true);
        File file = new File(FILENAME_WITHOUT_DIRECTORIES);
        assertThat(file.exists(), is(true));
    }

    @Test
    final void violationFileWithParentDirectoryShouldBeCreatedOnVerificationWarn() throws Exception {
        SortPomImplUtil.create()
                .verifyFail("Warn")
                .violationFile(FILENAME_WITH_DIRECTORIES)
                .testVerifyWarn(
                        "/full_unsorted_input.xml",
                        "[WARNING] The xml element <modelVersion> should be placed before <parent>",
                        true);
        File file = new File(FILENAME_WITH_DIRECTORIES);
        assertThat(file.exists(), is(true));
    }

    @Test
    final void violationFileContentShouldBeEncodedOnVerificationSort() throws Exception {
        SortPomImplUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .verifyFail("Sort")
                .violationFile(FILENAME_WITH_DIRECTORIES)
                .testVerifySort(
                        "/full_unsorted_input.xml",
                        "/full_expected.xml",
                        "[INFO] The xml element <modelVersion> should be placed before <parent>",
                        true);
        String xml = FileUtils.readFileToString(new File(FILENAME_WITH_DIRECTORIES), Charset.defaultCharset());
        assertThat(xml, containsString("The xml element &lt;modelVersion&gt; should be placed before &lt;parent&gt;"));
    }
}
