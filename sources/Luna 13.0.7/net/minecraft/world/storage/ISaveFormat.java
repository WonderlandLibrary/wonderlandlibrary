package net.minecraft.world.storage;

import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.IProgressUpdate;

public abstract interface ISaveFormat
{
  public abstract String func_154333_a();
  
  public abstract ISaveHandler getSaveLoader(String paramString, boolean paramBoolean);
  
  public abstract List getSaveList()
    throws AnvilConverterException;
  
  public abstract void flushCache();
  
  public abstract WorldInfo getWorldInfo(String paramString);
  
  public abstract boolean func_154335_d(String paramString);
  
  public abstract boolean deleteWorldDirectory(String paramString);
  
  public abstract void renameWorld(String paramString1, String paramString2);
  
  public abstract boolean func_154334_a(String paramString);
  
  public abstract boolean isOldMapFormat(String paramString);
  
  public abstract boolean convertMapFormat(String paramString, IProgressUpdate paramIProgressUpdate);
  
  public abstract boolean canLoadWorld(String paramString);
}
