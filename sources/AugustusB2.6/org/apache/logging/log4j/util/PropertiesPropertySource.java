// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class PropertiesPropertySource implements PropertySource
{
    private static final String PREFIX = "log4j2.";
    private final Properties properties;
    
    public PropertiesPropertySource(final Properties properties) {
        this.properties = properties;
    }
    
    @Override
    public int getPriority() {
        return 0;
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
}
