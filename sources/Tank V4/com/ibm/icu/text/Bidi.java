package com.ibm.icu.text;

import com.ibm.icu.impl.UBiDiProps;
import com.ibm.icu.lang.UCharacter;
import java.awt.font.NumericShaper;
import java.awt.font.TextAttribute;
import java.lang.reflect.Array;
import java.text.AttributedCharacterIterator;
import java.util.Arrays;

public class Bidi {
   public static final byte LEVEL_DEFAULT_LTR = 126;
   public static final byte LEVEL_DEFAULT_RTL = 127;
   public static final byte MAX_EXPLICIT_LEVEL = 61;
   public static final byte LEVEL_OVERRIDE = -128;
   public static final int MAP_NOWHERE = -1;
   public static final byte LTR = 0;
   public static final byte RTL = 1;
   public static final byte MIXED = 2;
   public static final byte NEUTRAL = 3;
   public static final short KEEP_BASE_COMBINING = 1;
   public static final short DO_MIRRORING = 2;
   public static final short INSERT_LRM_FOR_NUMERIC = 4;
   public static final short REMOVE_BIDI_CONTROLS = 8;
   public static final short OUTPUT_REVERSE = 16;
   public static final short REORDER_DEFAULT = 0;
   public static final short REORDER_NUMBERS_SPECIAL = 1;
   public static final short REORDER_GROUP_NUMBERS_WITH_R = 2;
   public static final short REORDER_RUNS_ONLY = 3;
   public static final short REORDER_INVERSE_NUMBERS_AS_L = 4;
   public static final short REORDER_INVERSE_LIKE_DIRECT = 5;
   public static final short REORDER_INVERSE_FOR_NUMBERS_SPECIAL = 6;
   static final short REORDER_COUNT = 7;
   static final short REORDER_LAST_LOGICAL_TO_VISUAL = 1;
   public static final int OPTION_DEFAULT = 0;
   public static final int OPTION_INSERT_MARKS = 1;
   public static final int OPTION_REMOVE_CONTROLS = 2;
   public static final int OPTION_STREAMING = 4;
   static final byte L = 0;
   static final byte R = 1;
   static final byte EN = 2;
   static final byte ES = 3;
   static final byte ET = 4;
   static final byte AN = 5;
   static final byte CS = 6;
   static final byte B = 7;
   static final byte S = 8;
   static final byte WS = 9;
   static final byte ON = 10;
   static final byte LRE = 11;
   static final byte LRO = 12;
   static final byte AL = 13;
   static final byte RLE = 14;
   static final byte RLO = 15;
   static final byte PDF = 16;
   static final byte NSM = 17;
   static final byte BN = 18;
   static final int MASK_R_AL = 8194;
   public static final int CLASS_DEFAULT = 19;
   private static final char CR = '\r';
   private static final char LF = '\n';
   static final int LRM_BEFORE = 1;
   static final int LRM_AFTER = 2;
   static final int RLM_BEFORE = 4;
   static final int RLM_AFTER = 8;
   Bidi paraBidi;
   final UBiDiProps bdp;
   char[] text;
   int originalLength;
   int length;
   int resultLength;
   boolean mayAllocateText;
   boolean mayAllocateRuns;
   byte[] dirPropsMemory;
   byte[] levelsMemory;
   byte[] dirProps;
   byte[] levels;
   boolean isInverse;
   int reorderingMode;
   int reorderingOptions;
   boolean orderParagraphsLTR;
   byte paraLevel;
   byte defaultParaLevel;
   String prologue;
   String epilogue;
   Bidi.ImpTabPair impTabPair;
   byte direction;
   int flags;
   int lastArabicPos;
   int trailingWSStart;
   int paraCount;
   int[] parasMemory;
   int[] paras;
   int[] simpleParas;
   int runCount;
   BidiRun[] runsMemory;
   BidiRun[] runs;
   BidiRun[] simpleRuns;
   int[] logicalToVisualRunsMap;
   boolean isGoodLogicalToVisualRunsMap;
   BidiClassifier customClassifier;
   Bidi.InsertPoints insertPoints;
   int controlCount;
   static final byte CONTEXT_RTL_SHIFT = 6;
   static final byte CONTEXT_RTL = 64;
   static final int DirPropFlagMultiRuns = DirPropFlag((byte)31);
   static final int[] DirPropFlagLR = new int[]{DirPropFlag((byte)0), DirPropFlag((byte)1)};
   static final int[] DirPropFlagE = new int[]{DirPropFlag((byte)11), DirPropFlag((byte)14)};
   static final int[] DirPropFlagO = new int[]{DirPropFlag((byte)12), DirPropFlag((byte)15)};
   static final int MASK_LTR = DirPropFlag((byte)0) | DirPropFlag((byte)2) | DirPropFlag((byte)5) | DirPropFlag((byte)11) | DirPropFlag((byte)12);
   static final int MASK_RTL = DirPropFlag((byte)1) | DirPropFlag((byte)13) | DirPropFlag((byte)14) | DirPropFlag((byte)15);
   static final int MASK_LRX = DirPropFlag((byte)11) | DirPropFlag((byte)12);
   static final int MASK_RLX = DirPropFlag((byte)14) | DirPropFlag((byte)15);
   static final int MASK_OVERRIDE = DirPropFlag((byte)12) | DirPropFlag((byte)15);
   static final int MASK_EXPLICIT;
   static final int MASK_BN_EXPLICIT;
   static final int MASK_B_S;
   static final int MASK_WS;
   static final int MASK_N;
   static final int MASK_ET_NSM_BN;
   static final int MASK_POSSIBLE_N;
   static final int MASK_EMBEDDING;
   private static final int IMPTABPROPS_COLUMNS = 14;
   private static final int IMPTABPROPS_RES = 13;
   private static final short[] groupProp;
   private static final short _L = 0;
   private static final short _R = 1;
   private static final short _EN = 2;
   private static final short _AN = 3;
   private static final short _ON = 4;
   private static final short _S = 5;
   private static final short _B = 6;
   private static final short[][] impTabProps;
   private static final int IMPTABLEVELS_COLUMNS = 8;
   private static final int IMPTABLEVELS_RES = 7;
   private static final byte[][] impTabL_DEFAULT;
   private static final byte[][] impTabR_DEFAULT;
   private static final short[] impAct0;
   private static final Bidi.ImpTabPair impTab_DEFAULT;
   private static final byte[][] impTabL_NUMBERS_SPECIAL;
   private static final Bidi.ImpTabPair impTab_NUMBERS_SPECIAL;
   private static final byte[][] impTabL_GROUP_NUMBERS_WITH_R;
   private static final byte[][] impTabR_GROUP_NUMBERS_WITH_R;
   private static final Bidi.ImpTabPair impTab_GROUP_NUMBERS_WITH_R;
   private static final byte[][] impTabL_INVERSE_NUMBERS_AS_L;
   private static final byte[][] impTabR_INVERSE_NUMBERS_AS_L;
   private static final Bidi.ImpTabPair impTab_INVERSE_NUMBERS_AS_L;
   private static final byte[][] impTabR_INVERSE_LIKE_DIRECT;
   private static final short[] impAct1;
   private static final Bidi.ImpTabPair impTab_INVERSE_LIKE_DIRECT;
   private static final byte[][] impTabL_INVERSE_LIKE_DIRECT_WITH_MARKS;
   private static final byte[][] impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS;
   private static final short[] impAct2;
   private static final Bidi.ImpTabPair impTab_INVERSE_LIKE_DIRECT_WITH_MARKS;
   private static final Bidi.ImpTabPair impTab_INVERSE_FOR_NUMBERS_SPECIAL;
   private static final byte[][] impTabL_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS;
   private static final Bidi.ImpTabPair impTab_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS;
   static final int FIRSTALLOC = 10;
   public static final int DIRECTION_LEFT_TO_RIGHT = 0;
   public static final int DIRECTION_RIGHT_TO_LEFT = 1;
   public static final int DIRECTION_DEFAULT_LEFT_TO_RIGHT = 126;
   public static final int DIRECTION_DEFAULT_RIGHT_TO_LEFT = 127;

