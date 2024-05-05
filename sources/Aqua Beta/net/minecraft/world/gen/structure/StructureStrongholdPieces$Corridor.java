package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

public static class StructureStrongholdPieces.Corridor
extends StructureStrongholdPieces.Stronghold {
    private int field_74993_a;

    public StructureStrongholdPieces.Corridor() {
    }

    public StructureStrongholdPieces.Corridor(int p_i45581_1_, Random p_i45581_2_, StructureBoundingBox p_i45581_3_, EnumFacing p_i45581_4_) {
        super(p_i45581_1_);
        this.coordBaseMode = p_i45581_4_;
        this.boundingBox = p_i45581_3_;
        this.field_74993_a = p_i45581_4_ != EnumFacing.NORTH && p_i45581_4_ != EnumFacing.SOUTH ? p_i45581_3_.getXSize() : p_i45581_3_.getZSize();
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setInteger("Steps", this.field_74993_a);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
        this.field_74993_a = tagCompound.getInteger("Steps");
    }

    public static StructureBoundingBox func_175869_a(List<StructureComponent> p_175869_0_, Random p_175869_1_, int p_175869_2_, int p_175869_3_, int p_175869_4_, EnumFacing p_175869_5_) {
        int i = 3;
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175869_2_, (int)p_175869_3_, (int)p_175869_4_, (int)-1, (int)-1, (int)0, (int)5, (int)5, (int)4, (EnumFacing)p_175869_5_);
        StructureComponent structurecomponent = StructureComponent.findIntersecting(p_175869_0_, (StructureBoundingBox)structureboundingbox);
        if (structurecomponent == null) {
            return null;
        }
        if (structurecomponent.getBoundingBox().minY == structureboundingbox.minY) {
            for (int j = 3; j >= 1; --j) {
                structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175869_2_, (int)p_175869_3_, (int)p_175869_4_, (int)-1, (int)-1, (int)0, (int)5, (int)5, (int)(j - 1), (EnumFacing)p_175869_5_);
                if (structurecomponent.getBoundingBox().intersectsWith(structureboundingbox)) continue;
                return StructureBoundingBox.getComponentToAddBoundingBox((int)p_175869_2_, (int)p_175869_3_, (int)p_175869_4_, (int)-1, (int)-1, (int)0, (int)5, (int)5, (int)j, (EnumFacing)p_175869_5_);
            }
        }
        return null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
            return false;
        }
        for (int i = 0; i < this.field_74993_a; ++i) {
            this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 0, 0, i, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 0, i, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 0, i, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 0, i, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 4, 0, i, structureBoundingBoxIn);
            for (int j = 1; j <= 3; ++j) {
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 0, j, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.air.getDefaultState(), 1, j, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.air.getDefaultState(), 2, j, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.air.getDefaultState(), 3, j, i, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 4, j, i, structureBoundingBoxIn);
            }
            this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 0, 4, i, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 1, 4, i, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 2, 4, i, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 3, 4, i, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.stonebrick.getDefaultState(), 4, 4, i, structureBoundingBoxIn);
        }
        return true;
    }
}
