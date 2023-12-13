// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.chunk.storage;

import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProvider;
import java.io.File;
import net.minecraft.world.storage.SaveHandler;

public class AnvilSaveHandler extends SaveHandler
{
    private static final String __OBFID = "CL_00000581";
    
    public AnvilSaveHandler(final File p_i2142_1_, final String p_i2142_2_, final boolean p_i2142_3_) {
        super(p_i2142_1_, p_i2142_2_, p_i2142_3_);
    }
    
    @Override
    public IChunkLoader getChunkLoader(final WorldProvider p_75763_1_) {
        final File var2 = this.getWorldDirectory();
        if (p_75763_1_ instanceof WorldProviderHell) {
            final File var3 = new File(var2, "DIM-1");
            var3.mkdirs();
            return new AnvilChunkLoader(var3);
        }
        if (p_75763_1_ instanceof WorldProviderEnd) {
            final File var3 = new File(var2, "DIM1");
            var3.mkdirs();
            return new AnvilChunkLoader(var3);
        }
        return new AnvilChunkLoader(var2);
    }
    
    @Override
    public void saveWorldInfoWithPlayer(final WorldInfo p_75755_1_, final NBTTagCompound p_75755_2_) {
        p_75755_1_.setSaveVersion(19133);
        super.saveWorldInfoWithPlayer(p_75755_1_, p_75755_2_);
    }
    
    @Override
    public void flush() {
        try {
            ThreadedFileIOBase.func_178779_a().waitForFinish();
        }
        catch (InterruptedException var2) {
            var2.printStackTrace();
        }
        RegionFileCache.clearRegionFileReferences();
    }
}
