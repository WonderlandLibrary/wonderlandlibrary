/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IServerData;
import net.ccbluex.liquidbounce.ui.client.GuiMainMenu;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class ServerUtils
extends MinecraftInstance {
    public static IServerData serverData;

    public static void connectToLastServer() {
        if (serverData == null) {
            return;
        }
        mc.displayGuiScreen(classProvider.createGuiConnecting(classProvider.createGuiMultiplayer(classProvider.wrapGuiScreen(new GuiMainMenu())), mc, serverData));
    }

    public static String getRemoteIp() {
        IServerData serverData;
        String serverIp = "Singleplayer";
        if (mc.getTheWorld().isRemote() && (serverData = mc.getCurrentServerData()) != null) {
            serverIp = serverData.getServerIP();
        }
        return serverIp;
    }
}

