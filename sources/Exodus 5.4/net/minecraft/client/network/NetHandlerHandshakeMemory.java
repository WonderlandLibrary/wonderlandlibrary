/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.network;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.util.IChatComponent;

public class NetHandlerHandshakeMemory
implements INetHandlerHandshakeServer {
    private final MinecraftServer mcServer;
    private final NetworkManager networkManager;

    @Override
    public void onDisconnect(IChatComponent iChatComponent) {
    }

    public NetHandlerHandshakeMemory(MinecraftServer minecraftServer, NetworkManager networkManager) {
        this.mcServer = minecraftServer;
        this.networkManager = networkManager;
    }

    @Override
    public void processHandshake(C00Handshake c00Handshake) {
        this.networkManager.setConnectionState(c00Handshake.getRequestedState());
        this.networkManager.setNetHandler(new NetHandlerLoginServer(this.mcServer, this.networkManager));
    }
}

