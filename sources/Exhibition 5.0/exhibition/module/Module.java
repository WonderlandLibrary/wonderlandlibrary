// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module;

import exhibition.management.keybinding.KeyHandler;
import java.util.HashMap;
import exhibition.util.render.Colors;
import exhibition.util.StringConversions;
import exhibition.event.EventSystem;
import java.util.Iterator;
import exhibition.module.data.Options;
import exhibition.module.data.Setting;
import java.util.List;
import exhibition.util.FileUtils;
import exhibition.Client;
import java.util.ArrayList;
import java.io.File;
import exhibition.management.keybinding.Keybind;
import exhibition.module.data.SettingsMap;
import com.google.gson.annotations.Expose;
import exhibition.module.data.ModuleData;
import net.minecraft.client.Minecraft;
import exhibition.management.keybinding.Bindable;
import exhibition.event.EventListener;
import exhibition.management.Saveable;

public abstract class Module extends Saveable implements EventListener, Bindable, Toggleable
{
    protected static final Minecraft mc;
    @Expose
    protected final ModuleData data;
    @Expose
    protected final SettingsMap settings;
    private Keybind keybind;
    private boolean enabled;
    private String suffix;
    private int color;
    private static final File MODULE_DIR;
    private static final File SETTINGS_DIR;
    
    public int getColor() {
        return this.color;
    }
    
    public Module(final ModuleData data) {
        this.settings = new SettingsMap();
        this.data = data;
        this.setKeybind(new Keybind(this, data.key, data.mask));
    }
    
    public static void saveStatus() {
        final List<String> fileContent = new ArrayList<String>();
        for (final Module module : Client.getModuleManager().getArray()) {
            final String displayName = module.getName();
            final String enabled = Boolean.toString(module.isEnabled());
            final int bind = module.data.getKey();
            fileContent.add(String.format("%s:%s:%s", displayName, enabled, bind));
        }
        FileUtils.write(Module.MODULE_DIR, fileContent, true);
    }
    
    public static void saveSettings() {
        final List<String> fileContent = new ArrayList<String>();
        for (final Module module : Client.getModuleManager().getArray()) {
            for (final Setting setting : ((HashMap<K, Setting>)module.getSettings()).values()) {
                if (!(setting.getValue() instanceof Options)) {
                    final String displayName = module.getName();
                    final String settingName = setting.getName();
                    final String settingValue = setting.getValue().toString();
                    fileContent.add(String.format("%s:%s:%s", displayName, settingName, settingValue));
                }
                else {
                    final String displayName = module.getName();
                    final String settingName = setting.getName();
                    final String settingValue = setting.getValue().getSelected();
                    fileContent.add(String.format("%s:%s:%s", displayName, settingName, settingValue));
                }
            }
        }
        FileUtils.write(Module.SETTINGS_DIR, fileContent, true);
    }
    
    public static void loadStatus() {
        try {
            final List<String> fileContent = FileUtils.read(Module.MODULE_DIR);
            for (final String line : fileContent) {
                final String[] split = line.split(":");
                final String displayName = split[0];
                for (final Module module : Client.getModuleManager().getArray()) {
                    if (module.getName().equalsIgnoreCase(displayName)) {
                        final String strEnabled = split[1];
                        final boolean enabled = Boolean.parseBoolean(strEnabled);
                        final String key = split[2];
                        module.setKeybind(new Keybind(module, Integer.parseInt(key)));
                        if (enabled && !module.isEnabled()) {
                            module.enabled = true;
                            EventSystem.register(module);
                            module.onEnable();
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadSettings() {
        try {
            final List<String> fileContent = FileUtils.read(Module.SETTINGS_DIR);
            for (final String line : fileContent) {
                final String[] split = line.split(":");
                for (final Module module : Client.getModuleManager().getArray()) {
                    if (module.getName().equalsIgnoreCase(split[0])) {
                        final Setting setting = getSetting(module.getSettings(), split[1]);
                        final String settingValue = split[2];
                        if (setting != null) {
                            if (setting.getValue() instanceof Number) {
                                final Object newValue = StringConversions.castNumber(settingValue, setting.getValue());
                                if (newValue != null) {
                                    setting.setValue(newValue);
                                }
                            }
                            else if (setting.getValue().getClass().equals(String.class)) {
                                final String parsed = settingValue.toString().replaceAll("_", " ");
                                setting.setValue(parsed);
                            }
                            else if (setting.getValue().getClass().equals(Boolean.class)) {
                                setting.setValue(Boolean.parseBoolean(settingValue));
                            }
                            else if (setting.getValue().getClass().equals(Options.class)) {
                                setting.getValue().setSelected(settingValue);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Setting getSetting(final SettingsMap map, String settingText) {
        settingText = settingText.toUpperCase();
        if (map.containsKey(settingText)) {
            return ((HashMap<K, Setting>)map).get(settingText);
        }
        for (final String key : ((HashMap<String, V>)map).keySet()) {
            if (key.startsWith(settingText)) {
                return ((HashMap<K, Setting>)map).get(key);
            }
        }
        return null;
    }
    
    @Override
    public void toggle() {
        this.enabled = !this.enabled;
        if (this.enabled) {
            saveStatus();
            this.color = Colors.getColor((int)(255.0 * Math.random()), (int)(255.0 * Math.random()), (int)(255.0 * Math.random()), 255);
            EventSystem.register(this);
            this.onEnable();
        }
        else {
            saveStatus();
            EventSystem.unregister(this);
            this.onDisable();
        }
    }
    
    private void loadData() {
        final Module module = (Module)this.load();
        if (module != null) {
            if (module.data != null) {
                this.data.update(module.data);
            }
            if (module.settings != null) {
                this.settings.update(module.settings);
            }
            this.setKeybind(new Keybind(this, this.data.key, this.data.mask));
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
    public void setKeybind(final Keybind newBind) {
        if (newBind == null) {
            return;
        }
        if (this.keybind == null) {
            KeyHandler.register(this.keybind = newBind);
            return;
        }
        final boolean sameKey = newBind.getKeyInt() == this.keybind.getKeyInt();
        final boolean sameMask = newBind.getMask() == this.keybind.getMask();
        if (sameKey && !sameMask) {
            KeyHandler.update(this, this.keybind, newBind);
        }
        else if (!sameKey) {
            if (KeyHandler.keyHasBinds(this.keybind.getKeyInt())) {
                KeyHandler.unregister(this, this.keybind);
            }
            KeyHandler.register(newBind);
        }
        this.keybind.update(newBind);
        this.data.key = this.keybind.getKeyInt();
        this.data.mask = this.keybind.getMask();
    }
    
    public static int getColor(final ModuleData.Type type) {
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
    
    public boolean addSetting(final String key, final Setting setting) {
        if (this.settings.containsKey(key)) {
            return false;
        }
        this.settings.put(key, setting);
        return true;
    }
    
    public Setting getSetting(final String key) {
        return ((HashMap<K, Setting>)this.settings).get(key);
    }
    
    public String getSuffix() {
        return this.suffix;
    }
    
    public void setSuffix(final String suffix) {
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
    
    static {
        mc = Minecraft.getMinecraft();
        MODULE_DIR = FileUtils.getConfigFile("Mods");
        SETTINGS_DIR = FileUtils.getConfigFile("Sets");
    }
}
