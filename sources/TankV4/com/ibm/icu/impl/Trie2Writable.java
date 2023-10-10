package com.ibm.icu.impl;

import java.util.Iterator;

public class Trie2Writable extends Trie2 {
   private static final int UTRIE2_MAX_INDEX_LENGTH = 65535;
   private static final int UTRIE2_MAX_DATA_LENGTH = 262140;
   private static final int UNEWTRIE2_INITIAL_DATA_LENGTH = 16384;
   private static final int UNEWTRIE2_MEDIUM_DATA_LENGTH = 131072;
   private static final int UNEWTRIE2_INDEX_2_NULL_OFFSET = 2656;
   private static final int UNEWTRIE2_INDEX_2_START_OFFSET = 2720;
   private static final int UNEWTRIE2_DATA_NULL_OFFSET = 192;
   private static final int UNEWTRIE2_DATA_START_OFFSET = 256;
   private static final int UNEWTRIE2_DATA_0800_OFFSET = 2176;
   private int[] index1 = new int[544];
   private int[] index2 = new int['誠'];
   private int[] data;
   private int index2Length;
   private int dataCapacity;
   private int firstFreeBlock;
   private int index2NullOffset;
   private boolean isCompacted;
   private int[] map = new int['蠤'];
   private boolean UTRIE2_DEBUG = false;
   static final boolean $assertionsDisabled = !Trie2Writable.class.desiredAssertionStatus();

   public Trie2Writable(int var1, int var2) {
      this.init(var1, var2);
   }

   private void init(int var1, int var2) {
      this.initialValue = var1;
      this.errorValue = var2;
      this.highStart = 1114112;
      this.data = new int[16384];
      this.dataCapacity = 16384;
      this.initialValue = var1;
      this.errorValue = var2;
      this.highStart = 1114112;
      this.firstFreeBlock = 0;
      this.isCompacted = false;

      int var3;
      for(var3 = 0; var3 < 128; ++var3) {
         this.data[var3] = this.initialValue;
      }

      while(var3 < 192) {
         this.data[var3] = this.errorValue;
         ++var3;
      }

      for(var3 = 192; var3 < 256; ++var3) {
         this.data[var3] = this.initialValue;
      }

      this.dataNullOffset = 192;
      this.dataLength = 256;
      var3 = 0;

      int var4;
      for(var4 = 0; var4 < 128; var4 += 32) {
         this.index2[var3] = var4;
         this.map[var3] = 1;
         ++var3;
      }

      while(var4 < 192) {
         this.map[var3] = 0;
         ++var3;
         var4 += 32;
      }

      this.map[var3++] = 34845;

      for(var4 += 32; var4 < 256; var4 += 32) {
         this.map[var3] = 0;
         ++var3;
      }

      for(var3 = 4; var3 < 2080; ++var3) {
         this.index2[var3] = 192;
      }

      for(var3 = 0; var3 < 576; ++var3) {
         this.index2[2080 + var3] = -1;
      }

      for(var3 = 0; var3 < 64; ++var3) {
         this.index2[2656 + var3] = 192;
      }

      this.index2NullOffset = 2656;
      this.index2Length = 2720;
      var3 = 0;

      for(var4 = 0; var3 < 32; var4 += 64) {
         this.index1[var3] = var4;
         ++var3;
      }

      while(var3 < 544) {
         this.index1[var3] = 2656;
         ++var3;
      }

      for(var3 = 128; var3 < 2048; var3 += 32) {
         this.set(var3, this.initialValue);
      }

   }

