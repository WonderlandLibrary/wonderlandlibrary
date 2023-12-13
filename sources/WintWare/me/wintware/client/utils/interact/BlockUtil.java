/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.utils.interact;

import java.util.ArrayList;
import java.util.Objects;
import me.wintware.client.utils.other.MinecraftHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class BlockUtil
implements MinecraftHelper {
    public static boolean collideBlock(AxisAlignedBB axisAlignedBB, Collidable collide) {
        for (int x = MathHelper.floor(Minecraft.player.getEntityBoundingBox().minX); x < MathHelper.floor(Minecraft.player.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor(Minecraft.player.getEntityBoundingBox().minZ); z < MathHelper.floor(Minecraft.player.getEntityBoundingBox().maxZ) + 1; ++z) {
                Block block = BlockUtil.getBlock(new BlockPos(x, axisAlignedBB.minY, z));
                if (collide.collideBlock(block)) continue;
                return false;
            }
        }
        return true;
    }

    public static float getHorizontalPlayerBlockDistance(BlockPos blockPos) {
        float xDiff = (float)(Minecraft.player.posX - (double)blockPos.getX());
        float zDiff = (float)(Minecraft.player.posZ - (double)blockPos.getZ());
        return MathHelper.sqrt((xDiff - 0.5f) * (xDiff - 0.5f) + (zDiff - 0.5f) * (zDiff - 0.5f));
    }

    public static boolean isOnIce(Entity entity) {
        if (entity == null) {
            return false;
        }
        boolean onIce = false;
        int y = (int)entity.getEntityBoundingBox().offset(0.0, -0.01, 0.0).minY;
        for (int x = MathHelper.floor(entity.getEntityBoundingBox().minX); x < MathHelper.floor(entity.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor(entity.getEntityBoundingBox().minZ); z < MathHelper.floor(entity.getEntityBoundingBox().maxZ) + 1; ++z) {
                Block block = BlockUtil.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block == null || block instanceof BlockAir) continue;
                if (!(block instanceof BlockIce) && !(block instanceof BlockPackedIce)) {
                    return false;
                }
                onIce = true;
            }
        }
        return onIce;
    }

    public static boolean isOnLadder(Entity entity) {
        if (entity == null) {
            return false;
        }
        boolean onLadder = false;
        int y = (int)entity.getEntityBoundingBox().offset(0.0, 1.0, 0.0).minY;
        for (int x = MathHelper.floor(entity.getEntityBoundingBox().minX); x < MathHelper.floor(entity.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor(entity.getEntityBoundingBox().minZ); z < MathHelper.floor(entity.getEntityBoundingBox().maxZ) + 1; ++z) {
                Block block = BlockUtil.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (!Objects.nonNull(block) || block instanceof BlockAir) continue;
                if (!(block instanceof BlockLadder) && !(block instanceof BlockVine)) {
                    return false;
                }
                onLadder = true;
            }
        }
        return onLadder || Minecraft.player.isOnLadder();
    }

    public static boolean isOnLiquid(double profondeur) {
        return BlockUtil.mc.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY - profondeur, Minecraft.player.posZ)).getBlock().getMaterial(null).isLiquid();
    }

    public static boolean isTotalOnLiquid(double profondeur) {
        for (double x = Minecraft.player.boundingBox.minX; x < Minecraft.player.boundingBox.maxX; x += 0.01f) {
            for (double z = Minecraft.player.boundingBox.minZ; z < Minecraft.player.boundingBox.maxZ; z += 0.01f) {
                Block block = BlockUtil.mc.world.getBlockState(new BlockPos(x, Minecraft.player.posY - profondeur, z)).getBlock();
                if (block instanceof BlockLiquid || block instanceof BlockAir) continue;
                return false;
            }
        }
        return true;
    }

    public static ArrayList<BlockPos> getBlocks(int x, int y, int z) {
        BlockPos min = new BlockPos(Minecraft.player.posX - (double)x, Minecraft.player.posY - (double)y, Minecraft.player.posZ - (double)z);
        BlockPos max = new BlockPos(Minecraft.player.posX + (double)x, Minecraft.player.posY + (double)y, Minecraft.player.posZ + (double)z);
        return BlockUtil.getAllInBox(min, max);
    }

    public static Block getBlock(BlockPos pos) {
        return BlockUtil.getState(pos).getBlock();
    }

    public static IBlockState getState(BlockPos pos) {
        return BlockUtil.mc.world.getBlockState(pos);
    }

    public static ArrayList<BlockPos> getAllInBox(BlockPos from, BlockPos to) {
        ArrayList<BlockPos> blocks = new ArrayList<BlockPos>();
        BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        for (int x = min.getX(); x <= max.getX(); ++x) {
            for (int y = min.getY(); y <= max.getY(); ++y) {
                for (int z = min.getZ(); z <= max.getZ(); ++z) {
                    blocks.add(new BlockPos(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static Block getBlock(int x, int y, int z) {
        return BlockUtil.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public static boolean canSeeBlock(float x, float y, float z) {
        return BlockUtil.getFacing(new BlockPos(x, y, z)) != null;
    }

    public static EnumFacing getFacing(BlockPos pos) {
        EnumFacing[] orderedValues;
        EnumFacing[] var2 = orderedValues = new EnumFacing[]{EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.DOWN};
        int var3 = orderedValues.length;
        for (int var4 = 0; var4 < var3; ++var4) {
            EnumFacing facing = var2[var4];
            EntitySnowball temp = new EntitySnowball(BlockUtil.mc.world);
            temp.posX = (double)pos.getX() + 0.5;
            temp.posY = (double)pos.getY() + 0.5;
            temp.posZ = (double)pos.getZ() + 0.5;
            temp.posX += (double)facing.getDirectionVec().getX() * 0.5;
            temp.posY += (double)facing.getDirectionVec().getY() * 0.5;
            temp.posZ += (double)facing.getDirectionVec().getZ() * 0.5;
            if (!Minecraft.player.canEntityBeSeen(temp)) continue;
            return facing;
        }
        return null;
    }

    public interface Collidable {
        boolean collideBlock(Block var1);
    }
}

