package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

static class StructureOceanMonumentPieces.ZDoubleRoomFitHelper
implements StructureOceanMonumentPieces.MonumentRoomFitHelper {
    private StructureOceanMonumentPieces.ZDoubleRoomFitHelper() {
    }

    public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_) {
        return p_175969_1_.field_175966_c[EnumFacing.NORTH.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d;
    }

    public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_) {
        StructureOceanMonumentPieces.RoomDefinition structureoceanmonumentpieces$roomdefinition = p_175968_2_;
        if (!p_175968_2_.field_175966_c[EnumFacing.NORTH.getIndex()] || p_175968_2_.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d) {
            structureoceanmonumentpieces$roomdefinition = p_175968_2_.field_175965_b[EnumFacing.SOUTH.getIndex()];
        }
        structureoceanmonumentpieces$roomdefinition.field_175963_d = true;
        structureoceanmonumentpieces$roomdefinition.field_175965_b[EnumFacing.NORTH.getIndex()].field_175963_d = true;
        return new StructureOceanMonumentPieces.DoubleZRoom(p_175968_1_, structureoceanmonumentpieces$roomdefinition, p_175968_3_);
    }
}
