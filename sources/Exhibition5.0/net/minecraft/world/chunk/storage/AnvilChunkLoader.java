// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.chunk.storage;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EntityList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.nbt.NBTTagList;
import java.io.DataOutputStream;
import java.io.DataOutput;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraft.world.MinecraftException;
import net.minecraft.nbt.NBTBase;
import java.io.IOException;
import java.io.DataInputStream;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.World;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.Set;
import java.util.List;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.storage.IThreadedFileIO;

public class AnvilChunkLoader implements IChunkLoader, IThreadedFileIO
{
    private static final Logger logger;
    private List chunksToRemove;
    private Set pendingAnvilChunksCoordinates;
    private Object syncLockObject;
    private final File chunkSaveLocation;
    private static final String __OBFID = "CL_00000384";
    
    public AnvilChunkLoader(final File p_i2003_1_) {
        this.chunksToRemove = Lists.newArrayList();
        this.pendingAnvilChunksCoordinates = Sets.newHashSet();
        this.syncLockObject = new Object();
        this.chunkSaveLocation = p_i2003_1_;
    }
    
    @Override
    public Chunk loadChunk(final World worldIn, final int x, final int z) throws IOException {
        NBTTagCompound var4 = null;
        final ChunkCoordIntPair var5 = new ChunkCoordIntPair(x, z);
        final Object var6 = this.syncLockObject;
        synchronized (this.syncLockObject) {
            if (this.pendingAnvilChunksCoordinates.contains(var5)) {
                for (int var7 = 0; var7 < this.chunksToRemove.size(); ++var7) {
                    if (this.chunksToRemove.get(var7).chunkCoordinate.equals(var5)) {
                        var4 = this.chunksToRemove.get(var7).nbtTags;
                        break;
                    }
                }
            }
        }
        if (var4 == null) {
            final DataInputStream var8 = RegionFileCache.getChunkInputStream(this.chunkSaveLocation, x, z);
            if (var8 == null) {
                return null;
            }
            var4 = CompressedStreamTools.read(var8);
        }
        return this.checkedReadChunkFromNBT(worldIn, x, z, var4);
    }
    
    protected Chunk checkedReadChunkFromNBT(final World worldIn, final int p_75822_2_, final int p_75822_3_, final NBTTagCompound p_75822_4_) {
        if (!p_75822_4_.hasKey("Level", 10)) {
            AnvilChunkLoader.logger.error("Chunk file at " + p_75822_2_ + "," + p_75822_3_ + " is missing level data, skipping");
            return null;
        }
        if (!p_75822_4_.getCompoundTag("Level").hasKey("Sections", 9)) {
            AnvilChunkLoader.logger.error("Chunk file at " + p_75822_2_ + "," + p_75822_3_ + " is missing block data, skipping");
            return null;
        }
        Chunk var5 = this.readChunkFromNBT(worldIn, p_75822_4_.getCompoundTag("Level"));
        if (!var5.isAtLocation(p_75822_2_, p_75822_3_)) {
            AnvilChunkLoader.logger.error("Chunk file at " + p_75822_2_ + "," + p_75822_3_ + " is in the wrong location; relocating. (Expected " + p_75822_2_ + ", " + p_75822_3_ + ", got " + var5.xPosition + ", " + var5.zPosition + ")");
            p_75822_4_.setInteger("xPos", p_75822_2_);
            p_75822_4_.setInteger("zPos", p_75822_3_);
            var5 = this.readChunkFromNBT(worldIn, p_75822_4_.getCompoundTag("Level"));
        }
        return var5;
    }
    
    @Override
    public void saveChunk(final World worldIn, final Chunk chunkIn) throws MinecraftException, IOException {
        worldIn.checkSessionLock();
        try {
            final NBTTagCompound var3 = new NBTTagCompound();
            final NBTTagCompound var4 = new NBTTagCompound();
            var3.setTag("Level", var4);
            this.writeChunkToNBT(chunkIn, worldIn, var4);
            this.addChunkToPending(chunkIn.getChunkCoordIntPair(), var3);
        }
        catch (Exception var5) {
            var5.printStackTrace();
        }
    }
    
