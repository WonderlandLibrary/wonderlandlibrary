/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package com.wallhacks.losebypass.systems.setting.settings;

import com.wallhacks.losebypass.systems.SettingsHolder;
import com.wallhacks.losebypass.systems.setting.Setting;
import com.wallhacks.losebypass.utils.MouseUtil;
import com.wallhacks.losebypass.utils.StringUtil;
import java.util.function.Predicate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeySetting
extends Setting<Integer> {
    public KeySetting(String name, SettingsHolder settingsHolder, int key) {
        super(key, name, settingsHolder);
    }

    public KeySetting visibility(Predicate<Integer> visible) {
        this.visible = visible;
        return this;
    }

    public KeySetting description(String description) {
        super.setDescription(description);
        return this;
    }

    public int getKey() {
        return (Integer)this.getValue();
    }

    public String getKeyName() {
        return StringUtil.getNameForKey(this.getKey());
    }

    public boolean isDown() {
        if ((Integer)this.getValue() > -1) {
            return Keyboard.isKeyDown((int)((Integer)this.getValue()));
        }
        if ((Integer)this.getValue() >= -1) return false;
        return Mouse.isButtonDown((int)MouseUtil.convertToMouse((Integer)this.getValue()));
    }

    public void setKey(int key) {
        this.setValue(key);
    }

    @Override
    public boolean setValueString(String value) {
        this.setValue(Integer.parseInt(value));
        return true;
    }
}

