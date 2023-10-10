package tech.atani.client.utility.world.block;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import tech.atani.client.utility.interfaces.Methods;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BlockUtil implements Methods {

    public static boolean isOkBlock(final BlockPos blockPos) {
        final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
        return !(block instanceof BlockLiquid)
                && !(block instanceof BlockAir)
                && !(block instanceof BlockChest)
                && !(block instanceof BlockFurnace);
    }

    public static BlockPos getAimBlockPos() {
        final BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ);

        if ((mc.gameSettings.keyBindJump.isKeyDown() || !mc.thePlayer.onGround)
                && mc.thePlayer.moveForward == 0.0f
                && mc.thePlayer.moveStrafing == 0.0f
                && isOkBlock(playerPos.add(0, -1, 0))) {
            return playerPos.add(0, -1, 0);
        }

        BlockPos blockPos = null;
        final List<BlockPos> blockPosList = getBlockPos();

        if (!blockPosList.isEmpty()) {
            blockPosList.sort(Comparator.comparingDouble(BlockUtil::getDistanceToBlockPos));
            blockPos = blockPosList.get(0);
        }

        return blockPos;
    }

    public static List<BlockPos> getBlockPos() {
        final BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ);
        final List<BlockPos> blockPosList = new ArrayList<>();

        for (int x = playerPos.getX() - 2; x <= playerPos.getX() + 2; ++x) {
            for (int y = playerPos.getY() - 1; y <= playerPos.getY(); ++y) {
                for (int z = playerPos.getZ() - 2; z <= playerPos.getZ() + 2; ++z) {
                    final BlockPos currentPos = new BlockPos(x, y, z);

                    if (isOkBlock(currentPos)) {
                        blockPosList.add(currentPos);
                    }
                }
            }
        }

        if (!blockPosList.isEmpty()) {
            blockPosList.sort(Comparator.comparingDouble(blockPos -> mc.thePlayer.getDistance(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5)));
        }

        return blockPosList;
    }

    public static double getDistanceToBlockPos(final BlockPos blockPos) {
        double distance = 1337.0;

        for (float x = (float) blockPos.getX(); x <= blockPos.getX() + 1; x += (float) 0.2) {
            for (float y = (float) blockPos.getY(); y <= blockPos.getY() + 1; y += (float) 0.2) {
                for (float z = (float) blockPos.getZ(); z <= blockPos.getZ() + 1; z += (float) 0.2) {
                    final double d0 = mc.thePlayer.getDistance(x, y, z);

                    if (d0 < distance) {
                        distance = d0;
                    }
                }
            }
        }

        return distance;
    }

    public static boolean isBlockUnderPlayer() {
        for (int offset = 0; offset < mc.thePlayer.posY + mc.thePlayer.getEyeHeight(); offset += 2) {

            AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(0.0D, -offset, 0.0D);

            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isCollidingOnGround(BlockPos pos, double size) {
        for(double x = -size; x < size * 2; x += size) {
            for(double z = -size; z < size * 2; z += size) {
                Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z)).getBlock();

                if(block.isFullBlock() && block != Blocks.air) {
                    return true;
                }
            }
        }
        return false;
    }

}
