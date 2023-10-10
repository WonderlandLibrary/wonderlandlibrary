// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.Logger;

public class AsyncQueueFullPolicyFactory
{
    static final String PROPERTY_NAME_ASYNC_EVENT_ROUTER = "log4j2.AsyncQueueFullPolicy";
    static final String PROPERTY_VALUE_DEFAULT_ASYNC_EVENT_ROUTER = "Default";
    static final String PROPERTY_VALUE_DISCARDING_ASYNC_EVENT_ROUTER = "Discard";
    static final String PROPERTY_NAME_DISCARDING_THRESHOLD_LEVEL = "log4j2.DiscardThreshold";
    private static final Logger LOGGER;
    
    public static AsyncQueueFullPolicy create() {
        final String router = PropertiesUtil.getProperties().getStringProperty("log4j2.AsyncQueueFullPolicy");
        if (router == null || isRouterSelected(router, DefaultAsyncQueueFullPolicy.class, "Default")) {
            return new DefaultAsyncQueueFullPolicy();
        }
        if (isRouterSelected(router, DiscardingAsyncQueueFullPolicy.class, "Discard")) {
            return createDiscardingAsyncQueueFullPolicy();
        }
        return createCustomRouter(router);
    }
    
    private static boolean isRouterSelected(final String propertyValue, final Class<? extends AsyncQueueFullPolicy> policy, final String shortPropertyValue) {
        return propertyValue != null && (shortPropertyValue.equalsIgnoreCase(propertyValue) || policy.getName().equals(propertyValue) || policy.getSimpleName().equals(propertyValue));
    }
    
    private static AsyncQueueFullPolicy createCustomRouter(final String router) {
        try {
            final Class<? extends AsyncQueueFullPolicy> cls = Loader.loadClass(router).asSubclass(AsyncQueueFullPolicy.class);
            AsyncQueueFullPolicyFactory.LOGGER.debug("Creating custom AsyncQueueFullPolicy '{}'", router);
            return (AsyncQueueFullPolicy)cls.newInstance();
        }
        catch (Exception ex) {
            AsyncQueueFullPolicyFactory.LOGGER.debug("Using DefaultAsyncQueueFullPolicy. Could not create custom AsyncQueueFullPolicy '{}': {}", router, ex.toString());
            return new DefaultAsyncQueueFullPolicy();
        }
    }
    
    private static AsyncQueueFullPolicy createDiscardingAsyncQueueFullPolicy() {
        final PropertiesUtil util = PropertiesUtil.getProperties();
        final String level = util.getStringProperty("log4j2.DiscardThreshold", Level.INFO.name());
        final Level thresholdLevel = Level.toLevel(level, Level.INFO);
        AsyncQueueFullPolicyFactory.LOGGER.debug("Creating custom DiscardingAsyncQueueFullPolicy(discardThreshold:{})", thresholdLevel);
        return new DiscardingAsyncQueueFullPolicy(thresholdLevel);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
