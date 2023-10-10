// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "sys", category = "Lookup")
public class SystemPropertiesLookup extends AbstractLookup
{
    private static final Logger LOGGER;
    private static final Marker LOOKUP;
    
    @Override
    public String lookup(final LogEvent event, final String key) {
        try {
            return System.getProperty(key);
        }
        catch (Exception ex) {
            SystemPropertiesLookup.LOGGER.warn(SystemPropertiesLookup.LOOKUP, "Error while getting system property [{}].", key, ex);
            return null;
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        LOOKUP = MarkerManager.getMarker("LOOKUP");
    }
}
