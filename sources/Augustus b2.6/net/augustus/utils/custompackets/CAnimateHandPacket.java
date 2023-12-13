// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils.custompackets;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CAnimateHandPacket implements Packet<INetHandlerPlayServer>
{
    private Hand hand;
    
    public CAnimateHandPacket() {
    }
    
    public CAnimateHandPacket(final Hand handIn) {
        this.hand = handIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.hand = buf.readEnumValue(Hand.class);
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.hand);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.handleHandAnimation(this);
    }
    
    public Hand getHand() {
        return this.hand;
    }
}
