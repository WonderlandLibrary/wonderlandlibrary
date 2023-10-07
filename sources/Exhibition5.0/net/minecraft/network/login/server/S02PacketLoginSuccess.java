// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.login.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.login.INetHandlerLoginClient;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.PacketBuffer;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.Packet;

public class S02PacketLoginSuccess implements Packet
{
    private GameProfile profile;
    private static final String __OBFID = "CL_00001375";
    
    public S02PacketLoginSuccess() {
    }
    
    public S02PacketLoginSuccess(final GameProfile profileIn) {
        this.profile = profileIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        final String var2 = data.readStringFromBuffer(36);
        final String var3 = data.readStringFromBuffer(16);
        final UUID var4 = UUID.fromString(var2);
        this.profile = new GameProfile(var4, var3);
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        final UUID var2 = this.profile.getId();
        data.writeString((var2 == null) ? "" : var2.toString());
        data.writeString(this.profile.getName());
    }
    
    public void func_180771_a(final INetHandlerLoginClient p_180771_1_) {
        p_180771_1_.handleLoginSuccess(this);
    }
    
    public GameProfile func_179730_a() {
        return this.profile;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180771_a((INetHandlerLoginClient)handler);
    }
}
