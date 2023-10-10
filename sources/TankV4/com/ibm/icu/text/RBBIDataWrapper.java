package com.ibm.icu.text;

import com.ibm.icu.impl.CharTrie;
import com.ibm.icu.impl.Trie;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

final class RBBIDataWrapper {
   RBBIDataWrapper.RBBIDataHeader fHeader;
   short[] fFTable;
   short[] fRTable;
   short[] fSFTable;
   short[] fSRTable;
   CharTrie fTrie;
   String fRuleSource;
   int[] fStatusTable;
   static final int DH_SIZE = 24;
   static final int DH_MAGIC = 0;
   static final int DH_FORMATVERSION = 1;
   static final int DH_LENGTH = 2;
   static final int DH_CATCOUNT = 3;
   static final int DH_FTABLE = 4;
   static final int DH_FTABLELEN = 5;
   static final int DH_RTABLE = 6;
   static final int DH_RTABLELEN = 7;
   static final int DH_SFTABLE = 8;
   static final int DH_SFTABLELEN = 9;
   static final int DH_SRTABLE = 10;
   static final int DH_SRTABLELEN = 11;
   static final int DH_TRIE = 12;
   static final int DH_TRIELEN = 13;
   static final int DH_RULESOURCE = 14;
   static final int DH_RULESOURCELEN = 15;
   static final int DH_STATUSTABLE = 16;
   static final int DH_STATUSTABLELEN = 17;
   static final int ACCEPTING = 0;
   static final int LOOKAHEAD = 1;
   static final int TAGIDX = 2;
   static final int RESERVED = 3;
   static final int NEXTSTATES = 4;
   static final int NUMSTATES = 0;
   static final int ROWLEN = 2;
   static final int FLAGS = 4;
   static final int RESERVED_2 = 6;
   static final int ROW_DATA = 8;
   static final int RBBI_LOOKAHEAD_HARD_BREAK = 1;
   static final int RBBI_BOF_REQUIRED = 2;
   static RBBIDataWrapper.TrieFoldingFunc fTrieFoldingFunc = new RBBIDataWrapper.TrieFoldingFunc();

   int getRowIndex(int var1) {
      return 8 + var1 * (this.fHeader.fCatCount + 4);
   }

