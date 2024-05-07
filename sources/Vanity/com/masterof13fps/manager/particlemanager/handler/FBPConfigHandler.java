package com.masterof13fps.manager.particlemanager.handler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Paths;

import com.masterof13fps.manager.particlemanager.FBP;
import com.masterof13fps.manager.particlemanager.util.FBPObfUtil;
import net.minecraft.block.material.Material;

public class FBPConfigHandler {
    static FileInputStream fis;

    static InputStreamReader isr;

    static BufferedReader br;

    public static void init() {
        try {
            defaults(false);
            if (!Paths.get(FBP.config.getParent(), new String[0]).toFile().exists())
                Paths.get(FBP.config.getParent(), new String[0]).toFile().mkdirs();
            if (!FBP.config.exists()) {
                FBP.config.createNewFile();
                write();
            }
            if (!FBP.particleBlacklistFile.exists())
                FBP.particleBlacklistFile.createNewFile();
            if (!FBP.floatingMaterialsFile.exists()) {
                FBP.floatingMaterialsFile.createNewFile();
                FBP.INSTANCE.floatingMaterials.clear();
                FBP.INSTANCE.floatingMaterials.add(Material.leaves);
                FBP.INSTANCE.floatingMaterials.add(Material.plants);
                FBP.INSTANCE.floatingMaterials.add(Material.ice);
                FBP.INSTANCE.floatingMaterials.add(Material.packedIce);
                FBP.INSTANCE.floatingMaterials.add(Material.carpet);
                FBP.INSTANCE.floatingMaterials.add(Material.wood);
                FBP.INSTANCE.floatingMaterials.add(Material.web);
            } else {
                readFloatingMaterials();
            }
            read();
            readParticleBlacklist();
            write();
            writeParticleBlacklist();
            writeFloatingMaterials();
            closeStreams();
        } catch (IOException e) {
            closeStreams();
            write();
        }
    }

