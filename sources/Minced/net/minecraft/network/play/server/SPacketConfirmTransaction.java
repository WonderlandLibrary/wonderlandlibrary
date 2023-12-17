// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketConfirmTransaction implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    private short actionNumber;
    private boolean accepted;
    
    public SPacketConfirmTransaction() {
    }
    
    public SPacketConfirmTransaction(final int windowIdIn, final short actionNumberIn, final boolean acceptedIn) {
        this.windowId = windowIdIn;
        this.actionNumber = actionNumberIn;
        this.accepted = acceptedIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleConfirmTransaction(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readUnsignedByte();
        this.actionNumber = buf.readShort();
        this.accepted = buf.readBoolean();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeShort(this.actionNumber);
        buf.writeBoolean(this.accepted);
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public short getActionNumber() {
        return this.actionNumber;
    }
    
    public boolean wasAccepted() {
        return this.accepted;
    }
}
