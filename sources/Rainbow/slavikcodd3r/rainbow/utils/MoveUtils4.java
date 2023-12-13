package slavikcodd3r.rainbow.utils;

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

public class MoveUtils4
{
    private static Minecraft mc;
    
    static {
        MoveUtils4.mc = Minecraft.getMinecraft();
    }
    
    public static double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static void strafe(final double speed) {
        final float a = MoveUtils4.mc.thePlayer.rotationYaw * 0.017453292f;
        final float l = MoveUtils4.mc.thePlayer.rotationYaw * 0.017453292f - 4.712389f;
        final float r = MoveUtils4.mc.thePlayer.rotationYaw * 0.017453292f + 4.712389f;
        final float rf = MoveUtils4.mc.thePlayer.rotationYaw * 0.017453292f + 0.5969026f;
        final float lf = MoveUtils4.mc.thePlayer.rotationYaw * 0.017453292f - 0.5969026f;
        final float lb = MoveUtils4.mc.thePlayer.rotationYaw * 0.017453292f - 2.3876104f;
        final float rb = MoveUtils4.mc.thePlayer.rotationYaw * 0.017453292f + 2.3876104f;
        if (MoveUtils4.mc.gameSettings.keyBindForward.isPressed()) {
            if (MoveUtils4.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils4.mc.gameSettings.keyBindRight.isPressed()) {
                final EntityPlayerSP thePlayer17;
                final EntityPlayerSP thePlayer = thePlayer17 = MoveUtils4.mc.thePlayer;
                thePlayer17.motionX -= MathHelper.sin(lf) * speed;
                final EntityPlayerSP thePlayer18;
                final EntityPlayerSP thePlayer2 = thePlayer18 = MoveUtils4.mc.thePlayer;
                thePlayer18.motionZ += MathHelper.cos(lf) * speed;
            }
            else if (MoveUtils4.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils4.mc.gameSettings.keyBindLeft.isPressed()) {
                final EntityPlayerSP thePlayer19;
                final EntityPlayerSP thePlayer3 = thePlayer19 = MoveUtils4.mc.thePlayer;
                thePlayer19.motionX -= MathHelper.sin(rf) * speed;
                final EntityPlayerSP thePlayer20;
                final EntityPlayerSP thePlayer4 = thePlayer20 = MoveUtils4.mc.thePlayer;
                thePlayer20.motionZ += MathHelper.cos(rf) * speed;
            }
            else {
                final EntityPlayerSP thePlayer21;
                final EntityPlayerSP thePlayer5 = thePlayer21 = MoveUtils4.mc.thePlayer;
                thePlayer21.motionX -= MathHelper.sin(a) * speed;
                final EntityPlayerSP thePlayer22;
                final EntityPlayerSP thePlayer6 = thePlayer22 = MoveUtils4.mc.thePlayer;
                thePlayer22.motionZ += MathHelper.cos(a) * speed;
            }
        }
        else if (MoveUtils4.mc.gameSettings.keyBindBack.isPressed()) {
            if (MoveUtils4.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils4.mc.gameSettings.keyBindRight.isPressed()) {
                final EntityPlayerSP thePlayer23;
                final EntityPlayerSP thePlayer7 = thePlayer23 = MoveUtils4.mc.thePlayer;
                thePlayer23.motionX -= MathHelper.sin(lb) * speed;
                final EntityPlayerSP thePlayer24;
                final EntityPlayerSP thePlayer8 = thePlayer24 = MoveUtils4.mc.thePlayer;
                thePlayer24.motionZ += MathHelper.cos(lb) * speed;
            }
            else if (MoveUtils4.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils4.mc.gameSettings.keyBindLeft.isPressed()) {
                final EntityPlayerSP thePlayer25;
                final EntityPlayerSP thePlayer9 = thePlayer25 = MoveUtils4.mc.thePlayer;
                thePlayer25.motionX -= MathHelper.sin(rb) * speed;
                final EntityPlayerSP thePlayer26;
                final EntityPlayerSP thePlayer10 = thePlayer26 = MoveUtils4.mc.thePlayer;
                thePlayer26.motionZ += MathHelper.cos(rb) * speed;
            }
            else {
                final EntityPlayerSP thePlayer27;
                final EntityPlayerSP thePlayer11 = thePlayer27 = MoveUtils4.mc.thePlayer;
                thePlayer27.motionX += MathHelper.sin(a) * speed;
                final EntityPlayerSP thePlayer28;
                final EntityPlayerSP thePlayer12 = thePlayer28 = MoveUtils4.mc.thePlayer;
                thePlayer28.motionZ -= MathHelper.cos(a) * speed;
            }
        }
        else if (MoveUtils4.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils4.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils4.mc.gameSettings.keyBindForward.isPressed() && !MoveUtils4.mc.gameSettings.keyBindBack.isPressed()) {
            final EntityPlayerSP thePlayer29;
            final EntityPlayerSP thePlayer13 = thePlayer29 = MoveUtils4.mc.thePlayer;
            thePlayer29.motionX += MathHelper.sin(l) * speed;
            final EntityPlayerSP thePlayer30;
            final EntityPlayerSP thePlayer14 = thePlayer30 = MoveUtils4.mc.thePlayer;
            thePlayer30.motionZ -= MathHelper.cos(l) * speed;
        }
        else if (MoveUtils4.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils4.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils4.mc.gameSettings.keyBindForward.isPressed() && !MoveUtils4.mc.gameSettings.keyBindBack.isPressed()) {
            final EntityPlayerSP thePlayer31;
            final EntityPlayerSP thePlayer15 = thePlayer31 = MoveUtils4.mc.thePlayer;
            thePlayer31.motionX += MathHelper.sin(r) * speed;
            final EntityPlayerSP thePlayer32;
            final EntityPlayerSP thePlayer16 = thePlayer32 = MoveUtils4.mc.thePlayer;
            thePlayer32.motionZ -= MathHelper.cos(r) * speed;
        }
    }
    
