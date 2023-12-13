// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.login.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.login.INetHandlerLoginServer;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.PacketBuffer;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.Packet;

public class C00PacketLoginStart implements Packet
{
    private GameProfile profile;
    private static final String __OBFID = "CL_00001379";
    
    public C00PacketLoginStart() {
    }
    
    public C00PacketLoginStart(final GameProfile profileIn) {
        this.profile = profileIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.profile = new GameProfile((UUID)null, data.readStringFromBuffer(16));
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeString(this.profile.getName());
    }
    
    public void func_180773_a(final INetHandlerLoginServer p_180773_1_) {
        p_180773_1_.processLoginStart(this);
    }
    
    public GameProfile getProfile() {
        return this.profile;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180773_a((INetHandlerLoginServer)handler);
    }
}
