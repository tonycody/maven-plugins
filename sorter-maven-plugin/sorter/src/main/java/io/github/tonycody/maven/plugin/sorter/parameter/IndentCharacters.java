package io.github.tonycody.maven.plugin.sorter.parameter;

import io.github.tonycody.maven.plugin.sorter.exception.FailureException;
import java.util.Arrays;

/** Encapsulates functionality to get indent characters from the nrOfIndentSpace parameter */
class IndentCharacters {
    /** Indicates that a tab character should be used instead of spaces. */
    private static final int INDENT_TAB = -1;

    private static final int MAX_INDENT_SPACES = 255;

    private final int nrOfIndentSpace;

    IndentCharacters(int nrOfIndentSpace) {
        this.nrOfIndentSpace = nrOfIndentSpace;
    }

    /**
     * Gets the indent characters from parameter.
     *
     * @return the indent characters
     */
    public String getIndentCharacters() {
        if (nrOfIndentSpace == 0) {
            return "";
        }
        if (nrOfIndentSpace == INDENT_TAB) {
            return "\t";
        }
        if (nrOfIndentSpace < INDENT_TAB || nrOfIndentSpace > MAX_INDENT_SPACES) {
            throw new FailureException("nrOfIndentSpace cannot be below -1 or above 255, was: " + nrOfIndentSpace);
        }
        char[] chars = new char[nrOfIndentSpace];
        Arrays.fill(chars, ' ');
        return new String(chars);
    }
}
