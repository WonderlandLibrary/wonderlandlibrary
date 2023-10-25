/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.core.config.CustomLevelConfig;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name="CustomLevels", category="Core", printObject=true)
public final class CustomLevels {
    private final List<CustomLevelConfig> customLevels;

    private CustomLevels(CustomLevelConfig[] customLevels) {
        this.customLevels = new ArrayList<CustomLevelConfig>(Arrays.asList(customLevels));
    }

    @PluginFactory
    public static CustomLevels createCustomLevels(@PluginElement(value="CustomLevels") CustomLevelConfig[] customLevels) {
        return new CustomLevels(customLevels == null ? new CustomLevelConfig[0] : customLevels);
    }

    public List<CustomLevelConfig> getCustomLevels() {
        return this.customLevels;
    }
}

