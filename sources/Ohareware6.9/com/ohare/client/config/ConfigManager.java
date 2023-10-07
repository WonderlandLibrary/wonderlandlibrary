package com.ohare.client.config;

import com.google.gson.*;
import com.ohare.client.Client;
import com.ohare.client.module.Module;
import com.ohare.client.module.modules.visuals.hudcomps.HudComp;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

public class ConfigManager {
    private File dir;
    private ArrayList<Config> configs = new ArrayList<>();
    public Config currentConfig;

    public ConfigManager(File dir) {
        this.dir = dir;
    }

    public ArrayList<Config> getConfigs() {
        return configs;
    }

    public void clear() throws IOException {
        FileUtils.cleanDirectory(new File(dir + "/"));
    }

    public void setCurrentConfig(Config config) {
        currentConfig = config;
    }

    public void load() {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (dir != null) {
            File[] files = dir.listFiles(f -> !f.isDirectory() && FilenameUtils.getExtension(f.getName()).equals("json"));
            for (File f : files) {
                Config config = new Config(FilenameUtils.removeExtension(f.getName()).replace(" ", ""));
                setCurrentConfig(config);
                configs.add(config);
            }
        }
    }

    public void deleteConfig(String cfgname) {
        File f = new File(dir, cfgname + ".json");
        if (f.exists()) {
            try {
                Files.delete(f.toPath());
            } catch (IOException e) {
            }
        }
        Client.INSTANCE.getConfigManager().getConfigs().remove(getConfig(cfgname));
    }

    public void saveConfig(String cfgname, boolean key) {
        File f = new File(dir, cfgname + ".json");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
            }
        }
        JsonArray arr = new JsonArray();
        Client.INSTANCE.getModuleManager().getModuleMap().values().forEach(module -> {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", module.getLabel().toLowerCase());
            module.save(obj, key);
            arr.add(obj);
        });

        Client.INSTANCE.getHudCompManager().getHudMap().values().forEach(comp -> {
            JsonObject obj1 = new JsonObject();
            obj1.addProperty("name", comp.getLabel().toLowerCase());
            comp.save(obj1);
            arr.add(obj1);
        });

        try (Writer writer = new FileWriter(f)) {
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(arr));
        } catch (IOException e) {
            f.delete();
        }
    }

    public void loadConfig(String cfgname) {
        File f = new File(dir, cfgname + ".json");
        if (!f.exists()) {
            return;
        }
        try {
            Reader reader = new FileReader(f);
            JsonElement node = new JsonParser().parse(reader);
            JsonArray arr = node.getAsJsonArray();
            if (!arr.isJsonArray()) {
                return;
            }
            arr.forEach(element -> {
                JsonObject subobj = element.getAsJsonObject();
                String name = subobj.get("name").getAsString();
                Module m = Client.INSTANCE.getModuleManager().getModule(name);
                if (m == null) {
                    HudComp hudcomp = Client.INSTANCE.getHudCompManager().getHudComp(name);
                    if (hudcomp != null) {
                        hudcomp.load(subobj);
                    }
                } else {
                    m.load(subobj);
                }
            });
        } catch (FileNotFoundException e) {
        }
    }

    public Config getPresetByName(String name) {
        for (Config p : configs) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public Config getConfig(String name) {
        for (Config cfg : configs) {
            if (cfg.getName().equalsIgnoreCase(name)) {
                return cfg;
            }
        }
        return null;
    }

    public boolean isConfig(String name) {
        return getConfig(name) != null;
    }
}
