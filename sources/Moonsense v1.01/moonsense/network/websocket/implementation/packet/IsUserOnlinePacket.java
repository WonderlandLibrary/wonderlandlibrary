// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.websocket.implementation.packet;

import java.io.DataOutputStream;
import java.io.IOException;
import moonsense.MoonsenseClient;
import java.io.DataInputStream;
import moonsense.network.websocket.packets.Packet;

public class IsUserOnlinePacket implements Packet
{
    private String username;
    private boolean isOnline;
    
    public IsUserOnlinePacket(final String username) {
        this.username = username;
    }
    
    public IsUserOnlinePacket(final DataInputStream in) throws IOException {
        this.username = in.readUTF();
        this.isOnline = in.readBoolean();
        MoonsenseClient.INSTANCE.getSocketClient().updateUser(this.username, this.isOnline);
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeUTF(this.username);
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public boolean isOnline() {
        return this.isOnline;
    }
}
