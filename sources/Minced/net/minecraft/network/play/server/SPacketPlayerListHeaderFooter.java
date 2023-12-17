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

public class SPacketPlayerListHeaderFooter implements Packet<INetHandlerPlayClient>
{
    private ITextComponent header;
    private ITextComponent footer;
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.header = buf.readTextComponent();
        this.footer = buf.readTextComponent();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeTextComponent(this.header);
        buf.writeTextComponent(this.footer);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handlePlayerListHeaderFooter(this);
    }
    
    public ITextComponent getHeader() {
        return this.header;
    }
    
    public ITextComponent getFooter() {
        return this.footer;
    }
}
