// 
// Decompiled by Procyon v0.5.30
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
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.WorldType;
import java.util.Collection;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import java.util.ArrayList;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.world.storage.SaveFormatComparator;
import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.Lists;
import java.util.List;
import java.io.File;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.storage.SaveFormatOld;

public class AnvilSaveConverter extends SaveFormatOld
{
    private static final Logger logger;
    private static final String __OBFID = "CL_00000582";
    
    public AnvilSaveConverter(final File p_i2144_1_) {
        super(p_i2144_1_);
    }
    
    @Override
    public String func_154333_a() {
        return "Anvil";
    }
    
    @Override
    public List getSaveList() throws AnvilConverterException {
        if (this.savesDirectory != null && this.savesDirectory.exists() && this.savesDirectory.isDirectory()) {
            final ArrayList var1 = Lists.newArrayList();
            final File[] var3;
            final File[] var2 = var3 = this.savesDirectory.listFiles();
            for (int var4 = var2.length, var5 = 0; var5 < var4; ++var5) {
                final File var6 = var3[var5];
                if (var6.isDirectory()) {
                    final String var7 = var6.getName();
                    final WorldInfo var8 = this.getWorldInfo(var7);
                    if (var8 != null && (var8.getSaveVersion() == 19132 || var8.getSaveVersion() == 19133)) {
                        final boolean var9 = var8.getSaveVersion() != this.getSaveVersion();
                        String var10 = var8.getWorldName();
                        if (StringUtils.isEmpty((CharSequence)var10)) {
                            var10 = var7;
                        }
                        final long var11 = 0L;
                        var1.add(new SaveFormatComparator(var7, var10, var8.getLastTimePlayed(), var11, var8.getGameType(), var9, var8.isHardcoreModeEnabled(), var8.areCommandsAllowed()));
                    }
                }
            }
            return var1;
        }
        throw new AnvilConverterException("Unable to read or access folder where game worlds are saved!");
    }
    
    protected int getSaveVersion() {
        return 19133;
    }
    
    @Override
    public void flushCache() {
        RegionFileCache.clearRegionFileReferences();
    }
    
    @Override
    public ISaveHandler getSaveLoader(final String p_75804_1_, final boolean p_75804_2_) {
        return new AnvilSaveHandler(this.savesDirectory, p_75804_1_, p_75804_2_);
    }
    
    @Override
    public boolean func_154334_a(final String p_154334_1_) {
        final WorldInfo var2 = this.getWorldInfo(p_154334_1_);
        return var2 != null && var2.getSaveVersion() == 19132;
    }
    
    @Override
    public boolean isOldMapFormat(final String p_75801_1_) {
        final WorldInfo var2 = this.getWorldInfo(p_75801_1_);
        return var2 != null && var2.getSaveVersion() != this.getSaveVersion();
    }
    
    @Override
    public boolean convertMapFormat(final String p_75805_1_, final IProgressUpdate p_75805_2_) {
        p_75805_2_.setLoadingProgress(0);
        final ArrayList var3 = Lists.newArrayList();
        final ArrayList var4 = Lists.newArrayList();
        final ArrayList var5 = Lists.newArrayList();
        final File var6 = new File(this.savesDirectory, p_75805_1_);
        final File var7 = new File(var6, "DIM-1");
        final File var8 = new File(var6, "DIM1");
        AnvilSaveConverter.logger.info("Scanning folders...");
        this.addRegionFilesToCollection(var6, var3);
        if (var7.exists()) {
            this.addRegionFilesToCollection(var7, var4);
        }
        if (var8.exists()) {
            this.addRegionFilesToCollection(var8, var5);
        }
        final int var9 = var3.size() + var4.size() + var5.size();
        AnvilSaveConverter.logger.info("Total conversion count is " + var9);
        final WorldInfo var10 = this.getWorldInfo(p_75805_1_);
        Object var11 = null;
        if (var10.getTerrainType() == WorldType.FLAT) {
            var11 = new WorldChunkManagerHell(BiomeGenBase.plains, 0.5f);
        }
        else {
            var11 = new WorldChunkManager(var10.getSeed(), var10.getTerrainType(), var10.getGeneratorOptions());
        }
        this.convertFile(new File(var6, "region"), var3, (WorldChunkManager)var11, 0, var9, p_75805_2_);
        this.convertFile(new File(var7, "region"), var4, new WorldChunkManagerHell(BiomeGenBase.hell, 0.0f), var3.size(), var9, p_75805_2_);
        this.convertFile(new File(var8, "region"), var5, new WorldChunkManagerHell(BiomeGenBase.sky, 0.0f), var3.size() + var4.size(), var9, p_75805_2_);
        var10.setSaveVersion(19133);
        if (var10.getTerrainType() == WorldType.DEFAULT_1_1) {
            var10.setTerrainType(WorldType.DEFAULT);
        }
        this.createFile(p_75805_1_);
        final ISaveHandler var12 = this.getSaveLoader(p_75805_1_, false);
        var12.saveWorldInfo(var10);
        return true;
    }
    
