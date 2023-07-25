package io.github.tonycody.maven.plugin.sorter.parameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.tonycody.maven.plugin.sorter.exception.FailureException;
import io.github.tonycody.maven.plugin.sorter.util.SortPomImplUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class BackupFileExtensionParameterTest {

    @Test
    final void emptyBackupFileExtensionShouldNotWork() {

        final Executable testMethod = () -> SortPomImplUtil.create()
                .backupFileExtension("")
                .customSortOrderFile("difforder/differentOrder.xml")
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml", "/sortOrderFiles/sorted_differentOrder.xml");

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("Could not create backup file, extension name was empty")));
    }
}
