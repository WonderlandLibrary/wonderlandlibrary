/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.network;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.status.INetHandlerStatusServer;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class NetHandlerStatusServer
implements INetHandlerStatusServer {
    private static final IChatComponent field_183007_a = new ChatComponentText("Status request has been handled.");
    private final NetworkManager networkManager;
    private boolean field_183008_d;
    private final MinecraftServer server;

    @Override
    public void processServerQuery(C00PacketServerQuery c00PacketServerQuery) {
        if (this.field_183008_d) {
            this.networkManager.closeChannel(field_183007_a);
        } else {
            this.field_183008_d = true;
            this.networkManager.sendPacket(new S00PacketServerInfo(this.server.getServerStatusResponse()));
        }
    }

    @Override
    public void onDisconnect(IChatComponent iChatComponent) {
    }

    @Override
    public void processPing(C01PacketPing c01PacketPing) {
        this.networkManager.sendPacket(new S01PacketPong(c01PacketPing.getClientTime()));
        this.networkManager.closeChannel(field_183007_a);
    }

    public NetHandlerStatusServer(MinecraftServer minecraftServer, NetworkManager networkManager) {
        this.server = minecraftServer;
        this.networkManager = networkManager;
    }
}

