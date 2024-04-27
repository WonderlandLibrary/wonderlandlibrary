package net.ccbluex.liquidbounce.features.special;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.client.Minecraft;
import scala.util.parsing.json.JSON;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Setting {
    private final String name;

    public Setting(String name,boolean create) {
        this.name = name;

        if (create) {
            save();
        }

        try {
            Path info = Paths.get(LiquidBounce.fileManager.dir.getAbsolutePath(),"settings","info.json");
            File infoFile = info.toFile();
            if (!infoFile.exists()) {
                infoFile.createNewFile();
            }
            JsonObject infoJson;
            infoJson = new Gson().fromJson(new String(Files.readAllBytes(info),StandardCharsets.UTF_8),JsonObject.class);
            if (infoJson == null) {
                infoJson = new JsonObject();
            }
            JsonObject settingInfoJson = new JsonObject();
            settingInfoJson.addProperty("name",getName());
            infoJson.add(name,settingInfoJson);
            Files.write(info,infoJson.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getName() {
        return name;
    }

    public Path getPath() {
        return Paths.get(LiquidBounce.fileManager.dir.getAbsolutePath(),"settings",name);
    }

    public void load() {
        Path clickGui = Paths.get(LiquidBounce.fileManager.dir.getPath(), "clickgui.json");
        Path hud = Paths.get(LiquidBounce.fileManager.dir.getPath(), "hud.json");
        Path modules = Paths.get(LiquidBounce.fileManager.dir.getPath(), "modules.json");
        Path values = Paths.get(LiquidBounce.fileManager.dir.getPath(), "values.json");
        Path xrayBlocks = Paths.get(LiquidBounce.fileManager.dir.getPath(), "xray-blocks.json");

        List<Path> paths = new ArrayList<>();
        paths.add(clickGui);
        paths.add(hud);
        paths.add(modules);
        paths.add(values);
        paths.add(xrayBlocks);

        Path settingClickGui = Paths.get(getPath().toString(), "clickgui.json");
        Path settingHud = Paths.get(getPath().toString(), "hud.json");
        Path settingModules = Paths.get(getPath().toString(), "modules.json");
        Path settingValues = Paths.get(getPath().toString(), "values.json");
        Path settingXrayBlocks = Paths.get(getPath().toString(), "xray-blocks.json");

        List<Path> settingPaths = new ArrayList<>();
        settingPaths.add(settingClickGui);
        settingPaths.add(settingHud);
        settingPaths.add(settingModules);
        settingPaths.add(settingValues);
        settingPaths.add(settingXrayBlocks);

        for (Path path : paths) {
            for (Path settingPath : settingPaths) {
                File pathFile = path.toFile();
                File settingPathFile = settingPath.toFile();
                if (pathFile.getName().equals(settingPathFile.getName())) {
                    try {
                        if (!pathFile.exists()) {
                            pathFile.createNewFile();
                        }
                        if (!Paths.get(settingPathFile.getParent()).toFile().exists()) {
                            Paths.get(settingPathFile.getParent()).toFile().mkdir();
                        }
                        if (!settingPathFile.exists()) {
                            settingPathFile.createNewFile();
                        }
                        byte[] settingPathBytes = Files.readAllBytes(settingPath);
                        Files.write(path,settingPathBytes);
                    } catch (IOException exception) {
                        throw new RuntimeException(exception);
                    }
                }
            }
        }
    }

    public void save() {
        Path clickGui = Paths.get(LiquidBounce.fileManager.dir.getAbsolutePath(),"clickgui.json");
        Path hud = Paths.get(LiquidBounce.fileManager.dir.getAbsolutePath(),"hud.json");
        Path modules = Paths.get(LiquidBounce.fileManager.dir.getAbsolutePath(),"modules.json");
        Path values = Paths.get(LiquidBounce.fileManager.dir.getAbsolutePath(),"values.json");
        Path xrayBlocks = Paths.get(LiquidBounce.fileManager.dir.getAbsolutePath(),"xray-blocks.json");

        List<Path> paths = new ArrayList<>();
        paths.add(clickGui);
        paths.add(hud);
        paths.add(modules);
        paths.add(values);
        paths.add(xrayBlocks);

        Path settingClickGui = Paths.get(getPath().toString(),"clickgui.json");
        Path settingHud = Paths.get(getPath().toString(),"hud.json");
        Path settingModules = Paths.get(getPath().toString(),"modules.json");
        Path settingValues = Paths.get(getPath().toString(),"values.json");
        Path settingXrayBlocks = Paths.get(getPath().toString(),"xray-blocks.json");

        List<Path> settingPaths = new ArrayList<>();
        settingPaths.add(settingClickGui);
        settingPaths.add(settingHud);
        settingPaths.add(settingModules);
        settingPaths.add(settingValues);
        settingPaths.add(settingXrayBlocks);

        for (Path path : paths) {
            for (Path settingPath : settingPaths) {
                File pathFile = path.toFile();
                File settingPathFile = settingPath.toFile();
                if (pathFile.getName().equals(settingPathFile.getName())) {
                    try {
                        if (!pathFile.exists()) {
                            pathFile.createNewFile();
                        }
                        if (!Paths.get(settingPathFile.getParent()).toFile().exists()) {
                            Paths.get(settingPathFile.getParent()).toFile().mkdir();
                        }
                        if (!settingPathFile.exists()) {
                            settingPathFile.createNewFile();
                        }
                        byte[] pathBytes = Files.readAllBytes(path);
                        Files.write(settingPath,pathBytes);
                    } catch (IOException exception) {
                        throw new RuntimeException(exception);
                    }
                }
            }
        }
        Path settingInfo = Paths.get(getPath().toString(),"info.json");
        if (!settingInfo.toFile().exists()) {
            try {
                settingInfo.toFile().createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        JsonObject settingInfoJson;
        try {
            settingInfoJson = new Gson().fromJson(new String(Files.readAllBytes(settingInfo),StandardCharsets.UTF_8), JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (settingInfoJson == null) {
            settingInfoJson = new JsonObject();
        }
        settingInfoJson.addProperty("name",getName());
        try {
            Files.write(settingInfo,settingInfoJson.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
