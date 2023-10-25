/*
 * Decompiled with CFR 0.152.
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
    private static final Map<File, RegionFile> regionsByFilename = Maps.newHashMap();

    public static synchronized RegionFile createOrLoadRegionFile(File file, int n, int n2) {
        File file2 = new File(file, "region");
        File file3 = new File(file2, "r." + (n >> 5) + "." + (n2 >> 5) + ".mca");
        RegionFile regionFile = regionsByFilename.get(file3);
        if (regionFile != null) {
            return regionFile;
        }
        if (!file2.exists()) {
            file2.mkdirs();
        }
        if (regionsByFilename.size() >= 256) {
            RegionFileCache.clearRegionFileReferences();
        }
        RegionFile regionFile2 = new RegionFile(file3);
        regionsByFilename.put(file3, regionFile2);
        return regionFile2;
    }

    public static DataOutputStream getChunkOutputStream(File file, int n, int n2) {
        RegionFile regionFile = RegionFileCache.createOrLoadRegionFile(file, n, n2);
        return regionFile.getChunkDataOutputStream(n & 0x1F, n2 & 0x1F);
    }

    public static DataInputStream getChunkInputStream(File file, int n, int n2) {
        RegionFile regionFile = RegionFileCache.createOrLoadRegionFile(file, n, n2);
        return regionFile.getChunkDataInputStream(n & 0x1F, n2 & 0x1F);
    }

    public static synchronized void clearRegionFileReferences() {
        for (RegionFile regionFile : regionsByFilename.values()) {
            try {
                if (regionFile == null) continue;
                regionFile.close();
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        regionsByFilename.clear();
    }
}

