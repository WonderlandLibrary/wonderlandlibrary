package com.ibm.icu.impl;

import com.ibm.icu.util.BytesTrie;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;

public final class UPropertyAliases {
   private static final int IX_VALUE_MAPS_OFFSET = 0;
   private static final int IX_BYTE_TRIES_OFFSET = 1;
   private static final int IX_NAME_GROUPS_OFFSET = 2;
   private static final int IX_RESERVED3_OFFSET = 3;
   private int[] valueMaps;
   private byte[] bytesTries;
   private String nameGroups;
   private static final UPropertyAliases.IsAcceptable IS_ACCEPTABLE = new UPropertyAliases.IsAcceptable();
   private static final byte[] DATA_FORMAT = new byte[]{112, 110, 97, 109};
   public static final UPropertyAliases INSTANCE;

   private void load(InputStream var1) throws IOException {
      BufferedInputStream var2 = new BufferedInputStream(var1);
      ICUBinary.readHeader(var2, DATA_FORMAT, IS_ACCEPTABLE);
      DataInputStream var3 = new DataInputStream(var2);
      int var4 = var3.readInt() / 4;
      if (var4 < 8) {
         throw new IOException("pnames.icu: not enough indexes");
      } else {
         int[] var5 = new int[var4];
         var5[0] = var4 * 4;

         int var6;
         for(var6 = 1; var6 < var4; ++var6) {
            var5[var6] = var3.readInt();
         }

         var6 = var5[0];
         int var7 = var5[1];
         int var8 = (var7 - var6) / 4;
         this.valueMaps = new int[var8];

         int var9;
         for(var9 = 0; var9 < var8; ++var9) {
            this.valueMaps[var9] = var3.readInt();
         }

         var6 = var7;
         var7 = var5[2];
         var9 = var7 - var6;
         this.bytesTries = new byte[var9];
         var3.readFully(this.bytesTries);
         var6 = var7;
         var7 = var5[3];
         var9 = var7 - var6;
         StringBuilder var10 = new StringBuilder(var9);

         for(int var11 = 0; var11 < var9; ++var11) {
            var10.append((char)var3.readByte());
         }

         this.nameGroups = var10.toString();
         var1.close();
      }
   }

   private UPropertyAliases() throws IOException {
      this.load(ICUData.getRequiredStream("data/icudt51b/pnames.icu"));
   }

   private int findProperty(int var1) {
      int var2 = 1;

      for(int var3 = this.valueMaps[0]; var3 > 0; --var3) {
         int var4 = this.valueMaps[var2];
         int var5 = this.valueMaps[var2 + 1];
         var2 += 2;
         if (var1 < var4) {
            break;
         }

         if (var1 < var5) {
            return var2 + (var1 - var4) * 2;
         }

         var2 += (var5 - var4) * 2;
      }

      return 0;
   }

   private int findPropertyValueNameGroup(int var1, int var2) {
      if (var1 == 0) {
         return 0;
      } else {
         ++var1;
         int var3 = this.valueMaps[var1++];
         int var4;
         int var5;
         if (var3 < 16) {
            while(var3 > 0) {
               var4 = this.valueMaps[var1];
               var5 = this.valueMaps[var1 + 1];
               var1 += 2;
               if (var2 < var4) {
                  break;
               }

               if (var2 < var5) {
                  return this.valueMaps[var1 + var2 - var4];
               }

               var1 += var5 - var4;
               --var3;
            }
         } else {
            var4 = var1;
            var5 = var1 + var3 - 16;

            do {
               int var6 = this.valueMaps[var1];
               if (var2 < var6) {
                  break;
               }

               if (var2 == var6) {
                  return this.valueMaps[var5 + var1 - var4];
               }

               ++var1;
            } while(var1 < var5);
         }

         return 0;
      }
   }

   private String getName(int var1, int var2) {
      char var3 = this.nameGroups.charAt(var1++);
      if (var2 >= 0 && var3 > var2) {
         while(var2 > 0) {
            while(0 != this.nameGroups.charAt(var1++)) {
            }

            --var2;
         }

         int var4;
         for(var4 = var1; 0 != this.nameGroups.charAt(var1); ++var1) {
         }

         if (var4 == var1) {
            return null;
         } else {
            return this.nameGroups.substring(var4, var1);
         }
      } else {
         throw new IllegalIcuArgumentException("Invalid property (value) name choice");
      }
   }

