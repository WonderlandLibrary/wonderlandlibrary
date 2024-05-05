package net.minecraft.world.storage;

import java.util.concurrent.Callable;

class WorldInfo.1
implements Callable<String> {
    WorldInfo.1() {
    }

    public String call() throws Exception {
        return String.valueOf((long)WorldInfo.this.getSeed());
    }
}
