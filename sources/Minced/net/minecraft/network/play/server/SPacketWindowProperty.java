// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketWindowProperty implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    private int property;
    private int value;
    
    public SPacketWindowProperty() {
    }
    
    public SPacketWindowProperty(final int windowIdIn, final int propertyIn, final int valueIn) {
        this.windowId = windowIdIn;
        this.property = propertyIn;
        this.value = valueIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleWindowProperty(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readUnsignedByte();
        this.property = buf.readShort();
        this.value = buf.readShort();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeShort(this.property);
        buf.writeShort(this.value);
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public int getProperty() {
        return this.property;
    }
    
    public int getValue() {
        return this.value;
    }
}
