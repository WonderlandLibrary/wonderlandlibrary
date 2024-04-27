/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityPlayer;
import net.ccbluex.liquidbounce.api.minecraft.util.WMathHelper;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import org.jetbrains.annotations.Nullable;

public final class Rotation
extends MinecraftInstance {
    private float yaw;
    private float pitch;

    public final void applyStrafeToPlayer(StrafeEvent strafeEvent) {
        float f;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        int n = (int)((WMathHelper.wrapAngleTo180_float(iEntityPlayerSP2.getRotationYaw() - this.yaw - 23.5f - (float)135) + (float)180) / (float)45);
        float f2 = this.yaw;
        float f3 = strafeEvent.getStrafe();
        float f4 = strafeEvent.getForward();
        float f5 = strafeEvent.getFriction();
        float f6 = 0.0f;
        float f7 = 0.0f;
        switch (n) {
            case 0: {
                f6 = f4;
                f7 = f3;
                break;
            }
            case 1: {
                f6 += f4;
                f7 -= f4;
                f6 += f3;
                f7 += f3;
                break;
            }
            case 2: {
                f6 = f3;
                f7 = -f4;
                break;
            }
            case 3: {
                f6 -= f4;
                f7 -= f4;
                f6 += f3;
                f7 -= f3;
                break;
            }
            case 4: {
                f6 = -f4;
                f7 = -f3;
                break;
            }
            case 5: {
                f6 -= f4;
                f7 += f4;
                f6 -= f3;
                f7 -= f3;
                break;
            }
            case 6: {
                f6 = -f3;
                f7 = f4;
                break;
            }
            case 7: {
                f6 += f4;
                f7 += f4;
                f6 -= f3;
                f7 += f3;
                break;
            }
        }
        if (f6 > 1.0f || f6 < 0.9f && f6 > 0.3f || f6 < -1.0f || f6 > -0.9f && f6 < -0.3f) {
            f6 *= 0.5f;
        }
        if (f7 > 1.0f || f7 < 0.9f && f7 > 0.3f || f7 < -1.0f || f7 > -0.9f && f7 < -0.3f) {
            f7 *= 0.5f;
        }
        if ((f = f7 * f7 + f6 * f6) >= 1.0E-4f) {
            boolean bl = false;
            if ((f = (float)Math.sqrt(f)) < 1.0f) {
                f = 1.0f;
            }
            f = f5 / f;
            f7 *= f;
            f6 *= f;
            float f8 = (float)((double)f2 * Math.PI / (double)180.0f);
            boolean bl2 = false;
            float f9 = (float)Math.sin(f8);
            float f10 = (float)((double)f2 * Math.PI / (double)180.0f);
            boolean bl3 = false;
            f8 = (float)Math.cos(f10);
            IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
            iEntityPlayerSP3.setMotionX(iEntityPlayerSP3.getMotionX() + ((double)(f7 * f8) - (double)f6 * (double)f9));
            IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
            iEntityPlayerSP4.setMotionZ(iEntityPlayerSP4.getMotionZ() + ((double)(f6 * f8) + (double)f7 * (double)f9));
        }
    }

    public final Rotation copy(float f, float f2) {
        return new Rotation(f, f2);
    }

    public final void toPlayer(IEntityPlayer iEntityPlayer) {
        block3: {
            block2: {
                float f = this.yaw;
                boolean bl = false;
                if (Float.isNaN(f)) break block2;
                f = this.pitch;
                bl = false;
                if (!Float.isNaN(f)) break block3;
            }
            return;
        }
        this.fixedSensitivity(MinecraftInstance.mc.getGameSettings().getMouseSensitivity());
        iEntityPlayer.setRotationYaw(this.yaw);
        iEntityPlayer.setRotationPitch(this.pitch);
    }

    public int hashCode() {
        return Float.hashCode(this.yaw) * 31 + Float.hashCode(this.pitch);
    }

    public final float component1() {
        return this.yaw;
    }

    public String toString() {
        return "Rotation(yaw=" + this.yaw + ", pitch=" + this.pitch + ")";
    }

    public final void fixedSensitivity(float f) {
        float f2 = f * 0.6f + 0.2f;
        float f3 = f2 * f2 * f2 * 1.2f;
        Rotation rotation = RotationUtils.serverRotation;
        float f4 = this.yaw - rotation.yaw;
        f4 -= f4 % f3;
        this.yaw = rotation.yaw + f4;
        float f5 = this.pitch - rotation.pitch;
        f5 -= f5 % f3;
        this.pitch = rotation.pitch + f5;
    }

    public boolean equals(@Nullable Object object) {
        block3: {
            block2: {
                if (this == object) break block2;
                if (!(object instanceof Rotation)) break block3;
                Rotation rotation = (Rotation)object;
                if (Float.compare(this.yaw, rotation.yaw) != 0 || Float.compare(this.pitch, rotation.pitch) != 0) break block3;
            }
            return true;
        }
        return false;
    }

    public final float getYaw() {
        return this.yaw;
    }

    public final void setPitch(float f) {
        this.pitch = f;
    }

    public static Rotation copy$default(Rotation rotation, float f, float f2, int n, Object object) {
        if ((n & 1) != 0) {
            f = rotation.yaw;
        }
        if ((n & 2) != 0) {
            f2 = rotation.pitch;
        }
        return rotation.copy(f, f2);
    }

    public final void setYaw(float f) {
        this.yaw = f;
    }

    public final float getPitch() {
        return this.pitch;
    }

    public Rotation(float f, float f2) {
        this.yaw = f;
        this.pitch = f2;
    }

    public final float component2() {
        return this.pitch;
    }
}

