// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.augustus.utils.interfaces.MC;

public class SigmaPlayerUtils implements MC
{
    public static boolean isInLiquid() {
        if (SigmaPlayerUtils.mc.thePlayer == null) {
            return false;
        }
        for (int x = MathHelper.floor_double(SigmaPlayerUtils.mc.thePlayer.boundingBox.minX); x < MathHelper.floor_double(SigmaPlayerUtils.mc.thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(SigmaPlayerUtils.mc.thePlayer.boundingBox.minZ); z < MathHelper.floor_double(SigmaPlayerUtils.mc.thePlayer.boundingBox.maxZ) + 1; ++z) {
                final BlockPos pos = new BlockPos(x, (int)SigmaPlayerUtils.mc.thePlayer.boundingBox.minY, z);
                final Block block = SigmaPlayerUtils.mc.theWorld.getBlockState(pos).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    return block instanceof BlockLiquid;
                }
            }
        }
        return false;
    }
    
    public static BlockPos getHypixelBlockpos(final String str) {
        int val = 89;
        if (str != null && str.length() > 1) {
            final char[] chs = str.toCharArray();
            for (int lenght = chs.length, i = 0; i < lenght; ++i) {
                val += chs[i] * str.length() * str.length() + str.charAt(0) + str.charAt(1);
            }
            val /= str.length();
        }
        return new BlockPos(val, -val % 255, val);
    }
    
    public static boolean isOnLiquid() {
        AxisAlignedBB boundingBox = SigmaPlayerUtils.mc.thePlayer.getEntityBoundingBox();
        if (boundingBox == null) {
            return false;
        }
        boundingBox = boundingBox.contract(0.01, 0.0, 0.01).offset(0.0, -0.01, 0.0);
        boolean onLiquid = false;
        final int y = (int)boundingBox.minY;
        for (int x = MathHelper.floor_double(boundingBox.minX); x < MathHelper.floor_double(boundingBox.maxX + 1.0); ++x) {
            for (int z = MathHelper.floor_double(boundingBox.minZ); z < MathHelper.floor_double(boundingBox.maxZ + 1.0); ++z) {
                final Block block = SigmaPlayerUtils.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != Blocks.air) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }
    
    public static void blinkToPos(final double[] startPos, final BlockPos endPos, final double slack, final double[] pOffset) {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        try {
            final double endX = endPos.getX() + 0.5;
            final double endY = endPos.getY() + 1.0;
            final double endZ = endPos.getZ() + 0.5;
            double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            int count = 0;
            while (distance > slack) {
                distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
                if (count > 120) {
                    break;
                }
                final boolean next = false;
                final double diffX = curX - endX;
                final double diffY = curY - endY;
                final double diffZ = curZ - endZ;
                final double offset = ((count & 0x1) == 0x0) ? pOffset[0] : pOffset[1];
                if (diffX < 0.0) {
                    if (Math.abs(diffX) > offset) {
                        curX += offset;
                    }
                    else {
                        curX += Math.abs(diffX);
                    }
                }
                if (diffX > 0.0) {
                    if (Math.abs(diffX) > offset) {
                        curX -= offset;
                    }
                    else {
                        curX -= Math.abs(diffX);
                    }
                }
                if (diffY < 0.0) {
                    if (Math.abs(diffY) > 0.25) {
                        curY += 0.25;
                    }
                    else {
                        curY += Math.abs(diffY);
                    }
                }
                if (diffY > 0.0) {
                    if (Math.abs(diffY) > 0.25) {
                        curY -= 0.25;
                    }
                    else {
                        curY -= Math.abs(diffY);
                    }
                }
                if (diffZ < 0.0) {
                    if (Math.abs(diffZ) > offset) {
                        curZ += offset;
                    }
                    else {
                        curZ += Math.abs(diffZ);
                    }
                }
                if (diffZ > 0.0) {
                    if (Math.abs(diffZ) > offset) {
                        curZ -= offset;
                    }
                    else {
                        curZ -= Math.abs(diffZ);
                    }
                }
                Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
                ++count;
            }
        }
        catch (final Exception ex) {}
    }
    
    public static void hypixelTeleport(final double[] startPos, final BlockPos endPos) {
        final double distx = startPos[0] - endPos.getX() + 0.5;
        final double disty = startPos[1] - endPos.getY();
        final double distz = startPos[2] - endPos.getZ() + 0.5;
        final double dist = Math.sqrt(SigmaPlayerUtils.mc.thePlayer.getDistanceSq(endPos));
        final double distanceEntreLesPackets = 0.31 + SigmaMoveUtils.getSpeedEffect() / 20;
        double ztp = 0.0;
        if (dist > distanceEntreLesPackets) {
            final double nbPackets = (double)(Math.round(dist / distanceEntreLesPackets + 0.49999999999) - 1L);
            double xtp = SigmaPlayerUtils.mc.thePlayer.posX;
            double ytp = SigmaPlayerUtils.mc.thePlayer.posY;
            ztp = SigmaPlayerUtils.mc.thePlayer.posZ;
            double count = 0.0;
            for (int i = 1; i < nbPackets; ++i) {
                final double xdi = (endPos.getX() - SigmaPlayerUtils.mc.thePlayer.posX) / nbPackets;
                xtp += xdi;
                final double zdi = (endPos.getZ() - SigmaPlayerUtils.mc.thePlayer.posZ) / nbPackets;
                ztp += zdi;
                final double ydi = (endPos.getY() - SigmaPlayerUtils.mc.thePlayer.posY) / nbPackets;
                ytp += ydi;
                ++count;
                if (!SigmaPlayerUtils.mc.theWorld.getBlockState(new BlockPos(xtp, ytp - 1.0, ztp)).getBlock().isFullBlock()) {
                    if (count <= 2.0) {
                        ytp += 2.0E-8;
                    }
                    else if (count >= 4.0) {
                        count = 0.0;
                    }
                }
                final C03PacketPlayer.C04PacketPlayerPosition Packet = new C03PacketPlayer.C04PacketPlayerPosition(xtp, ytp, ztp, false);
                SigmaPlayerUtils.mc.thePlayer.sendQueue.addToSendQueue(Packet);
            }
            SigmaPlayerUtils.mc.thePlayer.setPosition(endPos.getX() + 0.5, endPos.getY(), endPos.getZ() + 0.5);
        }
        else {
            SigmaPlayerUtils.mc.thePlayer.setPosition(endPos.getX(), endPos.getY(), endPos.getZ());
        }
    }
    
    public static void teleport(final double[] startPos, final BlockPos endPos) {
        final double distx = startPos[0] - endPos.getX() + 0.5;
        final double disty = startPos[1] - endPos.getY();
        final double distz = startPos[2] - endPos.getZ() + 0.5;
        final double dist = Math.sqrt(SigmaPlayerUtils.mc.thePlayer.getDistanceSq(endPos));
        final double distanceEntreLesPackets = 5.0;
        double ztp = 0.0;
        if (dist > distanceEntreLesPackets) {
            final double nbPackets = (double)(Math.round(dist / distanceEntreLesPackets + 0.49999999999) - 1L);
            double xtp = SigmaPlayerUtils.mc.thePlayer.posX;
            double ytp = SigmaPlayerUtils.mc.thePlayer.posY;
            ztp = SigmaPlayerUtils.mc.thePlayer.posZ;
            double count = 0.0;
            for (int i = 1; i < nbPackets; ++i) {
                final double xdi = (endPos.getX() - SigmaPlayerUtils.mc.thePlayer.posX) / nbPackets;
                xtp += xdi;
                final double zdi = (endPos.getZ() - SigmaPlayerUtils.mc.thePlayer.posZ) / nbPackets;
                ztp += zdi;
                final double ydi = (endPos.getY() - SigmaPlayerUtils.mc.thePlayer.posY) / nbPackets;
                ytp += ydi;
                ++count;
                final C03PacketPlayer.C04PacketPlayerPosition Packet = new C03PacketPlayer.C04PacketPlayerPosition(xtp, ytp, ztp, true);
                SigmaPlayerUtils.mc.thePlayer.sendQueue.addToSendQueue(Packet);
            }
            SigmaPlayerUtils.mc.thePlayer.setPosition(endPos.getX() + 0.5, endPos.getY(), endPos.getZ() + 0.5);
        }
        else {
            SigmaPlayerUtils.mc.thePlayer.setPosition(endPos.getX(), endPos.getY(), endPos.getZ());
        }
    }
    
    public static boolean isMoving() {
        return !SigmaPlayerUtils.mc.thePlayer.isCollidedHorizontally && !SigmaPlayerUtils.mc.thePlayer.isSneaking() && (SigmaPlayerUtils.mc.thePlayer.movementInput.moveForward != 0.0f || SigmaPlayerUtils.mc.thePlayer.movementInput.moveStrafe != 0.0f);
    }
    
    public static boolean isMoving2() {
        return SigmaPlayerUtils.mc.thePlayer.moveForward != 0.0f || SigmaPlayerUtils.mc.thePlayer.moveStrafing != 0.0f;
    }
}
