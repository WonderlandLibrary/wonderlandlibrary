// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "bundle", category = "Lookup")
public class ResourceBundleLookup extends AbstractLookup
{
    private static final Logger LOGGER;
    private static final Marker LOOKUP;
    
    @Override
    public String lookup(final LogEvent event, final String key) {
        if (key == null) {
            return null;
        }
        final String[] keys = key.split(":");
        final int keyLen = keys.length;
        if (keyLen != 2) {
            ResourceBundleLookup.LOGGER.warn(ResourceBundleLookup.LOOKUP, "Bad ResourceBundle key format [{}]. Expected format is BundleName:KeyName.", key);
            return null;
        }
        final String bundleName = keys[0];
        final String bundleKey = keys[1];
        try {
            return ResourceBundle.getBundle(bundleName).getString(bundleKey);
        }
        catch (MissingResourceException e) {
            ResourceBundleLookup.LOGGER.warn(ResourceBundleLookup.LOOKUP, "Error looking up ResourceBundle [{}].", bundleName, e);
            return null;
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        LOOKUP = MarkerManager.getMarker("LOOKUP");
    }
}
