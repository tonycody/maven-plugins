package io.github.tonycody.maven.plugin.sorter.processinstruction;

import io.github.tonycody.maven.plugin.sorter.logger.SorterLogger;
import java.util.regex.Matcher;

/**
 * Check the pom file for processing instructions and verifies that they are correct and balanced
 *
 * @author bjorn
 * @since 2013-12-28
 */
class SorterPiScanner {
    private final SorterLogger logger;
    private InstructionType expectedNextInstruction = InstructionType.IGNORE;
    private String errorString;
    private boolean containsIgnoredSections = false;

    public SorterPiScanner(SorterLogger logger) {
        this.logger = logger;
    }

    /** Scan and verifies the pom file for processing instructions */
    public void scan(String originalXml) {
        Matcher matcher = InstructionType.INSTRUCTION_PATTERN.matcher(originalXml);
        while (matcher.find()) {
            scanOneInstruction(matcher.group(1));
            containsIgnoredSections = true;
        }
        if (expectedNextInstruction != InstructionType.IGNORE) {
            addError(String.format(
                    "Xml processing instructions for sortpom was not properly terminated. Every <?sortpom %s?> must be followed with <?sortpom %s?>",
                    InstructionType.IGNORE, InstructionType.RESUME));
        }
    }

    private void scanOneInstruction(String instruction) {
        if (!InstructionType.containsType(instruction)) {
            addError(String.format(
                    "Xml contained unknown sortpom instruction '%s'. Please use <?sortpom %s?> or <?sortpom %s?>",
                    instruction, InstructionType.IGNORE, InstructionType.RESUME));
        } else {
            if (!expectedNextInstruction.matches(instruction)) {
                addError(String.format(
                        "Xml contained unexpected sortpom instruction '%s'. Please use expected instruction <?sortpom %s?>",
                        instruction, expectedNextInstruction));
            } else {
                expectedNextInstruction = expectedNextInstruction.next();
            }
        }
    }

    private void addError(String msg) {
        if (errorString == null) {
            errorString = msg;
        }
        logger.error(msg);
    }

    public boolean isScanError() {
        return errorString != null;
    }

    public String getFirstError() {
        return errorString;
    }

    /** Returns true if ignored sections where found the pon file */
    public boolean containsIgnoredSections() {
        return containsIgnoredSections;
    }
}