   private static int asciiToLowercase(int var0) {
      return 65 <= var0 && var0 <= 90 ? var0 + 32 : var0;
   }

   public String getPropertyName(int var1, int var2) {
      int var3 = this.findProperty(var1);
      if (var3 == 0) {
         throw new IllegalArgumentException("Invalid property enum " + var1 + " (0x" + Integer.toHexString(var1) + ")");
      } else {
         return this.getName(this.valueMaps[var3], var2);
      }
   }

   public String getPropertyValueName(int var1, int var2, int var3) {
      int var4 = this.findProperty(var1);
      if (var4 == 0) {
         throw new IllegalArgumentException("Invalid property enum " + var1 + " (0x" + Integer.toHexString(var1) + ")");
      } else {
         int var5 = this.findPropertyValueNameGroup(this.valueMaps[var4 + 1], var2);
         if (var5 == 0) {
            throw new IllegalArgumentException("Property " + var1 + " (0x" + Integer.toHexString(var1) + ") does not have named values");
         } else {
            return this.getName(var5, var3);
         }
      }
   }

   private int getPropertyOrValueEnum(int var1, CharSequence var2) {
      BytesTrie var3 = new BytesTrie(this.bytesTries, var1);
      return var3 < var2 ? var3.getValue() : -1;
   }

   public int getPropertyEnum(CharSequence var1) {
      return this.getPropertyOrValueEnum(0, var1);
   }

   public int getPropertyValueEnum(int var1, CharSequence var2) {
      int var3 = this.findProperty(var1);
      if (var3 == 0) {
         throw new IllegalArgumentException("Invalid property enum " + var1 + " (0x" + Integer.toHexString(var1) + ")");
      } else {
         var3 = this.valueMaps[var3 + 1];
         if (var3 == 0) {
            throw new IllegalArgumentException("Property " + var1 + " (0x" + Integer.toHexString(var1) + ") does not have named values");
         } else {
            return this.getPropertyOrValueEnum(this.valueMaps[var3], var2);
         }
      }
   }

   public static int compare(String var0, String var1) {
      int var2 = 0;
      int var3 = 0;
      char var5 = 0;
      char var6 = 0;

      while(true) {
         while(true) {
            if (var2 < var0.length()) {
               var5 = var0.charAt(var2);
               switch(var5) {
               case '\t':
               case '\n':
               case '\u000b':
               case '\f':
               case '\r':
               case ' ':
               case '-':
               case '_':
                  ++var2;
                  continue;
               }
            }

            label52:
            while(var3 < var1.length()) {
               var6 = var1.charAt(var3);
               switch(var6) {
               case '\t':
               case '\n':
               case '\u000b':
               case '\f':
               case '\r':
               case ' ':
               case '-':
               case '_':
                  ++var3;
                  break;
               default:
                  break label52;
               }
            }

            boolean var7 = var2 == var0.length();
            boolean var8 = var3 == var1.length();
            if (var7) {
               if (var8) {
                  return 0;
               }

               var5 = 0;
            } else if (var8) {
               var6 = 0;
            }

            int var4 = asciiToLowercase(var5) - asciiToLowercase(var6);
            if (var4 != 0) {
               return var4;
            }

            ++var2;
            ++var3;
         }
      }
   }

   static {
      try {
         INSTANCE = new UPropertyAliases();
      } catch (IOException var2) {
         MissingResourceException var1 = new MissingResourceException("Could not construct UPropertyAliases. Missing pnames.icu", "", "");
         var1.initCause(var2);
         throw var1;
      }
   }

   private static final class IsAcceptable implements ICUBinary.Authenticate {
      private IsAcceptable() {
      }

      public boolean isDataVersionAcceptable(byte[] var1) {
         return var1[0] == 2;
      }

      IsAcceptable(Object var1) {
         this();
      }
   }
}
