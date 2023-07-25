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
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import refutils.ReflectionHelper;

/**
 * @author bjorn
 * @since 2012-08-23
 */
class SortMojoTest {
    private final SorterHandler sortPom = mock(SorterHandler.class);
    private final Log log = mock(Log.class);
    private SortMojo sortMojo;

    @BeforeEach
    void setup() {
        sortMojo = new SortMojo();
        ReflectionHelper mojoHelper = new ReflectionHelper(sortMojo);
        mojoHelper.setField(sortPom);
        mojoHelper.setField("lineSeparator", "\n");
    }

    @Test
    void executeShouldStartMojo() throws Exception {
        sortMojo.execute();

        verify(sortPom).initialize(any(SorterLogger.class), any(PluginParameters.class));
        verify(sortPom).sort();
        verifyNoMoreInteractions(sortPom);
    }

    @Test
    void thrownExceptionShouldBeConvertedToMojoException() {
        doThrow(new FailureException("Gurka")).when(sortPom).sort();

        final Executable testMethod = () -> sortMojo.execute();

        final MojoFailureException thrown = assertThrows(MojoFailureException.class, testMethod);

        assertThat("Unexpected message", thrown.getMessage(), is(equalTo("Gurka")));
    }

    @Test
    void thrownExceptionShouldBeConvertedToMojoExceptionInSetup() {
        doThrow(new FailureException("Gurka"))
                .when(sortPom)
                .initialize(any(SorterLogger.class), any(PluginParameters.class));

        final Executable testMethod = () -> sortMojo.execute();

        final MojoFailureException thrown = assertThrows(MojoFailureException.class, testMethod);

        assertThat("Unexpected message", thrown.getMessage(), is(equalTo("Gurka")));
    }

    @Test
    void skipParameterShouldSkipExecution() throws Exception {
        new ReflectionHelper(sortMojo).setField("skip", true);
        new ReflectionHelper(sortMojo).setField(log);

        sortMojo.execute();

        verify(log).info("Skipping Sortpom");
        verifyNoMoreInteractions(sortPom);
    }
}
