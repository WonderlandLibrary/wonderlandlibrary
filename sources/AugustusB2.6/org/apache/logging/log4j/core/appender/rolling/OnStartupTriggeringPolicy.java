// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.LogEvent;
import java.lang.reflect.Method;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.util.Loader;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "OnStartupTriggeringPolicy", category = "Core", printObject = true)
public class OnStartupTriggeringPolicy extends AbstractTriggeringPolicy
{
    private static final long JVM_START_TIME;
    private final long minSize;
    
    private OnStartupTriggeringPolicy(final long minSize) {
        this.minSize = minSize;
    }
    
    private static long initStartTime() {
        try {
            final Class<?> factoryClass = Loader.loadSystemClass("java.lang.management.ManagementFactory");
            final Method getRuntimeMXBean = factoryClass.getMethod("getRuntimeMXBean", (Class<?>[])new Class[0]);
            final Object runtimeMXBean = getRuntimeMXBean.invoke(null, new Object[0]);
            final Class<?> runtimeMXBeanClass = Loader.loadSystemClass("java.lang.management.RuntimeMXBean");
            final Method getStartTime = runtimeMXBeanClass.getMethod("getStartTime", (Class<?>[])new Class[0]);
            final Long result = (Long)getStartTime.invoke(runtimeMXBean, new Object[0]);
            return result;
        }
        catch (Throwable t) {
            StatusLogger.getLogger().error("Unable to call ManagementFactory.getRuntimeMXBean().getStartTime(), using system time for OnStartupTriggeringPolicy", t);
            return System.currentTimeMillis();
        }
    }
    
    @Override
    public void initialize(final RollingFileManager manager) {
        if (manager.getFileTime() < OnStartupTriggeringPolicy.JVM_START_TIME && manager.getFileSize() >= this.minSize) {
            StatusLogger.getLogger().debug("Initiating rollover at startup");
            if (this.minSize == 0L) {
                manager.setRenameEmptyFiles(true);
            }
            manager.skipFooter(true);
            manager.rollover();
            manager.skipFooter(false);
        }
    }
    
    @Override
    public boolean isTriggeringEvent(final LogEvent event) {
        return false;
    }
    
    @Override
    public String toString() {
        return "OnStartupTriggeringPolicy";
    }
    
    @PluginFactory
    public static OnStartupTriggeringPolicy createPolicy(@PluginAttribute(value = "minSize", defaultLong = 1L) final long minSize) {
        return new OnStartupTriggeringPolicy(minSize);
    }
    
    static {
        JVM_START_TIME = initStartTime();
    }
}
