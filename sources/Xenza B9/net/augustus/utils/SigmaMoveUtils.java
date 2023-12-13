// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils;

import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.minecraft.potion.Potion;
import net.minecraft.client.Minecraft;

public class SigmaMoveUtils
{
    private static Minecraft mc;
    
    public static double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static void strafe(final double speed) {
        final float a = SigmaMoveUtils.mc.thePlayer.rotationYaw * 0.017453292f;
        final float l = SigmaMoveUtils.mc.thePlayer.rotationYaw * 0.017453292f - 4.712389f;
        final float r = SigmaMoveUtils.mc.thePlayer.rotationYaw * 0.017453292f + 4.712389f;
        final float rf = SigmaMoveUtils.mc.thePlayer.rotationYaw * 0.017453292f + 0.5969026f;
        final float lf = SigmaMoveUtils.mc.thePlayer.rotationYaw * 0.017453292f - 0.5969026f;
        final float lb = SigmaMoveUtils.mc.thePlayer.rotationYaw * 0.017453292f - 2.3876104f;
        final float rb = SigmaMoveUtils.mc.thePlayer.rotationYaw * 0.017453292f + 2.3876104f;
        if (SigmaMoveUtils.mc.gameSettings.keyBindForward.pressed) {
            if (SigmaMoveUtils.mc.gameSettings.keyBindLeft.pressed && !SigmaMoveUtils.mc.gameSettings.keyBindRight.pressed) {
                final EntityPlayerSP thePlayer = SigmaMoveUtils.mc.thePlayer;
                thePlayer.motionX -= MathHelper.sin(lf) * speed;
                final EntityPlayerSP thePlayer2 = SigmaMoveUtils.mc.thePlayer;
                thePlayer2.motionZ += MathHelper.cos(lf) * speed;
            }
            else if (SigmaMoveUtils.mc.gameSettings.keyBindRight.pressed && !SigmaMoveUtils.mc.gameSettings.keyBindLeft.pressed) {
                final EntityPlayerSP thePlayer3 = SigmaMoveUtils.mc.thePlayer;
                thePlayer3.motionX -= MathHelper.sin(rf) * speed;
                final EntityPlayerSP thePlayer4 = SigmaMoveUtils.mc.thePlayer;
                thePlayer4.motionZ += MathHelper.cos(rf) * speed;
            }
            else {
                final EntityPlayerSP thePlayer5 = SigmaMoveUtils.mc.thePlayer;
                thePlayer5.motionX -= MathHelper.sin(a) * speed;
                final EntityPlayerSP thePlayer6 = SigmaMoveUtils.mc.thePlayer;
                thePlayer6.motionZ += MathHelper.cos(a) * speed;
            }
        }
        else if (SigmaMoveUtils.mc.gameSettings.keyBindBack.pressed) {
            if (SigmaMoveUtils.mc.gameSettings.keyBindLeft.pressed && !SigmaMoveUtils.mc.gameSettings.keyBindRight.pressed) {
                final EntityPlayerSP thePlayer7 = SigmaMoveUtils.mc.thePlayer;
                thePlayer7.motionX -= MathHelper.sin(lb) * speed;
                final EntityPlayerSP thePlayer8 = SigmaMoveUtils.mc.thePlayer;
                thePlayer8.motionZ += MathHelper.cos(lb) * speed;
            }
            else if (SigmaMoveUtils.mc.gameSettings.keyBindRight.pressed && !SigmaMoveUtils.mc.gameSettings.keyBindLeft.pressed) {
                final EntityPlayerSP thePlayer9 = SigmaMoveUtils.mc.thePlayer;
                thePlayer9.motionX -= MathHelper.sin(rb) * speed;
                final EntityPlayerSP thePlayer10 = SigmaMoveUtils.mc.thePlayer;
                thePlayer10.motionZ += MathHelper.cos(rb) * speed;
            }
            else {
                final EntityPlayerSP thePlayer11 = SigmaMoveUtils.mc.thePlayer;
                thePlayer11.motionX += MathHelper.sin(a) * speed;
                final EntityPlayerSP thePlayer12 = SigmaMoveUtils.mc.thePlayer;
                thePlayer12.motionZ -= MathHelper.cos(a) * speed;
            }
        }
        else if (SigmaMoveUtils.mc.gameSettings.keyBindLeft.pressed && !SigmaMoveUtils.mc.gameSettings.keyBindRight.pressed && !SigmaMoveUtils.mc.gameSettings.keyBindForward.pressed && !SigmaMoveUtils.mc.gameSettings.keyBindBack.pressed) {
            final EntityPlayerSP thePlayer13 = SigmaMoveUtils.mc.thePlayer;
            thePlayer13.motionX += MathHelper.sin(l) * speed;
            final EntityPlayerSP thePlayer14 = SigmaMoveUtils.mc.thePlayer;
            thePlayer14.motionZ -= MathHelper.cos(l) * speed;
        }
        else if (SigmaMoveUtils.mc.gameSettings.keyBindRight.pressed && !SigmaMoveUtils.mc.gameSettings.keyBindLeft.pressed && !SigmaMoveUtils.mc.gameSettings.keyBindForward.pressed && !SigmaMoveUtils.mc.gameSettings.keyBindBack.pressed) {
            final EntityPlayerSP thePlayer15 = SigmaMoveUtils.mc.thePlayer;
            thePlayer15.motionX += MathHelper.sin(r) * speed;
            final EntityPlayerSP thePlayer16 = SigmaMoveUtils.mc.thePlayer;
            thePlayer16.motionZ -= MathHelper.cos(r) * speed;
        }
    }
    
