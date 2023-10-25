package net.minecraft.world.gen.structure;

import net.minecraft.world.gen.structure.StructureStrongholdPieces;

static class StructureStrongholdPieces.PieceWeight {
    public Class<? extends StructureStrongholdPieces.Stronghold> pieceClass;
    public final int pieceWeight;
    public int instancesSpawned;
    public int instancesLimit;

    public StructureStrongholdPieces.PieceWeight(Class<? extends StructureStrongholdPieces.Stronghold> p_i2076_1_, int p_i2076_2_, int p_i2076_3_) {
        this.pieceClass = p_i2076_1_;
        this.pieceWeight = p_i2076_2_;
        this.instancesLimit = p_i2076_3_;
    }

    public boolean canSpawnMoreStructuresOfType(int p_75189_1_) {
        return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
    }

    public boolean canSpawnMoreStructures() {
        return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
    }
}
