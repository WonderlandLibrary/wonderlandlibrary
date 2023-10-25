/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveFormatOld
implements ISaveFormat {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final File savesDirectory;
    protected final DataFixer dataFixer;

    public SaveFormatOld(File savesDirectoryIn, DataFixer dataFixerIn) {
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
        ArrayList<WorldSummary> list = Lists.newArrayList();
        for (int i = 0; i < 5; ++i) {
            String s = "World" + (i + 1);
            WorldInfo worldinfo = this.getWorldInfo(s);
            if (worldinfo == null) continue;
            list.add(new WorldSummary(worldinfo, s, "", worldinfo.getSizeOnDisk(), false));
        }
        return list;
    }

    @Override
    public void flushCache() {
    }

    @Override
    @Nullable
    public WorldInfo getWorldInfo(String saveName) {
        WorldInfo worldinfo;
        File file1 = new File(this.savesDirectory, saveName);
        if (!file1.exists()) {
            return null;
        }
        File file2 = new File(file1, "level.dat");
        if (file2.exists() && (worldinfo = SaveFormatOld.getWorldData(file2, this.dataFixer)) != null) {
            return worldinfo;
        }
        file2 = new File(file1, "level.dat_old");
        return file2.exists() ? SaveFormatOld.getWorldData(file2, this.dataFixer) : null;
    }

    @Nullable
    public static WorldInfo getWorldData(File p_186353_0_, DataFixer dataFixerIn) {
        try {
            NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(p_186353_0_));
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
            return new WorldInfo(dataFixerIn.process(FixTypes.LEVEL, nbttagcompound1));
        }
        catch (Exception exception) {
            LOGGER.error("Exception reading {}", p_186353_0_, exception);
            return null;
        }
    }

    @Override
    public void renameWorld(String dirName, String newName) {
        File file2;
        File file1 = new File(this.savesDirectory, dirName);
        if (file1.exists() && (file2 = new File(file1, "level.dat")).exists()) {
            try {
                NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");
                nbttagcompound1.setString("LevelName", newName);
                CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file2));
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public boolean isNewLevelIdAcceptable(String saveName) {
        File file1 = new File(this.savesDirectory, saveName);
        if (file1.exists()) {
            return false;
        }
        try {
            file1.mkdir();
            file1.delete();
            return true;
        }
        catch (Throwable throwable) {
            LOGGER.warn("Couldn't make new level", throwable);
            return false;
        }
    }

    @Override
    public boolean deleteWorldDirectory(String saveName) {
        File file1 = new File(this.savesDirectory, saveName);
        if (!file1.exists()) {
            return true;
        }
        LOGGER.info("Deleting level {}", saveName);
        for (int i = 1; i <= 5; ++i) {
            LOGGER.info("Attempt {}...", i);
            if (SaveFormatOld.deleteFiles(file1.listFiles())) break;
            LOGGER.warn("Unsuccessful in deleting contents.");
            if (i >= 5) continue;
            try {
                Thread.sleep(500L);
                continue;
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
        return file1.delete();
    }

    protected static boolean deleteFiles(File[] files) {
        for (File file1 : files) {
            LOGGER.debug("Deleting {}", file1);
            if (file1.isDirectory() && !SaveFormatOld.deleteFiles(file1.listFiles())) {
                LOGGER.warn("Couldn't delete directory {}", file1);
                return false;
            }
            if (file1.delete()) continue;
            LOGGER.warn("Couldn't delete file {}", file1);
            return false;
        }
        return true;
    }

    @Override
    public ISaveHandler getSaveLoader(String saveName, boolean storePlayerdata) {
        return new SaveHandler(this.savesDirectory, saveName, storePlayerdata, this.dataFixer);
    }

    @Override
    public boolean isConvertible(String saveName) {
        return false;
    }

    @Override
    public boolean isOldMapFormat(String saveName) {
        return false;
    }

    @Override
    public boolean convertMapFormat(String filename, IProgressUpdate progressCallback) {
        return false;
    }

    @Override
    public boolean canLoadWorld(String saveName) {
        File file1 = new File(this.savesDirectory, saveName);
        return file1.isDirectory();
    }

    @Override
    public File getFile(String p_186352_1_, String p_186352_2_) {
        return new File(new File(this.savesDirectory, p_186352_1_), p_186352_2_);
    }
}

