// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "CustomLevel", category = "Core", printObject = true)
public final class CustomLevelConfig
{
    static final CustomLevelConfig[] EMPTY_ARRAY;
    private final String levelName;
    private final int intLevel;
    
    private CustomLevelConfig(final String levelName, final int intLevel) {
        this.levelName = Objects.requireNonNull(levelName, "levelName is null");
        this.intLevel = intLevel;
    }
    
    @PluginFactory
    public static CustomLevelConfig createLevel(@PluginAttribute("name") final String levelName, @PluginAttribute("intLevel") final int intLevel) {
        StatusLogger.getLogger().debug("Creating CustomLevel(name='{}', intValue={})", levelName, intLevel);
        Level.forName(levelName, intLevel);
        return new CustomLevelConfig(levelName, intLevel);
    }
    
    public String getLevelName() {
        return this.levelName;
    }
    
    public int getIntLevel() {
        return this.intLevel;
    }
    
    @Override
    public int hashCode() {
        return this.intLevel ^ this.levelName.hashCode();
    }
    
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof CustomLevelConfig)) {
            return false;
        }
        final CustomLevelConfig other = (CustomLevelConfig)object;
        return this.intLevel == other.intLevel && this.levelName.equals(other.levelName);
    }
    
    @Override
    public String toString() {
        return "CustomLevel[name=" + this.levelName + ", intLevel=" + this.intLevel + "]";
    }
    
    static {
        EMPTY_ARRAY = new CustomLevelConfig[0];
    }
}