   public Trie2Writable(Trie2 var1) {
      this.init(var1.initialValue, var1.errorValue);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Trie2.Range var3 = (Trie2.Range)var2.next();
         this.setRange(var3, true);
      }

   }

   private int allocIndex2Block() {
      int var1 = this.index2Length;
      int var2 = var1 + 64;
      if (var2 > this.index2.length) {
         throw new IllegalStateException("Internal error in Trie2 creation.");
      } else {
         this.index2Length = var2;
         System.arraycopy(this.index2, this.index2NullOffset, this.index2, var1, 64);
         return var1;
      }
   }

   private int getIndex2Block(int var1, boolean var2) {
      if (var1 >= 55296 && var1 < 56320 && var2) {
         return 2048;
      } else {
         int var3 = var1 >> 11;
         int var4 = this.index1[var3];
         if (var4 == this.index2NullOffset) {
            var4 = this.allocIndex2Block();
            this.index1[var3] = var4;
         }

         return var4;
      }
   }

   private int allocDataBlock(int var1) {
      int var2;
      if (this.firstFreeBlock != 0) {
         var2 = this.firstFreeBlock;
         this.firstFreeBlock = -this.map[var2 >> 5];
      } else {
         var2 = this.dataLength;
         int var3 = var2 + 32;
         if (var3 > this.dataCapacity) {
            int var4;
            if (this.dataCapacity < 131072) {
               var4 = 131072;
            } else {
               if (this.dataCapacity >= 1115264) {
                  throw new IllegalStateException("Internal error in Trie2 creation.");
               }

               var4 = 1115264;
            }

            int[] var5 = new int[var4];
            System.arraycopy(this.data, 0, var5, 0, this.dataLength);
            this.data = var5;
            this.dataCapacity = var4;
         }

         this.dataLength = var3;
      }

      System.arraycopy(this.data, var1, this.data, var2, 32);
      this.map[var2 >> 5] = 0;
      return var2;
   }

   private void releaseDataBlock(int var1) {
      this.map[var1 >> 5] = -this.firstFreeBlock;
      this.firstFreeBlock = var1;
   }

   private void setIndex2Entry(int var1, int var2) {
      int var10002 = this.map[var2 >> 5]++;
      int var3 = this.index2[var1];
      if (0 == --this.map[var3 >> 5]) {
         this.releaseDataBlock(var3);
      }

      this.index2[var1] = var2;
   }

   private int getDataBlock(int var1, boolean var2) {
      int var3 = this.getIndex2Block(var1, var2);
      var3 += var1 >> 5 & 63;
      int var4 = this.index2[var3];
      if (this != var4) {
         return var4;
      } else {
         int var5 = this.allocDataBlock(var4);
         this.setIndex2Entry(var3, var5);
         return var5;
      }
   }

   public Trie2Writable set(int var1, int var2) {
      if (var1 >= 0 && var1 <= 1114111) {
         this.set(var1, true, var2);
         this.fHash = 0;
         return this;
      } else {
         throw new IllegalArgumentException("Invalid code point.");
      }
   }

   private Trie2Writable set(int var1, boolean var2, int var3) {
      if (this.isCompacted) {
         this.uncompact();
      }

      int var4 = this.getDataBlock(var1, var2);
      this.data[var4 + (var1 & 31)] = var3;
      return this;
   }

   private void uncompact() {
      Trie2Writable var1 = new Trie2Writable(this);
      this.index1 = var1.index1;
      this.index2 = var1.index2;
      this.data = var1.data;
      this.index2Length = var1.index2Length;
      this.dataCapacity = var1.dataCapacity;
      this.isCompacted = var1.isCompacted;
      this.header = var1.header;
      this.index = var1.index;
      this.data16 = var1.data16;
      this.data32 = var1.data32;
      this.indexLength = var1.indexLength;
      this.dataLength = var1.dataLength;
      this.index2NullOffset = var1.index2NullOffset;
      this.initialValue = var1.initialValue;
      this.errorValue = var1.errorValue;
      this.highStart = var1.highStart;
      this.highValueIndex = var1.highValueIndex;
      this.dataNullOffset = var1.dataNullOffset;
   }

   private void writeBlock(int var1, int var2) {
      for(int var3 = var1 + 32; var1 < var3; this.data[var1++] = var2) {
      }

   }

   private void fillBlock(int var1, int var2, int var3, int var4, int var5, boolean var6) {
      int var8 = var1 + var3;
      int var7;
      if (var6) {
         for(var7 = var1 + var2; var7 < var8; ++var7) {
            this.data[var7] = var4;
         }
      } else {
         for(var7 = var1 + var2; var7 < var8; ++var7) {
            if (this.data[var7] == var5) {
               this.data[var7] = var4;
            }
         }
      }

   }

   public Trie2Writable setRange(int var1, int var2, int var3, boolean var4) {
      if (var1 <= 1114111 && var1 >= 0 && var2 <= 1114111 && var2 >= 0 && var1 <= var2) {
         if (!var4 && var3 == this.initialValue) {
            return this;
         } else {
            this.fHash = 0;
            if (this.isCompacted) {
               this.uncompact();
            }

            int var8 = var2 + 1;
            int var5;
            int var9;
            if ((var1 & 31) != 0) {
               var5 = this.getDataBlock(var1, true);
               var9 = var1 + 32 & -32;
               if (var9 > var8) {
                  this.fillBlock(var5, var1 & 31, var8 & 31, var3, this.initialValue, var4);
                  return this;
               }

               this.fillBlock(var5, var1 & 31, 32, var3, this.initialValue, var4);
               var1 = var9;
            }

            int var6 = var8 & 31;
            var8 &= -32;
            int var7;
            if (var3 == this.initialValue) {
               var7 = this.dataNullOffset;
            } else {
               var7 = -1;
            }

            while(true) {
               while(var1 < var8) {
                  boolean var10 = false;
                  if (var3 == this.initialValue) {
                     var1 += 32;
                  } else {
                     var9 = this.getIndex2Block(var1, true);
                     var9 += var1 >> 5 & 63;
                     var5 = this.index2[var9];
                     if (this != var5) {
                        if (var4 && var5 >= 2176) {
                           var10 = true;
                        } else {
                           this.fillBlock(var5, 0, 32, var3, this.initialValue, var4);
                        }
                     } else if (this.data[var5] != var3 && (var4 || var5 == this.dataNullOffset)) {
                        var10 = true;
                     }

                     if (var10) {
                        if (var7 >= 0) {
                           this.setIndex2Entry(var9, var7);
                        } else {
                           var7 = this.getDataBlock(var1, true);
                           this.writeBlock(var7, var3);
                        }
                     }

                     var1 += 32;
                  }
               }

               if (var6 > 0) {
                  var5 = this.getDataBlock(var1, true);
                  this.fillBlock(var5, 0, var6, var3, this.initialValue, var4);
               }

               return this;
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid code point range.");
      }
   }

   public Trie2Writable setRange(Trie2.Range var1, boolean var2) {
      this.fHash = 0;
      if (var1.leadSurrogate) {
         for(int var3 = var1.startCodePoint; var3 <= var1.endCodePoint; ++var3) {
            if (var2 || this.getFromU16SingleLead((char)var3) == this.initialValue) {
               this.setForLeadSurrogateCodeUnit((char)var3, var1.value);
            }
         }
      } else {
         this.setRange(var1.startCodePoint, var1.endCodePoint, var1.value, var2);
      }

      return this;
   }

   public Trie2Writable setForLeadSurrogateCodeUnit(char var1, int var2) {
      this.fHash = 0;
      this.set(var1, false, var2);
      return this;
   }

   public int get(int var1) {
      return var1 >= 0 && var1 <= 1114111 ? this.get(var1, true) : this.errorValue;
   }

   private int get(int var1, boolean var2) {
      if (var1 >= this.highStart && (var1 < 55296 || var1 >= 56320 || var2)) {
         return this.data[this.dataLength - 4];
      } else {
         int var3;
         if (var1 >= 55296 && var1 < 56320 && var2) {
            var3 = 320 + (var1 >> 5);
         } else {
            var3 = this.index1[var1 >> 11] + (var1 >> 5 & 63);
         }

         int var4 = this.index2[var3];
         return this.data[var4 + (var1 & 31)];
      }
   }

   public int getFromU16SingleLead(char var1) {
      return this.get(var1, false);
   }

   private int findSameIndex2Block(int var1, int var2) {
      var1 -= 64;

      for(int var3 = 0; var3 <= var1; ++var3) {
         int[] var10001 = this.index2;
         if (var2 < 64) {
            return var3;
         }
      }

      return -1;
   }

   private int findSameDataBlock(int var1, int var2, int var3) {
      var1 -= var3;

      for(int var4 = 0; var4 <= var1; var4 += 4) {
         int[] var10001 = this.data;
         if (var2 < var3) {
            return var4;
         }
      }

      return -1;
   }

   private int findHighStart(int var1) {
      int var9;
      int var11;
      if (var1 == this.initialValue) {
         var9 = this.index2NullOffset;
         var11 = this.dataNullOffset;
      } else {
         var9 = -1;
         var11 = -1;
      }

      int var4 = 1114112;
      int var5 = 544;
      int var3 = var4;

      while(true) {
         label59:
         while(var3 > 0) {
            --var5;
            int var8 = this.index1[var5];
            if (var8 == var9) {
               var3 -= 2048;
            } else {
               var9 = var8;
               if (var8 == this.index2NullOffset) {
                  if (var1 != this.initialValue) {
                     return var3;
                  }

                  var3 -= 2048;
               } else {
                  int var6 = 64;

                  while(true) {
                     while(true) {
                        if (var6 <= 0) {
                           continue label59;
                        }

                        --var6;
                        int var10 = this.index2[var8 + var6];
                        if (var10 == var11) {
                           var3 -= 32;
                        } else {
                           var11 = var10;
                           if (var10 == this.dataNullOffset) {
                              if (var1 != this.initialValue) {
                                 return var3;
                              }

                              var3 -= 32;
                           } else {
                              for(int var7 = 32; var7 > 0; --var3) {
                                 --var7;
                                 int var2 = this.data[var10 + var7];
                                 if (var2 != var1) {
                                    return var3;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return 0;
      }
   }

   private void compactData() {
      int var2 = 192;
      int var1 = 0;

      int var6;
      for(var6 = 0; var1 < var2; ++var6) {
         this.map[var6] = var1;
         var1 += 32;
      }

      byte var4 = 64;
      int var8 = var4 >> 5;
      var1 = var2;

      while(var1 < this.dataLength) {
         if (var1 == 2176) {
            var4 = 32;
            var8 = 1;
         }

         if (this.map[var1 >> 5] <= 0) {
            var1 += var4;
         } else {
            int var3 = this.findSameDataBlock(var2, var1, var4);
            int var7;
            if (var3 >= 0) {
               var6 = var8;

               for(var7 = var1 >> 5; var6 > 0; --var6) {
                  this.map[var7++] = var3;
                  var3 += 32;
               }

               var1 += var4;
            } else {
               int var5;
               for(var5 = var4 - 4; var5 > 0; var5 -= 4) {
                  int[] var10001 = this.data;
                  int var10002 = var2 - var5;
                  if (var1 >= var5) {
                     break;
                  }
               }

               if (var5 <= 0 && var2 >= var1) {
                  var6 = var8;

                  for(var7 = var1 >> 5; var6 > 0; --var6) {
                     this.map[var7++] = var1;
                     var1 += 32;
                  }

                  var2 = var1;
               } else {
                  var3 = var2 - var5;
                  var6 = var8;

                  for(var7 = var1 >> 5; var6 > 0; --var6) {
                     this.map[var7++] = var3;
                     var3 += 32;
                  }

                  var1 += var5;

                  for(var6 = var4 - var5; var6 > 0; --var6) {
                     this.data[var2++] = this.data[var1++];
                  }
               }
            }
         }
      }

      for(var6 = 0; var6 < this.index2Length; ++var6) {
         if (var6 == 2080) {
            var6 += 576;
         }

         this.index2[var6] = this.map[this.index2[var6] >> 5];
      }

      for(this.dataNullOffset = this.map[this.dataNullOffset >> 5]; (var2 & 3) != 0; this.data[var2++] = this.initialValue) {
      }

      if (this.UTRIE2_DEBUG) {
         System.out.printf("compacting UTrie2: count of 32-bit data words %d->%d\n", this.dataLength, var2);
      }

      this.dataLength = var2;
   }

   private void compactIndex2() {
      short var3 = 2080;
      int var2 = 0;

      int var1;
      for(var1 = 0; var2 < var3; ++var1) {
         this.map[var1] = var2;
         var2 += 64;
      }

      int var6 = var3 + 32 + (this.highStart - 65536 >> 11);
      var2 = 2656;

      while(true) {
         while(var2 < this.index2Length) {
            int var4;
            if ((var4 = this.findSameIndex2Block(var6, var2)) >= 0) {
               this.map[var2 >> 6] = var4;
               var2 += 64;
            } else {
               int var5;
               for(var5 = 63; var5 > 0; --var5) {
                  int[] var10001 = this.index2;
                  int var10002 = var6 - var5;
                  if (var2 >= var5) {
                     break;
                  }
               }

               if (var5 <= 0 && var6 >= var2) {
                  this.map[var2 >> 6] = var2;
                  var2 += 64;
                  var6 = var2;
               } else {
                  this.map[var2 >> 6] = var6 - var5;
                  var2 += var5;

                  for(var1 = 64 - var5; var1 > 0; --var1) {
                     this.index2[var6++] = this.index2[var2++];
                  }
               }
            }
         }

         for(var1 = 0; var1 < 544; ++var1) {
            this.index1[var1] = this.map[this.index1[var1] >> 6];
         }

         for(this.index2NullOffset = this.map[this.index2NullOffset >> 6]; (var6 & 3) != 0; this.index2[var6++] = 262140) {
         }

         if (this.UTRIE2_DEBUG) {
            System.out.printf("compacting UTrie2: count of 16-bit index-2 words %d->%d\n", this.index2Length, var6);
         }

         this.index2Length = var6;
         return;
      }
   }

   private void compactTrie() {
      int var3 = this.get(1114111);
      int var1 = this.findHighStart(var3);
      var1 = var1 + 2047 & -2048;
      if (var1 == 1114112) {
         var3 = this.errorValue;
      }

      this.highStart = var1;
      if (this.UTRIE2_DEBUG) {
         System.out.printf("UTrie2: highStart U+%04x  highValue 0x%x  initialValue 0x%x\n", this.highStart, var3, this.initialValue);
      }

      if (this.highStart < 1114112) {
         int var2 = this.highStart <= 65536 ? 65536 : this.highStart;
         this.setRange(var2, 1114111, this.initialValue, true);
      }

      this.compactData();
      if (this.highStart > 65536) {
         this.compactIndex2();
      } else if (this.UTRIE2_DEBUG) {
         System.out.printf("UTrie2: highStart U+%04x  count of 16-bit index-2 words %d->%d\n", this.highStart, this.index2Length, 2112);
      }

      for(this.data[this.dataLength++] = var3; (this.dataLength & 3) != 0; this.data[this.dataLength++] = this.initialValue) {
      }

      this.isCompacted = true;
   }

   public Trie2_16 toTrie2_16() {
      Trie2_16 var1 = new Trie2_16();
      this.freeze(var1, Trie2.ValueWidth.BITS_16);
      return var1;
   }

   public Trie2_32 toTrie2_32() {
      Trie2_32 var1 = new Trie2_32();
      this.freeze(var1, Trie2.ValueWidth.BITS_32);
      return var1;
   }

   private void freeze(Trie2 var1, Trie2.ValueWidth var2) {
      if (!this.isCompacted) {
         this.compactTrie();
      }

      int var4;
      if (this.highStart <= 65536) {
         var4 = 2112;
      } else {
         var4 = this.index2Length;
      }

      int var5;
      if (var2 == Trie2.ValueWidth.BITS_16) {
         var5 = var4;
      } else {
         var5 = 0;
      }

      if (var4 <= 65535 && var5 + this.dataNullOffset <= 65535 && var5 + 2176 <= 65535 && var5 + this.dataLength <= 262140) {
         int var6 = var4;
         if (var2 == Trie2.ValueWidth.BITS_16) {
            var6 = var4 + this.dataLength;
         } else {
            var1.data32 = new int[this.dataLength];
         }

         var1.index = new char[var6];
         var1.indexLength = var4;
         var1.dataLength = this.dataLength;
         if (this.highStart <= 65536) {
            var1.index2NullOffset = 65535;
         } else {
            var1.index2NullOffset = 0 + this.index2NullOffset;
         }

         var1.initialValue = this.initialValue;
         var1.errorValue = this.errorValue;
         var1.highStart = this.highStart;
         var1.highValueIndex = var5 + this.dataLength - 4;
         var1.dataNullOffset = var5 + this.dataNullOffset;
         var1.header = new Trie2.UTrie2Header();
         var1.header.signature = 1416784178;
         var1.header.options = var2 == Trie2.ValueWidth.BITS_16 ? 0 : 1;
         var1.header.indexLength = var1.indexLength;
         var1.header.shiftedDataLength = var1.dataLength >> 2;
         var1.header.index2NullOffset = var1.index2NullOffset;
         var1.header.dataNullOffset = var1.dataNullOffset;
         var1.header.shiftedHighStart = var1.highStart >> 11;
         int var7 = 0;

         int var3;
         for(var3 = 0; var3 < 2080; ++var3) {
            var1.index[var7++] = (char)(this.index2[var3] + var5 >> 2);
         }

         if (this.UTRIE2_DEBUG) {
            System.out.println("\n\nIndex2 for BMP limit is " + Integer.toHexString(var7));
         }

         for(var3 = 0; var3 < 2; ++var3) {
            var1.index[var7++] = (char)(var5 + 128);
         }

         while(var3 < 32) {
            var1.index[var7++] = (char)(var5 + this.index2[var3 << 1]);
            ++var3;
         }

         if (this.UTRIE2_DEBUG) {
            System.out.println("Index2 for UTF-8 2byte values limit is " + Integer.toHexString(var7));
         }

         if (this.highStart > 65536) {
            int var8 = this.highStart - 65536 >> 11;
            int var9 = 2112 + var8;

            for(var3 = 0; var3 < var8; ++var3) {
               var1.index[var7++] = (char)(0 + this.index1[var3 + 32]);
            }

            if (this.UTRIE2_DEBUG) {
               System.out.println("Index 1 for supplementals, limit is " + Integer.toHexString(var7));
            }

            for(var3 = 0; var3 < this.index2Length - var9; ++var3) {
               var1.index[var7++] = (char)(var5 + this.index2[var9 + var3] >> 2);
            }

            if (this.UTRIE2_DEBUG) {
               System.out.println("Index 2 for supplementals, limit is " + Integer.toHexString(var7));
            }
         }

         switch(var2) {
         case BITS_16:
            if (!$assertionsDisabled && var7 != var5) {
               throw new AssertionError();
            }

            var1.data16 = var7;

            for(var3 = 0; var3 < this.dataLength; ++var3) {
               var1.index[var7++] = (char)this.data[var3];
            }

            return;
         case BITS_32:
            for(var3 = 0; var3 < this.dataLength; ++var3) {
               var1.data32[var3] = this.data[var3];
            }
         }

      } else {
         throw new UnsupportedOperationException("Trie2 data is too large.");
      }
   }
}
