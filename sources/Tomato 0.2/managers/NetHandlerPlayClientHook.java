/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.managers;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;

public class NetHandlerPlayClientHook
extends NetHandlerPlayClient {
    public static NetworkManager netManager;

    public NetHandlerPlayClientHook(Minecraft mc2, GuiScreen gui, NetworkManager netManager, GameProfile profile) {
        super(mc2, gui, netManager, profile);
        NetHandlerPlayClientHook.netManager = netManager;
    }
}

