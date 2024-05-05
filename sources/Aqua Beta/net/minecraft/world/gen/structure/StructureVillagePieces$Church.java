package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public static class StructureVillagePieces.Church
extends StructureVillagePieces.Village {
    public StructureVillagePieces.Church() {
    }

    public StructureVillagePieces.Church(StructureVillagePieces.Start start, int p_i45564_2_, Random rand, StructureBoundingBox p_i45564_4_, EnumFacing facing) {
        super(start, p_i45564_2_);
        this.coordBaseMode = facing;
        this.boundingBox = p_i45564_4_;
    }

    public static StructureVillagePieces.Church func_175854_a(StructureVillagePieces.Start start, List<StructureComponent> p_175854_1_, Random rand, int p_175854_3_, int p_175854_4_, int p_175854_5_, EnumFacing facing, int p_175854_7_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175854_3_, (int)p_175854_4_, (int)p_175854_5_, (int)0, (int)0, (int)0, (int)5, (int)12, (int)9, (EnumFacing)facing);
        return StructureVillagePieces.Church.canVillageGoDeeper((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175854_1_, (StructureBoundingBox)structureboundingbox) == null ? new StructureVillagePieces.Church(start, p_175854_7_, rand, structureboundingbox, facing) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
            if (this.field_143015_k < 0) {
                return true;
            }
            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 12 - 1, 0);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 3, 3, 7, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 1, 3, 9, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 3, 0, 8, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 3, 10, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 10, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 1, 4, 10, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 4, 0, 4, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 4, 4, 4, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 8, 3, 4, 8, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 4, 3, 10, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 5, 3, 5, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 9, 0, 4, 9, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 0, 4, 4, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 0, 11, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 4, 11, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 2, 11, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 2, 11, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 1, 1, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 1, 1, 7, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 2, 1, 7, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 3, 1, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.cobblestone.getDefaultState(), 3, 1, 7, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 1, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 1, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 3, 1, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 1)), 1, 2, 7, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 0)), 3, 2, 7, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 3, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 2, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 3, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 6, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 7, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 6, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 7, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 6, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 7, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 6, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 7, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 0, 3, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 4, 3, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.glass_pane.getDefaultState(), 2, 3, 8, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty((IProperty)BlockTorch.FACING, (Comparable)this.coordBaseMode.getOpposite()), 2, 4, 7, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty((IProperty)BlockTorch.FACING, (Comparable)this.coordBaseMode.rotateY()), 1, 4, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty((IProperty)BlockTorch.FACING, (Comparable)this.coordBaseMode.rotateYCCW()), 3, 4, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.torch.getDefaultState().withProperty((IProperty)BlockTorch.FACING, (Comparable)this.coordBaseMode), 2, 4, 5, structureBoundingBoxIn);
        int i = this.getMetadataWithOffset(Blocks.ladder, 4);
        for (int j = 1; j <= 9; ++j) {
            this.setBlockState(worldIn, Blocks.ladder.getStateFromMeta(i), 3, j, 3, structureBoundingBoxIn);
        }
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 2, 1, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.air.getDefaultState(), 2, 2, 0, structureBoundingBoxIn);
        this.placeDoorCurrentPosition(worldIn, structureBoundingBoxIn, randomIn, 2, 1, 0, EnumFacing.getHorizontal((int)this.getMetadataWithOffset(Blocks.oak_door, 1)));
        if (this.getBlockStateFromPos(worldIn, 2, 0, -1, structureBoundingBoxIn).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(worldIn, 2, -1, -1, structureBoundingBoxIn).getBlock().getMaterial() != Material.air) {
            this.setBlockState(worldIn, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, structureBoundingBoxIn);
        }
        for (int l = 0; l < 9; ++l) {
            for (int k = 0; k < 5; ++k) {
                this.clearCurrentPositionBlocksUpwards(worldIn, k, 12, l, structureBoundingBoxIn);
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.cobblestone.getDefaultState(), k, -1, l, structureBoundingBoxIn);
            }
        }
        this.spawnVillagers(worldIn, structureBoundingBoxIn, 2, 1, 2, 1);
        return true;
    }

    protected int func_180779_c(int p_180779_1_, int p_180779_2_) {
        return 2;
    }
}
