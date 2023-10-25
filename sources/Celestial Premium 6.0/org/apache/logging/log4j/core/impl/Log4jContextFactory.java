/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.impl;

import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.jmx.Server;
import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;
import org.apache.logging.log4j.core.selector.ContextSelector;
import org.apache.logging.log4j.spi.LoggerContextFactory;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public class Log4jContextFactory
implements LoggerContextFactory {
    private static final StatusLogger LOGGER = StatusLogger.getLogger();
    private ContextSelector selector;

    public Log4jContextFactory() {
        String sel = PropertiesUtil.getProperties().getStringProperty("Log4jContextSelector");
        if (sel != null) {
            try {
                Class<?> clazz = Loader.loadClass(sel);
                if (clazz != null && ContextSelector.class.isAssignableFrom(clazz)) {
                    this.selector = (ContextSelector)clazz.newInstance();
                }
            }
            catch (Exception ex) {
                LOGGER.error("Unable to create context " + sel, (Throwable)ex);
            }
        }
        if (this.selector == null) {
            this.selector = new ClassLoaderContextSelector();
        }
        try {
            Server.registerMBeans(this.selector);
        }
        catch (Exception ex) {
            LOGGER.error("Could not start JMX", (Throwable)ex);
        }
    }

    public ContextSelector getSelector() {
        return this.selector;
    }

    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext) {
        LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext);
        if (ctx.getStatus() == LoggerContext.Status.INITIALIZED) {
            ctx.start();
        }
        return ctx;
    }

    @Override
    public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
        LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, configLocation);
        if (ctx.getStatus() == LoggerContext.Status.INITIALIZED) {
            ctx.start();
        }
        return ctx;
    }

    @Override
    public void removeContext(org.apache.logging.log4j.spi.LoggerContext context) {
        if (context instanceof LoggerContext) {
            this.selector.removeContext((LoggerContext)context);
        }
    }
}

