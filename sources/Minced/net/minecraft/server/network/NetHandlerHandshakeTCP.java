// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.network;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.login.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;

public class NetHandlerHandshakeTCP implements INetHandlerHandshakeServer
{
    private final MinecraftServer server;
    private final NetworkManager networkManager;
    
    public NetHandlerHandshakeTCP(final MinecraftServer serverIn, final NetworkManager netManager) {
        this.server = serverIn;
        this.networkManager = netManager;
    }
    
    @Override
    public void processHandshake(final C00Handshake packetIn) {
        switch (packetIn.getRequestedState()) {
            case LOGIN: {
                this.networkManager.setConnectionState(EnumConnectionState.LOGIN);
                if (packetIn.getProtocolVersion() > 340) {
                    final ITextComponent itextcomponent = new TextComponentTranslation("multiplayer.disconnect.outdated_server", new Object[] { "1.12.2" });
                    this.networkManager.sendPacket(new SPacketDisconnect(itextcomponent));
                    this.networkManager.closeChannel(itextcomponent);
                    break;
                }
                if (packetIn.getProtocolVersion() < 340) {
                    final ITextComponent itextcomponent2 = new TextComponentTranslation("multiplayer.disconnect.outdated_client", new Object[] { "1.12.2" });
                    this.networkManager.sendPacket(new SPacketDisconnect(itextcomponent2));
                    this.networkManager.closeChannel(itextcomponent2);
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
                throw new UnsupportedOperationException("Invalid intention " + packetIn.getRequestedState());
            }
        }
    }
    
    @Override
    public void onDisconnect(final ITextComponent reason) {
    }
}
