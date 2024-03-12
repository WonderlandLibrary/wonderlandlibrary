// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module;

import dev.tenacity.module.settings.impl.ColorSetting;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.settings.impl.StringSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import java.util.Iterator;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.utils.misc.Multithreading;
import java.util.concurrent.TimeUnit;
import dev.tenacity.module.impl.render.GlowESP;
import dev.tenacity.Tenacity;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.module.impl.render.NotificationsMod;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import java.util.Collection;
import java.util.Arrays;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.module.settings.impl.KeybindSetting;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.config.ConfigSetting;
import dev.tenacity.module.settings.Setting;
import java.util.concurrent.CopyOnWriteArrayList;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import dev.tenacity.utils.Utils;
import dev.tenacity.event.ListenerAdapter;

public class Module extends ListenerAdapter implements Utils
{
    @Expose
    @SerializedName("name")
    private final String name;
    private final String spacedName;
    private final String description;
    private final Category category;
    private final CopyOnWriteArrayList<Setting> settingsList;
    private String suffix;
    private String author;
    @Expose
    @SerializedName("toggled")
    protected boolean enabled;
    @Expose
    @SerializedName("settings")
    public ConfigSetting[] cfgSettings;
    private boolean expanded;
    private final Animation animation;
    public static int categoryCount;
    public static float allowedClickGuiHeight;
    private final KeybindSetting keybind;
    
    public Module(final String name, final String spacedName, final Category category, final String description) {
        this.settingsList = new CopyOnWriteArrayList<Setting>();
        this.author = "";
        this.animation = new DecelerateAnimation(250, 1.0).setDirection(Direction.BACKWARDS);
        this.keybind = new KeybindSetting(0);
        this.name = name;
        this.spacedName = spacedName;
        this.category = category;
        this.description = description;
        this.addSettings(this.keybind);
    }
    
    public boolean isInGame() {
        return Module.mc.theWorld != null && Module.mc.thePlayer != null;
    }
    
    public void addSettings(final Setting... settings) {
        this.settingsList.addAll(Arrays.asList(settings));
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setToggled(final boolean toggled) {
        this.enabled = toggled;
        if (toggled) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void toggleSilent() {
        this.enabled = !this.enabled;
        if (this.enabled) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void toggleSilent(final boolean toggled) {
        this.enabled = toggled;
        if (this.enabled) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void toggle() {
        this.toggleSilent();
        if (NotificationsMod.toggleNotifications.isEnabled()) {
            String titleToggle = "Module toggled";
            String descriptionToggleOn = this.getName() + " was §aenabled\r";
            String descriptionToggleOff = this.getName() + " was §cdisabled\r";
            final String mode = NotificationsMod.mode.getMode();
            switch (mode) {
                case "Default": {
                    if (NotificationsMod.onlyTitle.isEnabled()) {
                        titleToggle = this.getName() + " toggled";
                        break;
                    }
                    break;
                }
                case "SuicideX": {
                    if (this.isEnabled()) {
                        titleToggle = "Enabled Module " + this.getName() + ". PogO";
                    }
                    else {
                        titleToggle = "Disabled Module " + this.getName() + ". :/";
                    }
                    descriptionToggleOff = "";
                    descriptionToggleOn = "";
                    break;
                }
            }
            if (this.enabled) {
                NotificationManager.post(NotificationType.SUCCESS, titleToggle, descriptionToggleOn);
            }
            else {
                NotificationManager.post(NotificationType.DISABLE, titleToggle, descriptionToggleOff);
            }
        }
    }
    
    public boolean hasMode() {
        return this.suffix != null;
    }
    
    public void onEnable() {
        Tenacity.INSTANCE.getEventProtocol().register(this);
    }
    
    public void onDisable() {
        if (this instanceof GlowESP) {
            GlowESP.fadeIn.setDirection(Direction.BACKWARDS);
            Multithreading.schedule(() -> Tenacity.INSTANCE.getEventProtocol().unregister(this), 250L, TimeUnit.MILLISECONDS);
        }
        else {
            Tenacity.INSTANCE.getEventProtocol().unregister(this);
        }
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public void setKey(final int code) {
        this.keybind.setCode(code);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public String getName() {
        return this.name;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public String getDescription() {
        return this.description;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public boolean isEnabled() {
        return this.enabled;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public int getKeybindCode() {
        return this.keybind.getCode();
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public NumberSetting getNumberSetting(final String name) {
        for (final Setting setting : this.settingsList) {
            if (setting instanceof NumberSetting && setting.getName().equalsIgnoreCase(name)) {
                return (NumberSetting)setting;
            }
        }
        return null;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public BooleanSetting getBooleanSetting(final String name) {
        for (final Setting setting : this.settingsList) {
            if (setting instanceof BooleanSetting && setting.getName().equalsIgnoreCase(name)) {
                return (BooleanSetting)setting;
            }
        }
        return null;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public ModeSetting getModeSetting(final String name) {
        for (final Setting setting : this.settingsList) {
            if (setting instanceof ModeSetting && setting.getName().equalsIgnoreCase(name)) {
                return (ModeSetting)setting;
            }
        }
        return null;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public StringSetting getStringSetting(final String name) {
        for (final Setting setting : this.settingsList) {
            if (setting instanceof StringSetting && setting.getName().equalsIgnoreCase(name)) {
                return (StringSetting)setting;
            }
        }
        return null;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public MultipleBoolSetting getMultiBoolSetting(final String name) {
        for (final Setting setting : this.settingsList) {
            if (setting instanceof MultipleBoolSetting && setting.getName().equalsIgnoreCase(name)) {
                return (MultipleBoolSetting)setting;
            }
        }
        return null;
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public ColorSetting getColorSetting(final String name) {
        for (final Setting setting : this.settingsList) {
            if (setting instanceof ColorSetting && setting.getName().equalsIgnoreCase(name)) {
                return (ColorSetting)setting;
            }
        }
        return null;
    }
    
    public String getSpacedName() {
        return this.spacedName;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public CopyOnWriteArrayList<Setting> getSettingsList() {
        return this.settingsList;
    }
    
    public String getSuffix() {
        return this.suffix;
    }
    
    public String getAuthor() {
        return this.author;
    }
    
    public ConfigSetting[] getCfgSettings() {
        return this.cfgSettings;
    }
    
    public boolean isExpanded() {
        return this.expanded;
    }
    
    public Animation getAnimation() {
        return this.animation;
    }
    
    public KeybindSetting getKeybind() {
        return this.keybind;
    }
    
    public void setSuffix(final String suffix) {
        this.suffix = suffix;
    }
    
    public void setAuthor(final String author) {
        this.author = author;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public void setCfgSettings(final ConfigSetting[] cfgSettings) {
        this.cfgSettings = cfgSettings;
    }
    
    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }
    
    static {
        Module.allowedClickGuiHeight = 300.0f;
    }
}
