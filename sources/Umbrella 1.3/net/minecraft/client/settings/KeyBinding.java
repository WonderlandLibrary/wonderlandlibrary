/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 */
package net.minecraft.client.settings;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IntHashMap;

public class KeyBinding
implements Comparable {
    private static final List keybindArray = Lists.newArrayList();
    private static final IntHashMap hash = new IntHashMap();
    private static final Set keybindSet = Sets.newHashSet();
    private final String keyDescription;
    private final int keyCodeDefault;
    private final String keyCategory;
    private int keyCode;
    public boolean pressed;
    private int pressTime;
    private static final String __OBFID = "CL_00000628";

    public static void onTick(int keyCode) {
        KeyBinding var1;
        if (keyCode != 0 && (var1 = (KeyBinding)hash.lookup(keyCode)) != null) {
            ++var1.pressTime;
        }
    }

    public static void setKeyBindState(int keyCode, boolean pressed) {
        KeyBinding var2;
        if (keyCode != 0 && (var2 = (KeyBinding)hash.lookup(keyCode)) != null) {
            var2.pressed = pressed;
        }
    }

    public void setKeyPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public static void unPressAllKeys() {
        for (KeyBinding var1 : keybindArray) {
            var1.unpressKey();
        }
    }

    public static void resetKeyBindingArrayAndHash() {
        hash.clearMap();
        for (KeyBinding var1 : keybindArray) {
            hash.addKey(var1.keyCode, var1);
        }
    }

    public static Set getKeybinds() {
        return keybindSet;
    }

    public KeyBinding(String description, int keyCode, String category) {
        this.keyDescription = description;
        this.keyCode = keyCode;
        this.keyCodeDefault = keyCode;
        this.keyCategory = category;
        keybindArray.add(this);
        hash.addKey(keyCode, this);
        keybindSet.add(category);
    }

    public boolean getIsKeyPressed() {
        return this.pressed;
    }

    public String getKeyCategory() {
        return this.keyCategory;
    }

    public boolean isPressed() {
        if (this.pressTime == 0) {
            return false;
        }
        --this.pressTime;
        return true;
    }

    private void unpressKey() {
        this.pressTime = 0;
        this.pressed = false;
    }

    public String getKeyDescription() {
        return this.keyDescription;
    }

    public int getKeyCodeDefault() {
        return this.keyCodeDefault;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public int compareTo(KeyBinding p_compareTo_1_) {
        int var2 = I18n.format(this.keyCategory, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyCategory, new Object[0]));
        if (var2 == 0) {
            var2 = I18n.format(this.keyDescription, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyDescription, new Object[0]));
        }
        return var2;
    }

    public int compareTo(Object p_compareTo_1_) {
        return this.compareTo((KeyBinding)p_compareTo_1_);
    }
}

