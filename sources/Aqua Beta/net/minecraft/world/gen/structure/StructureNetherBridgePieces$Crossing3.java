package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;

public static class StructureNetherBridgePieces.Crossing3
extends StructureNetherBridgePieces.Piece {
    public StructureNetherBridgePieces.Crossing3() {
    }

    public StructureNetherBridgePieces.Crossing3(int p_i45622_1_, Random p_i45622_2_, StructureBoundingBox p_i45622_3_, EnumFacing p_i45622_4_) {
        super(p_i45622_1_);
        this.coordBaseMode = p_i45622_4_;
        this.boundingBox = p_i45622_3_;
    }

    protected StructureNetherBridgePieces.Crossing3(Random p_i2042_1_, int p_i2042_2_, int p_i2042_3_) {
        super(0);
        this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(p_i2042_1_);
        switch (StructureNetherBridgePieces.1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
            case 1: 
            case 2: {
                this.boundingBox = new StructureBoundingBox(p_i2042_2_, 64, p_i2042_3_, p_i2042_2_ + 19 - 1, 73, p_i2042_3_ + 19 - 1);
                break;
            }
            default: {
                this.boundingBox = new StructureBoundingBox(p_i2042_2_, 64, p_i2042_3_, p_i2042_2_ + 19 - 1, 73, p_i2042_3_ + 19 - 1);
            }
        }
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        this.getNextComponentNormal((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 8, 3, false);
        this.getNextComponentX((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 3, 8, false);
        this.getNextComponentZ((StructureNetherBridgePieces.Start)componentIn, listIn, rand, 3, 8, false);
    }

    public static StructureNetherBridgePieces.Crossing3 func_175885_a(List<StructureComponent> p_175885_0_, Random p_175885_1_, int p_175885_2_, int p_175885_3_, int p_175885_4_, EnumFacing p_175885_5_, int p_175885_6_) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox((int)p_175885_2_, (int)p_175885_3_, (int)p_175885_4_, (int)-8, (int)-3, (int)0, (int)19, (int)10, (int)19, (EnumFacing)p_175885_5_);
        return StructureNetherBridgePieces.Crossing3.isAboveGround((StructureBoundingBox)structureboundingbox) && StructureComponent.findIntersecting(p_175885_0_, (StructureBoundingBox)structureboundingbox) == null ? new StructureNetherBridgePieces.Crossing3(p_175885_6_, p_175885_1_, structureboundingbox, p_175885_5_) : null;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 0, 11, 4, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 7, 18, 4, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 5, 0, 10, 7, 18, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 8, 18, 7, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 5, 0, 7, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 5, 11, 7, 5, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 0, 11, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 11, 11, 5, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 7, 7, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 7, 18, 5, 7, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 11, 7, 5, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 5, 11, 18, 5, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 0, 11, 2, 5, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 13, 11, 2, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 0, 11, 1, 3, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 15, 11, 1, 18, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        for (int i = 7; i <= 11; ++i) {
            for (int j = 0; j <= 2; ++j) {
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, j, structureBoundingBoxIn);
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), i, -1, 18 - j, structureBoundingBoxIn);
            }
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 7, 5, 2, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 2, 7, 18, 2, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 7, 3, 1, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 0, 7, 18, 1, 11, Blocks.nether_brick.getDefaultState(), Blocks.nether_brick.getDefaultState(), false);
        for (int k = 0; k <= 2; ++k) {
            for (int l = 7; l <= 11; ++l) {
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), k, -1, l, structureBoundingBoxIn);
                this.replaceAirAndLiquidDownwards(worldIn, Blocks.nether_brick.getDefaultState(), 18 - k, -1, l, structureBoundingBoxIn);
            }
        }
        return true;
    }
}
