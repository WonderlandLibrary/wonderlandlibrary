// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;

public class S04PacketEntityEquipment implements Packet
{
    private int field_149394_a;
    private int field_149392_b;
    private ItemStack field_149393_c;
    private static final String __OBFID = "CL_00001330";
    
    public S04PacketEntityEquipment() {
    }
    
    public S04PacketEntityEquipment(final int p_i45221_1_, final int p_i45221_2_, final ItemStack p_i45221_3_) {
        this.field_149394_a = p_i45221_1_;
        this.field_149392_b = p_i45221_2_;
        this.field_149393_c = ((p_i45221_3_ == null) ? null : p_i45221_3_.copy());
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149394_a = data.readVarIntFromBuffer();
        this.field_149392_b = data.readShort();
        this.field_149393_c = data.readItemStackFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149394_a);
        data.writeShort(this.field_149392_b);
        data.writeItemStackToBuffer(this.field_149393_c);
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityEquipment(this);
    }
    
    public ItemStack func_149390_c() {
        return this.field_149393_c;
    }
    
    public int func_149389_d() {
        return this.field_149394_a;
    }
    
    public int func_149388_e() {
        return this.field_149392_b;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
