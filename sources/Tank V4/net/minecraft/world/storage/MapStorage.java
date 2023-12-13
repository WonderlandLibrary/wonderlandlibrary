package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.world.WorldSavedData;

public class MapStorage {
   private Map idCounts = Maps.newHashMap();
   private List loadedDataList = Lists.newArrayList();
   protected Map loadedDataMap = Maps.newHashMap();
   private ISaveHandler saveHandler;

   public void saveAllData() {
      for(int var1 = 0; var1 < this.loadedDataList.size(); ++var1) {
         WorldSavedData var2 = (WorldSavedData)this.loadedDataList.get(var1);
         if (var2.isDirty()) {
            this.saveData(var2);
            var2.setDirty(false);
         }
      }

   }

   public int getUniqueDataId(String var1) {
      Short var2 = (Short)this.idCounts.get(var1);
      if (var2 == null) {
         var2 = Short.valueOf((short)0);
      } else {
         var2 = (short)(var2 + 1);
      }

      this.idCounts.put(var1, var2);
      if (this.saveHandler == null) {
         return var2;
      } else {
         try {
            File var3 = this.saveHandler.getMapFileFromName("idcounts");
            if (var3 != null) {
               NBTTagCompound var4 = new NBTTagCompound();
               Iterator var6 = this.idCounts.keySet().iterator();

               while(var6.hasNext()) {
                  String var5 = (String)var6.next();
                  short var7 = (Short)this.idCounts.get(var5);
                  var4.setShort(var5, var7);
               }

               DataOutputStream var9 = new DataOutputStream(new FileOutputStream(var3));
               CompressedStreamTools.write(var4, (DataOutput)var9);
               var9.close();
            }
         } catch (Exception var8) {
            var8.printStackTrace();
         }

         return var2;
      }
   }

   public void setData(String var1, WorldSavedData var2) {
      if (this.loadedDataMap.containsKey(var1)) {
         this.loadedDataList.remove(this.loadedDataMap.remove(var1));
      }

      this.loadedDataMap.put(var1, var2);
      this.loadedDataList.add(var2);
   }

   private void loadIdCounts() {
      try {
         this.idCounts.clear();
         if (this.saveHandler == null) {
            return;
         }

         File var1 = this.saveHandler.getMapFileFromName("idcounts");
         if (var1 != null && var1.exists()) {
            DataInputStream var2 = new DataInputStream(new FileInputStream(var1));
            NBTTagCompound var3 = CompressedStreamTools.read(var2);
            var2.close();
            Iterator var5 = var3.getKeySet().iterator();

            while(var5.hasNext()) {
               String var4 = (String)var5.next();
               NBTBase var6 = var3.getTag(var4);
               if (var6 instanceof NBTTagShort) {
                  NBTTagShort var7 = (NBTTagShort)var6;
                  short var8 = var7.getShort();
                  this.idCounts.put(var4, var8);
               }
            }
         }
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   private void saveData(WorldSavedData var1) {
      if (this.saveHandler != null) {
         try {
            File var2 = this.saveHandler.getMapFileFromName(var1.mapName);
            if (var2 != null) {
               NBTTagCompound var3 = new NBTTagCompound();
               var1.writeToNBT(var3);
               NBTTagCompound var4 = new NBTTagCompound();
               var4.setTag("data", var3);
               FileOutputStream var5 = new FileOutputStream(var2);
               CompressedStreamTools.writeCompressed(var4, var5);
               var5.close();
            }
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      }

   }

   public MapStorage(ISaveHandler var1) {
      this.saveHandler = var1;
      this.loadIdCounts();
   }

   public WorldSavedData loadData(Class var1, String var2) {
      WorldSavedData var3 = (WorldSavedData)this.loadedDataMap.get(var2);
      if (var3 != null) {
         return var3;
      } else {
         if (this.saveHandler != null) {
            try {
               File var4 = this.saveHandler.getMapFileFromName(var2);
               if (var4 != null && var4.exists()) {
                  try {
                     var3 = (WorldSavedData)var1.getConstructor(String.class).newInstance(var2);
                  } catch (Exception var7) {
                     throw new RuntimeException("Failed to instantiate " + var1.toString(), var7);
                  }

                  FileInputStream var5 = new FileInputStream(var4);
                  NBTTagCompound var6 = CompressedStreamTools.readCompressed(var5);
                  var5.close();
                  var3.readFromNBT(var6.getCompoundTag("data"));
               }
            } catch (Exception var8) {
               var8.printStackTrace();
            }
         }

         if (var3 != null) {
            this.loadedDataMap.put(var2, var3);
            this.loadedDataList.add(var3);
         }

         return var3;
      }
   }
}
