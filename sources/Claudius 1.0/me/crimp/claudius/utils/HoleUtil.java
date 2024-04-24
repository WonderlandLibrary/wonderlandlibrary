package me.crimp.claudius.utils;

import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HoleUtil implements Globals {

    public static boolean isInHole(EntityPlayer player) {
        return !BlockUtil.getBlock(new BlockPos(player.posX + 1, player.posY, player.posZ)).equals(Blocks.AIR) && !BlockUtil.getBlock(new BlockPos(player.posX - 1, player.posY, player.posZ)).equals(Blocks.AIR) && !BlockUtil.getBlock(new BlockPos(player.posX, player.posY, player.posZ + 1)).equals(Blocks.AIR) && !BlockUtil.getBlock(new BlockPos(player.posX, player.posY, player.posZ - 1)).equals(Blocks.AIR) && player.posY == new BlockPos(player.posX, Math.floor(player.posY), player.posZ).getY();
    }

    public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<>();
        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();

        for (int x = cx - (int) r; (float) x <= (float) cx + r; ++x) {
            int z = cz - (int) r;

            while ((float) z <= (float) cz + r) {
                int y = sphere ? cy - (int) r : cy;

                while (true) {
                    float f = sphere ? (float) cy + r : (float) (cy + h);

                    if ((float) y >= f) {
                        ++z;
                        break;
                    }

                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);

                    if (dist < (double) (r * r) && (!hollow || dist >= (double) ((r - 1.0F) * (r - 1.0F)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);

                        if (!l.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)) && (isBedrock(l) || isObby(l))) {
                            circleblocks.add(l);

                            for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(l))) {
                                if (entity instanceof EntityLivingBase) {
                                    circleblocks.remove(l);
                                }
                            }
                        }
                    }

                    ++y;
                }
            }
        }

        return circleblocks;
    }

    public static List<BlockPos> findObbyHoles(float range) {
        NonNullList positions = NonNullList.create();
        positions.addAll(getSphere(mc.player.getPosition(), range, ((int) range), false, true, 0).stream().filter(HoleUtil::isObby).collect(Collectors.toList()));
        return positions;
    }

    public static List<BlockPos> findBRockHoles(float range) {
        NonNullList positions = NonNullList.create();
        positions.addAll(getSphere(mc.player.getPosition(), range, ((int) range), false, true, 0).stream().filter(HoleUtil::isBedrock).collect(Collectors.toList()));
        return positions;
    }

    public static boolean isBedrock(BlockPos blockPos) {
        boolean air = mc.world.getBlockState(blockPos).getBlock() instanceof BlockAir && mc.world.getBlockState(blockPos.up()).getBlock() instanceof BlockAir && mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() instanceof BlockAir;
        boolean down = mc.world.getBlockState(blockPos.down()).getBlock() == Blocks.BEDROCK;
        boolean north = mc.world.getBlockState(blockPos.north()).getBlock() == Blocks.BEDROCK;
        boolean south = mc.world.getBlockState(blockPos.south()).getBlock() == Blocks.BEDROCK;
        boolean west = mc.world.getBlockState(blockPos.west()).getBlock() == Blocks.BEDROCK;
        boolean east = mc.world.getBlockState(blockPos.east()).getBlock() == Blocks.BEDROCK;

        return air && north && south && west && east && down;
    }

    public static boolean isObby(BlockPos blockPos) {
        boolean air = mc.world.getBlockState(blockPos).getBlock() instanceof BlockAir && mc.world.getBlockState(blockPos.up()).getBlock() instanceof BlockAir && mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock() instanceof BlockAir;
        boolean down = mc.world.getBlockState(blockPos.down()).getBlock() == Blocks.BEDROCK || obbyOrEchest(blockPos.down());
        boolean north = mc.world.getBlockState(blockPos.north()).getBlock() == Blocks.BEDROCK || obbyOrEchest(blockPos.north());
        boolean south = mc.world.getBlockState(blockPos.south()).getBlock() == Blocks.BEDROCK || obbyOrEchest(blockPos.south());
        boolean west = mc.world.getBlockState(blockPos.west()).getBlock() == Blocks.BEDROCK || obbyOrEchest(blockPos.west());
        boolean east = mc.world.getBlockState(blockPos.east()).getBlock() == Blocks.BEDROCK || obbyOrEchest(blockPos.east());

        return air && north && south && west && east && down;
    }

    private static boolean obbyOrEchest(BlockPos blockPos) {
        return mc.world != null && (mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST);
    }

    public static List<BlockPos> getAllHoles(float range) {
        NonNullList positions = NonNullList.create();
        positions.addAll(getSphere(mc.player.getPosition(), range, ((int) range), false, true, 0).stream().filter(HoleUtil::isBedrock).collect(Collectors.toList()));
        positions.addAll(getSphere(mc.player.getPosition(), range, ((int) range), false, true, 0).stream().filter(HoleUtil::isObby).collect(Collectors.toList()));
        return positions;
    }

    public static BlockPos getClosestHole(EntityPlayer player, float maxDistance) { //set maxDistance to the chunk amount * 16 if you dont want a specific range

        BlockPos closestHole = null;

        if (HoleUtil.isInHole(player)) return player.getPosition();

        if (mc.world.playerEntities.isEmpty()) return null;

        for (final BlockPos pos : getAllHoles(maxDistance)) {

            if (closestHole != null) {
                if (player.getDistance(pos.getX(), pos.getY(), pos.getZ()) > player.getDistance(closestHole.getX(), closestHole.getY(), closestHole.getZ())) {
                    continue;
                }
            }

            closestHole = pos;
        }
        return closestHole;
    }

}
