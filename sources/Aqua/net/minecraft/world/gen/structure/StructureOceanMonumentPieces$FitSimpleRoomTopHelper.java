package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

static class StructureOceanMonumentPieces.FitSimpleRoomTopHelper
implements StructureOceanMonumentPieces.MonumentRoomFitHelper {
    private StructureOceanMonumentPieces.FitSimpleRoomTopHelper() {
    }

    public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition p_175969_1_) {
        return !p_175969_1_.field_175966_c[EnumFacing.WEST.getIndex()] && !p_175969_1_.field_175966_c[EnumFacing.EAST.getIndex()] && !p_175969_1_.field_175966_c[EnumFacing.NORTH.getIndex()] && !p_175969_1_.field_175966_c[EnumFacing.SOUTH.getIndex()] && !p_175969_1_.field_175966_c[EnumFacing.UP.getIndex()];
    }

    public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing p_175968_1_, StructureOceanMonumentPieces.RoomDefinition p_175968_2_, Random p_175968_3_) {
        p_175968_2_.field_175963_d = true;
        return new StructureOceanMonumentPieces.SimpleTopRoom(p_175968_1_, p_175968_2_, p_175968_3_);
    }
}
