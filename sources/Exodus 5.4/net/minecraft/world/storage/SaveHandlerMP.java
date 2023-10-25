/*
 * Decompiled with CFR 0.152.
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
    @Override
    public WorldInfo loadWorldInfo() {
        return null;
    }

    @Override
    public void flush() {
    }

    @Override
    public File getWorldDirectory() {
        return null;
    }

    @Override
    public void saveWorldInfoWithPlayer(WorldInfo worldInfo, NBTTagCompound nBTTagCompound) {
    }

    @Override
    public void checkSessionLock() throws MinecraftException {
    }

    @Override
    public File getMapFileFromName(String string) {
        return null;
    }

    @Override
    public IPlayerFileData getPlayerNBTManager() {
        return null;
    }

    @Override
    public void saveWorldInfo(WorldInfo worldInfo) {
    }

    @Override
    public String getWorldDirectoryName() {
        return "none";
    }

    @Override
    public IChunkLoader getChunkLoader(WorldProvider worldProvider) {
        return null;
    }
}

