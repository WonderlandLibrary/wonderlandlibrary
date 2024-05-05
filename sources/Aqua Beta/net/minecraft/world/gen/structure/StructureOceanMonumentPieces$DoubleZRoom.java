package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

public static class StructureOceanMonumentPieces.DoubleZRoom
extends StructureOceanMonumentPieces.Piece {
    public StructureOceanMonumentPieces.DoubleZRoom() {
    }

    public StructureOceanMonumentPieces.DoubleZRoom(EnumFacing p_i45593_1_, StructureOceanMonumentPieces.RoomDefinition p_i45593_2_, Random p_i45593_3_) {
        super(1, p_i45593_1_, p_i45593_2_, 1, 1, 2);
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = this.field_175830_k.field_175965_b[EnumFacing.NORTH.getIndex()];
        StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition1 = this.field_175830_k;
        if (this.field_175830_k.field_175967_a / 25 > 0) {
            this.func_175821_a(worldIn, structureBoundingBoxIn, 0, 8, structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.DOWN.getIndex()]);
            this.func_175821_a(worldIn, structureBoundingBoxIn, 0, 0, structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.DOWN.getIndex()]);
        }
        if (structureoceanmonumentpieces$roomdefinition1.field_175965_b[EnumFacing.UP.getIndex()] == null) {
            this.func_175819_a(worldIn, structureBoundingBoxIn, 1, 4, 1, 6, 4, 7, field_175828_a);
        }
        if (structureoceanmonumentpieces$roomdefinition.field_175965_b[EnumFacing.UP.getIndex()] == null) {
            this.func_175819_a(worldIn, structureBoundingBoxIn, 1, 4, 8, 6, 4, 14, field_175828_a);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 0, 3, 15, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 3, 0, 7, 3, 15, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 7, 3, 0, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 15, 6, 3, 15, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 2, 15, field_175828_a, field_175828_a, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 2, 0, 7, 2, 15, field_175828_a, field_175828_a, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 7, 2, 0, field_175828_a, field_175828_a, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 15, 6, 2, 15, field_175828_a, field_175828_a, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 15, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 1, 0, 7, 1, 15, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 7, 1, 0, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 15, 6, 1, 15, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 1, 1, 2, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 1, 6, 1, 2, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 1, 1, 3, 2, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 1, 6, 3, 2, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 13, 1, 1, 14, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 13, 6, 1, 14, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 13, 1, 3, 14, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 3, 13, 6, 3, 14, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 6, 2, 3, 6, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 6, 5, 3, 6, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 9, 2, 3, 9, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 9, 5, 3, 9, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 6, 4, 2, 6, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 2, 9, 4, 2, 9, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, 7, 2, 2, 8, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 7, 5, 2, 8, field_175826_b, field_175826_b, false);
        this.setBlockState(worldIn, field_175825_e, 2, 2, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, field_175825_e, 5, 2, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, field_175825_e, 2, 2, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, field_175825_e, 5, 2, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, field_175826_b, 2, 3, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, field_175826_b, 5, 3, 5, structureBoundingBoxIn);
        this.setBlockState(worldIn, field_175826_b, 2, 3, 10, structureBoundingBoxIn);
        this.setBlockState(worldIn, field_175826_b, 5, 3, 10, structureBoundingBoxIn);
        if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 2, 0, false);
        }
        if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.EAST.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 7, 1, 3, 7, 2, 4, false);
        }
        if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.WEST.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 0, 1, 3, 0, 2, 4, false);
        }
        if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.NORTH.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 3, 1, 15, 4, 2, 15, false);
        }
        if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.WEST.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 0, 1, 11, 0, 2, 12, false);
        }
        if (structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.EAST.getIndex()]) {
            this.func_181655_a(worldIn, structureBoundingBoxIn, 7, 1, 11, 7, 2, 12, false);
        }
        return true;
    }
}
