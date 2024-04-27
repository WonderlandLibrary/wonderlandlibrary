package net.ccbluex.liquidbounce.features.special;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ccbluex.liquidbounce.LiquidBounce;
import scala.collection.Iterator;
import scala.reflect.io.Directory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettingManager {
    private final List<Setting> settings = new ArrayList<>();

    public void load() {
        try {
            Path info = Paths.get(LiquidBounce.fileManager.dir.getAbsolutePath(),"settings","info.json");
            if (info.toFile().exists()) {
                File infoFile = info.toFile();
                JsonObject infoJson = new Gson().fromJson(new String(Files.readAllBytes(info), StandardCharsets.UTF_8),JsonObject.class);
                if (infoJson == null) {
                    infoJson = new JsonObject();
                }
                for (Map.Entry<String, JsonElement> entry : infoJson.entrySet()) {
                    settings.add(new Setting(entry.getKey(),false));
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    public List<Setting> getSettings() {
        return settings;
    }

    public Setting getSetting(String name) {
        for (Setting setting : settings) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return setting;
            }
        }
        return null;
    }

    public void addSetting(Setting setting) {
        settings.add(setting);
    }
}
