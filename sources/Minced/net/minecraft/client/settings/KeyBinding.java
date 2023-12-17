// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.settings;

import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.function.Supplier;
import net.minecraft.client.resources.I18n;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import java.util.Set;
import net.minecraft.util.IntHashMap;
import java.util.Map;

public class KeyBinding implements Comparable<KeyBinding>
{
    private static final Map<String, KeyBinding> KEYBIND_ARRAY;
    private static final IntHashMap<KeyBinding> HASH;
    private static final Set<String> KEYBIND_SET;
    private static final Map<String, Integer> CATEGORY_ORDER;
    private final String keyDescription;
    private final int keyCodeDefault;
    private final String keyCategory;
    private int keyCode;
    public boolean pressed;
    private int pressTime;
    
    public static void onTick(final int keyCode) {
        if (keyCode != 0) {
            final KeyBinding keybinding = KeyBinding.HASH.lookup(keyCode);
            if (keybinding != null) {
                final KeyBinding keyBinding = keybinding;
                ++keyBinding.pressTime;
            }
        }
    }
    
    public static void setKeyBindState(final int keyCode, final boolean pressed) {
        if (keyCode != 0) {
            final KeyBinding keybinding = KeyBinding.HASH.lookup(keyCode);
            if (keybinding != null) {
                keybinding.pressed = pressed;
            }
        }
    }
    
    public static void updateKeyBindState() {
        for (final KeyBinding keybinding : KeyBinding.KEYBIND_ARRAY.values()) {
            try {
                setKeyBindState(keybinding.keyCode, keybinding.keyCode < 256 && Keyboard.isKeyDown(keybinding.keyCode));
            }
            catch (IndexOutOfBoundsException ex) {}
        }
    }
    
    public static void unPressAllKeys() {
        for (final KeyBinding keybinding : KeyBinding.KEYBIND_ARRAY.values()) {
            keybinding.unpressKey();
        }
    }
    
    public static void resetKeyBindingArrayAndHash() {
        KeyBinding.HASH.clearMap();
        for (final KeyBinding keybinding : KeyBinding.KEYBIND_ARRAY.values()) {
            KeyBinding.HASH.addKey(keybinding.keyCode, keybinding);
        }
    }
    
    public static Set<String> getKeybinds() {
        return KeyBinding.KEYBIND_SET;
    }
    
    public KeyBinding(final String description, final int keyCode, final String category) {
        this.keyDescription = description;
        this.keyCode = keyCode;
        this.keyCodeDefault = keyCode;
        this.keyCategory = category;
        KeyBinding.KEYBIND_ARRAY.put(description, this);
        KeyBinding.HASH.addKey(keyCode, this);
        KeyBinding.KEYBIND_SET.add(category);
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
    
    public void setKeyCode(final int keyCode) {
        this.keyCode = keyCode;
    }
    
    @Override
    public int compareTo(final KeyBinding p_compareTo_1_) {
        return this.keyCategory.equals(p_compareTo_1_.keyCategory) ? I18n.format(this.keyDescription, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyDescription, new Object[0])) : KeyBinding.CATEGORY_ORDER.get(this.keyCategory).compareTo(KeyBinding.CATEGORY_ORDER.get(p_compareTo_1_.keyCategory));
    }
    
    public static Supplier<String> getDisplayString(final String key) {
        final KeyBinding keybinding = KeyBinding.KEYBIND_ARRAY.get(key);
        return (keybinding == null) ? (() -> key) : (() -> GameSettings.getKeyDisplayString(keybinding.getKeyCode()));
    }
    
    static {
        KEYBIND_ARRAY = Maps.newHashMap();
        HASH = new IntHashMap<KeyBinding>();
        KEYBIND_SET = Sets.newHashSet();
        (CATEGORY_ORDER = Maps.newHashMap()).put("key.categories.movement", 1);
        KeyBinding.CATEGORY_ORDER.put("key.categories.gameplay", 2);
        KeyBinding.CATEGORY_ORDER.put("key.categories.inventory", 3);
        KeyBinding.CATEGORY_ORDER.put("key.categories.creative", 4);
        KeyBinding.CATEGORY_ORDER.put("key.categories.multiplayer", 5);
        KeyBinding.CATEGORY_ORDER.put("key.categories.ui", 6);
        KeyBinding.CATEGORY_ORDER.put("key.categories.misc", 7);
    }
}