    public static void setMotion(final double speed) {
        double forward = SigmaMoveUtils.mc.thePlayer.movementInput.moveForward;
        double strafe = SigmaMoveUtils.mc.thePlayer.movementInput.moveStrafe;
        float yaw = SigmaMoveUtils.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            SigmaMoveUtils.mc.thePlayer.motionX = 0.0;
            SigmaMoveUtils.mc.thePlayer.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            SigmaMoveUtils.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            SigmaMoveUtils.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }
    
    public static boolean checkTeleport(final double x, final double y, final double z, final double distBetweenPackets) {
        final double distx = SigmaMoveUtils.mc.thePlayer.posX - x;
        final double disty = SigmaMoveUtils.mc.thePlayer.posY - y;
        final double distz = SigmaMoveUtils.mc.thePlayer.posZ - z;
        final double dist = Math.sqrt(SigmaMoveUtils.mc.thePlayer.getDistanceSq(x, y, z));
        final double distanceEntreLesPackets = distBetweenPackets;
        final double nbPackets = (double)(Math.round(dist / distanceEntreLesPackets + 0.49999999999) - 1L);
        double xtp = SigmaMoveUtils.mc.thePlayer.posX;
        double ytp = SigmaMoveUtils.mc.thePlayer.posY;
        double ztp = SigmaMoveUtils.mc.thePlayer.posZ;
        for (int i = 1; i < nbPackets; ++i) {
            final double xdi = (x - SigmaMoveUtils.mc.thePlayer.posX) / nbPackets;
            xtp += xdi;
            final double zdi = (z - SigmaMoveUtils.mc.thePlayer.posZ) / nbPackets;
            ztp += zdi;
            final double ydi = (y - SigmaMoveUtils.mc.thePlayer.posY) / nbPackets;
            ytp += ydi;
            final AxisAlignedBB bb = new AxisAlignedBB(xtp - 0.3, ytp, ztp - 0.3, xtp + 0.3, ytp + 1.8, ztp + 0.3);
            if (!SigmaMoveUtils.mc.theWorld.getCollidingBoundingBoxes(SigmaMoveUtils.mc.thePlayer, bb).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isOnGround(final double height) {
        return !SigmaMoveUtils.mc.theWorld.getCollidingBoundingBoxes(SigmaMoveUtils.mc.thePlayer, SigmaMoveUtils.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public static int getJumpEffect() {
        if (SigmaMoveUtils.mc.thePlayer.isPotionActive(Potion.jump)) {
            return SigmaMoveUtils.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static int getSpeedEffect() {
        if (SigmaMoveUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return SigmaMoveUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer, final double height) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock();
    }
    
    public static Block getBlockAtPosC(final double x, final double y, final double z) {
        final EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().thePlayer;
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(entityPlayerSP.posX + x, entityPlayerSP.posY + y, entityPlayerSP.posZ + z)).getBlock();
    }
    
    public static float getDistanceToGround(final Entity e) {
        if (SigmaMoveUtils.mc.thePlayer.isCollidedVertically && SigmaMoveUtils.mc.thePlayer.onGround) {
            return 0.0f;
        }
        float a = (float)e.posY;
        while (a > 0.0f) {
            final int[] stairs = { 53, 67, 108, 109, 114, 128, 134, 135, 136, 156, 163, 164, 180 };
            final int[] exemptIds = { 6, 27, 28, 30, 31, 32, 37, 38, 39, 40, 50, 51, 55, 59, 63, 65, 66, 68, 69, 70, 72, 75, 76, 77, 83, 92, 93, 94, 104, 105, 106, 115, 119, 131, 132, 143, 147, 148, 149, 150, 157, 171, 175, 176, 177 };
            final Block block = SigmaMoveUtils.mc.theWorld.getBlockState(new BlockPos(e.posX, a - 1.0f, e.posZ)).getBlock();
            if (!(block instanceof BlockAir)) {
                if (Block.getIdFromBlock(block) == 44 || Block.getIdFromBlock(block) == 126) {
                    return ((float)(e.posY - a - 0.5) < 0.0f) ? 0.0f : ((float)(e.posY - a - 0.5));
                }
                int[] arrayOfInt1;
                for (int j = (arrayOfInt1 = stairs).length, i = 0; i < j; ++i) {
                    final int id = arrayOfInt1[i];
                    if (Block.getIdFromBlock(block) == id) {
                        return ((float)(e.posY - a - 1.0) < 0.0f) ? 0.0f : ((float)(e.posY - a - 1.0));
                    }
                }
                for (int j = (arrayOfInt1 = exemptIds).length, i = 0; i < j; ++i) {
                    final int id = arrayOfInt1[i];
                    if (Block.getIdFromBlock(block) == id) {
                        return ((float)(e.posY - a) < 0.0f) ? 0.0f : ((float)(e.posY - a));
                    }
                }
                return (float)(e.posY - a + block.getBlockBoundsMaxY() - 1.0);
            }
            else {
                --a;
            }
        }
        return 0.0f;
    }
    
    public static float[] getRotationsBlock(final BlockPos block, final EnumFacing face) {
        final double x = block.getX() + 0.5 - SigmaMoveUtils.mc.thePlayer.posX + face.getFrontOffsetX() / 2.0;
        final double z = block.getZ() + 0.5 - SigmaMoveUtils.mc.thePlayer.posZ + face.getFrontOffsetZ() / 2.0;
        final double y = block.getY() + 0.5;
        final double d1 = SigmaMoveUtils.mc.thePlayer.posY + SigmaMoveUtils.mc.thePlayer.getEyeHeight() - y;
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[] { yaw, pitch };
    }
    
    public static boolean isBlockAboveHead() {
        final AxisAlignedBB bb = new AxisAlignedBB(SigmaMoveUtils.mc.thePlayer.posX - 0.3, SigmaMoveUtils.mc.thePlayer.posY + SigmaMoveUtils.mc.thePlayer.getEyeHeight(), SigmaMoveUtils.mc.thePlayer.posZ + 0.3, SigmaMoveUtils.mc.thePlayer.posX + 0.3, SigmaMoveUtils.mc.thePlayer.posY + 2.5, SigmaMoveUtils.mc.thePlayer.posZ - 0.3);
        return !SigmaMoveUtils.mc.theWorld.getCollidingBoundingBoxes(SigmaMoveUtils.mc.thePlayer, bb).isEmpty();
    }
    
    public static boolean isCollidedH(final double dist) {
        final AxisAlignedBB bb = new AxisAlignedBB(SigmaMoveUtils.mc.thePlayer.posX - 0.3, SigmaMoveUtils.mc.thePlayer.posY + 2.0, SigmaMoveUtils.mc.thePlayer.posZ + 0.3, SigmaMoveUtils.mc.thePlayer.posX + 0.3, SigmaMoveUtils.mc.thePlayer.posY + 3.0, SigmaMoveUtils.mc.thePlayer.posZ - 0.3);
        return !SigmaMoveUtils.mc.theWorld.getCollidingBoundingBoxes(SigmaMoveUtils.mc.thePlayer, bb.offset(0.3 + dist, 0.0, 0.0)).isEmpty() || !SigmaMoveUtils.mc.theWorld.getCollidingBoundingBoxes(SigmaMoveUtils.mc.thePlayer, bb.offset(-0.3 - dist, 0.0, 0.0)).isEmpty() || !SigmaMoveUtils.mc.theWorld.getCollidingBoundingBoxes(SigmaMoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, 0.3 + dist)).isEmpty() || !SigmaMoveUtils.mc.theWorld.getCollidingBoundingBoxes(SigmaMoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, -0.3 - dist)).isEmpty();
    }
    
    public static boolean isRealCollidedH(final double dist) {
        final AxisAlignedBB bb = new AxisAlignedBB(SigmaMoveUtils.mc.thePlayer.posX - 0.3, SigmaMoveUtils.mc.thePlayer.posY + 0.5, SigmaMoveUtils.mc.thePlayer.posZ + 0.3, SigmaMoveUtils.mc.thePlayer.posX + 0.3, SigmaMoveUtils.mc.thePlayer.posY + 1.9, SigmaMoveUtils.mc.thePlayer.posZ - 0.3);
        return !SigmaMoveUtils.mc.theWorld.getCollidingBoundingBoxes(SigmaMoveUtils.mc.thePlayer, bb.offset(0.3 + dist, 0.0, 0.0)).isEmpty() || !SigmaMoveUtils.mc.theWorld.getCollidingBoundingBoxes(SigmaMoveUtils.mc.thePlayer, bb.offset(-0.3 - dist, 0.0, 0.0)).isEmpty() || !SigmaMoveUtils.mc.theWorld.getCollidingBoundingBoxes(SigmaMoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, 0.3 + dist)).isEmpty() || !SigmaMoveUtils.mc.theWorld.getCollidingBoundingBoxes(SigmaMoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, -0.3 - dist)).isEmpty();
    }
    
    static {
        SigmaMoveUtils.mc = Minecraft.getMinecraft();
    }
}
