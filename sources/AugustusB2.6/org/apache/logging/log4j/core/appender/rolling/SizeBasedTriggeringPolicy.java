// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "SizeBasedTriggeringPolicy", category = "Core", printObject = true)
public class SizeBasedTriggeringPolicy extends AbstractTriggeringPolicy
{
    private static final long MAX_FILE_SIZE = 10485760L;
    private final long maxFileSize;
    private RollingFileManager manager;
    
    protected SizeBasedTriggeringPolicy() {
        this.maxFileSize = 10485760L;
    }
    
    protected SizeBasedTriggeringPolicy(final long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
    
    public long getMaxFileSize() {
        return this.maxFileSize;
    }
    
    @Override
    public void initialize(final RollingFileManager aManager) {
        this.manager = aManager;
    }
    
    @Override
    public boolean isTriggeringEvent(final LogEvent event) {
        final boolean triggered = this.manager.getFileSize() > this.maxFileSize;
        if (triggered) {
            this.manager.getPatternProcessor().updateTime();
        }
        return triggered;
    }
    
    @Override
    public String toString() {
        return "SizeBasedTriggeringPolicy(size=" + this.maxFileSize + ')';
    }
    
    @PluginFactory
    public static SizeBasedTriggeringPolicy createPolicy(@PluginAttribute("size") final String size) {
        final long maxSize = (size == null) ? 10485760L : FileSize.parse(size, 10485760L);
        return new SizeBasedTriggeringPolicy(maxSize);
    }
}
