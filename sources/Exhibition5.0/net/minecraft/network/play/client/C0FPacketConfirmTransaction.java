// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C0FPacketConfirmTransaction implements Packet
{
    private int id;
    private short uid;
    private boolean accepted;
    private static final String __OBFID = "CL_00001351";
    
    public C0FPacketConfirmTransaction() {
    }
    
    public C0FPacketConfirmTransaction(final int p_i45244_1_, final short p_i45244_2_, final boolean p_i45244_3_) {
        this.id = p_i45244_1_;
        this.uid = p_i45244_2_;
        this.accepted = p_i45244_3_;
    }
    
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processConfirmTransaction(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.id = data.readByte();
        this.uid = data.readShort();
        this.accepted = (data.readByte() != 0);
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeByte(this.id);
        data.writeShort(this.uid);
        data.writeByte(this.accepted ? 1 : 0);
    }
    
    public int getId() {
        return this.id;
    }
    
    public short getUid() {
        return this.uid;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}
