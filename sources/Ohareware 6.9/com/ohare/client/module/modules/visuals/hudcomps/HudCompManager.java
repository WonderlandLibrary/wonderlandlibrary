package com.ohare.client.module.modules.visuals.hudcomps;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ohare.client.module.modules.visuals.hudcomps.comps.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * made by oHare for oHareWare
 *
 * @since 5/31/2019
 **/
public class HudCompManager {
    public Map<String,HudComp> map = new HashMap<>();

    private File directory;

    public Map<String, HudComp> getHudMap() {
        return map;
    }

    public void initialize() {
        register(WatermarkComp.class);
        register(ItemComp.class);
        register(ScoreboardComp.class);
        register(TargetHudComp.class);
        register(ArrayListComp.class);
        register(SpeedComp.class);
        register(CoordinateComp.class);
        register(RadarComp.class);
        register(PotionComp.class);
        register(TabGUIComp.class);
        register(TimeComp.class);
        register(PingComp.class);
    }

    public HudComp getHudCompClass(final Class<?> clazz) {
        for (HudComp hudComp : getHudMap().values()) {
            if (hudComp.getClass().equals(clazz)) {
                return hudComp;
            }
        }
        return null;
    }

    public HudComp getHudComp(String name) {
        return getHudMap().get(name.toLowerCase());
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    private void register(Class<? extends HudComp> compClass) {
        try {
            HudComp createdHud = compClass.newInstance();
            map.put(createdHud.getLabel().toLowerCase(), createdHud);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveComps() {
        if (map.values().isEmpty()) {
            directory.delete();
        }
        File[] files = directory.listFiles();
        if (!directory.exists()) {
            directory.mkdir();
        } else if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        map.values().forEach(comp -> {
            File file = new File(directory, comp.getLabel() + ".json");
            JsonObject node = new JsonObject();
            comp.save(node);
            if (node.entrySet().isEmpty()) {
                return;
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                return;
            }
            try (Writer writer = new FileWriter(file)) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(node));
            } catch (IOException e) {
                file.delete();
            }
        });
        files = directory.listFiles();
        if (files == null || files.length == 0) {
            directory.delete();
        }
    }

    public void loadComps() {
        map.values().forEach(comp -> {
            final File file = new File(directory, comp.getLabel() + ".json");
            if (!file.exists()) {
                return;
            }
            try (Reader reader = new FileReader(file)) {
                JsonElement node = new JsonParser().parse(reader);
                if (!node.isJsonObject()) {
                    return;
                }
                comp.load(node.getAsJsonObject());
            } catch (IOException e) {
            }
        });
    }
}
