package me.nyan.flush.file;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import me.nyan.flush.Flush;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.ModuleManager;
import me.nyan.flush.module.settings.*;

import java.io.*;
import java.util.Map;

public class ConfigManager {
    private final File path = new File(Flush.getClientPath(), "configs");
    private final ModuleManager moduleManager = Flush.getInstance().getModuleManager();

    public ConfigManager() {
        if (!path.exists()) {
            path.mkdir();
        }
    }

    public boolean save(String config) {
        File file = new File(path, config.replace('/', File.separatorChar) + ".json");

        try {
            JsonWriter writer = new JsonWriter(new PrintWriter(file));
            writer.setIndent("  ");

            writer.beginObject();

            writer.name("modules").beginArray();
            for (Module module : moduleManager.getModules()) {
                writer.beginObject();

                writer.name("name").value(module.getName());
                writer.name("enabled").value(module.isEnabled());

                writer.name("settings").beginObject();
                for (Setting setting : module.getSettings()) {
                    writer.name(setting.getName());

                    if (setting instanceof ColorSetting) {
                        writer.value(((ColorSetting) setting).getRGB());
                    } else if (setting instanceof ModeSetting) {
                        writer.value(((ModeSetting) setting).getValue());
                    } else if (setting instanceof NumberSetting) {
                        writer.value(((NumberSetting) setting).getValue());
                    } else {
                        writer.value(((BooleanSetting) setting).getValue());
                    }
                }
                writer.endObject();

                writer.endObject();
            }
            writer.endArray();

            writer.endObject();
            writer.close();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean load(String config) {
        File file = getPath(config);

        if (!file.exists()) {
            return false;
        }

        try {
            JsonObject json = new JsonParser().parse(new BufferedReader(new FileReader(file))).getAsJsonObject();

            JsonArray modules = json.get("modules").getAsJsonArray();
            for (JsonElement moduleElement : modules) {
                if (!moduleElement.isJsonObject()) {
                    continue;
                }

                try {
                    JsonObject moduleObject = moduleElement.getAsJsonObject();
                    Module module = moduleManager.getModule(moduleObject.get("name").getAsString());
                    if (module == null) {
                        continue;
                    }

                    module.setEnabled(moduleObject.get("enabled").getAsBoolean());

                    JsonObject settings = moduleObject.get("settings").getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : settings.entrySet()) {
                        try {
                            Setting setting = module.getSetting(entry.getKey());
                            if (setting == null) {
                                continue;
                            }

                            JsonElement value = entry.getValue();
                            if (setting instanceof ColorSetting) {
                                ((ColorSetting) setting).setRGB(value.getAsInt());
                            } else if (setting instanceof ModeSetting) {
                                ((ModeSetting) setting).setValue(value.getAsString());
                            } else if (setting instanceof NumberSetting) {
                                ((NumberSetting) setting).setValue(value.getAsDouble());
                            } else {
                                ((BooleanSetting) setting).setValue(value.getAsBoolean());
                            }
                        } catch (NullPointerException | NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (NullPointerException | NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean delete(String config) {
        File file = getPath(config);
        return file.exists() && file.delete();
    }

    private File getPath(String config) {
        return new File(path, config.replace('/', File.separatorChar) + ".json");
    }

    public File getPath() {
        return path;
    }
}