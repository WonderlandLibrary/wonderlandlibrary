// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class S2EPacketCloseWindow implements Packet
{
    private int field_148896_a;
    private static final String __OBFID = "CL_00001292";
    
    public S2EPacketCloseWindow() {
    }
    
    public S2EPacketCloseWindow(final int p_i45183_1_) {
        this.field_148896_a = p_i45183_1_;
    }
    
    public void func_180731_a(final INetHandlerPlayClient p_180731_1_) {
        p_180731_1_.handleCloseWindow(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_148896_a = data.readUnsignedByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeByte(this.field_148896_a);
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180731_a((INetHandlerPlayClient)handler);
    }
}
