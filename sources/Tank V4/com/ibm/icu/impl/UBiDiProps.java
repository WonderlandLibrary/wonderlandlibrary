package com.ibm.icu.impl;

import com.ibm.icu.text.UnicodeSet;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public final class UBiDiProps {
   private int[] indexes;
   private int[] mirrors;
   private byte[] jgArray;
   private Trie2_16 trie;
   private static final String DATA_NAME = "ubidi";
   private static final String DATA_TYPE = "icu";
   private static final String DATA_FILE_NAME = "ubidi.icu";
   private static final byte[] FMT = new byte[]{66, 105, 68, 105};
   private static final int IX_TRIE_SIZE = 2;
   private static final int IX_MIRROR_LENGTH = 3;
   private static final int IX_JG_START = 4;
   private static final int IX_JG_LIMIT = 5;
   private static final int IX_MAX_VALUES = 15;
   private static final int IX_TOP = 16;
   private static final int JT_SHIFT = 5;
   private static final int JOIN_CONTROL_SHIFT = 10;
   private static final int BIDI_CONTROL_SHIFT = 11;
   private static final int IS_MIRRORED_SHIFT = 12;
   private static final int MIRROR_DELTA_SHIFT = 13;
   private static final int MAX_JG_SHIFT = 16;
   private static final int CLASS_MASK = 31;
   private static final int JT_MASK = 224;
   private static final int MAX_JG_MASK = 16711680;
   private static final int ESC_MIRROR_DELTA = -4;
   private static final int MIRROR_INDEX_SHIFT = 21;
   public static final UBiDiProps INSTANCE;

   private UBiDiProps() throws IOException {
      InputStream var1 = ICUData.getStream("data/icudt51b/ubidi.icu");
      BufferedInputStream var2 = new BufferedInputStream(var1, 4096);
      this.readData(var2);
      var2.close();
      var1.close();
   }

   private void readData(InputStream var1) throws IOException {
      DataInputStream var2 = new DataInputStream(var1);
      ICUBinary.readHeader(var2, FMT, new UBiDiProps.IsAcceptable());
      int var4 = var2.readInt();
      if (var4 < 16) {
         throw new IOException("indexes[0] too small in ubidi.icu");
      } else {
         this.indexes = new int[var4];
         this.indexes[0] = var4;

         int var3;
         for(var3 = 1; var3 < var4; ++var3) {
            this.indexes[var3] = var2.readInt();
         }

         this.trie = Trie2_16.createFromSerialized(var2);
         int var5 = this.indexes[2];
         int var6 = this.trie.getSerializedLength();
         if (var6 > var5) {
            throw new IOException("ubidi.icu: not enough bytes for the trie");
         } else {
            var2.skipBytes(var5 - var6);
            var4 = this.indexes[3];
            if (var4 > 0) {
               this.mirrors = new int[var4];

               for(var3 = 0; var3 < var4; ++var3) {
                  this.mirrors[var3] = var2.readInt();
               }
            }

            var4 = this.indexes[5] - this.indexes[4];
            this.jgArray = new byte[var4];

            for(var3 = 0; var3 < var4; ++var3) {
               this.jgArray[var3] = var2.readByte();
            }

         }
      }
   }

   public final void addPropertyStarts(UnicodeSet var1) {
      Iterator var9 = this.trie.iterator();

      Trie2.Range var10;
      while(var9.hasNext() && !(var10 = (Trie2.Range)var9.next()).leadSurrogate) {
         var1.add(var10.startCodePoint);
      }

      int var3 = this.indexes[3];

      int var2;
      for(var2 = 0; var2 < var3; ++var2) {
         int var4 = getMirrorCodePoint(this.mirrors[var2]);
         var1.add(var4, var4 + 1);
      }

      int var5 = this.indexes[4];
      int var6 = this.indexes[5];
      var3 = var6 - var5;
      byte var7 = 0;

      for(var2 = 0; var2 < var3; ++var2) {
         byte var8 = this.jgArray[var2];
         if (var8 != var7) {
            var1.add(var5);
            var7 = var8;
         }

         ++var5;
      }

      if (var7 != 0) {
         var1.add(var6);
      }

   }

   public final int getMaxValue(int var1) {
      int var2 = this.indexes[15];
      switch(var1) {
      case 4096:
         return var2 & 31;
      case 4102:
         return (var2 & 16711680) >> 16;
      case 4103:
         return (var2 & 224) >> 5;
      default:
         return -1;
      }
   }

   public final int getClass(int var1) {
      return getClassFromProps(this.trie.get(var1));
   }

   public final boolean isMirrored(int var1) {
      return getFlagFromProps(this.trie.get(var1), 12);
   }

   public final int getMirror(int var1) {
      int var2 = this.trie.get(var1);
      int var3 = (short)var2 >> 13;
      if (var3 != -4) {
         return var1 + var3;
      } else {
         int var6 = this.indexes[3];

         for(int var5 = 0; var5 < var6; ++var5) {
            int var4 = this.mirrors[var5];
            int var7 = getMirrorCodePoint(var4);
            if (var1 == var7) {
               return getMirrorCodePoint(this.mirrors[getMirrorIndex(var4)]);
            }

            if (var1 < var7) {
               break;
            }
         }

         return var1;
      }
   }

   public final boolean isBidiControl(int var1) {
      return getFlagFromProps(this.trie.get(var1), 11);
   }

   public final boolean isJoinControl(int var1) {
      return getFlagFromProps(this.trie.get(var1), 10);
   }

   public final int getJoiningType(int var1) {
      return (this.trie.get(var1) & 224) >> 5;
   }

   public final int getJoiningGroup(int var1) {
      int var2 = this.indexes[4];
      int var3 = this.indexes[5];
      return var2 <= var1 && var1 < var3 ? this.jgArray[var1 - var2] & 255 : 0;
   }

   private static final int getClassFromProps(int var0) {
      return var0 & 31;
   }

   private static final boolean getFlagFromProps(int var0, int var1) {
      return (var0 >> var1 & 1) != 0;
   }

   private static final int getMirrorCodePoint(int var0) {
      return var0 & 2097151;
   }

   private static final int getMirrorIndex(int var0) {
      return var0 >>> 21;
   }

   static {
      try {
         INSTANCE = new UBiDiProps();
      } catch (IOException var1) {
         throw new RuntimeException(var1);
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
