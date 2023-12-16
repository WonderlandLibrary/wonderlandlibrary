// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.config;

import moonsense.MoonsenseClient;
import com.google.common.collect.Lists;
import moonsense.config.utils.Config;
import java.util.List;

public class ConfigManager
{
    public static final ConfigManager INSTANCE;
    public final List<Config> configs;
    
    static {
        INSTANCE = new ConfigManager();
    }
    
    public ConfigManager() {
        (this.configs = (List<Config>)Lists.newArrayList()).add(ModuleConfig.INSTANCE);
        this.configs.add(GeneralConfig.INSTANCE);
        this.configs.add(ThemeConfig.INSTANCE);
    }
    
    public void saveAll() {
        final long start;
        this.configs.forEach(config -> {
            start = System.currentTimeMillis();
            MoonsenseClient.info("Saving " + config.name + "...", new Object[0]);
            config.saveConfig();
            MoonsenseClient.info("Saved in " + (System.currentTimeMillis() - start) + "ms!", new Object[0]);
        });
    }
    
    public void loadAll() {
        final long start;
        this.configs.forEach(config -> {
            start = System.currentTimeMillis();
            MoonsenseClient.info("Loading " + config.name + "...", new Object[0]);
            config.loadConfig();
            MoonsenseClient.info("Loaded in " + (System.currentTimeMillis() - start) + "ms!", new Object[0]);
        });
    }
}
