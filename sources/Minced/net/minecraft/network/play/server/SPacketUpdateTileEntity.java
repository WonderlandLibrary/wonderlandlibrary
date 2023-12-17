// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketUpdateTileEntity implements Packet<INetHandlerPlayClient>
{
    private BlockPos blockPos;
    private int tileEntityType;
    private NBTTagCompound nbt;
    
    public SPacketUpdateTileEntity() {
    }
    
    public SPacketUpdateTileEntity(final BlockPos blockPosIn, final int tileEntityTypeIn, final NBTTagCompound compoundIn) {
        this.blockPos = blockPosIn;
        this.tileEntityType = tileEntityTypeIn;
        this.nbt = compoundIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.blockPos = buf.readBlockPos();
        this.tileEntityType = buf.readUnsignedByte();
        this.nbt = buf.readCompoundTag();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBlockPos(this.blockPos);
        buf.writeByte((byte)this.tileEntityType);
        buf.writeCompoundTag(this.nbt);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleUpdateTileEntity(this);
    }
    
    public BlockPos getPos() {
        return this.blockPos;
    }
    
    public int getTileEntityType() {
        return this.tileEntityType;
    }
    
    public NBTTagCompound getNbtCompound() {
        return this.nbt;
    }
}
