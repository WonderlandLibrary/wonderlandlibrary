package net.minecraft.client.main;

import net.minecraft.client.Minecraft;

static final class Main.2
extends Thread {
    Main.2(String arg0) {
        super(arg0);
    }

    public void run() {
        Minecraft.stopIntegratedServer();
    }
}
