// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;

public class S2FPacketSetSlot implements Packet
{
    private int field_149179_a;
    private int field_149177_b;
    private ItemStack field_149178_c;
    private static final String __OBFID = "CL_00001296";
    
    public S2FPacketSetSlot() {
    }
    
    public S2FPacketSetSlot(final int p_i45188_1_, final int p_i45188_2_, final ItemStack p_i45188_3_) {
        this.field_149179_a = p_i45188_1_;
        this.field_149177_b = p_i45188_2_;
        this.field_149178_c = ((p_i45188_3_ == null) ? null : p_i45188_3_.copy());
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSetSlot(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149179_a = data.readByte();
        this.field_149177_b = data.readShort();
        this.field_149178_c = data.readItemStackFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeByte(this.field_149179_a);
        data.writeShort(this.field_149177_b);
        data.writeItemStackToBuffer(this.field_149178_c);
    }
    
    public int func_149175_c() {
        return this.field_149179_a;
    }
    
    public int func_149173_d() {
        return this.field_149177_b;
    }
    
    public ItemStack func_149174_e() {
        return this.field_149178_c;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
