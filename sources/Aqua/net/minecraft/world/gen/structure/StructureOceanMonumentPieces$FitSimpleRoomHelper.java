package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

static class StructureOceanMonumentPieces.FitSimpleRoomHelper
implements StructureOceanMonumentPieces.MonumentRoomFitHelper {
    private StructureOceanMonumentPieces.FitSimpleRoomHelper() {
    }

    public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_) {
        return true;
    }

    public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_) {
        p_175968_2_.field_175963_d = true;
        return new StructureOceanMonumentPieces.SimpleRoom(p_175968_1_, p_175968_2_, p_175968_3_);
    }
}
