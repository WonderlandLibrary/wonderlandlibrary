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
import org.jetbrains.annotations.Nullable;

public final class Rotation
extends MinecraftInstance {
    private float yaw;
    private float pitch;

    public final void toPlayer(IEntityPlayer player) {
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
        player.setRotationYaw(this.yaw);
        player.setRotationPitch(this.pitch);
    }

    public final void fixedSensitivity(float sensitivity) {
        float f = sensitivity * 0.6f + 0.2f;
        float gcd = f * f * f * 1.2f;
        this.yaw -= this.yaw % gcd;
        this.pitch -= this.pitch % gcd;
    }

    public final void applyStrafeToPlayer(StrafeEvent event) {
        float d;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntityPlayerSP player = iEntityPlayerSP;
        int dif = (int)((WMathHelper.wrapAngleTo180_float(player.getRotationYaw() - this.yaw - 23.5f - (float)135) + (float)180) / (float)45);
        float yaw = this.yaw;
        float strafe = event.getStrafe();
        float forward = event.getForward();
        float friction = event.getFriction();
        float calcForward = 0.0f;
        float calcStrafe = 0.0f;
        switch (dif) {
            case 0: {
                calcForward = forward;
                calcStrafe = strafe;
                break;
            }
            case 1: {
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            }
            case 2: {
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            }
            case 3: {
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            }
            case 4: {
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            }
            case 5: {
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            }
            case 6: {
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            }
            case 7: {
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
            }
        }
        if (calcForward > 1.0f || calcForward < 0.9f && calcForward > 0.3f || calcForward < -1.0f || calcForward > -0.9f && calcForward < -0.3f) {
            calcForward *= 0.5f;
        }
        if (calcStrafe > 1.0f || calcStrafe < 0.9f && calcStrafe > 0.3f || calcStrafe < -1.0f || calcStrafe > -0.9f && calcStrafe < -0.3f) {
            calcStrafe *= 0.5f;
        }
        if ((d = calcStrafe * calcStrafe + calcForward * calcForward) >= 1.0E-4f) {
            boolean bl = false;
            if ((d = (float)Math.sqrt(d)) < 1.0f) {
                d = 1.0f;
            }
            d = friction / d;
            calcStrafe *= d;
            calcForward *= d;
            float f = (float)((double)yaw * Math.PI / (double)180.0f);
            boolean bl2 = false;
            float yawSin = (float)Math.sin(f);
            float f2 = (float)((double)yaw * Math.PI / (double)180.0f);
            boolean bl3 = false;
            float yawCos = (float)Math.cos(f2);
            IEntityPlayerSP iEntityPlayerSP2 = player;
            iEntityPlayerSP2.setMotionX(iEntityPlayerSP2.getMotionX() + ((double)(calcStrafe * yawCos) - (double)calcForward * (double)yawSin));
            IEntityPlayerSP iEntityPlayerSP3 = player;
            iEntityPlayerSP3.setMotionZ(iEntityPlayerSP3.getMotionZ() + ((double)(calcForward * yawCos) + (double)calcStrafe * (double)yawSin));
        }
    }

    public final float getYaw() {
        return this.yaw;
    }

    public final void setYaw(float f) {
        this.yaw = f;
    }

    public final float getPitch() {
        return this.pitch;
    }

    public final void setPitch(float f) {
        this.pitch = f;
    }

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public final float component1() {
        return this.yaw;
    }

    public final float component2() {
        return this.pitch;
    }

    public final Rotation copy(float yaw, float pitch) {
        return new Rotation(yaw, pitch);
    }

    public static /* synthetic */ Rotation copy$default(Rotation rotation, float f, float f2, int n, Object object) {
        if ((n & 1) != 0) {
            f = rotation.yaw;
        }
        if ((n & 2) != 0) {
            f2 = rotation.pitch;
        }
        return rotation.copy(f, f2);
    }

    public String toString() {
        return "Rotation(yaw=" + this.yaw + ", pitch=" + this.pitch + ")";
    }

    public int hashCode() {
        return Float.hashCode(this.yaw) * 31 + Float.hashCode(this.pitch);
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
}

