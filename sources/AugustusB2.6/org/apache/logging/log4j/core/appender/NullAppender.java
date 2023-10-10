// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.LogEvent;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Null", category = "Core", elementType = "appender", printObject = true)
public class NullAppender extends AbstractAppender
{
    public static final String PLUGIN_NAME = "Null";
    
    @PluginFactory
    public static NullAppender createAppender(@PluginAttribute(value = "name", defaultString = "null") final String name) {
        return new NullAppender(name);
    }
    
    private NullAppender(final String name) {
        super(name, null, null, true, Property.EMPTY_ARRAY);
    }
    
    @Override
    public void append(final LogEvent event) {
    }
}
