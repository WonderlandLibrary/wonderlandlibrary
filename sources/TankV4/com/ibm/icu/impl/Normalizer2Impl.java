package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.VersionInfo;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public final class Normalizer2Impl {
   private static final Normalizer2Impl.IsAcceptable IS_ACCEPTABLE = new Normalizer2Impl.IsAcceptable();
   private static final byte[] DATA_FORMAT = new byte[]{78, 114, 109, 50};
   private static final Trie2.ValueMapper segmentStarterMapper = new Trie2.ValueMapper() {
      public int map(int var1) {
         return var1 & Integer.MIN_VALUE;
      }
   };
   public static final int MIN_CCC_LCCC_CP = 768;
   public static final int MIN_YES_YES_WITH_CC = 65281;
   public static final int JAMO_VT = 65280;
   public static final int MIN_NORMAL_MAYBE_YES = 65024;
   public static final int JAMO_L = 1;
   public static final int MAX_DELTA = 64;
   public static final int IX_NORM_TRIE_OFFSET = 0;
   public static final int IX_EXTRA_DATA_OFFSET = 1;
   public static final int IX_SMALL_FCD_OFFSET = 2;
   public static final int IX_RESERVED3_OFFSET = 3;
   public static final int IX_TOTAL_SIZE = 7;
   public static final int IX_MIN_DECOMP_NO_CP = 8;
   public static final int IX_MIN_COMP_NO_MAYBE_CP = 9;
   public static final int IX_MIN_YES_NO = 10;
   public static final int IX_MIN_NO_NO = 11;
   public static final int IX_LIMIT_NO_NO = 12;
   public static final int IX_MIN_MAYBE_YES = 13;
   public static final int IX_MIN_YES_NO_MAPPINGS_ONLY = 14;
   public static final int IX_COUNT = 16;
   public static final int MAPPING_HAS_CCC_LCCC_WORD = 128;
   public static final int MAPPING_HAS_RAW_MAPPING = 64;
   public static final int MAPPING_NO_COMP_BOUNDARY_AFTER = 32;
   public static final int MAPPING_LENGTH_MASK = 31;
   public static final int COMP_1_LAST_TUPLE = 32768;
   public static final int COMP_1_TRIPLE = 1;
   public static final int COMP_1_TRAIL_LIMIT = 13312;
   public static final int COMP_1_TRAIL_MASK = 32766;
   public static final int COMP_1_TRAIL_SHIFT = 9;
   public static final int COMP_2_TRAIL_SHIFT = 6;
   public static final int COMP_2_TRAIL_MASK = 65472;
   private VersionInfo dataVersion;
   private int minDecompNoCP;
   private int minCompNoMaybeCP;
   private int minYesNo;
   private int minYesNoMappingsOnly;
   private int minNoNo;
   private int limitNoNo;
   private int minMaybeYes;
   private Trie2_16 normTrie;
   private String maybeYesCompositions;
   private String extraData;
   private byte[] smallFCD;
   private int[] tccc180;
   private Trie2_32 canonIterData;
   private ArrayList canonStartSets;
   private static final int CANON_NOT_SEGMENT_STARTER = Integer.MIN_VALUE;
   private static final int CANON_HAS_COMPOSITIONS = 1073741824;
   private static final int CANON_HAS_SET = 2097152;
   private static final int CANON_VALUE_MASK = 2097151;

   public Normalizer2Impl load(InputStream var1) {
      try {
         BufferedInputStream var2 = new BufferedInputStream(var1);
         this.dataVersion = ICUBinary.readHeaderAndDataVersion(var2, DATA_FORMAT, IS_ACCEPTABLE);
         DataInputStream var3 = new DataInputStream(var2);
         int var4 = var3.readInt() / 4;
         if (var4 <= 13) {
            throw new IOException("Normalizer2 data: not enough indexes");
         } else {
            int[] var5 = new int[var4];
            var5[0] = var4 * 4;

            int var6;
            for(var6 = 1; var6 < var4; ++var6) {
               var5[var6] = var3.readInt();
            }

            this.minDecompNoCP = var5[8];
            this.minCompNoMaybeCP = var5[9];
            this.minYesNo = var5[10];
            this.minYesNoMappingsOnly = var5[14];
            this.minNoNo = var5[11];
            this.limitNoNo = var5[12];
            this.minMaybeYes = var5[13];
            var6 = var5[0];
            int var7 = var5[1];
            this.normTrie = Trie2_16.createFromSerialized(var3);
            int var8 = this.normTrie.getSerializedLength();
            if (var8 > var7 - var6) {
               throw new IOException("Normalizer2 data: not enough bytes for normTrie");
            } else {
               var3.skipBytes(var7 - var6 - var8);
               var6 = var7;
               var7 = var5[2];
               int var9 = (var7 - var6) / 2;
               int var11;
               if (var9 != 0) {
                  char[] var10 = new char[var9];

                  for(var11 = 0; var11 < var9; ++var11) {
                     var10[var11] = var3.readChar();
                  }

                  this.maybeYesCompositions = new String(var10);
                  this.extraData = this.maybeYesCompositions.substring('︀' - this.minMaybeYes);
               }

               this.smallFCD = new byte[256];

               for(var11 = 0; var11 < 256; ++var11) {
                  this.smallFCD[var11] = var3.readByte();
               }

               this.tccc180 = new int[384];
               var11 = 0;

               for(int var12 = 0; var12 < 384; var11 >>= 1) {
                  if ((var12 & 255) == 0) {
                     var11 = this.smallFCD[var12 >> 8];
                  }

                  if ((var11 & 1) != 0) {
                     for(int var13 = 0; var13 < 32; ++var12) {
                        this.tccc180[var12] = this.getFCD16FromNormData(var12) & 255;
                        ++var13;
                     }
                  } else {
                     var12 += 32;
                  }
               }

               var1.close();
               return this;
            }
         }
      } catch (IOException var14) {
         throw new RuntimeException(var14);
      }
   }

   public Normalizer2Impl load(String var1) {
      return this.load(ICUData.getRequiredStream(var1));
   }

   public void addPropertyStarts(UnicodeSet var1) {
      Iterator var2 = this.normTrie.iterator();

      Trie2.Range var3;
      while(var2.hasNext() && !(var3 = (Trie2.Range)var2.next()).leadSurrogate) {
         var1.add(var3.startCodePoint);
      }

      for(int var4 = 44032; var4 < 55204; var4 += 28) {
         var1.add(var4);
         var1.add(var4 + 1);
      }

      var1.add(55204);
   }

   public void addCanonIterPropertyStarts(UnicodeSet var1) {
      this.ensureCanonIterData();
      Iterator var2 = this.canonIterData.iterator(segmentStarterMapper);

      Trie2.Range var3;
      while(var2.hasNext() && !(var3 = (Trie2.Range)var2.next()).leadSurrogate) {
         var1.add(var3.startCodePoint);
      }

   }

   public Trie2_16 getNormTrie() {
      return this.normTrie;
   }

   public synchronized Normalizer2Impl ensureCanonIterData() {
      if (this.canonIterData == null) {
         Trie2Writable var1 = new Trie2Writable(0, 0);
         this.canonStartSets = new ArrayList();
         Iterator var2 = this.normTrie.iterator();

         while(true) {
            Trie2.Range var3;
            int var4;
            do {
               do {
                  if (!var2.hasNext() || (var3 = (Trie2.Range)var2.next()).leadSurrogate) {
                     this.canonIterData = var1.toTrie2_32();
                     return this;
                  }

                  var4 = var3.value;
               } while(var4 == 0);
            } while(this.minYesNo <= var4 && var4 < this.minNoNo);

            for(int var5 = var3.startCodePoint; var5 <= var3.endCodePoint; ++var5) {
               int var6 = var1.get(var5);
               int var7 = var6;
               if (var4 >= this.minMaybeYes) {
                  var7 = var6 | Integer.MIN_VALUE;
                  if (var4 < 65024) {
                     var7 |= 1073741824;
                  }
               } else if (var4 < this.minYesNo) {
                  var7 = var6 | 1073741824;
               } else {
                  int var8 = var5;

                  int var9;
                  for(var9 = var4; this.limitNoNo <= var9 && var9 < this.minMaybeYes; var9 = this.getNorm16(var8)) {
                     var8 = this.mapAlgorithmic(var8, var9);
                  }

                  if (this.minYesNo <= var9 && var9 < this.limitNoNo) {
                     char var10 = this.extraData.charAt(var9);
                     int var11 = var10 & 31;
                     if ((var10 & 128) != 0 && var5 == var8 && (this.extraData.charAt(var9 - 1) & 255) != 0) {
                        var7 = var6 | Integer.MIN_VALUE;
                     }

                     if (var11 != 0) {
                        ++var9;
                        int var12 = var9 + var11;
                        var8 = this.extraData.codePointAt(var9);
                        this.addToStartSet(var1, var5, var8);
                        if (var9 >= this.minNoNo) {
                           while((var9 += Character.charCount(var8)) < var12) {
                              var8 = this.extraData.codePointAt(var9);
                              int var13 = var1.get(var8);
                              if ((var13 & Integer.MIN_VALUE) == 0) {
                                 var1.set(var8, var13 | Integer.MIN_VALUE);
                              }
                           }
                        }
                     }
                  } else {
                     this.addToStartSet(var1, var5, var8);
                  }
               }

               if (var7 != var6) {
                  var1.set(var5, var7);
               }
            }
         }
      } else {
         return this;
      }
   }

   public int getNorm16(int var1) {
      return this.normTrie.get(var1);
   }

   public int getCompQuickCheck(int var1) {
      if (var1 >= this.minNoNo && 65281 > var1) {
         return this.minMaybeYes <= var1 ? 2 : 0;
      } else {
         return 1;
      }
   }

   public boolean isCompNo(int var1) {
      return this.minNoNo <= var1 && var1 < this.minMaybeYes;
   }

   public int getCC(int var1) {
      if (var1 >= 65024) {
         return var1 & 255;
      } else {
         return var1 >= this.minNoNo && this.limitNoNo > var1 ? this.getCCFromNoNo(var1) : 0;
      }
   }

   public static int getCCFromYesOrMaybe(int var0) {
      return var0 >= 65024 ? var0 & 255 : 0;
   }

   public int getFCD16(int var1) {
      if (var1 < 0) {
         return 0;
      } else if (var1 < 384) {
         return this.tccc180[var1];
      } else {
         return var1 <= 65535 && var1 == 0 ? 0 : this.getFCD16FromNormData(var1);
      }
   }

   public int getFCD16FromBelow180(int var1) {
      return this.tccc180[var1];
   }

   public int getFCD16FromNormData(int var1) {
      while(true) {
         int var2 = this.getNorm16(var1);
         if (var2 <= this.minYesNo) {
            return 0;
         }

         if (var2 >= 65024) {
            var2 &= 255;
            return var2 | var2 << 8;
         }

         if (var2 >= this.minMaybeYes) {
            return 0;
         }

         if (!(this >= var2)) {
            char var3 = this.extraData.charAt(var2);
            if ((var3 & 31) == 0) {
               return 511;
            }

            int var4 = var3 >> 8;
            if ((var3 & 128) != 0) {
               var4 |= this.extraData.charAt(var2 - 1) & '\uff00';
            }

            return var4;
         }

         var1 = this.mapAlgorithmic(var1, var2);
      }
   }

   public String getDecomposition(int var1) {
      int var2;
      int var3;
      for(var2 = -1; var1 >= this.minDecompNoCP && !(this >= (var3 = this.getNorm16(var1))); var2 = var1 = this.mapAlgorithmic(var1, var3)) {
         if (this == var3) {
            StringBuilder var5 = new StringBuilder();
            Normalizer2Impl.Hangul.decompose(var1, var5);
            return var5.toString();
         }

         if (!(this >= var3)) {
            int var4 = this.extraData.charAt(var3++) & 31;
            return this.extraData.substring(var3, var3 + var4);
         }
      }

      return var2 < 0 ? null : UTF16.valueOf(var2);
   }

   public String getRawDecomposition(int var1) {
      int var2;
      if (var1 >= this.minDecompNoCP && !(this >= (var2 = this.getNorm16(var1)))) {
         if (this == var2) {
            StringBuilder var8 = new StringBuilder();
            Normalizer2Impl.Hangul.getRawDecomposition(var1, var8);
            return var8.toString();
         } else if (this >= var2) {
            return UTF16.valueOf(this.mapAlgorithmic(var1, var2));
         } else {
            char var3 = this.extraData.charAt(var2);
            int var4 = var3 & 31;
            if ((var3 & 64) != 0) {
               int var5 = var2 - (var3 >> 7 & 1) - 1;
               char var6 = this.extraData.charAt(var5);
               if (var6 <= 31) {
                  return this.extraData.substring(var5 - var6, var5);
               } else {
                  StringBuilder var7 = (new StringBuilder(var4 - 1)).append(var6);
                  var2 += 3;
                  return var7.append(this.extraData, var2, var2 + var4 - 2).toString();
               }
            } else {
               ++var2;
               return this.extraData.substring(var2, var2 + var4);
            }
         }
      } else {
         return null;
      }
   }

   public boolean isCanonSegmentStarter(int var1) {
      return this.canonIterData.get(var1) >= 0;
   }

   public boolean getCanonStartSet(int var1, UnicodeSet var2) {
      int var3 = this.canonIterData.get(var1) & Integer.MAX_VALUE;
      if (var3 == 0) {
         return false;
      } else {
         var2.clear();
         int var4 = var3 & 2097151;
         if ((var3 & 2097152) != 0) {
            var2.addAll((UnicodeSet)this.canonStartSets.get(var4));
         } else if (var4 != 0) {
            var2.add(var4);
         }

         if ((var3 & 1073741824) != 0) {
            int var5 = this.getNorm16(var1);
            if (var5 == 1) {
               int var6 = '가' + (var1 - 4352) * 588;
               var2.add(var6, var6 + 588 - 1);
            } else {
               this.addComposites(this.getCompositionsList(var5), var2);
            }
         }

         return true;
      }
   }

   public int decompose(CharSequence var1, int var2, int var3, Normalizer2Impl.ReorderingBuffer var4) {
      int var5 = this.minDecompNoCP;
      int var7 = 0;
      int var8 = 0;
      int var9 = var2;
      int var10 = 0;

      while(true) {
         while(true) {
            int var6 = var2;

            label58:
            while(true) {
               while(true) {
                  if (var2 == var3) {
                     break label58;
                  }

                  if ((var7 = var1.charAt(var2)) >= var5 && !(this >= (var8 = this.normTrie.getFromU16SingleLead((char)var7)))) {
                     if (!UTF16.isSurrogate((char)var7)) {
                        break label58;
                     }

                     char var11;
                     if (Normalizer2Impl.UTF16Plus.isSurrogateLead(var7)) {
                        if (var2 + 1 != var3 && Character.isLowSurrogate(var11 = var1.charAt(var2 + 1))) {
                           var7 = Character.toCodePoint((char)var7, var11);
                        }
                     } else if (var6 < var2 && Character.isHighSurrogate(var11 = var1.charAt(var2 - 1))) {
                        --var2;
                        var7 = Character.toCodePoint(var11, (char)var7);
                     }

                     if (!(this >= (var8 = this.getNorm16(var7)))) {
                        break label58;
                     }

                     var2 += Character.charCount(var7);
                  } else {
                     ++var2;
                  }
               }
            }

            if (var2 != var6) {
               if (var4 != null) {
                  var4.flushAndAppendZeroCC(var1, var6, var2);
               } else {
                  var10 = 0;
                  var9 = var2;
               }
            }

            if (var2 == var3) {
               return var2;
            }

            var2 += Character.charCount(var7);
            if (var4 == null) {
               if (!(this >= var8)) {
                  return var9;
               }

               int var12 = getCCFromYesOrMaybe(var8);
               if (var10 > var12 && var12 != 0) {
                  return var9;
               }

               var10 = var12;
               if (var12 <= 1) {
                  var9 = var2;
               }
            } else {
               this.decompose(var7, var8, var4);
            }
         }
      }
   }

   public void decomposeAndAppend(CharSequence var1, boolean var2, Normalizer2Impl.ReorderingBuffer var3) {
      int var4 = var1.length();
      if (var4 != 0) {
         if (var2) {
            this.decompose(var1, 0, var4, var3);
         } else {
            int var5 = Character.codePointAt(var1, 0);
            int var6 = 0;

            int var7;
            int var8;
            int var9;
            for(var7 = var8 = var9 = this.getCC(this.getNorm16(var5)); var9 != 0; var9 = this.getCC(this.getNorm16(var5))) {
               var8 = var9;
               var6 += Character.charCount(var5);
               if (var6 >= var4) {
                  break;
               }

               var5 = Character.codePointAt(var1, var6);
            }

            var3.append(var1, 0, var6, var7, var8);
            var3.append(var1, var6, var4);
         }
      }
   }

   public boolean compose(CharSequence param1, int param2, int param3, boolean param4, boolean param5, Normalizer2Impl.ReorderingBuffer param6) {
      // $FF: Couldn't be decompiled
   }

   public int composeQuickCheck(CharSequence var1, int var2, int var3, boolean var4, boolean var5) {
      byte var6 = 0;
      int var7 = this.minCompNoMaybeCP;
      int var8 = var2;
      boolean var10 = false;
      boolean var11 = false;
      int var12 = 0;

      while(true) {
         int var9 = var2;

         int var14;
         int var15;
         label69:
         while(true) {
            while(var2 != var3) {
               if ((var14 = var1.charAt(var2)) >= var7 && !(this < (var15 = this.normTrie.getFromU16SingleLead((char)var14)))) {
                  if (!UTF16.isSurrogate((char)var14)) {
                     break label69;
                  }

                  char var13;
                  if (Normalizer2Impl.UTF16Plus.isSurrogateLead(var14)) {
                     if (var2 + 1 != var3 && Character.isLowSurrogate(var13 = var1.charAt(var2 + 1))) {
                        var14 = Character.toCodePoint((char)var14, var13);
                     }
                  } else if (var9 < var2 && Character.isHighSurrogate(var13 = var1.charAt(var2 - 1))) {
                     --var2;
                     var14 = Character.toCodePoint(var13, (char)var14);
                  }

                  if (!(this < (var15 = this.getNorm16(var14)))) {
                     break label69;
                  }

                  var2 += Character.charCount(var14);
               } else {
                  ++var2;
               }
            }

            return var2 << 1 | var6;
         }

         if (var2 != var9) {
            var8 = var2 - 1;
            if (Character.isLowSurrogate(var1.charAt(var8)) && var9 < var8 && Character.isHighSurrogate(var1.charAt(var8 - 1))) {
               --var8;
            }

            var12 = 0;
            var9 = var2;
         }

         var2 += Character.charCount(var14);
         if (!(this >= var15)) {
            break;
         }

         int var16 = getCCFromYesOrMaybe(var15);
         if (var4 && var16 != 0 && var12 == 0 && var8 < var9 && this.getTrailCCFromCompYesAndZeroCC(var1, var8, var9) > var16 || var12 > var16 && var16 != 0) {
            break;
         }

         var12 = var16;
         if (var15 < 65281) {
            if (var5) {
               return var8 << 1;
            }

            var6 = 1;
         }
      }

      return var8 << 1;
   }

   public void composeAndAppend(CharSequence var1, boolean var2, boolean var3, Normalizer2Impl.ReorderingBuffer var4) {
      int var5 = 0;
      int var6 = var1.length();
      if (!var4.isEmpty()) {
         int var7 = this.findNextCompBoundary(var1, 0, var6);
         if (0 != var7) {
            int var8 = this.findPreviousCompBoundary(var4.getStringBuilder(), var4.length());
            StringBuilder var9 = new StringBuilder(var4.length() - var8 + var7 + 16);
            var9.append(var4.getStringBuilder(), var8, var4.length());
            var4.removeSuffix(var4.length() - var8);
            var9.append(var1, 0, var7);
            this.compose(var9, 0, var9.length(), var3, true, var4);
            var5 = var7;
         }
      }

      if (var2) {
         this.compose(var1, var5, var6, var3, true, var4);
      } else {
         var4.append(var1, var5, var6);
      }

   }

   public int makeFCD(CharSequence var1, int var2, int var3, Normalizer2Impl.ReorderingBuffer var4) {
      int var5 = var2;
      int var7 = 0;
      int var8 = 0;
      int var9 = 0;

      while(true) {
         int var6 = var2;

         while(var2 != var3) {
            if ((var7 = var1.charAt(var2)) < 768) {
               var8 = ~var7;
               ++var2;
            } else if (var7 == 0) {
               var8 = 0;
               ++var2;
            } else {
               if (UTF16.isSurrogate((char)var7)) {
                  char var10;
                  if (Normalizer2Impl.UTF16Plus.isSurrogateLead(var7)) {
                     if (var2 + 1 != var3 && Character.isLowSurrogate(var10 = var1.charAt(var2 + 1))) {
                        var7 = Character.toCodePoint((char)var7, var10);
                     }
                  } else if (var6 < var2 && Character.isHighSurrogate(var10 = var1.charAt(var2 - 1))) {
                     --var2;
                     var7 = Character.toCodePoint(var10, (char)var7);
                  }
               }

               if ((var9 = this.getFCD16FromNormData(var7)) > 255) {
                  break;
               }

               var8 = var9;
               var2 += Character.charCount(var7);
            }
         }

         if (var2 != var6) {
            if (var2 == var3) {
               if (var4 != null) {
                  var4.flushAndAppendZeroCC(var1, var6, var2);
               }
               break;
            }

            var5 = var2;
            int var11;
            if (var8 < 0) {
               var11 = ~var8;
               var8 = var11 < 384 ? this.tccc180[var11] : this.getFCD16FromNormData(var11);
               if (var8 > 1) {
                  var5 = var2 - 1;
               }
            } else {
               var11 = var2 - 1;
               if (Character.isLowSurrogate(var1.charAt(var11)) && var6 < var11 && Character.isHighSurrogate(var1.charAt(var11 - 1))) {
                  --var11;
                  var8 = this.getFCD16FromNormData(Character.toCodePoint(var1.charAt(var11), var1.charAt(var11 + 1)));
               }

               if (var8 > 1) {
                  var5 = var11;
               }
            }

            if (var4 != null) {
               var4.flushAndAppendZeroCC(var1, var6, var5);
               var4.append(var1, var5, var2);
            }

            var6 = var2;
         } else if (var2 == var3) {
            break;
         }

         var2 += Character.charCount(var7);
         if ((var8 & 255) <= var9 >> 8) {
            if ((var9 & 255) <= 1) {
               var5 = var2;
            }

            if (var4 != null) {
               var4.appendZeroCC(var7);
            }

            var8 = var9;
         } else {
            if (var4 == null) {
               return var5;
            }

            var4.removeSuffix(var6 - var5);
            var2 = this.findNextFCDBoundary(var1, var2, var3);
            this.decomposeShort(var1, var5, var2, var4);
            var5 = var2;
            var8 = 0;
         }
      }

      return var2;
   }

   public void makeFCDAndAppend(CharSequence var1, boolean var2, Normalizer2Impl.ReorderingBuffer var3) {
      int var4 = 0;
      int var5 = var1.length();
      if (!var3.isEmpty()) {
         int var6 = this.findNextFCDBoundary(var1, 0, var5);
         if (0 != var6) {
            int var7 = this.findPreviousFCDBoundary(var3.getStringBuilder(), var3.length());
            StringBuilder var8 = new StringBuilder(var3.length() - var7 + var6 + 16);
            var8.append(var3.getStringBuilder(), var7, var3.length());
            var3.removeSuffix(var3.length() - var7);
            var8.append(var1, 0, var6);
            this.makeFCD(var8, 0, var8.length(), var3);
            var4 = var6;
         }
      }

      if (var2) {
         this.makeFCD(var1, var4, var5, var3);
      } else {
         var3.append(var1, var4, var5);
      }

   }

   public boolean hasDecompBoundary(int var1, boolean var2) {
      while(var1 >= this.minDecompNoCP) {
         int var3 = this.getNorm16(var1);
         if (this == var3 && !(this >= var3)) {
            if (var3 > 65024) {
               return false;
            }

            if (this >= var3) {
               var1 = this.mapAlgorithmic(var1, var3);
               continue;
            }

            char var4 = this.extraData.charAt(var3);
            if ((var4 & 31) == 0) {
               return false;
            }

            if (!var2) {
               if (var4 > 511) {
                  return false;
               }

               if (var4 <= 255) {
                  return true;
               }
            }

            return (var4 & 128) == 0 || (this.extraData.charAt(var3 - 1) & '\uff00') == 0;
         }

         return true;
      }

      return true;
   }

   public boolean isDecompInert(int var1) {
      return this.isDecompYesAndZeroCC(this.getNorm16(var1));
   }

   public boolean hasCompBoundaryAfter(int var1, boolean var2, boolean var3) {
      while(true) {
         int var4 = this.getNorm16(var1);
         if (var4 == 0) {
            return true;
         }

         if (var4 <= this.minYesNo) {
            return this == var4 && !Normalizer2Impl.Hangul.isHangulWithoutJamoT((char)var1);
         }

         if (var4 >= (var3 ? this.minNoNo : this.minMaybeYes)) {
            return false;
         }

         if (!(this >= var4)) {
            char var5 = this.extraData.charAt(var4);
            return (var5 & 32) == 0 && (!var2 || var5 <= 511);
         }

         var1 = this.mapAlgorithmic(var1, var4);
      }
   }

   public boolean hasFCDBoundaryBefore(int var1) {
      return var1 < 768 || this.getFCD16(var1) <= 255;
   }

   public boolean hasFCDBoundaryAfter(int var1) {
      int var2 = this.getFCD16(var1);
      return var2 <= 1 || (var2 & 255) == 0;
   }

   public boolean isFCDInert(int var1) {
      return this.getFCD16(var1) <= 1;
   }

   private int getCCFromNoNo(int var1) {
      return (this.extraData.charAt(var1) & 128) != 0 ? this.extraData.charAt(var1 - 1) & 255 : 0;
   }

   int getTrailCCFromCompYesAndZeroCC(CharSequence var1, int var2, int var3) {
      int var4;
      if (var2 == var3 - 1) {
         var4 = var1.charAt(var2);
      } else {
         var4 = Character.codePointAt(var1, var2);
      }

      int var5 = this.getNorm16(var4);
      return var5 <= this.minYesNo ? 0 : this.extraData.charAt(var5) >> 8;
   }

   private int mapAlgorithmic(int var1, int var2) {
      return var1 + var2 - (this.minMaybeYes - 64 - 1);
   }

   private int getCompositionsListForDecompYes(int var1) {
      if (var1 != 0 && 65024 > var1) {
         if ((var1 -= this.minMaybeYes) < 0) {
            var1 += 65024;
         }

         return var1;
      } else {
         return -1;
      }
   }

   private int getCompositionsListForComposite(int var1) {
      char var2 = this.extraData.charAt(var1);
      return '︀' - this.minMaybeYes + var1 + 1 + (var2 & 31);
   }

   private int getCompositionsList(int var1) {
      return this >= var1 ? this.getCompositionsListForDecompYes(var1) : this.getCompositionsListForComposite(var1);
   }

   public void decomposeShort(CharSequence var1, int var2, int var3, Normalizer2Impl.ReorderingBuffer var4) {
      while(var2 < var3) {
         int var5 = Character.codePointAt(var1, var2);
         var2 += Character.charCount(var5);
         this.decompose(var5, this.getNorm16(var5), var4);
      }

   }

   private void decompose(int var1, int var2, Normalizer2Impl.ReorderingBuffer var3) {
      while(true) {
         if (this >= var2) {
            var3.append(var1, getCCFromYesOrMaybe(var2));
         } else if (this == var2) {
            Normalizer2Impl.Hangul.decompose(var1, var3);
         } else {
            if (this >= var2) {
               var1 = this.mapAlgorithmic(var1, var2);
               var2 = this.getNorm16(var1);
               continue;
            }

            char var4 = this.extraData.charAt(var2);
            int var5 = var4 & 31;
            int var7 = var4 >> 8;
            int var6;
            if ((var4 & 128) != 0) {
               var6 = this.extraData.charAt(var2 - 1) >> 8;
            } else {
               var6 = 0;
            }

            ++var2;
            var3.append(this.extraData, var2, var2 + var5, var6, var7);
         }

         return;
      }
   }

   private static int combine(String var0, int var1, int var2) {
      int var3;
      char var4;
      if (var2 < 13312) {
         for(var3 = var2 << 1; var3 > (var4 = var0.charAt(var1)); var1 += 2 + (var4 & 1)) {
         }

         if (var3 == (var4 & 32766)) {
            if ((var4 & 1) != 0) {
               return var0.charAt(var1 + 1) << 16 | var0.charAt(var1 + 2);
            } else {
               return var0.charAt(var1 + 1);
            }
         } else {
            return -1;
         }
      } else {
         var3 = 13312 + (var2 >> 9 & -2);
         int var5 = var2 << 6 & '\uffff';

         while(true) {
            while(var3 <= (var4 = var0.charAt(var1))) {
               if (var3 != (var4 & 32766)) {
                  return -1;
               }

               char var6;
               if (var5 <= (var6 = var0.charAt(var1 + 1))) {
                  if (var5 == (var6 & '\uffc0')) {
                     return (var6 & -65473) << 16 | var0.charAt(var1 + 2);
                  }

                  return -1;
               }

               if ((var4 & '耀') != 0) {
                  return -1;
               }

               var1 += 3;
            }

            var1 += 2 + (var4 & 1);
         }
      }
   }

   private void addComposites(int var1, UnicodeSet var2) {
      char var3;
      do {
         var3 = this.maybeYesCompositions.charAt(var1);
         int var4;
         if ((var3 & 1) == 0) {
            var4 = this.maybeYesCompositions.charAt(var1 + 1);
            var1 += 2;
         } else {
            var4 = (this.maybeYesCompositions.charAt(var1 + 1) & -65473) << 16 | this.maybeYesCompositions.charAt(var1 + 2);
            var1 += 3;
         }

         int var5 = var4 >> 1;
         if ((var4 & 1) != 0) {
            this.addComposites(this.getCompositionsListForComposite(this.getNorm16(var5)), var2);
         }

         var2.add(var5);
      } while((var3 & '耀') == 0);

   }

   private void recompose(Normalizer2Impl.ReorderingBuffer param1, int param2, boolean param3) {
      // $FF: Couldn't be decompiled
   }

   public int composePair(int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   private int findPreviousCompBoundary(CharSequence var1, int var2) {
      while(true) {
         if (var2 > 0) {
            int var3 = Character.codePointBefore(var1, var2);
            var2 -= Character.charCount(var3);
            if (!(this >= var3)) {
               continue;
            }
         }

         return var2;
      }
   }

   private int findNextCompBoundary(CharSequence var1, int var2, int var3) {
      while(true) {
         if (var2 < var3) {
            int var4 = Character.codePointAt(var1, var2);
            int var5 = this.normTrie.get(var4);
            if (var4 >= var5) {
               var2 += Character.charCount(var4);
               continue;
            }
         }

         return var2;
      }
   }

   private int findPreviousFCDBoundary(CharSequence var1, int var2) {
      while(true) {
         if (var2 > 0) {
            int var3 = Character.codePointBefore(var1, var2);
            var2 -= Character.charCount(var3);
            if (var3 >= 768 && this.getFCD16(var3) > 255) {
               continue;
            }
         }

         return var2;
      }
   }

   private int findNextFCDBoundary(CharSequence var1, int var2, int var3) {
      while(true) {
         if (var2 < var3) {
            int var4 = Character.codePointAt(var1, var2);
            if (var4 >= 768 && this.getFCD16(var4) > 255) {
               var2 += Character.charCount(var4);
               continue;
            }
         }

         return var2;
      }
   }

   private void addToStartSet(Trie2Writable var1, int var2, int var3) {
      int var4 = var1.get(var3);
      if ((var4 & 4194303) == 0 && var2 != 0) {
         var1.set(var3, var4 | var2);
      } else {
         UnicodeSet var5;
         if ((var4 & 2097152) == 0) {
            int var6 = var4 & 2097151;
            var4 = var4 & -2097152 | 2097152 | this.canonStartSets.size();
            var1.set(var3, var4);
            this.canonStartSets.add(var5 = new UnicodeSet());
            if (var6 != 0) {
               var5.add(var6);
            }
         } else {
            var5 = (UnicodeSet)this.canonStartSets.get(var4 & 2097151);
         }

         var5.add(var2);
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

   public static final class UTF16Plus {
      public static boolean isSurrogateLead(int var0) {
         return (var0 & 1024) == 0;
      }

      public static boolean equal(CharSequence var0, CharSequence var1) {
         if (var0 == var1) {
            return true;
         } else {
            int var2 = var0.length();
            if (var2 != var1.length()) {
               return false;
            } else {
               for(int var3 = 0; var3 < var2; ++var3) {
                  if (var0.charAt(var3) != var1.charAt(var3)) {
                     return false;
                  }
               }

               return true;
            }
         }
      }

      public static boolean equal(CharSequence var0, int var1, int var2, CharSequence var3, int var4, int var5) {
         if (var2 - var1 != var5 - var4) {
            return false;
         } else if (var0 == var3 && var1 == var4) {
            return true;
         } else {
            do {
               if (var1 >= var2) {
                  return true;
               }
            } while(var0.charAt(var1++) == var3.charAt(var4++));

            return false;
         }
      }
   }

   public static final class ReorderingBuffer implements Appendable {
      private final Normalizer2Impl impl;
      private final Appendable app;
      private final StringBuilder str;
      private final boolean appIsStringBuilder;
      private int reorderStart;
      private int lastCC;
      private int codePointStart;
      private int codePointLimit;

      public ReorderingBuffer(Normalizer2Impl var1, Appendable var2, int var3) {
         this.impl = var1;
         this.app = var2;
         if (this.app instanceof StringBuilder) {
            this.appIsStringBuilder = true;
            this.str = (StringBuilder)var2;
            this.str.ensureCapacity(var3);
            this.reorderStart = 0;
            if (this.str.length() == 0) {
               this.lastCC = 0;
            } else {
               this.setIterator();
               this.lastCC = this.previousCC();
               if (this.lastCC > 1) {
                  while(this.previousCC() > 1) {
                  }
               }

               this.reorderStart = this.codePointLimit;
            }
         } else {
            this.appIsStringBuilder = false;
            this.str = new StringBuilder();
            this.reorderStart = 0;
            this.lastCC = 0;
         }

      }

      public boolean isEmpty() {
         return this.str.length() == 0;
      }

      public int length() {
         return this.str.length();
      }

      public int getLastCC() {
         return this.lastCC;
      }

      public StringBuilder getStringBuilder() {
         return this.str;
      }

      public boolean equals(CharSequence var1, int var2, int var3) {
         return Normalizer2Impl.UTF16Plus.equal(this.str, 0, this.str.length(), var1, var2, var3);
      }

      public void setLastChar(char var1) {
         this.str.setCharAt(this.str.length() - 1, var1);
      }

      public void append(int var1, int var2) {
         if (this.lastCC > var2 && var2 != 0) {
            this.insert(var1, var2);
         } else {
            this.str.appendCodePoint(var1);
            this.lastCC = var2;
            if (var2 <= 1) {
               this.reorderStart = this.str.length();
            }
         }

      }

      public void append(CharSequence var1, int var2, int var3, int var4, int var5) {
         if (var2 != var3) {
            if (this.lastCC > var4 && var4 != 0) {
               int var6 = Character.codePointAt(var1, var2);
               var2 += Character.charCount(var6);
               this.insert(var6, var4);

               for(; var2 < var3; this.append(var6, var4)) {
                  var6 = Character.codePointAt(var1, var2);
                  var2 += Character.charCount(var6);
                  if (var2 < var3) {
                     var4 = Normalizer2Impl.getCCFromYesOrMaybe(this.impl.getNorm16(var6));
                  } else {
                     var4 = var5;
                  }
               }
            } else {
               if (var5 <= 1) {
                  this.reorderStart = this.str.length() + (var3 - var2);
               } else if (var4 <= 1) {
                  this.reorderStart = this.str.length() + 1;
               }

               this.str.append(var1, var2, var3);
               this.lastCC = var5;
            }

         }
      }

      public Normalizer2Impl.ReorderingBuffer append(char var1) {
         this.str.append(var1);
         this.lastCC = 0;
         this.reorderStart = this.str.length();
         return this;
      }

      public void appendZeroCC(int var1) {
         this.str.appendCodePoint(var1);
         this.lastCC = 0;
         this.reorderStart = this.str.length();
      }

      public Normalizer2Impl.ReorderingBuffer append(CharSequence var1) {
         if (var1.length() != 0) {
            this.str.append(var1);
            this.lastCC = 0;
            this.reorderStart = this.str.length();
         }

         return this;
      }

      public Normalizer2Impl.ReorderingBuffer append(CharSequence var1, int var2, int var3) {
         if (var2 != var3) {
            this.str.append(var1, var2, var3);
            this.lastCC = 0;
            this.reorderStart = this.str.length();
         }

         return this;
      }

      public void flush() {
         if (this.appIsStringBuilder) {
            this.reorderStart = this.str.length();
         } else {
            try {
               this.app.append(this.str);
               this.str.setLength(0);
               this.reorderStart = 0;
            } catch (IOException var2) {
               throw new RuntimeException(var2);
            }
         }

         this.lastCC = 0;
      }

      public Normalizer2Impl.ReorderingBuffer flushAndAppendZeroCC(CharSequence var1, int var2, int var3) {
         if (this.appIsStringBuilder) {
            this.str.append(var1, var2, var3);
            this.reorderStart = this.str.length();
         } else {
            try {
               this.app.append(this.str).append(var1, var2, var3);
               this.str.setLength(0);
               this.reorderStart = 0;
            } catch (IOException var5) {
               throw new RuntimeException(var5);
            }
         }

         this.lastCC = 0;
         return this;
      }

      public void remove() {
         this.str.setLength(0);
         this.lastCC = 0;
         this.reorderStart = 0;
      }

      public void removeSuffix(int var1) {
         int var2 = this.str.length();
         this.str.delete(var2 - var1, var2);
         this.lastCC = 0;
         this.reorderStart = this.str.length();
      }

      private void insert(int var1, int var2) {
         this.setIterator();
         this.skipPrevious();

         while(this.previousCC() > var2) {
         }

         if (var1 <= 65535) {
            this.str.insert(this.codePointLimit, (char)var1);
            if (var2 <= 1) {
               this.reorderStart = this.codePointLimit + 1;
            }
         } else {
            this.str.insert(this.codePointLimit, Character.toChars(var1));
            if (var2 <= 1) {
               this.reorderStart = this.codePointLimit + 2;
            }
         }

      }

      private void setIterator() {
         this.codePointStart = this.str.length();
      }

      private void skipPrevious() {
         this.codePointLimit = this.codePointStart;
         this.codePointStart = this.str.offsetByCodePoints(this.codePointStart, -1);
      }

      private int previousCC() {
         this.codePointLimit = this.codePointStart;
         if (this.reorderStart >= this.codePointStart) {
            return 0;
         } else {
            int var1 = this.str.codePointBefore(this.codePointStart);
            this.codePointStart -= Character.charCount(var1);
            return var1 < 768 ? 0 : Normalizer2Impl.getCCFromYesOrMaybe(this.impl.getNorm16(var1));
         }
      }

      public Appendable append(char var1) throws IOException {
         return this.append(var1);
      }

      public Appendable append(CharSequence var1, int var2, int var3) throws IOException {
         return this.append(var1, var2, var3);
      }

      public Appendable append(CharSequence var1) throws IOException {
         return this.append(var1);
      }
   }

   public static final class Hangul {
      public static final int JAMO_L_BASE = 4352;
      public static final int JAMO_V_BASE = 4449;
      public static final int JAMO_T_BASE = 4519;
      public static final int HANGUL_BASE = 44032;
      public static final int JAMO_L_COUNT = 19;
      public static final int JAMO_V_COUNT = 21;
      public static final int JAMO_T_COUNT = 28;
      public static final int JAMO_L_LIMIT = 4371;
      public static final int JAMO_V_LIMIT = 4470;
      public static final int JAMO_VT_COUNT = 588;
      public static final int HANGUL_COUNT = 11172;
      public static final int HANGUL_LIMIT = 55204;

      public static boolean isHangul(int var0) {
         return 44032 <= var0 && var0 < 55204;
      }

      public static boolean isHangulWithoutJamoT(char var0) {
         var0 -= '가';
         return var0 < 11172 && var0 % 28 == 0;
      }

      public static boolean isJamoL(int var0) {
         return 4352 <= var0 && var0 < 4371;
      }

      public static boolean isJamoV(int var0) {
         return 4449 <= var0 && var0 < 4470;
      }

      public static int decompose(int var0, Appendable var1) {
         try {
            var0 -= 44032;
            int var2 = var0 % 28;
            var0 /= 28;
            var1.append((char)(4352 + var0 / 21));
            var1.append((char)(4449 + var0 % 21));
            if (var2 == 0) {
               return 2;
            } else {
               var1.append((char)(4519 + var2));
               return 3;
            }
         } catch (IOException var3) {
            throw new RuntimeException(var3);
         }
      }

      public static void getRawDecomposition(int var0, Appendable var1) {
         try {
            int var2 = var0;
            var0 -= 44032;
            int var3 = var0 % 28;
            if (var3 == 0) {
               var0 /= 28;
               var1.append((char)(4352 + var0 / 21));
               var1.append((char)(4449 + var0 % 21));
            } else {
               var1.append((char)(var2 - var3));
               var1.append((char)(4519 + var3));
            }

         } catch (IOException var4) {
            throw new RuntimeException(var4);
         }
      }
   }
}