    private void createFile(final String p_75809_1_) {
        final File var2 = new File(this.savesDirectory, p_75809_1_);
        if (!var2.exists()) {
            AnvilSaveConverter.logger.warn("Unable to create level.dat_mcr backup");
        }
        else {
            final File var3 = new File(var2, "level.dat");
            if (!var3.exists()) {
                AnvilSaveConverter.logger.warn("Unable to create level.dat_mcr backup");
            }
            else {
                final File var4 = new File(var2, "level.dat_mcr");
                if (!var3.renameTo(var4)) {
                    AnvilSaveConverter.logger.warn("Unable to create level.dat_mcr backup");
                }
            }
        }
    }
    
    private void convertFile(final File p_75813_1_, final Iterable p_75813_2_, final WorldChunkManager p_75813_3_, int p_75813_4_, final int p_75813_5_, final IProgressUpdate p_75813_6_) {
        for (final File var8 : p_75813_2_) {
            this.convertChunks(p_75813_1_, var8, p_75813_3_, p_75813_4_, p_75813_5_, p_75813_6_);
            ++p_75813_4_;
            final int var9 = (int)Math.round(100.0 * p_75813_4_ / p_75813_5_);
            p_75813_6_.setLoadingProgress(var9);
        }
    }
    
    private void convertChunks(final File p_75811_1_, final File p_75811_2_, final WorldChunkManager p_75811_3_, final int p_75811_4_, final int p_75811_5_, final IProgressUpdate p_75811_6_) {
        try {
            final String var7 = p_75811_2_.getName();
            final RegionFile var8 = new RegionFile(p_75811_2_);
            final RegionFile var9 = new RegionFile(new File(p_75811_1_, var7.substring(0, var7.length() - ".mcr".length()) + ".mca"));
            for (int var10 = 0; var10 < 32; ++var10) {
                for (int var11 = 0; var11 < 32; ++var11) {
                    if (var8.isChunkSaved(var10, var11) && !var9.isChunkSaved(var10, var11)) {
                        final DataInputStream var12 = var8.getChunkDataInputStream(var10, var11);
                        if (var12 == null) {
                            AnvilSaveConverter.logger.warn("Failed to fetch input stream");
                        }
                        else {
                            final NBTTagCompound var13 = CompressedStreamTools.read(var12);
                            var12.close();
                            final NBTTagCompound var14 = var13.getCompoundTag("Level");
                            final ChunkLoader.AnvilConverterData var15 = ChunkLoader.load(var14);
                            final NBTTagCompound var16 = new NBTTagCompound();
                            final NBTTagCompound var17 = new NBTTagCompound();
                            var16.setTag("Level", var17);
                            ChunkLoader.convertToAnvilFormat(var15, var17, p_75811_3_);
                            final DataOutputStream var18 = var9.getChunkDataOutputStream(var10, var11);
                            CompressedStreamTools.write(var16, var18);
                            var18.close();
                        }
                    }
                }
                int var11 = (int)Math.round(100.0 * (p_75811_4_ * 1024) / (p_75811_5_ * 1024));
                final int var19 = (int)Math.round(100.0 * ((var10 + 1) * 32 + p_75811_4_ * 1024) / (p_75811_5_ * 1024));
                if (var19 > var11) {
                    p_75811_6_.setLoadingProgress(var19);
                }
            }
            var8.close();
            var9.close();
        }
        catch (IOException var20) {
            var20.printStackTrace();
        }
    }
    
    private void addRegionFilesToCollection(final File p_75810_1_, final Collection p_75810_2_) {
        final File var3 = new File(p_75810_1_, "region");
        final File[] var4 = var3.listFiles(new FilenameFilter() {
            private static final String __OBFID = "CL_00000583";
            
            @Override
            public boolean accept(final File p_accept_1_, final String p_accept_2_) {
                return p_accept_2_.endsWith(".mcr");
            }
        });
        if (var4 != null) {
            Collections.addAll(p_75810_2_, var4);
        }
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
