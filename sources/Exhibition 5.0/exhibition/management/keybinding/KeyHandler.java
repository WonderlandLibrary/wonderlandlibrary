// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.keybinding;

import java.util.Iterator;
import java.util.Collection;
import org.lwjgl.input.Keyboard;
import exhibition.Client;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class KeyHandler
{
    private static final HashMap<Integer, ArrayList<Keybind>> registeredKeys;
    private static final List<KeyMask> activeMasks;
    
    public static void update(final boolean isInGui) {
        if (Client.isHidden()) {
            return;
        }
        final boolean pressed = Keyboard.getEventKeyState();
        final int key = Keyboard.getEventKey();
        updateMasks(key, pressed);
        if (isInGui) {}
        if (key == 0) {
            return;
        }
        if (key == 1) {
            KeyHandler.activeMasks.clear();
        }
        if (!keyHasBinds(key)) {
            return;
        }
        final ArrayList<Keybind> set = new ArrayList<Keybind>();
        set.addAll(KeyHandler.registeredKeys.get(key));
        for (final Keybind keybind : set) {
            if (isMaskDown(keybind.getMask())) {
                if (pressed) {
                    keybind.press();
                }
                else {
                    keybind.release();
                }
            }
        }
    }
    
    private static void updateMasks(final int key, final boolean pressed) {
        for (final KeyMask mask : KeyMask.values()) {
            if (!mask.equals(KeyMask.None)) {
                for (final int keyInList : mask.getKeys()) {
                    if (keyInList == key) {
                        final boolean contains = KeyHandler.activeMasks.contains(mask);
                        if (pressed) {
                            if (!contains) {
                                KeyHandler.activeMasks.add(mask);
                            }
                        }
                        else if (contains) {
                            KeyHandler.activeMasks.remove(mask);
                        }
                        return;
                    }
                }
            }
        }
    }
    
    public static boolean isRegistered(final Keybind keybind) {
        final int key = keybind.getKeyInt();
        final boolean hasKeys = keyHasBinds(key);
        return hasKeys && KeyHandler.registeredKeys.get(key).contains(keybind);
    }
    
    public static void register(final Keybind keybind) {
        final int key = keybind.getKeyInt();
        final boolean hasKeys = keyHasBinds(key);
        if (hasKeys) {
            if (!KeyHandler.registeredKeys.get(key).contains(keybind)) {
                KeyHandler.registeredKeys.get(key).add(keybind);
            }
        }
        else {
            final ArrayList<Keybind> keyList = new ArrayList<Keybind>();
            keyList.add(keybind);
            KeyHandler.registeredKeys.put(key, keyList);
        }
    }
    
    public static void update(final Bindable owner, final Keybind keybind, final Keybind newBind) {
        final int key = keybind.getKeyInt();
        final int newKey = newBind.getKeyInt();
        final boolean hasKeys = keyHasBinds(key);
        if (hasKeys) {
            for (final Keybind regKey : KeyHandler.registeredKeys.get(key)) {
                if (regKey.getBindOwner().equals(owner)) {
                    regKey.update(newBind);
                }
            }
        }
    }
    
    public static void unregister(final Bindable owner, final Keybind keybind) {
        final int key = keybind.getKeyInt();
        final boolean hasKeys = keyHasBinds(key);
        if (hasKeys) {
            final ArrayList<Keybind> list = KeyHandler.registeredKeys.get(key);
            int in = -1;
            int i = 0;
            for (final Keybind bind : list) {
                if (bind.getBindOwner().equals(owner)) {
                    in = i;
                }
                ++i;
            }
            if (in >= 0) {
                list.remove(in);
            }
        }
    }
    
    public static boolean keyHasBinds(final int key) {
        return KeyHandler.registeredKeys.containsKey(key);
    }
    
    static boolean isMaskDown(final KeyMask mask) {
        return mask == null || mask == KeyMask.None || KeyHandler.activeMasks.contains(mask);
    }
    
    static {
        registeredKeys = new HashMap<Integer, ArrayList<Keybind>>();
        activeMasks = new ArrayList<KeyMask>();
    }
}
