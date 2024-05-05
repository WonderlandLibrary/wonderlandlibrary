package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

public static class StructureOceanMonumentPieces.WingRoom
extends StructureOceanMonumentPieces.Piece {
    private int field_175834_o;

    public StructureOceanMonumentPieces.WingRoom() {
    }

    public StructureOceanMonumentPieces.WingRoom(EnumFacing p_i45585_1_, StructureBoundingBox p_i45585_2_, int p_i45585_3_) {
        super(p_i45585_1_, p_i45585_2_);
        this.field_175834_o = p_i45585_3_ & 1;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.field_175834_o == 0) {
            for (int i = 0; i < 4; ++i) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10 - i, 3 - i, 20 - i, 12 + i, 3 - i, 20, field_175826_b, field_175826_b, false);
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 6, 15, 0, 16, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 6, 6, 3, 20, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 16, 0, 6, 16, 3, 20, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 7, 7, 1, 20, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 1, 7, 15, 1, 20, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 6, 9, 3, 6, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 6, 15, 3, 6, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 7, 9, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 7, 14, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 5, 13, 0, 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 0, 7, 12, 0, 7, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 0, 10, 8, 0, 12, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 14, 0, 10, 14, 0, 12, field_175827_c, field_175827_c, false);
            for (int i1 = 18; i1 >= 7; i1 -= 3) {
                this.setBlockState(worldIn, field_175825_e, 6, 3, i1, structureBoundingBoxIn);
                this.setBlockState(worldIn, field_175825_e, 16, 3, i1, structureBoundingBoxIn);
            }
            this.setBlockState(worldIn, field_175825_e, 10, 0, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175825_e, 12, 0, 10, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175825_e, 10, 0, 12, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175825_e, 12, 0, 12, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175825_e, 8, 3, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175825_e, 14, 3, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175826_b, 4, 2, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175825_e, 4, 1, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175826_b, 4, 0, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175826_b, 18, 2, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175825_e, 18, 1, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175826_b, 18, 0, 4, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175826_b, 4, 2, 18, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175825_e, 4, 1, 18, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175826_b, 4, 0, 18, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175826_b, 18, 2, 18, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175825_e, 18, 1, 18, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175826_b, 18, 0, 18, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175826_b, 9, 7, 20, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175826_b, 13, 7, 20, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 21, 7, 4, 21, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 0, 21, 16, 4, 21, field_175826_b, field_175826_b, false);
            this.func_175817_a(worldIn, structureBoundingBoxIn, 11, 2, 16);
        } else if (this.field_175834_o == 1) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 3, 18, 13, 3, 20, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 18, 9, 2, 18, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 0, 18, 13, 2, 18, field_175826_b, field_175826_b, false);
            int j1 = 9;
            int j = 20;
            int k = 5;
            for (int l = 0; l < 2; ++l) {
                this.setBlockState(worldIn, field_175826_b, j1, k + 1, j, structureBoundingBoxIn);
                this.setBlockState(worldIn, field_175825_e, j1, k, j, structureBoundingBoxIn);
                this.setBlockState(worldIn, field_175826_b, j1, k - 1, j, structureBoundingBoxIn);
                j1 = 13;
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 7, 15, 3, 14, field_175826_b, field_175826_b, false);
            j1 = 10;
            for (int k1 = 0; k1 < 2; ++k1) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, j1, 0, 10, j1, 6, 10, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, j1, 0, 12, j1, 6, 12, field_175826_b, field_175826_b, false);
                this.setBlockState(worldIn, field_175825_e, j1, 0, 10, structureBoundingBoxIn);
                this.setBlockState(worldIn, field_175825_e, j1, 0, 12, structureBoundingBoxIn);
                this.setBlockState(worldIn, field_175825_e, j1, 4, 10, structureBoundingBoxIn);
                this.setBlockState(worldIn, field_175825_e, j1, 4, 12, structureBoundingBoxIn);
                j1 = 12;
            }
            j1 = 8;
            for (int l1 = 0; l1 < 2; ++l1) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, j1, 0, 7, j1, 2, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, j1, 0, 14, j1, 2, 14, field_175826_b, field_175826_b, false);
                j1 = 14;
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 3, 8, 8, 3, 13, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 14, 3, 8, 14, 3, 13, field_175827_c, field_175827_c, false);
            this.func_175817_a(worldIn, structureBoundingBoxIn, 11, 5, 13);
        }
        return true;
    }
}
