// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "env", category = "Lookup")
public class EnvironmentLookup extends AbstractLookup
{
    @Override
    public String lookup(final LogEvent event, final String key) {
        return System.getenv(key);
    }
}
