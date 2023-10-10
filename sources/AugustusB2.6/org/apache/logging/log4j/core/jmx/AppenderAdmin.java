// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import org.apache.logging.log4j.core.filter.AbstractFilterable;
import java.util.Objects;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.Appender;

public class AppenderAdmin implements AppenderAdminMBean
{
    private final String contextName;
    private final Appender appender;
    private final ObjectName objectName;
    
    public AppenderAdmin(final String contextName, final Appender appender) {
        this.contextName = Objects.requireNonNull(contextName, "contextName");
        this.appender = Objects.requireNonNull(appender, "appender");
        try {
            final String ctxName = Server.escape(this.contextName);
            final String configName = Server.escape(appender.getName());
            final String name = String.format("org.apache.logging.log4j2:type=%s,component=Appenders,name=%s", ctxName, configName);
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
        return this.appender.getName();
    }
    
    @Override
    public String getLayout() {
        return String.valueOf(this.appender.getLayout());
    }
    
    @Override
    public boolean isIgnoreExceptions() {
        return this.appender.ignoreExceptions();
    }
    
    @Override
    public String getErrorHandler() {
        return String.valueOf(this.appender.getHandler());
    }
    
    @Override
    public String getFilter() {
        if (this.appender instanceof AbstractFilterable) {
            return String.valueOf(((AbstractFilterable)this.appender).getFilter());
        }
        return null;
    }
}
