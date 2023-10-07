// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.Minecraft;

public class ServerUtil
{
    public static String getRemoteIp() {
        String serverIp = "MainMenu";
        if (Minecraft.getMinecraft().isSingleplayer()) {
            serverIp = "SinglePlayer";
        }
        else if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld.isRemote) {
            final ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
            if (serverData != null) {
                serverIp = serverData.serverIP;
            }
        }
        return serverIp;
    }
}
