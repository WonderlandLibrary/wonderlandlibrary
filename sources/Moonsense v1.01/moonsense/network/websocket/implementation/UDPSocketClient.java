// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.websocket.implementation;

import moonsense.network.websocket.implementation.packet.PingServerPacket;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.SocketException;
import moonsense.network.websocket.packets.PacketProtocol;
import net.minecraft.client.Minecraft;
import moonsense.network.websocket.implementation.packet.UpdateUserPacket;
import moonsense.network.websocket.implementation.packet.IsUserOnlinePacket;
import java.net.InetAddress;
import moonsense.network.websocket.packets.Packet;
import moonsense.network.websocket.listener.UDPNetworkingListener;
import moonsense.network.websocket.implementation.protocol.ClientProtocol;
import java.util.HashMap;
import moonsense.network.websocket.UDPClient;

public class UDPSocketClient
{
    private UDPClient client;
    private HashMap<String, Boolean> users;
    private boolean running;
    
    public UDPSocketClient() throws SocketException, UnknownHostException {
        this.users = new HashMap<String, Boolean>();
        this.client = new UDPClient("127.0.0.1", 4425, new ClientProtocol(), new UDPNetworkingListener[] { new UDPNetworkingListener() {
                @Override
                public void onPacketReceived(final Packet packet, final InetAddress address, final int port) {
                    if (packet instanceof IsUserOnlinePacket) {
                        UDPSocketClient.this.updateUser(((IsUserOnlinePacket)packet).getUsername(), ((IsUserOnlinePacket)packet).isOnline());
                    }
                    if (packet instanceof UpdateUserPacket) {
                        ((UpdateUserPacket)packet).getUsername().equals(Minecraft.getMinecraft().getSession().getUsername());
                        UDPSocketClient.this.updateUser(((UpdateUserPacket)packet).getUsername(), ((UpdateUserPacket)packet).isOnline());
                    }
                }
            } });
    }
    
    public void send(final Packet packet) throws IOException {
        this.client.sendData(packet);
    }
    
    public void pingServer() {
        try {
            this.send(new PingServerPacket());
            this.running = true;
        }
        catch (IOException e) {
            this.running = false;
        }
    }
    
    public void updateSelf(final boolean online) {
        if (online) {
            this.users.put(Minecraft.getMinecraft().getSession().getUsername(), online);
            try {
                this.send(new UpdateUserPacket(Minecraft.getMinecraft().getSession().getUsername(), online));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            this.users.put(Minecraft.getMinecraft().getSession().getUsername(), online);
            try {
                this.send(new UpdateUserPacket(Minecraft.getMinecraft().getSession().getUsername(), online));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void updateUserWithPacket(final String username, final boolean online) {
        if (online) {
            this.users.put(username, online);
            try {
                this.send(new UpdateUserPacket(username, online));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            this.users.put(username, online);
            try {
                this.send(new UpdateUserPacket(username, online));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void updateUser(final String username, final boolean online) {
        this.users.put(username, online);
    }
    
    public boolean isUserOnline(final String username) {
        if (this.users.containsKey(username)) {
            if (this.users.get(username)) {
                return this.users.get(username);
            }
            try {
                this.send(new IsUserOnlinePacket(username));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                this.send(new IsUserOnlinePacket(username));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public boolean isRunning() {
        return this.running;
    }
}
