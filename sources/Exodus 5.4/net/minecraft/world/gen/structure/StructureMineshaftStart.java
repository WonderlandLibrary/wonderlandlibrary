/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;
import net.minecraft.world.gen.structure.StructureStart;

public class StructureMineshaftStart
extends StructureStart {
    public StructureMineshaftStart(World world, Random random, int n, int n2) {
        super(n, n2);
        StructureMineshaftPieces.Room room = new StructureMineshaftPieces.Room(0, random, (n << 4) + 2, (n2 << 4) + 2);
        this.components.add(room);
        room.buildComponent(room, this.components, random);
        this.updateBoundingBox();
        this.markAvailableHeight(world, random, 10);
    }

    public StructureMineshaftStart() {
    }
}

