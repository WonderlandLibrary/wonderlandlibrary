package net.minecraft.client.multiplayer;

import java.util.concurrent.Callable;
import net.minecraft.client.multiplayer.WorldClient;

/*
 * Exception performing whole class analysis ignored.
 */
class WorldClient.4
implements Callable<String> {
    WorldClient.4() {
    }

    public String call() throws Exception {
        return WorldClient.access$200((WorldClient)WorldClient.this).getIntegratedServer() == null ? "Non-integrated multiplayer server" : "Integrated singleplayer server";
    }
}
