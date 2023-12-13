package me.aidanmees.trivia.client.bungeehack;


import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.client.Minecraft;
import net.minecraft.network.AbstractPacket;

public interface SCMC {

    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final trivia tr = new trivia();
}

