/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.settings;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IntHashMap;

public class KeyBinding
implements Comparable<KeyBinding> {
    private static final List<KeyBinding> keybindArray = Lists.newArrayList();
    private static final IntHashMap<KeyBinding> hash = new IntHashMap();
    private static final Set<String> keybindSet = Sets.newHashSet();
    private final String keyDescription;
    private final int keyCodeDefault;
    private final String keyCategory;
    private int keyCode;
    public boolean pressed;
    private int pressTime;

    public static void onTick(int keyCode) {
        if (keyCode == 0) return;
        KeyBinding keybinding = hash.lookup(keyCode);
        if (keybinding == null) return;
        ++keybinding.pressTime;
    }

    public static void setKeyBindState(int keyCode, boolean pressed) {
        if (keyCode == 0) return;
        KeyBinding keybinding = hash.lookup(keyCode);
        if (keybinding == null) return;
        keybinding.pressed = pressed;
    }

    public static void unPressAllKeys() {
        Iterator<KeyBinding> iterator = keybindArray.iterator();
        while (iterator.hasNext()) {
            KeyBinding keybinding = iterator.next();
            keybinding.unpressKey();
        }
    }

    public static void resetKeyBindingArrayAndHash() {
        hash.clearMap();
        Iterator<KeyBinding> iterator = keybindArray.iterator();
        while (iterator.hasNext()) {
            KeyBinding keybinding = iterator.next();
            hash.addKey(keybinding.keyCode, keybinding);
        }
    }

    public static Set<String> getKeybinds() {
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

    public boolean isKeyDown() {
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

    @Override
    public int compareTo(KeyBinding p_compareTo_1_) {
        int i = I18n.format(this.keyCategory, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyCategory, new Object[0]));
        if (i != 0) return i;
        return I18n.format(this.keyDescription, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyDescription, new Object[0]));
    }
}

