// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.login.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.PacketBuffer;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.Packet;

public class SPacketLoginSuccess implements Packet<INetHandlerLoginClient>
{
    private GameProfile profile;
    
    public SPacketLoginSuccess() {
    }
    
    public SPacketLoginSuccess(final GameProfile profileIn) {
        this.profile = profileIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        final String s = buf.readString(36);
        final String s2 = buf.readString(16);
        final UUID uuid = UUID.fromString(s);
        this.profile = new GameProfile(uuid, s2);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        final UUID uuid = this.profile.getId();
        buf.writeString((uuid == null) ? "" : uuid.toString());
        buf.writeString(this.profile.getName());
    }
    
    @Override
    public void processPacket(final INetHandlerLoginClient handler) {
        handler.handleLoginSuccess(this);
    }
    
    public GameProfile getProfile() {
        return this.profile;
    }
}
