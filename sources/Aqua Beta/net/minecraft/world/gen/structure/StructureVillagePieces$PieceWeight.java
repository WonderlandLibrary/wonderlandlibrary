package net.minecraft.world.gen.structure;

import net.minecraft.world.gen.structure.StructureVillagePieces;

public static class StructureVillagePieces.PieceWeight {
    public Class<? extends StructureVillagePieces.Village> villagePieceClass;
    public final int villagePieceWeight;
    public int villagePiecesSpawned;
    public int villagePiecesLimit;

    public StructureVillagePieces.PieceWeight(Class<? extends StructureVillagePieces.Village> p_i2098_1_, int p_i2098_2_, int p_i2098_3_) {
        this.villagePieceClass = p_i2098_1_;
        this.villagePieceWeight = p_i2098_2_;
        this.villagePiecesLimit = p_i2098_3_;
    }

    public boolean canSpawnMoreVillagePiecesOfType(int p_75085_1_) {
        return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
    }

    public boolean canSpawnMoreVillagePieces() {
        return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
    }
}