   static int DirPropFlag(byte var0) {
      return 1 << var0;
   }

   boolean testDirPropFlagAt(int var1, int var2) {
      return (DirPropFlag((byte)(this.dirProps[var2] & -65)) & var1) != 0;
   }

   static byte NoContextRTL(byte var0) {
      return (byte)(var0 & -65);
   }

   static int DirPropFlagNC(byte var0) {
      return 1 << (var0 & -65);
   }

   static final int DirPropFlagLR(byte var0) {
      return DirPropFlagLR[var0 & 1];
   }

   static final int DirPropFlagE(byte var0) {
      return DirPropFlagE[var0 & 1];
   }

   static final int DirPropFlagO(byte var0) {
      return DirPropFlagO[var0 & 1];
   }

   static byte GetLRFromLevel(byte var0) {
      return (byte)(var0 & 1);
   }

   byte GetParaLevelAt(int var1) {
      return this.defaultParaLevel != 0 ? (byte)(this.dirProps[var1] >> 6) : this.paraLevel;
   }

   void verifyValidPara() {
      if (this != this.paraBidi) {
         throw new IllegalStateException();
      }
   }

   void verifyValidParaOrLine() {
      Bidi var1 = this.paraBidi;
      if (this != var1) {
         if (var1 == null || var1 != var1.paraBidi) {
            throw new IllegalStateException();
         }
      }
   }

   void verifyRange(int var1, int var2, int var3) {
      if (var1 < var2 || var1 >= var3) {
         throw new IllegalArgumentException("Value " + var1 + " is out of range " + var2 + " to " + var3);
      }
   }

   public Bidi() {
      this(0, 0);
   }

   public Bidi(int var1, int var2) {
      this.dirPropsMemory = new byte[1];
      this.levelsMemory = new byte[1];
      this.parasMemory = new int[1];
      this.simpleParas = new int[]{0};
      this.runsMemory = new BidiRun[0];
      this.simpleRuns = new BidiRun[]{new BidiRun()};
      this.customClassifier = null;
      this.insertPoints = new Bidi.InsertPoints();
      if (var1 >= 0 && var2 >= 0) {
         this.bdp = UBiDiProps.INSTANCE;
         if (var1 > 0) {
            this.getInitialDirPropsMemory(var1);
            this.getInitialLevelsMemory(var1);
         } else {
            this.mayAllocateText = true;
         }

         if (var2 > 0) {
            if (var2 > 1) {
               this.getInitialRunsMemory(var2);
            }
         } else {
            this.mayAllocateRuns = true;
         }

      } else {
         throw new IllegalArgumentException();
      }
   }

   private Object getMemory(String var1, Object var2, Class var3, boolean var4, int var5) {
      int var6 = Array.getLength(var2);
      if (var5 == var6) {
         return var2;
      } else if (!var4) {
         if (var5 <= var6) {
            return var2;
         } else {
            throw new OutOfMemoryError("Failed to allocate memory for " + var1);
         }
      } else {
         try {
            return Array.newInstance(var3, var5);
         } catch (Exception var8) {
            throw new OutOfMemoryError("Failed to allocate memory for " + var1);
         }
      }
   }

   private void getDirPropsMemory(boolean var1, int var2) {
      Object var3 = this.getMemory("DirProps", this.dirPropsMemory, Byte.TYPE, var1, var2);
      this.dirPropsMemory = (byte[])((byte[])var3);
   }

   void getDirPropsMemory(int var1) {
      this.getDirPropsMemory(this.mayAllocateText, var1);
   }

   private void getLevelsMemory(boolean var1, int var2) {
      Object var3 = this.getMemory("Levels", this.levelsMemory, Byte.TYPE, var1, var2);
      this.levelsMemory = (byte[])((byte[])var3);
   }

   void getLevelsMemory(int var1) {
      this.getLevelsMemory(this.mayAllocateText, var1);
   }

   private void getRunsMemory(boolean var1, int var2) {
      Object var3 = this.getMemory("Runs", this.runsMemory, BidiRun.class, var1, var2);
      this.runsMemory = (BidiRun[])((BidiRun[])var3);
   }

   void getRunsMemory(int var1) {
      this.getRunsMemory(this.mayAllocateRuns, var1);
   }

   private void getInitialDirPropsMemory(int var1) {
      this.getDirPropsMemory(true, var1);
   }

   private void getInitialLevelsMemory(int var1) {
      this.getLevelsMemory(true, var1);
   }

   private void getInitialParasMemory(int var1) {
      Object var2 = this.getMemory("Paras", this.parasMemory, Integer.TYPE, true, var1);
      this.parasMemory = (int[])((int[])var2);
   }

   private void getInitialRunsMemory(int var1) {
      this.getRunsMemory(true, var1);
   }

   public void setInverse(boolean var1) {
      this.isInverse = var1;
      this.reorderingMode = var1 ? 4 : 0;
   }

   public boolean isInverse() {
      return this.isInverse;
   }

   public void setReorderingMode(int var1) {
      if (var1 >= 0 && var1 < 7) {
         this.reorderingMode = var1;
         this.isInverse = var1 == 4;
      }
   }

   public int getReorderingMode() {
      return this.reorderingMode;
   }

   public void setReorderingOptions(int var1) {
      if ((var1 & 2) != 0) {
         this.reorderingOptions = var1 & -2;
      } else {
         this.reorderingOptions = var1;
      }

   }

   public int getReorderingOptions() {
      return this.reorderingOptions;
   }

   private byte firstL_R_AL() {
      byte var1 = 10;
      int var2 = 0;

      while(true) {
         byte var4;
         label29:
         do {
            while(var2 < this.prologue.length()) {
               int var3 = this.prologue.codePointAt(var2);
               var2 += Character.charCount(var3);
               var4 = (byte)this.getCustomizedClass(var3);
               if (var1 == 10) {
                  continue label29;
               }

               if (var4 == 7) {
                  var1 = 10;
               }
            }

            return var1;
         } while(var4 != 0 && var4 != 1 && var4 != 13);

         var1 = var4;
      }
   }

   private void getDirProps() {
      // $FF: Couldn't be decompiled
   }

   private byte directionFromFlags() {
      if ((this.flags & MASK_RTL) != 0 || (this.flags & DirPropFlag((byte)5)) != 0 && (this.flags & MASK_POSSIBLE_N) != 0) {
         return (byte)((this.flags & MASK_LTR) == 0 ? 1 : 2);
      } else {
         return 0;
      }
   }

