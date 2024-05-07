package com.masterof13fps.manager.settingsmanager;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Made by HeroCode it's free to use but you have to credit me
 *
 * @author HeroCode
 */
public class SettingsManager {

    private ArrayList<Setting> settings;
    private File settingsFile;

    public SettingsManager() {
        this.settings = new ArrayList<>();

        try {
            settingsFile = new File(Client.main().getClientDir() + "/config.txt");
            if (settingsFile.createNewFile()) {
                System.out.println("File created: " + settingsFile.getName());
            } else {
                System.out.println("File \"config.txt\" already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public void addSetting(Setting in) {
        this.settings.add(in);
    }

    public ArrayList<Setting> getSettings() {
        return this.settings;
    }

    public ArrayList<Setting> getSettingsByMod(Module mod) {
        ArrayList<Setting> out = new ArrayList<>();
        for (Setting s : getSettings()) {
            if (s.getParentMod() != null) {
                if (s.getParentMod().equals(mod)) {
                    out.add(s);
                }
            } else
                return null;
        }
        if (out.isEmpty()) {
            return null;
        }
        return out;
    }

    public Setting settingByName(String settingName, Module settingModule) {
        for (Setting set : getSettings()) {
            if (set.getFullName().equalsIgnoreCase((settingModule == null ? "global" : settingModule.name()) + "_" + settingName)) {
                return set;
            }
        }
        System.err.println("[" + Client.main().getClientName() + "] Error Setting NOT found: '" + settingName + "'!");
        return null;
    }


    public void saveSettings() {
        List<String> formattedSettings = new ArrayList<String>();
        for (final Setting set : Client.main().setMgr().getSettings()) {
            String yeet = set.getParentMod() != null ? set.getParentMod().name() : "global";
            if (set.isSlider()) {
                formattedSettings.add(yeet + ":" + set.getName() + ":" + set.getCurrentValue());
            }
            if (set.isCheck()) {
                formattedSettings.add(yeet + ":" + set.getName() + ":" + set.isToggled());
            }
            if (set.isCombo()) {
                formattedSettings.add(yeet + ":" + set.getName() + ":" + set.getCurrentMode());
            }
        }
        FileUtils.saveFile(settingsFile, formattedSettings);
    }

    public void loadSettings() {
        try {
            Objects.requireNonNull(FileUtils.loadFile(settingsFile)).forEach(line -> {
                final String[] arguments = line.split(":");
                if (arguments.length == 3) {
                    Module parent = arguments[0].equalsIgnoreCase("global") ? null : Client.main().modMgr().getByName(arguments[0]);
                    Setting set = settingByName(arguments[1], parent);

                    if (set != null) {
                        if (set.isSlider())
                            set.setNum(Double.parseDouble(arguments[2]));
                        if (set.isCheck())
                            set.setBool(Boolean.parseBoolean(arguments[2]));
                        if (set.isCombo())
                            set.setMode(arguments[2]);
                    }
                }
            });
        } catch (Exception ignored) {
        }
    }

    public File getSettingsFile() {
        return settingsFile;
    }

}