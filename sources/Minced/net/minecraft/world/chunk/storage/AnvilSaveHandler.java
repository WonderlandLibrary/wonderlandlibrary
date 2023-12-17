// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.chunk.storage;

import net.minecraft.world.storage.ThreadedFileIOBase;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProvider;
import net.minecraft.util.datafix.DataFixer;
import java.io.File;
import net.minecraft.world.storage.SaveHandler;

public class AnvilSaveHandler extends SaveHandler
{
    public AnvilSaveHandler(final File p_i46650_1_, final String saveDirectoryName, final boolean p_i46650_3_, final DataFixer dataFixerIn) {
        super(p_i46650_1_, saveDirectoryName, p_i46650_3_, dataFixerIn);
    }
    
    @Override
    public IChunkLoader getChunkLoader(final WorldProvider provider) {
        final File file1 = this.getWorldDirectory();
        if (provider instanceof WorldProviderHell) {
            final File file2 = new File(file1, "DIM-1");
            file2.mkdirs();
            return new AnvilChunkLoader(file2, this.dataFixer);
        }
        if (provider instanceof WorldProviderEnd) {
            final File file3 = new File(file1, "DIM1");
            file3.mkdirs();
            return new AnvilChunkLoader(file3, this.dataFixer);
        }
        return new AnvilChunkLoader(file1, this.dataFixer);
    }
    
    @Override
    public void saveWorldInfoWithPlayer(final WorldInfo worldInformation, @Nullable final NBTTagCompound tagCompound) {
        worldInformation.setSaveVersion(19133);
        super.saveWorldInfoWithPlayer(worldInformation, tagCompound);
    }
    
    @Override
    public void flush() {
        try {
            ThreadedFileIOBase.getThreadedIOInstance().waitForFinish();
        }
        catch (InterruptedException interruptedexception) {
            interruptedexception.printStackTrace();
        }
        RegionFileCache.clearRegionFileReferences();
    }
}
