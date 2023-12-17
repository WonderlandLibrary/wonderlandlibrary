// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketBlockChange implements Packet<INetHandlerPlayClient>
{
    private BlockPos blockPosition;
    private IBlockState blockState;
    
    public SPacketBlockChange() {
    }
    
    public SPacketBlockChange(final World worldIn, final BlockPos posIn) {
        this.blockPosition = posIn;
        this.blockState = worldIn.getBlockState(posIn);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.blockPosition = buf.readBlockPos();
        this.blockState = Block.BLOCK_STATE_IDS.getByValue(buf.readVarInt());
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeBlockPos(this.blockPosition);
        buf.writeVarInt(Block.BLOCK_STATE_IDS.get(this.blockState));
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleBlockChange(this);
    }
    
    public IBlockState getBlockState() {
        return this.blockState;
    }
    
    public BlockPos getBlockPosition() {
        return this.blockPosition;
    }
}
