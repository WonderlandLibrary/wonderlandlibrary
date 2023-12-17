// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.config;

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
import org.apache.commons.io.FilenameUtils;
import java.util.ArrayList;
import java.io.File;

public final class ConfigManager extends Manager<Config>
{
    public static final File configDirectory;
    private static final ArrayList<Config> loadedConfigs;
    
    public ConfigManager() {
        this.setContents(loadConfigs());
        ConfigManager.configDirectory.mkdirs();
    }
    
    private static ArrayList<Config> loadConfigs() {
        final File[] files = ConfigManager.configDirectory.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (FilenameUtils.getExtension(file.getName()).equals("json")) {
                    ConfigManager.loadedConfigs.add(new Config(FilenameUtils.removeExtension(file.getName())));
                }
            }
        }
        return ConfigManager.loadedConfigs;
    }
    
    public static ArrayList<Config> getLoadedConfigs() {
        return ConfigManager.loadedConfigs;
    }
    
    public void load() {
        if (!ConfigManager.configDirectory.exists()) {
            ConfigManager.configDirectory.mkdirs();
        }
        if (ConfigManager.configDirectory != null) {
            final File[] listFiles;
            final File[] files = listFiles = ConfigManager.configDirectory.listFiles(f -> !f.isDirectory() && FilenameUtils.getExtension(f.getName()).equals("json"));
            for (final File f2 : listFiles) {
                final Config config = new Config(FilenameUtils.removeExtension(f2.getName()).replace(" ", ""));
                ConfigManager.loadedConfigs.add(config);
            }
        }
    }
    
    public boolean loadConfig(final String configName) {
        if (configName == null) {
            return false;
        }
        final Config config = this.findConfig(configName);
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
        Config config;
        if ((config = this.findConfig(configName)) == null) {
            final Config newConfig;
            config = (newConfig = new Config(configName));
            this.getContents().add(newConfig);
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
    
    public Config findConfig(final String configName) {
        if (configName == null) {
            return null;
        }
        for (final Config config : this.getContents()) {
            if (config.getName().equalsIgnoreCase(configName)) {
                return config;
            }
        }
        if (new File(ConfigManager.configDirectory, configName + ".json").exists()) {
            return new Config(configName);
        }
        return null;
    }
    
    public boolean deleteConfig(final String configName) {
        if (configName == null) {
            return false;
        }
        final Config config;
        if ((config = this.findConfig(configName)) != null) {
            final File f = config.getFile();
            this.getContents().remove(config);
            return f.exists() && f.delete();
        }
        return false;
    }
    
    static {
        configDirectory = new File("C:\\Minced\\game", "\\minced\\configs");
        loadedConfigs = new ArrayList<Config>();
    }
}
