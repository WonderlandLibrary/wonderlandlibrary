/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.multiplayer.GuiConnecting
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IServerData;
import net.ccbluex.liquidbounce.injection.backend.ServerDataImplKt;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ui.GuiMainMenu;

@SideOnly(value=Side.CLIENT)
public final class ServerUtils
extends MinecraftInstance {
    public static IServerData serverData;

    public static void connectToLastServer() {
        if (serverData == null) {
            return;
        }
        mc2.func_147108_a((GuiScreen)new GuiConnecting((GuiScreen)new GuiMultiplayer((GuiScreen)new GuiMainMenu()), mc2, ServerDataImplKt.unwrap(serverData)));
    }

    public static String getRemoteIp() {
        IServerData serverData;
        String serverIp = "Singleplayer";
        if (mc.getTheWorld() != null && mc.getTheWorld().isRemote() && (serverData = mc.getCurrentServerData()) != null) {
            serverIp = serverData.getServerIP();
        }
        return serverIp;
    }
}

