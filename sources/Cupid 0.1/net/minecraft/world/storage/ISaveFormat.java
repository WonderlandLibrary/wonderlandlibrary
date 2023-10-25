package net.minecraft.world.storage;

import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.IProgressUpdate;

public interface ISaveFormat {
  String getName();
  
  ISaveHandler getSaveLoader(String paramString, boolean paramBoolean);
  
  List<SaveFormatComparator> getSaveList() throws AnvilConverterException;
  
  void flushCache();
  
  WorldInfo getWorldInfo(String paramString);
  
  boolean func_154335_d(String paramString);
  
  boolean deleteWorldDirectory(String paramString);
  
  void renameWorld(String paramString1, String paramString2);
  
  boolean func_154334_a(String paramString);
  
  boolean isOldMapFormat(String paramString);
  
  boolean convertMapFormat(String paramString, IProgressUpdate paramIProgressUpdate);
  
  boolean canLoadWorld(String paramString);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\world\storage\ISaveFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */