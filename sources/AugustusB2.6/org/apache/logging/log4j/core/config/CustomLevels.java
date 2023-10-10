// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "CustomLevels", category = "Core", printObject = true)
public final class CustomLevels
{
    private final List<CustomLevelConfig> customLevels;
    
    private CustomLevels(final CustomLevelConfig[] customLevels) {
        this.customLevels = new ArrayList<CustomLevelConfig>(Arrays.asList(customLevels));
    }
    
    @PluginFactory
    public static CustomLevels createCustomLevels(@PluginElement("CustomLevels") final CustomLevelConfig[] customLevels) {
        return new CustomLevels((customLevels == null) ? CustomLevelConfig.EMPTY_ARRAY : customLevels);
    }
    
    public List<CustomLevelConfig> getCustomLevels() {
        return this.customLevels;
    }
}
