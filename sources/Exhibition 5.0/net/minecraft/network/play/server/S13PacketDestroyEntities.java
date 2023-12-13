// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.Packet;

public class S13PacketDestroyEntities implements Packet
{
    private int[] field_149100_a;
    private static final String __OBFID = "CL_00001320";
    
    public S13PacketDestroyEntities() {
    }
    
    public S13PacketDestroyEntities(final int... p_i45211_1_) {
        this.field_149100_a = p_i45211_1_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_149100_a = new int[data.readVarIntFromBuffer()];
        for (int var2 = 0; var2 < this.field_149100_a.length; ++var2) {
            this.field_149100_a[var2] = data.readVarIntFromBuffer();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeVarIntToBuffer(this.field_149100_a.length);
        for (int var2 = 0; var2 < this.field_149100_a.length; ++var2) {
            data.writeVarIntToBuffer(this.field_149100_a[var2]);
        }
    }
    
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleDestroyEntities(this);
    }
    
    public int[] func_149098_c() {
        return this.field_149100_a;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
