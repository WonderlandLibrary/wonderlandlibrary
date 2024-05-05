package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

public static class StructureOceanMonumentPieces.DoubleXYRoom
extends StructureOceanMonumentPieces.Piece {
    public StructureOceanMonumentPieces.DoubleXYRoom() {
    }

    public StructureOceanMonumentPieces.DoubleXYRoom(EnumFacing p_i45596_1_, StructureOceanMonumentPieces.RoomDefinition p_i45596_2_, Random p_i45596_3_) {
        super(1, p_i45596_1_, p_i45596_2_, 2, 2, 1);
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = this.field_175830_k.field_175965_b[EnumFacing.EAST.getIndex()];
        StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition1 = this.field_175830_k;
        StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition2 = structureoceanmonumentpieces$roomdefinition1.field_175965_b[EnumFacing.UP.getIndex()];
        StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition3 = structureoceanmonumentpieces$roomdefinition.field_175965_b[EnumFacing.UP.getIndex()];
        if (this.field_175830_k.field_175967_a / 25 > 0) {
            this.func_175821_a(worldIn, structureBoundingBoxIn, 8, 0, structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.DOWN.getIndex()]);
            this.func_175821_a(worldIn, structureBoundingBoxIn, 0, 0, structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.DOWN.getIndex()]);
        }
        if (structureoceanmonumentpieces$roomdefinition2.field_175965_b[EnumFacing.UP.getIndex()] == null) {
            this.func_175819_a(worldIn, structureBoundingBoxIn, 1, 8, 1, 7, 8, 6, field_175828_a);
        }
        if (structureoceanmonumentpieces$roomdefinition3.field_175965_b[EnumFacing.UP.getIndex()] == null) {
            this.func_175819_a(worldIn, structureBoundingBoxIn, 8, 8, 1, 14, 8, 6, field_175828_a);
        }
        for (int i = 1; i <= 7; ++i) {
            IBlockState iblockstate = field_175826_b;
            if (i == 2 || i == 6) {
                iblockstate = field_175828_a;
            }
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i, 0, 0, i, 7, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, i, 0, 15, i, 7, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 0, 15, i, 0, iblockstate, iblockstate, false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, i, 7, 14, i, 7, iblockstate, iblockstate, false);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 3, 2, 7, 4, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 2, 4, 7, 2, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 1, 5, 4, 7, 5, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 13, 1, 3, 13, 7, 4, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 1, 2, 12, 7, 2, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 11, 1, 5, 12, 7, 5, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 3, 5, 3, 4, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 1, 3, 10, 3, 4, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 7, 2, 10, 7, 5, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 2, 5, 7, 2, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 5, 2, 10, 7, 2, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 5, 5, 5, 7, 5, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 10, 5, 5, 10, 7, 5, field_175826_b, field_175826_b, false);
        this.setBlockState(worldIn, field_175826_b, 6, 6, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, field_175826_b, 9, 6, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, field_175826_b, 6, 6, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, field_175826_b, 9, 6, 5, structureBoundingBoxIn);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 3, 6, 4, 4, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 4, 3, 10, 4, 4, field_175826_b, field_175826_b, false);
        this.setBlockState(worldIn, field_175825_e, 5, 4, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, field_175825_e, 5, 4, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, field_175825_e, 10, 4, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, field_175825_e, 10, 4, 5, structureBoundingBoxIn);
        if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
        }
        if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.NORTH.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 2, 7, false);
        }
        if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.WEST.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, false);
        }
        if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 11, 1, 0, 12, 2, 0, false);
        }
        if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.NORTH.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 11, 1, 7, 12, 2, 7, false);
        }
        if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.EAST.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 15, 1, 3, 15, 2, 4, false);
        }
        if (structureoceanmonumentpieces$roomdefinition2.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 3, 5, 0, 4, 6, 0, false);
        }
        if (structureoceanmonumentpieces$roomdefinition2.field_175966_c[EnumFacing.NORTH.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 3, 5, 7, 4, 6, 7, false);
        }
        if (structureoceanmonumentpieces$roomdefinition2.field_175966_c[EnumFacing.WEST.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 0, 5, 3, 0, 6, 4, false);
        }
        if (structureoceanmonumentpieces$roomdefinition3.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 11, 5, 0, 12, 6, 0, false);
        }
        if (structureoceanmonumentpieces$roomdefinition3.field_175966_c[EnumFacing.NORTH.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 11, 5, 7, 12, 6, 7, false);
        }
        if (structureoceanmonumentpieces$roomdefinition3.field_175966_c[EnumFacing.EAST.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 15, 5, 3, 15, 6, 4, false);
        }
        return true;
    }
}
