// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;

public class S35PacketUpdateTileEntity implements Packet
{
    private BlockPos field_179824_a;
    private int metadata;
    private NBTTagCompound nbt;
    private static final String __OBFID = "CL_00001285";
    
    public S35PacketUpdateTileEntity() {
    }
    
    public S35PacketUpdateTileEntity(final BlockPos p_i45990_1_, final int p_i45990_2_, final NBTTagCompound p_i45990_3_) {
        this.field_179824_a = p_i45990_1_;
        this.metadata = p_i45990_2_;
        this.nbt = p_i45990_3_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_179824_a = data.readBlockPos();
        this.metadata = data.readUnsignedByte();
        this.nbt = data.readNBTTagCompoundFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeBlockPos(this.field_179824_a);
        data.writeByte((byte)this.metadata);
        data.writeNBTTagCompoundToBuffer(this.nbt);
    }
    
    public void func_180725_a(final INetHandlerPlayClient p_180725_1_) {
        p_180725_1_.handleUpdateTileEntity(this);
    }
    
    public BlockPos func_179823_a() {
        return this.field_179824_a;
    }
    
    public int getTileEntityType() {
        return this.metadata;
    }
    
    public NBTTagCompound getNbtCompound() {
        return this.nbt;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180725_a((INetHandlerPlayClient)handler);
    }
}
