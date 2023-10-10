// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import java.util.List;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.Level;
import java.util.Objects;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.LoggerContext;

public class LoggerConfigAdmin implements LoggerConfigAdminMBean
{
    private final LoggerContext loggerContext;
    private final LoggerConfig loggerConfig;
    private final ObjectName objectName;
    
    public LoggerConfigAdmin(final LoggerContext loggerContext, final LoggerConfig loggerConfig) {
        this.loggerContext = Objects.requireNonNull(loggerContext, "loggerContext");
        this.loggerConfig = Objects.requireNonNull(loggerConfig, "loggerConfig");
        try {
            final String ctxName = Server.escape(loggerContext.getName());
            final String configName = Server.escape(loggerConfig.getName());
            final String name = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s", ctxName, configName);
            this.objectName = new ObjectName(name);
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public ObjectName getObjectName() {
        return this.objectName;
    }
    
    @Override
    public String getName() {
        return this.loggerConfig.getName();
    }
    
    @Override
    public String getLevel() {
        return this.loggerConfig.getLevel().name();
    }
    
    @Override
    public void setLevel(final String level) {
        this.loggerConfig.setLevel(Level.getLevel(level));
        this.loggerContext.updateLoggers();
    }
    
    @Override
    public boolean isAdditive() {
        return this.loggerConfig.isAdditive();
    }
    
    @Override
    public void setAdditive(final boolean additive) {
        this.loggerConfig.setAdditive(additive);
        this.loggerContext.updateLoggers();
    }
    
    @Override
    public boolean isIncludeLocation() {
        return this.loggerConfig.isIncludeLocation();
    }
    
    @Override
    public String getFilter() {
        return String.valueOf(this.loggerConfig.getFilter());
    }
    
    @Override
    public String[] getAppenderRefs() {
        final List<AppenderRef> refs = this.loggerConfig.getAppenderRefs();
        final String[] result = new String[refs.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = refs.get(i).getRef();
        }
        return result;
    }
}
