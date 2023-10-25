/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.chunk.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraft.world.storage.WorldInfo;

public class AnvilSaveHandler
extends SaveHandler {
    @Override
    public IChunkLoader getChunkLoader(WorldProvider worldProvider) {
        File file = this.getWorldDirectory();
        if (worldProvider instanceof WorldProviderHell) {
            File file2 = new File(file, "DIM-1");
            file2.mkdirs();
            return new AnvilChunkLoader(file2);
        }
        if (worldProvider instanceof WorldProviderEnd) {
            File file3 = new File(file, "DIM1");
            file3.mkdirs();
            return new AnvilChunkLoader(file3);
        }
        return new AnvilChunkLoader(file);
    }

    public AnvilSaveHandler(File file, String string, boolean bl) {
        super(file, string, bl);
    }

    @Override
    public void flush() {
        try {
            ThreadedFileIOBase.getThreadedIOInstance().waitForFinish();
        }
        catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        RegionFileCache.clearRegionFileReferences();
    }

    @Override
    public void saveWorldInfoWithPlayer(WorldInfo worldInfo, NBTTagCompound nBTTagCompound) {
        worldInfo.setSaveVersion(19133);
        super.saveWorldInfoWithPlayer(worldInfo, nBTTagCompound);
    }
}

