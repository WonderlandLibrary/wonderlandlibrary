// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.message.StructuredDataMessage;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "sd", category = "Lookup")
public class StructuredDataLookup implements StrLookup
{
    public static final String ID_KEY = "id";
    public static final String TYPE_KEY = "type";
    
    @Override
    public String lookup(final String key) {
        return null;
    }
    
    @Override
    public String lookup(final LogEvent event, final String key) {
        if (event == null || !(event.getMessage() instanceof StructuredDataMessage)) {
            return null;
        }
        final StructuredDataMessage msg = (StructuredDataMessage)event.getMessage();
        if ("id".equalsIgnoreCase(key)) {
            return msg.getId().getName();
        }
        if ("type".equalsIgnoreCase(key)) {
            return msg.getType();
        }
        return msg.get(key);
    }
}
