package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

public static class StructureOceanMonumentPieces.DoubleYRoom
extends StructureOceanMonumentPieces.Piece {
    public StructureOceanMonumentPieces.DoubleYRoom() {
    }

    public StructureOceanMonumentPieces.DoubleYRoom(EnumFacing p_i45595_1_, StructureOceanMonumentPieces.RoomDefinition p_i45595_2_, Random p_i45595_3_) {
        super(1, p_i45595_1_, p_i45595_2_, 1, 2, 1);
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.field_175830_k.field_175967_a / 25 > 0) {
            this.func_175821_a(worldIn, structureBoundingBoxIn, 0, 0, this.field_175830_k.field_175966_c[EnumFacing.DOWN.getIndex()]);
        }
        StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = this.field_175830_k.field_175965_b[EnumFacing.UP.getIndex()];
        if (structureoceanmonumentpieces$roomdefinition.field_175965_b[EnumFacing.UP.getIndex()] == null) {
            this.func_175819_a(worldIn, structureBoundingBoxIn, 1, 8, 1, 6, 8, 6, field_175828_a);
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 0, 0, 4, 7, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 0, 7, 4, 7, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 0, 6, 4, 0, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 7, 6, 4, 7, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 1, 2, 4, 2, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 1, 4, 2, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 1, 5, 4, 2, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 2, 6, 4, 2, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 5, 2, 4, 6, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 5, 1, 4, 5, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 4, 5, 5, 4, 6, field_175826_b, field_175826_b, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 5, 6, 4, 5, field_175826_b, field_175826_b, false);
        StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition1 = this.field_175830_k;
        for (int i = 1; i <= 5; i += 4) {
            int j = 0;
            if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.SOUTH.getIndex()]) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, i, j, 2, i + 2, j, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, i, j, 5, i + 2, j, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, i + 2, j, 4, i + 2, j, field_175826_b, field_175826_b, false);
            } else {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i, j, 7, i + 2, j, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i + 1, j, 7, i + 1, j, field_175828_a, field_175828_a, false);
            }
            j = 7;
            if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.NORTH.getIndex()]) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, i, j, 2, i + 2, j, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 5, i, j, 5, i + 2, j, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, i + 2, j, 4, i + 2, j, field_175826_b, field_175826_b, false);
            } else {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i, j, 7, i + 2, j, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, i + 1, j, 7, i + 1, j, field_175828_a, field_175828_a, false);
            }
            int k = 0;
            if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.WEST.getIndex()]) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 2, k, i + 2, 2, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 5, k, i + 2, 5, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i + 2, 3, k, i + 2, 4, field_175826_b, field_175826_b, false);
            } else {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 0, k, i + 2, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i + 1, 0, k, i + 1, 7, field_175828_a, field_175828_a, false);
            }
            k = 7;
            if (structureoceanmonumentpieces$roomdefinition1.field_175966_c[EnumFacing.EAST.getIndex()]) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 2, k, i + 2, 2, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 5, k, i + 2, 5, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i + 2, 3, k, i + 2, 4, field_175826_b, field_175826_b, false);
            } else {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i, 0, k, i + 2, 7, field_175826_b, field_175826_b, false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, k, i + 1, 0, k, i + 1, 7, field_175828_a, field_175828_a, false);
            }
            structureoceanmonumentpieces$roomdefinition1 = structureoceanmonumentpieces$roomdefinition;
        }
        return true;
    }
}
