package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

/*
 * Exception performing whole class analysis ignored.
 */
public static class StructureStrongholdPieces.Stairs
extends StructureStrongholdPieces.Stronghold {
    private boolean field_75024_a;

    public StructureStrongholdPieces.Stairs() {
    }

    public StructureStrongholdPieces.Stairs(int p_i2081_1_, Random p_i2081_2_, int p_i2081_3_, int p_i2081_4_) {
        super(p_i2081_1_);
        this.field_75024_a = true;
        this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(p_i2081_2_);
        this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;
        switch (StructureStrongholdPieces.3.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
            case 1: 
            case 4: {
                this.boundingBox = new StructureBoundingBox(p_i2081_3_, 64, p_i2081_4_, p_i2081_3_ + 5 - 1, 74, p_i2081_4_ + 5 - 1);
                break;
            }
            default: {
                this.boundingBox = new StructureBoundingBox(p_i2081_3_, 64, p_i2081_4_, p_i2081_3_ + 5 - 1, 74, p_i2081_4_ + 5 - 1);
            }
        }
    }

    public StructureStrongholdPieces.Stairs(int p_i45574_1_, Random p_i45574_2_, StructureBoundingBox p_i45574_3_, EnumFacing p_i45574_4_) {
        super(p_i45574_1_);
        this.field_75024_a = false;
        this.coordBaseMode = p_i45574_4_;
        this.field_143013_d = this.getRandomDoor(p_i45574_2_);
        this.boundingBox = p_i45574_3_;
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setBoolean("Source", this.field_75024_a);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.field_75024_a = tagCompound.getBoolean("Source");
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        if (this.field_75024_a) {
            StructureStrongholdPieces.access$202(StructureStrongholdPieces.Crossing.class);
        }
        this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
    }

    public static StructureStrongholdPieces.Stairs func_175863_a(List<StructureComponent> p_175863_0_, Random p_175863_1_, int p_175863_2_, int p_175863_3_, int p_175863_4_, EnumFacing p_175863_5_, int p_175863_6_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175863_2_, (int)p_175863_3_, (int)p_175863_4_, (int)-1, (int)-7, (int)0, (int)5, (int)11, (int)5, (EnumFacing)p_175863_5_);
        return StructureStrongholdPieces.Stairs.canStrongholdGoDeeper((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175863_0_, (StructureBoundingBox)structureboundingbox) == null ? new StructureStrongholdPieces.Stairs(p_175863_6_, p_175863_1_, structureboundingbox, p_175863_5_) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
            return false;
        }
        this.fillWithRandomizedBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 4, 10, 4, true, randomIn, (StructureComponent.BlockSelector)StructureStrongholdPieces.access$100());
        this.placeDoor(worldIn, randomIn, structureBoundingBoxIn, this.field_143013_d, 1, 7, 0);
        this.placeDoor(worldIn, randomIn, structureBoundingBoxIn, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 4);
        this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 6, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 5, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 6, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 5, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 4, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 5, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 4, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 3, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 4, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 3, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 2, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 3, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 2, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 1, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 2, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 1, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 1, 3, structureBoundingBoxIn);
        return true;
    }
}
