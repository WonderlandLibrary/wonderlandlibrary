package de.theBest.MythicX.utils;

import de.Hero.settings.Setting;
import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.modules.Module;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static final File DIRECTORY = new File(Minecraft.getMinecraft().mcDataDir, MythicX.name);
    public static final File DIRECTORY2 = new File(DIRECTORY, "configs");


    public void loadKeys() {
        try {
            final Path path1 = Paths.get(Minecraft.getMinecraft().mcDataDir + "/" + MythicX.name);
            if (!Files.exists(path1)) try {
                Files.createDirectory(path1);
            } catch (final IOException e) {
                //fail to create directory
                e.printStackTrace();
            }
            final Path path = Paths.get(Minecraft.getMinecraft().mcDataDir + "/" + MythicX.name + "/keys.txt");
            //if directory exists?
            if (!Files.exists(path)) try {
                Files.createFile(path);
            } catch (final IOException e) {
                //fail to create directory
                e.printStackTrace();
            }
            final File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + MythicX.name + "/keys.txt");
            if (!file.exists()) file.createNewFile();
            else if (file.exists()) {
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String readString;
                while ((readString = bufferedReader.readLine()) != null) {
                    final String[] split = readString.split(":");
                    final Module mod = MythicX.moduleManager.getModuleByName(split[0]);
                    if (mod != null) {
                        final int key = Integer.parseInt(split[1]);
                        mod.setKeyBind(key);
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void saveKeys() {
        final File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + MythicX.name + "/keys.txt");
        if (!file.exists()) try {
            file.createNewFile();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (final Module module : MythicX.moduleManager.modules) {
                final String moduleName = module.getName();
                final int moduleKey = module.getKeyBind(); // TODO: Keybinding system
                final String endstring = moduleName + ":" + moduleKey + "\n";
                writer.write(endstring);
                writer.close();
            }
            writer.close();
        } catch (final Exception ignored) {
        }
    }


    public void saveModules() {
        final File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + MythicX.name + "/modules.txt");
        if (!file.exists()) try {
            file.createNewFile();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (final Module module : MythicX.moduleManager.modules) {
                final String modName = module.getName();
                final String string = "TOGGLE " + modName + " " + module.isToggled() + "\n";
                writer.write(string);
                }
            for (final Module module : MythicX.moduleManager.modules) {
                for (Setting setting : MythicX.setmgr.getSettings()){
                    final String modName = module.getName();
                    if(setting.getParentMod().equals(module)){
                        if(setting.isCheck()) {
                            final String string2 = "SET " + modName + " " + setting.getName()+ " " + "boolean " +setting.getValBoolean() + "\n";
                            writer.write(string2);
                        }
                        if(setting.isSlider()) {
                            final String string2 = "SET " + modName + " " + setting.getName()+ " " + "double " +setting.getValDouble() + "\n";
                            writer.write(string2);
                        }
                        if(setting.isCombo()){
                            final String string2 = "SET " + modName + " " + setting.getName()+ " " + "combo " +setting.getValString() + "\n";
                            writer.write(string2);
                        }
                    }
                }
            }
        writer.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void loadModules() {
        try {
            final File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + MythicX.name + "/modules.txt");
            if (!file.exists()) file.createNewFile();
            else {
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String readString;
                while ((readString = bufferedReader.readLine()) != null) {
                    final String[] split = readString.split(" ");
                    final String isMod = split[0];
                    if(isMod.equals("TOGGLE")){
                        final Module mod = MythicX.moduleManager.getModuleByName(split[1]);
                        final boolean enabled = Boolean.parseBoolean(split[2]);
                        if (mod != null && enabled) mod.setToggled(true);
                        if (mod != null && !enabled) mod.setToggled(false);
                    }else {
                        final Module mod = MythicX.moduleManager.getModuleByName(split[1]);
                        final Setting setting = MythicX.setmgr.getSettingByName(split[2]);
                        final String type = split[3];
                        if(type.equals("boolean")){
                            final boolean state = Boolean.parseBoolean(split[4]);
                            setting.setValBoolean(state);
                        } else if (type.equals("double")) {
                            final Double state = Double.valueOf(split[4]);
                            setting.setValDouble(state);
                        }else if(type.equals("combo")){
                            final String state = split[4];
                            setting.setValString(state);
                        }
                    }

                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig(String name){
        final File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + MythicX.name + "/configs/"+name+".txt");
        if (!file.exists()) try {
            file.createNewFile();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (final Module module : MythicX.moduleManager.modules) {
                final String modName = module.getName();
                final String string = "TOGGLE " + modName + " " + module.isToggled() + "\n";
                writer.write(string);
            }
            for (final Module module : MythicX.moduleManager.modules) {
                for (Setting setting : MythicX.setmgr.getSettings()){
                    final String modName = module.getName();
                    if(setting.getParentMod().equals(module)){
                        if(setting.isCheck()) {
                            final String string2 = "SET " + modName + " " + setting.getName()+ " " + "boolean " +setting.getValBoolean() + "\n";
                            writer.write(string2);
                        }
                        if(setting.isSlider()) {
                            final String string2 = "SET " + modName + " " + setting.getName()+ " " + "double " +setting.getValDouble() + "\n";
                            writer.write(string2);
                        }
                        if(setting.isCombo()){
                            final String string2 = "SET " + modName + " " + setting.getName()+ " " + "combo " +setting.getValString() + "\n";
                            writer.write(string2);
                        }
                    }
                }
            }
            writer.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadConfig(String name){
        try {
            final File file = new File(Minecraft.getMinecraft().mcDataDir + "/" + MythicX.name + "/configs/" + name+".txt");
            if (!file.exists()) file.createNewFile();
            else {
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String readString;
                while ((readString = bufferedReader.readLine()) != null) {
                    final String[] split = readString.split(" ");
                    final String isMod = split[0];
                    if(isMod.equals("TOGGLE")){
                        final Module mod = MythicX.moduleManager.getModuleByName(split[1]);
                        final boolean enabled = Boolean.parseBoolean(split[2]);
                        if (mod != null) mod.setToggled(enabled);
                    }else {
                        final Module mod = MythicX.moduleManager.getModuleByName(split[1]);
                        final Setting setting = MythicX.setmgr.getSettingByName(split[2]);
                        final String type = split[3];
                        if(type.equals("boolean")){
                            final boolean state = Boolean.parseBoolean(split[4]);
                            setting.setValBoolean(state);
                        } else if (type.equals("double")) {
                            final Double state = Double.valueOf(split[4]);
                            setting.setValDouble(state);
                        }else if(type.equals("combo")){
                            final String state = split[4];
                            setting.setValString(state);
                        }
                    }

                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }


    public void createFolder() {

        if (!DIRECTORY.exists() || !DIRECTORY.isDirectory()) {
            DIRECTORY.mkdir();
            DIRECTORY.mkdirs();
        }
        if (!DIRECTORY2.exists() || !DIRECTORY2.isDirectory()) {
            DIRECTORY2.mkdir();
            DIRECTORY2.mkdirs();
        }
    }
}
