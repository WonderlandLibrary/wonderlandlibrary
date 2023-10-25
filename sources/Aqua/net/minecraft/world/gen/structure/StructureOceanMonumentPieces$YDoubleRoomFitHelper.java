package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

static class StructureOceanMonumentPieces.YDoubleRoomFitHelper
implements StructureOceanMonumentPieces.MonumentRoomFitHelper {
    private StructureOceanMonumentPieces.YDoubleRoomFitHelper() {
    }

    public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_) {
        return p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()] && !p_175969_1_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d;
    }

    public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_) {
        p_175968_2_.field_175963_d = true;
        p_175968_2_.field_175965_b[EnumFacing.UP.getIndex()].field_175963_d = true;
        return new StructureOceanMonumentPieces.DoubleYRoom(p_175968_1_, p_175968_2_, p_175968_3_);
    }
}
