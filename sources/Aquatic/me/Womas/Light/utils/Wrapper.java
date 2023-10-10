package me.Womas.Light.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;

public class Wrapper {

    public static Minecraft getMc() {
        return Minecraft.getMinecraft();
    }

    public static FontRenderer getFr() {
        return Minecraft.getMinecraft().fontRendererObj;
    }

    public static EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }



}
