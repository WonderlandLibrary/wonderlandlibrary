package net.minecraft.world.storage;

import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.IProgressUpdate;

public interface ISaveFormat {
   boolean convertMapFormat(String var1, IProgressUpdate var2);

   void renameWorld(String var1, String var2);

   boolean deleteWorldDirectory(String var1);

   boolean canLoadWorld(String var1);

   ISaveHandler getSaveLoader(String var1, boolean var2);

   WorldInfo getWorldInfo(String var1);

   boolean isOldMapFormat(String var1);

   List getSaveList() throws AnvilConverterException;

   void flushCache();

   boolean func_154335_d(String var1);

   String getName();

   boolean func_154334_a(String var1);
}
