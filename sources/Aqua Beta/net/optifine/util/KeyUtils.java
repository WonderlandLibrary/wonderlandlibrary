package net.optifine.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.client.settings.KeyBinding;

public class KeyUtils {
    public static void fixKeyConflicts(KeyBinding[] keys, KeyBinding[] keysPrio) {
        HashSet set = new HashSet();
        for (int i = 0; i < keysPrio.length; ++i) {
            KeyBinding keybinding = keysPrio[i];
            set.add((Object)keybinding.getKeyCode());
        }
        HashSet set1 = new HashSet((Collection)Arrays.asList((Object[])keys));
        set1.removeAll((Collection)Arrays.asList((Object[])keysPrio));
        for (KeyBinding keybinding1 : set1) {
            Integer integer = keybinding1.getKeyCode();
            if (!set.contains((Object)integer)) continue;
            keybinding1.setKeyCode(0);
        }
    }
}
