package net.smoothboot.client.module.client;

import net.minecraft.client.MinecraftClient;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.module.Mod;

public class Hide extends Mod {

    public Hide() {
        super("Hide", "Deactivates client until restart for screenshare", Category.Client);
    }

    public static boolean hidden = false;

    protected MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void onEnable() {
        if (hidden) hidden = false;
        else hidden = true;
        mc.setScreen(null);
    }

}