    public static void setMotion(final double speed) {
        double forward = MoveUtils4.mc.thePlayer.movementInput.moveForward;
        double strafe = MoveUtils4.mc.thePlayer.movementInput.moveStrafe;
        float yaw = MoveUtils4.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            MoveUtils4.mc.thePlayer.motionX = 0.0;
            MoveUtils4.mc.thePlayer.motionZ = 0.0;
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
            MoveUtils4.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            MoveUtils4.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }
    
    public static boolean checkTeleport(final double x, final double y, final double z, final double distBetweenPackets) {
        final double distx = MoveUtils4.mc.thePlayer.posX - x;
        final double disty = MoveUtils4.mc.thePlayer.posY - y;
        final double distz = MoveUtils4.mc.thePlayer.posZ - z;
        final double dist = Math.sqrt(MoveUtils4.mc.thePlayer.getDistanceSq(x, y, z));
        final double distanceEntreLesPackets = distBetweenPackets;
        final double nbPackets = (double)(Math.round(dist / distanceEntreLesPackets + 0.49999999999) - 1L);
        double xtp = MoveUtils4.mc.thePlayer.posX;
        double ytp = MoveUtils4.mc.thePlayer.posY;
        double ztp = MoveUtils4.mc.thePlayer.posZ;
        for (int i = 1; i < nbPackets; ++i) {
            final double xdi = (x - MoveUtils4.mc.thePlayer.posX) / nbPackets;
            xtp += xdi;
            final double zdi = (z - MoveUtils4.mc.thePlayer.posZ) / nbPackets;
            ztp += zdi;
            final double ydi = (y - MoveUtils4.mc.thePlayer.posY) / nbPackets;
            ytp += ydi;
            final AxisAlignedBB bb = new AxisAlignedBB(xtp - 0.3, ytp, ztp - 0.3, xtp + 0.3, ytp + 1.8, ztp + 0.3);
            if (!MoveUtils4.mc.theWorld.getCollidingBoundingBoxes(MoveUtils4.mc.thePlayer, bb).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isOnGround(final double height) {
        return !MoveUtils4.mc.theWorld.getCollidingBoundingBoxes(MoveUtils4.mc.thePlayer, MoveUtils4.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
    
    public static int getJumpEffect() {
        if (MoveUtils4.mc.thePlayer.isPotionActive(Potion.jump)) {
            return MoveUtils4.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static int getSpeedEffect() {
        if (MoveUtils4.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return MoveUtils4.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer, final double height) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock();
    }
    
    public static Block getBlockAtPosC(final double x, final double y, final double z) {
        final EntityPlayer inPlayer = Minecraft.getMinecraft().thePlayer;
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(inPlayer.posX + x, inPlayer.posY + y, inPlayer.posZ + z)).getBlock();
    }
    
    public static float getDistanceToGround(final Entity e) {
        if (MoveUtils4.mc.thePlayer.isCollidedVertically && MoveUtils4.mc.thePlayer.onGround) {
            return 0.0f;
        }
        float a = (float)e.posY;
        while (a > 0.0f) {
            final int[] stairs = { 53, 67, 108, 109, 114, 128, 134, 135, 136, 156, 163, 164, 180 };
            final int[] exemptIds = { 6, 27, 28, 30, 31, 32, 37, 38, 39, 40, 50, 51, 55, 59, 63, 65, 66, 68, 69, 70, 72, 75, 76, 77, 83, 92, 93, 94, 104, 105, 106, 115, 119, 131, 132, 143, 147, 148, 149, 150, 157, 171, 175, 176, 177 };
            final Block block = MoveUtils4.mc.theWorld.getBlockState(new BlockPos(e.posX, a - 1.0f, e.posZ)).getBlock();
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
        final double x = block.getX() + 0.5 - MoveUtils4.mc.thePlayer.posX + face.getFrontOffsetX() / 2.0;
        final double z = block.getZ() + 0.5 - MoveUtils4.mc.thePlayer.posZ + face.getFrontOffsetZ() / 2.0;
        final double y = block.getY() + 0.5;
        final double d1 = MoveUtils4.mc.thePlayer.posY + MoveUtils4.mc.thePlayer.getEyeHeight() - y;
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[] { yaw, pitch };
    }
    
    public static boolean isBlockAboveHead() {
        final AxisAlignedBB bb = new AxisAlignedBB(MoveUtils4.mc.thePlayer.posX - 0.3, MoveUtils4.mc.thePlayer.posY + MoveUtils4.mc.thePlayer.getEyeHeight(), MoveUtils4.mc.thePlayer.posZ + 0.3, MoveUtils4.mc.thePlayer.posX + 0.3, MoveUtils4.mc.thePlayer.posY + 2.5, MoveUtils4.mc.thePlayer.posZ - 0.3);
        return !MoveUtils4.mc.theWorld.getCollidingBoundingBoxes(MoveUtils4.mc.thePlayer, bb).isEmpty();
    }
    
    public static boolean isCollidedH(final double dist) {
        final AxisAlignedBB bb = new AxisAlignedBB(MoveUtils4.mc.thePlayer.posX - 0.3, MoveUtils4.mc.thePlayer.posY + 2.0, MoveUtils4.mc.thePlayer.posZ + 0.3, MoveUtils4.mc.thePlayer.posX + 0.3, MoveUtils4.mc.thePlayer.posY + 3.0, MoveUtils4.mc.thePlayer.posZ - 0.3);
        return !MoveUtils4.mc.theWorld.getCollidingBoundingBoxes(MoveUtils4.mc.thePlayer, bb.offset(0.3 + dist, 0.0, 0.0)).isEmpty() || !MoveUtils4.mc.theWorld.getCollidingBoundingBoxes(MoveUtils4.mc.thePlayer, bb.offset(-0.3 - dist, 0.0, 0.0)).isEmpty() || !MoveUtils4.mc.theWorld.getCollidingBoundingBoxes(MoveUtils4.mc.thePlayer, bb.offset(0.0, 0.0, 0.3 + dist)).isEmpty() || !MoveUtils4.mc.theWorld.getCollidingBoundingBoxes(MoveUtils4.mc.thePlayer, bb.offset(0.0, 0.0, -0.3 - dist)).isEmpty();
    }
    
    public static boolean isRealCollidedH(final double dist) {
        final AxisAlignedBB bb = new AxisAlignedBB(MoveUtils4.mc.thePlayer.posX - 0.3, MoveUtils4.mc.thePlayer.posY + 0.5, MoveUtils4.mc.thePlayer.posZ + 0.3, MoveUtils4.mc.thePlayer.posX + 0.3, MoveUtils4.mc.thePlayer.posY + 1.9, MoveUtils4.mc.thePlayer.posZ - 0.3);
        return !MoveUtils4.mc.theWorld.getCollidingBoundingBoxes(MoveUtils4.mc.thePlayer, bb.offset(0.3 + dist, 0.0, 0.0)).isEmpty() || !MoveUtils4.mc.theWorld.getCollidingBoundingBoxes(MoveUtils4.mc.thePlayer, bb.offset(-0.3 - dist, 0.0, 0.0)).isEmpty() || !MoveUtils4.mc.theWorld.getCollidingBoundingBoxes(MoveUtils4.mc.thePlayer, bb.offset(0.0, 0.0, 0.3 + dist)).isEmpty() || !MoveUtils4.mc.theWorld.getCollidingBoundingBoxes(MoveUtils4.mc.thePlayer, bb.offset(0.0, 0.0, -0.3 - dist)).isEmpty();
    }
}
