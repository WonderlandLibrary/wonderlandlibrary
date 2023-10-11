package com.initial.utils;

import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.multiplayer.*;

public class Util
{
    public static Minecraft mc;
    public static FontRenderer fr;
    
    public static EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
    
    public static EntityPlayerSP player() {
        return Util.mc.thePlayer;
    }
    
    public static PlayerControllerMP playerController() {
        return Util.mc.playerController;
    }
    
    public static WorldClient world() {
        return Util.mc.theWorld;
    }
    
    static {
        Util.mc = Minecraft.getMinecraft();
        Util.fr = Util.mc.fontRendererObj;
    }
}
