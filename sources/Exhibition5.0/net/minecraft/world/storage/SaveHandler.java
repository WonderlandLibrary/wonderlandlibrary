// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.storage;

import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.MinecraftException;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import net.minecraft.server.MinecraftServer;
import java.io.File;
import org.apache.logging.log4j.Logger;

public class SaveHandler implements ISaveHandler, IPlayerFileData
{
    private static final Logger logger;
    private final File worldDirectory;
    private final File playersDirectory;
    private final File mapDataDir;
    private final long initializationTime;
    private final String saveDirectoryName;
    private static final String __OBFID = "CL_00000585";
    
    public SaveHandler(final File p_i2146_1_, final String p_i2146_2_, final boolean p_i2146_3_) {
        this.initializationTime = MinecraftServer.getCurrentTimeMillis();
        (this.worldDirectory = new File(p_i2146_1_, p_i2146_2_)).mkdirs();
        this.playersDirectory = new File(this.worldDirectory, "playerdata");
        (this.mapDataDir = new File(this.worldDirectory, "data")).mkdirs();
        this.saveDirectoryName = p_i2146_2_;
        if (p_i2146_3_) {
            this.playersDirectory.mkdirs();
        }
        this.setSessionLock();
    }
    
    private void setSessionLock() {
        try {
            final File var1 = new File(this.worldDirectory, "session.lock");
            final DataOutputStream var2 = new DataOutputStream(new FileOutputStream(var1));
            try {
                var2.writeLong(this.initializationTime);
            }
            finally {
                var2.close();
            }
        }
        catch (IOException var3) {
            var3.printStackTrace();
            throw new RuntimeException("Failed to check session lock, aborting");
        }
    }
    
    @Override
    public File getWorldDirectory() {
        return this.worldDirectory;
    }
    
    @Override
    public void checkSessionLock() throws MinecraftException {
        try {
            final File var1 = new File(this.worldDirectory, "session.lock");
            final DataInputStream var2 = new DataInputStream(new FileInputStream(var1));
            try {
                if (var2.readLong() != this.initializationTime) {
                    throw new MinecraftException("The save is being accessed from another location, aborting");
                }
            }
            finally {
                var2.close();
            }
        }
        catch (IOException var3) {
            throw new MinecraftException("Failed to check session lock, aborting");
        }
    }
    
    @Override
    public IChunkLoader getChunkLoader(final WorldProvider p_75763_1_) {
        throw new RuntimeException("Old Chunk Storage is no longer supported.");
    }
    
    @Override
    public WorldInfo loadWorldInfo() {
        File var1 = new File(this.worldDirectory, "level.dat");
        if (var1.exists()) {
            try {
                final NBTTagCompound var2 = CompressedStreamTools.readCompressed(new FileInputStream(var1));
                final NBTTagCompound var3 = var2.getCompoundTag("Data");
                return new WorldInfo(var3);
            }
            catch (Exception var4) {
                var4.printStackTrace();
            }
        }
        var1 = new File(this.worldDirectory, "level.dat_old");
        if (var1.exists()) {
            try {
                final NBTTagCompound var2 = CompressedStreamTools.readCompressed(new FileInputStream(var1));
                final NBTTagCompound var3 = var2.getCompoundTag("Data");
                return new WorldInfo(var3);
            }
            catch (Exception var5) {
                var5.printStackTrace();
            }
        }
        return null;
    }
    
    @Override
    public void saveWorldInfoWithPlayer(final WorldInfo p_75755_1_, final NBTTagCompound p_75755_2_) {
        final NBTTagCompound var3 = p_75755_1_.cloneNBTCompound(p_75755_2_);
        final NBTTagCompound var4 = new NBTTagCompound();
        var4.setTag("Data", var3);
        try {
            final File var5 = new File(this.worldDirectory, "level.dat_new");
            final File var6 = new File(this.worldDirectory, "level.dat_old");
            final File var7 = new File(this.worldDirectory, "level.dat");
            CompressedStreamTools.writeCompressed(var4, new FileOutputStream(var5));
            if (var6.exists()) {
                var6.delete();
            }
            var7.renameTo(var6);
            if (var7.exists()) {
                var7.delete();
            }
            var5.renameTo(var7);
            if (var5.exists()) {
                var5.delete();
            }
        }
        catch (Exception var8) {
            var8.printStackTrace();
        }
    }
    
    @Override
    public void saveWorldInfo(final WorldInfo p_75761_1_) {
        final NBTTagCompound var2 = p_75761_1_.getNBTTagCompound();
        final NBTTagCompound var3 = new NBTTagCompound();
        var3.setTag("Data", var2);
        try {
            final File var4 = new File(this.worldDirectory, "level.dat_new");
            final File var5 = new File(this.worldDirectory, "level.dat_old");
            final File var6 = new File(this.worldDirectory, "level.dat");
            CompressedStreamTools.writeCompressed(var3, new FileOutputStream(var4));
            if (var5.exists()) {
                var5.delete();
            }
            var6.renameTo(var5);
            if (var6.exists()) {
                var6.delete();
            }
            var4.renameTo(var6);
            if (var4.exists()) {
                var4.delete();
            }
        }
        catch (Exception var7) {
            var7.printStackTrace();
        }
    }
    
    @Override
    public void writePlayerData(final EntityPlayer p_75753_1_) {
        try {
            final NBTTagCompound var2 = new NBTTagCompound();
            p_75753_1_.writeToNBT(var2);
            final File var3 = new File(this.playersDirectory, p_75753_1_.getUniqueID().toString() + ".dat.tmp");
            final File var4 = new File(this.playersDirectory, p_75753_1_.getUniqueID().toString() + ".dat");
            CompressedStreamTools.writeCompressed(var2, new FileOutputStream(var3));
            if (var4.exists()) {
                var4.delete();
            }
            var3.renameTo(var4);
        }
        catch (Exception var5) {
            SaveHandler.logger.warn("Failed to save player data for " + p_75753_1_.getName());
        }
    }
    
    @Override
    public NBTTagCompound readPlayerData(final EntityPlayer p_75752_1_) {
        NBTTagCompound var2 = null;
        try {
            final File var3 = new File(this.playersDirectory, p_75752_1_.getUniqueID().toString() + ".dat");
            if (var3.exists() && var3.isFile()) {
                var2 = CompressedStreamTools.readCompressed(new FileInputStream(var3));
            }
        }
        catch (Exception var4) {
            SaveHandler.logger.warn("Failed to load player data for " + p_75752_1_.getName());
        }
        if (var2 != null) {
            p_75752_1_.readFromNBT(var2);
        }
        return var2;
    }
    
    @Override
    public IPlayerFileData getPlayerNBTManager() {
        return this;
    }
    
    @Override
    public String[] getAvailablePlayerDat() {
        String[] var1 = this.playersDirectory.list();
        if (var1 == null) {
            var1 = new String[0];
        }
        for (int var2 = 0; var2 < var1.length; ++var2) {
            if (var1[var2].endsWith(".dat")) {
                var1[var2] = var1[var2].substring(0, var1[var2].length() - 4);
            }
        }
        return var1;
    }
    
    @Override
    public void flush() {
    }
    
    @Override
    public File getMapFileFromName(final String p_75758_1_) {
        return new File(this.mapDataDir, p_75758_1_ + ".dat");
    }
    
    @Override
    public String getWorldDirectoryName() {
        return this.saveDirectoryName;
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
