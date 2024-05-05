package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

public static class StructureOceanMonumentPieces.DoubleXRoom
extends StructureOceanMonumentPieces.Piece {
    public StructureOceanMonumentPieces.DoubleXRoom() {
    }

    public StructureOceanMonumentPieces.DoubleXRoom(EnumFacing p_i45597_1_, StructureOceanMonumentPieces.RoomDefinition p_i45597_2_, Random p_i45597_3_) {
        super(1, p_i45597_1_, p_i45597_2_, 2, 1, 1);
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = this.field_175830_k.field_175965_b[EnumFacing.EAST.getIndex()];
        StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition1 = this.field_175830_k;
        if (this.field_175830_k.field_175967_a / 25 > 0) {
            this.func_175821_a(worldIn, structureBoundingBoxIn, 8, 0, structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.DOWN.getIndex()]);
            this.func_175821_a(worldIn, structureBoundingBoxIn, 0, 0, structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.DOWN.getIndex()]);
        }
        if (structureoceanmonumentpieces$roomdefinition1.field_175965_b[EnumFacing.UP.getIndex()] == null) {
            this.func_175819_a(worldIn, structureBoundingBoxIn, 1, 4, 1, 7, 4, 6, field_175828_a);
        }
        if (structureoceanmonumentpieces$roomdefinition.field_175965_b[EnumFacing.UP.getIndex()] == null) {
            this.func_175819_a(worldIn, structureBoundingBoxIn, 8, 4, 1, 14, 4, 6, field_175828_a);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 0, 3, 7, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 3, 0, 15, 3, 7, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 0, 15, 3, 0, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 7, 14, 3, 7, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 2, 7, field_175828_a, field_175828_a, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 2, 0, 15, 2, 7, field_175828_a, field_175828_a, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 15, 2, 0, field_175828_a, field_175828_a, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 7, 14, 2, 7, field_175828_a, field_175828_a, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 7, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 15, 1, 0, 15, 1, 7, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 15, 1, 0, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 7, 14, 1, 7, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 0, 10, 1, 4, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 2, 0, 9, 2, 3, field_175828_a, field_175828_a, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 0, 10, 3, 4, field_175826_b, field_175826_b, false);
        this.setBlockState(worldIn, field_175825_e, 6, 2, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, field_175825_e, 9, 2, 3, structureBoundingBoxIn);
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
        return true;
    }
}
