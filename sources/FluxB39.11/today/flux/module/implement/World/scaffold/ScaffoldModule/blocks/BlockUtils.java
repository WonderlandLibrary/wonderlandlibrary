package today.flux.module.implement.World.scaffold.ScaffoldModule.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import java.util.LinkedHashMap;
import java.util.Map;


public final class BlockUtils {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static final Block getBlock(BlockPos blockPos) {
        WorldClient var10000 = mc.theWorld;
        Block var2;
        if (var10000 != null) {
            IBlockState var1 = var10000.getBlockState(blockPos);
            if (var1 != null) {
                var2 = var1.getBlock();
                return var2;
            }
        }

        var2 = null;
        return var2;
    }

    public static final Material getMaterial(BlockPos blockPos) {
        Block var10000 = getBlock(blockPos);
        return var10000 != null ? var10000.getMaterial() : null;
    }

    public static final boolean isReplaceable(BlockPos blockPos) {
        Material var10000 = getMaterial(blockPos);
        return var10000 != null && var10000.isReplaceable();
    }

    public static final IBlockState getState(BlockPos blockPos) {
        IBlockState var10000 = mc.theWorld.getBlockState(blockPos);
        return var10000;
    }

    public static final boolean canBeClicked(BlockPos blockPos) {
        Block var10000 = getBlock(blockPos);
        boolean var2;
        if (var10000 != null && var10000.canCollideCheck(getState(blockPos), false)) {
            WorldClient var1 = mc.theWorld;
            if (var1.getWorldBorder().contains(blockPos)) {
                var2 = true;
                return var2;
            }
        }

        var2 = false;
        return var2;
    }

    public static final String getBlockName(int id) {
        Block var10000 = Block.getBlockById(id);
        String var1 = var10000.getLocalizedName();
        return var1;
    }

    public static final boolean isFullBlock(BlockPos blockPos) {
        Block var10000 = getBlock(blockPos);
        if (var10000 != null) {
            AxisAlignedBB var2 = var10000.getCollisionBoundingBox(mc.theWorld, blockPos, getState(blockPos));
            if (var2 != null) {
                AxisAlignedBB axisAlignedBB = var2;
                return axisAlignedBB.maxX - axisAlignedBB.minX == 1.0D && axisAlignedBB.maxY - axisAlignedBB.minY == 1.0D && axisAlignedBB.maxZ - axisAlignedBB.minZ == 1.0D;
            }
        }

        return false;
    }

    public static final double getCenterDistance(BlockPos blockPos) {
        return mc.thePlayer.getDistance((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D);
    }

    public static final Map searchBlocks(int radius) {
        boolean var2 = false;
        Map blocks = new LinkedHashMap();
        int x = radius;
        int var3 = -radius + 1;
        if (radius >= var3) {
            while (true) {
                int y = radius;
                int var5 = -radius + 1;
                if (radius >= var5) {
                    while (true) {
                        int z = radius;
                        int var7 = -radius + 1;
                        if (radius >= var7) {
                            while (true) {
                                BlockPos blockPos = new BlockPos((int) mc.thePlayer.posX + x, (int) mc.thePlayer.posY + y, (int) mc.thePlayer.posZ + z);
                                Block var10000 = getBlock(blockPos);
                                if (var10000 != null) {
                                    Block block = var10000;
                                    blocks.put(blockPos, block);
                                }

                                if (z == var7) {
                                    break;
                                }

                                --z;
                            }
                        }

                        if (y == var5) {
                            break;
                        }

                        --y;
                    }
                }

                if (x == var3) {
                    break;
                }

                --x;
            }
        }

        return blocks;
    }

    public static final boolean collideBlock(AxisAlignedBB axisAlignedBB, Collidable collide) {
        EntityPlayerSP var10000 = mc.thePlayer;
        int x = MathHelper.floor_double(var10000.getEntityBoundingBox().minX);
        var10000 = mc.thePlayer;

        for (int var3 = MathHelper.floor_double(var10000.getEntityBoundingBox().maxX) + 1; x < var3; ++x) {
            var10000 = mc.thePlayer;
            int z = MathHelper.floor_double(var10000.getEntityBoundingBox().minZ);
            var10000 = mc.thePlayer;

            for (int var5 = MathHelper.floor_double(var10000.getEntityBoundingBox().maxZ) + 1; z < var5; ++z) {
                Block block = getBlock(new BlockPos(x, axisAlignedBB.minY, z));
                if (!collide.collideBlock(block)) {
                    return false;
                }
            }
        }

        return true;
    }


    public static final boolean collideBlockIntersects(AxisAlignedBB axisAlignedBB, Collidable collide) {
        EntityPlayerSP var10000 = mc.thePlayer;
        int x = MathHelper.floor_double(var10000.getEntityBoundingBox().minX);
        var10000 = mc.thePlayer;
        for (int var3 = MathHelper.floor_double(var10000.getEntityBoundingBox().maxX) + 1; x < var3; ++x) {
            var10000 = mc.thePlayer;
            int z = MathHelper.floor_double(var10000.getEntityBoundingBox().minZ);
            var10000 = mc.thePlayer;
            for (int var5 = MathHelper.floor_double(var10000.getEntityBoundingBox().maxZ) + 1; z < var5; ++z) {
                BlockPos blockPos = new BlockPos(x, axisAlignedBB.minY, z);
                Block block = getBlock(blockPos);
                if (collide.collideBlock(block) && block != null) {
                    AxisAlignedBB var9 = block.getCollisionBoundingBox(mc.theWorld, blockPos, getState(blockPos));
                    if (var9 != null) {
                        AxisAlignedBB boundingBox = var9;
                        var10000 = mc.thePlayer;
                        if (var10000.getEntityBoundingBox().intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    interface Collidable {
        boolean collideBlock(Block block);
    }

}