   private byte resolveExplicitLevels() {
      boolean var1 = false;
      byte var3 = this.GetParaLevelAt(0);
      int var5 = 0;
      byte var4 = this.directionFromFlags();
      if (var4 == 2 || this.paraCount != 1) {
         int var12;
         if (this.paraCount == 1 && ((this.flags & MASK_EXPLICIT) == 0 || this.reorderingMode > 1)) {
            for(var12 = 0; var12 < this.length; ++var12) {
               this.levels[var12] = var3;
            }
         } else {
            byte var6 = var3;
            byte var8 = 0;
            byte[] var9 = new byte[61];
            int var10 = 0;
            int var11 = 0;
            this.flags = 0;

            for(var12 = 0; var12 < this.length; ++var12) {
               byte var2 = NoContextRTL(this.dirProps[var12]);
               byte var7;
               switch(var2) {
               case 7:
                  var8 = 0;
                  var10 = 0;
                  var11 = 0;
                  var3 = this.GetParaLevelAt(var12);
                  if (var12 + 1 < this.length) {
                     var6 = this.GetParaLevelAt(var12 + 1);
                     if (this.text[var12] != '\r' || this.text[var12 + 1] != '\n') {
                        this.paras[var5++] = var12 + 1;
                     }
                  }

                  this.flags |= DirPropFlag((byte)7);
                  break;
               case 8:
               case 9:
               case 10:
               case 13:
               case 17:
               default:
                  if (var3 != var6) {
                     var3 = var6;
                     if ((var6 & -128) != 0) {
                        this.flags |= DirPropFlagO(var6) | DirPropFlagMultiRuns;
                     } else {
                        this.flags |= DirPropFlagE(var6) | DirPropFlagMultiRuns;
                     }
                  }

                  if ((var3 & -128) == 0) {
                     this.flags |= DirPropFlag(var2);
                  }
                  break;
               case 11:
               case 12:
                  var7 = (byte)(var6 + 2 & 126);
                  if (var7 <= 61) {
                     var9[var8] = var6;
                     ++var8;
                     var6 = var7;
                     if (var2 == 12) {
                        var6 = (byte)(var7 | -128);
                     }
                  } else if ((var6 & 127) == 61) {
                     ++var11;
                  } else {
                     ++var10;
                  }

                  this.flags |= DirPropFlag((byte)18);
                  break;
               case 14:
               case 15:
                  var7 = (byte)((var6 & 127) + 1 | 1);
                  if (var7 <= 61) {
                     var9[var8] = var6;
                     ++var8;
                     var6 = var7;
                     if (var2 == 15) {
                        var6 = (byte)(var7 | -128);
                     }
                  } else {
                     ++var11;
                  }

                  this.flags |= DirPropFlag((byte)18);
                  break;
               case 16:
                  if (var11 > 0) {
                     --var11;
                  } else if (var10 > 0 && (var6 & 127) != 61) {
                     --var10;
                  } else if (var8 > 0) {
                     --var8;
                     var6 = var9[var8];
                  }

                  this.flags |= DirPropFlag((byte)18);
                  break;
               case 18:
                  this.flags |= DirPropFlag((byte)18);
               }

               this.levels[var12] = var3;
            }

            if ((this.flags & MASK_EMBEDDING) != 0) {
               this.flags |= DirPropFlagLR(this.paraLevel);
            }

            if (this.orderParagraphsLTR && (this.flags & DirPropFlag((byte)7)) != 0) {
               this.flags |= DirPropFlag((byte)0);
            }

            var4 = this.directionFromFlags();
         }
      }

      return var4;
   }

   private byte checkExplicitLevels() {
      this.flags = 0;
      int var4 = 0;

      for(int var2 = 0; var2 < this.length; ++var2) {
         byte var3 = this.levels[var2];
         byte var1 = NoContextRTL(this.dirProps[var2]);
         if ((var3 & -128) != 0) {
            var3 = (byte)(var3 & 127);
            this.flags |= DirPropFlagO(var3);
         } else {
            this.flags |= DirPropFlagE(var3) | DirPropFlag(var1);
         }

         if (var3 < this.GetParaLevelAt(var2) && (0 != var3 || var1 != 7) || 61 < var3) {
            throw new IllegalArgumentException("level " + var3 + " out of bounds at " + var2);
         }

         if (var1 == 7 && var2 + 1 < this.length && (this.text[var2] != '\r' || this.text[var2 + 1] != '\n')) {
            this.paras[var4++] = var2 + 1;
         }
      }

      if ((this.flags & MASK_EMBEDDING) != 0) {
         this.flags |= DirPropFlagLR(this.paraLevel);
      }

      return this.directionFromFlags();
   }

   private static short GetStateProps(short var0) {
      return (short)(var0 & 31);
   }

   private static short GetActionProps(short var0) {
      return (short)(var0 >> 5);
   }

   private static short GetState(byte var0) {
      return (short)(var0 & 15);
   }

   private static short GetAction(byte var0) {
      return (short)(var0 >> 4);
   }

   private void addPoint(int var1, int var2) {
      Bidi.Point var3 = new Bidi.Point();
      int var4 = this.insertPoints.points.length;
      if (var4 == 0) {
         this.insertPoints.points = new Bidi.Point[10];
         var4 = 10;
      }

      if (this.insertPoints.size >= var4) {
         Bidi.Point[] var5 = this.insertPoints.points;
         this.insertPoints.points = new Bidi.Point[var4 * 2];
         System.arraycopy(var5, 0, this.insertPoints.points, 0, var4);
      }

      var3.pos = var1;
      var3.flag = var2;
      this.insertPoints.points[this.insertPoints.size] = var3;
      ++this.insertPoints.size;
   }

