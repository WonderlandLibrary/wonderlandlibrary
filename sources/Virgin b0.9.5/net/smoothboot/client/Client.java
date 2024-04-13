package net.smoothboot.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.smoothboot.client.mixinterface.IMinecraftClient;
import net.smoothboot.client.util.RotationFaker;

public enum Client {
    INSTANCE;


    public static MinecraftClient mc;
    public static IMinecraftClient imc;

    private RotationFaker rotationFaker;

    public RotationFaker getRotationFaker()
    {
        return rotationFaker;
    }
    public void initialize() {
        mc = MinecraftClient.getInstance();
        imc = (IMinecraftClient)mc;
    }

    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }
    public static ClientPlayerEntity me() {
        MinecraftClient client = getClient();
        return client == null ? null : client.player;
    }
}
