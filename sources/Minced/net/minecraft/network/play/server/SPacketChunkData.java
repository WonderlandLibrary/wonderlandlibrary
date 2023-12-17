// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import com.google.common.collect.Lists;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketChunkData implements Packet<INetHandlerPlayClient>
{
    private int chunkX;
    private int chunkZ;
    private int availableSections;
    private byte[] buffer;
    private List<NBTTagCompound> tileEntityTags;
    private boolean fullChunk;
    
    public SPacketChunkData() {
    }
    
    public SPacketChunkData(final Chunk chunkIn, final int changedSectionFilter) {
        this.chunkX = chunkIn.x;
        this.chunkZ = chunkIn.z;
        this.fullChunk = (changedSectionFilter == 65535);
        final boolean flag = chunkIn.getWorld().provider.hasSkyLight();
        this.buffer = new byte[this.calculateChunkSize(chunkIn, flag, changedSectionFilter)];
        this.availableSections = this.extractChunkData(new PacketBuffer(this.getWriteBuffer()), chunkIn, flag, changedSectionFilter);
        this.tileEntityTags = (List<NBTTagCompound>)Lists.newArrayList();
        for (final Map.Entry<BlockPos, TileEntity> entry : chunkIn.getTileEntityMap().entrySet()) {
            final BlockPos blockpos = entry.getKey();
            final TileEntity tileentity = entry.getValue();
            final int i = blockpos.getY() >> 4;
            if (this.isFullChunk() || (changedSectionFilter & 1 << i) != 0x0) {
                final NBTTagCompound nbttagcompound = tileentity.getUpdateTag();
                this.tileEntityTags.add(nbttagcompound);
            }
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.chunkX = buf.readInt();
        this.chunkZ = buf.readInt();
        this.fullChunk = buf.readBoolean();
        this.availableSections = buf.readVarInt();
        final int i = buf.readVarInt();
        if (i > 2097152) {
            throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
        }
        buf.readBytes(this.buffer = new byte[i]);
        final int j = buf.readVarInt();
        this.tileEntityTags = (List<NBTTagCompound>)Lists.newArrayList();
        for (int k = 0; k < j; ++k) {
            this.tileEntityTags.add(buf.readCompoundTag());
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.chunkX);
        buf.writeInt(this.chunkZ);
        buf.writeBoolean(this.fullChunk);
        buf.writeVarInt(this.availableSections);
        buf.writeVarInt(this.buffer.length);
        buf.writeBytes(this.buffer);
        buf.writeVarInt(this.tileEntityTags.size());
        for (final NBTTagCompound nbttagcompound : this.tileEntityTags) {
            buf.writeCompoundTag(nbttagcompound);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleChunkData(this);
    }
    
    public PacketBuffer getReadBuffer() {
        return new PacketBuffer(Unpooled.wrappedBuffer(this.buffer));
    }
    
    private ByteBuf getWriteBuffer() {
        final ByteBuf bytebuf = Unpooled.wrappedBuffer(this.buffer);
        bytebuf.writerIndex(0);
        return bytebuf;
    }
    
    public int extractChunkData(final PacketBuffer buf, final Chunk chunkIn, final boolean writeSkylight, final int changedSectionFilter) {
        int i = 0;
        final ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getBlockStorageArray();
        for (int j = 0, k = aextendedblockstorage.length; j < k; ++j) {
            final ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[j];
            if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE && (!this.isFullChunk() || !extendedblockstorage.isEmpty()) && (changedSectionFilter & 1 << j) != 0x0) {
                i |= 1 << j;
                extendedblockstorage.getData().write(buf);
                buf.writeBytes(extendedblockstorage.getBlockLight().getData());
                if (writeSkylight) {
                    buf.writeBytes(extendedblockstorage.getSkyLight().getData());
                }
            }
        }
        if (this.isFullChunk()) {
            buf.writeBytes(chunkIn.getBiomeArray());
        }
        return i;
    }
    
    protected int calculateChunkSize(final Chunk chunkIn, final boolean p_189556_2_, final int p_189556_3_) {
        int i = 0;
        final ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getBlockStorageArray();
        for (int j = 0, k = aextendedblockstorage.length; j < k; ++j) {
            final ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[j];
            if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE && (!this.isFullChunk() || !extendedblockstorage.isEmpty()) && (p_189556_3_ & 1 << j) != 0x0) {
                i += extendedblockstorage.getData().getSerializedSize();
                i += extendedblockstorage.getBlockLight().getData().length;
                if (p_189556_2_) {
                    i += extendedblockstorage.getSkyLight().getData().length;
                }
            }
        }
        if (this.isFullChunk()) {
            i += chunkIn.getBiomeArray().length;
        }
        return i;
    }
    
    public int getChunkX() {
        return this.chunkX;
    }
    
    public int getChunkZ() {
        return this.chunkZ;
    }
    
    public int getExtractedSize() {
        return this.availableSections;
    }
    
    public boolean isFullChunk() {
        return this.fullChunk;
    }
    
    public List<NBTTagCompound> getTileEntityTags() {
        return this.tileEntityTags;
    }
}