   private void processPropertySeq(Bidi.LevState var1, short var2, int var3, int var4) {
      byte[][] var6 = var1.impTab;
      short[] var7 = var1.impAct;
      int var12 = var3;
      short var8 = var1.state;
      byte var5 = var6[var8][var2];
      var1.state = GetState(var5);
      short var9 = var7[GetAction(var5)];
      byte var11 = var6[var1.state][7];
      byte var10;
      int var13;
      if (var9 != 0) {
         byte[] var10000;
         label158:
         switch(var9) {
         case 1:
            var1.startON = var3;
            break;
         case 2:
            var3 = var1.startON;
            break;
         case 3:
            if (var1.startL2EN >= 0) {
               this.addPoint(var1.startL2EN, 1);
            }

            var1.startL2EN = -1;
            if (this.insertPoints.points.length != 0 && this.insertPoints.size > this.insertPoints.confirmed) {
               for(var13 = var1.lastStrongRTL + 1; var13 < var12; ++var13) {
                  this.levels[var13] = (byte)(this.levels[var13] - 2 & -2);
               }

               this.insertPoints.confirmed = this.insertPoints.size;
               var1.lastStrongRTL = -1;
               if (var2 == 5) {
                  this.addPoint(var12, 1);
                  this.insertPoints.confirmed = this.insertPoints.size;
               }
            } else {
               var1.lastStrongRTL = -1;
               var10 = var6[var8][7];
               if ((var10 & 1) != 0 && var1.startON > 0) {
                  var3 = var1.startON;
               }

               if (var2 == 5) {
                  this.addPoint(var3, 1);
                  this.insertPoints.confirmed = this.insertPoints.size;
               }
            }
            break;
         case 4:
            if (this.insertPoints.points.length > 0) {
               this.insertPoints.size = this.insertPoints.confirmed;
            }

            var1.startON = -1;
            var1.startL2EN = -1;
            var1.lastStrongRTL = var4 - 1;
            break;
         case 5:
            if (var2 == 3 && NoContextRTL(this.dirProps[var3]) == 5 && this.reorderingMode != 6) {
               if (var1.startL2EN == -1) {
                  var1.lastStrongRTL = var4 - 1;
               } else {
                  if (var1.startL2EN >= 0) {
                     this.addPoint(var1.startL2EN, 1);
                     var1.startL2EN = -2;
                  }

                  this.addPoint(var3, 1);
               }
            } else if (var1.startL2EN == -1) {
               var1.startL2EN = var3;
            }
            break;
         case 6:
            var1.lastStrongRTL = var4 - 1;
            var1.startON = -1;
            break;
         case 7:
            for(var13 = var3 - 1; var13 >= 0 && (this.levels[var13] & 1) == 0; --var13) {
            }

            if (var13 >= 0) {
               this.addPoint(var13, 4);
               this.insertPoints.confirmed = this.insertPoints.size;
            }

            var1.startON = var3;
            break;
         case 8:
            this.addPoint(var3, 1);
            this.addPoint(var3, 2);
            break;
         case 9:
            this.insertPoints.size = this.insertPoints.confirmed;
            if (var2 == 5) {
               this.addPoint(var3, 4);
               this.insertPoints.confirmed = this.insertPoints.size;
            }
            break;
         case 10:
            var10 = (byte)(var1.runLevel + var11);

            for(var13 = var1.startON; var13 < var12; ++var13) {
               if (this.levels[var13] < var10) {
                  this.levels[var13] = var10;
               }
            }

            this.insertPoints.confirmed = this.insertPoints.size;
            var1.startON = var12;
            break;
         case 11:
            var10 = var1.runLevel;
            var13 = var3 - 1;

            while(true) {
               if (var13 < var1.startON) {
                  break label158;
               }

               if (this.levels[var13] == var10 + 3) {
                  while(this.levels[var13] == var10 + 3) {
                     var10000 = this.levels;
                     int var10001 = var13--;
                     var10000[var10001] = (byte)(var10000[var10001] - 2);
                  }

                  while(this.levels[var13] == var10) {
                     --var13;
                  }
               }

               if (this.levels[var13] == var10 + 2) {
                  this.levels[var13] = var10;
               } else {
                  this.levels[var13] = (byte)(var10 + 1);
               }

               --var13;
            }
         case 12:
            var10 = (byte)(var1.runLevel + 1);
            var13 = var3 - 1;

            while(true) {
               if (var13 < var1.startON) {
                  break label158;
               }

               if (this.levels[var13] > var10) {
                  var10000 = this.levels;
                  var10000[var13] = (byte)(var10000[var13] - 2);
               }

               --var13;
            }
         default:
            throw new IllegalStateException("Internal ICU error in processPropertySeq");
         }
      }

      if (var11 != 0 || var3 < var12) {
         var10 = (byte)(var1.runLevel + var11);

         for(var13 = var3; var13 < var4; ++var13) {
            this.levels[var13] = var10;
         }
      }

   }

   private byte lastL_R_AL() {
      int var1 = this.prologue.length();

      byte var3;
      do {
         if (var1 <= 0) {
            return 4;
         }

         int var2 = this.prologue.codePointBefore(var1);
         var1 -= Character.charCount(var2);
         var3 = (byte)this.getCustomizedClass(var2);
         if (var3 == 0) {
            return 0;
         }

         if (var3 == 1 || var3 == 13) {
            return 1;
         }
      } while(var3 != 7);

      return 4;
   }

   private byte firstL_R_AL_EN_AN() {
      int var1 = 0;

      byte var3;
      do {
         if (var1 >= this.epilogue.length()) {
            return 4;
         }

         int var2 = this.epilogue.codePointAt(var1);
         var1 += Character.charCount(var2);
         var3 = (byte)this.getCustomizedClass(var2);
         if (var3 == 0) {
            return 0;
         }

         if (var3 == 1 || var3 == 13) {
            return 1;
         }

         if (var3 == 2) {
            return 2;
         }
      } while(var3 != 5);

      return 3;
   }

   private void resolveImplicitLevels(int var1, int var2, short var3, short var4) {
      Bidi.LevState var5 = new Bidi.LevState();
      short var16 = 1;
      int var17 = -1;
      boolean var15 = var1 < this.lastArabicPos && (this.GetParaLevelAt(var1) & 1) > 0 && (this.reorderingMode == 5 || this.reorderingMode == 6);
      var5.startL2EN = -1;
      var5.lastStrongRTL = -1;
      var5.state = 0;
      var5.runLevel = this.levels[var1];
      var5.impTab = this.impTabPair.imptab[var5.runLevel & 1];
      var5.impAct = this.impTabPair.impact[var5.runLevel & 1];
      byte var18;
      if (var1 == 0 && this.prologue != null) {
         var18 = this.lastL_R_AL();
         if (var18 != 4) {
            var3 = (short)var18;
         }
      }

      this.processPropertySeq(var5, var3, var1, var1);
      short var10;
      if (NoContextRTL(this.dirProps[var1]) == 17) {
         var10 = (short)(1 + var3);
      } else {
         var10 = 0;
      }

      int var7 = var1;
      int var8 = 0;

      for(int var6 = var1; var6 <= var2; ++var6) {
         short var12;
         if (var6 >= var2) {
            var12 = var4;
         } else {
            short var21 = (short)NoContextRTL(this.dirProps[var6]);
            if (var15) {
               if (var21 == 13) {
                  var21 = 1;
               } else if (var21 == 2) {
                  if (var17 <= var6) {
                     var16 = 1;
                     var17 = var2;

                     for(int var20 = var6 + 1; var20 < var2; ++var20) {
                        short var19 = (short)NoContextRTL(this.dirProps[var20]);
                        if (var19 == 0 || var19 == 1 || var19 == 13) {
                           var16 = var19;
                           var17 = var20;
                           break;
                        }
                     }
                  }

                  if (var16 == 13) {
                     var21 = 5;
                  }
               }
            }

            var12 = groupProp[var21];
         }

         short var9 = var10;
         short var14 = impTabProps[var10][var12];
         var10 = GetStateProps(var14);
         short var11 = GetActionProps(var14);
         if (var6 == var2 && var11 == 0) {
            var11 = 1;
         }

         if (var11 != 0) {
            short var13 = impTabProps[var9][13];
            switch(var11) {
            case 1:
               this.processPropertySeq(var5, var13, var7, var6);
               var7 = var6;
               break;
            case 2:
               var8 = var6;
               break;
            case 3:
               this.processPropertySeq(var5, var13, var7, var8);
               this.processPropertySeq(var5, (short)4, var8, var6);
               var7 = var6;
               break;
            case 4:
               this.processPropertySeq(var5, var13, var7, var8);
               var7 = var8;
               var8 = var6;
               break;
            default:
               throw new IllegalStateException("Internal ICU error in resolveImplicitLevels");
            }
         }
      }

      if (var2 == this.length && this.epilogue != null) {
         var18 = this.firstL_R_AL_EN_AN();
         if (var18 != 4) {
            var4 = (short)var18;
         }
      }

      this.processPropertySeq(var5, var4, var2, var2);
   }

