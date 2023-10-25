/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;

@Plugin(name="sys", category="Lookup")
public class SystemPropertiesLookup
implements StrLookup {
    @Override
    public String lookup(String key) {
        try {
            return System.getProperty(key);
        }
        catch (Exception ex) {
            return null;
        }
    }

    @Override
    public String lookup(LogEvent event, String key) {
        try {
            return System.getProperty(key);
        }
        catch (Exception ex) {
            return null;
        }
    }
}

