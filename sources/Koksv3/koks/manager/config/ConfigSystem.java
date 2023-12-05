package koks.manager.config;

import koks.Koks;
import koks.manager.module.Module;
import koks.api.settings.Setting;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 14:36
 */
public class ConfigSystem {

    public void saveConfig(String config) {
        try {
            File dir = new File(Minecraft.getMinecraft().mcDataDir + "/Koks/Configs");
            if (!dir.exists()) dir.mkdirs();
            File file = new File(Minecraft.getMinecraft().mcDataDir + "/Koks/Configs/" + config + ".koks");
            if (!file.exists()) file.createNewFile();
            FileWriter fileWriter = new FileWriter(Minecraft.getMinecraft().mcDataDir + "/Koks/Configs/" + config + ".koks");
            for (Module module : Koks.getKoks().moduleManager.getModules()) {
                if (module.getCategory() != Module.Category.GUI && module.getCategory() != Module.Category.RENDER && module.getCategory() != Module.Category.DEBUG) {
                    fileWriter.write("Module:" + module.getName() + ":" + module.isToggled() + ":" + module.isBypass() + "\n");
                }
            }
            for (Setting setting : Koks.getKoks().settingsManager.getSettings()) {
                String arguments = "";
                switch (setting.getType()) {
                    case SLIDER:
                        arguments = setting.getCurrentValue() + "";
                        break;
                    case CHECKBOX:
                        arguments = setting.isToggled() + "";
                        break;
                    case COMBOBOX:
                        arguments = setting.getCurrentMode();
                        break;
                }
                fileWriter.write("Setting:" + setting.getModule().getName() + ":" + setting.getName() + ":" + arguments + "\n");
            }
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadConfigOnline(String config) {

        for (Module module : Koks.getKoks().moduleManager.getModules()) {
            if (module.getCategory() != Module.Category.RENDER && module.getCategory() != Module.Category.GUI) {
                module.setToggled(false);
                module.setBypass(false);
            }
        }

        try {
            URL url = new URL(config);
            Scanner scanner = new Scanner(url.openStream(), "UTF-8");
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String[] args = line.split(":");
                switch (args[0]) {
                    case "Module":
                        Module module = Koks.getKoks().moduleManager.getModule(args[1]);
                        if (module != null) {
                            module.setToggled(Boolean.parseBoolean(args[2]));
                            module.setBypass(Boolean.parseBoolean(args[3]));
                        }
                        break;
                    case "Setting":
                        module = Koks.getKoks().moduleManager.getModule(args[1]);
                        Setting setting = Koks.getKoks().settingsManager.getSetting(module, args[2]);
                        if (setting != null && module != null) {
                            if (module.getCategory() != Module.Category.GUI && module.getCategory() != Module.Category.RENDER && module.getCategory() != Module.Category.DEBUG) {
                                switch (setting.getType()) {
                                    case SLIDER:
                                        setting.setCurrentValue(Float.parseFloat(args[3]));
                                        break;
                                    case CHECKBOX:
                                        setting.setToggled(Boolean.parseBoolean(args[3]));
                                        break;
                                    case COMBOBOX:
                                        setting.setCurrentMode(args[3]);
                                        break;
                                }
                            }
                        }
                        break;
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig(String config) {

        for (Module module : Koks.getKoks().moduleManager.getModules()) {
            if (module.getCategory() != Module.Category.RENDER && module.getCategory() != Module.Category.GUI) {
                module.setToggled(false);
                module.setBypass(false);
            }
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Minecraft.getMinecraft().mcDataDir + "/Koks/Configs/" + config + ".koks"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] args = line.split(":");
                switch (args[0]) {
                    case "Module":
                        Module module = Koks.getKoks().moduleManager.getModule(args[1]);
                        if (module != null) {
                            module.setToggled(Boolean.parseBoolean(args[2]));
                            module.setBypass(Boolean.parseBoolean(args[3]));
                        }
                        break;
                    case "Setting":
                        module = Koks.getKoks().moduleManager.getModule(args[1]);
                        Setting setting = Koks.getKoks().settingsManager.getSetting(module, args[2]);
                        if (setting != null && module != null) {
                            if (module.getCategory() != Module.Category.GUI && module.getCategory() != Module.Category.RENDER && module.getCategory() != Module.Category.DEBUG) {
                                switch (setting.getType()) {
                                    case SLIDER:
                                        setting.setCurrentValue(Float.parseFloat(args[3]));
                                        break;
                                    case CHECKBOX:
                                        setting.setToggled(Boolean.parseBoolean(args[3]));
                                        break;
                                    case COMBOBOX:
                                        setting.setCurrentMode(args[3]);
                                        break;
                                }
                            }
                        }
                        break;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
