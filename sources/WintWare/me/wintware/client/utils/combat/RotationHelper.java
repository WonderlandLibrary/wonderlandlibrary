/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.utils.combat;

import me.wintware.client.utils.combat.RotationUtil;
import net.minecraft.client.Minecraft;

public class RotationHelper {
    public float yaw;
    public float pitch;
    private final Minecraft mc = Minecraft.getMinecraft();
    private static RotationHelper rotationManager;

    public boolean hasDiffrence() {
        double diffYaw = RotationUtil.angleDifference(Minecraft.player.rotationYaw, this.yaw);
        double diffPitch = RotationUtil.angleDifference(Minecraft.player.rotationPitch, this.pitch);
        if (diffPitch > 10.0) {
            return true;
        }
        return diffYaw > 30.0;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public static RotationHelper getRotationManager() {
        return rotationManager;
    }
}

