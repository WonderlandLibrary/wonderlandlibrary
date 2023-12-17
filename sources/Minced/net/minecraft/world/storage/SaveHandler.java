// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import javax.annotation.Nullable;
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
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.gen.structure.template.TemplateManager;
import java.io.File;
import org.apache.logging.log4j.Logger;

public class SaveHandler implements ISaveHandler, IPlayerFileData
{
    private static final Logger LOGGER;
    private final File worldDirectory;
    private final File playersDirectory;
    private final File mapDataDir;
    private final long initializationTime;
    private final String saveDirectoryName;
    private final TemplateManager structureTemplateManager;
    protected final DataFixer dataFixer;
    
    public SaveHandler(final File p_i46648_1_, final String saveDirectoryNameIn, final boolean p_i46648_3_, final DataFixer dataFixerIn) {
        this.initializationTime = MinecraftServer.getCurrentTimeMillis();
        this.dataFixer = dataFixerIn;
        (this.worldDirectory = new File(p_i46648_1_, saveDirectoryNameIn)).mkdirs();
        this.playersDirectory = new File(this.worldDirectory, "playerdata");
        (this.mapDataDir = new File(this.worldDirectory, "data")).mkdirs();
        this.saveDirectoryName = saveDirectoryNameIn;
        if (p_i46648_3_) {
            this.playersDirectory.mkdirs();
            this.structureTemplateManager = new TemplateManager(new File(this.worldDirectory, "structures").toString(), dataFixerIn);
        }
        else {
            this.structureTemplateManager = null;
        }
        this.setSessionLock();
    }
    
    private void setSessionLock() {
        try {
            final File file1 = new File(this.worldDirectory, "session.lock");
            final DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));
            try {
                dataoutputstream.writeLong(this.initializationTime);
            }
            finally {
                dataoutputstream.close();
            }
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
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
            final File file1 = new File(this.worldDirectory, "session.lock");
            final DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));
            try {
                if (datainputstream.readLong() != this.initializationTime) {
                    throw new MinecraftException("The save is being accessed from another location, aborting");
                }
            }
            finally {
                datainputstream.close();
            }
        }
        catch (IOException var7) {
            throw new MinecraftException("Failed to check session lock, aborting");
        }
    }
    
    @Override
    public IChunkLoader getChunkLoader(final WorldProvider provider) {
        throw new RuntimeException("Old Chunk Storage is no longer supported.");
    }
    
    @Nullable
    @Override
    public WorldInfo loadWorldInfo() {
        File file1 = new File(this.worldDirectory, "level.dat");
        if (file1.exists()) {
            final WorldInfo worldinfo = SaveFormatOld.getWorldData(file1, this.dataFixer);
            if (worldinfo != null) {
                return worldinfo;
            }
        }
        file1 = new File(this.worldDirectory, "level.dat_old");
        return file1.exists() ? SaveFormatOld.getWorldData(file1, this.dataFixer) : null;
    }
    
    @Override
    public void saveWorldInfoWithPlayer(final WorldInfo worldInformation, @Nullable final NBTTagCompound tagCompound) {
        final NBTTagCompound nbttagcompound = worldInformation.cloneNBTCompound(tagCompound);
        final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
        nbttagcompound2.setTag("Data", nbttagcompound);
        try {
            final File file1 = new File(this.worldDirectory, "level.dat_new");
            final File file2 = new File(this.worldDirectory, "level.dat_old");
            final File file3 = new File(this.worldDirectory, "level.dat");
            CompressedStreamTools.writeCompressed(nbttagcompound2, new FileOutputStream(file1));
            if (file2.exists()) {
                file2.delete();
            }
            file3.renameTo(file2);
            if (file3.exists()) {
                file3.delete();
            }
            file1.renameTo(file3);
            if (file1.exists()) {
                file1.delete();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    @Override
    public void saveWorldInfo(final WorldInfo worldInformation) {
        this.saveWorldInfoWithPlayer(worldInformation, null);
    }
    
    @Override
    public void writePlayerData(final EntityPlayer player) {
        try {
            final NBTTagCompound nbttagcompound = player.writeToNBT(new NBTTagCompound());
            final File file1 = new File(this.playersDirectory, player.getCachedUniqueIdString() + ".dat.tmp");
            final File file2 = new File(this.playersDirectory, player.getCachedUniqueIdString() + ".dat");
            CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file1));
            if (file2.exists()) {
                file2.delete();
            }
            file1.renameTo(file2);
        }
        catch (Exception var5) {
            SaveHandler.LOGGER.warn("Failed to save player data for {}", (Object)player.getName());
        }
    }
    
    @Nullable
    @Override
    public NBTTagCompound readPlayerData(final EntityPlayer player) {
        NBTTagCompound nbttagcompound = null;
        try {
            final File file1 = new File(this.playersDirectory, player.getCachedUniqueIdString() + ".dat");
            if (file1.exists() && file1.isFile()) {
                nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file1));
            }
        }
        catch (Exception var4) {
            SaveHandler.LOGGER.warn("Failed to load player data for {}", (Object)player.getName());
        }
        if (nbttagcompound != null) {
            player.readFromNBT(this.dataFixer.process(FixTypes.PLAYER, nbttagcompound));
        }
        return nbttagcompound;
    }
    
    @Override
    public IPlayerFileData getPlayerNBTManager() {
        return this;
    }
    
    @Override
    public String[] getAvailablePlayerDat() {
        String[] astring = this.playersDirectory.list();
        if (astring == null) {
            astring = new String[0];
        }
        for (int i = 0; i < astring.length; ++i) {
            if (astring[i].endsWith(".dat")) {
                astring[i] = astring[i].substring(0, astring[i].length() - 4);
            }
        }
        return astring;
    }
    
    @Override
    public void flush() {
    }
    
    @Override
    public File getMapFileFromName(final String mapName) {
        return new File(this.mapDataDir, mapName + ".dat");
    }
    
    @Override
    public TemplateManager getStructureTemplateManager() {
        return this.structureTemplateManager;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
