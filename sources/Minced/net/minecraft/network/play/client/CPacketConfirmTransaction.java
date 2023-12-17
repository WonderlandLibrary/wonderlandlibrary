// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketConfirmTransaction implements Packet<INetHandlerPlayServer>
{
    private int windowId;
    private short uid;
    private boolean accepted;
    
    public CPacketConfirmTransaction() {
    }
    
    public CPacketConfirmTransaction(final int windowIdIn, final short uidIn, final boolean acceptedIn) {
        this.windowId = windowIdIn;
        this.uid = uidIn;
        this.accepted = acceptedIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processConfirmTransaction(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readByte();
        this.uid = buf.readShort();
        this.accepted = (buf.readByte() != 0);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeShort(this.uid);
        buf.writeByte(this.accepted ? 1 : 0);
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public short getUid() {
        return this.uid;
    }
}
