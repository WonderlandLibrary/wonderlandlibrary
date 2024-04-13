package com.canon.majik.impl.hook;

import com.canon.majik.api.config.Config;

public class ConfigHook implements Hooker{
    @Override
    public void init() {
        Config.loadConfig();
    }

    @Override
    public void unInit() {
        Config.saveConfig();
    }
}