    public static void write() {
        try {
            PrintWriter writer = new PrintWriter(FBP.config.getPath(), "UTF-8");
            writer.println("enabled=" + FBP.enabled);
            writer.println("weatherParticleDensity=" + FBP.weatherParticleDensity);
            writer.println("particlesPerAxis=" + FBP.particlesPerAxis);
            writer.println("restOnFloor=" + FBP.restOnFloor);
            writer.println("waterPhysics=" + FBP.waterPhysics);
            writer.println("fancyFlame=" + FBP.fancyFlame);
            writer.println("fancySmoke=" + FBP.fancySmoke);
            writer.println("fancyRain=" + FBP.fancyRain);
            writer.println("fancySnow=" + FBP.fancySnow);
            writer.println("smartBreaking=" + FBP.smartBreaking);
            writer.println("lowTraction=" + FBP.lowTraction);
            writer.println("bounceOffWalls=" + FBP.bounceOffWalls);
            writer.println("showInMillis=" + FBP.showInMillis);
            writer.println("randomRotation=" + FBP.randomRotation);
            writer.println("cartoonMode=" + FBP.cartoonMode);
            writer.println("entityCollision=" + FBP.entityCollision);
            writer.println("randomizedScale=" + FBP.randomizedScale);
            writer.println("randomFadingSpeed=" + FBP.randomFadingSpeed);
            writer.println("spawnRedstoneBlockParticles=" + FBP.spawnRedstoneBlockParticles);
            writer.println("spawnWhileFrozen=" + FBP.spawnWhileFrozen);
            writer.println("infiniteDuration=" + FBP.infiniteDuration);
            writer.println("minAge=" + FBP.minAge);
            writer.println("maxAge=" + FBP.maxAge);
            writer.println("scaleMult=" + FBP.scaleMult);
            writer.println("gravityMult=" + FBP.gravityMult);
            writer.print("rotationMult=" + FBP.rotationMult);
            writer.close();
        } catch (Exception e) {
            closeStreams();
            if (!FBP.config.exists()) {
                if (!Paths.get(FBP.config.getParent(), new String[0]).toFile().exists())
                    Paths.get(FBP.config.getParent(), new String[0]).toFile().mkdirs();
                try {
                    FBP.config.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            write();
        }
    }

    public static void writeParticleBlacklist() {
        try {
            PrintWriter writer = new PrintWriter(FBP.particleBlacklistFile.getPath(), "UTF-8");
            for (String ex : FBP.INSTANCE.blockParticleBlacklist)
                writer.println(ex);
            writer.close();
        } catch (Exception e) {
            closeStreams();
            if (!FBP.particleBlacklistFile.exists()) {
                if (!Paths.get(FBP.particleBlacklistFile.getParent(), new String[0]).toFile().exists())
                    Paths.get(FBP.particleBlacklistFile.getParent(), new String[0]).toFile().mkdirs();
                try {
                    FBP.particleBlacklistFile.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    static void writeFloatingMaterials() {
        try {
            PrintWriter writer = new PrintWriter(FBP.floatingMaterialsFile.getPath(), "UTF-8");
            Field[] materials = Material.class.getDeclaredFields();
            for (Field f : materials) {
                String fieldName = f.getName();
                if (f.getType() == Material.class) {
                    String translated = FBPObfUtil.translateObfMaterialName(fieldName).toLowerCase();
                    try {
                        Material mat = (Material)f.get(null);
                        if (mat != Material.air) {
                            boolean flag = FBP.INSTANCE.doesMaterialFloat(mat);
                            writer.println(translated + "=" + flag);
                        }
                    } catch (Exception exception) {}
                }
            }
            writer.close();
        } catch (Exception e) {
            closeStreams();
        }
    }

    static void read() {
        try {
            fis = new FileInputStream(FBP.config);
            isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll(" ", "");
                if (line.contains("enabled=")) {
                    FBP.enabled = Boolean.valueOf(line.replace("enabled=", "")).booleanValue();
                    continue;
                }
                if (line.contains("weatherParticleDensity=")) {
                    FBP.weatherParticleDensity = Double.valueOf(line.replace("weatherParticleDensity=", "")).doubleValue();
                    continue;
                }
                if (line.contains("particlesPerAxis=")) {
                    FBP.particlesPerAxis = Integer.valueOf(line.replace("particlesPerAxis=", "")).intValue();
                    continue;
                }
                if (line.contains("restOnFloor=")) {
                    FBP.restOnFloor = Boolean.valueOf(line.replace("restOnFloor=", "")).booleanValue();
                    continue;
                }
                if (line.contains("waterPhysics=")) {
                    FBP.waterPhysics = Boolean.valueOf(line.replace("waterPhysics=", "")).booleanValue();
                    continue;
                }
                if (line.contains("fancyFlame=")) {
                    FBP.fancyFlame = Boolean.valueOf(line.replace("fancyFlame=", "")).booleanValue();
                    continue;
                }
                if (line.contains("fancySmoke=")) {
                    FBP.fancySmoke = Boolean.valueOf(line.replace("fancySmoke=", "")).booleanValue();
                    continue;
                }
                if (line.contains("fancyRain=")) {
                    FBP.fancyRain = Boolean.valueOf(line.replace("fancyRain=", "")).booleanValue();
                    continue;
                }
                if (line.contains("fancySnow=")) {
                    FBP.fancySnow = Boolean.valueOf(line.replace("fancySnow=", "")).booleanValue();
                    continue;
                }
                if (line.contains("smartBreaking=")) {
                    FBP.smartBreaking = Boolean.valueOf(line.replace("smartBreaking=", "")).booleanValue();
                    continue;
                }
                if (line.contains("lowTraction=")) {
                    FBP.lowTraction = Boolean.valueOf(line.replace("lowTraction=", "")).booleanValue();
                    continue;
                }
                if (line.contains("bounceOffWalls=")) {
                    FBP.bounceOffWalls = Boolean.valueOf(line.replace("bounceOffWalls=", "")).booleanValue();
                    continue;
                }
                if (line.contains("showInMillis=")) {
                    FBP.showInMillis = Boolean.valueOf(line.replace("showInMillis=", "")).booleanValue();
                    continue;
                }
                if (line.contains("randomRotation=")) {
                    FBP.randomRotation = Boolean.valueOf(line.replace("randomRotation=", "")).booleanValue();
                    continue;
                }
                if (line.contains("cartoonMode=")) {
                    FBP.cartoonMode = Boolean.valueOf(line.replace("cartoonMode=", "")).booleanValue();
                    continue;
                }
                if (line.contains("entityCollision=")) {
                    FBP.entityCollision = Boolean.valueOf(line.replace("entityCollision=", "")).booleanValue();
                    continue;
                }
                if (line.contains("randomFadingSpeed=")) {
                    FBP.randomFadingSpeed = Boolean.valueOf(line.replace("randomFadingSpeed=", "")).booleanValue();
                    continue;
                }
                if (line.contains("smoothTransitions=")) {
                    FBP.randomizedScale = Boolean.valueOf(line.replace("randomizedScale=", "")).booleanValue();
                    continue;
                }
                if (line.contains("spawnWhileFrozen=")) {
                    FBP.spawnWhileFrozen = Boolean.valueOf(line.replace("spawnWhileFrozen=", "")).booleanValue();
                    continue;
                }
                if (line.contains("spawnRedstoneBlockParticles=")) {
                    FBP.spawnRedstoneBlockParticles = Boolean.valueOf(line.replace("spawnRedstoneBlockParticles=", "")).booleanValue();
                    continue;
                }
                if (line.contains("infiniteDuration=")) {
                    FBP.infiniteDuration = Boolean.valueOf(line.replace("infiniteDuration=", "")).booleanValue();
                    continue;
                }
                if (line.contains("minAge=")) {
                    FBP.minAge = Integer.valueOf(line.replace("minAge=", "")).intValue();
                    continue;
                }
                if (line.contains("maxAge=")) {
                    FBP.maxAge = Integer.valueOf(line.replace("maxAge=", "")).intValue();
                    continue;
                }
                if (line.contains("scaleMult=")) {
                    FBP.scaleMult = Double.valueOf(line.replace("scaleMult=", "")).doubleValue();
                    continue;
                }
                if (line.contains("gravityMult=")) {
                    FBP.gravityMult = Double.valueOf(line.replace("gravityMult=", "")).doubleValue();
                    continue;
                }
                if (line.contains("rotationMult="))
                    FBP.rotationMult = Double.valueOf(line.replace("rotationMult=", "")).doubleValue();
            }
            closeStreams();
        } catch (Exception e) {
            closeStreams();
            write();
        }
    }

    static void readParticleBlacklist() {
        try {
            fis = new FileInputStream(FBP.particleBlacklistFile);
            isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
            FBP.INSTANCE.resetBlacklist();
            String line;
            while ((line = br.readLine()) != null && !(line = line.replaceAll(" ", "").toLowerCase()).equals(""))
                FBP.INSTANCE.addToBlacklist(line);
        } catch (Exception exception) {}
        closeStreams();
    }

    static void readFloatingMaterials() {
        try {
            fis = new FileInputStream(FBP.floatingMaterialsFile);
            isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);
            FBP.INSTANCE.floatingMaterials.clear();
            Field[] materials = Material.class.getDeclaredFields();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().toLowerCase();
                String[] split = line.split("=");
                if (split.length < 2)
                    continue;
                String materialName = split[0].replace("_", "");
                boolean flag = Boolean.parseBoolean(split[1]);
                if (!flag)
                    continue;
                boolean found = false;
                for (Field f : materials) {
                    String fieldName = f.getName();
                    if (f.getType() == Material.class) {
                        String translated = FBPObfUtil.translateObfMaterialName(fieldName).toLowerCase().replace("_", "");
                        if (materialName.equals(translated))
                            try {
                                Material mat = (Material)f.get(null);
                                if (!FBP.INSTANCE.floatingMaterials.contains(mat))
                                    FBP.INSTANCE.floatingMaterials.add(mat);
                                found = true;
                                break;
                            } catch (Exception exception) {}
                    }
                }
                if (!found)
                    System.out.println("[FBP]: Material not recognized: " + materialName);
            }
            closeStreams();
        } catch (Exception e) {
            closeStreams();
            write();
        }
    }

    static void closeStreams() {
        try {
            br.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void defaults(boolean write) {
        FBP.minAge = 10;
        FBP.maxAge = 55;
        FBP.scaleMult = 0.75D;
        FBP.gravityMult = 1.0D;
        FBP.rotationMult = 1.0D;
        FBP.particlesPerAxis = 4;
        FBP.weatherParticleDensity = 1.0D;
        FBP.lowTraction = false;
        FBP.bounceOffWalls = true;
        FBP.randomRotation = true;
        FBP.cartoonMode = false;
        FBP.entityCollision = false;
        FBP.randomizedScale = true;
        FBP.randomFadingSpeed = true;
        FBP.spawnRedstoneBlockParticles = false;
        FBP.infiniteDuration = false;
        FBP.spawnWhileFrozen = true;
        FBP.smartBreaking = true;
        FBP.fancyRain = true;
        FBP.fancySnow = true;
        FBP.fancySmoke = true;
        FBP.fancyFlame = true;
        FBP.waterPhysics = true;
        FBP.restOnFloor = true;
        if (write)
            write();
    }
}
