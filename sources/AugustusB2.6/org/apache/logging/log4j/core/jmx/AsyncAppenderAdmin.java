// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import java.util.Objects;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.appender.AsyncAppender;

public class AsyncAppenderAdmin implements AsyncAppenderAdminMBean
{
    private final String contextName;
    private final AsyncAppender asyncAppender;
    private final ObjectName objectName;
    
    public AsyncAppenderAdmin(final String contextName, final AsyncAppender appender) {
        this.contextName = Objects.requireNonNull(contextName, "contextName");
        this.asyncAppender = Objects.requireNonNull(appender, "async appender");
        try {
            final String ctxName = Server.escape(this.contextName);
            final String configName = Server.escape(appender.getName());
            final String name = String.format("org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s", ctxName, configName);
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
        return this.asyncAppender.getName();
    }
    
    @Override
    public String getLayout() {
        return String.valueOf(this.asyncAppender.getLayout());
    }
    
    @Override
    public boolean isIgnoreExceptions() {
        return this.asyncAppender.ignoreExceptions();
    }
    
    @Override
    public String getErrorHandler() {
        return String.valueOf(this.asyncAppender.getHandler());
    }
    
    @Override
    public String getFilter() {
        return String.valueOf(this.asyncAppender.getFilter());
    }
    
    @Override
    public String[] getAppenderRefs() {
        return this.asyncAppender.getAppenderRefStrings();
    }
    
    @Override
    public boolean isIncludeLocation() {
        return this.asyncAppender.isIncludeLocation();
    }
    
    @Override
    public boolean isBlocking() {
        return this.asyncAppender.isBlocking();
    }
    
    @Override
    public String getErrorRef() {
        return this.asyncAppender.getErrorRef();
    }
    
    @Override
    public int getQueueCapacity() {
        return this.asyncAppender.getQueueCapacity();
    }
    
    @Override
    public int getQueueRemainingCapacity() {
        return this.asyncAppender.getQueueRemainingCapacity();
    }
}
