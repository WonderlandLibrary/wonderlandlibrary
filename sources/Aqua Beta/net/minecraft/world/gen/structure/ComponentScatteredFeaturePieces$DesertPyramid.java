package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public static class ComponentScatteredFeaturePieces.DesertPyramid
extends ComponentScatteredFeaturePieces.Feature {
    private boolean[] hasPlacedChest = new boolean[4];
    private static final List<WeightedRandomChestContent> itemsToGenerateInTemple = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15), new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2), new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20), new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)});

    public ComponentScatteredFeaturePieces.DesertPyramid() {
    }

    public ComponentScatteredFeaturePieces.DesertPyramid(Random p_i2062_1_, int p_i2062_2_, int p_i2062_3_) {
        super(p_i2062_1_, p_i2062_2_, 64, p_i2062_3_, 21, 15, 21);
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setBoolean("hasPlacedChest0", this.hasPlacedChest[0]);
        tagCompound.setBoolean("hasPlacedChest1", this.hasPlacedChest[1]);
        tagCompound.setBoolean("hasPlacedChest2", this.hasPlacedChest[2]);
        tagCompound.setBoolean("hasPlacedChest3", this.hasPlacedChest[3]);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.hasPlacedChest[0] = tagCompound.getBoolean("hasPlacedChest0");
        this.hasPlacedChest[1] = tagCompound.getBoolean("hasPlacedChest1");
        this.hasPlacedChest[2] = tagCompound.getBoolean("hasPlacedChest2");
        this.hasPlacedChest[3] = tagCompound.getBoolean("hasPlacedChest3");
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0, this.scatteredFeatureSizeZ - 1, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        for (int i = 1; i <= 9; ++i) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, i, i, i, this.scatteredFeatureSizeX - 1 - i, i, this.scatteredFeatureSizeZ - 1 - i, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, i + 1, i, i + 1, this.scatteredFeatureSizeX - 2 - i, i, this.scatteredFeatureSizeZ - 2 - i, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        }
        for (int j2 = 0; j2 < this.scatteredFeatureSizeX; ++j2) {
            for (int j = 0; j < this.scatteredFeatureSizeZ; ++j) {
                int k = -5;
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.sandstone.getDefaultState(), j2, k, j, structureBoundingBoxIn);
            }
        }
        int k2 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 3);
        int l2 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 2);
        int i3 = this.getMetadataWithOffset(Blocks.sandstone_stairs, 0);
        int l = this.getMetadataWithOffset(Blocks.sandstone_stairs, 1);
        int i1 = ~EnumDyeColor.ORANGE.getDyeDamage() & 0xF;
        int j1 = ~EnumDyeColor.BLUE.getDyeDamage() & 0xF;
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 9, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 10, 1, 3, 10, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), 2, 10, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(l2), 2, 10, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i3), 0, 10, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(l), 4, 10, 2, structureBoundingBoxIn);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 5, 0, 0, this.scatteredFeatureSizeX - 1, 9, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 4, 10, 1, this.scatteredFeatureSizeX - 2, 10, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), this.scatteredFeatureSizeX - 3, 10, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(l2), this.scatteredFeatureSizeX - 3, 10, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i3), this.scatteredFeatureSizeX - 5, 10, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(l), this.scatteredFeatureSizeX - 1, 10, 2, structureBoundingBoxIn);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 0, 12, 4, 4, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 0, 11, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 1, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 2, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 9, 3, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, 3, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 3, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 2, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 11, 1, 1, structureBoundingBoxIn);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 8, 3, 3, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 2, 8, 2, 2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 1, 16, 3, 3, Blocks.sandstone.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 2, 16, 2, 2, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 5, this.scatteredFeatureSizeX - 6, 4, this.scatteredFeatureSizeZ - 6, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 4, 9, 11, 4, 11, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 8, 8, 3, 8, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 8, 12, 3, 8, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 12, 8, 3, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 12, 12, 3, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 5, 4, 4, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 5, 1, 5, this.scatteredFeatureSizeX - 2, 4, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 7, 9, 6, 7, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 7, 7, 9, this.scatteredFeatureSizeX - 7, 7, 11, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 9, 5, 7, 11, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 6, 5, 9, this.scatteredFeatureSizeX - 6, 7, 11, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 5, 5, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 5, 6, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 6, 6, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 6, 5, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 6, 6, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), this.scatteredFeatureSizeX - 7, 6, 10, structureBoundingBoxIn);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 4, 2, 6, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 3, 4, 4, this.scatteredFeatureSizeX - 3, 6, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), 2, 4, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), 2, 3, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), this.scatteredFeatureSizeX - 3, 4, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(k2), this.scatteredFeatureSizeX - 3, 3, 4, structureBoundingBoxIn);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 3, 2, 2, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 3, 1, 3, this.scatteredFeatureSizeX - 2, 2, 3, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getDefaultState(), 1, 1, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getDefaultState(), this.scatteredFeatureSizeX - 2, 1, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), 1, 2, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SAND.getMetadata()), this.scatteredFeatureSizeX - 2, 2, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(l), 2, 1, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone_stairs.getStateFromMeta(i3), this.scatteredFeatureSizeX - 3, 1, 2, structureBoundingBoxIn);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 3, 5, 4, 3, 18, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 5, 3, 5, this.scatteredFeatureSizeX - 5, 3, 17, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 5, 4, 2, 16, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.scatteredFeatureSizeX - 6, 1, 5, this.scatteredFeatureSizeX - 5, 2, 16, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        for (int k1 = 5; k1 <= 17; k1 += 2) {
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 4, 1, k1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 4, 2, k1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), this.scatteredFeatureSizeX - 5, 1, k1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), this.scatteredFeatureSizeX - 5, 2, k1, structureBoundingBoxIn);
        }
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 10, 0, 7, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 10, 0, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 9, 0, 9, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 11, 0, 9, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 8, 0, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 12, 0, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 7, 0, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 13, 0, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 9, 0, 11, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 11, 0, 11, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 10, 0, 12, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 10, 0, 13, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(j1), 10, 0, 10, structureBoundingBoxIn);
        for (int j3 = 0; j3 <= this.scatteredFeatureSizeX - 1; j3 += this.scatteredFeatureSizeX - 1) {
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 2, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 2, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 3, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 3, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 3, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 4, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), j3, 4, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 4, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 5, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 5, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 5, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 6, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), j3, 6, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 6, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 7, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 7, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), j3, 7, 3, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 8, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 8, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), j3, 8, 3, structureBoundingBoxIn);
        }
        for (int k3 = 2; k3 <= this.scatteredFeatureSizeX - 3; k3 += this.scatteredFeatureSizeX - 3 - 2) {
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 - 1, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 + 1, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 - 1, 3, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3, 3, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 + 1, 3, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3 - 1, 4, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), k3, 4, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3 + 1, 4, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 - 1, 5, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3, 5, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 + 1, 5, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3 - 1, 6, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), k3, 6, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3 + 1, 6, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3 - 1, 7, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3, 7, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), k3 + 1, 7, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 - 1, 8, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3, 8, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), k3 + 1, 8, 0, structureBoundingBoxIn);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 4, 0, 12, 6, 0, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 8, 6, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 12, 6, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 9, 5, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, 5, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stained_hardened_clay.getStateFromMeta(i1), 11, 5, 0, structureBoundingBoxIn);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -14, 8, 12, -11, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -10, 8, 12, -10, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -9, 8, 12, -9, 12, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, -8, 8, 12, -1, 12, Blocks.sandstone.getDefaultState(), Blocks.sandstone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, -11, 9, 11, -1, 11, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.stone_pressure_plate.getDefaultState(), 10, -11, 10, structureBoundingBoxIn);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, -13, 9, 11, -13, 11, Blocks.tnt.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 8, -11, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 8, -10, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 7, -10, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 7, -11, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 12, -11, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 12, -10, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 13, -10, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 13, -11, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 10, -11, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 10, -10, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 7, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 7, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 10, -11, 12, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 10, -10, 12, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.CHISELED.getMetadata()), 10, -10, 13, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata()), 10, -11, 13, structureBoundingBoxIn);
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (this.hasPlacedChest[enumfacing.getHorizontalIndex()]) continue;
            int l1 = enumfacing.getFrontOffsetX() * 2;
            int i2 = enumfacing.getFrontOffsetZ() * 2;
            this.hasPlacedChest[enumfacing.getHorizontalIndex()] = this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 10 + l1, -11, 10 + i2, WeightedRandomChestContent.func_177629_a(itemsToGenerateInTemple, (WeightedRandomChestContent[])new WeightedRandomChestContent[]{Items.enchanted_book.getRandom(randomIn)}), 2 + randomIn.nextInt(5));
        }
        return true;
    }
}
