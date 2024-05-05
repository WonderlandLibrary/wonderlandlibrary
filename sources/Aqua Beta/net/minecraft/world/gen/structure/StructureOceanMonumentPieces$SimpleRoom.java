package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

public static class StructureOceanMonumentPieces.SimpleRoom
extends StructureOceanMonumentPieces.Piece {
    private int field_175833_o;

    public StructureOceanMonumentPieces.SimpleRoom() {
    }

    public StructureOceanMonumentPieces.SimpleRoom(EnumFacing p_i45587_1_, StructureOceanMonumentPieces.RoomDefinition p_i45587_2_, Random p_i45587_3_) {
        super(1, p_i45587_1_, p_i45587_2_, 1, 1, 1);
        this.field_175833_o = p_i45587_3_.nextInt(3);
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        boolean flag;
        if (this.field_175830_k.field_175967_a / 25 > 0) {
            this.func_175821_a(worldIn, structureBoundingBoxIn, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()]);
        }
        if (this.field_175830_k.field_175965_b[EnumFacing.UP.getIndex()] == null) {
            this.func_175819_a(worldIn, structureBoundingBoxIn, 1, 4, 1, 6, 4, 6, field_175828_a);
        }
        boolean bl = flag = this.field_175833_o != 0 && randomIn.nextBoolean() && !this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()] && !this.field_175830_k.field_175966_c[EnumFacing.UP.getIndex()] && this.field_175830_k.func_175960_c() > 1;
        if (this.field_175833_o == 0) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 2, 1, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 2, 3, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 2, 2, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 2, 2, 0, field_175828_a, field_175828_a, false);
            this.setBlockState(worldIn, field_175825_e, 1, 2, 1, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 0, 7, 1, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 0, 7, 3, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 0, 7, 2, 2, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 0, 6, 2, 0, field_175828_a, field_175828_a, false);
            this.setBlockState(worldIn, field_175825_e, 6, 2, 1, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 5, 2, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 5, 2, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 5, 0, 2, 7, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 7, 2, 2, 7, field_175828_a, field_175828_a, false);
            this.setBlockState(worldIn, field_175825_e, 1, 2, 6, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 5, 7, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 5, 7, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 5, 7, 2, 7, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 7, 6, 2, 7, field_175828_a, field_175828_a, false);
            this.setBlockState(worldIn, field_175825_e, 6, 2, 6, structureBoundingBoxIn);
            if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 0, 4, 3, 0, field_175826_b, field_175826_b, false);
            } else {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 0, 4, 3, 1, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 0, 4, 2, 0, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 1, 1, field_175826_b, field_175826_b, false);
            }
            if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 7, 4, 3, 7, field_175826_b, field_175826_b, false);
            } else {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 6, 4, 3, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 7, 4, 2, 7, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 6, 4, 1, 7, field_175826_b, field_175826_b, false);
            }
            if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 3, 0, 3, 4, field_175826_b, field_175826_b, false);
            } else {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 3, 1, 3, 4, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 3, 0, 2, 4, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 3, 1, 1, 4, field_175826_b, field_175826_b, false);
            }
            if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 3, 7, 3, 4, field_175826_b, field_175826_b, false);
            } else {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 3, 7, 3, 4, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 3, 7, 2, 4, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 3, 7, 1, 4, field_175826_b, field_175826_b, false);
            }
        } else if (this.field_175833_o == 1) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 2, 2, 3, 2, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 5, 2, 3, 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 5, 5, 3, 5, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 2, 5, 3, 2, field_175826_b, field_175826_b, false);
            this.setBlockState(worldIn, field_175825_e, 2, 2, 2, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175825_e, 2, 2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175825_e, 5, 2, 5, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175825_e, 5, 2, 2, structureBoundingBoxIn);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 1, 3, 0, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 3, 1, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 7, 1, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 6, 0, 3, 6, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 7, 7, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 6, 7, 3, 6, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 0, 7, 3, 0, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 1, 7, 3, 1, field_175826_b, field_175826_b, false);
            this.setBlockState(worldIn, field_175828_a, 1, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175828_a, 0, 2, 1, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175828_a, 1, 2, 7, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175828_a, 0, 2, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175828_a, 6, 2, 7, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175828_a, 7, 2, 6, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175828_a, 6, 2, 0, structureBoundingBoxIn);
            this.setBlockState(worldIn, field_175828_a, 7, 2, 1, structureBoundingBoxIn);
            if (!this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 6, 3, 0, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 6, 2, 0, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 6, 1, 0, field_175826_b, field_175826_b, false);
            }
            if (!this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 7, 6, 3, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 7, 6, 2, 7, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 6, 1, 7, field_175826_b, field_175826_b, false);
            }
            if (!this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 1, 0, 3, 6, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 1, 0, 2, 6, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 0, 1, 6, field_175826_b, field_175826_b, false);
            }
            if (!this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 1, 7, 3, 6, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 1, 7, 2, 6, field_175828_a, field_175828_a, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 1, 7, 1, 6, field_175826_b, field_175826_b, false);
            }
        } else if (this.field_175833_o == 2) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 0, 7, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 6, 1, 0, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 6, 1, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 2, 7, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 0, 7, 2, 7, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 6, 2, 0, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 7, 6, 2, 7, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 0, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 0, 7, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 6, 3, 0, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 7, 6, 3, 7, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 2, 4, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, field_175827_c, field_175827_c, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, field_175827_c, field_175827_c, false);
            if (this.field_175830_k.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
            }
            if (this.field_175830_k.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, false);
            }
            if (this.field_175830_k.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.func_181655_a(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, false);
            }
            if (this.field_175830_k.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.func_181655_a(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 2, 4, false);
            }
        }
        if (flag) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 3, 4, 1, 4, field_175826_b, field_175826_b, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 3, 4, 2, 4, field_175828_a, field_175828_a, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 3, 3, 4, 3, 4, field_175826_b, field_175826_b, false);
        }
        return true;
    }
}
