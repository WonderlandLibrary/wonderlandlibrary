// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.network;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.network.status.server.SPacketPong;
import net.minecraft.network.status.client.CPacketPing;
import net.minecraft.network.Packet;
import net.minecraft.network.status.server.SPacketServerInfo;
import net.minecraft.network.status.client.CPacketServerQuery;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.status.INetHandlerStatusServer;

public class NetHandlerStatusServer implements INetHandlerStatusServer
{
    private static final ITextComponent EXIT_MESSAGE;
    private final MinecraftServer server;
    private final NetworkManager networkManager;
    private boolean handled;
    
    public NetHandlerStatusServer(final MinecraftServer serverIn, final NetworkManager netManager) {
        this.server = serverIn;
        this.networkManager = netManager;
    }
    
    @Override
    public void onDisconnect(final ITextComponent reason) {
    }
    
    @Override
    public void processServerQuery(final CPacketServerQuery packetIn) {
        if (this.handled) {
            this.networkManager.closeChannel(NetHandlerStatusServer.EXIT_MESSAGE);
        }
        else {
            this.handled = true;
            this.networkManager.sendPacket(new SPacketServerInfo(this.server.getServerStatusResponse()));
        }
    }
    
    @Override
    public void processPing(final CPacketPing packetIn) {
        this.networkManager.sendPacket(new SPacketPong(packetIn.getClientTime()));
        this.networkManager.closeChannel(NetHandlerStatusServer.EXIT_MESSAGE);
    }
    
    static {
        EXIT_MESSAGE = new TextComponentString("Status request has been handled.");
    }
}
