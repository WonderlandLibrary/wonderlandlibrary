// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.util;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class PlayerUtil implements MinecraftUtil
{
    public static boolean isInLiquid() {
        if (PlayerUtil.mc.thePlayer == null || PlayerUtil.mc.theWorld == null) {
            return false;
        }
        final AxisAlignedBB boundingBox = PlayerUtil.mc.thePlayer.getEntityBoundingBox();
        if (boundingBox == null) {
            return false;
        }
        final int x1 = MathHelper.floor_double(boundingBox.minX);
        final int x2 = MathHelper.floor_double(boundingBox.maxX + 1.0);
        final int y1 = MathHelper.floor_double(boundingBox.minY);
        final int y2 = MathHelper.floor_double(boundingBox.maxY + 1.0);
        final int z1 = MathHelper.floor_double(boundingBox.minZ);
        final int z2 = MathHelper.floor_double(boundingBox.maxZ + 1.0);
        if (PlayerUtil.mc.theWorld.getChunkFromChunkCoords((int)PlayerUtil.mc.thePlayer.posX >> 4, (int)PlayerUtil.mc.thePlayer.posZ >> 4) == null) {
            return false;
        }
        for (int x3 = x1; x3 < x2; ++x3) {
            for (int y3 = y1; y3 < y2; ++y3) {
                for (int z3 = z1; z3 < z2; ++z3) {
                    final Block block = PlayerUtil.mc.theWorld.getBlockState(new BlockPos(x3, y3, z3)).getBlock();
                    if (block instanceof BlockLiquid) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isOnLiquid() {
        AxisAlignedBB boundingBox = PlayerUtil.mc.thePlayer.getEntityBoundingBox();
        if (boundingBox == null) {
            return false;
        }
        boundingBox = boundingBox.contract(0.01, 0.0, 0.01).offset(0.0, -0.01, 0.0);
        boolean onLiquid = false;
        final int y = (int)boundingBox.minY;
        for (int x = MathHelper.floor_double(boundingBox.minX); x < MathHelper.floor_double(boundingBox.maxX + 1.0); ++x) {
            for (int z = MathHelper.floor_double(boundingBox.minZ); z < MathHelper.floor_double(boundingBox.maxZ + 1.0); ++z) {
                final Block block = PlayerUtil.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
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
        catch (Exception ex) {}
    }
    
    public static boolean isMoving() {
        return PlayerUtil.mc.gameSettings.keyBindForward.isPressed() || PlayerUtil.mc.gameSettings.keyBindBack.isPressed() || PlayerUtil.mc.gameSettings.keyBindLeft.isPressed() || PlayerUtil.mc.gameSettings.keyBindRight.isPressed() || PlayerUtil.mc.thePlayer.movementInput.moveForward != 0.0f || PlayerUtil.mc.thePlayer.movementInput.moveStrafe != 0.0f;
    }
}
