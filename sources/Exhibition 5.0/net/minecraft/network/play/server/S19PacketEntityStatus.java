// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.world.World;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;

public class S19PacketEntityStatus implements Packet
{
    private int field_149164_a;
    private byte field_149163_b;
    private static final String __OBFID = "CL_00001299";
    
    public S19PacketEntityStatus() {
    }
    
    public S19PacketEntityStatus(final Entity p_i46335_1_, final byte p_i46335_2_) {
        this.field_149164_a = p_i46335_1_.getEntityId();
        this.field_149163_b = p_i46335_2_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149164_a = data.readInt();
        this.field_149163_b = data.readByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeInt(this.field_149164_a);
        data.writeByte(this.field_149163_b);
    }
    
    public void func_180736_a(final INetHandlerPlayClient p_180736_1_) {
        p_180736_1_.handleEntityStatus(this);
    }
    
    public Entity func_149161_a(final World worldIn) {
        return worldIn.getEntityByID(this.field_149164_a);
    }
    
    public byte func_149160_c() {
        return this.field_149163_b;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180736_a((INetHandlerPlayClient)handler);
    }
}
