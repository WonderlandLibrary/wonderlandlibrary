package optifine;

import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import org.apache.commons.lang3.StringEscapeUtils;

public class NbtTagValue {
   private String[] parents = null;
   private String name = null;
   private int type = 0;
   private String value = null;
   private static final int TYPE_TEXT = 0;
   private static final int TYPE_PATTERN = 1;
   private static final int TYPE_IPATTERN = 2;
   private static final int TYPE_REGEX = 3;
   private static final int TYPE_IREGEX = 4;
   private static final String PREFIX_PATTERN = "pattern:";
   private static final String PREFIX_IPATTERN = "ipattern:";
   private static final String PREFIX_REGEX = "regex:";
   private static final String PREFIX_IREGEX = "iregex:";

   public NbtTagValue(String var1, String var2) {
      String[] var3 = Config.tokenize(var1, ".");
      this.parents = (String[])Arrays.copyOfRange(var3, 0, var3.length - 1);
      this.name = var3[var3.length - 1];
      if (var2.startsWith("pattern:")) {
         this.type = 1;
         var2 = var2.substring(8);
      } else if (var2.startsWith("ipattern:")) {
         this.type = 2;
         var2 = var2.substring(9).toLowerCase();
      } else if (var2.startsWith("regex:")) {
         this.type = 3;
         var2 = var2.substring(6);
      } else if (var2.startsWith("iregex:")) {
         this.type = 4;
         var2 = var2.substring(7).toLowerCase();
      } else {
         this.type = 0;
      }

      var2 = StringEscapeUtils.unescapeJava(var2);
      this.value = var2;
   }

   public boolean matches(NBTTagCompound var1) {
      if (var1 == null) {
         return false;
      } else {
         Object var2 = var1;

         for(int var3 = 0; var3 < this.parents.length; ++var3) {
            String var4 = this.parents[var3];
            var2 = getChildTag((NBTBase)var2, var4);
            if (var2 == null) {
               return false;
            }
         }

         if (this.name.equals("*")) {
            return this.matchesAnyChild((NBTBase)var2);
         } else {
            NBTBase var5 = getChildTag((NBTBase)var2, this.name);
            if (var5 == null) {
               return false;
            } else {
               return var5 == null;
            }
         }
      }
   }

   private boolean matchesAnyChild(NBTBase var1) {
      NBTBase var5;
      if (var1 instanceof NBTTagCompound) {
         NBTTagCompound var2 = (NBTTagCompound)var1;
         Iterator var4 = var2.getKeySet().iterator();

         while(var4.hasNext()) {
            String var3 = (String)var4.next();
            var5 = var2.getTag(var3);
            if (var5 == null) {
               return true;
            }
         }
      }

      if (var1 instanceof NBTTagList) {
         NBTTagList var6 = (NBTTagList)var1;
         int var7 = var6.tagCount();

         for(int var8 = 0; var8 < var7; ++var8) {
            var5 = var6.get(var8);
            if (var5 == null) {
               return true;
            }
         }
      }

      return false;
   }

   private static NBTBase getChildTag(NBTBase var0, String var1) {
      if (var0 instanceof NBTTagCompound) {
         NBTTagCompound var4 = (NBTTagCompound)var0;
         return var4.getTag(var1);
      } else if (var0 instanceof NBTTagList) {
         NBTTagList var2 = (NBTTagList)var0;
         int var3 = Config.parseInt(var1, -1);
         return var3 < 0 ? null : var2.get(var3);
      } else {
         return null;
      }
   }

   private boolean matchesPattern(String var1, String var2) {
      return StrUtils.equalsMask(var1, var2, '*', '?');
   }

   private boolean matchesRegex(String var1, String var2) {
      return var1.matches(var2);
   }

   private static String getValue(NBTBase var0) {
      if (var0 == null) {
         return null;
      } else if (var0 instanceof NBTTagString) {
         NBTTagString var7 = (NBTTagString)var0;
         return var7.getString();
      } else if (var0 instanceof NBTTagInt) {
         NBTTagInt var6 = (NBTTagInt)var0;
         return Integer.toString(var6.getInt());
      } else if (var0 instanceof NBTTagByte) {
         NBTTagByte var5 = (NBTTagByte)var0;
         return Byte.toString(var5.getByte());
      } else if (var0 instanceof NBTTagShort) {
         NBTTagShort var4 = (NBTTagShort)var0;
         return Short.toString(var4.getShort());
      } else if (var0 instanceof NBTTagLong) {
         NBTTagLong var3 = (NBTTagLong)var0;
         return Long.toString(var3.getLong());
      } else if (var0 instanceof NBTTagFloat) {
         NBTTagFloat var2 = (NBTTagFloat)var0;
         return Float.toString(var2.getFloat());
      } else if (var0 instanceof NBTTagDouble) {
         NBTTagDouble var1 = (NBTTagDouble)var0;
         return Double.toString(var1.getDouble());
      } else {
         return var0.toString();
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < this.parents.length; ++var2) {
         String var3 = this.parents[var2];
         if (var2 > 0) {
            var1.append(".");
         }

         var1.append(var3);
      }

      if (var1.length() > 0) {
         var1.append(".");
      }

      var1.append(this.name);
      var1.append(" = ");
      var1.append(this.value);
      return var1.toString();
   }
}
