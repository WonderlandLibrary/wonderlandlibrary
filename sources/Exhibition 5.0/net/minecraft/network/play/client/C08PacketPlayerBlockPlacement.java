// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayServer;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;

public class C08PacketPlayerBlockPlacement implements Packet
{
    private static final BlockPos field_179726_a;
    private BlockPos blockPos;
    private int placedBlockDirection;
    private ItemStack stack;
    private float facingX;
    private float facingY;
    private float facingZ;
    private static final String __OBFID = "CL_00001371";
    
    public C08PacketPlayerBlockPlacement() {
    }
    
    public C08PacketPlayerBlockPlacement(final ItemStack p_i45930_1_) {
        this(C08PacketPlayerBlockPlacement.field_179726_a, 255, p_i45930_1_, 0.0f, 0.0f, 0.0f);
    }
    
    public C08PacketPlayerBlockPlacement(final BlockPos pos, final int direction, final ItemStack stack, final float x, final float y, final float z) {
        this.blockPos = pos;
        this.placedBlockDirection = direction;
        this.stack = ((stack != null) ? stack.copy() : null);
        this.facingX = x;
        this.facingY = y;
        this.facingZ = z;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.blockPos = data.readBlockPos();
        this.placedBlockDirection = data.readUnsignedByte();
        this.stack = data.readItemStackFromBuffer();
        this.facingX = data.readUnsignedByte() / 16.0f;
        this.facingY = data.readUnsignedByte() / 16.0f;
        this.facingZ = data.readUnsignedByte() / 16.0f;
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeBlockPos(this.blockPos);
        data.writeByte(this.placedBlockDirection);
        data.writeItemStackToBuffer(this.stack);
        data.writeByte((int)(this.facingX * 16.0f));
        data.writeByte((int)(this.facingY * 16.0f));
        data.writeByte((int)(this.facingZ * 16.0f));
    }
    
    public void processPlacement(final INetHandlerPlayServer handler) {
        handler.processPlayerBlockPlacement(this);
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    public void setBlockPos(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }
    
    public int getPlacedBlockDirection() {
        return this.placedBlockDirection;
    }
    
    public ItemStack getStack() {
        return this.stack;
    }
    
    public float getPlacedBlockOffsetX() {
        return this.facingX;
    }
    
    public float getPlacedBlockOffsetY() {
        return this.facingY;
    }
    
    public float getPlacedBlockOffsetZ() {
        return this.facingZ;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.processPlacement((INetHandlerPlayServer)handler);
    }
    
    static {
        field_179726_a = new BlockPos(-1, -1, -1);
    }
}
