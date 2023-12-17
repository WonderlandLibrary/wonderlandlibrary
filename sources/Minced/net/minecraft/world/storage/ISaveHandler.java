// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage;

import net.minecraft.world.gen.structure.template.TemplateManager;
import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.MinecraftException;
import javax.annotation.Nullable;

public interface ISaveHandler
{
    @Nullable
    WorldInfo loadWorldInfo();
    
    void checkSessionLock() throws MinecraftException;
    
    IChunkLoader getChunkLoader(final WorldProvider p0);
    
    void saveWorldInfoWithPlayer(final WorldInfo p0, final NBTTagCompound p1);
    
    void saveWorldInfo(final WorldInfo p0);
    
    IPlayerFileData getPlayerNBTManager();
    
    void flush();
    
    File getWorldDirectory();
    
    File getMapFileFromName(final String p0);
    
    TemplateManager getStructureTemplateManager();
}
