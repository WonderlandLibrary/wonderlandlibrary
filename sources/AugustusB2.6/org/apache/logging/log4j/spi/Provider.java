// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.Properties;
import java.lang.ref.WeakReference;
import java.net.URL;
import org.apache.logging.log4j.Logger;

public class Provider
{
    public static final String FACTORY_PRIORITY = "FactoryPriority";
    public static final String THREAD_CONTEXT_MAP = "ThreadContextMap";
    public static final String LOGGER_CONTEXT_FACTORY = "LoggerContextFactory";
    private static final Integer DEFAULT_PRIORITY;
    private static final Logger LOGGER;
    private final Integer priority;
    private final String className;
    private final Class<? extends LoggerContextFactory> loggerContextFactoryClass;
    private final String threadContextMap;
    private final Class<? extends ThreadContextMap> threadContextMapClass;
    private final String versions;
    private final URL url;
    private final WeakReference<ClassLoader> classLoader;
    
    public Provider(final Properties props, final URL url, final ClassLoader classLoader) {
        this.url = url;
        this.classLoader = new WeakReference<ClassLoader>(classLoader);
        final String weight = props.getProperty("FactoryPriority");
        this.priority = ((weight == null) ? Provider.DEFAULT_PRIORITY : Integer.valueOf(weight));
        this.className = props.getProperty("LoggerContextFactory");
        this.threadContextMap = props.getProperty("ThreadContextMap");
        this.loggerContextFactoryClass = null;
        this.threadContextMapClass = null;
        this.versions = null;
    }
    
    public Provider(final Integer priority, final String versions, final Class<? extends LoggerContextFactory> loggerContextFactoryClass) {
        this(priority, versions, loggerContextFactoryClass, null);
    }
    
    public Provider(final Integer priority, final String versions, final Class<? extends LoggerContextFactory> loggerContextFactoryClass, final Class<? extends ThreadContextMap> threadContextMapClass) {
        this.url = null;
        this.classLoader = null;
        this.priority = priority;
        this.loggerContextFactoryClass = loggerContextFactoryClass;
        this.threadContextMapClass = threadContextMapClass;
        this.className = null;
        this.threadContextMap = null;
        this.versions = versions;
    }
    
    public String getVersions() {
        return this.versions;
    }
    
    public Integer getPriority() {
        return this.priority;
    }
    
    public String getClassName() {
        if (this.loggerContextFactoryClass != null) {
            return this.loggerContextFactoryClass.getName();
        }
        return this.className;
    }
    
    public Class<? extends LoggerContextFactory> loadLoggerContextFactory() {
        if (this.loggerContextFactoryClass != null) {
            return this.loggerContextFactoryClass;
        }
        if (this.className == null) {
            return null;
        }
        final ClassLoader loader = this.classLoader.get();
        if (loader == null) {
            return null;
        }
        try {
            final Class<?> clazz = loader.loadClass(this.className);
            if (LoggerContextFactory.class.isAssignableFrom(clazz)) {
                return clazz.asSubclass(LoggerContextFactory.class);
            }
        }
        catch (Exception e) {
            Provider.LOGGER.error("Unable to create class {} specified in {}", this.className, this.url.toString(), e);
        }
        return null;
    }
    
    public String getThreadContextMap() {
        if (this.threadContextMapClass != null) {
            return this.threadContextMapClass.getName();
        }
        return this.threadContextMap;
    }
    
    public Class<? extends ThreadContextMap> loadThreadContextMap() {
        if (this.threadContextMapClass != null) {
            return this.threadContextMapClass;
        }
        if (this.threadContextMap == null) {
            return null;
        }
        final ClassLoader loader = this.classLoader.get();
        if (loader == null) {
            return null;
        }
        try {
            final Class<?> clazz = loader.loadClass(this.threadContextMap);
            if (ThreadContextMap.class.isAssignableFrom(clazz)) {
                return clazz.asSubclass(ThreadContextMap.class);
            }
        }
        catch (Exception e) {
            Provider.LOGGER.error("Unable to create class {} specified in {}", this.threadContextMap, this.url.toString(), e);
        }
        return null;
    }
    
    public URL getUrl() {
        return this.url;
    }
    
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder("Provider[");
        if (!Provider.DEFAULT_PRIORITY.equals(this.priority)) {
            result.append("priority=").append(this.priority).append(", ");
        }
        if (this.threadContextMap != null) {
            result.append("threadContextMap=").append(this.threadContextMap).append(", ");
        }
        else if (this.threadContextMapClass != null) {
            result.append("threadContextMapClass=").append(this.threadContextMapClass.getName());
        }
        if (this.className != null) {
            result.append("className=").append(this.className).append(", ");
        }
        else if (this.loggerContextFactoryClass != null) {
            result.append("class=").append(this.loggerContextFactoryClass.getName());
        }
        if (this.url != null) {
            result.append("url=").append(this.url);
        }
        final ClassLoader loader;
        if (this.classLoader == null || (loader = this.classLoader.get()) == null) {
            result.append(", classLoader=null(not reachable)");
        }
        else {
            result.append(", classLoader=").append(loader);
        }
        result.append("]");
        return result.toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Provider provider = (Provider)o;
        Label_0062: {
            if (this.priority != null) {
                if (this.priority.equals(provider.priority)) {
                    break Label_0062;
                }
            }
            else if (provider.priority == null) {
                break Label_0062;
            }
            return false;
        }
        Label_0095: {
            if (this.className != null) {
                if (this.className.equals(provider.className)) {
                    break Label_0095;
                }
            }
            else if (provider.className == null) {
                break Label_0095;
            }
            return false;
        }
        if (this.loggerContextFactoryClass != null) {
            if (this.loggerContextFactoryClass.equals(provider.loggerContextFactoryClass)) {
                return (this.versions != null) ? this.versions.equals(provider.versions) : (provider.versions == null);
            }
        }
        else if (provider.loggerContextFactoryClass == null) {
            return (this.versions != null) ? this.versions.equals(provider.versions) : (provider.versions == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.priority != null) ? this.priority.hashCode() : 0;
        result = 31 * result + ((this.className != null) ? this.className.hashCode() : 0);
        result = 31 * result + ((this.loggerContextFactoryClass != null) ? this.loggerContextFactoryClass.hashCode() : 0);
        result = 31 * result + ((this.versions != null) ? this.versions.hashCode() : 0);
        return result;
    }
    
    static {
        DEFAULT_PRIORITY = -1;
        LOGGER = StatusLogger.getLogger();
    }
}
