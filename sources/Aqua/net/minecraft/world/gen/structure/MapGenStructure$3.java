package net.minecraft.world.gen.structure;

import java.util.concurrent.Callable;

class MapGenStructure.3
implements Callable<String> {
    MapGenStructure.3() {
    }

    public String call() throws Exception {
        return MapGenStructure.this.getClass().getCanonicalName();
    }
}
