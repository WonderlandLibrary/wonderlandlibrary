// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySkeleton;
import org.apache.logging.log4j.LogManager;
import net.minecraft.tileentity.TileEntity;
import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class WorldGenDungeons extends WorldGenerator
{
    private static final Logger LOGGER;
    private static final ResourceLocation[] SPAWNERTYPES;
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        final int i = 3;
        final int j = rand.nextInt(2) + 2;
        final int k = -j - 1;
        final int l = j + 1;
        final int i2 = -1;
        final int j2 = 4;
        final int k2 = rand.nextInt(2) + 2;
        final int l2 = -k2 - 1;
        final int i3 = k2 + 1;
        int j3 = 0;
        for (int k3 = k; k3 <= l; ++k3) {
            for (int l3 = -1; l3 <= 4; ++l3) {
                for (int i4 = l2; i4 <= i3; ++i4) {
                    final BlockPos blockpos = position.add(k3, l3, i4);
                    final Material material = worldIn.getBlockState(blockpos).getMaterial();
                    final boolean flag = material.isSolid();
                    if (l3 == -1 && !flag) {
                        return false;
                    }
                    if (l3 == 4 && !flag) {
                        return false;
                    }
                    if ((k3 == k || k3 == l || i4 == l2 || i4 == i3) && l3 == 0 && worldIn.isAirBlock(blockpos) && worldIn.isAirBlock(blockpos.up())) {
                        ++j3;
                    }
                }
            }
        }
        if (j3 >= 1 && j3 <= 5) {
            for (int k4 = k; k4 <= l; ++k4) {
                for (int i5 = 3; i5 >= -1; --i5) {
                    for (int k5 = l2; k5 <= i3; ++k5) {
                        final BlockPos blockpos2 = position.add(k4, i5, k5);
                        if (k4 != k && i5 != -1 && k5 != l2 && k4 != l && i5 != 4 && k5 != i3) {
                            if (worldIn.getBlockState(blockpos2).getBlock() != Blocks.CHEST) {
                                worldIn.setBlockToAir(blockpos2);
                            }
                        }
                        else if (blockpos2.getY() >= 0 && !worldIn.getBlockState(blockpos2.down()).getMaterial().isSolid()) {
                            worldIn.setBlockToAir(blockpos2);
                        }
                        else if (worldIn.getBlockState(blockpos2).getMaterial().isSolid() && worldIn.getBlockState(blockpos2).getBlock() != Blocks.CHEST) {
                            if (i5 == -1 && rand.nextInt(4) != 0) {
                                worldIn.setBlockState(blockpos2, Blocks.MOSSY_COBBLESTONE.getDefaultState(), 2);
                            }
                            else {
                                worldIn.setBlockState(blockpos2, Blocks.COBBLESTONE.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }
            for (int l4 = 0; l4 < 2; ++l4) {
                for (int j4 = 0; j4 < 3; ++j4) {
                    final int l5 = position.getX() + rand.nextInt(j * 2 + 1) - j;
                    final int i6 = position.getY();
                    final int j5 = position.getZ() + rand.nextInt(k2 * 2 + 1) - k2;
                    final BlockPos blockpos3 = new BlockPos(l5, i6, j5);
                    if (worldIn.isAirBlock(blockpos3)) {
                        int j6 = 0;
                        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                            if (worldIn.getBlockState(blockpos3.offset(enumfacing)).getMaterial().isSolid()) {
                                ++j6;
                            }
                        }
                        if (j6 == 1) {
                            worldIn.setBlockState(blockpos3, Blocks.CHEST.correctFacing(worldIn, blockpos3, Blocks.CHEST.getDefaultState()), 2);
                            final TileEntity tileentity1 = worldIn.getTileEntity(blockpos3);
                            if (tileentity1 instanceof TileEntityChest) {
                                ((TileEntityChest)tileentity1).setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON, rand.nextLong());
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            worldIn.setBlockState(position, Blocks.MOB_SPAWNER.getDefaultState(), 2);
            final TileEntity tileentity2 = worldIn.getTileEntity(position);
            if (tileentity2 instanceof TileEntityMobSpawner) {
                ((TileEntityMobSpawner)tileentity2).getSpawnerBaseLogic().setEntityId(this.pickMobSpawner(rand));
            }
            else {
                WorldGenDungeons.LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", (Object)position.getX(), (Object)position.getY(), (Object)position.getZ());
            }
            return true;
        }
        return false;
    }
    
    private ResourceLocation pickMobSpawner(final Random rand) {
        return WorldGenDungeons.SPAWNERTYPES[rand.nextInt(WorldGenDungeons.SPAWNERTYPES.length)];
    }
    
    static {
        LOGGER = LogManager.getLogger();
        SPAWNERTYPES = new ResourceLocation[] { EntityList.getKey(EntitySkeleton.class), EntityList.getKey(EntityZombie.class), EntityList.getKey(EntityZombie.class), EntityList.getKey(EntitySpider.class) };
    }
}
