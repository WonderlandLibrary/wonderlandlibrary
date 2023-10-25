package net.minecraft.world;

import java.util.concurrent.Callable;

class World.3
implements Callable<String> {
    World.3() {
    }

    public String call() {
        return World.this.playerEntities.size() + " total; " + World.this.playerEntities.toString();
    }
}
