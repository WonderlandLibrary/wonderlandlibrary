package net.minecraft.client.multiplayer;

import java.util.concurrent.Callable;
import net.minecraft.client.multiplayer.WorldClient;

/*
 * Exception performing whole class analysis ignored.
 */
class WorldClient.1
implements Callable<String> {
    WorldClient.1() {
    }

    public String call() {
        return WorldClient.access$000((WorldClient)WorldClient.this).size() + " total; " + WorldClient.access$000((WorldClient)WorldClient.this).toString();
    }
}
