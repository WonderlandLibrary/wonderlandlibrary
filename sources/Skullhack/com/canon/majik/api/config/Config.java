package com.canon.majik.api.config;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.Setting;
import com.canon.majik.impl.setting.settings.BooleanSetting;
import com.canon.majik.impl.setting.settings.ColorSetting;
import com.canon.majik.impl.setting.settings.ModeSetting;
import com.canon.majik.impl.setting.settings.NumberSetting;
import net.minecraft.client.Minecraft;
import org.json.JSONObject;

import java.io.*;

public class Config {
    static JSONObject config = new JSONObject();

    static File configFile = new File(getDir() + new File("modules.json"));


    public static void saveConfig() {


        for (Module m : Initializer.moduleManager.getModules()) {
            JSONObject settings = new JSONObject();

            for (Setting<?> setting : m.getSettings()) {
                if (setting instanceof BooleanSetting) {
                    settings.put(setting.getName(), ((BooleanSetting) setting).getValue());
                } else if (setting instanceof NumberSetting) {
                    settings.put(setting.getName(), ((NumberSetting) setting).getValue());
                } else if (setting instanceof ModeSetting) {
                    settings.put(setting.getName(), ((ModeSetting) setting).getValue());
                } else if (setting instanceof ColorSetting) {
                    settings.put(setting.getName(), ((ColorSetting) setting).getValue().getRGB());
                }

            }
            settings.put("key", m.getKey());
            settings.put("toggled", m.isEnabled());

            config.put(m.getName(), settings);
        }

        System.out.println(config.toString());

        try {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();


            FileWriter file = new FileWriter(configFile);
            file.write(config.toString());
            file.close();
            System.out.println("Saved config successfully");
        } catch (IOException e) {
            System.out.println("Failed to save config");
            e.printStackTrace();
        }
    }

    public static void loadConfig() {


        try {
            configFile.createNewFile();
        } catch (IOException ignored) {

        }

        JSONObject config = readJSONFile(configFile.getAbsolutePath());

        if (config == null) {
            return;
        }

        for (Module m : Initializer.moduleManager.getModules()) {

            JSONObject settings = null;

            try {
                settings = config.getJSONObject(m.getName());
            } catch (Exception e) {
                System.out.println(String.format("Module %s has not been initialized yet", m.getName()));
            }

            if (settings == null){
                continue;
            }

            for (Setting setting : m.getSettings()) {
                String settingName = setting.getName();

                if (setting instanceof BooleanSetting) {
                    setting.setValue(settings.getBoolean(settingName));
                } else if (setting instanceof NumberSetting) {
                    setting.setValue(settings.getDouble(settingName));
                } else if (setting instanceof ModeSetting) {
                    setting.setValue(settings.get(settingName));
                } else if (setting instanceof ColorSetting) {
                    //setting.setValue(settings.get(settingName));
                }
            }

            m.setToggle(true);

            m.setToggle(settings.getBoolean("toggled"));
            m.setKey(settings.getInt("key"));

        }
    }


    private static JSONObject readJSONFile(String filePath) {
        try {
            FileReader file = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(file);
            String jsonString = reader.readLine();
            JSONObject json = new JSONObject(jsonString);
            reader.close();
            return json;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (IllegalArgumentException e){
            return null;
        }catch (NullPointerException e){
            return null;
        }
    }


    public static String getDir() {
        return Minecraft.getMinecraft().gameDir.getAbsoluteFile() + "/skullhack/";
    }
}
