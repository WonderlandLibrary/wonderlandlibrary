// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.bridge;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.Property;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.util.Strings;
import org.apache.log4j.Appender;

public class AppenderAdapter
{
    private final Appender appender;
    private final Adapter adapter;
    
    public static org.apache.logging.log4j.core.Appender adapt(final Appender appender) {
        if (appender instanceof org.apache.logging.log4j.core.Appender) {
            return (org.apache.logging.log4j.core.Appender)appender;
        }
        if (appender instanceof AppenderWrapper) {
            return ((AppenderWrapper)appender).getAppender();
        }
        if (appender != null) {
            return new AppenderAdapter(appender).getAdapter();
        }
        return null;
    }
    
    private AppenderAdapter(final Appender appender) {
        this.appender = appender;
        final Filter appenderFilter = FilterAdapter.adapt(appender.getFilter());
        String name = appender.getName();
        if (Strings.isEmpty(name)) {
            name = String.format("0x%08x", appender.hashCode());
        }
        this.adapter = new Adapter(name, appenderFilter, null, true, null);
    }
    
    public Adapter getAdapter() {
        return this.adapter;
    }
    
    public class Adapter extends AbstractAppender
    {
        protected Adapter(final String name, final Filter filter, final Layout<? extends Serializable> layout, final boolean ignoreExceptions, final Property[] properties) {
            super(name, filter, layout, ignoreExceptions, properties);
        }
        
        @Override
        public void append(final LogEvent event) {
            AppenderAdapter.this.appender.doAppend(new LogEventAdapter(event));
        }
        
        @Override
        public void stop() {
            AppenderAdapter.this.appender.close();
        }
        
        public org.apache.log4j.Appender getAppender() {
            return AppenderAdapter.this.appender;
        }
    }
}
