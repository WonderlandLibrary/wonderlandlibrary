// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketPlayerTryUseItem implements Packet<INetHandlerPlayServer>
{
    private EnumHand hand;
    
    public CPacketPlayerTryUseItem() {
    }
    
    public CPacketPlayerTryUseItem(final EnumHand handIn) {
        this.hand = handIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.hand = buf.readEnumValue(EnumHand.class);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.hand);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processTryUseItem(this);
    }
    
    public EnumHand getHand() {
        return this.hand;
    }
}
