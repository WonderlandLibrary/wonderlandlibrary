package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public static class ComponentScatteredFeaturePieces.JunglePyramid
extends ComponentScatteredFeaturePieces.Feature {
    private boolean placedMainChest;
    private boolean placedHiddenChest;
    private boolean placedTrap1;
    private boolean placedTrap2;
    private static final List<WeightedRandomChestContent> field_175816_i = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 2, 7, 15), new WeightedRandomChestContent(Items.emerald, 0, 1, 3, 2), new WeightedRandomChestContent(Items.bone, 0, 4, 6, 20), new WeightedRandomChestContent(Items.rotten_flesh, 0, 3, 7, 16), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)});
    private static final List<WeightedRandomChestContent> field_175815_j = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.arrow, 0, 2, 7, 30)});
    private static Stones junglePyramidsRandomScatteredStones = new Stones(null);

    public ComponentScatteredFeaturePieces.JunglePyramid() {
    }

    public ComponentScatteredFeaturePieces.JunglePyramid(Random p_i2064_1_, int p_i2064_2_, int p_i2064_3_) {
        super(p_i2064_1_, p_i2064_2_, 64, p_i2064_3_, 12, 10, 15);
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setBoolean("placedMainChest", this.placedMainChest);
        tagCompound.setBoolean("placedHiddenChest", this.placedHiddenChest);
        tagCompound.setBoolean("placedTrap1", this.placedTrap1);
        tagCompound.setBoolean("placedTrap2", this.placedTrap2);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.placedMainChest = tagCompound.getBoolean("placedMainChest");
        this.placedHiddenChest = tagCompound.getBoolean("placedHiddenChest");
        this.placedTrap1 = tagCompound.getBoolean("placedTrap1");
        this.placedTrap2 = tagCompound.getBoolean("placedTrap2");
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (!this.func_74935_a(worldIn, structureBoundingBoxIn, 0)) {
            return false;
        }
        int i = this.getMetadataWithOffset(Blocks.stone_stairs, 3);
        int j = this.getMetadataWithOffset(Blocks.stone_stairs, 2);
        int k = this.getMetadataWithOffset(Blocks.stone_stairs, 0);
        int l = this.getMetadataWithOffset(Blocks.stone_stairs, 1);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, -4, 0, this.scatteredFeatureSizeX - 1, 0, this.scatteredFeatureSizeZ - 1, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 1, 2, 9, 2, 2, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 1, 12, 9, 2, 12, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 1, 3, 2, 2, 11, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 1, 3, 9, 2, 11, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 1, 10, 6, 1, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 13, 10, 6, 13, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, 3, 2, 1, 6, 12, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 10, 3, 2, 10, 6, 12, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 3, 2, 9, 3, 12, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 6, 2, 9, 6, 12, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 3, 7, 3, 8, 7, 11, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 8, 4, 7, 8, 10, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 3, 8, 2, 11);
        this.fillWithAir(worldIn, structureBoundingBoxIn, 4, 3, 6, 7, 3, 9);
        this.fillWithAir(worldIn, structureBoundingBoxIn, 2, 4, 2, 9, 5, 12);
        this.fillWithAir(worldIn, structureBoundingBoxIn, 4, 6, 5, 7, 6, 9);
        this.fillWithAir(worldIn, structureBoundingBoxIn, 5, 7, 6, 6, 7, 8);
        this.fillWithAir(worldIn, structureBoundingBoxIn, 5, 1, 2, 6, 2, 2);
        this.fillWithAir(worldIn, structureBoundingBoxIn, 5, 2, 12, 6, 2, 12);
        this.fillWithAir(worldIn, structureBoundingBoxIn, 5, 5, 1, 6, 5, 1);
        this.fillWithAir(worldIn, structureBoundingBoxIn, 5, 5, 13, 6, 5, 13);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 1, 5, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 10, 5, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 1, 5, 9, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 10, 5, 9, structureBoundingBoxIn);
        for (int i1 = 0; i1 <= 14; i1 += 14) {
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 4, i1, 2, 5, i1, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 4, i1, 4, 5, i1, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 4, i1, 7, 5, i1, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 4, i1, 9, 5, i1, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        }
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 6, 0, 6, 6, 0, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        for (int k1 = 0; k1 <= 11; k1 += 11) {
            for (int j1 = 2; j1 <= 12; j1 += 2) {
                this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, k1, 4, j1, k1, 5, j1, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
            }
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, k1, 6, 5, k1, 6, 5, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, k1, 6, 9, k1, 6, 9, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        }
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 7, 2, 2, 9, 2, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 7, 2, 9, 9, 2, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, 7, 12, 2, 9, 12, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, 7, 12, 9, 9, 12, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 9, 4, 4, 9, 4, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 9, 4, 7, 9, 4, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 9, 10, 4, 9, 10, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 9, 10, 7, 9, 10, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 9, 7, 6, 9, 7, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 5, 9, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 6, 9, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 5, 9, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 6, 9, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 0, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 5, 0, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 6, 0, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 0, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 1, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 2, 9, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 4, 3, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 1, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 2, 9, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(i), 7, 3, 10, structureBoundingBoxIn);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 9, 4, 1, 9, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, 1, 9, 7, 1, 9, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 4, 1, 10, 7, 2, 10, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 5, 4, 5, 6, 4, 5, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(k), 4, 4, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(l), 7, 4, 5, structureBoundingBoxIn);
        for (int l1 = 0; l1 < 4; ++l1) {
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 5, 0 - l1, 6 + l1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(j), 6, 0 - l1, 6 + l1, structureBoundingBoxIn);
            this.fillWithAir(worldIn, structureBoundingBoxIn, 5, 0 - l1, 7 + l1, 6, 0 - l1, 9 + l1);
        }
        this.fillWithAir(worldIn, structureBoundingBoxIn, 1, -3, 12, 10, -1, 13);
        this.fillWithAir(worldIn, structureBoundingBoxIn, 1, -3, 1, 3, -1, 13);
        this.fillWithAir(worldIn, structureBoundingBoxIn, 1, -3, 1, 9, -1, 5);
        for (int i2 = 1; i2 <= 13; i2 += 2) {
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, -3, i2, 1, -2, i2, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        }
        for (int j2 = 2; j2 <= 12; j2 += 2) {
            this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 1, -1, j2, 3, -1, j2, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        }
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 2, -2, 1, 5, -2, 1, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 7, -2, 1, 9, -2, 1, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 6, -3, 1, 6, -3, 1, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 6, -1, 1, 6, -1, 1, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.setBlockState(worldIn, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset((Block)Blocks.tripwire_hook, EnumFacing.EAST.getHorizontalIndex())).withProperty((IProperty)BlockTripWireHook.ATTACHED, (Comparable)Boolean.valueOf((boolean)true)), 1, -3, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset((Block)Blocks.tripwire_hook, EnumFacing.WEST.getHorizontalIndex())).withProperty((IProperty)BlockTripWireHook.ATTACHED, (Comparable)Boolean.valueOf((boolean)true)), 4, -3, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty((IProperty)BlockTripWire.ATTACHED, (Comparable)Boolean.valueOf((boolean)true)), 2, -3, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty((IProperty)BlockTripWire.ATTACHED, (Comparable)Boolean.valueOf((boolean)true)), 3, -3, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 7, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 5, -3, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 4, -3, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 3, -3, 1, structureBoundingBoxIn);
        if (!this.placedTrap1) {
            this.placedTrap1 = this.generateDispenserContents(worldIn, structureBoundingBoxIn, randomIn, 3, -2, 1, EnumFacing.NORTH.getIndex(), field_175815_j, 2);
        }
        this.setBlockState(worldIn, Blocks.vine.getStateFromMeta(15), 3, -2, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset((Block)Blocks.tripwire_hook, EnumFacing.NORTH.getHorizontalIndex())).withProperty((IProperty)BlockTripWireHook.ATTACHED, (Comparable)Boolean.valueOf((boolean)true)), 7, -3, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.tripwire_hook.getStateFromMeta(this.getMetadataWithOffset((Block)Blocks.tripwire_hook, EnumFacing.SOUTH.getHorizontalIndex())).withProperty((IProperty)BlockTripWireHook.ATTACHED, (Comparable)Boolean.valueOf((boolean)true)), 7, -3, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty((IProperty)BlockTripWire.ATTACHED, (Comparable)Boolean.valueOf((boolean)true)), 7, -3, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty((IProperty)BlockTripWire.ATTACHED, (Comparable)Boolean.valueOf((boolean)true)), 7, -3, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.tripwire.getDefaultState().withProperty((IProperty)BlockTripWire.ATTACHED, (Comparable)Boolean.valueOf((boolean)true)), 7, -3, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 8, -3, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 9, -3, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 9, -3, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 9, -3, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 9, -2, 4, structureBoundingBoxIn);
        if (!this.placedTrap2) {
            this.placedTrap2 = this.generateDispenserContents(worldIn, structureBoundingBoxIn, randomIn, 9, -2, 3, EnumFacing.WEST.getIndex(), field_175815_j, 2);
        }
        this.setBlockState(worldIn, Blocks.vine.getStateFromMeta(15), 8, -1, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.vine.getStateFromMeta(15), 8, -2, 3, structureBoundingBoxIn);
        if (!this.placedMainChest) {
            this.placedMainChest = this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 8, -3, 3, WeightedRandomChestContent.func_177629_a(field_175816_i, (WeightedRandomChestContent[])new WeightedRandomChestContent[]{Items.enchanted_book.getRandom(randomIn)}), 2 + randomIn.nextInt(5));
        }
        this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 9, -3, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 8, -3, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 4, -3, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 5, -2, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 5, -1, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 6, -3, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 7, -2, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 7, -1, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 8, -3, 5, structureBoundingBoxIn);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 9, -1, 1, 9, -1, 5, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithAir(worldIn, structureBoundingBoxIn, 8, -3, 8, 10, -1, 10);
        this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 8, -2, 11, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 9, -2, 11, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CHISELED_META), 10, -2, 11, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing((EnumFacing)EnumFacing.getFront((int)this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 8, -2, 12, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing((EnumFacing)EnumFacing.getFront((int)this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 9, -2, 12, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.lever.getStateFromMeta(BlockLever.getMetadataForFacing((EnumFacing)EnumFacing.getFront((int)this.getMetadataWithOffset(Blocks.lever, EnumFacing.NORTH.getIndex())))), 10, -2, 12, structureBoundingBoxIn);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 8, -3, 8, 8, -3, 10, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 10, -3, 8, 10, -3, 10, false, randomIn, (StructureComponent.BlockSelector)junglePyramidsRandomScatteredStones);
        this.setBlockState(worldIn, Blocks.mossy_cobblestone.getDefaultState(), 10, -2, 9, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 8, -2, 9, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 8, -2, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.redstone_wire.getDefaultState(), 10, -1, 9, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sticky_piston.getStateFromMeta(EnumFacing.UP.getIndex()), 9, -2, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sticky_piston.getStateFromMeta(this.getMetadataWithOffset((Block)Blocks.sticky_piston, EnumFacing.WEST.getIndex())), 10, -2, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.sticky_piston.getStateFromMeta(this.getMetadataWithOffset((Block)Blocks.sticky_piston, EnumFacing.WEST.getIndex())), 10, -1, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.unpowered_repeater.getStateFromMeta(this.getMetadataWithOffset((Block)Blocks.unpowered_repeater, EnumFacing.NORTH.getHorizontalIndex())), 10, -2, 10, structureBoundingBoxIn);
        if (!this.placedHiddenChest) {
            this.placedHiddenChest = this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 9, -3, 10, WeightedRandomChestContent.func_177629_a(field_175816_i, (WeightedRandomChestContent[])new WeightedRandomChestContent[]{Items.enchanted_book.getRandom(randomIn)}), 2 + randomIn.nextInt(5));
        }
        return true;
    }
}
