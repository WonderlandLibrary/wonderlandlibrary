/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldGenDungeons
extends WorldGenerator {
    private static final List<WeightedRandomChestContent> CHESTCONTENT;
    private static final String[] SPAWNERTYPES;
    private static final Logger field_175918_a;

    static {
        field_175918_a = LogManager.getLogger();
        SPAWNERTYPES = new String[]{"Skeleton", "Zombie", "Zombie", "Spider"};
        CHESTCONTENT = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 10), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 4, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 1, 10), new WeightedRandomChestContent(Items.wheat, 0, 1, 4, 10), new WeightedRandomChestContent(Items.gunpowder, 0, 1, 4, 10), new WeightedRandomChestContent(Items.string, 0, 1, 4, 10), new WeightedRandomChestContent(Items.bucket, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1), new WeightedRandomChestContent(Items.redstone, 0, 1, 4, 10), new WeightedRandomChestContent(Items.record_13, 0, 1, 1, 4), new WeightedRandomChestContent(Items.record_cat, 0, 1, 1, 4), new WeightedRandomChestContent(Items.name_tag, 0, 1, 1, 10), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 2), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 5), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)});
    }

    private String pickMobSpawner(Random random) {
        return SPAWNERTYPES[random.nextInt(SPAWNERTYPES.length)];
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        BlockPos blockPos2;
        int n;
        int n2;
        int n3 = 3;
        int n4 = random.nextInt(2) + 2;
        int n5 = -n4 - 1;
        int n6 = n4 + 1;
        int n7 = -1;
        int n8 = 4;
        int n9 = random.nextInt(2) + 2;
        int n10 = -n9 - 1;
        int n11 = n9 + 1;
        int n12 = 0;
        int n13 = n5;
        while (n13 <= n6) {
            n2 = -1;
            while (n2 <= 4) {
                n = n10;
                while (n <= n11) {
                    blockPos2 = blockPos.add(n13, n2, n);
                    Material material = world.getBlockState(blockPos2).getBlock().getMaterial();
                    boolean bl = material.isSolid();
                    if (n2 == -1 && !bl) {
                        return false;
                    }
                    if (n2 == 4 && !bl) {
                        return false;
                    }
                    if ((n13 == n5 || n13 == n6 || n == n10 || n == n11) && n2 == 0 && world.isAirBlock(blockPos2) && world.isAirBlock(blockPos2.up())) {
                        ++n12;
                    }
                    ++n;
                }
                ++n2;
            }
            ++n13;
        }
        if (n12 >= 1 && n12 <= 5) {
            n13 = n5;
            while (n13 <= n6) {
                n2 = 3;
                while (n2 >= -1) {
                    n = n10;
                    while (n <= n11) {
                        blockPos2 = blockPos.add(n13, n2, n);
                        if (n13 != n5 && n2 != -1 && n != n10 && n13 != n6 && n2 != 4 && n != n11) {
                            if (world.getBlockState(blockPos2).getBlock() != Blocks.chest) {
                                world.setBlockToAir(blockPos2);
                            }
                        } else if (blockPos2.getY() >= 0 && !world.getBlockState(blockPos2.down()).getBlock().getMaterial().isSolid()) {
                            world.setBlockToAir(blockPos2);
                        } else if (world.getBlockState(blockPos2).getBlock().getMaterial().isSolid() && world.getBlockState(blockPos2).getBlock() != Blocks.chest) {
                            if (n2 == -1 && random.nextInt(4) != 0) {
                                world.setBlockState(blockPos2, Blocks.mossy_cobblestone.getDefaultState(), 2);
                            } else {
                                world.setBlockState(blockPos2, Blocks.cobblestone.getDefaultState(), 2);
                            }
                        }
                        ++n;
                    }
                    --n2;
                }
                ++n13;
            }
            n13 = 0;
            while (n13 < 2) {
                n2 = 0;
                while (n2 < 3) {
                    int n14;
                    int n15;
                    n = blockPos.getX() + random.nextInt(n4 * 2 + 1) - n4;
                    BlockPos blockPos3 = new BlockPos(n, n15 = blockPos.getY(), n14 = blockPos.getZ() + random.nextInt(n9 * 2 + 1) - n9);
                    if (world.isAirBlock(blockPos3)) {
                        int n16 = 0;
                        for (Object object : EnumFacing.Plane.HORIZONTAL) {
                            if (!world.getBlockState(blockPos3.offset((EnumFacing)object)).getBlock().getMaterial().isSolid()) continue;
                            ++n16;
                        }
                        if (n16 == 1) {
                            Object object;
                            world.setBlockState(blockPos3, Blocks.chest.correctFacing(world, blockPos3, Blocks.chest.getDefaultState()), 2);
                            object = WeightedRandomChestContent.func_177629_a(CHESTCONTENT, Items.enchanted_book.getRandom(random));
                            TileEntity tileEntity = world.getTileEntity(blockPos3);
                            if (!(tileEntity instanceof TileEntityChest)) break;
                            WeightedRandomChestContent.generateChestContents(random, (List<WeightedRandomChestContent>)object, (TileEntityChest)tileEntity, 8);
                            break;
                        }
                    }
                    ++n2;
                }
                ++n13;
            }
            world.setBlockState(blockPos, Blocks.mob_spawner.getDefaultState(), 2);
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (tileEntity instanceof TileEntityMobSpawner) {
                ((TileEntityMobSpawner)tileEntity).getSpawnerBaseLogic().setEntityName(this.pickMobSpawner(random));
            } else {
                field_175918_a.error("Failed to fetch mob spawner entity at (" + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ() + ")");
            }
            return true;
        }
        return false;
    }
}

