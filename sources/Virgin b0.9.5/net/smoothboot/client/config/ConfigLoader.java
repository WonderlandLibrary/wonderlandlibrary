package net.smoothboot.client.config;

import com.google.gson.JsonObject;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.modmanager;
import net.smoothboot.client.module.settings.*;
import net.smoothboot.client.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigLoader {
    public static void loadConfig(){
        List<Mod> modules = modmanager.INSTANCE.getModule();
        File config = new File(JsonUtil.configDir+"\\optifine.json");
        if(!config.exists()){
            try{
                config.createNewFile();
            } catch (IOException e) {
            }
            ConfigWriter.writeConfig(false, null);
            return;
        }

        boolean compatibilityMode = false;

        JsonObject root = new JsonObject();

        for(Mod m : modules){
            JsonObject moduleData = JsonUtil.getKeyValue(config, m.getName()).getAsJsonObject();

            if(!compatibilityMode){
                try {
                    m.setEnabled(moduleData.get("enabled").getAsJsonPrimitive().getAsBoolean());
                } catch (Exception e){
                    compatibilityMode = true;
                }
            } else {

                m.setEnabled(m.isEnabled());
            }

            for(Setting s : m.getSettings()){
                if(s instanceof ModeSetting){
                    ((ModeSetting) s).setMode(
                            moduleData.get
                                            (
                                                    s.getName()
                                            )
                                    .getAsJsonPrimitive()
                                    .getAsString()
                    );
                } else if (s instanceof BooleanSetting) {
                    ((BooleanSetting) s).setEnabled(
                            moduleData.get(
                                            s.getName()
                                    )
                                    .getAsJsonPrimitive()
                                    .getAsBoolean()
                    );
                } else if (s instanceof KeyBindSetting) {
                    ((KeyBindSetting) s).setKey(
                            moduleData.get(
                                            s.getName()
                                    )
                                    .getAsJsonPrimitive()
                                    .getAsInt()
                    );
                } else if (s instanceof NumberSetting) {
                    ((NumberSetting) s).setValue(
                            moduleData.get(
                                            s.getName()
                                    )
                                    .getAsJsonPrimitive()
                                    .getAsDouble()
                    );
                }
                root.add(m.getName(), moduleData);
            }
        }
    }
}