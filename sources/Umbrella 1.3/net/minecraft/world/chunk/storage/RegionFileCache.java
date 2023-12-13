/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.world.chunk.storage;

import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import net.minecraft.world.chunk.storage.RegionFile;

public class RegionFileCache {
    private static final Map regionsByFilename = Maps.newHashMap();
    private static final String __OBFID = "CL_00000383";

    public static synchronized RegionFile createOrLoadRegionFile(File worldDir, int chunkX, int chunkZ) {
        File var3 = new File(worldDir, "region");
        File var4 = new File(var3, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");
        RegionFile var5 = (RegionFile)regionsByFilename.get(var4);
        if (var5 != null) {
            return var5;
        }
        if (!var3.exists()) {
            var3.mkdirs();
        }
        if (regionsByFilename.size() >= 256) {
            RegionFileCache.clearRegionFileReferences();
        }
        RegionFile var6 = new RegionFile(var4);
        regionsByFilename.put(var4, var6);
        return var6;
    }

    public static synchronized void clearRegionFileReferences() {
        for (RegionFile var1 : regionsByFilename.values()) {
            try {
                if (var1 == null) continue;
                var1.close();
            }
            catch (IOException var3) {
                var3.printStackTrace();
            }
        }
        regionsByFilename.clear();
    }

    public static DataInputStream getChunkInputStream(File worldDir, int chunkX, int chunkZ) {
        RegionFile var3 = RegionFileCache.createOrLoadRegionFile(worldDir, chunkX, chunkZ);
        return var3.getChunkDataInputStream(chunkX & 0x1F, chunkZ & 0x1F);
    }

    public static DataOutputStream getChunkOutputStream(File worldDir, int chunkX, int chunkZ) {
        RegionFile var3 = RegionFileCache.createOrLoadRegionFile(worldDir, chunkX, chunkZ);
        return var3.getChunkDataOutputStream(chunkX & 0x1F, chunkZ & 0x1F);
    }
}

