// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk.storage;

import com.google.common.collect.Maps;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.util.Iterator;
import java.io.IOException;
import java.io.File;
import java.util.Map;

public class RegionFileCache
{
    private static final Map<File, RegionFile> REGIONS_BY_FILE;
    
    public static synchronized RegionFile createOrLoadRegionFile(final File worldDir, final int chunkX, final int chunkZ) {
        final File file1 = new File(worldDir, "region");
        final File file2 = new File(file1, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");
        final RegionFile regionfile = RegionFileCache.REGIONS_BY_FILE.get(file2);
        if (regionfile != null) {
            return regionfile;
        }
        if (!file1.exists()) {
            file1.mkdirs();
        }
        if (RegionFileCache.REGIONS_BY_FILE.size() >= 256) {
            clearRegionFileReferences();
        }
        final RegionFile regionfile2 = new RegionFile(file2);
        RegionFileCache.REGIONS_BY_FILE.put(file2, regionfile2);
        return regionfile2;
    }
    
    public static synchronized RegionFile getRegionFileIfExists(final File worldDir, final int chunkX, final int chunkZ) {
        final File file1 = new File(worldDir, "region");
        final File file2 = new File(file1, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");
        final RegionFile regionfile = RegionFileCache.REGIONS_BY_FILE.get(file2);
        if (regionfile != null) {
            return regionfile;
        }
        if (file1.exists() && file2.exists()) {
            if (RegionFileCache.REGIONS_BY_FILE.size() >= 256) {
                clearRegionFileReferences();
            }
            final RegionFile regionfile2 = new RegionFile(file2);
            RegionFileCache.REGIONS_BY_FILE.put(file2, regionfile2);
            return regionfile2;
        }
        return null;
    }
    
    public static synchronized void clearRegionFileReferences() {
        for (final RegionFile regionfile : RegionFileCache.REGIONS_BY_FILE.values()) {
            try {
                if (regionfile == null) {
                    continue;
                }
                regionfile.close();
            }
            catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
        RegionFileCache.REGIONS_BY_FILE.clear();
    }
    
    public static DataInputStream getChunkInputStream(final File worldDir, final int chunkX, final int chunkZ) {
        final RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
        return regionfile.getChunkDataInputStream(chunkX & 0x1F, chunkZ & 0x1F);
    }
    
    public static DataOutputStream getChunkOutputStream(final File worldDir, final int chunkX, final int chunkZ) {
        final RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
        return regionfile.getChunkDataOutputStream(chunkX & 0x1F, chunkZ & 0x1F);
    }
    
    public static boolean chunkExists(final File worldDir, final int chunkX, final int chunkZ) {
        final RegionFile regionfile = getRegionFileIfExists(worldDir, chunkX, chunkZ);
        return regionfile != null && regionfile.isChunkSaved(chunkX & 0x1F, chunkZ & 0x1F);
    }
    
    static {
        REGIONS_BY_FILE = Maps.newHashMap();
    }
}
