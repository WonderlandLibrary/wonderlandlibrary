/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;

@Plugin(name="ctx", category="Lookup")
public class ContextMapLookup
implements StrLookup {
    @Override
    public String lookup(String key) {
        return ThreadContext.get(key);
    }

    @Override
    public String lookup(LogEvent event, String key) {
        return event.getContextMap().get(key);
    }
}

