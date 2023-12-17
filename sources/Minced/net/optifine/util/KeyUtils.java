// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.Arrays;
import java.util.HashSet;
import net.minecraft.client.settings.KeyBinding;

public class KeyUtils
{
    public static void fixKeyConflicts(final KeyBinding[] keys, final KeyBinding[] keysPrio) {
        final Set<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < keysPrio.length; ++i) {
            final KeyBinding keybinding = keysPrio[i];
            set.add(keybinding.getKeyCode());
        }
        final Set<KeyBinding> set2 = new HashSet<KeyBinding>(Arrays.asList(keys));
        set2.removeAll(Arrays.asList(keysPrio));
        for (final KeyBinding keybinding2 : set2) {
            final Integer integer = keybinding2.getKeyCode();
            if (set.contains(integer)) {
                keybinding2.setKeyCode(0);
            }
        }
    }
}
