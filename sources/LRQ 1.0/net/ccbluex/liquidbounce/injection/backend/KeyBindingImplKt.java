/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding;
import net.ccbluex.liquidbounce.injection.backend.KeyBindingImpl;
import net.minecraft.client.settings.KeyBinding;

public final class KeyBindingImplKt {
    public static final KeyBinding unwrap(IKeyBinding $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((KeyBindingImpl)$this$unwrap).getWrapped();
    }

    public static final IKeyBinding wrap(KeyBinding $this$wrap) {
        int $i$f$wrap = 0;
        return new KeyBindingImpl($this$wrap);
    }
}