    protected void addChunkToPending(final ChunkCoordIntPair p_75824_1_, final NBTTagCompound p_75824_2_) {
        final Object var3 = this.syncLockObject;
        synchronized (this.syncLockObject) {
            if (this.pendingAnvilChunksCoordinates.contains(p_75824_1_)) {
                for (int var4 = 0; var4 < this.chunksToRemove.size(); ++var4) {
                    if (this.chunksToRemove.get(var4).chunkCoordinate.equals(p_75824_1_)) {
                        this.chunksToRemove.set(var4, new PendingChunk(p_75824_1_, p_75824_2_));
                        return;
                    }
                }
            }
            this.chunksToRemove.add(new PendingChunk(p_75824_1_, p_75824_2_));
            this.pendingAnvilChunksCoordinates.add(p_75824_1_);
            ThreadedFileIOBase.func_178779_a().queueIO(this);
        }
    }
    
    @Override
    public boolean writeNextIO() {
        PendingChunk var1 = null;
        final Object var2 = this.syncLockObject;
        synchronized (this.syncLockObject) {
            if (this.chunksToRemove.isEmpty()) {
                return false;
            }
            var1 = this.chunksToRemove.remove(0);
            this.pendingAnvilChunksCoordinates.remove(var1.chunkCoordinate);
        }
        if (var1 != null) {
            try {
                this.writeChunkNBTTags(var1);
            }
            catch (Exception var3) {
                var3.printStackTrace();
            }
        }
        return true;
    }
    
    private void writeChunkNBTTags(final PendingChunk p_75821_1_) throws IOException {
        final DataOutputStream var2 = RegionFileCache.getChunkOutputStream(this.chunkSaveLocation, p_75821_1_.chunkCoordinate.chunkXPos, p_75821_1_.chunkCoordinate.chunkZPos);
        CompressedStreamTools.write(p_75821_1_.nbtTags, var2);
        var2.close();
    }
    
    @Override
    public void saveExtraChunkData(final World worldIn, final Chunk chunkIn) {
    }
    
    @Override
    public void chunkTick() {
    }
    
    @Override
    public void saveExtraData() {
        while (this.writeNextIO()) {}
    }
    
