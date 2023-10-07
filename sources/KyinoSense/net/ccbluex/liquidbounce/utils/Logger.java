/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.IChatComponent
 */
package net.ccbluex.liquidbounce.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class Logger {
    public static Minecraft mc = Minecraft.func_71410_x();

    public static void printmessage(String message) {
        Logger.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(message));
    }

    public static void printinfo(String message) {
        Logger.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText("\u00a77[\u00a79KyinoSense\u00a77]:" + message));
    }

    public static void printconsolemessage(String s) {
        System.out.println("[KyinoSense]:" + s);
    }
}

