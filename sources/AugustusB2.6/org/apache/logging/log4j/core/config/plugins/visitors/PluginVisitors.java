// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins.visitors;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginVisitorStrategy;
import java.lang.annotation.Annotation;
import org.apache.logging.log4j.Logger;

public final class PluginVisitors
{
    private static final Logger LOGGER;
    
    private PluginVisitors() {
    }
    
    public static PluginVisitor<? extends Annotation> findVisitor(final Class<? extends Annotation> annotation) {
        final PluginVisitorStrategy strategy = annotation.getAnnotation(PluginVisitorStrategy.class);
        if (strategy == null) {
            return null;
        }
        try {
            return (PluginVisitor<? extends Annotation>)strategy.value().newInstance();
        }
        catch (Exception e) {
            PluginVisitors.LOGGER.error("Error loading PluginVisitor [{}] for annotation [{}].", strategy.value(), annotation, e);
            return null;
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
