package net.minecraft.server;

import java.util.concurrent.Callable;

class MinecraftServer.2
implements Callable<String> {
    MinecraftServer.2() {
    }

    public String call() throws Exception {
        return MinecraftServer.this.theProfiler.profilingEnabled ? MinecraftServer.this.theProfiler.getNameOfLastSection() : "N/A (disabled)";
    }
}
