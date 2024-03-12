// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.settings.impl;

import java.util.Iterator;
import java.util.Collection;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import dev.tenacity.module.settings.Setting;

public class MultipleBoolSetting extends Setting
{
    private final Map<String, BooleanSetting> boolSettings;
    
    public MultipleBoolSetting(final String name, final String... booleanSettingNames) {
        this.name = name;
        this.boolSettings = new HashMap<String, BooleanSetting>();
        final BooleanSetting booleanSetting;
        Arrays.stream(booleanSettingNames).forEach(boolName -> booleanSetting = this.boolSettings.put(boolName.toLowerCase(), new BooleanSetting(boolName, false)));
    }
    
    public MultipleBoolSetting(final String name, final BooleanSetting... booleanSettings) {
        this.name = name;
        this.boolSettings = new HashMap<String, BooleanSetting>();
        final BooleanSetting booleanSetting2;
        Arrays.stream(booleanSettings).forEach(booleanSetting -> booleanSetting2 = this.boolSettings.put(booleanSetting.name.toLowerCase(), booleanSetting));
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public BooleanSetting getSetting(final String settingName) {
        return this.boolSettings.computeIfAbsent(settingName.toLowerCase(), k -> null);
    }
    
    @Exclude({ Strategy.NAME_REMAPPING })
    public boolean isEnabled(final String settingName) {
        return this.boolSettings.get(settingName.toLowerCase()).isEnabled();
    }
    
    public Collection<BooleanSetting> getBoolSettings() {
        return this.boolSettings.values();
    }
    
    @Override
    public HashMap<String, Boolean> getConfigValue() {
        final HashMap<String, Boolean> booleans = new HashMap<String, Boolean>();
        for (final BooleanSetting booleanSetting : this.boolSettings.values()) {
            booleans.put(booleanSetting.name, booleanSetting.isEnabled());
        }
        return booleans;
    }
}
