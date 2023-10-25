/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender;

import java.util.Objects;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.config.Configuration;

public abstract class HttpManager
extends AbstractManager {
    private final Configuration configuration;

    protected HttpManager(Configuration configuration, LoggerContext loggerContext, String name) {
        super(loggerContext, name);
        this.configuration = Objects.requireNonNull(configuration);
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public void startup() {
    }

    public abstract void send(Layout<?> var1, LogEvent var2) throws Exception;
}

