package com.initial.utils;

import net.minecraft.client.*;
import net.minecraft.util.*;

public class WorldUtils
{
    public static BlockPos getForwardBlock(final double length) {
        final Minecraft mc = Minecraft.getMinecraft();
        final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        final BlockPos fPos = new BlockPos(mc.thePlayer.posX + -Math.sin(yaw) * length, mc.thePlayer.posY, mc.thePlayer.posZ + Math.cos(yaw) * length);
        return fPos;
    }
    
    public static BlockPos getForwardBlockFromMovement(final double length) {
        final Minecraft mc = Minecraft.getMinecraft();
        final float forward = mc.thePlayer.movementInput.moveForward;
        float strafe = mc.thePlayer.movementInput.moveStrafe;
        double yaw = mc.thePlayer.rotationYaw;
        if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
                strafe = 0.0f;
            }
            else if (strafe <= -1.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
                strafe = 0.0f;
            }
        }
        if (forward == 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((strafe > 0.0f) ? -90 : 90);
                strafe = 0.0f;
            }
            else if (strafe <= -1.0f) {
                yaw += ((strafe > 0.0f) ? -90 : 90);
                strafe = 0.0f;
            }
        }
        if (forward < 0.0f) {
            yaw += 180.0;
        }
        yaw = Math.toRadians(yaw);
        final BlockPos fPos = new BlockPos(mc.thePlayer.posX + -Math.sin(yaw) * length, mc.thePlayer.posY, mc.thePlayer.posZ + Math.cos(yaw) * length);
        return fPos;
    }
    
    public static BlockPos getForwardBlockFromMovement(final double length, final double sidewaysOffset) {
        final Minecraft mc = Minecraft.getMinecraft();
        float forward = mc.thePlayer.movementInput.moveForward;
        float strafe = mc.thePlayer.movementInput.moveStrafe;
        double yaw = mc.thePlayer.rotationYaw;
        if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
                strafe = 0.0f;
            }
            else if (strafe <= -1.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
                strafe = 0.0f;
            }
        }
        if (forward == 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((strafe > 0.0f) ? -90 : 90);
                strafe = 0.0f;
            }
            else if (strafe <= -1.0f) {
                yaw += ((strafe > 0.0f) ? -90 : 90);
                strafe = 0.0f;
            }
        }
        if (forward < 0.0f) {
            yaw += 180.0;
        }
        yaw = Math.toRadians(yaw);
        BlockPos fPos = new BlockPos(mc.thePlayer.posX + -Math.sin(yaw) * length, mc.thePlayer.posY, mc.thePlayer.posZ + Math.cos(yaw) * length);
        forward = mc.thePlayer.movementInput.moveForward;
        strafe = mc.thePlayer.movementInput.moveStrafe;
        yaw = Math.abs(mc.thePlayer.rotationYaw);
        if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
                strafe = 0.0f;
            }
            else if (strafe <= -1.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
                strafe = 0.0f;
            }
        }
        if (forward == 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((strafe > 0.0f) ? -90 : 90);
                strafe = 0.0f;
            }
            else if (strafe <= -1.0f) {
                yaw += ((strafe > 0.0f) ? -90 : 90);
                strafe = 0.0f;
            }
        }
        if (forward < 0.0f) {
            yaw += 180.0;
        }
        yaw += 6.0;
        yaw -= yaw % 45.0;
        yaw += 90.0;
        yaw = Math.toRadians(yaw);
        fPos = new BlockPos(fPos.add(-Math.sin(yaw) * sidewaysOffset, 0.0, Math.cos(yaw) * sidewaysOffset));
        return fPos;
    }
    
    public static double getDistance(final BlockPos pos1, final BlockPos pos2) {
        return getDistance(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
    }
    
    public static double getDistance(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        final double d0 = x2 - x1;
        final double d2 = y2 - y1;
        final double d3 = z2 - z1;
        return MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
    }
}
