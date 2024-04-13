package net.smoothboot.client.config;

import com.google.gson.JsonObject;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.modmanager;
import net.smoothboot.client.module.settings.*;
import net.smoothboot.client.util.JsonUtil;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class ConfigWriter {

    public static void writeConfig(boolean compatibilityMode, @Nullable JsonObject presetData){

        File config = new File(JsonUtil.configDir+"\\optifine.json");

        JsonObject config_obj = new JsonObject();

        for(Mod m : modmanager.INSTANCE.getModule()){
            JsonObject module = new JsonObject();
            if(compatibilityMode){
                module = presetData;
            }
            else {
                for(Setting s : m.getSettings()){
                    if(s instanceof ModeSetting){
                        module.addProperty(s.getName(), ((ModeSetting) s).getMode());
                    }
                    else if (s instanceof BooleanSetting) {
                        module.addProperty(s.getName(), ((BooleanSetting) s).isEnabled());
                    }
                    else if (s instanceof KeyBindSetting) {
                        module.addProperty(s.getName(), ((KeyBindSetting) s).getKey());
                    }
                    else if (s instanceof NumberSetting) {
                        module.addProperty(s.getName(), ((NumberSetting) s).getValue());
                }

            }

            module.addProperty("enabled", m.isEnabled());

            config_obj.add(m.getName(), module);
            }

            try {
                JsonUtil.writeJson(config, config_obj);

            } catch (Exception e){

            }

        }
    }
}
