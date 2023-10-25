package net.minecraft.world.gen.structure;

import java.util.concurrent.Callable;

class MapGenStructure.1
implements Callable<String> {
    final /* synthetic */ int val$chunkX;
    final /* synthetic */ int val$chunkZ;

    MapGenStructure.1(int n, int n2) {
        this.val$chunkX = n;
        this.val$chunkZ = n2;
    }

    public String call() throws Exception {
        return MapGenStructure.this.canSpawnStructureAtCoords(this.val$chunkX, this.val$chunkZ) ? "True" : "False";
    }
}
