// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

import java.util.Hashtable;
import java.util.Collection;
import java.util.Properties;
import java.util.Objects;

public class SystemPropertiesPropertySource implements PropertySource
{
    private static final int DEFAULT_PRIORITY = 0;
    private static final String PREFIX = "log4j2.";
    
    @Override
    public int getPriority() {
        return 0;
    }
    
    @Override
    public void forEach(final BiConsumer<String, String> action) {
        Properties properties;
        try {
            properties = System.getProperties();
        }
        catch (final SecurityException e) {
            return;
        }
        final Object[] keySet;
        synchronized (properties) {
            keySet = ((Hashtable<Object, V>)properties).keySet().toArray();
        }
        for (final Object key : keySet) {
            final String keyStr = Objects.toString(key, null);
            action.accept(keyStr, properties.getProperty(keyStr));
        }
    }
    
    @Override
    public CharSequence getNormalForm(final Iterable<? extends CharSequence> tokens) {
        return "log4j2." + (Object)Util.joinAsCamelCase(tokens);
    }
    
    @Override
    public Collection<String> getPropertyNames() {
        try {
            return System.getProperties().stringPropertyNames();
        }
        catch (final SecurityException e) {
            return super.getPropertyNames();
        }
    }
    
    @Override
    public String getProperty(final String key) {
        try {
            return System.getProperty(key);
        }
        catch (final SecurityException e) {
            return super.getProperty(key);
        }
    }
    
    @Override
    public boolean containsProperty(final String key) {
        return this.getProperty(key) != null;
    }
}
