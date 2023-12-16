// 
// Decompiled by Procyon v0.5.36
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
    private BlockPos field_179725_b;
    private int placedBlockDirection;
    private ItemStack stack;
    private float facingX;
    private float facingY;
    private float facingZ;
    private static final String __OBFID = "CL_00001371";
    
    static {
        field_179726_a = new BlockPos(-1, -1, -1);
    }
    
    public C08PacketPlayerBlockPlacement() {
    }
    
    public C08PacketPlayerBlockPlacement(final ItemStack p_i45930_1_) {
        this(C08PacketPlayerBlockPlacement.field_179726_a, 255, p_i45930_1_, 0.0f, 0.0f, 0.0f);
    }
    
    public C08PacketPlayerBlockPlacement(final BlockPos p_i45931_1_, final int p_i45931_2_, final ItemStack p_i45931_3_, final float p_i45931_4_, final float p_i45931_5_, final float p_i45931_6_) {
        this.field_179725_b = p_i45931_1_;
        this.placedBlockDirection = p_i45931_2_;
        this.stack = ((p_i45931_3_ != null) ? p_i45931_3_.copy() : null);
        this.facingX = p_i45931_4_;
        this.facingY = p_i45931_5_;
        this.facingZ = p_i45931_6_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_179725_b = data.readBlockPos();
        this.placedBlockDirection = data.readUnsignedByte();
        this.stack = data.readItemStackFromBuffer();
        this.facingX = data.readUnsignedByte() / 16.0f;
        this.facingY = data.readUnsignedByte() / 16.0f;
        this.facingZ = data.readUnsignedByte() / 16.0f;
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeBlockPos(this.field_179725_b);
        data.writeByte(this.placedBlockDirection);
        data.writeItemStackToBuffer(this.stack);
        data.writeByte((int)(this.facingX * 16.0f));
        data.writeByte((int)(this.facingY * 16.0f));
        data.writeByte((int)(this.facingZ * 16.0f));
    }
    
    public void func_180769_a(final INetHandlerPlayServer p_180769_1_) {
        p_180769_1_.processPlayerBlockPlacement(this);
    }
    
    public BlockPos func_179724_a() {
        return this.field_179725_b;
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
        this.func_180769_a((INetHandlerPlayServer)handler);
    }
}
