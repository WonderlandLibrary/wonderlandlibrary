/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.Expose
 */
package me.arithmo.module;

import com.google.gson.annotations.Expose;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import me.arithmo.Client;
import me.arithmo.event.EventListener;
import me.arithmo.event.EventSystem;
import me.arithmo.management.Saveable;
import me.arithmo.management.SubFolder;
import me.arithmo.management.keybinding.Bindable;
import me.arithmo.management.keybinding.KeyHandler;
import me.arithmo.management.keybinding.KeyMask;
import me.arithmo.management.keybinding.Keybind;
import me.arithmo.module.Toggleable;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Options;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.FileUtils;
import me.arithmo.util.StringConversions;
import me.arithmo.util.render.Colors;
import net.minecraft.client.Minecraft;

public abstract class Module
extends Saveable
implements EventListener,
Bindable,
Toggleable {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    @Expose
    protected final ModuleData data;
    @Expose
    protected final SettingsMap settings = new SettingsMap();
    private Keybind keybind;
    private boolean enabled;
    private String suffix;
    private int color;
    private static final File MODULE_DIR = FileUtils.getConfigFile("Mods");
    private static final File SETTINGS_DIR = FileUtils.getConfigFile("Sets");

    public int getColor() {
        return this.color;
    }

    public Module(ModuleData data) {
        this.data = data;
        this.setFolderType(SubFolder.Module);
        this.setKeybind(new Keybind(this, data.key, data.mask));
        Module.loadStatus();
        this.color = Colors.getColor((int)(255.0 * Math.random()), (int)(255.0 * Math.random()), (int)(255.0 * Math.random()), 255);
    }

    public static void saveStatus() {
        ArrayList<String> fileContent = new ArrayList<String>();
        for (Module module : Client.getModuleManager().getArray()) {
            String displayName = module.getName();
            String enabled = Boolean.toString(module.isEnabled());
            int bind = module.data.getKey();
            fileContent.add(String.format("%s:%s:%s", displayName, enabled, bind));
        }
        FileUtils.write(MODULE_DIR, fileContent, true);
    }

    public static void saveSettings() {
        ArrayList<String> fileContent = new ArrayList<String>();
        for (Module module : Client.getModuleManager().getArray()) {
            for (Setting setting : module.getSettings().values()) {
                String displayName;
                String settingName;
                String settingValue;
                if (!(setting.getValue() instanceof Options)) {
                    displayName = module.getName();
                    settingName = setting.getName();
                    settingValue = setting.getValue().toString();
                    fileContent.add(String.format("%s:%s:%s", displayName, settingName, settingValue));
                    continue;
                }
                displayName = module.getName();
                settingName = setting.getName();
                settingValue = ((Options)setting.getValue()).getSelected();
                fileContent.add(String.format("%s:%s:%s", displayName, settingName, settingValue));
            }
        }
        FileUtils.write(SETTINGS_DIR, fileContent, true);
    }

    public static void loadStatus() {
        try {
            List<String> fileContent = FileUtils.read(MODULE_DIR);
            for (String line : fileContent) {
                String[] split = line.split(":");
                String displayName = split[0];
                for (Module module : Client.getModuleManager().getArray()) {
                    if (!module.getName().equalsIgnoreCase(displayName)) continue;
                    String strEnabled = split[1];
                    boolean enabled = Boolean.parseBoolean(strEnabled);
                    String key = split[2];
                    module.setKeybind(new Keybind(module, Integer.parseInt(key)));
                    if (!enabled || module.isEnabled()) continue;
                    module.enabled = true;
                    EventSystem.register(module);
                    module.onEnable();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadSettings() {
        try {
            List<String> fileContent = FileUtils.read(SETTINGS_DIR);
            for (String line : fileContent) {
                String[] split = line.split(":");
                block3 : for (Module module : Client.getModuleManager().getArray()) {
                    if (!module.getName().equalsIgnoreCase(split[0])) continue;
                    Setting setting = Module.getSetting(module.getSettings(), split[1]);
                    String settingValue = split[2];
                    if (setting == null) continue;
                    if (setting.getValue() instanceof Number) {
                        Object newValue = StringConversions.castNumber(settingValue, setting.getValue());
                        if (newValue == null) continue;
                        setting.setValue(newValue);
                        continue;
                    }
                    if (setting.getValue().getClass().equals(String.class)) {
                        String parsed = settingValue.toString().replaceAll("_", " ");
                        setting.setValue(parsed);
                        continue;
                    }
                    if (setting.getValue().getClass().equals(Boolean.class)) {
                        setting.setValue(Boolean.parseBoolean(settingValue));
                        continue;
                    }
                    if (!setting.getValue().getClass().equals(Options.class)) continue;
                    Options dank = (Options)setting.getValue();
                    for (String str : dank.getOptions()) {
                        if (!str.equalsIgnoreCase(settingValue)) continue;
                        dank.setSelected(settingValue);
                        continue block3;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Setting getSetting(SettingsMap map, String settingText) {
        if (map.containsKey(settingText = settingText.toUpperCase())) {
            return (Setting)map.get(settingText);
        }
        for (String key : map.keySet()) {
            if (!key.startsWith(settingText)) continue;
            return (Setting)map.get(key);
        }
        return null;
    }

    @Override
    public void toggle() {
        boolean bl = this.enabled = !this.enabled;
        if (Client.getModuleManager().isSetup()) {
            Module.saveStatus();
        }
        if (this.enabled) {
            EventSystem.register(this);
            this.onEnable();
        } else {
            EventSystem.unregister(this);
            this.onDisable();
        }
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onBindPress() {
        this.toggle();
    }

    @Override
    public void onBindRelease() {
    }

    @Override
    public void setKeybind(Keybind newBind) {
        boolean sameMask;
        if (newBind == null) {
            return;
        }
        if (this.keybind == null) {
            this.keybind = newBind;
            KeyHandler.register(this.keybind);
            return;
        }
        boolean sameKey = newBind.getKeyInt() == this.keybind.getKeyInt();
        boolean bl = sameMask = newBind.getMask() == this.keybind.getMask();
        if (sameKey && !sameMask) {
            KeyHandler.update(this, this.keybind, newBind);
        } else if (!sameKey) {
            if (KeyHandler.keyHasBinds(this.keybind.getKeyInt())) {
                KeyHandler.unregister(this, this.keybind);
            }
            KeyHandler.register(newBind);
        }
        this.keybind.update(newBind);
        this.data.key = this.keybind.getKeyInt();
        this.data.mask = this.keybind.getMask();
    }

    public static int getColor(ModuleData.Type type) {
        int color = -1;
        switch (type) {
            case Combat: {
                color = Colors.getColor(135, 39, 39);
                break;
            }
            case Player: {
                color = Colors.getColor(90, 90, 90);
                break;
            }
            case Movement: {
                color = Colors.getColor(161, 180, 196);
                break;
            }
            case Visuals: {
                color = Colors.getColor(27, 198, 190);
                break;
            }
            case Other: {
                color = Colors.getColor(90, 90, 90);
                break;
            }
        }
        return color;
    }

    public Keybind getKeybind() {
        return this.keybind;
    }

    public boolean addSetting(String key, Setting setting) {
        if (this.settings.containsKey(key)) {
            return false;
        }
        this.settings.put(key, setting);
        return true;
    }

    public Setting getSetting(String key) {
        return (Setting)this.settings.get(key);
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public SettingsMap getSettings() {
        return this.settings;
    }

    public String getName() {
        return this.data.name;
    }

    public String getDescription() {
        return this.data.description;
    }

    public ModuleData.Type getType() {
        return this.data.type;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}

