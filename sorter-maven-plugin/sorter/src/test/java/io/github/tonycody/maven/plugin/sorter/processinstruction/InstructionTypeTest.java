package io.github.tonycody.maven.plugin.sorter.processinstruction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * @author bjorn
 * @since 2013-12-28
 */
class InstructionTypeTest {
    @Test
    void nextAfterIgnoreShouldBeResume() {
        MatcherAssert.assertThat(InstructionType.IGNORE.next(), Matchers.is(InstructionType.RESUME));
        MatcherAssert.assertThat(InstructionType.IGNORE.next().next(), Matchers.is(InstructionType.IGNORE));
    }

    @Test
    void nextAfterResumeShouldBeIgnore() {
        MatcherAssert.assertThat(InstructionType.RESUME.next(), Matchers.is(InstructionType.IGNORE));
        MatcherAssert.assertThat(InstructionType.RESUME.next().next(), Matchers.is(InstructionType.RESUME));
    }

    @Test
    void testContainsType() {
        assertThat(InstructionType.containsType("ignore"), is(true));
        assertThat(InstructionType.containsType("IGNORE"), is(true));
        assertThat(InstructionType.containsType("IgNoRe"), is(true));
        assertThat(InstructionType.containsType("resume"), is(true));
        assertThat(InstructionType.containsType("RESUME"), is(true));
        assertThat(InstructionType.containsType("rEsUmE"), is(true));
        assertThat(InstructionType.containsType("token"), is(false));
        assertThat(InstructionType.containsType("gurka"), is(false));
    }

    @Test
    void testMatches() {
        MatcherAssert.assertThat(InstructionType.IGNORE.matches("ignore"), is(true));
        MatcherAssert.assertThat(InstructionType.IGNORE.matches("IGNORE"), is(true));
        MatcherAssert.assertThat(InstructionType.IGNORE.matches("IgNoRe"), is(true));
        MatcherAssert.assertThat(InstructionType.IGNORE.matches("resume"), is(false));

        MatcherAssert.assertThat(InstructionType.RESUME.matches("resume"), is(true));
        MatcherAssert.assertThat(InstructionType.RESUME.matches("RESUME"), is(true));
        MatcherAssert.assertThat(InstructionType.RESUME.matches("rEsUmE"), is(true));
        MatcherAssert.assertThat(InstructionType.RESUME.matches("token"), is(false));
    }
}
