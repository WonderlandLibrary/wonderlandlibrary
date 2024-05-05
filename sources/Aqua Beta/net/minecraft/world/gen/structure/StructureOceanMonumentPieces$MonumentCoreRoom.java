package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

public static class StructureOceanMonumentPieces.MonumentCoreRoom
extends StructureOceanMonumentPieces.Piece {
    public StructureOceanMonumentPieces.MonumentCoreRoom() {
    }

    public StructureOceanMonumentPieces.MonumentCoreRoom(EnumFacing p_i45598_1_, StructureOceanMonumentPieces.RoomDefinition p_i45598_2_, Random p_i45598_3_) {
        super(1, p_i45598_1_, p_i45598_2_, 2, 2, 2);
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        this.func_175819_a(worldIn, structureBoundingBoxIn, 1, 8, 0, 14, 8, 14, field_175828_a);
        int i = 7;
        IBlockState iblockstate = field_175826_b;
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i, 0, 0, i, 15, iblockstate, iblockstate, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, i, 0, 15, i, 15, iblockstate, iblockstate, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 0, 15, i, 0, iblockstate, iblockstate, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 15, 14, i, 15, iblockstate, iblockstate, false);
        for (i = 1; i <= 6; ++i) {
            iblockstate = field_175826_b;
            if (i == 2 || i == 6) {
                iblockstate = field_175828_a;
            }
            for (int j = 0; j <= 15; j += 15) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, j, i, 0, j, i, 1, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, j, i, 6, j, i, 9, iblockstate, iblockstate, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, j, i, 14, j, i, 15, iblockstate, iblockstate, false);
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 0, 1, i, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, i, 0, 9, i, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 14, i, 0, 14, i, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 15, 14, i, 15, iblockstate, iblockstate, false);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 6, 9, 6, 9, field_175827_c, field_175827_c, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 7, 8, 5, 8, Blocks.gold_block.getDefaultState(), Blocks.gold_block.getDefaultState(), false);
        for (i = 3; i <= 6; i += 3) {
            for (int k = 6; k <= 9; k += 3) {
                this.setBlockState(worldIn, field_175825_e, k, i, 6, structureBoundingBoxIn);
                this.setBlockState(worldIn, field_175825_e, k, i, 9, structureBoundingBoxIn);
            }
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 6, 5, 2, 6, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 9, 5, 2, 9, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 6, 10, 2, 6, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 9, 10, 2, 9, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 5, 6, 2, 5, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 5, 9, 2, 5, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 10, 6, 2, 10, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 10, 9, 2, 10, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 5, 5, 6, 5, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 10, 5, 6, 10, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 2, 5, 10, 6, 5, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 2, 10, 10, 6, 10, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 7, 1, 5, 7, 6, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 7, 1, 10, 7, 6, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 7, 9, 5, 7, 14, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 7, 9, 10, 7, 14, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, 5, 6, 7, 5, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, 10, 6, 7, 10, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 7, 5, 14, 7, 5, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 7, 10, 14, 7, 10, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 2, 2, 1, 3, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 2, 3, 1, 2, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 2, 13, 1, 3, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 2, 12, 1, 2, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 12, 2, 1, 13, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 13, 3, 1, 13, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 12, 13, 1, 13, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 12, 1, 13, 12, 1, 13, field_175826_b, field_175826_b, false);
        return true;
    }
}
