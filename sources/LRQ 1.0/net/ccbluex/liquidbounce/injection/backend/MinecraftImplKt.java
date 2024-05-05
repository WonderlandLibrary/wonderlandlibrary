/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.IMinecraft;
import net.ccbluex.liquidbounce.injection.backend.MinecraftImpl;
import net.minecraft.client.Minecraft;

public final class MinecraftImplKt {
    public static final Minecraft unwrap(IMinecraft $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((MinecraftImpl)$this$unwrap).getWrapped();
    }

    public static final IMinecraft wrap(Minecraft $this$wrap) {
        int $i$f$wrap = 0;
        return new MinecraftImpl($this$wrap);
    }
}

