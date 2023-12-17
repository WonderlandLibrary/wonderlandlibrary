// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Potion;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketRemoveEntityEffect implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private Potion effectId;
    
    public SPacketRemoveEntityEffect() {
    }
    
    public SPacketRemoveEntityEffect(final int entityIdIn, final Potion potionIn) {
        this.entityId = entityIdIn;
        this.effectId = potionIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarInt();
        this.effectId = Potion.getPotionById(buf.readUnsignedByte());
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.entityId);
        buf.writeByte(Potion.getIdFromPotion(this.effectId));
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleRemoveEntityEffect(this);
    }
    
    @Nullable
    public Entity getEntity(final World worldIn) {
        return worldIn.getEntityByID(this.entityId);
    }
    
    @Nullable
    public Potion getPotion() {
        return this.effectId;
    }
}
