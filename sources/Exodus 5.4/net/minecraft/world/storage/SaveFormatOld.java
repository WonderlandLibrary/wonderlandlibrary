/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveFormatOld
implements ISaveFormat {
    protected final File savesDirectory;
    private static final Logger logger = LogManager.getLogger();

    @Override
    public boolean func_154334_a(String string) {
        return false;
    }

    @Override
    public WorldInfo getWorldInfo(String string) {
        File file = new File(this.savesDirectory, string);
        if (!file.exists()) {
            return null;
        }
        File file2 = new File(file, "level.dat");
        if (file2.exists()) {
            try {
                NBTTagCompound nBTTagCompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("Data");
                return new WorldInfo(nBTTagCompound2);
            }
            catch (Exception exception) {
                logger.error("Exception reading " + file2, (Throwable)exception);
            }
        }
        if ((file2 = new File(file, "level.dat_old")).exists()) {
            try {
                NBTTagCompound nBTTagCompound = CompressedStreamTools.readCompressed(new FileInputStream(file2));
                NBTTagCompound nBTTagCompound3 = nBTTagCompound.getCompoundTag("Data");
                return new WorldInfo(nBTTagCompound3);
            }
            catch (Exception exception) {
                logger.error("Exception reading " + file2, (Throwable)exception);
            }
        }
        return null;
    }

    @Override
    public boolean canLoadWorld(String string) {
        File file = new File(this.savesDirectory, string);
        return file.isDirectory();
    }

    @Override
    public boolean func_154335_d(String string) {
        File file = new File(this.savesDirectory, string);
        if (file.exists()) {
            return false;
        }
        try {
            file.mkdir();
            file.delete();
            return true;
        }
        catch (Throwable throwable) {
            logger.warn("Couldn't make new level", throwable);
            return false;
        }
    }

    @Override
    public void renameWorld(String string, String string2) {
        File file;
        File file2 = new File(this.savesDirectory, string);
        if (file2.exists() && (file = new File(file2, "level.dat")).exists()) {
            try {
                NBTTagCompound nBTTagCompound = CompressedStreamTools.readCompressed(new FileInputStream(file));
                NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("Data");
                nBTTagCompound2.setString("LevelName", string2);
                CompressedStreamTools.writeCompressed(nBTTagCompound, new FileOutputStream(file));
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public boolean deleteWorldDirectory(String string) {
        File file = new File(this.savesDirectory, string);
        if (!file.exists()) {
            return true;
        }
        logger.info("Deleting level " + string);
        int n = 1;
        while (n <= 5) {
            logger.info("Attempt " + n + "...");
            if (SaveFormatOld.deleteFiles(file.listFiles())) break;
            logger.warn("Unsuccessful in deleting contents.");
            if (n < 5) {
                try {
                    Thread.sleep(500L);
                }
                catch (InterruptedException interruptedException) {
                    // empty catch block
                }
            }
            ++n;
        }
        return file.delete();
    }

    @Override
    public String getName() {
        return "Old Format";
    }

    @Override
    public List<SaveFormatComparator> getSaveList() throws AnvilConverterException {
        ArrayList arrayList = Lists.newArrayList();
        int n = 0;
        while (n < 5) {
            String string = "World" + (n + 1);
            WorldInfo worldInfo = this.getWorldInfo(string);
            if (worldInfo != null) {
                arrayList.add(new SaveFormatComparator(string, "", worldInfo.getLastTimePlayed(), worldInfo.getSizeOnDisk(), worldInfo.getGameType(), false, worldInfo.isHardcoreModeEnabled(), worldInfo.areCommandsAllowed()));
            }
            ++n;
        }
        return arrayList;
    }

    protected static boolean deleteFiles(File[] fileArray) {
        int n = 0;
        while (n < fileArray.length) {
            File file = fileArray[n];
            logger.debug("Deleting " + file);
            if (file.isDirectory() && !SaveFormatOld.deleteFiles(file.listFiles())) {
                logger.warn("Couldn't delete directory " + file);
                return false;
            }
            if (!file.delete()) {
                logger.warn("Couldn't delete file " + file);
                return false;
            }
            ++n;
        }
        return true;
    }

    @Override
    public ISaveHandler getSaveLoader(String string, boolean bl) {
        return new SaveHandler(this.savesDirectory, string, bl);
    }

    @Override
    public void flushCache() {
    }

    @Override
    public boolean convertMapFormat(String string, IProgressUpdate iProgressUpdate) {
        return false;
    }

    @Override
    public boolean isOldMapFormat(String string) {
        return false;
    }

    public SaveFormatOld(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        this.savesDirectory = file;
    }
}

