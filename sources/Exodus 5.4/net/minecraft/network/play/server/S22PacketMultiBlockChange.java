/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;

public class S22PacketMultiBlockChange
implements Packet<INetHandlerPlayClient> {
    private ChunkCoordIntPair chunkPosCoord;
    private BlockUpdateData[] changedBlocks;

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleMultiBlockChange(this);
    }

    public BlockUpdateData[] getChangedBlocks() {
        return this.changedBlocks;
    }

    public S22PacketMultiBlockChange() {
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.chunkPosCoord = new ChunkCoordIntPair(packetBuffer.readInt(), packetBuffer.readInt());
        this.changedBlocks = new BlockUpdateData[packetBuffer.readVarIntFromBuffer()];
        int n = 0;
        while (n < this.changedBlocks.length) {
            this.changedBlocks[n] = new BlockUpdateData(packetBuffer.readShort(), Block.BLOCK_STATE_IDS.getByValue(packetBuffer.readVarIntFromBuffer()));
            ++n;
        }
    }

    public S22PacketMultiBlockChange(int n, short[] sArray, Chunk chunk) {
        this.chunkPosCoord = new ChunkCoordIntPair(chunk.xPosition, chunk.zPosition);
        this.changedBlocks = new BlockUpdateData[n];
        int n2 = 0;
        while (n2 < this.changedBlocks.length) {
            this.changedBlocks[n2] = new BlockUpdateData(sArray[n2], chunk);
            ++n2;
        }
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeInt(this.chunkPosCoord.chunkXPos);
        packetBuffer.writeInt(this.chunkPosCoord.chunkZPos);
        packetBuffer.writeVarIntToBuffer(this.changedBlocks.length);
        BlockUpdateData[] blockUpdateDataArray = this.changedBlocks;
        int n = this.changedBlocks.length;
        int n2 = 0;
        while (n2 < n) {
            BlockUpdateData blockUpdateData = blockUpdateDataArray[n2];
            packetBuffer.writeShort(blockUpdateData.func_180089_b());
            packetBuffer.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(blockUpdateData.getBlockState()));
            ++n2;
        }
    }

    public class BlockUpdateData {
        private final IBlockState blockState;
        private final short chunkPosCrammed;

        public IBlockState getBlockState() {
            return this.blockState;
        }

        public BlockUpdateData(short s, IBlockState iBlockState) {
            this.chunkPosCrammed = s;
            this.blockState = iBlockState;
        }

        public BlockUpdateData(short s, Chunk chunk) {
            this.chunkPosCrammed = s;
            this.blockState = chunk.getBlockState(this.getPos());
        }

        public BlockPos getPos() {
            return new BlockPos(S22PacketMultiBlockChange.this.chunkPosCoord.getBlock(this.chunkPosCrammed >> 12 & 0xF, this.chunkPosCrammed & 0xFF, this.chunkPosCrammed >> 8 & 0xF));
        }

        public short func_180089_b() {
            return this.chunkPosCrammed;
        }
    }
}

