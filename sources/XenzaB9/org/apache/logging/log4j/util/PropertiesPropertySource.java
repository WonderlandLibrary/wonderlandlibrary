// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class PropertiesPropertySource implements PropertySource
{
    private static final int DEFAULT_PRIORITY = 200;
    private static final String PREFIX = "log4j2.";
    private final Properties properties;
    private final int priority;
    
    public PropertiesPropertySource(final Properties properties) {
        this(properties, 200);
    }
    
    public PropertiesPropertySource(final Properties properties, final int priority) {
        this.properties = properties;
        this.priority = priority;
    }
    
    @Override
    public int getPriority() {
        return this.priority;
    }
    
    @Override
    public void forEach(final BiConsumer<String, String> action) {
        for (final Map.Entry<Object, Object> entry : this.properties.entrySet()) {
            action.accept(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public CharSequence getNormalForm(final Iterable<? extends CharSequence> tokens) {
        return "log4j2." + (Object)Util.joinAsCamelCase(tokens);
    }
    
    @Override
    public Collection<String> getPropertyNames() {
        return this.properties.stringPropertyNames();
    }
    
    @Override
    public String getProperty(final String key) {
        return this.properties.getProperty(key);
    }
    
    @Override
    public boolean containsProperty(final String key) {
        return this.getProperty(key) != null;
    }
}
