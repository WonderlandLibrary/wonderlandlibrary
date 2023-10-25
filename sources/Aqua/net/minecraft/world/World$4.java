package net.minecraft.world;

import java.util.concurrent.Callable;

class World.4
implements Callable<String> {
    World.4() {
    }

    public String call() {
        return World.this.chunkProvider.makeString();
    }
}
