/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.multiplayer.GuiConnecting
 *  net.minecraft.client.multiplayer.ServerData
 *  net.minecraft.entity.Entity
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.utils;

import me.report.liquidware.gui.GuiMainMenu;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class ServerUtils
extends MinecraftInstance {
    public static ServerData serverData;

    public static void connectToLastServer() {
        if (serverData == null) {
            return;
        }
        mc.func_147108_a((GuiScreen)new GuiConnecting((GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()), mc, serverData));
    }

    public static String getRemoteIp() {
        ServerData serverData;
        String serverIp = "Singleplayer";
        if (ServerUtils.mc.field_71441_e.field_72995_K && (serverData = mc.func_147104_D()) != null) {
            serverIp = serverData.field_78845_b;
        }
        return serverIp;
    }

    public static boolean isHypixelLobby() {
        if (ServerUtils.mc.field_71441_e == null) {
            return false;
        }
        String target = "CLICK TO PLAY";
        for (Entity entity : ServerUtils.mc.field_71441_e.field_72996_f) {
            if (!entity.func_70005_c_().startsWith("\u00a7e\u00a7l") || !entity.func_70005_c_().equals("\u00a7e\u00a7l" + target)) continue;
            return true;
        }
        return false;
    }

    public static boolean isHypixelDomain(String s1) {
        int chars = 0;
        String str = "www.hypixel.net";
        for (char c : str.toCharArray()) {
            if (!s1.contains(String.valueOf(c))) continue;
            ++chars;
        }
        return chars == str.length();
    }
}

