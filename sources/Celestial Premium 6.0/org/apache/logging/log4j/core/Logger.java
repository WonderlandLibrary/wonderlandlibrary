/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.CompositeFilter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.spi.AbstractLogger;

public class Logger
extends AbstractLogger {
    protected volatile PrivateConfig config;
    private final LoggerContext context;

    protected Logger(LoggerContext context, String name, MessageFactory messageFactory) {
        super(name, messageFactory);
        this.context = context;
        this.config = new PrivateConfig(context.getConfiguration(), this);
    }

    public Logger getParent() {
        LoggerConfig lc;
        LoggerConfig loggerConfig = lc = this.config.loggerConfig.getName().equals(this.getName()) ? this.config.loggerConfig.getParent() : this.config.loggerConfig;
        if (lc == null) {
            return null;
        }
        if (this.context.hasLogger(lc.getName())) {
            return this.context.getLogger(lc.getName(), this.getMessageFactory());
        }
        return new Logger(this.context, lc.getName(), this.getMessageFactory());
    }

    public LoggerContext getContext() {
        return this.context;
    }

    public synchronized void setLevel(Level level) {
        if (level != null) {
            this.config = new PrivateConfig(this.config, level);
        }
    }

    public Level getLevel() {
        return this.config.level;
    }

    @Override
    public void log(Marker marker, String fqcn, Level level, Message data, Throwable t) {
        if (data == null) {
            data = new SimpleMessage("");
        }
        this.config.config.getConfigurationMonitor().checkConfiguration();
        this.config.loggerConfig.log(this.getName(), marker, fqcn, level, data, t);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String msg) {
        return this.config.filter(level, marker, msg);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String msg, Throwable t) {
        return this.config.filter(level, marker, msg, t);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, String msg, Object ... p1) {
        return this.config.filter(level, marker, msg, p1);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Object msg, Throwable t) {
        return this.config.filter(level, marker, msg, t);
    }

    @Override
    public boolean isEnabled(Level level, Marker marker, Message msg, Throwable t) {
        return this.config.filter(level, marker, msg, t);
    }

    public void addAppender(Appender appender) {
        this.config.config.addLoggerAppender(this, appender);
    }

    public void removeAppender(Appender appender) {
        this.config.loggerConfig.removeAppender(appender.getName());
    }

    public Map<String, Appender> getAppenders() {
        return this.config.loggerConfig.getAppenders();
    }

    public Iterator<Filter> getFilters() {
        Filter filter = this.config.loggerConfig.getFilter();
        if (filter == null) {
            return new ArrayList().iterator();
        }
        if (filter instanceof CompositeFilter) {
            return ((CompositeFilter)filter).iterator();
        }
        ArrayList<Filter> filters = new ArrayList<Filter>();
        filters.add(filter);
        return filters.iterator();
    }

    public int filterCount() {
        Filter filter = this.config.loggerConfig.getFilter();
        if (filter == null) {
            return 0;
        }
        if (filter instanceof CompositeFilter) {
            return ((CompositeFilter)filter).size();
        }
        return 1;
    }

    public void addFilter(Filter filter) {
        this.config.config.addLoggerFilter(this, filter);
    }

    public boolean isAdditive() {
        return this.config.loggerConfig.isAdditive();
    }

    public void setAdditive(boolean additive) {
        this.config.config.setLoggerAdditive(this, additive);
    }

    void updateConfiguration(Configuration config) {
        this.config = new PrivateConfig(config, this);
    }

    @Override
    public String toString() {
        String nameLevel = "" + this.getName() + ":" + (Object)((Object)this.getLevel());
        if (this.context == null) {
            return nameLevel;
        }
        String contextName = this.context.getName();
        return contextName == null ? nameLevel : nameLevel + " in " + contextName;
    }

    protected class PrivateConfig {
        public final LoggerConfig loggerConfig;
        public final Configuration config;
        private final Level level;
        private final int intLevel;
        private final Logger logger;

        public PrivateConfig(Configuration config, Logger logger2) {
            this.config = config;
            this.loggerConfig = config.getLoggerConfig(Logger.this.getName());
            this.level = this.loggerConfig.getLevel();
            this.intLevel = this.level.intLevel();
            this.logger = logger2;
        }

        public PrivateConfig(PrivateConfig pc, Level level) {
            this.config = pc.config;
            this.loggerConfig = pc.loggerConfig;
            this.level = level;
            this.intLevel = this.level.intLevel();
            this.logger = pc.logger;
        }

        public PrivateConfig(PrivateConfig pc, LoggerConfig lc) {
            this.config = pc.config;
            this.loggerConfig = lc;
            this.level = lc.getLevel();
            this.intLevel = this.level.intLevel();
            this.logger = pc.logger;
        }

        public void logEvent(LogEvent event) {
            this.config.getConfigurationMonitor().checkConfiguration();
            this.loggerConfig.log(event);
        }

        boolean filter(Level level, Marker marker, String msg) {
            Filter.Result r;
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter filter = this.config.getFilter();
            if (filter != null && (r = filter.filter(this.logger, level, marker, msg, new Object[0])) != Filter.Result.NEUTRAL) {
                return r == Filter.Result.ACCEPT;
            }
            return this.intLevel >= level.intLevel();
        }

        boolean filter(Level level, Marker marker, String msg, Throwable t) {
            Filter.Result r;
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter filter = this.config.getFilter();
            if (filter != null && (r = filter.filter(this.logger, level, marker, (Object)msg, t)) != Filter.Result.NEUTRAL) {
                return r == Filter.Result.ACCEPT;
            }
            return this.intLevel >= level.intLevel();
        }

        boolean filter(Level level, Marker marker, String msg, Object ... p1) {
            Filter.Result r;
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter filter = this.config.getFilter();
            if (filter != null && (r = filter.filter(this.logger, level, marker, msg, p1)) != Filter.Result.NEUTRAL) {
                return r == Filter.Result.ACCEPT;
            }
            return this.intLevel >= level.intLevel();
        }

        boolean filter(Level level, Marker marker, Object msg, Throwable t) {
            Filter.Result r;
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter filter = this.config.getFilter();
            if (filter != null && (r = filter.filter(this.logger, level, marker, msg, t)) != Filter.Result.NEUTRAL) {
                return r == Filter.Result.ACCEPT;
            }
            return this.intLevel >= level.intLevel();
        }

        boolean filter(Level level, Marker marker, Message msg, Throwable t) {
            Filter.Result r;
            this.config.getConfigurationMonitor().checkConfiguration();
            Filter filter = this.config.getFilter();
            if (filter != null && (r = filter.filter(this.logger, level, marker, msg, t)) != Filter.Result.NEUTRAL) {
                return r == Filter.Result.ACCEPT;
            }
            return this.intLevel >= level.intLevel();
        }
    }
}

