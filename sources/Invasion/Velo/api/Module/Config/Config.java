package Velo.api.Module.Config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
import Velo.api.setting.Setting;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.KeybindSetting;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private final String name;
    private final File file;
    private final File file1;
    
    public Config(String name) {
        this.name = name;
        this.file = new File(Minecraft.getMinecraft().mcDataDir + "/Velo/configs/" + name + ".json");
        this.file1 = new File(Minecraft.getMinecraft().mcDataDir + "/Velo/theme/" + name + ".json");
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public String serialize() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        for (Module module : ModuleManager.modules) {
            List<Cfg> settings = new ArrayList<>();
            for (Setting setting : module.settings) {
                if (setting instanceof KeybindSetting)
                    continue;

                Cfg cfgSetting = new Cfg(null, null);
                cfgSetting.name = setting.name;
                if (setting instanceof BooleanSetting) {
                    cfgSetting.setting = ((BooleanSetting) setting).isEnabled();
                }
                if (setting instanceof ModeSetting) {
                    cfgSetting.setting = ((ModeSetting) setting).getMode();
                }
                if (setting instanceof NumberSetting) {
                    cfgSetting.setting = ((NumberSetting) setting).getValue();
                }

                settings.add(cfgSetting);
            }
            module.cfgSettings = settings.toArray(new Cfg[0]);
        }
        return gson.toJson(ModuleManager.modules);
     
    }

}
