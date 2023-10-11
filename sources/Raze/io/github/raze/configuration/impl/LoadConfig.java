package io.github.raze.configuration.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.raze.Raze;
import io.github.raze.configuration.Configuration;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.settings.system.BaseSetting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoadConfig extends Configuration {

    public LoadConfig() { super("Load", "Load a specific config."); }

    public void loadConfig(String directory, String filename) {
        if (filename == null || filename.isEmpty()) {
            setError("File name is empty!");
            return;
        } else if (!filename.endsWith(".json")) {
            filename += ".json";
        }

        try {
            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(new FileReader(directory + filename));
            JsonObject configData = gson.fromJson(reader, JsonObject.class);
            reader.close();

            setError("None");

            if (configData.has("Version") && configData.get("Version").getAsString().equals(Raze.INSTANCE.getVersion())) {
                for (AbstractModule module : Raze.INSTANCE.managerRegistry.moduleRegistry.getList()) {
                    String categoryName = module.getCategory().toString();
                    JsonArray moduleArray = configData.getAsJsonArray(categoryName);
                    for (JsonElement element : moduleArray) {
                        JsonObject moduleObject = element.getAsJsonObject();
                        if (module.getName().equals(moduleObject.get("Name").getAsString())) {
                            boolean enabled = moduleObject.get("State").getAsBoolean();
                            module.setEnabled(enabled);
                            for (BaseSetting setting : Raze.INSTANCE.managerRegistry.settingRegistry.getSettingsByModule(module)) {
                                if (setting instanceof BooleanSetting) {
                                    try {
                                        boolean bool = moduleObject.get(setting.getName()).getAsBoolean();
                                        BooleanSetting bool2 = (BooleanSetting) setting;
                                        bool2.set(bool);
                                    } catch (Exception e) {
                                        if(e.getMessage() == null)
                                         System.err.println("[R] Error while loading boolean setting: " + setting.getName() + " - setting not found!");
                                        else
                                            System.err.println("[R] Error while loading number setting: " + setting.getName() + " - " + e.getMessage());
                                    }
                                }
                                if (setting instanceof ArraySetting) {
                                    try {
                                        String mode = moduleObject.get(setting.getName()).getAsString();
                                        ArraySetting mode2 = (ArraySetting) setting;
                                        mode2.set(mode);
                                    } catch (Exception e) {
                                        if(e.getMessage() == null)
                                            System.err.println("[R] Error while loading array setting: " + setting.getName() + " - setting not found!");
                                        else
                                            System.err.println("[R] Error while loading number setting: " + setting.getName() + " - " + e.getMessage());
                                    }
                                }
                                if (setting instanceof NumberSetting) {
                                    try {
                                        double number = moduleObject.get(setting.getName()).getAsDouble();
                                        NumberSetting number1 = (NumberSetting) setting;
                                        number1.set(number);
                                    } catch (Exception e) {
                                        if(e.getMessage() == null)
                                            System.err.println("[R] Error while loading number setting: " + setting.getName() + " - setting not found!");
                                        else
                                            System.err.println("[R] Error while loading number setting: " + setting.getName() + " - " + e.getMessage());
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                String version = configData.get("Version").getAsString();

                if (!version.equals(Raze.INSTANCE.getVersion())) {
                    setError("Incompatible version found!");
                }else {
                    setError("None");
                }
                System.out.println("[R]" + getError());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadKeyBinds(String directory, String filename) {
        if (filename == null || filename.isEmpty()) {
            setError("File name is empty!");
            return;
        } else if (!filename.endsWith(".json")) {
            filename += ".json";
        }

        try {
            Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(new FileReader(directory + filename));
            JsonObject configData = gson.fromJson(reader, JsonObject.class);
            reader.close();

            setError("None");

            if (configData.has("Version") && configData.get("Version").getAsString().equals(Raze.INSTANCE.getVersion())) {
                for (AbstractModule module : Raze.INSTANCE.managerRegistry.moduleRegistry.getList()) {
                    String categoryName = module.getCategory().toString();
                    JsonArray moduleArray = configData.getAsJsonArray(categoryName);
                    for (JsonElement element : moduleArray) {
                        JsonObject moduleObject = element.getAsJsonObject();
                        if (module.getName().equals(moduleObject.get("Name").getAsString())) {
                            int keyBind = moduleObject.get("KeyBind").getAsInt();
                            module.setKeyCode(keyBind);
                        }
                    }
                }
            } else {
                String version = configData.get("Version").getAsString();

                if (!version.equals(Raze.INSTANCE.getVersion())) {
                    setError("Incompatible version found!");
                }else {
                    setError("None");
                }
                System.out.println("[R]" + getError());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
