// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.IProgressUpdate;
import java.io.OutputStream;
import java.io.FileOutputStream;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.datafix.FixTypes;
import java.io.InputStream;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.FileInputStream;
import javax.annotation.Nullable;
import net.minecraft.client.AnvilConverterException;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.datafix.DataFixer;
import java.io.File;
import org.apache.logging.log4j.Logger;

public class SaveFormatOld implements ISaveFormat
{
    private static final Logger LOGGER;
    protected final File savesDirectory;
    protected final DataFixer dataFixer;
    
    public SaveFormatOld(final File savesDirectoryIn, final DataFixer dataFixerIn) {
        this.dataFixer = dataFixerIn;
        if (!savesDirectoryIn.exists()) {
            savesDirectoryIn.mkdirs();
        }
        this.savesDirectory = savesDirectoryIn;
    }
    
    @Override
    public String getName() {
        return "Old Format";
    }
    
    @Override
    public List<WorldSummary> getSaveList() throws AnvilConverterException {
        final List<WorldSummary> list = (List<WorldSummary>)Lists.newArrayList();
        for (int i = 0; i < 5; ++i) {
            final String s = "World" + (i + 1);
            final WorldInfo worldinfo = this.getWorldInfo(s);
            if (worldinfo != null) {
                list.add(new WorldSummary(worldinfo, s, "", worldinfo.getSizeOnDisk(), false));
            }
        }
        return list;
    }
    
    @Override
    public void flushCache() {
    }
    
    @Nullable
    @Override
    public WorldInfo getWorldInfo(final String saveName) {
        final File file1 = new File(this.savesDirectory, saveName);
        if (!file1.exists()) {
            return null;
        }
        File file2 = new File(file1, "level.dat");
        if (file2.exists()) {
            final WorldInfo worldinfo = getWorldData(file2, this.dataFixer);
            if (worldinfo != null) {
                return worldinfo;
            }
        }
        file2 = new File(file1, "level.dat_old");
        return file2.exists() ? getWorldData(file2, this.dataFixer) : null;
    }
    
    @Nullable
    public static WorldInfo getWorldData(final File p_186353_0_, final DataFixer dataFixerIn) {
        try {
            final NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(p_186353_0_));
            final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Data");
            return new WorldInfo(dataFixerIn.process(FixTypes.LEVEL, nbttagcompound2));
        }
        catch (Exception exception) {
            SaveFormatOld.LOGGER.error("Exception reading {}", (Object)p_186353_0_, (Object)exception);
            return null;
        }
    }
    
    @Override
    public void renameWorld(final String dirName, final String newName) {
        final File file1 = new File(this.savesDirectory, dirName);
        if (file1.exists()) {
            final File file2 = new File(file1, "level.dat");
            if (file2.exists()) {
                try {
                    final NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                    final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Data");
                    nbttagcompound2.setString("LevelName", newName);
                    CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file2));
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public boolean isNewLevelIdAcceptable(final String saveName) {
        final File file1 = new File(this.savesDirectory, saveName);
        if (file1.exists()) {
            return false;
        }
        try {
            file1.mkdir();
            file1.delete();
            return true;
        }
        catch (Throwable throwable) {
            SaveFormatOld.LOGGER.warn("Couldn't make new level", throwable);
            return false;
        }
    }
    
    @Override
    public boolean deleteWorldDirectory(final String saveName) {
        final File file1 = new File(this.savesDirectory, saveName);
        if (!file1.exists()) {
            return true;
        }
        SaveFormatOld.LOGGER.info("Deleting level {}", (Object)saveName);
        for (int i = 1; i <= 5; ++i) {
            SaveFormatOld.LOGGER.info("Attempt {}...", (Object)i);
            if (deleteFiles(file1.listFiles())) {
                break;
            }
            SaveFormatOld.LOGGER.warn("Unsuccessful in deleting contents.");
            if (i < 5) {
                try {
                    Thread.sleep(500L);
                }
                catch (InterruptedException ex) {}
            }
        }
        return file1.delete();
    }
    
    protected static boolean deleteFiles(final File[] files) {
        for (final File file1 : files) {
            SaveFormatOld.LOGGER.debug("Deleting {}", (Object)file1);
            if (file1.isDirectory() && !deleteFiles(file1.listFiles())) {
                SaveFormatOld.LOGGER.warn("Couldn't delete directory {}", (Object)file1);
                return false;
            }
            if (!file1.delete()) {
                SaveFormatOld.LOGGER.warn("Couldn't delete file {}", (Object)file1);
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ISaveHandler getSaveLoader(final String saveName, final boolean storePlayerdata) {
        return new SaveHandler(this.savesDirectory, saveName, storePlayerdata, this.dataFixer);
    }
    
    @Override
    public boolean isConvertible(final String saveName) {
        return false;
    }
    
    @Override
    public boolean isOldMapFormat(final String saveName) {
        return false;
    }
    
    @Override
    public boolean convertMapFormat(final String filename, final IProgressUpdate progressCallback) {
        return false;
    }
    
    @Override
    public boolean canLoadWorld(final String saveName) {
        final File file1 = new File(this.savesDirectory, saveName);
        return file1.isDirectory();
    }
    
    @Override
    public File getFile(final String p_186352_1_, final String p_186352_2_) {
        return new File(new File(this.savesDirectory, p_186352_1_), p_186352_2_);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
