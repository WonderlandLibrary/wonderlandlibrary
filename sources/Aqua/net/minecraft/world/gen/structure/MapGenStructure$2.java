package net.minecraft.world.gen.structure;

import java.util.concurrent.Callable;
import net.minecraft.world.ChunkCoordIntPair;

class MapGenStructure.2
implements Callable<String> {
    final /* synthetic */ int val$chunkX;
    final /* synthetic */ int val$chunkZ;

    MapGenStructure.2(int n, int n2) {
        this.val$chunkX = n;
        this.val$chunkZ = n2;
    }

    public String call() throws Exception {
        return String.valueOf((long)ChunkCoordIntPair.chunkXZ2Int((int)this.val$chunkX, (int)this.val$chunkZ));
    }
}
