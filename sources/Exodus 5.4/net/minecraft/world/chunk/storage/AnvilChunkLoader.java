/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.world.chunk.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnvilChunkLoader
implements IThreadedFileIO,
IChunkLoader {
    private final File chunkSaveLocation;
    private Set<ChunkCoordIntPair> pendingAnvilChunksCoordinates;
    private boolean field_183014_e = false;
    private static final Logger logger = LogManager.getLogger();
    private Map<ChunkCoordIntPair, NBTTagCompound> chunksToRemove = new ConcurrentHashMap<ChunkCoordIntPair, NBTTagCompound>();

    protected void addChunkToPending(ChunkCoordIntPair chunkCoordIntPair, NBTTagCompound nBTTagCompound) {
        if (!this.pendingAnvilChunksCoordinates.contains(chunkCoordIntPair)) {
            this.chunksToRemove.put(chunkCoordIntPair, nBTTagCompound);
        }
        ThreadedFileIOBase.getThreadedIOInstance().queueIO(this);
    }

    @Override
    public boolean writeNextIO() {
        if (this.chunksToRemove.isEmpty()) {
            if (this.field_183014_e) {
                logger.info("ThreadedAnvilChunkStorage ({}): All chunks are saved", new Object[]{this.chunkSaveLocation.getName()});
            }
            return false;
        }
        ChunkCoordIntPair chunkCoordIntPair = this.chunksToRemove.keySet().iterator().next();
        this.pendingAnvilChunksCoordinates.add(chunkCoordIntPair);
        NBTTagCompound nBTTagCompound = this.chunksToRemove.remove(chunkCoordIntPair);
        if (nBTTagCompound != null) {
            try {
                this.func_183013_b(chunkCoordIntPair, nBTTagCompound);
            }
            catch (Exception exception) {
                logger.error("Failed to save chunk", (Throwable)exception);
            }
        }
        boolean bl = true;
        this.pendingAnvilChunksCoordinates.remove(chunkCoordIntPair);
        return bl;
    }

    protected Chunk checkedReadChunkFromNBT(World world, int n, int n2, NBTTagCompound nBTTagCompound) {
        if (!nBTTagCompound.hasKey("Level", 10)) {
            logger.error("Chunk file at " + n + "," + n2 + " is missing level data, skipping");
            return null;
        }
        NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("Level");
        if (!nBTTagCompound2.hasKey("Sections", 9)) {
            logger.error("Chunk file at " + n + "," + n2 + " is missing block data, skipping");
            return null;
        }
        Chunk chunk = this.readChunkFromNBT(world, nBTTagCompound2);
        if (!chunk.isAtLocation(n, n2)) {
            logger.error("Chunk file at " + n + "," + n2 + " is in the wrong location; relocating. (Expected " + n + ", " + n2 + ", got " + chunk.xPosition + ", " + chunk.zPosition + ")");
            nBTTagCompound2.setInteger("xPos", n);
            nBTTagCompound2.setInteger("zPos", n2);
            chunk = this.readChunkFromNBT(world, nBTTagCompound2);
        }
        return chunk;
    }

    public AnvilChunkLoader(File file) {
        this.pendingAnvilChunksCoordinates = Collections.newSetFromMap(new ConcurrentHashMap());
        this.chunkSaveLocation = file;
    }

    private void func_183013_b(ChunkCoordIntPair chunkCoordIntPair, NBTTagCompound nBTTagCompound) throws IOException {
        DataOutputStream dataOutputStream = RegionFileCache.getChunkOutputStream(this.chunkSaveLocation, chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos);
        CompressedStreamTools.write(nBTTagCompound, dataOutputStream);
        dataOutputStream.close();
    }

    private Chunk readChunkFromNBT(World world, NBTTagCompound nBTTagCompound) {
        NBTTagList nBTTagList;
        NBTTagList nBTTagList2;
        Object object;
        Object object2;
        Object object3;
        Object object4;
        int n;
        NBTBase nBTBase;
        int n2 = nBTTagCompound.getInteger("xPos");
        int n3 = nBTTagCompound.getInteger("zPos");
        Chunk chunk = new Chunk(world, n2, n3);
        chunk.setHeightMap(nBTTagCompound.getIntArray("HeightMap"));
        chunk.setTerrainPopulated(nBTTagCompound.getBoolean("TerrainPopulated"));
        chunk.setLightPopulated(nBTTagCompound.getBoolean("LightPopulated"));
        chunk.setInhabitedTime(nBTTagCompound.getLong("InhabitedTime"));
        NBTTagList nBTTagList3 = nBTTagCompound.getTagList("Sections", 10);
        int n4 = 16;
        ExtendedBlockStorage[] extendedBlockStorageArray = new ExtendedBlockStorage[n4];
        boolean bl = !world.provider.getHasNoSky();
        int n5 = 0;
        while (n5 < nBTTagList3.tagCount()) {
            nBTBase = nBTTagList3.getCompoundTagAt(n5);
            n = ((NBTTagCompound)nBTBase).getByte("Y");
            object4 = new ExtendedBlockStorage(n << 4, bl);
            object3 = ((NBTTagCompound)nBTBase).getByteArray("Blocks");
            object2 = new NibbleArray(((NBTTagCompound)nBTBase).getByteArray("Data"));
            object = ((NBTTagCompound)nBTBase).hasKey("Add", 7) ? new NibbleArray(((NBTTagCompound)nBTBase).getByteArray("Add")) : null;
            char[] cArray = new char[((byte[])object3).length];
            int n6 = 0;
            while (n6 < cArray.length) {
                int n7 = n6 & 0xF;
                int n8 = n6 >> 8 & 0xF;
                int n9 = n6 >> 4 & 0xF;
                int n10 = object != null ? ((NibbleArray)object).get(n7, n8, n9) : 0;
                cArray[n6] = (char)(n10 << 12 | (object3[n6] & 0xFF) << 4 | ((NibbleArray)object2).get(n7, n8, n9));
                ++n6;
            }
            ((ExtendedBlockStorage)object4).setData(cArray);
            ((ExtendedBlockStorage)object4).setBlocklightArray(new NibbleArray(((NBTTagCompound)nBTBase).getByteArray("BlockLight")));
            if (bl) {
                ((ExtendedBlockStorage)object4).setSkylightArray(new NibbleArray(((NBTTagCompound)nBTBase).getByteArray("SkyLight")));
            }
            ((ExtendedBlockStorage)object4).removeInvalidBlocks();
            extendedBlockStorageArray[n] = object4;
            ++n5;
        }
        chunk.setStorageArrays(extendedBlockStorageArray);
        if (nBTTagCompound.hasKey("Biomes", 7)) {
            chunk.setBiomeArray(nBTTagCompound.getByteArray("Biomes"));
        }
        if ((nBTTagList2 = nBTTagCompound.getTagList("Entities", 10)) != null) {
            int n11 = 0;
            while (n11 < nBTTagList2.tagCount()) {
                NBTTagCompound nBTTagCompound2 = nBTTagList2.getCompoundTagAt(n11);
                object4 = EntityList.createEntityFromNBT(nBTTagCompound2, world);
                chunk.setHasEntities(true);
                if (object4 != null) {
                    chunk.addEntity((Entity)object4);
                    object3 = object4;
                    object2 = nBTTagCompound2;
                    while (((NBTTagCompound)object2).hasKey("Riding", 10)) {
                        object = EntityList.createEntityFromNBT(((NBTTagCompound)object2).getCompoundTag("Riding"), world);
                        if (object != null) {
                            chunk.addEntity((Entity)object);
                            ((Entity)object3).mountEntity((Entity)object);
                        }
                        object3 = object;
                        object2 = ((NBTTagCompound)object2).getCompoundTag("Riding");
                    }
                }
                ++n11;
            }
        }
        if ((nBTBase = nBTTagCompound.getTagList("TileEntities", 10)) != null) {
            n = 0;
            while (n < ((NBTTagList)nBTBase).tagCount()) {
                object4 = ((NBTTagList)nBTBase).getCompoundTagAt(n);
                object3 = TileEntity.createAndLoadEntity((NBTTagCompound)object4);
                if (object3 != null) {
                    chunk.addTileEntity((TileEntity)object3);
                }
                ++n;
            }
        }
        if (nBTTagCompound.hasKey("TileTicks", 9) && (nBTTagList = nBTTagCompound.getTagList("TileTicks", 10)) != null) {
            int n12 = 0;
            while (n12 < nBTTagList.tagCount()) {
                object3 = nBTTagList.getCompoundTagAt(n12);
                object2 = ((NBTTagCompound)object3).hasKey("i", 8) ? Block.getBlockFromName(((NBTTagCompound)object3).getString("i")) : Block.getBlockById(((NBTTagCompound)object3).getInteger("i"));
                world.scheduleBlockUpdate(new BlockPos(((NBTTagCompound)object3).getInteger("x"), ((NBTTagCompound)object3).getInteger("y"), ((NBTTagCompound)object3).getInteger("z")), (Block)object2, ((NBTTagCompound)object3).getInteger("t"), ((NBTTagCompound)object3).getInteger("p"));
                ++n12;
            }
        }
        return chunk;
    }

    @Override
    public void saveExtraData() {
        this.field_183014_e = true;
        while (true) {
            if (this.writeNextIO()) continue;
        }
    }

    @Override
    public void saveExtraChunkData(World world, Chunk chunk) throws IOException {
    }

    private void writeChunkToNBT(Chunk chunk, World world, NBTTagCompound nBTTagCompound) {
        Object object;
        NBTTagCompound nBTTagCompound2;
        Object object2;
        nBTTagCompound.setByte("V", (byte)1);
        nBTTagCompound.setInteger("xPos", chunk.xPosition);
        nBTTagCompound.setInteger("zPos", chunk.zPosition);
        nBTTagCompound.setLong("LastUpdate", world.getTotalWorldTime());
        nBTTagCompound.setIntArray("HeightMap", chunk.getHeightMap());
        nBTTagCompound.setBoolean("TerrainPopulated", chunk.isTerrainPopulated());
        nBTTagCompound.setBoolean("LightPopulated", chunk.isLightPopulated());
        nBTTagCompound.setLong("InhabitedTime", chunk.getInhabitedTime());
        ExtendedBlockStorage[] extendedBlockStorageArray = chunk.getBlockStorageArray();
        NBTTagList nBTTagList = new NBTTagList();
        boolean bl = !world.provider.getHasNoSky();
        ExtendedBlockStorage[] extendedBlockStorageArray2 = extendedBlockStorageArray;
        int n = extendedBlockStorageArray.length;
        int n2 = 0;
        while (n2 < n) {
            object2 = extendedBlockStorageArray2[n2];
            if (object2 != null) {
                nBTTagCompound2 = new NBTTagCompound();
                nBTTagCompound2.setByte("Y", (byte)(((ExtendedBlockStorage)object2).getYLocation() >> 4 & 0xFF));
                object = new byte[((ExtendedBlockStorage)object2).getData().length];
                NibbleArray object3 = new NibbleArray();
                Object object4 = null;
                int nBTTagCompound3 = 0;
                while (nBTTagCompound3 < ((ExtendedBlockStorage)object2).getData().length) {
                    char c = ((ExtendedBlockStorage)object2).getData()[nBTTagCompound3];
                    int n3 = nBTTagCompound3 & 0xF;
                    int n4 = nBTTagCompound3 >> 8 & 0xF;
                    int n5 = nBTTagCompound3 >> 4 & 0xF;
                    if (c >> 12 != 0) {
                        if (object4 == null) {
                            object4 = new NibbleArray();
                        }
                        ((NibbleArray)object4).set(n3, n4, n5, c >> 12);
                    }
                    object[nBTTagCompound3] = (byte)(c >> 4 & 0xFF);
                    object3.set(n3, n4, n5, c & 0xF);
                    ++nBTTagCompound3;
                }
                nBTTagCompound2.setByteArray("Blocks", (byte[])object);
                nBTTagCompound2.setByteArray("Data", object3.getData());
                if (object4 != null) {
                    nBTTagCompound2.setByteArray("Add", ((NibbleArray)object4).getData());
                }
                nBTTagCompound2.setByteArray("BlockLight", ((ExtendedBlockStorage)object2).getBlocklightArray().getData());
                if (bl) {
                    nBTTagCompound2.setByteArray("SkyLight", ((ExtendedBlockStorage)object2).getSkylightArray().getData());
                } else {
                    nBTTagCompound2.setByteArray("SkyLight", new byte[((ExtendedBlockStorage)object2).getBlocklightArray().getData().length]);
                }
                nBTTagList.appendTag(nBTTagCompound2);
            }
            ++n2;
        }
        nBTTagCompound.setTag("Sections", nBTTagList);
        nBTTagCompound.setByteArray("Biomes", chunk.getBiomeArray());
        chunk.setHasEntities(false);
        object2 = new NBTTagList();
        n2 = 0;
        while (n2 < chunk.getEntityLists().length) {
            for (Entity entity : chunk.getEntityLists()[n2]) {
                if (!entity.writeToNBTOptional(nBTTagCompound2 = new NBTTagCompound())) continue;
                chunk.setHasEntities(true);
                ((NBTTagList)object2).appendTag(nBTTagCompound2);
            }
            ++n2;
        }
        nBTTagCompound.setTag("Entities", (NBTBase)object2);
        NBTTagList nBTTagList2 = new NBTTagList();
        for (TileEntity tileEntity : chunk.getTileEntityMap().values()) {
            nBTTagCompound2 = new NBTTagCompound();
            tileEntity.writeToNBT(nBTTagCompound2);
            nBTTagList2.appendTag(nBTTagCompound2);
        }
        nBTTagCompound.setTag("TileEntities", nBTTagList2);
        List<NextTickListEntry> list = world.getPendingBlockUpdates(chunk, false);
        if (list != null) {
            long l = world.getTotalWorldTime();
            object = new NBTTagList();
            for (NextTickListEntry nextTickListEntry : list) {
                NBTTagCompound nBTTagCompound3 = new NBTTagCompound();
                ResourceLocation resourceLocation = (ResourceLocation)Block.blockRegistry.getNameForObject(nextTickListEntry.getBlock());
                nBTTagCompound3.setString("i", resourceLocation == null ? "" : resourceLocation.toString());
                nBTTagCompound3.setInteger("x", nextTickListEntry.position.getX());
                nBTTagCompound3.setInteger("y", nextTickListEntry.position.getY());
                nBTTagCompound3.setInteger("z", nextTickListEntry.position.getZ());
                nBTTagCompound3.setInteger("t", (int)(nextTickListEntry.scheduledTime - l));
                nBTTagCompound3.setInteger("p", nextTickListEntry.priority);
                ((NBTTagList)object).appendTag(nBTTagCompound3);
            }
            nBTTagCompound.setTag("TileTicks", (NBTBase)object);
        }
    }

    @Override
    public void chunkTick() {
    }

    @Override
    public Chunk loadChunk(World world, int n, int n2) throws IOException {
        ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair(n, n2);
        NBTTagCompound nBTTagCompound = this.chunksToRemove.get(chunkCoordIntPair);
        if (nBTTagCompound == null) {
            DataInputStream dataInputStream = RegionFileCache.getChunkInputStream(this.chunkSaveLocation, n, n2);
            if (dataInputStream == null) {
                return null;
            }
            nBTTagCompound = CompressedStreamTools.read(dataInputStream);
        }
        return this.checkedReadChunkFromNBT(world, n, n2, nBTTagCompound);
    }

    @Override
    public void saveChunk(World world, Chunk chunk) throws IOException, MinecraftException {
        world.checkSessionLock();
        try {
            NBTTagCompound nBTTagCompound = new NBTTagCompound();
            NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
            nBTTagCompound.setTag("Level", nBTTagCompound2);
            this.writeChunkToNBT(chunk, world, nBTTagCompound2);
            this.addChunkToPending(chunk.getChunkCoordIntPair(), nBTTagCompound);
        }
        catch (Exception exception) {
            logger.error("Failed to save chunk", (Throwable)exception);
        }
    }
}

