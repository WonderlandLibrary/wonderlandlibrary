package net.minecraft.server;

import java.util.concurrent.Callable;
import net.minecraft.server.MinecraftServer;

/*
 * Exception performing whole class analysis ignored.
 */
class MinecraftServer.3
implements Callable<String> {
    MinecraftServer.3() {
    }

    public String call() {
        return MinecraftServer.access$100((MinecraftServer)MinecraftServer.this).getCurrentPlayerCount() + " / " + MinecraftServer.access$100((MinecraftServer)MinecraftServer.this).getMaxPlayers() + "; " + MinecraftServer.access$100((MinecraftServer)MinecraftServer.this).getPlayerList();
    }
}
