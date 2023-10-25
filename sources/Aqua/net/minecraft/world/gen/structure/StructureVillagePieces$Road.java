package net.minecraft.world.gen.structure;

import net.minecraft.world.gen.structure.StructureVillagePieces;

public static abstract class StructureVillagePieces.Road
extends StructureVillagePieces.Village {
    public StructureVillagePieces.Road() {
    }

    protected StructureVillagePieces.Road(StructureVillagePieces.Start start, int type) {
        super(start, type);
    }
}
