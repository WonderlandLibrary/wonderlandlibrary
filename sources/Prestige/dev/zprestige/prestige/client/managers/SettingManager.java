package dev.zprestige.prestige.client.managers;

import dev.zprestige.prestige.client.setting.Setting;
import dev.zprestige.prestige.client.setting.impl.*;

import java.awt.*;
import java.util.ArrayList;

public class SettingManager {
    public ArrayList<Setting<?>> settings = new ArrayList<>();

    public IntSetting setting(String name, int value, int min, int max) {
        IntSetting setting = new IntSetting(name, value, min, max);
        settings.add(setting);
        return setting;
    }

    public DragSetting setting(String name, float first, float second, float min, float max) {
        DragSetting setting = new DragSetting(name, first, second, min, max);
        settings.add(setting);
        return setting;
    }

    public ModeSetting setting(String name, String value, String[] values) {
        ModeSetting setting = new ModeSetting(name, value, values);
        settings.add(setting);
        return setting;
    }

    public ColorSetting setting(String name, Color value) {
        ColorSetting setting = new ColorSetting(name, value);
        settings.add(setting);
        return setting;
    }

    public BindSetting setting(String name, int key) {
        BindSetting setting = new BindSetting(name, key);
        settings.add(setting);
        return setting;
    }

    public StringSetting setting(String name, String value, String placeHolder) {
        StringSetting setting = new StringSetting(name, value, placeHolder);
        settings.add(setting);
        return setting;
    }

    public BooleanSetting setting(String name, boolean value) {
        BooleanSetting setting = new BooleanSetting(name, value);
        settings.add(setting);
        return setting;
    }

    public FloatSetting setting(String name, float value, float min, float max) {
        FloatSetting setting = new FloatSetting(name, value, min, max);
        settings.add(setting);
        return setting;
    }

    public MultipleSetting setting(String name, String[] options, Boolean[] values) {
        MultipleSetting setting = new MultipleSetting(name, options, values);
        settings.add(setting);
        return setting;
    }

    public ArrayList<Setting<?>> getSettings() {
        return settings;
    }
}