package koks.manager.file.impl;

import koks.Koks;
import koks.api.settings.Setting;
import koks.manager.file.Files;
import koks.manager.file.IFile;
import koks.manager.module.Module;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author kroko
 * @created on 21.10.2020 : 05:34
 */
@IFile(name = "settings")
public class Settings extends Files {

    @Override
    public void readFile(BufferedReader bufferedReader) throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] args = line.split(":");
            if (args[0].equalsIgnoreCase("clientcolor")) {
                Koks.getKoks().clientColor = new Color(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            } else {
                Module module = Koks.getKoks().moduleManager.getModule(args[0]);
                Setting setting = Koks.getKoks().settingsManager.getSetting(module, args[1]);
                if (setting != null) {
                    switch (setting.getType()) {
                        case CHECKBOX:
                            setting.setToggled(Boolean.parseBoolean(args[2]));
                            break;
                        case COMBOBOX:
                            setting.setCurrentMode(args[2]);
                            break;
                        case SLIDER:
                            setting.setCurrentValue(Float.parseFloat(args[2]));
                            break;
                        case KEY:
                            setting.setKey(Integer.parseInt(args[2]));
                            break;
                        case TYPE:
                            setting.setTyped(args[2]);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void writeFile(FileWriter fileWriter) throws IOException {
        fileWriter.write("clientcolor:" + Koks.getKoks().clientColor.getRed() + ":" + Koks.getKoks().clientColor.getGreen() + ":" +  Koks.getKoks().clientColor.getBlue() + "\n");
        for (Module module : Koks.getKoks().moduleManager.getModules()) {
            if (module != null) {
                for (Setting setting : Koks.getKoks().settingsManager.getSettings()) {
                    if (setting.getModule().equals(module)) {
                        Object arg = "";
                        switch (setting.getType()) {
                            case CHECKBOX:
                                arg = setting.isToggled();
                                break;
                            case COMBOBOX:
                                arg = setting.getCurrentMode();
                                break;
                            case SLIDER:
                                arg = setting.getCurrentValue();
                                break;
                            case KEY:
                                arg = setting.getKey();
                                break;
                            case TYPE:
                                arg = setting.getTyped();
                                break;
                        }
                        fileWriter.write(module.getName() + ":" + setting.getName() + ":" + arg + "\n");
                    }
                }
            }
        }
    }
}
