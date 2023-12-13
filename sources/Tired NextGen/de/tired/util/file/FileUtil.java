package de.tired.util.file;

import com.google.gson.*;
import de.tired.Tired;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.guis.newclickgui.setting.Setting;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.base.guis.newclickgui.setting.impl.ColorPickerSetting;
import de.tired.base.module.Module;
import de.tired.util.render.ColorUtil;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Map;

public class FileUtil {

    public static FileUtil instance;

    public FileUtil() {
        instance = this;
    }

    public void loadClient() {
        final File f = new File(Tired.DIR + "\\" + "Data" + ".json");
        if (f.exists()) {
            try {
                load(new JsonParser().parse(new FileReader(f)));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void save() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        final FileWriter fileWriter;
        if (Tired.DIR.exists()) {
            try {
                fileWriter = new FileWriter(Tired.DIR + "\\" + "Data" + ".json");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            gson.toJson(serialize(), fileWriter);
            try {
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public JsonObject serialize() {
        final JsonObject jsonObject = new JsonObject();

        Tired.INSTANCE.moduleManager.getModuleList().forEach(feature -> {
            JsonObject moduleJson = new JsonObject();
            moduleJson.addProperty("Status", feature.isState());
            moduleJson.addProperty("Keybind", feature.key);
            final JsonObject settingJson = new JsonObject();
            for (Setting setting : Tired.INSTANCE.settingsManager.getSettingsByMod(feature)) {
                if (setting == null) continue;
                if (setting instanceof BooleanSetting) {
                    settingJson.addProperty(setting.getName(), ((BooleanSetting) setting).getValue());
                }
                if (setting instanceof ModeSetting) {
                    settingJson.addProperty(setting.getName(), ((ModeSetting) setting).getValue());
                }

                if (setting instanceof NumberSetting) {
                    settingJson.addProperty(setting.getName(), ((NumberSetting) setting).getValue().toString());
                }
                if (setting instanceof ColorPickerSetting) {
                    settingJson.addProperty(setting.getName(), ((ColorPickerSetting) setting).getColorPickerC().getRGB());
                }
            }
            moduleJson.add("Values", settingJson);
            jsonObject.add(feature.name, moduleJson);
        });
        return jsonObject;
    }


    public boolean load(final JsonElement jsonElement) {
        try {
            if (jsonElement instanceof JsonNull)
                return false;

            final List<Module> antiStackOverflow = Tired.INSTANCE.moduleManager.getModuleList();
            for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                for (final Module module : antiStackOverflow) {
                    if (entry.getKey().equalsIgnoreCase(module.name)) {
                        final JsonObject jsonModule = (JsonObject) entry.getValue();
                        if (jsonModule.get("Status").getAsBoolean()) {
                            module.executeMod();
                        } else
                            module.disableModule();

                        module.key = jsonModule.get("Keybind").getAsInt();

                        if (jsonModule.has("Values")) {
                            jsonModule.get("Values").getAsJsonObject().entrySet().forEach(stringJsonElementEntry -> {
                                Setting property = Tired.INSTANCE.settingsManager.settingBy(stringJsonElementEntry.getKey(), module);

                                if (property instanceof BooleanSetting) {
                                    ((BooleanSetting) property).setValue(stringJsonElementEntry.getValue().getAsBoolean());
                                } else if (property instanceof ModeSetting) {
                                    ((ModeSetting) property).setValue(stringJsonElementEntry.getValue().getAsString());
                                } else if (property instanceof NumberSetting) {
                                    ((NumberSetting) property).setValue(stringJsonElementEntry.getValue().getAsDouble());
                                } else if (property instanceof ColorPickerSetting) {
                                    final int color = stringJsonElementEntry.getValue().getAsInt();
                                    Color c = ColorUtil.valueOf(color);
                                    ((ColorPickerSetting) property).setColorPickerC(c);
                                }
                            });
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }


}
