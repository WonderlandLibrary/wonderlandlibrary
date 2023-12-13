package vip.radium.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FilenameUtils;
import vip.radium.RadiumClient;
import vip.radium.utils.handler.Manager;

import java.io.*;
import java.util.ArrayList;

public final class ConfigManager extends Manager<Config> {

    public ConfigManager() {
        super(loadConfigs());

        if (!CONFIGS_DIR.exists()) {
            boolean ignored = CONFIGS_DIR.mkdirs();
        }
    }

    public static final File CONFIGS_DIR = new File(RadiumClient.NAME, "configs");
    public static final String EXTENSION = ".json";

    public boolean loadConfig(String configName) {
        if (configName == null) return false;
        Config config = findConfig(configName);

        if (config == null) return false;
        try {
            FileReader reader = new FileReader(config.getFile());
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(reader);
            config.load(object);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public boolean saveConfig(String configName) {
        if (configName == null) return false;
        Config config;
        if ((config = findConfig(configName)) == null) {
            Config newConfig = (config = new Config(configName));
            getElements().add(newConfig);
        }

        String contentPrettyPrint = new GsonBuilder().setPrettyPrinting().create().toJson(config.save());
        try {
            FileWriter writer = new FileWriter(config.getFile());
            writer.write(contentPrettyPrint);
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Config findConfig(String configName) {
        if (configName == null) return null;
        for (Config config : getElements()) {
            if (config.getName().equalsIgnoreCase(configName))
                return config;
        }

        if (new File(CONFIGS_DIR, configName + EXTENSION).exists())
            return new Config(configName);

        return null;
    }

    public boolean deleteConfig(String configName) {
        if (configName == null) return false;
        Config config;
        if ((config = findConfig(configName)) != null) {
            final File f = config.getFile();
            getElements().remove(config);
            return f.exists() && f.delete();
        }
        return false;
    }

    private static ArrayList<Config> loadConfigs() {
        final ArrayList<Config> loadedConfigs = new ArrayList<>();
        File[] files = CONFIGS_DIR.listFiles();
        if (files != null) {
            for (File file : files) {
                if (FilenameUtils.getExtension(file.getName()).equals("json"))
                    loadedConfigs.add(new Config(FilenameUtils.removeExtension(file.getName())));
            }
        }
        return loadedConfigs;
    }
}
