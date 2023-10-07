/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.player.EntityPlayer
 */
package net.ccbluex.liquidbounce.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public final class GCDFix {
    public float pitch;
    public float yaw;

    public int hashCode() {
        return Float.hashCode(this.yaw) * 31 + Float.hashCode(this.pitch);
    }

    public boolean equals(Object paramObject) {
        if (this != paramObject) {
            if (paramObject instanceof GCDFix) {
                GCDFix gCDFix = (GCDFix)paramObject;
                return Float.compare(this.yaw, gCDFix.yaw) == 0 && Float.compare(this.pitch, gCDFix.pitch) == 0;
            }
            return false;
        }
        return true;
    }

    public static float getDeltaMouse(float paramFloat) {
        return Math.round(paramFloat / GCDFix.getGCDValue());
    }

    public float getYaw() {
        return this.yaw;
    }

    public GCDFix copy(float paramFloat1, float paramFloat2) {
        return new GCDFix(paramFloat1, paramFloat2);
    }

    public GCDFix(float paramFloat1, float paramFloat2) {
        this.yaw = paramFloat1;
        this.pitch = paramFloat2;
    }

    public static float getGCDValue() {
        return (float)((double)GCDFix.getGCD() * 0.15);
    }

    public void toPlayer(EntityPlayer paramEntityPlayer) {
        float f = this.yaw;
        if (!Float.isNaN(f) && !Float.isNaN(f = this.pitch)) {
            this.fixedSensitivity(Minecraft.func_71410_x().field_71474_y.field_74341_c);
            paramEntityPlayer.field_70177_z = this.yaw;
            paramEntityPlayer.field_70125_A = this.pitch;
        }
    }

    public float getPitch() {
        return this.pitch;
    }

    public void fixedSensitivity(float paramFloat) {
        float f1 = paramFloat * 0.6f + 0.2f;
        float f2 = f1 * f1 * f1 * 1.2f;
        this.yaw -= this.yaw % f2;
        this.pitch -= this.pitch % f2;
    }

    public void setYaw(float paramFloat) {
        this.yaw = paramFloat;
    }

    public float component1() {
        return this.yaw;
    }

    public static GCDFix copy$default(GCDFix paramGCDFix, float paramFloat1, float paramFloat2, int paramInt) {
        if ((paramInt & 1) != 0) {
            paramFloat1 = paramGCDFix.yaw;
        }
        if ((paramInt & 2) != 0) {
            paramFloat2 = paramGCDFix.pitch;
        }
        return paramGCDFix.copy(paramFloat1, paramFloat2);
    }

    public String toString() {
        return String.valueOf(new StringBuilder("Rotation(yaw=").append(this.yaw).append(", pitch=").append(this.pitch).append(")"));
    }

    public static float getFixedRotation(float paramFloat) {
        return GCDFix.getDeltaMouse(paramFloat) * GCDFix.getGCDValue();
    }

    public void setPitch(float paramFloat) {
        this.pitch = paramFloat;
    }

    public float component2() {
        return this.pitch;
    }

    public static float getGCD() {
        float f = (float)((double)Minecraft.func_71410_x().field_71474_y.field_74341_c * 0.6 + 0.2);
        return f * f * f * 8.0f;
    }
}

