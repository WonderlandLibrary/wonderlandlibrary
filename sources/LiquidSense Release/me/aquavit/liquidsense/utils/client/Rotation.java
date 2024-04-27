package me.aquavit.liquidsense.utils.client;

import me.aquavit.liquidsense.event.events.StrafeEvent;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public final class Rotation extends MinecraftInstance {
    private float yaw;
    private float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void toPlayer(EntityPlayer player) {
        if (Float.isNaN(yaw) || Float.isNaN(pitch))
            return;

        this.fixedSensitivity(mc.gameSettings.mouseSensitivity);
        player.rotationYaw = this.yaw;
        player.rotationPitch = this.pitch;
    }

    public void fixedSensitivity(float sensitivity) {
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

    public void applyStrafeToPlayer(StrafeEvent event) {
        float d;
        EntityPlayerSP player = mc.thePlayer;
        int dif = (int)((MathHelper.wrapAngleTo180_float(player.rotationYaw - this.yaw - 23.5f - (float)135) + (float)180) / (float)45);
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
            if ((d = MathHelper.sqrt_float((float)d)) < 1.0f) {
                d = 1.0f;
            }
            d = friction / d;
            calcStrafe *= d;
            calcForward *= d;
            float yawSin = MathHelper.sin((float)((float)((double)yaw * Math.PI / (double)180.0f)));
            float yawCos = MathHelper.cos((float)((float)((double)yaw * Math.PI / (double)180.0f)));
            player.motionX += (double)((calcStrafe *= d) * yawCos) - (double)(calcForward *= d) * (double)yawSin;
            player.motionZ += (double)(calcForward * yawCos) + (double)calcStrafe * (double)yawSin;
        }
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

    public float component1() {
        return this.yaw;
    }

    public float component2() {
        return this.pitch;
    }
}
