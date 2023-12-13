// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.storage;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.IProgressUpdate;
import java.io.OutputStream;
import java.io.FileOutputStream;
import net.minecraft.nbt.NBTTagCompound;
import java.io.InputStream;
import net.minecraft.nbt.CompressedStreamTools;
import java.io.FileInputStream;
import net.minecraft.client.AnvilConverterException;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.util.List;
import java.io.File;
import org.apache.logging.log4j.Logger;

public class SaveFormatOld implements ISaveFormat
{
    private static final Logger logger;
    protected final File savesDirectory;
    private static final String __OBFID = "CL_00000586";
    
    public SaveFormatOld(final File p_i2147_1_) {
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
        final ArrayList var1 = Lists.newArrayList();
        for (int var2 = 0; var2 < 5; ++var2) {
            final String var3 = "World" + (var2 + 1);
            final WorldInfo var4 = this.getWorldInfo(var3);
            if (var4 != null) {
                var1.add(new SaveFormatComparator(var3, "", var4.getLastTimePlayed(), var4.getSizeOnDisk(), var4.getGameType(), false, var4.isHardcoreModeEnabled(), var4.areCommandsAllowed()));
            }
        }
        return var1;
    }
    
    @Override
    public void flushCache() {
    }
    
    @Override
    public WorldInfo getWorldInfo(final String p_75803_1_) {
        final File var2 = new File(this.savesDirectory, p_75803_1_);
        if (!var2.exists()) {
            return null;
        }
        File var3 = new File(var2, "level.dat");
        if (var3.exists()) {
            try {
                final NBTTagCompound var4 = CompressedStreamTools.readCompressed(new FileInputStream(var3));
                final NBTTagCompound var5 = var4.getCompoundTag("Data");
                return new WorldInfo(var5);
            }
            catch (Exception var6) {
                SaveFormatOld.logger.error("Exception reading " + var3, (Throwable)var6);
            }
        }
        var3 = new File(var2, "level.dat_old");
        if (var3.exists()) {
            try {
                final NBTTagCompound var4 = CompressedStreamTools.readCompressed(new FileInputStream(var3));
                final NBTTagCompound var5 = var4.getCompoundTag("Data");
                return new WorldInfo(var5);
            }
            catch (Exception var7) {
                SaveFormatOld.logger.error("Exception reading " + var3, (Throwable)var7);
            }
        }
        return null;
    }
    
    @Override
    public void renameWorld(final String p_75806_1_, final String p_75806_2_) {
        final File var3 = new File(this.savesDirectory, p_75806_1_);
        if (var3.exists()) {
            final File var4 = new File(var3, "level.dat");
            if (var4.exists()) {
                try {
                    final NBTTagCompound var5 = CompressedStreamTools.readCompressed(new FileInputStream(var4));
                    final NBTTagCompound var6 = var5.getCompoundTag("Data");
                    var6.setString("LevelName", p_75806_2_);
                    CompressedStreamTools.writeCompressed(var5, new FileOutputStream(var4));
                }
                catch (Exception var7) {
                    var7.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public boolean func_154335_d(final String p_154335_1_) {
        final File var2 = new File(this.savesDirectory, p_154335_1_);
        if (var2.exists()) {
            return false;
        }
        try {
            var2.mkdir();
            var2.delete();
            return true;
        }
        catch (Throwable var3) {
            SaveFormatOld.logger.warn("Couldn't make new level", var3);
            return false;
        }
    }
    
    @Override
    public boolean deleteWorldDirectory(final String p_75802_1_) {
        final File var2 = new File(this.savesDirectory, p_75802_1_);
        if (!var2.exists()) {
            return true;
        }
        SaveFormatOld.logger.info("Deleting level " + p_75802_1_);
        for (int var3 = 1; var3 <= 5; ++var3) {
            SaveFormatOld.logger.info("Attempt " + var3 + "...");
            if (deleteFiles(var2.listFiles())) {
                break;
            }
            SaveFormatOld.logger.warn("Unsuccessful in deleting contents.");
            if (var3 < 5) {
                try {
                    Thread.sleep(500L);
                }
                catch (InterruptedException ex) {}
            }
        }
        return var2.delete();
    }
    
    protected static boolean deleteFiles(final File[] p_75807_0_) {
        for (int var1 = 0; var1 < p_75807_0_.length; ++var1) {
            final File var2 = p_75807_0_[var1];
            SaveFormatOld.logger.debug("Deleting " + var2);
            if (var2.isDirectory() && !deleteFiles(var2.listFiles())) {
                SaveFormatOld.logger.warn("Couldn't delete directory " + var2);
                return false;
            }
            if (!var2.delete()) {
                SaveFormatOld.logger.warn("Couldn't delete file " + var2);
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ISaveHandler getSaveLoader(final String p_75804_1_, final boolean p_75804_2_) {
        return new SaveHandler(this.savesDirectory, p_75804_1_, p_75804_2_);
    }
    
    @Override
    public boolean func_154334_a(final String p_154334_1_) {
        return false;
    }
    
    @Override
    public boolean isOldMapFormat(final String p_75801_1_) {
        return false;
    }
    
    @Override
    public boolean convertMapFormat(final String p_75805_1_, final IProgressUpdate p_75805_2_) {
        return false;
    }
    
    @Override
    public boolean canLoadWorld(final String p_90033_1_) {
        final File var2 = new File(this.savesDirectory, p_90033_1_);
        return var2.isDirectory();
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
