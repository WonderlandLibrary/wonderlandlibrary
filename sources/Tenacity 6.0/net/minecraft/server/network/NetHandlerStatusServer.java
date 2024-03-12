// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.network;

import net.minecraft.util.ChatComponentText;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.Packet;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.status.INetHandlerStatusServer;

public class NetHandlerStatusServer implements INetHandlerStatusServer
{
    private static final IChatComponent EXIT_MESSAGE;
    private final MinecraftServer server;
    private final NetworkManager networkManager;
    private boolean handled;
    
    public NetHandlerStatusServer(final MinecraftServer serverIn, final NetworkManager netManager) {
        this.server = serverIn;
        this.networkManager = netManager;
    }
    
    @Override
    public void onDisconnect(final IChatComponent reason) {
    }
    
    @Override
    public void processServerQuery(final C00PacketServerQuery packetIn) {
        if (this.handled) {
            this.networkManager.closeChannel(NetHandlerStatusServer.EXIT_MESSAGE);
        }
        else {
            this.handled = true;
            this.networkManager.sendPacket(new S00PacketServerInfo(this.server.getServerStatusResponse()));
        }
    }
    
    @Override
    public void processPing(final C01PacketPing packetIn) {
        this.networkManager.sendPacket(new S01PacketPong(packetIn.getClientTime()));
        this.networkManager.closeChannel(NetHandlerStatusServer.EXIT_MESSAGE);
    }
    
    static {
        EXIT_MESSAGE = new ChatComponentText("Status request has been handled.");
    }
}
