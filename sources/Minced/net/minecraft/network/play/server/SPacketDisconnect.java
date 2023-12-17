// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketDisconnect implements Packet<INetHandlerPlayClient>
{
    private ITextComponent reason;
    
    public SPacketDisconnect() {
    }
    
    public SPacketDisconnect(final ITextComponent messageIn) {
        this.reason = messageIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.reason = buf.readTextComponent();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeTextComponent(this.reason);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleDisconnect(this);
    }
    
    public ITextComponent getReason() {
        return this.reason;
    }
}
