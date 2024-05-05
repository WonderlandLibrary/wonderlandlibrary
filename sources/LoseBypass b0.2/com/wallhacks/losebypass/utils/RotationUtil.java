/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils;

import com.wallhacks.losebypass.utils.MC;
import com.wallhacks.losebypass.utils.MathUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class RotationUtil
implements MC {
    public static float get_rotation_yaw() {
        float rotation_yaw = RotationUtil.mc.thePlayer.rotationYaw;
        if (RotationUtil.mc.thePlayer.moveForward < 0.0f) {
            rotation_yaw += 180.0f;
        }
        float n = 1.0f;
        if (RotationUtil.mc.thePlayer.moveForward < 0.0f) {
            n = -0.5f;
        } else if (RotationUtil.mc.thePlayer.moveForward > 0.0f) {
            n = 0.5f;
        }
        if (RotationUtil.mc.thePlayer.moveStrafing > 0.0f) {
            rotation_yaw -= 90.0f * n;
        }
        if (!(RotationUtil.mc.thePlayer.moveStrafing < 0.0f)) return rotation_yaw * ((float)Math.PI / 180);
        rotation_yaw += 90.0f * n;
        return rotation_yaw * ((float)Math.PI / 180);
    }

    public static double[] directionSpeed(double speed) {
        float forward = RotationUtil.mc.thePlayer.movementInput.moveForward;
        float side = RotationUtil.mc.thePlayer.movementInput.moveStrafe;
        float yaw = RotationUtil.mc.thePlayer.prevRotationYaw + (RotationUtil.mc.thePlayer.rotationYaw - RotationUtil.mc.thePlayer.prevRotationYaw) * RotationUtil.mc.timer.renderPartialTicks;
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static float[] getViewRotations(Vec3 vec, EntityPlayer me) {
        Vec3 eyesPos = me.getPositionEyes(RotationUtil.mc.timer.renderPartialTicks);
        return RotationUtil.getViewRotations(vec, eyesPos, me);
    }

    public static float[] getViewRotations(Vec3 vec, Vec3 eyesPos, EntityPlayer me) {
        double diffX = vec.xCoord - eyesPos.xCoord;
        double diffY = vec.yCoord - eyesPos.yCoord;
        double diffZ = vec.zCoord - eyesPos.zCoord;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        float[] myRot = new float[]{me.rotationYaw, me.rotationPitch};
        return new float[]{myRot[0] + MathUtil.wrapDegrees(yaw - myRot[0]), myRot[1] + MathUtil.wrapDegrees(pitch - myRot[1])};
    }
}

