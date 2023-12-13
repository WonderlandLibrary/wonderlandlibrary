// 
// Decompiled by Procyon v0.5.30
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
    private static final Map regionsByFilename;
    private static final String __OBFID = "CL_00000383";
    
    public static synchronized RegionFile createOrLoadRegionFile(final File worldDir, final int chunkX, final int chunkZ) {
        final File var3 = new File(worldDir, "region");
        final File var4 = new File(var3, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");
        final RegionFile var5 = RegionFileCache.regionsByFilename.get(var4);
        if (var5 != null) {
            return var5;
        }
        if (!var3.exists()) {
            var3.mkdirs();
        }
        if (RegionFileCache.regionsByFilename.size() >= 256) {
            clearRegionFileReferences();
        }
        final RegionFile var6 = new RegionFile(var4);
        RegionFileCache.regionsByFilename.put(var4, var6);
        return var6;
    }
    
    public static synchronized void clearRegionFileReferences() {
        for (final RegionFile var2 : RegionFileCache.regionsByFilename.values()) {
            try {
                if (var2 == null) {
                    continue;
                }
                var2.close();
            }
            catch (IOException var3) {
                var3.printStackTrace();
            }
        }
        RegionFileCache.regionsByFilename.clear();
    }
    
    public static DataInputStream getChunkInputStream(final File worldDir, final int chunkX, final int chunkZ) {
        final RegionFile var3 = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
        return var3.getChunkDataInputStream(chunkX & 0x1F, chunkZ & 0x1F);
    }
    
    public static DataOutputStream getChunkOutputStream(final File worldDir, final int chunkX, final int chunkZ) {
        final RegionFile var3 = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
        return var3.getChunkDataOutputStream(chunkX & 0x1F, chunkZ & 0x1F);
    }
    
    static {
        regionsByFilename = Maps.newHashMap();
    }
}
