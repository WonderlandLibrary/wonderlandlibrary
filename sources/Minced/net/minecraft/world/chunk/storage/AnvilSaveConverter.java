// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk.storage;

import org.apache.logging.log4j.LogManager;
import java.util.Collections;
import java.io.FilenameFilter;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.DataOutput;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.CompressedStreamTools;
import java.util.Iterator;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import java.util.Collection;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.text.translation.I18n;
import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.Lists;
import net.minecraft.world.storage.WorldSummary;
import java.util.List;
import net.minecraft.util.datafix.DataFixer;
import java.io.File;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.storage.SaveFormatOld;

public class AnvilSaveConverter extends SaveFormatOld
{
    private static final Logger LOGGER;
    
    public AnvilSaveConverter(final File dir, final DataFixer dataFixerIn) {
        super(dir, dataFixerIn);
    }
    
    @Override
    public String getName() {
        return "Anvil";
    }
    
    @Override
    public List<WorldSummary> getSaveList() throws AnvilConverterException {
        if (this.savesDirectory != null && this.savesDirectory.exists() && this.savesDirectory.isDirectory()) {
            final List<WorldSummary> list = (List<WorldSummary>)Lists.newArrayList();
            final File[] listFiles;
            final File[] afile = listFiles = this.savesDirectory.listFiles();
            for (final File file1 : listFiles) {
                if (file1.isDirectory()) {
                    final String s = file1.getName();
                    final WorldInfo worldinfo = this.getWorldInfo(s);
                    if (worldinfo != null && (worldinfo.getSaveVersion() == 19132 || worldinfo.getSaveVersion() == 19133)) {
                        final boolean flag = worldinfo.getSaveVersion() != this.getSaveVersion();
                        String s2 = worldinfo.getWorldName();
                        if (StringUtils.isEmpty((CharSequence)s2)) {
                            s2 = s;
                        }
                        final long i = 0L;
                        list.add(new WorldSummary(worldinfo, s, s2, 0L, flag));
                    }
                }
            }
            return list;
        }
        throw new AnvilConverterException(I18n.translateToLocal("selectWorld.load_folder_access"));
    }
    
    protected int getSaveVersion() {
        return 19133;
    }
    
    @Override
    public void flushCache() {
        RegionFileCache.clearRegionFileReferences();
    }
    
    @Override
    public ISaveHandler getSaveLoader(final String saveName, final boolean storePlayerdata) {
        return new AnvilSaveHandler(this.savesDirectory, saveName, storePlayerdata, this.dataFixer);
    }
    
    @Override
    public boolean isConvertible(final String saveName) {
        final WorldInfo worldinfo = this.getWorldInfo(saveName);
        return worldinfo != null && worldinfo.getSaveVersion() == 19132;
    }
    
    @Override
    public boolean isOldMapFormat(final String saveName) {
        final WorldInfo worldinfo = this.getWorldInfo(saveName);
        return worldinfo != null && worldinfo.getSaveVersion() != this.getSaveVersion();
    }
    
    @Override
    public boolean convertMapFormat(final String filename, final IProgressUpdate progressCallback) {
        progressCallback.setLoadingProgress(0);
        final List<File> list = (List<File>)Lists.newArrayList();
        final List<File> list2 = (List<File>)Lists.newArrayList();
        final List<File> list3 = (List<File>)Lists.newArrayList();
        final File file1 = new File(this.savesDirectory, filename);
        final File file2 = new File(file1, "DIM-1");
        final File file3 = new File(file1, "DIM1");
        AnvilSaveConverter.LOGGER.info("Scanning folders...");
        this.addRegionFilesToCollection(file1, list);
        if (file2.exists()) {
            this.addRegionFilesToCollection(file2, list2);
        }
        if (file3.exists()) {
            this.addRegionFilesToCollection(file3, list3);
        }
        final int i = list.size() + list2.size() + list3.size();
        AnvilSaveConverter.LOGGER.info("Total conversion count is {}", (Object)i);
        final WorldInfo worldinfo = this.getWorldInfo(filename);
        BiomeProvider biomeprovider;
        if (worldinfo != null && worldinfo.getTerrainType() == WorldType.FLAT) {
            biomeprovider = new BiomeProviderSingle(Biomes.PLAINS);
        }
        else {
            biomeprovider = new BiomeProvider(worldinfo);
        }
        this.convertFile(new File(file1, "region"), list, biomeprovider, 0, i, progressCallback);
        this.convertFile(new File(file2, "region"), list2, new BiomeProviderSingle(Biomes.HELL), list.size(), i, progressCallback);
        this.convertFile(new File(file3, "region"), list3, new BiomeProviderSingle(Biomes.SKY), list.size() + list2.size(), i, progressCallback);
        worldinfo.setSaveVersion(19133);
        if (worldinfo.getTerrainType() == WorldType.DEFAULT_1_1) {
            worldinfo.setTerrainType(WorldType.DEFAULT);
        }
        this.createFile(filename);
        final ISaveHandler isavehandler = this.getSaveLoader(filename, false);
        isavehandler.saveWorldInfo(worldinfo);
        return true;
    }
    
