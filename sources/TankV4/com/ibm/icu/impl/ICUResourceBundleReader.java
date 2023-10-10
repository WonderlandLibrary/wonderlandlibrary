package com.ibm.icu.impl;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.VersionInfo;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class ICUResourceBundleReader implements ICUBinary.Authenticate {
   private static final byte[] DATA_FORMAT_ID = new byte[]{82, 101, 115, 66};
   private static final int URES_INDEX_LENGTH = 0;
   private static final int URES_INDEX_KEYS_TOP = 1;
   private static final int URES_INDEX_BUNDLE_TOP = 3;
   private static final int URES_INDEX_ATTRIBUTES = 5;
   private static final int URES_INDEX_16BIT_TOP = 6;
   private static final int URES_INDEX_POOL_CHECKSUM = 7;
   private static final int URES_ATT_NO_FALLBACK = 1;
   private static final int URES_ATT_IS_POOL_BUNDLE = 2;
   private static final int URES_ATT_USES_POOL_BUNDLE = 4;
   private static final boolean DEBUG = false;
   private byte[] dataVersion;
   private String s16BitUnits;
   private byte[] poolBundleKeys;
   private String poolBundleKeysAsString;
   private int rootRes;
   private int localKeyLimit;
   private boolean noFallback;
   private boolean isPoolBundle;
   private boolean usesPoolBundle;
   private int[] indexes;
   private byte[] keyStrings;
   private String keyStringsAsString;
   private byte[] resourceBytes;
   private int resourceBottom;
   private static ICUResourceBundleReader.ReaderCache CACHE = new ICUResourceBundleReader.ReaderCache();
   private static final ICUResourceBundleReader NULL_READER = new ICUResourceBundleReader();
   private static byte[] emptyBytes = new byte[0];
   private static ByteBuffer emptyByteBuffer = ByteBuffer.allocate(0).asReadOnlyBuffer();
   private static char[] emptyChars = new char[0];
   private static int[] emptyInts = new int[0];
   private static String emptyString = "";
   private static final String ICU_RESOURCE_SUFFIX = ".res";

   private ICUResourceBundleReader() {
   }

   private ICUResourceBundleReader(InputStream var1, String var2, String var3, ClassLoader var4) {
      BufferedInputStream var5 = new BufferedInputStream(var1);

      try {
         this.dataVersion = ICUBinary.readHeader(var5, DATA_FORMAT_ID, this);
         this.readData(var5);
         var1.close();
      } catch (IOException var8) {
         String var7 = getFullName(var2, var3);
         throw new RuntimeException("Data file " + var7 + " is corrupt - " + var8.getMessage());
      }

      if (this.usesPoolBundle) {
         ICUResourceBundleReader var6 = getReader(var2, "pool", var4);
         if (!var6.isPoolBundle) {
            throw new IllegalStateException("pool.res is not a pool bundle");
         }

         if (var6.indexes[7] != this.indexes[7]) {
            throw new IllegalStateException("pool.res has a different checksum than this bundle");
         }

         this.poolBundleKeys = var6.keyStrings;
         this.poolBundleKeysAsString = var6.keyStringsAsString;
      }

   }

   static ICUResourceBundleReader getReader(String var0, String var1, ClassLoader var2) {
      ICUResourceBundleReader.ReaderInfo var3 = new ICUResourceBundleReader.ReaderInfo(var0, var1, var2);
      ICUResourceBundleReader var4 = (ICUResourceBundleReader)CACHE.getInstance(var3, var3);
      return var4 == NULL_READER ? null : var4;
   }

   private void readData(InputStream var1) throws IOException {
      DataInputStream var2 = new DataInputStream(var1);
      this.rootRes = var2.readInt();
      int var3 = var2.readInt();
      int var4 = var3 & 255;
      this.indexes = new int[var4];
      this.indexes[0] = var3;

      int var5;
      for(var5 = 1; var5 < var4; ++var5) {
         this.indexes[var5] = var2.readInt();
      }

      this.resourceBottom = 1 + var4 << 2;
      if (var4 > 5) {
         var5 = this.indexes[5];
         this.noFallback = (var5 & 1) != 0;
         this.isPoolBundle = (var5 & 2) != 0;
         this.usesPoolBundle = (var5 & 4) != 0;
      }

      var5 = this.indexes[3] * 4;
      int var6;
      if (this.indexes[1] > 1 + var4) {
         var6 = 1 + var4 << 2;
         int var7 = this.indexes[1] << 2;
         this.resourceBottom = var7;
         if (this.isPoolBundle) {
            var7 -= var6;
            var6 = 0;
         } else {
            this.localKeyLimit = var7;
         }

         this.keyStrings = new byte[var7];
         var2.readFully(this.keyStrings, var6, var7 - var6);
         if (this.isPoolBundle) {
            while(var6 < var7 && this.keyStrings[var7 - 1] == -86) {
               --var7;
               this.keyStrings[var7] = 0;
            }

            this.keyStringsAsString = new String(this.keyStrings, "US-ASCII");
         }
      }

      if (var4 > 6 && this.indexes[6] > this.indexes[1]) {
         var6 = (this.indexes[6] - this.indexes[1]) * 2;
         char[] var10 = new char[var6];
         byte[] var8 = new byte[var6 * 2];
         var2.readFully(var8);

         for(int var9 = 0; var9 < var6; ++var9) {
            var10[var9] = (char)(var8[var9 * 2] << 8 | var8[var9 * 2 + 1] & 255);
         }

         this.s16BitUnits = new String(var10);
         this.resourceBottom = this.indexes[6] << 2;
      } else {
         this.s16BitUnits = "\u0000";
      }

      this.resourceBytes = new byte[var5 - this.resourceBottom];
      var2.readFully(this.resourceBytes);
   }

   VersionInfo getVersion() {
      return VersionInfo.getInstance(this.dataVersion[0], this.dataVersion[1], this.dataVersion[2], this.dataVersion[3]);
   }

   public boolean isDataVersionAcceptable(byte[] var1) {
      return var1[0] == 1 && var1[1] >= 1 || var1[0] == 2;
   }

   int getRootResource() {
      return this.rootRes;
   }

   boolean getNoFallback() {
      return this.noFallback;
   }

   boolean getUsesPoolBundle() {
      return this.usesPoolBundle;
   }

   static int RES_GET_TYPE(int var0) {
      return var0 >>> 28;
   }

   private static int RES_GET_OFFSET(int var0) {
      return var0 & 268435455;
   }

   private int getResourceByteOffset(int var1) {
      return (var1 << 2) - this.resourceBottom;
   }

   static int RES_GET_INT(int var0) {
      return var0 << 4 >> 4;
   }

   static int RES_GET_UINT(int var0) {
      return var0 & 268435455;
   }

   static boolean URES_IS_TABLE(int var0) {
      return var0 == 2 || var0 == 5 || var0 == 4;
   }

   private char getChar(int var1) {
      return (char)(this.resourceBytes[var1] << 8 | this.resourceBytes[var1 + 1] & 255);
   }

   private char[] getChars(int var1, int var2) {
      char[] var3 = new char[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = (char)(this.resourceBytes[var1] << 8 | this.resourceBytes[var1 + 1] & 255);
         var1 += 2;
      }

      return var3;
   }

   private int getInt(int var1) {
      return this.resourceBytes[var1] << 24 | (this.resourceBytes[var1 + 1] & 255) << 16 | (this.resourceBytes[var1 + 2] & 255) << 8 | this.resourceBytes[var1 + 3] & 255;
   }

   private int[] getInts(int var1, int var2) {
      int[] var3 = new int[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = this.resourceBytes[var1] << 24 | (this.resourceBytes[var1 + 1] & 255) << 16 | (this.resourceBytes[var1 + 2] & 255) << 8 | this.resourceBytes[var1 + 3] & 255;
         var1 += 4;
      }

      return var3;
   }

   private char[] getTable16KeyOffsets(int var1) {
      char var2 = this.s16BitUnits.charAt(var1++);
      return var2 > 0 ? this.s16BitUnits.substring(var1, var1 + var2).toCharArray() : emptyChars;
   }

   private char[] getTableKeyOffsets(int var1) {
      char var2 = this.getChar(var1);
      return var2 > 0 ? this.getChars(var1 + 2, var2) : emptyChars;
   }

   private int[] getTable32KeyOffsets(int var1) {
      int var2 = this.getInt(var1);
      return var2 > 0 ? this.getInts(var1 + 4, var2) : emptyInts;
   }

   private String makeKeyStringFromBytes(int var1) {
      StringBuilder var2 = new StringBuilder();

      byte var3;
      while((var3 = this.keyStrings[var1++]) != 0) {
         var2.append((char)var3);
      }

      return var2.toString();
   }

   private String makeKeyStringFromString(int var1) {
      int var2;
      for(var2 = var1; this.poolBundleKeysAsString.charAt(var2) != 0; ++var2) {
      }

      return this.poolBundleKeysAsString.substring(var1, var2);
   }

   private ICUResourceBundleReader.ByteSequence RES_GET_KEY16(char var1) {
      return var1 < this.localKeyLimit ? new ICUResourceBundleReader.ByteSequence(this.keyStrings, var1) : new ICUResourceBundleReader.ByteSequence(this.poolBundleKeys, var1 - this.localKeyLimit);
   }

   private String getKey16String(int var1) {
      return var1 < this.localKeyLimit ? this.makeKeyStringFromBytes(var1) : this.makeKeyStringFromString(var1 - this.localKeyLimit);
   }

   private ICUResourceBundleReader.ByteSequence RES_GET_KEY32(int var1) {
      return var1 >= 0 ? new ICUResourceBundleReader.ByteSequence(this.keyStrings, var1) : new ICUResourceBundleReader.ByteSequence(this.poolBundleKeys, var1 & Integer.MAX_VALUE);
   }

   private String getKey32String(int var1) {
      return var1 >= 0 ? this.makeKeyStringFromBytes(var1) : this.makeKeyStringFromString(var1 & Integer.MAX_VALUE);
   }

   private static int compareKeys(CharSequence var0, ICUResourceBundleReader.ByteSequence var1) {
      int var2;
      for(var2 = 0; var2 < var0.length(); ++var2) {
         byte var3 = var1.charAt(var2);
         if (var3 == 0) {
            return 1;
         }

         int var4 = var0.charAt(var2) - var3;
         if (var4 != 0) {
            return var4;
         }
      }

      return -var1.charAt(var2);
   }

   private int compareKeys(CharSequence var1, char var2) {
      return compareKeys(var1, this.RES_GET_KEY16(var2));
   }

   private int compareKeys32(CharSequence var1, int var2) {
      return compareKeys(var1, this.RES_GET_KEY32(var2));
   }

   String getString(int var1) {
      int var2 = RES_GET_OFFSET(var1);
      int var3;
      if (RES_GET_TYPE(var1) != 6) {
         if (var1 == var2) {
            if (var1 == 0) {
               return emptyString;
            } else {
               var2 = this.getResourceByteOffset(var2);
               var3 = this.getInt(var2);
               return new String(this.getChars(var2 + 4, var3));
            }
         } else {
            return null;
         }
      } else {
         char var4 = this.s16BitUnits.charAt(var2);
         if ((var4 & -1024) == 56320) {
            if (var4 < '\udfef') {
               var3 = var4 & 1023;
               ++var2;
            } else if (var4 < '\udfff') {
               var3 = var4 - '\udfef' << 16 | this.s16BitUnits.charAt(var2 + 1);
               var2 += 2;
            } else {
               var3 = this.s16BitUnits.charAt(var2 + 1) << 16 | this.s16BitUnits.charAt(var2 + 2);
               var2 += 3;
            }

            return this.s16BitUnits.substring(var2, var2 + var3);
         } else if (var4 == 0) {
            return emptyString;
         } else {
            int var5;
            for(var5 = var2 + 1; this.s16BitUnits.charAt(var5) != 0; ++var5) {
            }

            return this.s16BitUnits.substring(var2, var5);
         }
      }
   }

   String getAlias(int var1) {
      int var2 = RES_GET_OFFSET(var1);
      if (RES_GET_TYPE(var1) == 3) {
         if (var2 == 0) {
            return emptyString;
         } else {
            var2 = this.getResourceByteOffset(var2);
            int var3 = this.getInt(var2);
            return new String(this.getChars(var2 + 4, var3));
         }
      } else {
         return null;
      }
   }

   byte[] getBinary(int var1, byte[] var2) {
      int var3 = RES_GET_OFFSET(var1);
      if (RES_GET_TYPE(var1) == 1) {
         if (var3 == 0) {
            return emptyBytes;
         } else {
            var3 = this.getResourceByteOffset(var3);
            int var4 = this.getInt(var3);
            if (var2 == null || var2.length != var4) {
               var2 = new byte[var4];
            }

            System.arraycopy(this.resourceBytes, var3 + 4, var2, 0, var4);
            return var2;
         }
      } else {
         return null;
      }
   }

   ByteBuffer getBinary(int var1) {
      int var2 = RES_GET_OFFSET(var1);
      if (RES_GET_TYPE(var1) == 1) {
         if (var2 == 0) {
            return emptyByteBuffer.duplicate();
         } else {
            var2 = this.getResourceByteOffset(var2);
            int var3 = this.getInt(var2);
            return ByteBuffer.wrap(this.resourceBytes, var2 + 4, var3).slice().asReadOnlyBuffer();
         }
      } else {
         return null;
      }
   }

   int[] getIntVector(int var1) {
      int var2 = RES_GET_OFFSET(var1);
      if (RES_GET_TYPE(var1) == 14) {
         if (var2 == 0) {
            return emptyInts;
         } else {
            var2 = this.getResourceByteOffset(var2);
            int var3 = this.getInt(var2);
            return this.getInts(var2 + 4, var3);
         }
      } else {
         return null;
      }
   }

   ICUResourceBundleReader.Container getArray(int var1) {
      int var2 = RES_GET_TYPE(var1);
      int var3 = RES_GET_OFFSET(var1);
      switch(var2) {
      case 8:
      case 9:
         if (var3 == 0) {
            return new ICUResourceBundleReader.Container(this);
         } else {
            switch(var2) {
            case 8:
               return new ICUResourceBundleReader.Array(this, var3);
            case 9:
               return new ICUResourceBundleReader.Array16(this, var3);
            default:
               return null;
            }
         }
      default:
         return null;
      }
   }

   ICUResourceBundleReader.Table getTable(int var1) {
      int var2 = RES_GET_TYPE(var1);
      int var3 = RES_GET_OFFSET(var1);
      switch(var2) {
      case 2:
      case 4:
      case 5:
         if (var3 == 0) {
            return new ICUResourceBundleReader.Table(this);
         } else {
            switch(var2) {
            case 2:
               return new ICUResourceBundleReader.Table1632(this, var3);
            case 3:
            default:
               return null;
            case 4:
               return new ICUResourceBundleReader.Table32(this, var3);
            case 5:
               return new ICUResourceBundleReader.Table16(this, var3);
            }
         }
      case 3:
      default:
         return null;
      }
   }

   public static String getFullName(String var0, String var1) {
      if (var0 != null && var0.length() != 0) {
         if (var0.indexOf(46) == -1) {
            return var0.charAt(var0.length() - 1) != '/' ? var0 + "/" + var1 + ".res" : var0 + var1 + ".res";
         } else {
            var0 = var0.replace('.', '/');
            return var1.length() == 0 ? var0 + ".res" : var0 + "_" + var1 + ".res";
         }
      } else {
         return var1.length() == 0 ? ULocale.getDefault().toString() : var1 + ".res";
      }
   }

   static ICUResourceBundleReader access$100() {
      return NULL_READER;
   }

   ICUResourceBundleReader(InputStream var1, String var2, String var3, ClassLoader var4, Object var5) {
      this(var1, var2, var3, var4);
   }

   static String access$300(ICUResourceBundleReader var0) {
      return var0.s16BitUnits;
   }

   static int access$400(ICUResourceBundleReader var0, int var1) {
      return var0.getInt(var1);
   }

   static int access$500(ICUResourceBundleReader var0, int var1) {
      return var0.getResourceByteOffset(var1);
   }

   static String access$600(ICUResourceBundleReader var0, int var1) {
      return var0.getKey16String(var1);
   }

   static String access$700(ICUResourceBundleReader var0, int var1) {
      return var0.getKey32String(var1);
   }

   static int access$800(ICUResourceBundleReader var0, CharSequence var1, char var2) {
      return var0.compareKeys(var1, var2);
   }

   static int access$900(ICUResourceBundleReader var0, CharSequence var1, int var2) {
      return var0.compareKeys32(var1, var2);
   }

   static char[] access$1000(ICUResourceBundleReader var0, int var1) {
      return var0.getTableKeyOffsets(var1);
   }

   static char[] access$1100(ICUResourceBundleReader var0, int var1) {
      return var0.getTable16KeyOffsets(var1);
   }

   static int[] access$1200(ICUResourceBundleReader var0, int var1) {
      return var0.getTable32KeyOffsets(var1);
   }

   private static final class Table32 extends ICUResourceBundleReader.Table {
      int getContainerResource(int var1) {
         return this.getContainer32Resource(var1);
      }

      Table32(ICUResourceBundleReader var1, int var2) {
         super(var1);
         var2 = ICUResourceBundleReader.access$500(var1, var2);
         this.key32Offsets = ICUResourceBundleReader.access$1200(var1, var2);
         this.size = this.key32Offsets.length;
         this.itemsOffset = var2 + 4 * (1 + this.size);
      }
   }

   private static final class Table16 extends ICUResourceBundleReader.Table {
      int getContainerResource(int var1) {
         return this.getContainer16Resource(var1);
      }

      Table16(ICUResourceBundleReader var1, int var2) {
         super(var1);
         this.keyOffsets = ICUResourceBundleReader.access$1100(var1, var2);
         this.size = this.keyOffsets.length;
         this.itemsOffset = var2 + 1 + this.size;
      }
   }

   private static final class Table1632 extends ICUResourceBundleReader.Table {
      int getContainerResource(int var1) {
         return this.getContainer32Resource(var1);
      }

      Table1632(ICUResourceBundleReader var1, int var2) {
         super(var1);
         var2 = ICUResourceBundleReader.access$500(var1, var2);
         this.keyOffsets = ICUResourceBundleReader.access$1000(var1, var2);
         this.size = this.keyOffsets.length;
         this.itemsOffset = var2 + 2 * (this.size + 2 & -2);
      }
   }

   static class Table extends ICUResourceBundleReader.Container {
      protected char[] keyOffsets;
      protected int[] key32Offsets;
      private static final int URESDATA_ITEM_NOT_FOUND = -1;

      String getKey(int var1) {
         if (var1 >= 0 && this.size > var1) {
            return this.keyOffsets != null ? ICUResourceBundleReader.access$600(this.reader, this.keyOffsets[var1]) : ICUResourceBundleReader.access$700(this.reader, this.key32Offsets[var1]);
         } else {
            return null;
         }
      }

      int findTableItem(CharSequence var1) {
         int var3 = 0;
         int var4 = this.size;

         while(var3 < var4) {
            int var2 = var3 + var4 >>> 1;
            int var5;
            if (this.keyOffsets != null) {
               var5 = ICUResourceBundleReader.access$800(this.reader, var1, this.keyOffsets[var2]);
            } else {
               var5 = ICUResourceBundleReader.access$900(this.reader, var1, this.key32Offsets[var2]);
            }

            if (var5 < 0) {
               var4 = var2;
            } else {
               if (var5 <= 0) {
                  return var2;
               }

               var3 = var2 + 1;
            }
         }

         return -1;
      }

      int getTableResource(String var1) {
         return this.getContainerResource(this.findTableItem(var1));
      }

      Table(ICUResourceBundleReader var1) {
         super(var1);
      }
   }

   private static final class Array16 extends ICUResourceBundleReader.Container {
      int getContainerResource(int var1) {
         return this.getContainer16Resource(var1);
      }

      Array16(ICUResourceBundleReader var1, int var2) {
         super(var1);
         this.size = ICUResourceBundleReader.access$300(var1).charAt(var2);
         this.itemsOffset = var2 + 1;
      }
   }

   private static final class Array extends ICUResourceBundleReader.Container {
      int getContainerResource(int var1) {
         return this.getContainer32Resource(var1);
      }

      Array(ICUResourceBundleReader var1, int var2) {
         super(var1);
         var2 = ICUResourceBundleReader.access$500(var1, var2);
         this.size = ICUResourceBundleReader.access$400(var1, var2);
         this.itemsOffset = var2 + 4;
      }
   }

   static class Container {
      protected ICUResourceBundleReader reader;
      protected int size;
      protected int itemsOffset;

      int getSize() {
         return this.size;
      }

      int getContainerResource(int var1) {
         return -1;
      }

      protected int getContainer16Resource(int var1) {
         return var1 >= 0 && this.size > var1 ? 1610612736 | ICUResourceBundleReader.access$300(this.reader).charAt(this.itemsOffset + var1) : -1;
      }

      protected int getContainer32Resource(int var1) {
         return var1 >= 0 && this.size > var1 ? ICUResourceBundleReader.access$400(this.reader, this.itemsOffset + 4 * var1) : -1;
      }

      Container(ICUResourceBundleReader var1) {
         this.reader = var1;
      }
   }

   private static final class ByteSequence {
      private byte[] bytes;
      private int offset;

      public ByteSequence(byte[] var1, int var2) {
         this.bytes = var1;
         this.offset = var2;
      }

      public byte charAt(int var1) {
         return this.bytes[this.offset + var1];
      }
   }

   private static class ReaderCache extends SoftCache {
      private ReaderCache() {
      }

      protected ICUResourceBundleReader createInstance(ICUResourceBundleReader.ReaderInfo var1, ICUResourceBundleReader.ReaderInfo var2) {
         String var3 = ICUResourceBundleReader.getFullName(var2.baseName, var2.localeID);
         InputStream var4 = ICUData.getStream(var2.loader, var3);
         return var4 == null ? ICUResourceBundleReader.access$100() : new ICUResourceBundleReader(var4, var2.baseName, var2.localeID, var2.loader);
      }

      protected Object createInstance(Object var1, Object var2) {
         return this.createInstance((ICUResourceBundleReader.ReaderInfo)var1, (ICUResourceBundleReader.ReaderInfo)var2);
      }

      ReaderCache(Object var1) {
         this();
      }
   }

   private static class ReaderInfo {
      final String baseName;
      final String localeID;
      final ClassLoader loader;

      ReaderInfo(String var1, String var2, ClassLoader var3) {
         this.baseName = var1 == null ? "" : var1;
         this.localeID = var2 == null ? "" : var2;
         this.loader = var3;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof ICUResourceBundleReader.ReaderInfo)) {
            return false;
         } else {
            ICUResourceBundleReader.ReaderInfo var2 = (ICUResourceBundleReader.ReaderInfo)var1;
            return this.baseName.equals(var2.baseName) && this.localeID.equals(var2.localeID) && this.loader.equals(var2.loader);
         }
      }

      public int hashCode() {
         return this.baseName.hashCode() ^ this.localeID.hashCode() ^ this.loader.hashCode();
      }
   }
}