    private void writeChunkToNBT(final Chunk p_75820_1_, final World worldIn, final NBTTagCompound p_75820_3_) {
        p_75820_3_.setByte("V", (byte)1);
        p_75820_3_.setInteger("xPos", p_75820_1_.xPosition);
        p_75820_3_.setInteger("zPos", p_75820_1_.zPosition);
        p_75820_3_.setLong("LastUpdate", worldIn.getTotalWorldTime());
        p_75820_3_.setIntArray("HeightMap", p_75820_1_.getHeightMap());
        p_75820_3_.setBoolean("TerrainPopulated", p_75820_1_.isTerrainPopulated());
        p_75820_3_.setBoolean("LightPopulated", p_75820_1_.isLightPopulated());
        p_75820_3_.setLong("InhabitedTime", p_75820_1_.getInhabitedTime());
        final ExtendedBlockStorage[] var4 = p_75820_1_.getBlockStorageArray();
        final NBTTagList var5 = new NBTTagList();
        final boolean var6 = !worldIn.provider.getHasNoSky();
        final ExtendedBlockStorage[] var7 = var4;
        for (int var8 = var4.length, var9 = 0; var9 < var8; ++var9) {
            final ExtendedBlockStorage var10 = var7[var9];
            if (var10 != null) {
                final NBTTagCompound var11 = new NBTTagCompound();
                var11.setByte("Y", (byte)(var10.getYLocation() >> 4 & 0xFF));
                final byte[] var12 = new byte[var10.getData().length];
                final NibbleArray var13 = new NibbleArray();
                NibbleArray var14 = null;
                for (int var15 = 0; var15 < var10.getData().length; ++var15) {
                    final char var16 = var10.getData()[var15];
                    final int var17 = var15 & 0xF;
                    final int var18 = var15 >> 8 & 0xF;
                    final int var19 = var15 >> 4 & 0xF;
                    if (var16 >> 12 != '\0') {
                        if (var14 == null) {
                            var14 = new NibbleArray();
                        }
                        var14.set(var17, var18, var19, var16 >> 12);
                    }
                    var12[var15] = (byte)(var16 >> 4 & '\u00ff');
                    var13.set(var17, var18, var19, var16 & '\u000f');
                }
                var11.setByteArray("Blocks", var12);
                var11.setByteArray("Data", var13.getData());
                if (var14 != null) {
                    var11.setByteArray("Add", var14.getData());
                }
                var11.setByteArray("BlockLight", var10.getBlocklightArray().getData());
                if (var6) {
                    var11.setByteArray("SkyLight", var10.getSkylightArray().getData());
                }
                else {
                    var11.setByteArray("SkyLight", new byte[var10.getBlocklightArray().getData().length]);
                }
                var5.appendTag(var11);
            }
        }
        p_75820_3_.setTag("Sections", var5);
        p_75820_3_.setByteArray("Biomes", p_75820_1_.getBiomeArray());
        p_75820_1_.setHasEntities(false);
        final NBTTagList var20 = new NBTTagList();
        for (int var8 = 0; var8 < p_75820_1_.getEntityLists().length; ++var8) {
            for (final Entity var22 : p_75820_1_.getEntityLists()[var8]) {
                final NBTTagCompound var11 = new NBTTagCompound();
                if (var22.writeToNBTOptional(var11)) {
                    p_75820_1_.setHasEntities(true);
                    var20.appendTag(var11);
                }
            }
        }
        p_75820_3_.setTag("Entities", var20);
        final NBTTagList var23 = new NBTTagList();
        for (final TileEntity var24 : p_75820_1_.getTileEntityMap().values()) {
            final NBTTagCompound var11 = new NBTTagCompound();
            var24.writeToNBT(var11);
            var23.appendTag(var11);
        }
        p_75820_3_.setTag("TileEntities", var23);
        final List var25 = worldIn.getPendingBlockUpdates(p_75820_1_, false);
        if (var25 != null) {
            final long var26 = worldIn.getTotalWorldTime();
            final NBTTagList var27 = new NBTTagList();
            for (final NextTickListEntry var29 : var25) {
                final NBTTagCompound var30 = new NBTTagCompound();
                final ResourceLocation var31 = (ResourceLocation)Block.blockRegistry.getNameForObject(var29.func_151351_a());
                var30.setString("i", (var31 == null) ? "" : var31.toString());
                var30.setInteger("x", var29.field_180282_a.getX());
                var30.setInteger("y", var29.field_180282_a.getY());
                var30.setInteger("z", var29.field_180282_a.getZ());
                var30.setInteger("t", (int)(var29.scheduledTime - var26));
                var30.setInteger("p", var29.priority);
                var27.appendTag(var30);
            }
            p_75820_3_.setTag("TileTicks", var27);
        }
    }
    