   static RBBIDataWrapper get(InputStream var0) throws IOException {
      DataInputStream var2 = new DataInputStream(new BufferedInputStream(var0));
      RBBIDataWrapper var3 = new RBBIDataWrapper();
      var2.skip(128L);
      var3.fHeader = new RBBIDataWrapper.RBBIDataHeader();
      var3.fHeader.fMagic = var2.readInt();
      var3.fHeader.fVersion = var2.readInt();
      var3.fHeader.fFormatVersion[0] = (byte)(var3.fHeader.fVersion >> 24);
      var3.fHeader.fFormatVersion[1] = (byte)(var3.fHeader.fVersion >> 16);
      var3.fHeader.fFormatVersion[2] = (byte)(var3.fHeader.fVersion >> 8);
      var3.fHeader.fFormatVersion[3] = (byte)var3.fHeader.fVersion;
      var3.fHeader.fLength = var2.readInt();
      var3.fHeader.fCatCount = var2.readInt();
      var3.fHeader.fFTable = var2.readInt();
      var3.fHeader.fFTableLen = var2.readInt();
      var3.fHeader.fRTable = var2.readInt();
      var3.fHeader.fRTableLen = var2.readInt();
      var3.fHeader.fSFTable = var2.readInt();
      var3.fHeader.fSFTableLen = var2.readInt();
      var3.fHeader.fSRTable = var2.readInt();
      var3.fHeader.fSRTableLen = var2.readInt();
      var3.fHeader.fTrie = var2.readInt();
      var3.fHeader.fTrieLen = var2.readInt();
      var3.fHeader.fRuleSource = var2.readInt();
      var3.fHeader.fRuleSourceLen = var2.readInt();
      var3.fHeader.fStatusTable = var2.readInt();
      var3.fHeader.fStatusTableLen = var2.readInt();
      var2.skip(24L);
      if (var3.fHeader.fMagic == 45472 && (var3.fHeader.fVersion == 1 || var3.fHeader.fFormatVersion[0] == 3)) {
         byte var4 = 96;
         if (var3.fHeader.fFTable >= var4 && var3.fHeader.fFTable <= var3.fHeader.fLength) {
            var2.skip((long)(var3.fHeader.fFTable - var4));
            int var6 = var3.fHeader.fFTable;
            var3.fFTable = new short[var3.fHeader.fFTableLen / 2];

            int var1;
            for(var1 = 0; var1 < var3.fFTable.length; ++var1) {
               var3.fFTable[var1] = var2.readShort();
               var6 += 2;
            }

            var2.skip((long)(var3.fHeader.fRTable - var6));
            var6 = var3.fHeader.fRTable;
            var3.fRTable = new short[var3.fHeader.fRTableLen / 2];

            for(var1 = 0; var1 < var3.fRTable.length; ++var1) {
               var3.fRTable[var1] = var2.readShort();
               var6 += 2;
            }

            if (var3.fHeader.fSFTableLen > 0) {
               var2.skip((long)(var3.fHeader.fSFTable - var6));
               var6 = var3.fHeader.fSFTable;
               var3.fSFTable = new short[var3.fHeader.fSFTableLen / 2];

               for(var1 = 0; var1 < var3.fSFTable.length; ++var1) {
                  var3.fSFTable[var1] = var2.readShort();
                  var6 += 2;
               }
            }

            if (var3.fHeader.fSRTableLen > 0) {
               var2.skip((long)(var3.fHeader.fSRTable - var6));
               var6 = var3.fHeader.fSRTable;
               var3.fSRTable = new short[var3.fHeader.fSRTableLen / 2];

               for(var1 = 0; var1 < var3.fSRTable.length; ++var1) {
                  var3.fSRTable[var1] = var2.readShort();
                  var6 += 2;
               }
            }

            var2.skip((long)(var3.fHeader.fTrie - var6));
            var6 = var3.fHeader.fTrie;
            var2.mark(var3.fHeader.fTrieLen + 100);
            var3.fTrie = new CharTrie(var2, fTrieFoldingFunc);
            var2.reset();
            if (var6 > var3.fHeader.fStatusTable) {
               throw new IOException("Break iterator Rule data corrupt");
            } else {
               var2.skip((long)(var3.fHeader.fStatusTable - var6));
               var6 = var3.fHeader.fStatusTable;
               var3.fStatusTable = new int[var3.fHeader.fStatusTableLen / 4];

               for(var1 = 0; var1 < var3.fStatusTable.length; ++var1) {
                  var3.fStatusTable[var1] = var2.readInt();
                  var6 += 4;
               }

               if (var6 > var3.fHeader.fRuleSource) {
                  throw new IOException("Break iterator Rule data corrupt");
               } else {
                  var2.skip((long)(var3.fHeader.fRuleSource - var6));
                  var6 = var3.fHeader.fRuleSource;
                  StringBuilder var5 = new StringBuilder(var3.fHeader.fRuleSourceLen / 2);

                  for(var1 = 0; var1 < var3.fHeader.fRuleSourceLen; var1 += 2) {
                     var5.append(var2.readChar());
                     var6 += 2;
                  }

                  var3.fRuleSource = var5.toString();
                  if (RuleBasedBreakIterator.fDebugEnv != null && RuleBasedBreakIterator.fDebugEnv.indexOf("data") >= 0) {
                     var3.dump();
                  }

                  return var3;
               }
            }
         } else {
            throw new IOException("Break iterator Rule data corrupt");
         }
      } else {
         throw new IOException("Break Iterator Rule Data Magic Number Incorrect, or unsupported data version.");
      }
   }

   static final int getNumStates(short[] var0) {
      short var1 = var0[0];
      short var2 = var0[1];
      int var3 = (var1 << 16) + (var2 & '\uffff');
      return var3;
   }

   void dump() {
      if (this.fFTable.length == 0) {
         throw new NullPointerException();
      } else {
         System.out.println("RBBI Data Wrapper dump ...");
         System.out.println();
         System.out.println("Forward State Table");
         this.dumpTable(this.fFTable);
         System.out.println("Reverse State Table");
         this.dumpTable(this.fRTable);
         System.out.println("Forward Safe Points Table");
         this.dumpTable(this.fSFTable);
         System.out.println("Reverse Safe Points Table");
         this.dumpTable(this.fSRTable);
         this.dumpCharCategories();
         System.out.println("Source Rules: " + this.fRuleSource);
      }
   }

   public static String intToString(int var0, int var1) {
      StringBuilder var2 = new StringBuilder(var1);
      var2.append(var0);

      while(var2.length() < var1) {
         var2.insert(0, ' ');
      }

      return var2.toString();
   }

