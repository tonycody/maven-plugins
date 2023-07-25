package io.github.tonycody.maven.plugin.sorter.wrapper;

import io.github.tonycody.maven.plugin.sorter.content.NewlineText;
import io.github.tonycody.maven.plugin.sorter.parameter.PluginParameters;
import io.github.tonycody.maven.plugin.sorter.wrapper.content.SingleNewlineInTextWrapper;
import io.github.tonycody.maven.plugin.sorter.wrapper.content.UnsortedWrapper;
import io.github.tonycody.maven.plugin.sorter.wrapper.content.Wrapper;
import org.dom4j.Node;
import org.dom4j.Text;

/**
 * @author bjorn
 * @since 2012-05-19
 */
public class TextWrapperCreator {
    private boolean keepBlankLines;

    public void setup(PluginParameters pluginParameters) {
        keepBlankLines = pluginParameters.keepBlankLines;
    }

    Wrapper<Node> createWrapper(Text text) {
        if (isSingleNewLine(text)) {
            return SingleNewlineInTextWrapper.INSTANCE;
        } else if (isBlankLineOrLines(text)) {
            return new UnsortedWrapper<>(new NewlineText());
        }
        return new UnsortedWrapper<>(text);
    }

    private boolean isSingleNewLine(Text content) {
        return content.getText().matches("[\\t ]*\\r?\\n?[\\t ]*");
    }

    boolean isBlankLineOrLines(Text content) {
        if (!keepBlankLines) {
            return false;
        }
        return content.getText().matches("^\\s*?([\\r\\n])\\s*$");
    }
}