    private Chunk readChunkFromNBT(final World worldIn, final NBTTagCompound p_75823_2_) {
        final int var3 = p_75823_2_.getInteger("xPos");
        final int var4 = p_75823_2_.getInteger("zPos");
        final Chunk var5 = new Chunk(worldIn, var3, var4);
        var5.setHeightMap(p_75823_2_.getIntArray("HeightMap"));
        var5.setTerrainPopulated(p_75823_2_.getBoolean("TerrainPopulated"));
        var5.setLightPopulated(p_75823_2_.getBoolean("LightPopulated"));
        var5.setInhabitedTime(p_75823_2_.getLong("InhabitedTime"));
        final NBTTagList var6 = p_75823_2_.getTagList("Sections", 10);
        final byte var7 = 16;
        final ExtendedBlockStorage[] var8 = new ExtendedBlockStorage[var7];
        final boolean var9 = !worldIn.provider.getHasNoSky();
        for (int var10 = 0; var10 < var6.tagCount(); ++var10) {
            final NBTTagCompound var11 = var6.getCompoundTagAt(var10);
            final byte var12 = var11.getByte("Y");
            final ExtendedBlockStorage var13 = new ExtendedBlockStorage(var12 << 4, var9);
            final byte[] var14 = var11.getByteArray("Blocks");
            final NibbleArray var15 = new NibbleArray(var11.getByteArray("Data"));
            final NibbleArray var16 = var11.hasKey("Add", 7) ? new NibbleArray(var11.getByteArray("Add")) : null;
            final char[] var17 = new char[var14.length];
            for (int var18 = 0; var18 < var17.length; ++var18) {
                final int var19 = var18 & 0xF;
                final int var20 = var18 >> 8 & 0xF;
                final int var21 = var18 >> 4 & 0xF;
                final int var22 = (var16 != null) ? var16.get(var19, var20, var21) : 0;
                var17[var18] = (char)(var22 << 12 | (var14[var18] & 0xFF) << 4 | var15.get(var19, var20, var21));
            }
            var13.setData(var17);
            var13.setBlocklightArray(new NibbleArray(var11.getByteArray("BlockLight")));
            if (var9) {
                var13.setSkylightArray(new NibbleArray(var11.getByteArray("SkyLight")));
            }
            var13.removeInvalidBlocks();
            var8[var12] = var13;
        }
        var5.setStorageArrays(var8);
        if (p_75823_2_.hasKey("Biomes", 7)) {
            var5.setBiomeArray(p_75823_2_.getByteArray("Biomes"));
        }
        final NBTTagList var23 = p_75823_2_.getTagList("Entities", 10);
        if (var23 != null) {
            for (int var24 = 0; var24 < var23.tagCount(); ++var24) {
                final NBTTagCompound var25 = var23.getCompoundTagAt(var24);
                final Entity var26 = EntityList.createEntityFromNBT(var25, worldIn);
                var5.setHasEntities(true);
                if (var26 != null) {
                    var5.addEntity(var26);
                    Entity var27 = var26;
                    for (NBTTagCompound var28 = var25; var28.hasKey("Riding", 10); var28 = var28.getCompoundTag("Riding")) {
                        final Entity var29 = EntityList.createEntityFromNBT(var28.getCompoundTag("Riding"), worldIn);
                        if (var29 != null) {
                            var5.addEntity(var29);
                            var27.mountEntity(var29);
                        }
                        var27 = var29;
                    }
                }
            }
        }
        final NBTTagList var30 = p_75823_2_.getTagList("TileEntities", 10);
        if (var30 != null) {
            for (int var31 = 0; var31 < var30.tagCount(); ++var31) {
                final NBTTagCompound var32 = var30.getCompoundTagAt(var31);
                final TileEntity var33 = TileEntity.createAndLoadEntity(var32);
                if (var33 != null) {
                    var5.addTileEntity(var33);
                }
            }
        }
        if (p_75823_2_.hasKey("TileTicks", 9)) {
            final NBTTagList var34 = p_75823_2_.getTagList("TileTicks", 10);
            if (var34 != null) {
                for (int var35 = 0; var35 < var34.tagCount(); ++var35) {
                    final NBTTagCompound var36 = var34.getCompoundTagAt(var35);
                    Block var37;
                    if (var36.hasKey("i", 8)) {
                        var37 = Block.getBlockFromName(var36.getString("i"));
                    }
                    else {
                        var37 = Block.getBlockById(var36.getInteger("i"));
                    }
                    worldIn.func_180497_b(new BlockPos(var36.getInteger("x"), var36.getInteger("y"), var36.getInteger("z")), var37, var36.getInteger("t"), var36.getInteger("p"));
                }
            }
        }
        return var5;
    }
    
    static {
        logger = LogManager.getLogger();
    }
    
    static class PendingChunk
    {
        public final ChunkCoordIntPair chunkCoordinate;
        public final NBTTagCompound nbtTags;
        private static final String __OBFID = "CL_00000385";
        
        public PendingChunk(final ChunkCoordIntPair p_i2002_1_, final NBTTagCompound p_i2002_2_) {
            this.chunkCoordinate = p_i2002_1_;
            this.nbtTags = p_i2002_2_;
        }
    }
}
