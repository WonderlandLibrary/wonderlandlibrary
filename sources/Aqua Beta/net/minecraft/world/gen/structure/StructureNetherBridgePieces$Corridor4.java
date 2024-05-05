package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;

public static class StructureNetherBridgePieces.Corridor4
extends StructureNetherBridgePieces.Piece {
    public StructureNetherBridgePieces.Corridor4() {
    }

    public StructureNetherBridgePieces.Corridor4(int p_i45618_1_, Random p_i45618_2_, StructureBoundingBox p_i45618_3_, EnumFacing p_i45618_4_) {
        super(p_i45618_1_);
        this.coordBaseMode = p_i45618_4_;
        this.boundingBox = p_i45618_3_;
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        int i = 1;
        if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.NORTH) {
            i = 5;
        }
        this.getNextComponentX((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 0, i, rand.nextInt(8) > 0);
        this.getNextComponentZ((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 0, i, rand.nextInt(8) > 0);
    }

    public static StructureNetherBridgePieces.Corridor4 func_175880_a(List<StructureComponent> p_175880_0_, Random p_175880_1_, int p_175880_2_, int p_175880_3_, int p_175880_4_, EnumFacing p_175880_5_, int p_175880_6_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175880_2_, (int)p_175880_3_, (int)p_175880_4_, (int)-3, (int)0, (int)0, (int)9, (int)7, (int)9, (EnumFacing)p_175880_5_);
        return StructureNetherBridgePieces.Corridor4.isAboveGround((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175880_0_, (StructureBoundingBox)structureboundingbox) == null ? new StructureNetherBridgePieces.Corridor4(p_175880_6_, p_175880_1_, structureboundingbox, p_175880_5_) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 8, 1, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 8, 5, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 0, 8, 6, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 2, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 0, 8, 5, 0, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 1, 4, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 0, 7, 4, 0, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 8, 2, 8, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 4, 2, 2, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 4, 7, 2, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 8, 8, 3, 8, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 6, 0, 3, 7, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 3, 6, 8, 3, 7, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 4, 0, 5, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 3, 4, 8, 5, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 5, 2, 5, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 5, 7, 5, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 5, 1, 5, 5, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 5, 7, 5, 5, Blocks.nether_brick_fence.getDefaultState(), Blocks.nether_brick_fence.getDefaultState(), false);
        for (int i = 0; i <= 5; ++i) {
            for (int j = 0; j <= 8; ++j) {
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), j, -1, i, structureBoundingBoxIn);
            }
        }
        return true;
    }
}
