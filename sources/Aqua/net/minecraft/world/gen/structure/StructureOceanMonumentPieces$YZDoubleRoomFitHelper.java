package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

static class StructureOceanMonumentPieces.YZDoubleRoomFitHelper
implements StructureOceanMonumentPieces.MonumentRoomFitHelper {
    private StructureOceanMonumentPieces.YZDoubleRoomFitHelper() {
    }

    public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_) {
        if (p_175969_1_.field_175966_c[EnumFacing.NORTH.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d && p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d) {
            StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = p_175969_1_.field_175965_b[EnumFacing.NORTH.getIndex()];
            return structureoceanmonumentpieces$roomdefinition.field_175966_c[EnumFacing.UP.getIndex()] && !structureoceanmonumentpieces$roomdefinition.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d;
        }
        return false;
    }

    public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_) {
        p_175968_2_.field_175963_d = true;
        p_175968_2_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
        p_175968_2_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
        p_175968_2_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
        return new StructureOceanMonumentPieces.DoubleYZRoom(p_175968_1_, p_175968_2_, p_175968_3_);
    }
}
