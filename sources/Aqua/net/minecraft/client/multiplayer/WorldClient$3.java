package net.minecraft.client.multiplayer;

import java.util.concurrent.Callable;
import net.minecraft.client.multiplayer.WorldClient;

/*
 * Exception performing whole class analysis ignored.
 */
class WorldClient.3
implements Callable<String> {
    WorldClient.3() {
    }

    public String call() throws Exception {
        return WorldClient.access$200((WorldClient)WorldClient.this).thePlayer.getClientBrand();
    }
}
