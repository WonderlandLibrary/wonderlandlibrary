package net.minecraft.nbt;

import com.google.common.collect.Maps;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;

public class NBTTagCompound extends NBTBase {
   private Map tagMap = Maps.newHashMap();

   public Set getKeySet() {
      return this.tagMap.keySet();
   }

   public String getString(String var1) {
      try {
         return ((NBTBase)this.tagMap.get(var1)).getString();
      } catch (ClassCastException var3) {
         return "";
      }
   }

   public byte getByte(String var1) {
      try {
         return ((NBTBase.NBTPrimitive)this.tagMap.get(var1)).getByte();
      } catch (ClassCastException var3) {
         return 0;
      }
   }

   public boolean hasNoTags() {
      return this.tagMap.isEmpty();
   }

   public NBTBase copy() {
      NBTTagCompound var1 = new NBTTagCompound();
      Iterator var3 = this.tagMap.keySet().iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         var1.setTag(var2, ((NBTBase)this.tagMap.get(var2)).copy());
      }

      return var1;
   }

   public void setByteArray(String var1, byte[] var2) {
      this.tagMap.put(var1, new NBTTagByteArray(var2));
   }

   public byte getId() {
      return 10;
   }

   public long getLong(String var1) {
      try {
         return ((NBTBase.NBTPrimitive)this.tagMap.get(var1)).getLong();
      } catch (ClassCastException var3) {
         return 0L;
      }
   }

   public byte[] getByteArray(String var1) {
      try {
         return ((NBTTagByteArray)this.tagMap.get(var1)).getByteArray();
      } catch (ClassCastException var3) {
         throw new ReportedException(this.createCrashReport(var1, 7, var3));
      }
   }

   public void setByte(String var1, byte var2) {
      this.tagMap.put(var1, new NBTTagByte(var2));
   }

   public void setBoolean(String var1, boolean var2) {
      this.setByte(var1, (byte)(var2 ? 1 : 0));
   }

   public void setInteger(String var1, int var2) {
      this.tagMap.put(var1, new NBTTagInt(var2));
   }

   public byte getTagId(String var1) {
      NBTBase var2 = (NBTBase)this.tagMap.get(var1);
      return var2 != null ? var2.getId() : 0;
   }

   static NBTBase readNBT(byte var0, String var1, DataInput var2, int var3, NBTSizeTracker var4) throws IOException {
      NBTBase var5 = NBTBase.createNewByType(var0);

      try {
         var5.read(var2, var3, var4);
         return var5;
      } catch (IOException var9) {
         CrashReport var7 = CrashReport.makeCrashReport(var9, "Loading NBT data");
         CrashReportCategory var8 = var7.makeCategory("NBT Tag");
         var8.addCrashSection("Tag name", var1);
         var8.addCrashSection("Tag type", var0);
         throw new ReportedException(var7);
      }
   }

   private static byte readType(DataInput var0, NBTSizeTracker var1) throws IOException {
      return var0.readByte();
   }

   public void setIntArray(String var1, int[] var2) {
      this.tagMap.put(var1, new NBTTagIntArray(var2));
   }

   void read(DataInput var1, int var2, NBTSizeTracker var3) throws IOException {
      var3.read(384L);
      if (var2 > 512) {
         throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
      } else {
         this.tagMap.clear();

         byte var4;
         while((var4 = readType(var1, var3)) != 0) {
            String var5 = readKey(var1, var3);
            var3.read((long)(224 + 16 * var5.length()));
            NBTBase var6 = readNBT(var4, var5, var1, var2 + 1, var3);
            if (this.tagMap.put(var5, var6) != null) {
               var3.read(288L);
            }
         }

      }
   }

   private CrashReport createCrashReport(String var1, int var2, ClassCastException var3) {
      CrashReport var4 = CrashReport.makeCrashReport(var3, "Reading NBT data");
      CrashReportCategory var5 = var4.makeCategoryDepth("Corrupt NBT tag", 1);
      var5.addCrashSectionCallable("Tag type found", new Callable(this, var1) {
         private final String val$key;
         final NBTTagCompound this$0;

         public Object call() throws Exception {
            return this.call();
         }

         {
            this.this$0 = var1;
            this.val$key = var2;
         }

         public String call() throws Exception {
            return NBTBase.NBT_TYPES[((NBTBase)NBTTagCompound.access$0(this.this$0).get(this.val$key)).getId()];
         }
      });
      var5.addCrashSectionCallable("Tag type expected", new Callable(this, var2) {
         private final int val$expectedType;
         final NBTTagCompound this$0;

         public Object call() throws Exception {
            return this.call();
         }

         {
            this.this$0 = var1;
            this.val$expectedType = var2;
         }

         public String call() throws Exception {
            return NBTBase.NBT_TYPES[this.val$expectedType];
         }
      });
      var5.addCrashSection("Tag name", var1);
      return var4;
   }

   public int[] getIntArray(String var1) {
      try {
         return ((NBTTagIntArray)this.tagMap.get(var1)).getIntArray();
      } catch (ClassCastException var4) {
         throw new ReportedException(this.createCrashReport(var1, 11, var4));
      }
   }

   public NBTTagList getTagList(String var1, int var2) {
      try {
         if (this.getTagId(var1) != 9) {
            return new NBTTagList();
         } else {
            NBTTagList var3 = (NBTTagList)this.tagMap.get(var1);
            return var3.tagCount() > 0 && var3.getTagType() != var2 ? new NBTTagList() : var3;
         }
      } catch (ClassCastException var5) {
         throw new ReportedException(this.createCrashReport(var1, 9, var5));
      }
   }

   public void removeTag(String var1) {
      this.tagMap.remove(var1);
   }

   public boolean hasKey(String var1) {
      return this.tagMap.containsKey(var1);
   }

   public void setShort(String var1, short var2) {
      this.tagMap.put(var1, new NBTTagShort(var2));
   }

   public NBTTagCompound getCompoundTag(String var1) {
      try {
         return (NBTTagCompound)this.tagMap.get(var1);
      } catch (ClassCastException var4) {
         throw new ReportedException(this.createCrashReport(var1, 10, var4));
      }
   }

   public void setString(String var1, String var2) {
      this.tagMap.put(var1, new NBTTagString(var2));
   }

   public NBTBase getTag(String var1) {
      return (NBTBase)this.tagMap.get(var1);
   }

   public void setLong(String var1, long var2) {
      this.tagMap.put(var1, new NBTTagLong(var2));
   }

   public int hashCode() {
      return super.hashCode() ^ this.tagMap.hashCode();
   }

   public void setDouble(String var1, double var2) {
      this.tagMap.put(var1, new NBTTagDouble(var2));
   }

   public void setFloat(String var1, float var2) {
      this.tagMap.put(var1, new NBTTagFloat(var2));
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("{");

      Entry var2;
      for(Iterator var3 = this.tagMap.entrySet().iterator(); var3.hasNext(); var1.append((String)var2.getKey()).append(':').append(var2.getValue())) {
         var2 = (Entry)var3.next();
         if (var1.length() != 1) {
            var1.append(',');
         }
      }

      return var1.append('}').toString();
   }

   static Map access$0(NBTTagCompound var0) {
      return var0.tagMap;
   }

   private static void writeEntry(String var0, NBTBase var1, DataOutput var2) throws IOException {
      var2.writeByte(var1.getId());
      if (var1.getId() != 0) {
         var2.writeUTF(var0);
         var1.write(var2);
      }

   }

   void write(DataOutput var1) throws IOException {
      Iterator var3 = this.tagMap.keySet().iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         NBTBase var4 = (NBTBase)this.tagMap.get(var2);
         writeEntry(var2, var4, var1);
      }

      var1.writeByte(0);
   }

   public float getFloat(String var1) {
      try {
         return ((NBTBase.NBTPrimitive)this.tagMap.get(var1)).getFloat();
      } catch (ClassCastException var3) {
         return 0.0F;
      }
   }

   public int getInteger(String var1) {
      try {
         return var1 == true ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(var1)).getInt();
      } catch (ClassCastException var3) {
         return 0;
      }
   }

   private static String readKey(DataInput var0, NBTSizeTracker var1) throws IOException {
      return var0.readUTF();
   }

   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         NBTTagCompound var2 = (NBTTagCompound)var1;
         return this.tagMap.entrySet().equals(var2.tagMap.entrySet());
      } else {
         return false;
      }
   }

   public void setTag(String var1, NBTBase var2) {
      this.tagMap.put(var1, var2);
   }

   public double getDouble(String var1) {
      try {
         return var1 == true ? 0.0D : ((NBTBase.NBTPrimitive)this.tagMap.get(var1)).getDouble();
      } catch (ClassCastException var3) {
         return 0.0D;
      }
   }

   public boolean getBoolean(String var1) {
      return this.getByte(var1) != 0;
   }

   public void merge(NBTTagCompound var1) {
      Iterator var3 = var1.tagMap.keySet().iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         NBTBase var4 = (NBTBase)var1.tagMap.get(var2);
         if (var4.getId() == 10) {
            if (var2 == true) {
               NBTTagCompound var5 = this.getCompoundTag(var2);
               var5.merge((NBTTagCompound)var4);
            } else {
               this.setTag(var2, var4.copy());
            }
         } else {
            this.setTag(var2, var4.copy());
         }
      }

   }

   public short getShort(String var1) {
      try {
         return var1 == true ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(var1)).getShort();
      } catch (ClassCastException var3) {
         return 0;
      }
   }
}
