// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.config;

import com.google.gson.JsonElement;
import moonsense.MoonsenseClient;
import java.util.Iterator;
import java.util.Collections;
import com.google.gson.JsonParser;
import com.google.gson.GsonBuilder;
import moonsense.settings.SettingWrapper;
import moonsense.settings.Setting;
import moonsense.features.ThemeSettings;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import com.google.gson.JsonObject;
import moonsense.config.utils.Config;

public class ThemeConfig extends Config
{
    public static final ThemeConfig INSTANCE;
    
    static {
        INSTANCE = new ThemeConfig();
    }
    
    public ThemeConfig() {
        super("theme", "json", 0.1);
    }
    
    @Override
    public void saveConfig() {
        this.createStructure();
        final JsonObject configFileJson = new JsonObject();
        configFileJson.addProperty("version", this.version);
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(this.configFile));
            try {
                for (final Setting setting : ThemeSettings.INSTANCE.settings) {
                    if (!setting.hasValue()) {
                        continue;
                    }
                    SettingWrapper.addSettingKey(configFileJson, setting, setting.getObject());
                }
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(configFileJson.toString())));
            }
            finally {
                if (Collections.singletonList(writer).get(0) != null) {
                    writer.close();
                }
            }
            if (Collections.singletonList(writer).get(0) != null) {
                writer.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void loadConfig() {
        this.createStructure();
        final JsonObject configFileJson = this.loadJsonFile(this.configFile);
        this.getNonNull(configFileJson, "version", jsonElement -> MoonsenseClient.info("Detected " + this.name + " version: " + jsonElement.getAsDouble() + " => " + this.version, new Object[0]));
        for (final Setting setting : ThemeSettings.INSTANCE.settings) {
            if (!setting.hasValue()) {
                continue;
            }
            this.getNonNull(configFileJson, setting.getKey(), jsonElement -> SettingWrapper.setValue(setting, jsonElement));
        }
    }
}
