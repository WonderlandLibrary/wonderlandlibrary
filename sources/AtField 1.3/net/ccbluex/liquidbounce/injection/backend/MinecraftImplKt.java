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
    public static final Minecraft unwrap(IMinecraft iMinecraft) {
        boolean bl = false;
        return ((MinecraftImpl)iMinecraft).getWrapped();
    }

    public static final IMinecraft wrap(Minecraft minecraft) {
        boolean bl = false;
        return new MinecraftImpl(minecraft);
    }
}

