/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal.logging;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.JdkLoggerFactory;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

public abstract class InternalLoggerFactory {
    private static volatile InternalLoggerFactory defaultFactory = InternalLoggerFactory.newDefaultFactory(InternalLoggerFactory.class.getName());

    protected abstract InternalLogger newInstance(String var1);

    public static InternalLoggerFactory getDefaultFactory() {
        return defaultFactory;
    }

    private static InternalLoggerFactory newDefaultFactory(String string) {
        InternalLoggerFactory internalLoggerFactory;
        try {
            internalLoggerFactory = new Slf4JLoggerFactory(true);
            internalLoggerFactory.newInstance(string).debug("Using SLF4J as the default logging framework");
        }
        catch (Throwable throwable) {
            try {
                internalLoggerFactory = new Log4JLoggerFactory();
                internalLoggerFactory.newInstance(string).debug("Using Log4J as the default logging framework");
            }
            catch (Throwable throwable2) {
                internalLoggerFactory = new JdkLoggerFactory();
                internalLoggerFactory.newInstance(string).debug("Using java.util.logging as the default logging framework");
            }
        }
        return internalLoggerFactory;
    }

    public static InternalLogger getInstance(Class<?> clazz) {
        return InternalLoggerFactory.getInstance(clazz.getName());
    }

    public static void setDefaultFactory(InternalLoggerFactory internalLoggerFactory) {
        if (internalLoggerFactory == null) {
            throw new NullPointerException("defaultFactory");
        }
        defaultFactory = internalLoggerFactory;
    }

    public static InternalLogger getInstance(String string) {
        return InternalLoggerFactory.getDefaultFactory().newInstance(string);
    }
}

