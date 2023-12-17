// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketSteerBoat implements Packet<INetHandlerPlayServer>
{
    private boolean left;
    private boolean right;
    
    public CPacketSteerBoat() {
    }
    
    public CPacketSteerBoat(final boolean p_i46873_1_, final boolean p_i46873_2_) {
        this.left = p_i46873_1_;
        this.right = p_i46873_2_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.left = buf.readBoolean();
        this.right = buf.readBoolean();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBoolean(this.left);
        buf.writeBoolean(this.right);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processSteerBoat(this);
    }
    
    public boolean getLeft() {
        return this.left;
    }
    
    public boolean getRight() {
        return this.right;
    }
}