   private void adjustWSLevels() {
      if ((this.flags & MASK_WS) != 0) {
         int var1 = this.trailingWSStart;

         while(true) {
            while(var1 > 0) {
               int var2;
               label35:
               while(true) {
                  while(true) {
                     if (var1 <= 0) {
                        break label35;
                     }

                     --var1;
                     if (((var2 = DirPropFlagNC(this.dirProps[var1])) & MASK_WS) == 0) {
                        break label35;
                     }

                     if (this.orderParagraphsLTR && (var2 & DirPropFlag((byte)7)) != 0) {
                        this.levels[var1] = 0;
                     } else {
                        this.levels[var1] = this.GetParaLevelAt(var1);
                     }
                  }
               }

               while(var1 > 0) {
                  --var1;
                  var2 = DirPropFlagNC(this.dirProps[var1]);
                  if ((var2 & MASK_BN_EXPLICIT) != 0) {
                     this.levels[var1] = this.levels[var1 + 1];
                  } else {
                     if (this.orderParagraphsLTR && (var2 & DirPropFlag((byte)7)) != 0) {
                        this.levels[var1] = 0;
                        break;
                     }

                     if ((var2 & MASK_B_S) != 0) {
                        this.levels[var1] = this.GetParaLevelAt(var1);
                        break;
                     }
                  }
               }
            }

            return;
         }
      }
   }

   int Bidi_Min(int var1, int var2) {
      return var1 < var2 ? var1 : var2;
   }

   int Bidi_Abs(int var1) {
      return var1 >= 0 ? var1 : -var1;
   }

   void setParaRunsOnly(char[] var1, byte var2) {
      this.reorderingMode = 0;
      int var25 = var1.length;
      if (var25 == 0) {
         this.setPara((char[])var1, var2, (byte[])null);
         this.reorderingMode = 3;
      } else {
         int var24 = this.reorderingOptions;
         if ((var24 & 1) > 0) {
            this.reorderingOptions &= -2;
            this.reorderingOptions |= 2;
         }

         var2 = (byte)(var2 & 1);
         this.setPara((char[])var1, var2, (byte[])null);
         byte[] var7 = new byte[this.length];
         System.arraycopy(this.getLevels(), 0, var7, 0, this.length);
         int var6 = this.trailingWSStart;
         String var4 = this.writeReordered(2);
         int[] var3 = this.getVisualMap();
         this.reorderingOptions = var24;
         int var5 = this.length;
         byte var8 = this.direction;
         this.reorderingMode = 5;
         var2 = (byte)(var2 ^ 1);
         this.setPara((String)var4, var2, (byte[])null);
         BidiLine.getRuns(this);
         int var15 = 0;
         int var13 = this.runCount;
         int var11 = 0;

         int var9;
         int var10;
         int var12;
         int var14;
         int var22;
         int var23;
         for(var9 = 0; var9 < var13; var11 += var14) {
            var14 = this.runs[var9].limit - var11;
            if (var14 >= 2) {
               var12 = this.runs[var9].start;

               for(var10 = var12 + 1; var10 < var12 + var14; ++var10) {
                  var22 = var3[var10];
                  var23 = var3[var10 - 1];
                  if (this.Bidi_Abs(var22 - var23) != 1 || var7[var22] != var7[var23]) {
                     ++var15;
                  }
               }
            }

            ++var9;
         }

         if (var15 > 0) {
            this.getRunsMemory(var13 + var15);
            if (this.runCount == 1) {
               this.runsMemory[0] = this.runs[0];
            } else {
               System.arraycopy(this.runs, 0, this.runsMemory, 0, this.runCount);
            }

            this.runs = this.runsMemory;
            this.runCount += var15;

            for(var9 = var13; var9 < this.runCount; ++var9) {
               if (this.runs[var9] == null) {
                  this.runs[var9] = new BidiRun(0, 0, (byte)0);
               }
            }
         }

         for(var9 = var13 - 1; var9 >= 0; --var9) {
            int var26 = var9 + var15;
            var14 = var9 == 0 ? this.runs[0].limit : this.runs[var9].limit - this.runs[var9 - 1].limit;
            var12 = this.runs[var9].start;
            int var20 = this.runs[var9].level & 1;
            int var21;
            if (var14 < 2) {
               if (var15 > 0) {
                  this.runs[var26].copyFrom(this.runs[var9]);
               }

               var21 = var3[var12];
               this.runs[var26].start = var21;
               this.runs[var26].level = (byte)(var7[var21] ^ var20);
            } else {
               int var17;
               int var18;
               byte var19;
               if (var20 > 0) {
                  var17 = var12;
                  var18 = var12 + var14 - 1;
                  var19 = 1;
               } else {
                  var17 = var12 + var14 - 1;
                  var18 = var12;
                  var19 = -1;
               }

               for(var10 = var17; var10 != var18; var10 += var19) {
                  var22 = var3[var10];
                  var23 = var3[var10 + var19];
                  if (this.Bidi_Abs(var22 - var23) != 1 || var7[var22] != var7[var23]) {
                     var21 = this.Bidi_Min(var3[var17], var22);
                     this.runs[var26].start = var21;
                     this.runs[var26].level = (byte)(var7[var21] ^ var20);
                     this.runs[var26].limit = this.runs[var9].limit;
                     BidiRun var10000 = this.runs[var9];
                     var10000.limit -= this.Bidi_Abs(var10 - var17) + 1;
                     int var16 = this.runs[var9].insertRemove & 10;
                     this.runs[var26].insertRemove = var16;
                     var10000 = this.runs[var9];
                     var10000.insertRemove &= ~var16;
                     var17 = var10 + var19;
                     --var15;
                     --var26;
                  }
               }

               if (var15 > 0) {
                  this.runs[var26].copyFrom(this.runs[var9]);
               }

               var21 = this.Bidi_Min(var3[var17], var3[var18]);
               this.runs[var26].start = var21;
               this.runs[var26].level = (byte)(var7[var21] ^ var20);
            }
         }

         this.paraLevel = (byte)(this.paraLevel ^ 1);
         this.text = var1;
         this.length = var5;
         this.originalLength = var25;
         this.direction = var8;
         this.levels = var7;
         this.trailingWSStart = var6;
         if (this.runCount > 1) {
            this.direction = 2;
         }

         this.reorderingMode = 3;
      }
   }

   private void setParaSuccess() {
      this.prologue = null;
      this.epilogue = null;
      this.paraBidi = this;
   }

   public void setContext(String var1, String var2) {
      this.prologue = var1 != null && var1.length() > 0 ? var1 : null;
      this.epilogue = var2 != null && var2.length() > 0 ? var2 : null;
   }

   public void setPara(String var1, byte var2, byte[] var3) {
      if (var1 == null) {
         this.setPara(new char[0], var2, var3);
      } else {
         this.setPara(var1.toCharArray(), var2, var3);
      }

   }

   public void setPara(char[] param1, byte param2, byte[] param3) {
      // $FF: Couldn't be decompiled
   }

