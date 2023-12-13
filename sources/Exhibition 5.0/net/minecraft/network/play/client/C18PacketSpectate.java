// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import net.minecraft.network.play.INetHandlerPlayServer;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import java.util.UUID;
import net.minecraft.network.Packet;

public class C18PacketSpectate implements Packet
{
    private UUID field_179729_a;
    private static final String __OBFID = "CL_00002280";
    
    public C18PacketSpectate() {
    }
    
    public C18PacketSpectate(final UUID p_i45932_1_) {
        this.field_179729_a = p_i45932_1_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_179729_a = data.readUuid();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeUuid(this.field_179729_a);
    }
    
    public void func_179728_a(final INetHandlerPlayServer p_179728_1_) {
        p_179728_1_.func_175088_a(this);
    }
    
    public Entity func_179727_a(final WorldServer p_179727_1_) {
        return p_179727_1_.getEntityFromUuid(this.field_179729_a);
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_179728_a((INetHandlerPlayServer)handler);
    }
}
