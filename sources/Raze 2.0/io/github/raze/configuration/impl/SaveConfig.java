package io.github.raze.configuration.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.raze.Raze;
import io.github.raze.configuration.Configuration;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.settings.system.BaseSetting;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SaveConfig extends Configuration {

    public SaveConfig() {
        super("Save", "Save your settings to a config file.");
    }

    public void saveConfig(String directory, String filename) {
        if (filename == null || filename.isEmpty()) {
            setError("File name is empty!");
        } else if (!filename.endsWith(".json")) {
            setError("None");
            filename += ".json";
        }

        try {
            JsonObject configData = new JsonObject();

            configData.addProperty("Version", Raze.INSTANCE.getVersion());

            for (ModuleCategory category : ModuleCategory.values()) {
                JsonArray moduleArray = new JsonArray();
                List<AbstractModule> modulesInCategory = Raze.INSTANCE.managerRegistry.moduleRegistry.getModulesByCategory(category);
                for (AbstractModule module : modulesInCategory) {
                    JsonObject moduleObject = new JsonObject();
                    moduleObject.addProperty("Name", module.getName());
                    moduleObject.addProperty("State", module.isEnabled());
                    for (BaseSetting setting : Raze.INSTANCE.managerRegistry.settingRegistry.getSettingsByModule(module)) {
                        if (setting instanceof ArraySetting) {
                            moduleObject.addProperty(setting.getName(), ((ArraySetting) setting).get());
                        } else if (setting instanceof NumberSetting) {
                            moduleObject.addProperty(setting.getName(), ((NumberSetting) setting).get().floatValue());
                        } else if (setting instanceof BooleanSetting) {
                            moduleObject.addProperty(setting.getName(), ((BooleanSetting) setting).get());
                        }
                    }
                    moduleArray.add(moduleObject);
                }
                configData.add(category.name(), moduleArray);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(directory + filename);
            gson.toJson(configData, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setError("None");
    }

    public void saveKeyBinds(String directory, String filename) {
        if (filename == null || filename.isEmpty()) {
            setError("File name is empty!");
            return;
        } else if (!filename.endsWith(".json")) {
            setError("None");
            filename += ".json";
        }

        try {
            JsonObject configData = new JsonObject();

            configData.addProperty("Version", Raze.INSTANCE.getVersion());

            for (ModuleCategory category : ModuleCategory.values()) {
                JsonArray moduleArray = new JsonArray();
                List<AbstractModule> modulesInCategory = Raze.INSTANCE.managerRegistry.moduleRegistry.getModulesByCategory(category);
                for (AbstractModule module : modulesInCategory) {
                    JsonObject moduleObject = new JsonObject();
                    moduleObject.addProperty("Name", module.getName());
                    moduleObject.addProperty("KeyBind", module.getKeyCode());
                    moduleArray.add(moduleObject);
                }
                configData.add(category.name(), moduleArray);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(directory + filename);
            gson.toJson(configData, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setError("None");
    }

}