   public void setPara(AttributedCharacterIterator var1) {
      Boolean var3 = (Boolean)var1.getAttribute(TextAttribute.RUN_DIRECTION);
      int var2;
      if (var3 == null) {
         var2 = 126;
      } else {
         var2 = var3.equals(TextAttribute.RUN_DIRECTION_LTR) ? 0 : 1;
      }

      byte[] var4 = null;
      int var5 = var1.getEndIndex() - var1.getBeginIndex();
      byte[] var6 = new byte[var5];
      char[] var7 = new char[var5];
      int var8 = 0;

      for(char var9 = var1.first(); var9 != '\uffff'; ++var8) {
         var7[var8] = var9;
         Integer var10 = (Integer)var1.getAttribute(TextAttribute.BIDI_EMBEDDING);
         if (var10 != null) {
            byte var11 = var10.byteValue();
            if (var11 != 0) {
               if (var11 < 0) {
                  var4 = var6;
                  var6[var8] = (byte)(0 - var11 | -128);
               } else {
                  var4 = var6;
                  var6[var8] = var11;
               }
            }
         }

         var9 = var1.next();
      }

      NumericShaper var12 = (NumericShaper)var1.getAttribute(TextAttribute.NUMERIC_SHAPING);
      if (var12 != null) {
         var12.shape(var7, 0, var5);
      }

      this.setPara((char[])var7, (byte)var2, var4);
   }

   public void orderParagraphsLTR(boolean var1) {
      this.orderParagraphsLTR = var1;
   }

   public boolean isOrderParagraphsLTR() {
      return this.orderParagraphsLTR;
   }

   public byte getDirection() {
      this.verifyValidParaOrLine();
      return this.direction;
   }

   public String getTextAsString() {
      this.verifyValidParaOrLine();
      return new String(this.text);
   }

   public char[] getText() {
      this.verifyValidParaOrLine();
      return this.text;
   }

   public int getLength() {
      this.verifyValidParaOrLine();
      return this.originalLength;
   }

   public int getProcessedLength() {
      this.verifyValidParaOrLine();
      return this.length;
   }

   public int getResultLength() {
      this.verifyValidParaOrLine();
      return this.resultLength;
   }

   public byte getParaLevel() {
      this.verifyValidParaOrLine();
      return this.paraLevel;
   }

   public int countParagraphs() {
      this.verifyValidParaOrLine();
      return this.paraCount;
   }

   public BidiRun getParagraphByIndex(int var1) {
      this.verifyValidParaOrLine();
      this.verifyRange(var1, 0, this.paraCount);
      Bidi var2 = this.paraBidi;
      int var3;
      if (var1 == 0) {
         var3 = 0;
      } else {
         var3 = var2.paras[var1 - 1];
      }

      BidiRun var4 = new BidiRun();
      var4.start = var3;
      var4.limit = var2.paras[var1];
      var4.level = this.GetParaLevelAt(var3);
      return var4;
   }

   public BidiRun getParagraph(int var1) {
      this.verifyValidParaOrLine();
      Bidi var2 = this.paraBidi;
      this.verifyRange(var1, 0, var2.length);

      int var3;
      for(var3 = 0; var1 >= var2.paras[var3]; ++var3) {
      }

      return this.getParagraphByIndex(var3);
   }

   public int getParagraphIndex(int var1) {
      this.verifyValidParaOrLine();
      Bidi var2 = this.paraBidi;
      this.verifyRange(var1, 0, var2.length);

      int var3;
      for(var3 = 0; var1 >= var2.paras[var3]; ++var3) {
      }

      return var3;
   }

   public void setCustomClassifier(BidiClassifier var1) {
      this.customClassifier = var1;
   }

   public BidiClassifier getCustomClassifier() {
      return this.customClassifier;
   }

   public int getCustomizedClass(int var1) {
      int var2;
      return this.customClassifier != null && (var2 = this.customClassifier.classify(var1)) != 19 ? var2 : this.bdp.getClass(var1);
   }

   public Bidi setLine(int var1, int var2) {
      this.verifyValidPara();
      this.verifyRange(var1, 0, var2);
      this.verifyRange(var2, 0, this.length + 1);
      if (this.getParagraphIndex(var1) != this.getParagraphIndex(var2 - 1)) {
         throw new IllegalArgumentException();
      } else {
         return BidiLine.setLine(this, var1, var2);
      }
   }

   public byte getLevelAt(int var1) {
      this.verifyValidParaOrLine();
      this.verifyRange(var1, 0, this.length);
      return BidiLine.getLevelAt(this, var1);
   }

   public byte[] getLevels() {
      this.verifyValidParaOrLine();
      return this.length <= 0 ? new byte[0] : BidiLine.getLevels(this);
   }

   public BidiRun getLogicalRun(int var1) {
      this.verifyValidParaOrLine();
      this.verifyRange(var1, 0, this.length);
      return BidiLine.getLogicalRun(this, var1);
   }

   public int countRuns() {
      this.verifyValidParaOrLine();
      BidiLine.getRuns(this);
      return this.runCount;
   }

   public BidiRun getVisualRun(int var1) {
      this.verifyValidParaOrLine();
      BidiLine.getRuns(this);
      this.verifyRange(var1, 0, this.runCount);
      return BidiLine.getVisualRun(this, var1);
   }

   public int getVisualIndex(int var1) {
      this.verifyValidParaOrLine();
      this.verifyRange(var1, 0, this.length);
      return BidiLine.getVisualIndex(this, var1);
   }

   public int getLogicalIndex(int var1) {
      this.verifyValidParaOrLine();
      this.verifyRange(var1, 0, this.resultLength);
      if (this.insertPoints.size == 0 && this.controlCount == 0) {
         if (this.direction == 0) {
            return var1;
         }

         if (this.direction == 1) {
            return this.length - var1 - 1;
         }
      }

      BidiLine.getRuns(this);
      return BidiLine.getLogicalIndex(this, var1);
   }

   public int[] getLogicalMap() {
      this.countRuns();
      return this.length <= 0 ? new int[0] : BidiLine.getLogicalMap(this);
   }

   public int[] getVisualMap() {
      this.countRuns();
      return this.resultLength <= 0 ? new int[0] : BidiLine.getVisualMap(this);
   }

   public static int[] reorderLogical(byte[] var0) {
      return BidiLine.reorderLogical(var0);
   }

   public static int[] reorderVisual(byte[] var0) {
      return BidiLine.reorderVisual(var0);
   }

   public static int[] invertMap(int[] var0) {
      return var0 == null ? null : BidiLine.invertMap(var0);
   }

   public Bidi(String var1, int var2) {
      this(var1.toCharArray(), 0, (byte[])null, 0, var1.length(), var2);
   }

   public Bidi(AttributedCharacterIterator var1) {
      this();
      this.setPara(var1);
   }

   public Bidi(char[] var1, int var2, byte[] var3, int var4, int var5, int var6) {
      this();
      byte var7;
      switch(var6) {
      case 0:
      default:
         var7 = 0;
         break;
      case 1:
         var7 = 1;
         break;
      case 126:
         var7 = 126;
         break;
      case 127:
         var7 = 127;
      }

      byte[] var8;
      if (var3 == null) {
         var8 = null;
      } else {
         var8 = new byte[var5];

         for(int var10 = 0; var10 < var5; ++var10) {
            byte var9 = var3[var10 + var4];
            if (var9 < 0) {
               var9 = (byte)(-var9 | -128);
            } else if (var9 == 0) {
               var9 = var7;
               if (var7 > 61) {
                  var9 = (byte)(var7 & 1);
               }
            }

            var8[var10] = var9;
         }
      }

      if (var2 == 0 && var4 == 0 && var5 == var1.length) {
         this.setPara(var1, var7, var8);
      } else {
         char[] var11 = new char[var5];
         System.arraycopy(var1, var2, var11, 0, var5);
         this.setPara(var11, var7, var8);
      }

   }

