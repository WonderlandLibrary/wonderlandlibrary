/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;

@Plugin(name="lower", category="Lookup")
public class LowerLookup
implements StrLookup {
    @Override
    public String lookup(String key) {
        return key != null ? key.toLowerCase() : null;
    }

    @Override
    public String lookup(LogEvent event, String key) {
        return this.lookup(key);
    }
}

