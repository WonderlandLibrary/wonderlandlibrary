/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBigMushroom
extends WorldGenerator {
    private Block mushroomType;

    public WorldGenBigMushroom() {
        super(false);
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        block38: {
            int n;
            int n2;
            int n3;
            if (this.mushroomType == null) {
                this.mushroomType = random.nextBoolean() ? Blocks.brown_mushroom_block : Blocks.red_mushroom_block;
            }
            int n4 = random.nextInt(3) + 4;
            boolean bl = true;
            if (blockPos.getY() < 1 || blockPos.getY() + n4 + 1 >= 256) break block38;
            int n5 = blockPos.getY();
            while (n5 <= blockPos.getY() + 1 + n4) {
                n3 = 3;
                if (n5 <= blockPos.getY() + 3) {
                    n3 = 0;
                }
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                n2 = blockPos.getX() - n3;
                while (n2 <= blockPos.getX() + n3 && bl) {
                    n = blockPos.getZ() - n3;
                    while (n <= blockPos.getZ() + n3 && bl) {
                        if (n5 >= 0 && n5 < 256) {
                            Block block = world.getBlockState(mutableBlockPos.func_181079_c(n2, n5, n)).getBlock();
                            if (block.getMaterial() != Material.air && block.getMaterial() != Material.leaves) {
                                bl = false;
                            }
                        } else {
                            bl = false;
                        }
                        ++n;
                    }
                    ++n2;
                }
                ++n5;
            }
            if (!bl) {
                return false;
            }
            Block block = world.getBlockState(blockPos.down()).getBlock();
            if (block != Blocks.dirt && block != Blocks.grass && block != Blocks.mycelium) {
                return false;
            }
            n3 = blockPos.getY() + n4;
            if (this.mushroomType == Blocks.red_mushroom_block) {
                n3 = blockPos.getY() + n4 - 3;
            }
            int n6 = n3;
            while (n6 <= blockPos.getY() + n4) {
                n2 = 1;
                if (n6 < blockPos.getY() + n4) {
                    ++n2;
                }
                if (this.mushroomType == Blocks.brown_mushroom_block) {
                    n2 = 3;
                }
                n = blockPos.getX() - n2;
                int n7 = blockPos.getX() + n2;
                int n8 = blockPos.getZ() - n2;
                int n9 = blockPos.getZ() + n2;
                int n10 = n;
                while (n10 <= n7) {
                    int n11 = n8;
                    while (n11 <= n9) {
                        block40: {
                            BlockPos blockPos2;
                            BlockHugeMushroom.EnumType enumType;
                            block39: {
                                int n12 = 5;
                                if (n10 == n) {
                                    --n12;
                                } else if (n10 == n7) {
                                    ++n12;
                                }
                                if (n11 == n8) {
                                    n12 -= 3;
                                } else if (n11 == n9) {
                                    n12 += 3;
                                }
                                enumType = BlockHugeMushroom.EnumType.byMetadata(n12);
                                if (this.mushroomType != Blocks.brown_mushroom_block && n6 >= blockPos.getY() + n4) break block39;
                                if ((n10 == n || n10 == n7) && (n11 == n8 || n11 == n9)) break block40;
                                if (n10 == blockPos.getX() - (n2 - 1) && n11 == n8) {
                                    enumType = BlockHugeMushroom.EnumType.NORTH_WEST;
                                }
                                if (n10 == n && n11 == blockPos.getZ() - (n2 - 1)) {
                                    enumType = BlockHugeMushroom.EnumType.NORTH_WEST;
                                }
                                if (n10 == blockPos.getX() + (n2 - 1) && n11 == n8) {
                                    enumType = BlockHugeMushroom.EnumType.NORTH_EAST;
                                }
                                if (n10 == n7 && n11 == blockPos.getZ() - (n2 - 1)) {
                                    enumType = BlockHugeMushroom.EnumType.NORTH_EAST;
                                }
                                if (n10 == blockPos.getX() - (n2 - 1) && n11 == n9) {
                                    enumType = BlockHugeMushroom.EnumType.SOUTH_WEST;
                                }
                                if (n10 == n && n11 == blockPos.getZ() + (n2 - 1)) {
                                    enumType = BlockHugeMushroom.EnumType.SOUTH_WEST;
                                }
                                if (n10 == blockPos.getX() + (n2 - 1) && n11 == n9) {
                                    enumType = BlockHugeMushroom.EnumType.SOUTH_EAST;
                                }
                                if (n10 == n7 && n11 == blockPos.getZ() + (n2 - 1)) {
                                    enumType = BlockHugeMushroom.EnumType.SOUTH_EAST;
                                }
                            }
                            if (enumType == BlockHugeMushroom.EnumType.CENTER && n6 < blockPos.getY() + n4) {
                                enumType = BlockHugeMushroom.EnumType.ALL_INSIDE;
                            }
                            if (!(blockPos.getY() < blockPos.getY() + n4 - 1 && enumType == BlockHugeMushroom.EnumType.ALL_INSIDE || world.getBlockState(blockPos2 = new BlockPos(n10, n6, n11)).getBlock().isFullBlock())) {
                                this.setBlockAndNotifyAdequately(world, blockPos2, this.mushroomType.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, enumType));
                            }
                        }
                        ++n11;
                    }
                    ++n10;
                }
                ++n6;
            }
            n6 = 0;
            while (n6 < n4) {
                Block block2 = world.getBlockState(blockPos.up(n6)).getBlock();
                if (!block2.isFullBlock()) {
                    this.setBlockAndNotifyAdequately(world, blockPos.up(n6), this.mushroomType.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM));
                }
                ++n6;
            }
            return true;
        }
        return false;
    }

    public WorldGenBigMushroom(Block block) {
        super(true);
        this.mushroomType = block;
    }
}

