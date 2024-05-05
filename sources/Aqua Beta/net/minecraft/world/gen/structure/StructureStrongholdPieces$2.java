package net.minecraft.world.gen.structure;

import net.minecraft.world.gen.structure.StructureStrongholdPieces;

static final class StructureStrongholdPieces.2
extends StructureStrongholdPieces.PieceWeight {
    StructureStrongholdPieces.2(Class p_i2076_1_, int p_i2076_2_, int p_i2076_3_) {
        super(p_i2076_1_, p_i2076_2_, p_i2076_3_);
    }

    public boolean canSpawnMoreStructuresOfType(int p_75189_1_) {
        return super.canSpawnMoreStructuresOfType(p_75189_1_) && p_75189_1_ > 5;
    }
}
