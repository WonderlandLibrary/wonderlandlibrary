// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;

public class S24PacketBlockAction implements Packet
{
    private BlockPos field_179826_a;
    private int field_148872_d;
    private int field_148873_e;
    private Block field_148871_f;
    private static final String __OBFID = "CL_00001286";
    
    public S24PacketBlockAction() {
    }
    
    public S24PacketBlockAction(final BlockPos p_i45989_1_, final Block p_i45989_2_, final int p_i45989_3_, final int p_i45989_4_) {
        this.field_179826_a = p_i45989_1_;
        this.field_148872_d = p_i45989_3_;
        this.field_148873_e = p_i45989_4_;
        this.field_148871_f = p_i45989_2_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_179826_a = data.readBlockPos();
        this.field_148872_d = data.readUnsignedByte();
        this.field_148873_e = data.readUnsignedByte();
        this.field_148871_f = Block.getBlockById(data.readVarIntFromBuffer() & 0xFFF);
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeBlockPos(this.field_179826_a);
        data.writeByte(this.field_148872_d);
        data.writeByte(this.field_148873_e);
        data.writeVarIntToBuffer(Block.getIdFromBlock(this.field_148871_f) & 0xFFF);
    }
    
    public void func_180726_a(final INetHandlerPlayClient p_180726_1_) {
        p_180726_1_.handleBlockAction(this);
    }
    
    public BlockPos func_179825_a() {
        return this.field_179826_a;
    }
    
    public int getData1() {
        return this.field_148872_d;
    }
    
    public int getData2() {
        return this.field_148873_e;
    }
    
    public Block getBlockType() {
        return this.field_148871_f;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180726_a((INetHandlerPlayClient)handler);
    }
}
