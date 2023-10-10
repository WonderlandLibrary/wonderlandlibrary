// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.LogEvent;
import java.io.Serializable;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.Layout;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "CountingNoOp", category = "Core", elementType = "appender", printObject = true)
public class CountingNoOpAppender extends AbstractAppender
{
    private final AtomicLong total;
    
    public CountingNoOpAppender(final String name, final Layout<?> layout) {
        super(name, null, (Layout<? extends Serializable>)layout, true, Property.EMPTY_ARRAY);
        this.total = new AtomicLong();
    }
    
    private CountingNoOpAppender(final String name, final Layout<?> layout, final Property[] properties) {
        super(name, null, (Layout<? extends Serializable>)layout, true, properties);
        this.total = new AtomicLong();
    }
    
    public long getCount() {
        return this.total.get();
    }
    
    @Override
    public void append(final LogEvent event) {
        this.total.incrementAndGet();
    }
    
    @PluginFactory
    public static CountingNoOpAppender createAppender(@PluginAttribute("name") final String name) {
        return new CountingNoOpAppender(Objects.requireNonNull(name), null, Property.EMPTY_ARRAY);
    }
}
