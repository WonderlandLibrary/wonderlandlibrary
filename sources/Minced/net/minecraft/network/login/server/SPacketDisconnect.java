// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.login.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.Packet;

public class SPacketDisconnect implements Packet<INetHandlerLoginClient>
{
    private ITextComponent reason;
    
    public SPacketDisconnect() {
    }
    
    public SPacketDisconnect(final ITextComponent p_i46853_1_) {
        this.reason = p_i46853_1_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.reason = ITextComponent.Serializer.fromJsonLenient(buf.readString(32767));
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeTextComponent(this.reason);
    }
    
    @Override
    public void processPacket(final INetHandlerLoginClient handler) {
        handler.handleDisconnect(this);
    }
    
    public ITextComponent getReason() {
        return this.reason;
    }
}
