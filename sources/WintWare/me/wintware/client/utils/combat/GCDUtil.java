/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.utils.combat;

import me.wintware.client.utils.other.MinecraftHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class GCDUtil
implements MinecraftHelper {
    public float yaw;
    public float pitch;

    public GCDUtil(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static float getFixedRotation(float rot) {
        return GCDUtil.getDeltaMouse(rot) * GCDUtil.getGCDValue();
    }

    public static float getGCDValue() {
        return (float)((double)GCDUtil.getGCD() * 0.15);
    }

    public static float getGCD() {
        float f1 = (float)((double)GCDUtil.mc.gameSettings.mouseSensitivity * 0.6 + 0.2);
        return f1 * f1 * f1 * 8.0f;
    }

    public static float getDeltaMouse(float delta) {
        return Math.round(delta / GCDUtil.getGCDValue());
    }

    public static GCDUtil copy$default(GCDUtil var0, float var1, float var2, int var3, Object var4) {
        if ((var3 & 1) != 0) {
            var1 = var0.yaw;
        }
        if ((var3 & 2) != 0) {
            var2 = var0.pitch;
        }
        return var0.copy(var1, var2);
    }

    public final void toPlayer(EntityPlayer player) {
        float var2 = this.yaw;
        boolean var3 = false;
        if (!Float.isNaN(var2)) {
            var2 = this.pitch;
            var3 = false;
            if (!Float.isNaN(var2)) {
                this.fixedSensitivity(Minecraft.getMinecraft().gameSettings.mouseSensitivity);
                player.rotationYaw = this.yaw;
                player.rotationPitch = this.pitch;
                return;
            }
        }
    }

    public void fixedSensitivity(float sensitivity) {
        float f = sensitivity * 0.6f + 0.2f;
        float gcd = f * f * f * 1.2f;
        this.yaw -= this.yaw % gcd;
        this.pitch -= this.pitch % gcd;
    }

    public final float getYaw() {
        return this.yaw;
    }

    public final void setYaw(float var1) {
        this.yaw = var1;
    }

    public final float getPitch() {
        return this.pitch;
    }

    public final void setPitch(float var1) {
        this.pitch = var1;
    }

    public final float component1() {
        return this.yaw;
    }

    public final float component2() {
        return this.pitch;
    }

    public final GCDUtil copy(float yaw, float pitch) {
        return new GCDUtil(yaw, pitch);
    }

    public String toString() {
        return "Rotation(yaw=" + this.yaw + ", pitch=" + this.pitch + ")";
    }

    public int hashCode() {
        return Float.hashCode(this.yaw) * 31 + Float.hashCode(this.pitch);
    }

    public boolean equals(Object var1) {
        if (this != var1) {
            if (var1 instanceof GCDUtil) {
                GCDUtil var2 = (GCDUtil)var1;
                return Float.compare(this.yaw, var2.yaw) == 0 && Float.compare(this.pitch, var2.pitch) == 0;
            }
            return false;
        }
        return true;
    }
}

