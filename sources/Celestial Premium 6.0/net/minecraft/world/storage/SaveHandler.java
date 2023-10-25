/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatOld;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveHandler
implements ISaveHandler,
IPlayerFileData {
    private static final Logger LOGGER = LogManager.getLogger();
    private final File worldDirectory;
    private final File playersDirectory;
    private final File mapDataDir;
    private final long initializationTime = MinecraftServer.getCurrentTimeMillis();
    private final String saveDirectoryName;
    private final TemplateManager structureTemplateManager;
    protected final DataFixer dataFixer;

    public SaveHandler(File p_i46648_1_, String saveDirectoryNameIn, boolean p_i46648_3_, DataFixer dataFixerIn) {
        this.dataFixer = dataFixerIn;
        this.worldDirectory = new File(p_i46648_1_, saveDirectoryNameIn);
        this.worldDirectory.mkdirs();
        this.playersDirectory = new File(this.worldDirectory, "playerdata");
        this.mapDataDir = new File(this.worldDirectory, "data");
        this.mapDataDir.mkdirs();
        this.saveDirectoryName = saveDirectoryNameIn;
        if (p_i46648_3_) {
            this.playersDirectory.mkdirs();
            this.structureTemplateManager = new TemplateManager(new File(this.worldDirectory, "structures").toString(), dataFixerIn);
        } else {
            this.structureTemplateManager = null;
        }
        this.setSessionLock();
    }

    private void setSessionLock() {
        try {
            File file1 = new File(this.worldDirectory, "session.lock");
            try (DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));){
                dataoutputstream.writeLong(this.initializationTime);
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
            File file1 = new File(this.worldDirectory, "session.lock");
            try (DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));){
                if (datainputstream.readLong() != this.initializationTime) {
                    throw new MinecraftException("The save is being accessed from another location, aborting");
                }
            }
        }
        catch (IOException var7) {
            throw new MinecraftException("Failed to check session lock, aborting");
        }
    }

    @Override
    public IChunkLoader getChunkLoader(WorldProvider provider) {
        throw new RuntimeException("Old Chunk Storage is no longer supported.");
    }

    @Override
    @Nullable
    public WorldInfo loadWorldInfo() {
        WorldInfo worldinfo;
        File file1 = new File(this.worldDirectory, "level.dat");
        if (file1.exists() && (worldinfo = SaveFormatOld.getWorldData(file1, this.dataFixer)) != null) {
            return worldinfo;
        }
        file1 = new File(this.worldDirectory, "level.dat_old");
        return file1.exists() ? SaveFormatOld.getWorldData(file1, this.dataFixer) : null;
    }

    @Override
    public void saveWorldInfoWithPlayer(WorldInfo worldInformation, @Nullable NBTTagCompound tagCompound) {
        NBTTagCompound nbttagcompound = worldInformation.cloneNBTCompound(tagCompound);
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setTag("Data", nbttagcompound);
        try {
            File file1 = new File(this.worldDirectory, "level.dat_new");
            File file2 = new File(this.worldDirectory, "level.dat_old");
            File file3 = new File(this.worldDirectory, "level.dat");
            CompressedStreamTools.writeCompressed(nbttagcompound1, new FileOutputStream(file1));
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
    public void saveWorldInfo(WorldInfo worldInformation) {
        this.saveWorldInfoWithPlayer(worldInformation, null);
    }

    @Override
    public void writePlayerData(EntityPlayer player) {
        try {
            NBTTagCompound nbttagcompound = player.writeToNBT(new NBTTagCompound());
            File file1 = new File(this.playersDirectory, player.getCachedUniqueIdString() + ".dat.tmp");
            File file2 = new File(this.playersDirectory, player.getCachedUniqueIdString() + ".dat");
            CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file1));
            if (file2.exists()) {
                file2.delete();
            }
            file1.renameTo(file2);
        }
        catch (Exception var5) {
            LOGGER.warn("Failed to save player data for {}", player.getName());
        }
    }

    @Override
    @Nullable
    public NBTTagCompound readPlayerData(EntityPlayer player) {
        NBTTagCompound nbttagcompound = null;
        try {
            File file1 = new File(this.playersDirectory, player.getCachedUniqueIdString() + ".dat");
            if (file1.exists() && file1.isFile()) {
                nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file1));
            }
        }
        catch (Exception var4) {
            LOGGER.warn("Failed to load player data for {}", player.getName());
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
            astring = new String[]{};
        }
        for (int i = 0; i < astring.length; ++i) {
            if (!astring[i].endsWith(".dat")) continue;
            astring[i] = astring[i].substring(0, astring[i].length() - 4);
        }
        return astring;
    }

    @Override
    public void flush() {
    }

    @Override
    public File getMapFileFromName(String mapName) {
        return new File(this.mapDataDir, mapName + ".dat");
    }

    @Override
    public TemplateManager getStructureTemplateManager() {
        return this.structureTemplateManager;
    }
}

