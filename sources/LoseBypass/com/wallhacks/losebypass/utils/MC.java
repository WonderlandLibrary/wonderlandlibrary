/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils;

import net.minecraft.client.Minecraft;

public interface MC {
    public static final Minecraft mc = Minecraft.getMinecraft();

    default public boolean nullCheck() {
        if (MC.mc.thePlayer == null) return true;
        if (MC.mc.theWorld == null) return true;
        return false;
    }

    default public double posX() {
        if (this.nullCheck()) return 0.0;
        return MC.mc.thePlayer.posX;
    }

    default public double posY() {
        if (this.nullCheck()) return 0.0;
        return MC.mc.thePlayer.posY;
    }

    default public double posZ() {
        if (this.nullCheck()) return 0.0;
        return MC.mc.thePlayer.posZ;
    }

    default public float rotationYaw() {
        if (this.nullCheck()) return 0.0f;
        return MC.mc.thePlayer.rotationYaw;
    }

    default public float rotationPitch() {
        if (this.nullCheck()) return 0.0f;
        return MC.mc.thePlayer.rotationPitch;
    }
}

