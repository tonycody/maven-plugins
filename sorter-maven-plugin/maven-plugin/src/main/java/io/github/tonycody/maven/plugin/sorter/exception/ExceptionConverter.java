package io.github.tonycody.maven.plugin.sorter.exception;

import org.apache.maven.plugin.MojoFailureException;

/**
 * Converts internal runtime FailureException in a method to a MojoFailureException in order to give
 * nice output to the Maven framework
 */
public class ExceptionConverter {
    /** 方法 */
    private final Runnable method;

    public ExceptionConverter(Runnable method) {
        this.method = method;
    }

    /**
     * 获得实例
     *
     * @param method 方法
     * @return {@link ExceptionConverter}
     */
    public static final ExceptionConverter getInstance(Runnable method) {
        return new ExceptionConverter(method);
    }

    public void executeAndConvertException() throws MojoFailureException {
        try {
            method.run();
        } catch (FailureException fex) {
            if (fex.getCause() != null) {
                throw new MojoFailureException(fex.getMessage(), fex.getCause());
            } else {
                throw new MojoFailureException(fex.getMessage());
            }
        }
    }
}