   public Bidi createLineBidi(int var1, int var2) {
      return this.setLine(var1, var2);
   }

   public boolean isMixed() {
      // $FF: Couldn't be decompiled
   }

   public boolean baseIsLeftToRight() {
      return this.getParaLevel() == 0;
   }

   public int getBaseLevel() {
      return this.getParaLevel();
   }

   public int getRunCount() {
      return this.countRuns();
   }

   void getLogicalToVisualRunsMap() {
      if (!this.isGoodLogicalToVisualRunsMap) {
         int var1 = this.countRuns();
         if (this.logicalToVisualRunsMap == null || this.logicalToVisualRunsMap.length < var1) {
            this.logicalToVisualRunsMap = new int[var1];
         }

         long[] var3 = new long[var1];

         int var2;
         for(var2 = 0; var2 < var1; ++var2) {
            var3[var2] = ((long)this.runs[var2].start << 32) + (long)var2;
         }

         Arrays.sort(var3);

         for(var2 = 0; var2 < var1; ++var2) {
            this.logicalToVisualRunsMap[var2] = (int)(var3[var2] & -1L);
         }

         this.isGoodLogicalToVisualRunsMap = true;
      }
   }

   public int getRunLevel(int var1) {
      this.verifyValidParaOrLine();
      BidiLine.getRuns(this);
      this.verifyRange(var1, 0, this.runCount);
      this.getLogicalToVisualRunsMap();
      return this.runs[this.logicalToVisualRunsMap[var1]].level;
   }

   public int getRunStart(int var1) {
      this.verifyValidParaOrLine();
      BidiLine.getRuns(this);
      this.verifyRange(var1, 0, this.runCount);
      this.getLogicalToVisualRunsMap();
      return this.runs[this.logicalToVisualRunsMap[var1]].start;
   }

   public int getRunLimit(int var1) {
      this.verifyValidParaOrLine();
      BidiLine.getRuns(this);
      this.verifyRange(var1, 0, this.runCount);
      this.getLogicalToVisualRunsMap();
      int var2 = this.logicalToVisualRunsMap[var1];
      int var3 = var2 == 0 ? this.runs[var2].limit : this.runs[var2].limit - this.runs[var2 - 1].limit;
      return this.runs[var2].start + var3;
   }

   public static boolean requiresBidi(char[] var0, int var1, int var2) {
      char var3 = '\ue022';

      for(int var4 = var1; var4 < var2; ++var4) {
         if ((1 << UCharacter.getDirection(var0[var4]) & '\ue022') != 0) {
            return true;
         }
      }

      return false;
   }

   public static void reorderVisually(byte[] var0, int var1, Object[] var2, int var3, int var4) {
      byte[] var5 = new byte[var4];
      System.arraycopy(var0, var1, var5, 0, var4);
      int[] var6 = reorderVisual(var5);
      Object[] var7 = new Object[var4];
      System.arraycopy(var2, var3, var7, 0, var4);

      for(int var8 = 0; var8 < var4; ++var8) {
         var2[var3 + var8] = var7[var6[var8]];
      }

   }

   public String writeReordered(int var1) {
      this.verifyValidParaOrLine();
      return this.length == 0 ? "" : BidiWriter.writeReordered(this, var1);
   }

   public static String writeReverse(String var0, int var1) {
      if (var0 == null) {
         throw new IllegalArgumentException();
      } else {
         return var0.length() > 0 ? BidiWriter.writeReverse(var0, var1) : "";
      }
   }

   public static byte getBaseDirection(CharSequence var0) {
      if (var0 != null && var0.length() != 0) {
         int var1 = var0.length();

         for(int var4 = 0; var4 < var1; var4 = UCharacter.offsetByCodePoints(var0, var4, 1)) {
            int var2 = UCharacter.codePointAt(var0, var4);
            byte var3 = UCharacter.getDirectionality(var2);
            if (var3 == 0) {
               return 0;
            }

            if (var3 == 1 || var3 == 13) {
               return 1;
            }
         }

         return 3;
      } else {
         return 3;
      }
   }

