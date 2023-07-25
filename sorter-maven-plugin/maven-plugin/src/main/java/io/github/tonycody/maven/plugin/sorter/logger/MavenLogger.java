package io.github.tonycody.maven.plugin.sorter.logger;

import org.apache.maven.plugin.logging.Log;

/**
 * @author bjorn
 * @since 2012-12-22
 */
public class MavenLogger implements SorterLogger {
    private final Log pluginLogger;

    public MavenLogger(Log pluginLogger) {
        this.pluginLogger = pluginLogger;
    }

    @Override
    public void warn(String content) {
        pluginLogger.warn(content);
    }

    @Override
    public void info(String content) {
        pluginLogger.info(content);
    }

    @Override
    public void error(String content) {
        pluginLogger.error(content);
    }
}
