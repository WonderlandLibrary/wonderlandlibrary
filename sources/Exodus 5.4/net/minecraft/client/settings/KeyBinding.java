/*
 * Decompiled with CFR 0.152.
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
implements Comparable<KeyBinding> {
    private static final List<KeyBinding> keybindArray = Lists.newArrayList();
    private final String keyDescription;
    private static final IntHashMap<KeyBinding> hash = new IntHashMap();
    private final int keyCodeDefault;
    public boolean pressed;
    private int pressTime;
    private int keyCode;
    private static final Set<String> keybindSet = Sets.newHashSet();
    private final String keyCategory;

    private void unpressKey() {
        this.pressTime = 0;
        this.pressed = false;
    }

    @Override
    public int compareTo(KeyBinding keyBinding) {
        int n = I18n.format(this.keyCategory, new Object[0]).compareTo(I18n.format(keyBinding.keyCategory, new Object[0]));
        if (n == 0) {
            n = I18n.format(this.keyDescription, new Object[0]).compareTo(I18n.format(keyBinding.keyDescription, new Object[0]));
        }
        return n;
    }

    public String getKeyCategory() {
        return this.keyCategory;
    }

    public boolean isKeyDown() {
        return this.pressed;
    }

    public void setKeyCode(int n) {
        this.keyCode = n;
    }

    public static void setKeyBindState(int n, boolean bl) {
        KeyBinding keyBinding;
        if (n != 0 && (keyBinding = hash.lookup(n)) != null) {
            keyBinding.pressed = bl;
        }
    }

    public int getKeyCodeDefault() {
        return this.keyCodeDefault;
    }

    public static void onTick(int n) {
        KeyBinding keyBinding;
        if (n != 0 && (keyBinding = hash.lookup(n)) != null) {
            ++keyBinding.pressTime;
        }
    }

    public static Set<String> getKeybinds() {
        return keybindSet;
    }

    public boolean isPressed() {
        if (this.pressTime == 0) {
            return false;
        }
        --this.pressTime;
        return true;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public static void resetKeyBindingArrayAndHash() {
        hash.clearMap();
        for (KeyBinding keyBinding : keybindArray) {
            hash.addKey(keyBinding.keyCode, keyBinding);
        }
    }

    public static void unPressAllKeys() {
        for (KeyBinding keyBinding : keybindArray) {
            keyBinding.unpressKey();
        }
    }

    public String getKeyDescription() {
        return this.keyDescription;
    }

    public KeyBinding(String string, int n, String string2) {
        this.keyDescription = string;
        this.keyCode = n;
        this.keyCodeDefault = n;
        this.keyCategory = string2;
        keybindArray.add(this);
        hash.addKey(n, this);
        keybindSet.add(string2);
    }
}

