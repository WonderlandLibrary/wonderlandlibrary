// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketMultiBlockChange implements Packet<INetHandlerPlayClient>
{
    private ChunkPos chunkPos;
    private BlockUpdateData[] changedBlocks;
    
    public SPacketMultiBlockChange() {
    }
    
    public SPacketMultiBlockChange(final int p_i46959_1_, final short[] p_i46959_2_, final Chunk p_i46959_3_) {
        this.chunkPos = new ChunkPos(p_i46959_3_.x, p_i46959_3_.z);
        this.changedBlocks = new BlockUpdateData[p_i46959_1_];
        for (int i = 0; i < this.changedBlocks.length; ++i) {
            this.changedBlocks[i] = new BlockUpdateData(p_i46959_2_[i], p_i46959_3_);
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.chunkPos = new ChunkPos(buf.readInt(), buf.readInt());
        this.changedBlocks = new BlockUpdateData[buf.readVarInt()];
        for (int i = 0; i < this.changedBlocks.length; ++i) {
            this.changedBlocks[i] = new BlockUpdateData(buf.readShort(), Block.BLOCK_STATE_IDS.getByValue(buf.readVarInt()));
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.chunkPos.x);
        buf.writeInt(this.chunkPos.z);
        buf.writeVarInt(this.changedBlocks.length);
        for (final BlockUpdateData spacketmultiblockchange$blockupdatedata : this.changedBlocks) {
            buf.writeShort(spacketmultiblockchange$blockupdatedata.getOffset());
            buf.writeVarInt(Block.BLOCK_STATE_IDS.get(spacketmultiblockchange$blockupdatedata.getBlockState()));
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleMultiBlockChange(this);
    }
    
    public BlockUpdateData[] getChangedBlocks() {
        return this.changedBlocks;
    }
    
    public class BlockUpdateData
    {
        private final short offset;
        private final IBlockState blockState;
        
        public BlockUpdateData(final short p_i46544_2_, final IBlockState p_i46544_3_) {
            this.offset = p_i46544_2_;
            this.blockState = p_i46544_3_;
        }
        
        public BlockUpdateData(final short p_i46545_2_, final Chunk p_i46545_3_) {
            this.offset = p_i46545_2_;
            this.blockState = p_i46545_3_.getBlockState(this.getPos());
        }
        
        public BlockPos getPos() {
            return new BlockPos(SPacketMultiBlockChange.this.chunkPos.getBlock(this.offset >> 12 & 0xF, this.offset & 0xFF, this.offset >> 8 & 0xF));
        }
        
        public short getOffset() {
            return this.offset;
        }
        
        public IBlockState getBlockState() {
            return this.blockState;
        }
    }
}
