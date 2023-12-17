// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import java.util.UUID;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketSpawnPainting implements Packet<INetHandlerPlayClient>
{
    private int entityID;
    private UUID uniqueId;
    private BlockPos position;
    private EnumFacing facing;
    private String title;
    
    public SPacketSpawnPainting() {
    }
    
    public SPacketSpawnPainting(final EntityPainting painting) {
        this.entityID = painting.getEntityId();
        this.uniqueId = painting.getUniqueID();
        this.position = painting.getHangingPosition();
        this.facing = painting.facingDirection;
        this.title = painting.art.title;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityID = buf.readVarInt();
        this.uniqueId = buf.readUniqueId();
        this.title = buf.readString(EntityPainting.EnumArt.MAX_NAME_LENGTH);
        this.position = buf.readBlockPos();
        this.facing = EnumFacing.byHorizontalIndex(buf.readUnsignedByte());
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.entityID);
        buf.writeUniqueId(this.uniqueId);
        buf.writeString(this.title);
        buf.writeBlockPos(this.position);
        buf.writeByte(this.facing.getHorizontalIndex());
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSpawnPainting(this);
    }
    
    public int getEntityID() {
        return this.entityID;
    }
    
    public UUID getUniqueId() {
        return this.uniqueId;
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public EnumFacing getFacing() {
        return this.facing;
    }
    
    public String getTitle() {
        return this.title;
    }
}
