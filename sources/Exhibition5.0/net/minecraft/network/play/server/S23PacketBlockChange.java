// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayClient;
import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;

public class S23PacketBlockChange implements Packet
{
    private BlockPos field_179828_a;
    private IBlockState field_148883_d;
    private static final String __OBFID = "CL_00001287";
    
    public S23PacketBlockChange() {
    }
    
    public S23PacketBlockChange(final World worldIn, final BlockPos p_i45988_2_) {
        this.field_179828_a = p_i45988_2_;
        this.field_148883_d = worldIn.getBlockState(p_i45988_2_);
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        this.field_179828_a = data.readBlockPos();
        this.field_148883_d = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(data.readVarIntFromBuffer());
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        data.writeBlockPos(this.field_179828_a);
        data.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(this.field_148883_d));
    }
    
    public void func_180727_a(final INetHandlerPlayClient p_180727_1_) {
        p_180727_1_.handleBlockChange(this);
    }
    
    public IBlockState func_180728_a() {
        return this.field_148883_d;
    }
    
    public BlockPos func_179827_b() {
        return this.field_179828_a;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180727_a((INetHandlerPlayClient)handler);
    }
}
