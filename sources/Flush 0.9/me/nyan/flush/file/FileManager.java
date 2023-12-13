package me.nyan.flush.file;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import me.nyan.flush.Flush;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.ModuleManager;
import me.nyan.flush.module.impl.misc.Cape;
import me.nyan.flush.module.impl.misc.NameProtect;
import me.nyan.flush.module.impl.render.ESP;
import me.nyan.flush.module.impl.render.SigmaHUD;
import me.nyan.flush.module.settings.*;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.Map;

public class FileManager {
    private final File file = new File(Flush.getClientPath(), "settings.json");
    private final ModuleManager moduleManager = Flush.getInstance().getModuleManager();

    public FileManager() {
        if (!Flush.getClientPath().exists()) {
            Flush.getClientPath().mkdir();
        }
    }

    public void save() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            JsonWriter writer = new JsonWriter(new PrintWriter(file));
            writer.setIndent("  ");

            writer.beginObject();

            writer.name("modules").beginArray();
            for (Module module : moduleManager.getModules()) {
                writer.beginObject();

                writer.name("name").value(module.getName());
                writer.name("enabled").value(module.isEnabled());
                writer.name("keys").beginArray();
                for (int key : module.getKeys()) {
                    if (key != Keyboard.KEY_NONE) {
                        writer.value(Keyboard.getKeyName(key));
                    }
                }
                writer.endArray();
                writer.name("hidden").value(module.isHidden());

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

            writer.name("sigmaname").value(moduleManager.getModule(SigmaHUD.class).getSigmaname());
            writer.name("nameprotect").value(moduleManager.getModule(NameProtect.class).getCustomName());
            writer.name("imageesp").value(moduleManager.getModule(ESP.class).path);
            writer.name("customcape").value(moduleManager.getModule(Cape.class).getCustomPath());

            writer.endObject();
            writer.close();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        if (!file.exists()) {
            return;
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
                    if (moduleObject.has("key")) {
                        module.clearKeys();
                        int key = Keyboard.getKeyIndex(moduleObject.get("key").getAsString());
                        if (key != Keyboard.KEY_NONE) {
                            module.addKey(key);
                        }
                    }
                    if (moduleObject.has("keys")) {
                        module.clearKeys();
                        JsonArray keys = moduleObject.getAsJsonArray("keys");
                        for (JsonElement element : keys) {
                            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                                int key = Keyboard.getKeyIndex(element.getAsString());
                                if (key != Keyboard.KEY_NONE) {
                                    module.addKey(key);
                                }
                            }
                        }
                    }
                    module.setHidden(moduleObject.get("hidden").getAsBoolean());

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

            moduleManager.getModule(SigmaHUD.class).setSigmaname(json.get("sigmaname").getAsString());
            moduleManager.getModule(NameProtect.class).setCustomName(json.get("nameprotect").getAsString());
            ESP esp = moduleManager.getModule(ESP.class);
            JsonElement imageesp = json.get("imageesp");
            if (imageesp.isJsonPrimitive() && imageesp.getAsJsonPrimitive().isString()) {
                esp.path = imageesp.getAsString();
                if (esp.isEnabled()) {
                    esp.setEnabled(false);
                    esp.setEnabled(true);
                }
            }

            Cape cape = moduleManager.getModule(Cape.class);
            JsonElement customcape = json.get("customcape");
            if (customcape.isJsonPrimitive() && customcape.getAsJsonPrimitive().isString()) {
                cape.setCustomPath(customcape.getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}