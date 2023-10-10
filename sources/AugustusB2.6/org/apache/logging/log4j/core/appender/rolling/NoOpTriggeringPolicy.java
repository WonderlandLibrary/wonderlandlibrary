// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "NoOpTriggeringPolicy", category = "Core", printObject = true)
public class NoOpTriggeringPolicy extends AbstractTriggeringPolicy
{
    public static final NoOpTriggeringPolicy INSTANCE;
    
    @PluginFactory
    public static NoOpTriggeringPolicy createPolicy() {
        return NoOpTriggeringPolicy.INSTANCE;
    }
    
    @Override
    public void initialize(final RollingFileManager manager) {
    }
    
    @Override
    public boolean isTriggeringEvent(final LogEvent logEvent) {
        return false;
    }
    
    static {
        INSTANCE = new NoOpTriggeringPolicy();
    }
}
