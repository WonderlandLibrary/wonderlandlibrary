// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.websocket.implementation.packet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import moonsense.network.websocket.packets.Packet;

public class UpdateUserPacket implements Packet
{
    private String username;
    private boolean online;
    
    public UpdateUserPacket(final String username, final boolean online) {
        this.username = username;
        this.online = online;
    }
    
    public UpdateUserPacket(final DataInputStream in) throws IOException {
        this.username = in.readUTF();
        this.online = in.readBoolean();
    }
    
    @Override
    public void write(final DataOutputStream out) throws IOException {
        out.writeUTF(this.username);
        out.writeBoolean(this.online);
        out.writeBoolean(false);
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
    
    public boolean isOnline() {
        return this.online;
    }
    
    public void setOnline(final boolean online) {
        this.online = online;
    }
}
