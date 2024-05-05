package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public static class StructureVillagePieces.House1
extends StructureVillagePieces.Village {
    public StructureVillagePieces.House1() {
    }

    public StructureVillagePieces.House1(StructureVillagePieces.Start start, int p_i45571_2_, Random rand, StructureBoundingBox p_i45571_4_, EnumFacing facing) {
        super(start, p_i45571_2_);
        this.coordBaseMode = facing;
        this.boundingBox = p_i45571_4_;
    }

    public static StructureVillagePieces.House1 func_175850_a(StructureVillagePieces.Start start, List<StructureComponent> p_175850_1_, Random rand, int p_175850_3_, int p_175850_4_, int p_175850_5_, EnumFacing facing, int p_175850_7_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175850_3_, (int)p_175850_4_, (int)p_175850_5_, (int)0, (int)0, (int)0, (int)9, (int)9, (int)6, (EnumFacing)facing);
        return StructureVillagePieces.House1.canVillageGoDeeper((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175850_1_, (StructureBoundingBox)structureboundingbox) == null ? new StructureVillagePieces.House1(start, p_175850_7_, rand, structureboundingbox, facing) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
            if (this.field_143015_k < 0) {
                return true;
            }
            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 9 - 1, 0);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 7, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 8, 0, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 8, 5, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 1, 8, 6, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 7, 2, 8, 7, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        int i = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
        int j = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
        for (int k = -1; k <= 2; ++k) {
            for (int l = 0; l <= 8; ++l) {
                this.setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(i), l, 6 + k, k, structureBoundingBoxIn);
                this.setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(j), l, 6 + k, 5 - k, structureBoundingBoxIn);
            }
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 5, 8, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 0, 8, 1, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 7, 1, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 4, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 5, 0, 4, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 5, 8, 4, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 0, 8, 4, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 1, 0, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 5, 7, 4, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 1, 8, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 7, 4, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 2, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 5, 2, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 6, 2, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 3, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 5, 3, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 6, 3, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 3, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 3, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 8, 2, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 8, 3, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 8, 3, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 2, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 3, 2, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 5, 2, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 6, 2, 5, structureBoundingBoxIn);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 7, 4, 1, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 4, 7, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 4, 7, 3, 4, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.planks.getDefaultState(), 7, 1, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), 7, 1, 3, structureBoundingBoxIn);
        int j1 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
        this.setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(j1), 6, 1, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(j1), 5, 1, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(j1), 4, 1, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.oak_stairs.getStateFromMeta(j1), 3, 1, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 6, 1, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.wooden_pressure_plate.getDefaultState(), 6, 2, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.oak_fence.getDefaultState(), 4, 1, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.wooden_pressure_plate.getDefaultState(), 4, 2, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.crafting_table.getDefaultState(), 7, 1, 1, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 1, 1, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 1, 2, 0, structureBoundingBoxIn);
        this.placeDoorCurrentPosition(worldIn, structureBoundingBoxIn, randomIn, 1, 1, 0, EnumFacing.getHorizontal((int)this.getMetadataWithOffset(Blocks.oak_door, 1)));
        if (this.getBlockStateFromPos(worldIn, 1, 0, -1, structureBoundingBoxIn).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(worldIn, 1, -1, -1, structureBoundingBoxIn).getBlock().getMaterial() != Material.air) {
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 0, -1, structureBoundingBoxIn);
        }
        for (int k1 = 0; k1 < 6; ++k1) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.clearCurrentPositionBlocksUpwards(worldIn, i1, 9, k1, structureBoundingBoxIn);
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.cobblestone.getDefaultState(), i1, -1, k1, structureBoundingBoxIn);
            }
        }
        this.spawnVillagers(worldIn, structureBoundingBoxIn, 2, 1, 2, 1);
        return true;
    }

    protected int func_180779_c(int p_180779_1_, int p_180779_2_) {
        return 1;
    }
}
