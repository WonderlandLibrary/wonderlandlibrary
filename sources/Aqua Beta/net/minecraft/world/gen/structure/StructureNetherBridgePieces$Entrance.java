package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;

public static class StructureNetherBridgePieces.Entrance
extends StructureNetherBridgePieces.Piece {
    public StructureNetherBridgePieces.Entrance() {
    }

    public StructureNetherBridgePieces.Entrance(int p_i45617_1_, Random p_i45617_2_, StructureBoundingBox p_i45617_3_, EnumFacing p_i45617_4_) {
        super(p_i45617_1_);
        this.coordBaseMode = p_i45617_4_;
        this.boundingBox = p_i45617_3_;
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        this.getNextComponentNormal((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 5, 3, true);
    }

    public static StructureNetherBridgePieces.Entrance func_175881_a(List<StructureComponent> p_175881_0_, Random p_175881_1_, int p_175881_2_, int p_175881_3_, int p_175881_4_, EnumFacing p_175881_5_, int p_175881_6_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175881_2_, (int)p_175881_3_, (int)p_175881_4_, (int)-5, (int)-3, (int)0, (int)13, (int)14, (int)13, (EnumFacing)p_175881_5_);
        return StructureNetherBridgePieces.Entrance.isAboveGround((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175881_0_, (StructureBoundingBox)structureboundingbox) == null ? new StructureNetherBridgePieces.Entrance(p_175881_6_, p_175881_1_, structureboundingbox, p_175881_5_) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 12, 4, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 12, 13, 12, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 1, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 0, 12, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 11, 4, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 11, 10, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 9, 11, 7, 12, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 0, 4, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 0, 10, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 9, 0, 7, 12, 1, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 11, 2, 10, 12, 10, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 8, 0, 7, 8, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        for (int i = 1; i <= 11; i += 2) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, i, 10, 0, i, 11, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, i, 10, 12, i, 11, 12, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 10, i, 0, 11, i, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 10, i, 12, 11, i, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), i, 13, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), i, 13, 12, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), 0, 13, i, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), 12, 13, i, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), i + 1, 13, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), i + 1, 13, 12, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 0, 13, i + 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 12, 13, i + 1, structureBoundingBoxIn);
        }
        this.setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 12, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 0, 13, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.nether_brick_fence.getDefaultState(), 12, 13, 0, structureBoundingBoxIn);
        for (int k = 3; k <= 9; k += 2) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, k, 1, 8, k, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 7, k, 11, 8, k, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 2, 0, 8, 2, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 12, 2, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 8, 1, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 9, 8, 1, 12, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 4, 3, 1, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 4, 12, 1, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        for (int l = 4; l <= 8; ++l) {
            for (int j = 0; j <= 2; ++j) {
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), l, -1, j, structureBoundingBoxIn);
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), l, -1, 12 - j, structureBoundingBoxIn);
            }
        }
        for (int i1 = 0; i1 <= 2; ++i1) {
            for (int j1 = 4; j1 <= 8; ++j1) {
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i1, -1, j1, structureBoundingBoxIn);
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), 12 - i1, -1, j1, structureBoundingBoxIn);
            }
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 5, 7, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 6, 6, 4, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.setBlockState(worldIn, Blocks.nether_brick.getDefaultState(), 6, 0, 6, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.flowing_lava.getDefaultState(), 6, 5, 6, structureBoundingBoxIn);
        BlockPos blockpos = new BlockPos(this.getXWithOffset(6, 6), this.getYWithOffset(5), this.getZWithOffset(6, 6));
        if (structureBoundingBoxIn.isVecInside((Vec3i)blockpos)) {
            worldIn.forceBlockUpdateTick((Block)Blocks.flowing_lava, blockpos, randomIn);
        }
        return true;
    }
}
