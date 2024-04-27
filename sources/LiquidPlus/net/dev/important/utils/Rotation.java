/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.MathHelper
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.StrafeEvent;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.RotationUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\u001d\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\u000e\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u0003J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001J\u000e\u0010\u001a\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\u001cJ\t\u0010\u001d\u001a\u00020\u001eH\u00d6\u0001R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0007\"\u0004\b\u000b\u0010\t\u00a8\u0006\u001f"}, d2={"Lnet/dev/important/utils/Rotation;", "", "yaw", "", "pitch", "(FF)V", "getPitch", "()F", "setPitch", "(F)V", "getYaw", "setYaw", "applyStrafeToPlayer", "", "event", "Lnet/dev/important/event/StrafeEvent;", "component1", "component2", "copy", "equals", "", "other", "fixedSensitivity", "sensitivity", "hashCode", "", "toPlayer", "player", "Lnet/minecraft/entity/player/EntityPlayer;", "toString", "", "LiquidBounce"})
public final class Rotation {
    private float yaw;
    private float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
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

    public final void toPlayer(@NotNull EntityPlayer player) {
        Intrinsics.checkNotNullParameter(player, "player");
        if (Float.isNaN(this.yaw) || Float.isNaN(this.pitch)) {
            return;
        }
        this.fixedSensitivity(MinecraftInstance.mc.field_71474_y.field_74341_c);
        player.field_70177_z = this.yaw;
        player.field_70125_A = this.pitch;
    }

    public final void fixedSensitivity(float sensitivity) {
        float f = sensitivity * 0.6f + 0.2f;
        float gcd = f * f * f * 1.2f;
        Rotation rotation = RotationUtils.serverRotation;
        float deltaYaw = this.yaw - rotation.yaw;
        deltaYaw -= deltaYaw % gcd;
        this.yaw = rotation.yaw + deltaYaw;
        float deltaPitch = this.pitch - rotation.pitch;
        deltaPitch -= deltaPitch % gcd;
        this.pitch = rotation.pitch + deltaPitch;
    }

    public final void applyStrafeToPlayer(@NotNull StrafeEvent event) {
        float d;
        Intrinsics.checkNotNullParameter(event, "event");
        EntityPlayerSP player = MinecraftInstance.mc.field_71439_g;
        int dif = (int)((MathHelper.func_76142_g((float)(player.field_70177_z - this.yaw - 23.5f - (float)135)) + (float)180) / (float)45);
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
            if ((d = MathHelper.func_76129_c((float)d)) < 1.0f) {
                d = 1.0f;
            }
            d = friction / d;
            float yawSin = MathHelper.func_76126_a((float)((float)((double)yaw * Math.PI / (double)180.0f)));
            float yawCos = MathHelper.func_76134_b((float)((float)((double)yaw * Math.PI / (double)180.0f)));
            player.field_70159_w += (double)((calcStrafe *= d) * yawCos) - (double)(calcForward *= d) * (double)yawSin;
            player.field_70179_y += (double)(calcForward * yawCos) + (double)calcStrafe * (double)yawSin;
        }
    }

    public final float component1() {
        return this.yaw;
    }

    public final float component2() {
        return this.pitch;
    }

    @NotNull
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

    @NotNull
    public String toString() {
        return "Rotation(yaw=" + this.yaw + ", pitch=" + this.pitch + ')';
    }

    public int hashCode() {
        int result = Float.hashCode(this.yaw);
        result = result * 31 + Float.hashCode(this.pitch);
        return result;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Rotation)) {
            return false;
        }
        Rotation rotation = (Rotation)other;
        if (!Intrinsics.areEqual((Object)Float.valueOf(this.yaw), (Object)Float.valueOf(rotation.yaw))) {
            return false;
        }
        return Intrinsics.areEqual((Object)Float.valueOf(this.pitch), (Object)Float.valueOf(rotation.pitch));
    }
}

