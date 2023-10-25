/*
 * Decompiled with CFR 0.150.
 */
package baritone.cache;

import baritone.Baritone;
import baritone.api.cache.ICachedRegion;
import baritone.api.utils.BlockUtils;
import baritone.cache.CachedChunk;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public final class CachedRegion
implements ICachedRegion {
    private static final byte CHUNK_NOT_PRESENT = 0;
    private static final byte CHUNK_PRESENT = 1;
    private static final int CACHED_REGION_MAGIC = 456022910;
    private final CachedChunk[][] chunks = new CachedChunk[32][32];
    private final int x;
    private final int z;
    private final int dimension;
    private boolean hasUnsavedChanges;

    CachedRegion(int x, int z, int dimension) {
        this.x = x;
        this.z = z;
        this.hasUnsavedChanges = false;
        this.dimension = dimension;
    }

    @Override
    public final IBlockState getBlock(int x, int y, int z) {
        CachedChunk chunk = this.chunks[x >> 4][z >> 4];
        if (chunk != null) {
            return chunk.getBlock(x & 0xF, y, z & 0xF, this.dimension);
        }
        return null;
    }

    @Override
    public final boolean isCached(int x, int z) {
        return this.chunks[x >> 4][z >> 4] != null;
    }

    public final ArrayList<BlockPos> getLocationsOf(String block) {
        ArrayList<BlockPos> res = new ArrayList<BlockPos>();
        for (int chunkX = 0; chunkX < 32; ++chunkX) {
            for (int chunkZ = 0; chunkZ < 32; ++chunkZ) {
                ArrayList<BlockPos> locs;
                if (this.chunks[chunkX][chunkZ] == null || (locs = this.chunks[chunkX][chunkZ].getAbsoluteBlocks(block)) == null) continue;
                res.addAll(locs);
            }
        }
        return res;
    }

    public final synchronized void updateCachedChunk(int chunkX, int chunkZ, CachedChunk chunk) {
        this.chunks[chunkX][chunkZ] = chunk;
        this.hasUnsavedChanges = true;
    }

    public final synchronized void save(String directory) {
        if (!this.hasUnsavedChanges) {
            return;
        }
        this.removeExpired();
        try {
            Path path = Paths.get(directory, new String[0]);
            if (!Files.exists(path, new LinkOption[0])) {
                Files.createDirectories(path, new FileAttribute[0]);
            }
            System.out.println("Saving region " + this.x + "," + this.z + " to disk " + path);
            Path regionFile = CachedRegion.getRegionFile(path, this.x, this.z);
            if (!Files.exists(regionFile, new LinkOption[0])) {
                Files.createFile(regionFile, new FileAttribute[0]);
            }
            try (FileOutputStream fileOut = new FileOutputStream(regionFile.toFile());
                 GZIPOutputStream gzipOut = new GZIPOutputStream((OutputStream)fileOut, 16384);
                 DataOutputStream out = new DataOutputStream(gzipOut);){
                int z;
                int x;
                out.writeInt(456022910);
                for (x = 0; x < 32; ++x) {
                    for (z = 0; z < 32; ++z) {
                        CachedChunk chunk = this.chunks[x][z];
                        if (chunk == null) {
                            out.write(0);
                            continue;
                        }
                        out.write(1);
                        byte[] chunkBytes = chunk.toByteArray();
                        out.write(chunkBytes);
                        out.write(new byte[16384 - chunkBytes.length]);
                    }
                }
                for (x = 0; x < 32; ++x) {
                    for (z = 0; z < 32; ++z) {
                        if (this.chunks[x][z] == null) continue;
                        for (int i = 0; i < 256; ++i) {
                            out.writeUTF(BlockUtils.blockToString(this.chunks[x][z].getOverview()[i].getBlock()));
                        }
                    }
                }
                for (x = 0; x < 32; ++x) {
                    for (z = 0; z < 32; ++z) {
                        if (this.chunks[x][z] == null) continue;
                        Map<String, List<BlockPos>> locs = this.chunks[x][z].getRelativeBlocks();
                        out.writeShort(locs.entrySet().size());
                        for (Map.Entry<String, List<BlockPos>> entry : locs.entrySet()) {
                            out.writeUTF(entry.getKey());
                            out.writeShort(entry.getValue().size());
                            for (BlockPos pos : entry.getValue()) {
                                out.writeByte((byte)(pos.getZ() << 4 | pos.getX()));
                                out.writeByte((byte)pos.getY());
                            }
                        }
                    }
                }
                for (x = 0; x < 32; ++x) {
                    for (z = 0; z < 32; ++z) {
                        if (this.chunks[x][z] == null) continue;
                        out.writeLong(this.chunks[x][z].cacheTimestamp);
                    }
                }
            }
            this.hasUnsavedChanges = false;
            System.out.println("Saved region successfully");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void load(String directory) {
        try {
            Path regionFile;
            Path path = Paths.get(directory, new String[0]);
            if (!Files.exists(path, new LinkOption[0])) {
                Files.createDirectories(path, new FileAttribute[0]);
            }
            if (!Files.exists(regionFile = CachedRegion.getRegionFile(path, this.x, this.z), new LinkOption[0])) {
                return;
            }
            System.out.println("Loading region " + this.x + "," + this.z + " from disk " + path);
            long start = System.nanoTime() / 1000000L;
            try (FileInputStream fileIn = new FileInputStream(regionFile.toFile());
                 GZIPInputStream gzipIn = new GZIPInputStream((InputStream)fileIn, 32768);
                 DataInputStream in = new DataInputStream(gzipIn);){
                int z;
                int x;
                int magic = in.readInt();
                if (magic != 456022910) {
                    throw new IOException("Bad magic value " + magic);
                }
                boolean[][] present = new boolean[32][32];
                BitSet[][] bitSets = new BitSet[32][32];
                Map[][] location = new Map[32][32];
                IBlockState[][][] overview = new IBlockState[32][32][];
                long[][] cacheTimestamp = new long[32][32];
                for (x = 0; x < 32; ++x) {
                    block34: for (z = 0; z < 32; ++z) {
                        int isChunkPresent = in.read();
                        switch (isChunkPresent) {
                            case 1: {
                                byte[] bytes = new byte[16384];
                                in.readFully(bytes);
                                bitSets[x][z] = BitSet.valueOf(bytes);
                                location[x][z] = new HashMap();
                                overview[x][z] = new IBlockState[256];
                                present[x][z] = true;
                                continue block34;
                            }
                            case 0: {
                                continue block34;
                            }
                            default: {
                                throw new IOException("Malformed stream");
                            }
                        }
                    }
                }
                for (x = 0; x < 32; ++x) {
                    for (z = 0; z < 32; ++z) {
                        if (!present[x][z]) continue;
                        for (int i = 0; i < 256; ++i) {
                            overview[x][z][i] = BlockUtils.stringToBlockRequired(in.readUTF()).getDefaultState();
                        }
                    }
                }
                for (x = 0; x < 32; ++x) {
                    for (z = 0; z < 32; ++z) {
                        if (!present[x][z]) continue;
                        int numSpecialBlockTypes = in.readShort() & 0xFFFF;
                        for (int i = 0; i < numSpecialBlockTypes; ++i) {
                            String blockName = in.readUTF();
                            BlockUtils.stringToBlockRequired(blockName);
                            ArrayList<BlockPos> locs = new ArrayList<BlockPos>();
                            location[x][z].put(blockName, locs);
                            int numLocations = in.readShort() & 0xFFFF;
                            if (numLocations == 0) {
                                numLocations = 65536;
                            }
                            for (int j = 0; j < numLocations; ++j) {
                                byte xz = in.readByte();
                                int X = xz & 0xF;
                                int Z = xz >>> 4 & 0xF;
                                int Y = in.readByte() & 0xFF;
                                locs.add(new BlockPos(X, Y, Z));
                            }
                        }
                    }
                }
                for (x = 0; x < 32; ++x) {
                    for (z = 0; z < 32; ++z) {
                        if (!present[x][z]) continue;
                        cacheTimestamp[x][z] = in.readLong();
                    }
                }
                for (x = 0; x < 32; ++x) {
                    for (z = 0; z < 32; ++z) {
                        if (!present[x][z]) continue;
                        int regionX = this.x;
                        int regionZ = this.z;
                        int chunkX = x + 32 * regionX;
                        int chunkZ = z + 32 * regionZ;
                        this.chunks[x][z] = new CachedChunk(chunkX, chunkZ, bitSets[x][z], overview[x][z], location[x][z], cacheTimestamp[x][z]);
                    }
                }
            }
            this.removeExpired();
            this.hasUnsavedChanges = false;
            long end = System.nanoTime() / 1000000L;
            System.out.println("Loaded region successfully in " + (end - start) + "ms");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public final synchronized void removeExpired() {
        long expiry = (Long)Baritone.settings().cachedChunksExpirySeconds.value;
        if (expiry < 0L) {
            return;
        }
        long now = System.currentTimeMillis();
        long oldestAcceptableAge = now - expiry * 1000L;
        for (int x = 0; x < 32; ++x) {
            for (int z = 0; z < 32; ++z) {
                if (this.chunks[x][z] == null || this.chunks[x][z].cacheTimestamp >= oldestAcceptableAge) continue;
                System.out.println("Removing chunk " + (x + 32 * this.x) + "," + (z + 32 * this.z) + " because it was cached " + (now - this.chunks[x][z].cacheTimestamp) / 1000L + " seconds ago, and max age is " + expiry);
                this.chunks[x][z] = null;
            }
        }
    }

    public final synchronized CachedChunk mostRecentlyModified() {
        CachedChunk recent = null;
        for (int x = 0; x < 32; ++x) {
            for (int z = 0; z < 32; ++z) {
                if (this.chunks[x][z] == null || recent != null && this.chunks[x][z].cacheTimestamp <= recent.cacheTimestamp) continue;
                recent = this.chunks[x][z];
            }
        }
        return recent;
    }

    @Override
    public final int getX() {
        return this.x;
    }

    @Override
    public final int getZ() {
        return this.z;
    }

    private static Path getRegionFile(Path cacheDir, int regionX, int regionZ) {
        return Paths.get(cacheDir.toString(), "r." + regionX + "." + regionZ + ".bcr");
    }
}

