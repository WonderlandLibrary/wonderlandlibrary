// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketCollectItem implements Packet<INetHandlerPlayClient>
{
    private int collectedItemEntityId;
    private int entityId;
    private int collectedQuantity;
    
    public SPacketCollectItem() {
    }
    
    public SPacketCollectItem(final int p_i47316_1_, final int p_i47316_2_, final int p_i47316_3_) {
        this.collectedItemEntityId = p_i47316_1_;
        this.entityId = p_i47316_2_;
        this.collectedQuantity = p_i47316_3_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.collectedItemEntityId = buf.readVarInt();
        this.entityId = buf.readVarInt();
        this.collectedQuantity = buf.readVarInt();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.collectedItemEntityId);
        buf.writeVarInt(this.entityId);
        buf.writeVarInt(this.collectedQuantity);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleCollectItem(this);
    }
    
    public int getCollectedItemEntityID() {
        return this.collectedItemEntityId;
    }
    
    public int getEntityID() {
        return this.entityId;
    }
    
    public int getAmount() {
        return this.collectedQuantity;
    }
}
