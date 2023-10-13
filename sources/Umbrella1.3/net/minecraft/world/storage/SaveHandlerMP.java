/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class SaveHandlerMP
implements ISaveHandler {
    private static final String __OBFID = "CL_00000602";

    @Override
    public WorldInfo loadWorldInfo() {
        return null;
    }

    @Override
    public void checkSessionLock() throws MinecraftException {
    }

    @Override
    public IChunkLoader getChunkLoader(WorldProvider p_75763_1_) {
        return null;
    }

    @Override
    public void saveWorldInfoWithPlayer(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_) {
    }

    @Override
    public void saveWorldInfo(WorldInfo p_75761_1_) {
    }

    @Override
    public IPlayerFileData getPlayerNBTManager() {
        return null;
    }

    @Override
    public void flush() {
    }

    @Override
    public File getMapFileFromName(String p_75758_1_) {
        return null;
    }

    @Override
    public String getWorldDirectoryName() {
        return "none";
    }

    @Override
    public File getWorldDirectory() {
        return null;
    }
}