   public static String intToHexString(int var0, int var1) {
      StringBuilder var2 = new StringBuilder(var1);
      var2.append(Integer.toHexString(var0));

      while(var2.length() < var1) {
         var2.insert(0, ' ');
      }

      return var2.toString();
   }

   private void dumpTable(short[] var1) {
      if (var1 == null) {
         System.out.println("  -- null -- ");
      } else {
         StringBuilder var4 = new StringBuilder(" Row  Acc Look  Tag");

         int var2;
         for(var2 = 0; var2 < this.fHeader.fCatCount; ++var2) {
            var4.append(intToString(var2, 5));
         }

         System.out.println(var4.toString());

         for(var2 = 0; var2 < var4.length(); ++var2) {
            System.out.print("-");
         }

         System.out.println();

         for(int var3 = 0; var3 < getNumStates(var1); ++var3) {
            this.dumpRow(var1, var3);
         }

         System.out.println();
      }

   }

   private void dumpRow(short[] var1, int var2) {
      StringBuilder var3 = new StringBuilder(this.fHeader.fCatCount * 5 + 20);
      var3.append(intToString(var2, 4));
      int var4 = this.getRowIndex(var2);
      if (var1[var4 + 0] != 0) {
         var3.append(intToString(var1[var4 + 0], 5));
      } else {
         var3.append("     ");
      }

      if (var1[var4 + 1] != 0) {
         var3.append(intToString(var1[var4 + 1], 5));
      } else {
         var3.append("     ");
      }

      var3.append(intToString(var1[var4 + 2], 5));

      for(int var5 = 0; var5 < this.fHeader.fCatCount; ++var5) {
         var3.append(intToString(var1[var4 + 4 + var5], 5));
      }

      System.out.println(var3);
   }

   private void dumpCharCategories() {
      int var1 = this.fHeader.fCatCount;
      String[] var2 = new String[var1 + 1];
      int var3 = 0;
      int var4 = 0;
      int var5 = -1;
      int[] var8 = new int[var1 + 1];

      int var7;
      for(var7 = 0; var7 <= this.fHeader.fCatCount; ++var7) {
         var2[var7] = "";
      }

      System.out.println("\nCharacter Categories");
      System.out.println("--------------------");

      for(int var6 = 0; var6 <= 1114111; ++var6) {
         char var9 = this.fTrie.getCodePointValue(var6);
         var7 = var9 & -16385;
         if (var7 < 0 || var7 > this.fHeader.fCatCount) {
            System.out.println("Error, bad category " + Integer.toHexString(var7) + " for char " + Integer.toHexString(var6));
            break;
         }

         if (var7 == var5) {
            var4 = var6;
         } else {
            if (var5 >= 0) {
               if (var2[var5].length() > var8[var5] + 70) {
                  var8[var5] = var2[var5].length() + 10;
                  var2[var5] = var2[var5] + "\n       ";
               }

               var2[var5] = var2[var5] + " " + Integer.toHexString(var3);
               if (var4 != var3) {
                  var2[var5] = var2[var5] + "-" + Integer.toHexString(var4);
               }
            }

            var5 = var7;
            var4 = var6;
            var3 = var6;
         }
      }

      var2[var5] = var2[var5] + " " + Integer.toHexString(var3);
      if (var4 != var3) {
         var2[var5] = var2[var5] + "-" + Integer.toHexString(var4);
      }

      for(var7 = 0; var7 <= this.fHeader.fCatCount; ++var7) {
         System.out.println(intToString(var7, 5) + "  " + var2[var7]);
      }

      System.out.println();
   }

   static class TrieFoldingFunc implements Trie.DataManipulate {
      public int getFoldingOffset(int var1) {
         return (var1 & '耀') != 0 ? var1 & 32767 : 0;
      }
   }

   static final class RBBIDataHeader {
      int fMagic = 0;
      int fVersion;
      byte[] fFormatVersion = new byte[4];
      int fLength;
      int fCatCount;
      int fFTable;
      int fFTableLen;
      int fRTable;
      int fRTableLen;
      int fSFTable;
      int fSFTableLen;
      int fSRTable;
      int fSRTableLen;
      int fTrie;
      int fTrieLen;
      int fRuleSource;
      int fRuleSourceLen;
      int fStatusTable;
      int fStatusTableLen;

      public RBBIDataHeader() {
      }
   }
}
