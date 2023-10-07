// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.settings;

import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.I18n;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.util.IntHashMap;
import java.util.List;

public class KeyBinding implements Comparable
{
    private static final List keybindArray;
    private static final IntHashMap hash;
    private static final Set keybindSet;
    private final String keyDescription;
    private final int keyCodeDefault;
    private final String keyCategory;
    private int keyCode;
    private boolean pressed;
    private int pressTime;
    private static final String __OBFID = "CL_00000628";
    
    public static void onTick(final int keyCode) {
        if (keyCode != 0) {
            final KeyBinding var1 = (KeyBinding)KeyBinding.hash.lookup(keyCode);
            if (var1 != null) {
                final KeyBinding keyBinding = var1;
                ++keyBinding.pressTime;
            }
        }
    }
    
    public static void setKeyBindState(final int keyCode, final boolean pressed) {
        if (keyCode != 0) {
            final KeyBinding var2 = (KeyBinding)KeyBinding.hash.lookup(keyCode);
            if (var2 != null) {
                var2.pressed = pressed;
            }
        }
    }
    
    public static void unPressAllKeys() {
        for (final KeyBinding var2 : KeyBinding.keybindArray) {
            var2.unpressKey();
        }
    }
    
    public static void resetKeyBindingArrayAndHash() {
        KeyBinding.hash.clearMap();
        for (final KeyBinding var2 : KeyBinding.keybindArray) {
            KeyBinding.hash.addKey(var2.keyCode, var2);
        }
    }
    
    public static Set getKeybinds() {
        return KeyBinding.keybindSet;
    }
    
    public KeyBinding(final String description, final int keyCode, final String category) {
        this.keyDescription = description;
        this.keyCode = keyCode;
        this.keyCodeDefault = keyCode;
        this.keyCategory = category;
        KeyBinding.keybindArray.add(this);
        KeyBinding.hash.addKey(keyCode, this);
        KeyBinding.keybindSet.add(category);
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
    
    public void setKeyCode(final int keyCode) {
        this.keyCode = keyCode;
    }
    
    public int compareTo(final KeyBinding p_compareTo_1_) {
        int var2 = I18n.format(this.keyCategory, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyCategory, new Object[0]));
        if (var2 == 0) {
            var2 = I18n.format(this.keyDescription, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyDescription, new Object[0]));
        }
        return var2;
    }
    
    @Override
    public int compareTo(final Object p_compareTo_1_) {
        return this.compareTo((KeyBinding)p_compareTo_1_);
    }
    
    static {
        keybindArray = Lists.newArrayList();
        hash = new IntHashMap();
        keybindSet = Sets.newHashSet();
    }
}
