/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
import org.apache.logging.log4j.core.config.AppenderControl;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.impl.DefaultLogEventFactory;
import org.apache.logging.log4j.core.impl.LogEventFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

@Plugin(name="logger", category="Core", printObject=true)
public class LoggerConfig
extends AbstractFilterable {
    protected static final Logger LOGGER = StatusLogger.getLogger();
    private static final int MAX_RETRIES = 3;
    private static final long WAIT_TIME = 1000L;
    private static LogEventFactory LOG_EVENT_FACTORY = null;
    private List<AppenderRef> appenderRefs = new ArrayList<AppenderRef>();
    private final Map<String, AppenderControl> appenders = new ConcurrentHashMap<String, AppenderControl>();
    private final String name;
    private LogEventFactory logEventFactory;
    private Level level;
    private boolean additive = true;
    private boolean includeLocation = true;
    private LoggerConfig parent;
    private final AtomicInteger counter = new AtomicInteger();
    private boolean shutdown = false;
    private final Map<Property, Boolean> properties;
    private final Configuration config;

    public LoggerConfig() {
        this.logEventFactory = LOG_EVENT_FACTORY;
        this.level = Level.ERROR;
        this.name = "";
        this.properties = null;
        this.config = null;
    }

    public LoggerConfig(String name, Level level, boolean additive) {
        this.logEventFactory = LOG_EVENT_FACTORY;
        this.name = name;
        this.level = level;
        this.additive = additive;
        this.properties = null;
        this.config = null;
    }

    protected LoggerConfig(String name, List<AppenderRef> appenders, Filter filter, Level level, boolean additive, Property[] properties, Configuration config, boolean includeLocation) {
        super(filter);
        this.logEventFactory = LOG_EVENT_FACTORY;
        this.name = name;
        this.appenderRefs = appenders;
        this.level = level;
        this.additive = additive;
        this.includeLocation = includeLocation;
        this.config = config;
        if (properties != null && properties.length > 0) {
            this.properties = new HashMap<Property, Boolean>(properties.length);
            for (Property prop : properties) {
                boolean interpolate = prop.getValue().contains("${");
                this.properties.put(prop, interpolate);
            }
        } else {
            this.properties = null;
        }
    }

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    public String getName() {
        return this.name;
    }

    public void setParent(LoggerConfig parent) {
        this.parent = parent;
    }

    public LoggerConfig getParent() {
        return this.parent;
    }

    public void addAppender(Appender appender, Level level, Filter filter) {
        this.appenders.put(appender.getName(), new AppenderControl(appender, level, filter));
    }

    public void removeAppender(String name) {
        AppenderControl ctl = this.appenders.remove(name);
        if (ctl != null) {
            this.cleanupFilter(ctl);
        }
    }

    public Map<String, Appender> getAppenders() {
        HashMap<String, Appender> map = new HashMap<String, Appender>();
        for (Map.Entry<String, AppenderControl> entry : this.appenders.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getAppender());
        }
        return map;
    }

    protected void clearAppenders() {
        this.waitForCompletion();
        Collection<AppenderControl> controls = this.appenders.values();
        Iterator<AppenderControl> iterator = controls.iterator();
        while (iterator.hasNext()) {
            AppenderControl ctl = iterator.next();
            iterator.remove();
            this.cleanupFilter(ctl);
        }
    }

    private void cleanupFilter(AppenderControl ctl) {
        Filter filter = ctl.getFilter();
        if (filter != null) {
            ctl.removeFilter(filter);
            if (filter instanceof LifeCycle) {
                ((LifeCycle)((Object)filter)).stop();
            }
        }
    }

    public List<AppenderRef> getAppenderRefs() {
        return this.appenderRefs;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return this.level;
    }

    public LogEventFactory getLogEventFactory() {
        return this.logEventFactory;
    }

    public void setLogEventFactory(LogEventFactory logEventFactory) {
        this.logEventFactory = logEventFactory;
    }

    public boolean isAdditive() {
        return this.additive;
    }

    public void setAdditive(boolean additive) {
        this.additive = additive;
    }

    public boolean isIncludeLocation() {
        return this.includeLocation;
    }

    public Map<Property, Boolean> getProperties() {
        return this.properties == null ? null : Collections.unmodifiableMap(this.properties);
    }

    public void log(String loggerName, Marker marker, String fqcn, Level level, Message data, Throwable t) {
        ArrayList<Property> props = null;
        if (this.properties != null) {
            props = new ArrayList<Property>(this.properties.size());
            for (Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
                Property prop = entry.getKey();
                String value = entry.getValue() != false ? this.config.getStrSubstitutor().replace(prop.getValue()) : prop.getValue();
                props.add(Property.createProperty(prop.getName(), value));
            }
        }
        LogEvent event = this.logEventFactory.createEvent(loggerName, marker, fqcn, level, data, props, t);
        this.log(event);
    }

    private synchronized void waitForCompletion() {
        if (this.shutdown) {
            return;
        }
        this.shutdown = true;
        int retries = 0;
        while (this.counter.get() > 0) {
            try {
                this.wait(1000L * (long)(retries + 1));
            }
            catch (InterruptedException ie) {
                if (++retries <= 3) continue;
                break;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void log(LogEvent event) {
        this.counter.incrementAndGet();
        try {
            if (this.isFiltered(event)) {
                return;
            }
            event.setIncludeLocation(this.isIncludeLocation());
            this.callAppenders(event);
            if (this.additive && this.parent != null) {
                this.parent.log(event);
            }
        }
        finally {
            if (this.counter.decrementAndGet() == 0) {
                LoggerConfig loggerConfig = this;
                synchronized (loggerConfig) {
                    if (this.shutdown) {
                        this.notifyAll();
                    }
                }
            }
        }
    }

    protected void callAppenders(LogEvent event) {
        for (AppenderControl control : this.appenders.values()) {
            control.callAppender(event);
        }
    }

    public String toString() {
        return Strings.isEmpty(this.name) ? "root" : this.name;
    }

    @PluginFactory
    public static LoggerConfig createLogger(@PluginAttribute(value="additivity") String additivity, @PluginAttribute(value="level") String levelName, @PluginAttribute(value="name") String loggerName, @PluginAttribute(value="includeLocation") String includeLocation, @PluginElement(value="AppenderRef") AppenderRef[] refs, @PluginElement(value="Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement(value="Filters") Filter filter) {
        Level level;
        if (loggerName == null) {
            LOGGER.error("Loggers cannot be configured without a name");
            return null;
        }
        List<AppenderRef> appenderRefs = Arrays.asList(refs);
        try {
            level = Level.toLevel(levelName, Level.ERROR);
        }
        catch (Exception ex) {
            LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", levelName);
            level = Level.ERROR;
        }
        String name = loggerName.equals("root") ? "" : loggerName;
        boolean additive = Booleans.parseBoolean(additivity, true);
        return new LoggerConfig(name, appenderRefs, filter, level, additive, properties, config, LoggerConfig.includeLocation(includeLocation));
    }

    protected static boolean includeLocation(String includeLocationConfigValue) {
        if (includeLocationConfigValue == null) {
            boolean sync = !AsyncLoggerContextSelector.class.getName().equals(System.getProperty("Log4jContextSelector"));
            return sync;
        }
        return Boolean.parseBoolean(includeLocationConfigValue);
    }

    static {
        String factory = PropertiesUtil.getProperties().getStringProperty("Log4jLogEventFactory");
        if (factory != null) {
            try {
                Class<?> clazz = Loader.loadClass(factory);
                if (clazz != null && LogEventFactory.class.isAssignableFrom(clazz)) {
                    LOG_EVENT_FACTORY = (LogEventFactory)clazz.newInstance();
                }
            }
            catch (Exception ex) {
                LOGGER.error("Unable to create LogEventFactory " + factory, (Throwable)ex);
            }
        }
        if (LOG_EVENT_FACTORY == null) {
            LOG_EVENT_FACTORY = new DefaultLogEventFactory();
        }
    }

    @Plugin(name="root", category="Core", printObject=true)
    public static class RootLogger
    extends LoggerConfig {
        @PluginFactory
        public static LoggerConfig createLogger(@PluginAttribute(value="additivity") String additivity, @PluginAttribute(value="level") String levelName, @PluginAttribute(value="includeLocation") String includeLocation, @PluginElement(value="AppenderRef") AppenderRef[] refs, @PluginElement(value="Properties") Property[] properties, @PluginConfiguration Configuration config, @PluginElement(value="Filters") Filter filter) {
            Level level;
            List<AppenderRef> appenderRefs = Arrays.asList(refs);
            try {
                level = Level.toLevel(levelName, Level.ERROR);
            }
            catch (Exception ex) {
                LOGGER.error("Invalid Log level specified: {}. Defaulting to Error", levelName);
                level = Level.ERROR;
            }
            boolean additive = Booleans.parseBoolean(additivity, true);
            return new LoggerConfig("", appenderRefs, filter, level, additive, properties, config, RootLogger.includeLocation(includeLocation));
        }
    }
}

