/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.utils;

import dev.sakura_starring.ui.GuiLogin;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IServerData;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public final class ServerUtils
extends MinecraftInstance {
    public static IServerData serverData;

    public static String getRemoteIp() {
        IServerData iServerData;
        String string = "Singleplayer";
        if (mc.getTheWorld() != null && mc.getTheWorld().isRemote() && (iServerData = mc.getCurrentServerData()) != null) {
            string = iServerData.getServerIP();
        }
        return string;
    }

    public static boolean isHypixelDomain(String string) {
        int n = 0;
        String string2 = "www.hypixel.net";
        for (char c : string2.toCharArray()) {
            if (!string.contains(String.valueOf(c))) continue;
            ++n;
        }
        return n == string2.length();
    }

    public static void connectToLastServer() {
        if (serverData == null) {
            return;
        }
        mc.displayGuiScreen(classProvider.createGuiConnecting(classProvider.createGuiMultiplayer(classProvider.wrapGuiScreen(new GuiLogin())), mc, serverData));
    }
}

