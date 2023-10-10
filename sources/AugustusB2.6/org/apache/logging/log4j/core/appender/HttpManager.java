// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Layout;
import java.util.Objects;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

public abstract class HttpManager extends AbstractManager
{
    private final Configuration configuration;
    
    protected HttpManager(final Configuration configuration, final LoggerContext loggerContext, final String name) {
        super(loggerContext, name);
        this.configuration = Objects.requireNonNull(configuration);
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public void startup() {
    }
    
    public abstract void send(final Layout<?> layout, final LogEvent event) throws Exception;
}
