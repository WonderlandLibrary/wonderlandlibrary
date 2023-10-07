// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class C0DPacketCloseWindow implements Packet
{
    private int windowId;
    private static final String __OBFID = "CL_00001354";
    
    public C0DPacketCloseWindow() {
    }
    
    public C0DPacketCloseWindow(final int p_i45247_1_) {
        this.windowId = p_i45247_1_;
    }
    
    public void func_180759_a(final INetHandlerPlayServer p_180759_1_) {
        p_180759_1_.processCloseWindow(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.windowId = data.readByte();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeByte(this.windowId);
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180759_a((INetHandlerPlayServer)handler);
    }
}