    private void createFile(final String filename) {
        final File file1 = new File(this.savesDirectory, filename);
        if (!file1.exists()) {
            AnvilSaveConverter.LOGGER.warn("Unable to create level.dat_mcr backup");
        }
        else {
            final File file2 = new File(file1, "level.dat");
            if (!file2.exists()) {
                AnvilSaveConverter.LOGGER.warn("Unable to create level.dat_mcr backup");
            }
            else {
                final File file3 = new File(file1, "level.dat_mcr");
                if (!file2.renameTo(file3)) {
                    AnvilSaveConverter.LOGGER.warn("Unable to create level.dat_mcr backup");
                }
            }
        }
    }
    
    private void convertFile(final File baseFolder, final Iterable<File> regionFiles, final BiomeProvider p_75813_3_, int p_75813_4_, final int p_75813_5_, final IProgressUpdate progress) {
        for (final File file1 : regionFiles) {
            this.convertChunks(baseFolder, file1, p_75813_3_, p_75813_4_, p_75813_5_, progress);
            ++p_75813_4_;
            final int i = (int)Math.round(100.0 * p_75813_4_ / p_75813_5_);
            progress.setLoadingProgress(i);
        }
    }
    
    private void convertChunks(final File baseFolder, final File p_75811_2_, final BiomeProvider biomeSource, final int p_75811_4_, final int p_75811_5_, final IProgressUpdate progressCallback) {
        try {
            final String s = p_75811_2_.getName();
            final RegionFile regionfile = new RegionFile(p_75811_2_);
            final RegionFile regionfile2 = new RegionFile(new File(baseFolder, s.substring(0, s.length() - ".mcr".length()) + ".mca"));
            for (int i = 0; i < 32; ++i) {
                for (int j = 0; j < 32; ++j) {
                    if (regionfile.isChunkSaved(i, j) && !regionfile2.isChunkSaved(i, j)) {
                        final DataInputStream datainputstream = regionfile.getChunkDataInputStream(i, j);
                        if (datainputstream == null) {
                            AnvilSaveConverter.LOGGER.warn("Failed to fetch input stream");
                        }
                        else {
                            final NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);
                            datainputstream.close();
                            final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Level");
                            final ChunkLoader.AnvilConverterData chunkloader$anvilconverterdata = ChunkLoader.load(nbttagcompound2);
                            final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
                            final NBTTagCompound nbttagcompound4 = new NBTTagCompound();
                            nbttagcompound3.setTag("Level", nbttagcompound4);
                            ChunkLoader.convertToAnvilFormat(chunkloader$anvilconverterdata, nbttagcompound4, biomeSource);
                            final DataOutputStream dataoutputstream = regionfile2.getChunkDataOutputStream(i, j);
                            CompressedStreamTools.write(nbttagcompound3, dataoutputstream);
                            dataoutputstream.close();
                        }
                    }
                }
                final int k = (int)Math.round(100.0 * (p_75811_4_ * 1024) / (p_75811_5_ * 1024));
                final int l = (int)Math.round(100.0 * ((i + 1) * 32 + p_75811_4_ * 1024) / (p_75811_5_ * 1024));
                if (l > k) {
                    progressCallback.setLoadingProgress(l);
                }
            }
            regionfile.close();
            regionfile2.close();
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    private void addRegionFilesToCollection(final File worldDir, final Collection<File> collection) {
        final File file1 = new File(worldDir, "region");
        final File[] afile = file1.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File p_accept_1_, final String p_accept_2_) {
                return p_accept_2_.endsWith(".mcr");
            }
        });
        if (afile != null) {
            Collections.addAll(collection, afile);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
