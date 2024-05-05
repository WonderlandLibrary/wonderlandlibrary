package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.util.BlockPos;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

public static class StructureStrongholdPieces.Stairs2
extends StructureStrongholdPieces.Stairs {
    public StructureStrongholdPieces.PieceWeight strongholdPieceWeight;
    public StructureStrongholdPieces.PortalRoom strongholdPortalRoom;
    public List<StructureComponent> field_75026_c = Lists.newArrayList();

    public StructureStrongholdPieces.Stairs2() {
    }

    public StructureStrongholdPieces.Stairs2(int p_i2083_1_, Random p_i2083_2_, int p_i2083_3_, int p_i2083_4_) {
        super(0, p_i2083_2_, p_i2083_3_, p_i2083_4_);
    }

    public BlockPos getBoundingBoxCenter() {
        return this.strongholdPortalRoom != null ? this.strongholdPortalRoom.getBoundingBoxCenter() : super.getBoundingBoxCenter();
    }
}
