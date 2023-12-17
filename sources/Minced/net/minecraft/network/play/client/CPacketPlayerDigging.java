// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketPlayerDigging implements Packet<INetHandlerPlayServer>
{
    private BlockPos position;
    private EnumFacing facing;
    private Action action;
    
    public CPacketPlayerDigging() {
    }
    
    public CPacketPlayerDigging(final Action actionIn, final BlockPos posIn, final EnumFacing facingIn) {
        this.action = actionIn;
        this.position = posIn;
        this.facing = facingIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.action = buf.readEnumValue(Action.class);
        this.position = buf.readBlockPos();
        this.facing = EnumFacing.byIndex(buf.readUnsignedByte());
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.action);
        buf.writeBlockPos(this.position);
        buf.writeByte(this.facing.getIndex());
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processPlayerDigging(this);
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public EnumFacing getFacing() {
        return this.facing;
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public enum Action
    {
        START_DESTROY_BLOCK, 
        ABORT_DESTROY_BLOCK, 
        STOP_DESTROY_BLOCK, 
        DROP_ALL_ITEMS, 
        DROP_ITEM, 
        RELEASE_USE_ITEM, 
        SWAP_HELD_ITEMS;
    }
}
