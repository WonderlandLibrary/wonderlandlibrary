/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.plugins.visitors;

import java.lang.annotation.Annotation;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginVisitorStrategy;
import org.apache.logging.log4j.core.config.plugins.visitors.PluginVisitor;
import org.apache.logging.log4j.status.StatusLogger;

public final class PluginVisitors {
    private static final Logger LOGGER = StatusLogger.getLogger();

    private PluginVisitors() {
    }

    public static PluginVisitor<? extends Annotation> findVisitor(Class<? extends Annotation> annotation) {
        PluginVisitorStrategy strategy = annotation.getAnnotation(PluginVisitorStrategy.class);
        if (strategy == null) {
            return null;
        }
        try {
            return strategy.value().newInstance();
        }
        catch (Exception e) {
            LOGGER.error("Error loading PluginVisitor [{}] for annotation [{}].", strategy.value(), annotation, e);
            return null;
        }
    }
}

