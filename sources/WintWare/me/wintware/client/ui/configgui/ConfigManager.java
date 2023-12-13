/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.ui.configgui;

import java.util.ArrayList;
import me.wintware.client.ui.configgui.Config;
import me.wintware.client.ui.configgui.configs.Vanilla;
import me.wintware.client.ui.configgui.configs.WellMore;

public class ConfigManager {
    final ArrayList<Config> configs = new ArrayList<Config>();

    public ConfigManager() {
        this.configs.add(new WellMore());
        this.configs.add(new Vanilla());
        this.configs.add(new WellMore());
    }

    private void addConfig(Config config) {
        this.configs.add(config);
    }

    public ArrayList<Config> getConfigs() {
        return this.configs;
    }
}

