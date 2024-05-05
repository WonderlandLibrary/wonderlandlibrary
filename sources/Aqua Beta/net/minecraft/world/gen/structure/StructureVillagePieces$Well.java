package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

/*
 * Exception performing whole class analysis ignored.
 */
public static class StructureVillagePieces.Well
extends StructureVillagePieces.Village {
    public StructureVillagePieces.Well() {
    }

    public StructureVillagePieces.Well(StructureVillagePieces.Start start, int p_i2109_2_, Random rand, int p_i2109_4_, int p_i2109_5_) {
        super(start, p_i2109_2_);
        this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(rand);
        switch (StructureVillagePieces.1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
            case 1: 
            case 2: {
                this.boundingBox = new StructureBoundingBox(p_i2109_4_, 64, p_i2109_5_, p_i2109_4_ + 6 - 1, 78, p_i2109_5_ + 6 - 1);
                break;
            }
            default: {
                this.boundingBox = new StructureBoundingBox(p_i2109_4_, 64, p_i2109_5_, p_i2109_4_ + 6 - 1, 78, p_i2109_5_ + 6 - 1);
            }
        }
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        StructureVillagePieces.access$000((StructureVillagePieces.Start)((StructureVillagePieces.Start)componentIn), listIn, (Random)rand, (int)(this.boundingBox.minX - 1), (int)(this.boundingBox.maxY - 4), (int)(this.boundingBox.minZ + 1), (EnumFacing)EnumFacing.WEST, (int)this.getComponentType());
        StructureVillagePieces.access$000((StructureVillagePieces.Start)((StructureVillagePieces.Start)componentIn), listIn, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)(this.boundingBox.maxY - 4), (int)(this.boundingBox.minZ + 1), (EnumFacing)EnumFacing.EAST, (int)this.getComponentType());
        StructureVillagePieces.access$000((StructureVillagePieces.Start)((StructureVillagePieces.Start)componentIn), listIn, (Random)rand, (int)(this.boundingBox.minX + 1), (int)(this.boundingBox.maxY - 4), (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)this.getComponentType());
        StructureVillagePieces.access$000((StructureVillagePieces.Start)((StructureVillagePieces.Start)componentIn), listIn, (Random)rand, (int)(this.boundingBox.minX + 1), (int)(this.boundingBox.maxY - 4), (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)this.getComponentType());
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
            if (this.field_143015_k < 0) {
                return true;
            }
            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 3, 0);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 4, 12, 4, Blocks.cobblestone.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 2, 12, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 3, 12, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 2, 12, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 3, 12, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 13, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 14, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 13, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 14, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 13, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 1, 14, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 13, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 14, 4, structureBoundingBoxIn);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 15, 1, 4, 15, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        for (int i = 0; i <= 5; ++i) {
            for (int j = 0; j <= 5; ++j) {
                if (j != 0 && j != 5 && i != 0 && i != 5) continue;
                this.setBlockState(worldIn, Blocks.gravel.getDefaultState(), j, 11, i, structureBoundingBoxIn);
                this.clearCurrentPositionBlocksUpwards(worldIn, j, 12, i, structureBoundingBoxIn);
            }
        }
        return true;
    }
}
