/*
 * Decompiled with CFR 0.150.
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
    private static final Logger logger = LogManager.getLogger();
    protected final File savesDirectory;
    private static final String __OBFID = "CL_00000586";

    public SaveFormatOld(File p_i2147_1_) {
        if (!p_i2147_1_.exists()) {
            p_i2147_1_.mkdirs();
        }
        this.savesDirectory = p_i2147_1_;
    }

    @Override
    public String func_154333_a() {
        return "Old Format";
    }

    @Override
    public List getSaveList() throws AnvilConverterException {
        ArrayList var1 = Lists.newArrayList();
        for (int var2 = 0; var2 < 5; ++var2) {
            String var3 = "World" + (var2 + 1);
            WorldInfo var4 = this.getWorldInfo(var3);
            if (var4 == null) continue;
            var1.add(new SaveFormatComparator(var3, "", var4.getLastTimePlayed(), var4.getSizeOnDisk(), var4.getGameType(), false, var4.isHardcoreModeEnabled(), var4.areCommandsAllowed()));
        }
        return var1;
    }

    @Override
    public void flushCache() {
    }

    @Override
    public WorldInfo getWorldInfo(String p_75803_1_) {
        File var2 = new File(this.savesDirectory, p_75803_1_);
        if (!var2.exists()) {
            return null;
        }
        File var3 = new File(var2, "level.dat");
        if (var3.exists()) {
            try {
                NBTTagCompound var4 = CompressedStreamTools.readCompressed(new FileInputStream(var3));
                NBTTagCompound var5 = var4.getCompoundTag("Data");
                return new WorldInfo(var5);
            }
            catch (Exception var7) {
                logger.error("Exception reading " + var3, (Throwable)var7);
            }
        }
        if ((var3 = new File(var2, "level.dat_old")).exists()) {
            try {
                NBTTagCompound var4 = CompressedStreamTools.readCompressed(new FileInputStream(var3));
                NBTTagCompound var5 = var4.getCompoundTag("Data");
                return new WorldInfo(var5);
            }
            catch (Exception var6) {
                logger.error("Exception reading " + var3, (Throwable)var6);
            }
        }
        return null;
    }

    @Override
    public void renameWorld(String p_75806_1_, String p_75806_2_) {
        File var4;
        File var3 = new File(this.savesDirectory, p_75806_1_);
        if (var3.exists() && (var4 = new File(var3, "level.dat")).exists()) {
            try {
                NBTTagCompound var5 = CompressedStreamTools.readCompressed(new FileInputStream(var4));
                NBTTagCompound var6 = var5.getCompoundTag("Data");
                var6.setString("LevelName", p_75806_2_);
                CompressedStreamTools.writeCompressed(var5, new FileOutputStream(var4));
            }
            catch (Exception var7) {
                var7.printStackTrace();
            }
        }
    }

    @Override
    public boolean func_154335_d(String p_154335_1_) {
        File var2 = new File(this.savesDirectory, p_154335_1_);
        if (var2.exists()) {
            return false;
        }
        try {
            var2.mkdir();
            var2.delete();
            return true;
        }
        catch (Throwable var4) {
            logger.warn("Couldn't make new level", var4);
            return false;
        }
    }

    @Override
    public boolean deleteWorldDirectory(String p_75802_1_) {
        File var2 = new File(this.savesDirectory, p_75802_1_);
        if (!var2.exists()) {
            return true;
        }
        logger.info("Deleting level " + p_75802_1_);
        for (int var3 = 1; var3 <= 5; ++var3) {
            logger.info("Attempt " + var3 + "...");
            if (SaveFormatOld.deleteFiles(var2.listFiles())) break;
            logger.warn("Unsuccessful in deleting contents.");
            if (var3 >= 5) continue;
            try {
                Thread.sleep(500L);
                continue;
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
        return var2.delete();
    }

    protected static boolean deleteFiles(File[] p_75807_0_) {
        for (int var1 = 0; var1 < p_75807_0_.length; ++var1) {
            File var2 = p_75807_0_[var1];
            logger.debug("Deleting " + var2);
            if (var2.isDirectory() && !SaveFormatOld.deleteFiles(var2.listFiles())) {
                logger.warn("Couldn't delete directory " + var2);
                return false;
            }
            if (var2.delete()) continue;
            logger.warn("Couldn't delete file " + var2);
            return false;
        }
        return true;
    }

    @Override
    public ISaveHandler getSaveLoader(String p_75804_1_, boolean p_75804_2_) {
        return new SaveHandler(this.savesDirectory, p_75804_1_, p_75804_2_);
    }

    @Override
    public boolean func_154334_a(String p_154334_1_) {
        return false;
    }

    @Override
    public boolean isOldMapFormat(String p_75801_1_) {
        return false;
    }

    @Override
    public boolean convertMapFormat(String p_75805_1_, IProgressUpdate p_75805_2_) {
        return false;
    }

    @Override
    public boolean canLoadWorld(String p_90033_1_) {
        File var2 = new File(this.savesDirectory, p_90033_1_);
        return var2.isDirectory();
    }
}

