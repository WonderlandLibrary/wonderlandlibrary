// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketPlayerTryUseItemOnBlock implements Packet<INetHandlerPlayServer>
{
    private BlockPos position;
    private EnumFacing placedBlockDirection;
    private EnumHand hand;
    private float facingX;
    private float facingY;
    private float facingZ;
    
    public CPacketPlayerTryUseItemOnBlock() {
    }
    
    public CPacketPlayerTryUseItemOnBlock(final BlockPos posIn, final EnumFacing placedBlockDirectionIn, final EnumHand handIn, final float facingXIn, final float facingYIn, final float facingZIn) {
        this.position = posIn;
        this.placedBlockDirection = placedBlockDirectionIn;
        this.hand = handIn;
        this.facingX = facingXIn;
        this.facingY = facingYIn;
        this.facingZ = facingZIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.position = buf.readBlockPos();
        this.placedBlockDirection = buf.readEnumValue(EnumFacing.class);
        this.hand = buf.readEnumValue(EnumHand.class);
        this.facingX = buf.readFloat();
        this.facingY = buf.readFloat();
        this.facingZ = buf.readFloat();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBlockPos(this.position);
        buf.writeEnumValue(this.placedBlockDirection);
        buf.writeEnumValue(this.hand);
        buf.writeFloat(this.facingX);
        buf.writeFloat(this.facingY);
        buf.writeFloat(this.facingZ);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processTryUseItemOnBlock(this);
    }
    
    public BlockPos getPos() {
        return this.position;
    }
    
    public EnumFacing getDirection() {
        return this.placedBlockDirection;
    }
    
    public EnumHand getHand() {
        return this.hand;
    }
    
    public float getFacingX() {
        return this.facingX;
    }
    
    public float getFacingY() {
        return this.facingY;
    }
    
    public float getFacingZ() {
        return this.facingZ;
    }
}
