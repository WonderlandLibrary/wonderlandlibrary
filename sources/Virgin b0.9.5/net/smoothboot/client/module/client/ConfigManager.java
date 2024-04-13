package net.smoothboot.client.module.client;

import net.smoothboot.client.config.ConfigLoader;
import net.smoothboot.client.config.ConfigWriter;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.ModeSetting;

public class ConfigManager extends Mod {

    String load = "Load";
    String save = "Save";

    public ModeSetting managerMode = new ModeSetting("Mode", load, load, save);

    public ConfigManager() {
        super("Config Manager", "", Category.Client);
        addsettings(managerMode);
    }

    @Override
    public void onEnable() {
        if (managerMode.isMode(load)) {
            Thread configThr = new Thread(ConfigLoader::loadConfig, "loadConfig");
            configThr.start();
        }if (managerMode.isMode(save)) {
            Thread configThr = new Thread(() -> ConfigWriter.writeConfig(false, null), "saveConfig");
            configThr.start();
        }
        toggle();
    }


}
