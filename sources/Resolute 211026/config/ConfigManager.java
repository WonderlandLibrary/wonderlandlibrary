// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.config;

import org.apache.commons.io.FilenameUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import java.io.FileWriter;
import com.google.gson.JsonElement;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.Reader;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.util.List;
import java.io.File;
import vip.Resolute.util.player.Manager;

public final class ConfigManager extends Manager<SaveLoad>
{
    public static final File CONFIGS_DIR;
    public static final String EXTENSION = ".json";
    
    public ConfigManager() {
        super(loadConfigs());
        if (!ConfigManager.CONFIGS_DIR.exists()) {
            ConfigManager.CONFIGS_DIR.mkdirs();
        }
    }
    
    public boolean loadConfig(final String configName) {
        if (configName == null) {
            return false;
        }
        final SaveLoad config = this.findConfig(configName);
        if (config == null) {
            return false;
        }
        try {
            final FileReader reader = new FileReader(config.getFile());
            final JsonParser parser = new JsonParser();
            final JsonObject object = (JsonObject)parser.parse((Reader)reader);
            config.load(object);
            return true;
        }
        catch (FileNotFoundException e) {
            return false;
        }
    }
    
    public boolean saveConfig(final String configName) {
        if (configName == null) {
            return false;
        }
        SaveLoad config;
        if ((config = this.findConfig(configName)) == null) {
            final SaveLoad newConfig = config = new SaveLoad(configName);
            this.getElements().add(newConfig);
        }
        final String contentPrettyPrint = new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)config.save());
        try {
            final FileWriter writer = new FileWriter(config.getFile());
            writer.write(contentPrettyPrint);
            writer.close();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    
    public SaveLoad findConfig(final String configName) {
        if (configName == null) {
            return null;
        }
        for (final SaveLoad config : this.getElements()) {
            if (config.getName().equalsIgnoreCase(configName)) {
                return config;
            }
        }
        if (new File(ConfigManager.CONFIGS_DIR, String.valueOf(configName) + ".json").exists()) {
            return new SaveLoad(configName);
        }
        return null;
    }
    
    public boolean deleteConfig(final String configName) {
        if (configName == null) {
            return false;
        }
        final SaveLoad config;
        if ((config = this.findConfig(configName)) != null) {
            final File f = config.getFile();
            this.getElements().remove(config);
            return f.exists() && f.delete();
        }
        return false;
    }
    
    private static ArrayList<SaveLoad> loadConfigs() {
        final ArrayList<SaveLoad> loadedConfigs = new ArrayList<SaveLoad>();
        final File[] files = ConfigManager.CONFIGS_DIR.listFiles();
        if (files != null) {
            File[] array;
            for (int length = (array = files).length, i = 0; i < length; ++i) {
                final File file = array[i];
                if (FilenameUtils.getExtension(file.getName()).equals("json")) {
                    loadedConfigs.add(new SaveLoad(FilenameUtils.removeExtension(file.getName())));
                }
            }
        }
        return loadedConfigs;
    }
    
    static {
        CONFIGS_DIR = new File("Resolute", "configs");
    }
}
