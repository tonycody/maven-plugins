package io.github.tonycody.maven.plugin.sorter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import io.github.tonycody.maven.plugin.sorter.exception.FailureException;
import io.github.tonycody.maven.plugin.sorter.logger.SorterLogger;
import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import refutils.ReflectionHelper;

/**
 * @author bjorn
 * @since 2012-08-23
 */
class VerifyMojoTest {
    private final SorterHandler sort = mock(SorterHandler.class);
    private CheckMojo verifyMojo;

    @BeforeEach
    void setup() {
        verifyMojo = new CheckMojo();
        ReflectionHelper mojoHelper = new ReflectionHelper(verifyMojo);
        mojoHelper.setField(sort);
        mojoHelper.setField("lineSeparator", "\n");
        mojoHelper.setField("verifyFail", "SORT");
        mojoHelper.setField("verifyFailOn", "xmlElements");
    }

    @Test
    void executeShouldStartMojo() throws Exception {
        verifyMojo.execute();

        verify(sort).initialize(any(SorterLogger.class), any(PluginParameters.class));
        verify(sort).check();
        verifyNoMoreInteractions(sort);
    }

    @Test
    void thrownExceptionShouldBeConvertedToMojoExceptionInExecute() {
        doThrow(new FailureException("Gurka")).when(sort).check();

        final Executable testMethod = () -> verifyMojo.execute();

        final MojoFailureException thrown = assertThrows(MojoFailureException.class, testMethod);

        assertThat("Unexpected message", thrown.getMessage(), is(equalTo("Gurka")));
    }

    @Test
    void thrownExceptionShouldBeConvertedToMojoExceptionInSetup() {
        doThrow(new FailureException("Gurka"))
                .when(sort)
                .initialize(any(SorterLogger.class), any(PluginParameters.class));

        final Executable testMethod = () -> verifyMojo.execute();

        final MojoFailureException thrown = assertThrows(MojoFailureException.class, testMethod);

        assertThat("Unexpected message", thrown.getMessage(), is(equalTo("Gurka")));
    }

    @Test
    void skipParameterShouldSkipExecution() throws Exception {
        new ReflectionHelper(verifyMojo).setField("skip", true);

        verifyMojo.execute();

        verifyNoMoreInteractions(sort);
    }
}
