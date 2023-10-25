/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.server.network.NetHandlerStatusServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class NetHandlerHandshakeTCP
implements INetHandlerHandshakeServer {
    private final NetworkManager networkManager;
    private final MinecraftServer server;

    public NetHandlerHandshakeTCP(MinecraftServer minecraftServer, NetworkManager networkManager) {
        this.server = minecraftServer;
        this.networkManager = networkManager;
    }

    @Override
    public void processHandshake(C00Handshake c00Handshake) {
        switch (c00Handshake.getRequestedState()) {
            case LOGIN: {
                this.networkManager.setConnectionState(EnumConnectionState.LOGIN);
                if (c00Handshake.getProtocolVersion() > 47) {
                    ChatComponentText chatComponentText = new ChatComponentText("Outdated server! I'm still on 1.8.8");
                    this.networkManager.sendPacket(new S00PacketDisconnect(chatComponentText));
                    this.networkManager.closeChannel(chatComponentText);
                    break;
                }
                if (c00Handshake.getProtocolVersion() < 47) {
                    ChatComponentText chatComponentText = new ChatComponentText("Outdated client! Please use 1.8.8");
                    this.networkManager.sendPacket(new S00PacketDisconnect(chatComponentText));
                    this.networkManager.closeChannel(chatComponentText);
                    break;
                }
                this.networkManager.setNetHandler(new NetHandlerLoginServer(this.server, this.networkManager));
                break;
            }
            case STATUS: {
                this.networkManager.setConnectionState(EnumConnectionState.STATUS);
                this.networkManager.setNetHandler(new NetHandlerStatusServer(this.server, this.networkManager));
                break;
            }
            default: {
                throw new UnsupportedOperationException("Invalid intention " + (Object)((Object)c00Handshake.getRequestedState()));
            }
        }
    }

    @Override
    public void onDisconnect(IChatComponent iChatComponent) {
    }
}