   static {
      MASK_EXPLICIT = MASK_LRX | MASK_RLX | DirPropFlag((byte)16);
      MASK_BN_EXPLICIT = DirPropFlag((byte)18) | MASK_EXPLICIT;
      MASK_B_S = DirPropFlag((byte)7) | DirPropFlag((byte)8);
      MASK_WS = MASK_B_S | DirPropFlag((byte)9) | MASK_BN_EXPLICIT;
      MASK_N = DirPropFlag((byte)10) | MASK_WS;
      MASK_ET_NSM_BN = DirPropFlag((byte)4) | DirPropFlag((byte)17) | MASK_BN_EXPLICIT;
      MASK_POSSIBLE_N = DirPropFlag((byte)6) | DirPropFlag((byte)3) | DirPropFlag((byte)4) | MASK_N;
      MASK_EMBEDDING = DirPropFlag((byte)17) | MASK_POSSIBLE_N;
      groupProp = new short[]{0, 1, 2, 7, 8, 3, 9, 6, 5, 4, 4, 10, 10, 12, 10, 10, 10, 11, 10};
      impTabProps = new short[][]{{1, 2, 4, 5, 7, 15, 17, 7, 9, 7, 0, 7, 3, 4}, {1, 34, 36, 37, 39, 47, 49, 39, 41, 39, 1, 1, 35, 0}, {33, 2, 36, 37, 39, 47, 49, 39, 41, 39, 2, 2, 35, 1}, {33, 34, 38, 38, 40, 48, 49, 40, 40, 40, 3, 3, 3, 1}, {33, 34, 4, 37, 39, 47, 49, 74, 11, 74, 4, 4, 35, 2}, {33, 34, 36, 5, 39, 47, 49, 39, 41, 76, 5, 5, 35, 3}, {33, 34, 6, 6, 40, 48, 49, 40, 40, 77, 6, 6, 35, 3}, {33, 34, 36, 37, 7, 47, 49, 7, 78, 7, 7, 7, 35, 4}, {33, 34, 38, 38, 8, 48, 49, 8, 8, 8, 8, 8, 35, 4}, {33, 34, 4, 37, 7, 47, 49, 7, 9, 7, 9, 9, 35, 4}, {97, 98, 4, 101, 135, 111, 113, 135, 142, 135, 10, 135, 99, 2}, {33, 34, 4, 37, 39, 47, 49, 39, 11, 39, 11, 11, 35, 2}, {97, 98, 100, 5, 135, 111, 113, 135, 142, 135, 12, 135, 99, 3}, {97, 98, 6, 6, 136, 112, 113, 136, 136, 136, 13, 136, 99, 3}, {33, 34, 132, 37, 7, 47, 49, 7, 14, 7, 14, 14, 35, 4}, {33, 34, 36, 37, 39, 15, 49, 39, 41, 39, 15, 39, 35, 5}, {33, 34, 38, 38, 40, 16, 49, 40, 40, 40, 16, 40, 35, 5}, {33, 34, 36, 37, 39, 47, 17, 39, 41, 39, 17, 39, 35, 6}};
      impTabL_DEFAULT = new byte[][]{{0, 1, 0, 2, 0, 0, 0, 0}, {0, 1, 3, 3, 20, 20, 0, 1}, {0, 1, 0, 2, 21, 21, 0, 2}, {0, 1, 3, 3, 20, 20, 0, 2}, {32, 1, 3, 3, 4, 4, 32, 1}, {32, 1, 32, 2, 5, 5, 32, 1}};
      impTabR_DEFAULT = new byte[][]{{1, 0, 2, 2, 0, 0, 0, 0}, {1, 0, 1, 3, 20, 20, 0, 1}, {1, 0, 2, 2, 0, 0, 0, 1}, {1, 0, 1, 3, 5, 5, 0, 1}, {33, 0, 33, 3, 4, 4, 0, 0}, {1, 0, 1, 3, 5, 5, 0, 0}};
      impAct0 = new short[]{0, 1, 2, 3, 4, 5, 6};
      impTab_DEFAULT = new Bidi.ImpTabPair(impTabL_DEFAULT, impTabR_DEFAULT, impAct0, impAct0);
      impTabL_NUMBERS_SPECIAL = new byte[][]{{0, 2, 1, 1, 0, 0, 0, 0}, {0, 2, 1, 1, 0, 0, 0, 2}, {0, 2, 4, 4, 19, 0, 0, 1}, {32, 2, 4, 4, 3, 3, 32, 1}, {0, 2, 4, 4, 19, 19, 0, 2}};
      impTab_NUMBERS_SPECIAL = new Bidi.ImpTabPair(impTabL_NUMBERS_SPECIAL, impTabR_DEFAULT, impAct0, impAct0);
      impTabL_GROUP_NUMBERS_WITH_R = new byte[][]{{0, 3, 17, 17, 0, 0, 0, 0}, {32, 3, 1, 1, 2, 32, 32, 2}, {32, 3, 1, 1, 2, 32, 32, 1}, {0, 3, 5, 5, 20, 0, 0, 1}, {32, 3, 5, 5, 4, 32, 32, 1}, {0, 3, 5, 5, 20, 0, 0, 2}};
      impTabR_GROUP_NUMBERS_WITH_R = new byte[][]{{2, 0, 1, 1, 0, 0, 0, 0}, {2, 0, 1, 1, 0, 0, 0, 1}, {2, 0, 20, 20, 19, 0, 0, 1}, {34, 0, 4, 4, 3, 0, 0, 0}, {34, 0, 4, 4, 3, 0, 0, 1}};
      impTab_GROUP_NUMBERS_WITH_R = new Bidi.ImpTabPair(impTabL_GROUP_NUMBERS_WITH_R, impTabR_GROUP_NUMBERS_WITH_R, impAct0, impAct0);
      impTabL_INVERSE_NUMBERS_AS_L = new byte[][]{{0, 1, 0, 0, 0, 0, 0, 0}, {0, 1, 0, 0, 20, 20, 0, 1}, {0, 1, 0, 0, 21, 21, 0, 2}, {0, 1, 0, 0, 20, 20, 0, 2}, {32, 1, 32, 32, 4, 4, 32, 1}, {32, 1, 32, 32, 5, 5, 32, 1}};
      impTabR_INVERSE_NUMBERS_AS_L = new byte[][]{{1, 0, 1, 1, 0, 0, 0, 0}, {1, 0, 1, 1, 20, 20, 0, 1}, {1, 0, 1, 1, 0, 0, 0, 1}, {1, 0, 1, 1, 5, 5, 0, 1}, {33, 0, 33, 33, 4, 4, 0, 0}, {1, 0, 1, 1, 5, 5, 0, 0}};
      impTab_INVERSE_NUMBERS_AS_L = new Bidi.ImpTabPair(impTabL_INVERSE_NUMBERS_AS_L, impTabR_INVERSE_NUMBERS_AS_L, impAct0, impAct0);
      impTabR_INVERSE_LIKE_DIRECT = new byte[][]{{1, 0, 2, 2, 0, 0, 0, 0}, {1, 0, 1, 2, 19, 19, 0, 1}, {1, 0, 2, 2, 0, 0, 0, 1}, {33, 48, 6, 4, 3, 3, 48, 0}, {33, 48, 6, 4, 5, 5, 48, 3}, {33, 48, 6, 4, 5, 5, 48, 2}, {33, 48, 6, 4, 3, 3, 48, 1}};
      impAct1 = new short[]{0, 1, 11, 12};
      impTab_INVERSE_LIKE_DIRECT = new Bidi.ImpTabPair(impTabL_DEFAULT, impTabR_INVERSE_LIKE_DIRECT, impAct0, impAct1);
      impTabL_INVERSE_LIKE_DIRECT_WITH_MARKS = new byte[][]{{0, 99, 0, 1, 0, 0, 0, 0}, {0, 99, 0, 1, 18, 48, 0, 4}, {32, 99, 32, 1, 2, 48, 32, 3}, {0, 99, 85, 86, 20, 48, 0, 3}, {48, 67, 85, 86, 4, 48, 48, 3}, {48, 67, 5, 86, 20, 48, 48, 4}, {48, 67, 85, 6, 20, 48, 48, 4}};
      impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS = new byte[][]{{19, 0, 1, 1, 0, 0, 0, 0}, {35, 0, 1, 1, 2, 64, 0, 1}, {35, 0, 1, 1, 2, 64, 0, 0}, {3, 0, 3, 54, 20, 64, 0, 1}, {83, 64, 5, 54, 4, 64, 64, 0}, {83, 64, 5, 54, 4, 64, 64, 1}, {83, 64, 6, 6, 4, 64, 64, 3}};
      impAct2 = new short[]{0, 1, 7, 8, 9, 10};
      impTab_INVERSE_LIKE_DIRECT_WITH_MARKS = new Bidi.ImpTabPair(impTabL_INVERSE_LIKE_DIRECT_WITH_MARKS, impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS, impAct0, impAct2);
      impTab_INVERSE_FOR_NUMBERS_SPECIAL = new Bidi.ImpTabPair(impTabL_NUMBERS_SPECIAL, impTabR_INVERSE_LIKE_DIRECT, impAct0, impAct1);
      impTabL_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS = new byte[][]{{0, 98, 1, 1, 0, 0, 0, 0}, {0, 98, 1, 1, 0, 48, 0, 4}, {0, 98, 84, 84, 19, 48, 0, 3}, {48, 66, 84, 84, 3, 48, 48, 3}, {48, 66, 4, 4, 19, 48, 48, 4}};
      impTab_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS = new Bidi.ImpTabPair(impTabL_INVERSE_FOR_NUMBERS_SPECIAL_WITH_MARKS, impTabR_INVERSE_LIKE_DIRECT_WITH_MARKS, impAct0, impAct2);
   }

   private static class LevState {
      byte[][] impTab;
      short[] impAct;
      int startON;
      int startL2EN;
      int lastStrongRTL;
      short state;
      byte runLevel;

      private LevState() {
      }

      LevState(Object var1) {
         this();
      }
   }

   private static class ImpTabPair {
      byte[][][] imptab;
      short[][] impact;

      ImpTabPair(byte[][] var1, byte[][] var2, short[] var3, short[] var4) {
         this.imptab = new byte[][][]{var1, var2};
         this.impact = new short[][]{var3, var4};
      }
   }

   static class InsertPoints {
      int size;
      int confirmed;
      Bidi.Point[] points = new Bidi.Point[0];
   }

   static class Point {
      int pos;
      int flag;
   }
}
