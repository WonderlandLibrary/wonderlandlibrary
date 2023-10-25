package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureOceanMonumentPieces;

static interface StructureOceanMonumentPieces.MonumentRoomFitHelper {
    public boolean func_175969_a(StructureOceanMonumentPieces.RoomDefinition var1);

    public StructureOceanMonumentPieces.Piece func_175968_a(EnumFacing var1, StructureOceanMonumentPieces.RoomDefinition var2, Random var3);
}
