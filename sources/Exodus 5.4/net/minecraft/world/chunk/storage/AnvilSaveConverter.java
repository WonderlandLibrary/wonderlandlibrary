/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.commons.lang3.StringUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.world.chunk.storage;

import com.google.common.collect.Lists;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.world.chunk.storage.ChunkLoader;
import net.minecraft.world.chunk.storage.RegionFile;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.world.storage.SaveFormatOld;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnvilSaveConverter
extends SaveFormatOld {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void flushCache() {
        RegionFileCache.clearRegionFileReferences();
    }

    @Override
    public boolean func_154334_a(String string) {
        WorldInfo worldInfo = this.getWorldInfo(string);
        return worldInfo != null && worldInfo.getSaveVersion() == 19132;
    }

    @Override
    public boolean isOldMapFormat(String string) {
        WorldInfo worldInfo = this.getWorldInfo(string);
        return worldInfo != null && worldInfo.getSaveVersion() != this.getSaveVersion();
    }

    @Override
    public ISaveHandler getSaveLoader(String string, boolean bl) {
        return new AnvilSaveHandler(this.savesDirectory, string, bl);
    }

    private void createFile(String string) {
        File file = new File(this.savesDirectory, string);
        if (!file.exists()) {
            logger.warn("Unable to create level.dat_mcr backup");
        } else {
            File file2 = new File(file, "level.dat");
            if (!file2.exists()) {
                logger.warn("Unable to create level.dat_mcr backup");
            } else {
                File file3 = new File(file, "level.dat_mcr");
                if (!file2.renameTo(file3)) {
                    logger.warn("Unable to create level.dat_mcr backup");
                }
            }
        }
    }

    public AnvilSaveConverter(File file) {
        super(file);
    }

    @Override
    public List<SaveFormatComparator> getSaveList() throws AnvilConverterException {
        if (this.savesDirectory != null && this.savesDirectory.exists() && this.savesDirectory.isDirectory()) {
            File[] fileArray;
            ArrayList arrayList = Lists.newArrayList();
            File[] fileArray2 = fileArray = this.savesDirectory.listFiles();
            int n = fileArray.length;
            int n2 = 0;
            while (n2 < n) {
                String string;
                WorldInfo worldInfo;
                File file = fileArray2[n2];
                if (file.isDirectory() && (worldInfo = this.getWorldInfo(string = file.getName())) != null && (worldInfo.getSaveVersion() == 19132 || worldInfo.getSaveVersion() == 19133)) {
                    boolean bl = worldInfo.getSaveVersion() != this.getSaveVersion();
                    String string2 = worldInfo.getWorldName();
                    if (StringUtils.isEmpty((CharSequence)string2)) {
                        string2 = string;
                    }
                    long l = 0L;
                    arrayList.add(new SaveFormatComparator(string, string2, worldInfo.getLastTimePlayed(), l, worldInfo.getGameType(), bl, worldInfo.isHardcoreModeEnabled(), worldInfo.areCommandsAllowed()));
                }
                ++n2;
            }
            return arrayList;
        }
        throw new AnvilConverterException("Unable to read or access folder where game worlds are saved!");
    }

    @Override
    public boolean convertMapFormat(String string, IProgressUpdate iProgressUpdate) {
        iProgressUpdate.setLoadingProgress(0);
        ArrayList arrayList = Lists.newArrayList();
        ArrayList arrayList2 = Lists.newArrayList();
        ArrayList arrayList3 = Lists.newArrayList();
        File file = new File(this.savesDirectory, string);
        File file2 = new File(file, "DIM-1");
        File file3 = new File(file, "DIM1");
        logger.info("Scanning folders...");
        this.addRegionFilesToCollection(file, arrayList);
        if (file2.exists()) {
            this.addRegionFilesToCollection(file2, arrayList2);
        }
        if (file3.exists()) {
            this.addRegionFilesToCollection(file3, arrayList3);
        }
        int n = arrayList.size() + arrayList2.size() + arrayList3.size();
        logger.info("Total conversion count is " + n);
        WorldInfo worldInfo = this.getWorldInfo(string);
        WorldChunkManager worldChunkManager = null;
        worldChunkManager = worldInfo.getTerrainType() == WorldType.FLAT ? new WorldChunkManagerHell(BiomeGenBase.plains, 0.5f) : new WorldChunkManager(worldInfo.getSeed(), worldInfo.getTerrainType(), worldInfo.getGeneratorOptions());
        this.convertFile(new File(file, "region"), arrayList, worldChunkManager, 0, n, iProgressUpdate);
        this.convertFile(new File(file2, "region"), arrayList2, new WorldChunkManagerHell(BiomeGenBase.hell, 0.0f), arrayList.size(), n, iProgressUpdate);
        this.convertFile(new File(file3, "region"), arrayList3, new WorldChunkManagerHell(BiomeGenBase.sky, 0.0f), arrayList.size() + arrayList2.size(), n, iProgressUpdate);
        worldInfo.setSaveVersion(19133);
        if (worldInfo.getTerrainType() == WorldType.DEFAULT_1_1) {
            worldInfo.setTerrainType(WorldType.DEFAULT);
        }
        this.createFile(string);
        ISaveHandler iSaveHandler = this.getSaveLoader(string, false);
        iSaveHandler.saveWorldInfo(worldInfo);
        return true;
    }

    protected int getSaveVersion() {
        return 19133;
    }

    private void convertFile(File file, Iterable<File> iterable, WorldChunkManager worldChunkManager, int n, int n2, IProgressUpdate iProgressUpdate) {
        for (File file2 : iterable) {
            this.convertChunks(file, file2, worldChunkManager, n, n2, iProgressUpdate);
            int n3 = (int)Math.round(100.0 * (double)(++n) / (double)n2);
            iProgressUpdate.setLoadingProgress(n3);
        }
    }

    @Override
    public String getName() {
        return "Anvil";
    }

    private void addRegionFilesToCollection(File file, Collection<File> collection) {
        File file2 = new File(file, "region");
        File[] fileArray = file2.listFiles(new FilenameFilter(){

            @Override
            public boolean accept(File file, String string) {
                return string.endsWith(".mcr");
            }
        });
        if (fileArray != null) {
            Collections.addAll(collection, fileArray);
        }
    }

    private void convertChunks(File file, File file2, WorldChunkManager worldChunkManager, int n, int n2, IProgressUpdate iProgressUpdate) {
        try {
            String string = file2.getName();
            RegionFile regionFile = new RegionFile(file2);
            RegionFile regionFile2 = new RegionFile(new File(file, String.valueOf(string.substring(0, string.length() - 4)) + ".mca"));
            int n3 = 0;
            while (n3 < 32) {
                int n4 = 0;
                while (n4 < 32) {
                    if (regionFile.isChunkSaved(n3, n4) && !regionFile2.isChunkSaved(n3, n4)) {
                        DataInputStream dataInputStream = regionFile.getChunkDataInputStream(n3, n4);
                        if (dataInputStream == null) {
                            logger.warn("Failed to fetch input stream");
                        } else {
                            NBTTagCompound nBTTagCompound = CompressedStreamTools.read(dataInputStream);
                            dataInputStream.close();
                            NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("Level");
                            ChunkLoader.AnvilConverterData anvilConverterData = ChunkLoader.load(nBTTagCompound2);
                            NBTTagCompound nBTTagCompound3 = new NBTTagCompound();
                            NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
                            nBTTagCompound3.setTag("Level", nBTTagCompound4);
                            ChunkLoader.convertToAnvilFormat(anvilConverterData, nBTTagCompound4, worldChunkManager);
                            DataOutputStream dataOutputStream = regionFile2.getChunkDataOutputStream(n3, n4);
                            CompressedStreamTools.write(nBTTagCompound3, dataOutputStream);
                            dataOutputStream.close();
                        }
                    }
                    ++n4;
                }
                n4 = (int)Math.round(100.0 * (double)(n * 1024) / (double)(n2 * 1024));
                int n5 = (int)Math.round(100.0 * (double)((n3 + 1) * 32 + n * 1024) / (double)(n2 * 1024));
                if (n5 > n4) {
                    iProgressUpdate.setLoadingProgress(n5);
                }
                ++n3;
            }
            regionFile.close();
            regionFile2.close();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }
}

