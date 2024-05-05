package net.minecraft.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IProgressUpdate;

/*
 * Exception performing whole class analysis ignored.
 */
class MinecraftServer.1
implements IProgressUpdate {
    private long startTime = System.currentTimeMillis();

    MinecraftServer.1() {
    }

    public void displaySavingString(String message) {
    }

    public void resetProgressAndMessage(String message) {
    }

    public void setLoadingProgress(int progress) {
        if (System.currentTimeMillis() - this.startTime >= 1000L) {
            this.startTime = System.currentTimeMillis();
            MinecraftServer.access$000().info("Converting... " + progress + "%");
        }
    }

    public void setDoneWorking() {
    }

    public void displayLoadingString(String message) {
    }
}
