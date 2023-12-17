// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketDestroyEntities implements Packet<INetHandlerPlayClient>
{
    private int[] entityIDs;
    
    public SPacketDestroyEntities() {
    }
    
    public SPacketDestroyEntities(final int... entityIdsIn) {
        this.entityIDs = entityIdsIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityIDs = new int[buf.readVarInt()];
        for (int i = 0; i < this.entityIDs.length; ++i) {
            this.entityIDs[i] = buf.readVarInt();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.entityIDs.length);
        for (final int i : this.entityIDs) {
            buf.writeVarInt(i);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleDestroyEntities(this);
    }
    
    public int[] getEntityIDs() {
        return this.entityIDs;
    }
}
