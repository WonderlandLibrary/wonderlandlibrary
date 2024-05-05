package net.minecraft.server.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerHandshakeTCP;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.server.network.NetHandlerStatusServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class NetHandlerHandshakeTCP
implements INetHandlerHandshakeServer {
    private final MinecraftServer server;
    private final NetworkManager networkManager;

    public NetHandlerHandshakeTCP(MinecraftServer serverIn, NetworkManager netManager) {
        this.server = serverIn;
        this.networkManager = netManager;
    }

    public void processHandshake(C00Handshake packetIn) {
        switch (1.$SwitchMap$net$minecraft$network$EnumConnectionState[packetIn.getRequestedState().ordinal()]) {
            case 1: {
                this.networkManager.setConnectionState(EnumConnectionState.LOGIN);
                if (packetIn.getProtocolVersion() > 47) {
                    ChatComponentText chatcomponenttext = new ChatComponentText("Outdated server! I'm still on 1.8.9");
                    this.networkManager.sendPacket((Packet)new S00PacketDisconnect((IChatComponent)chatcomponenttext));
                    this.networkManager.closeChannel((IChatComponent)chatcomponenttext);
                    break;
                }
                if (packetIn.getProtocolVersion() < 47) {
                    ChatComponentText chatcomponenttext1 = new ChatComponentText("Outdated client! Please use 1.8.9");
                    this.networkManager.sendPacket((Packet)new S00PacketDisconnect((IChatComponent)chatcomponenttext1));
                    this.networkManager.closeChannel((IChatComponent)chatcomponenttext1);
                    break;
                }
                this.networkManager.setNetHandler((INetHandler)new NetHandlerLoginServer(this.server, this.networkManager));
                break;
            }
            case 2: {
                this.networkManager.setConnectionState(EnumConnectionState.STATUS);
                this.networkManager.setNetHandler((INetHandler)new NetHandlerStatusServer(this.server, this.networkManager));
                break;
            }
            default: {
                throw new UnsupportedOperationException("Invalid intention " + packetIn.getRequestedState());
            }
        }
    }

    public void onDisconnect(IChatComponent reason) {
    }
}
