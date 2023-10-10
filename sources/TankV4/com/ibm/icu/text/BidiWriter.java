package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;

final class BidiWriter {
   static final char LRM_CHAR = '\u200e';
   static final char RLM_CHAR = '\u200f';
   static final int MASK_R_AL = 8194;

   private static String doWriteForward(String var0, int var1) {
      int var3;
      StringBuffer var5;
      int var6;
      switch(var1 & 10) {
      case 0:
         return var0;
      case 2:
         var5 = new StringBuffer(var0.length());
         var3 = 0;

         do {
            var6 = UTF16.charAt(var0, var3);
            var3 += UTF16.getCharCount(var6);
            UTF16.append(var5, UCharacter.getMirror(var6));
         } while(var3 < var0.length());

         return var5.toString();
      case 8:
         StringBuilder var2 = new StringBuilder(var0.length());
         var3 = 0;

         do {
            char var4 = var0.charAt(var3++);
            if (!Bidi.IsBidiControlChar(var4)) {
               var2.append(var4);
            }
         } while(var3 < var0.length());

         return var2.toString();
      default:
         var5 = new StringBuffer(var0.length());
         var3 = 0;

         do {
            var6 = UTF16.charAt(var0, var3);
            var3 += UTF16.getCharCount(var6);
            if (!Bidi.IsBidiControlChar(var6)) {
               UTF16.append(var5, UCharacter.getMirror(var6));
            }
         } while(var3 < var0.length());

         return var5.toString();
      }
   }

   private static String doWriteForward(char[] var0, int var1, int var2, int var3) {
      return doWriteForward(new String(var0, var1, var2 - var1), var3);
   }

   static String writeReverse(String var0, int var1) {
      StringBuffer var2 = new StringBuffer(var0.length());
      int var3;
      int var4;
      int var5;
      switch(var1 & 11) {
      case 0:
         var3 = var0.length();

         do {
            var4 = var3;
            var3 -= UTF16.getCharCount(UTF16.charAt(var0, var3 - 1));
            var2.append(var0.substring(var3, var4));
         } while(var3 > 0);

         return var2.toString();
      case 1:
         var3 = var0.length();

         do {
            var5 = var3;

            do {
               var4 = UTF16.charAt(var0, var3 - 1);
               var3 -= UTF16.getCharCount(var4);
            } while(var3 > 0 && UCharacter.getType(var4) == 0);

            var2.append(var0.substring(var3, var5));
         } while(var3 > 0);

         return var2.toString();
      default:
         var3 = var0.length();

         do {
            var4 = var3;
            var5 = UTF16.charAt(var0, var3 - 1);
            var3 -= UTF16.getCharCount(var5);
            if ((var1 & 1) != 0) {
               while(var3 > 0 && UCharacter.getType(var5) != 0) {
                  var5 = UTF16.charAt(var0, var3 - 1);
                  var3 -= UTF16.getCharCount(var5);
               }
            }

            if ((var1 & 8) == 0 || !Bidi.IsBidiControlChar(var5)) {
               int var6 = var3;
               if ((var1 & 2) != 0) {
                  var5 = UCharacter.getMirror(var5);
                  UTF16.append(var2, var5);
                  var6 = var3 + UTF16.getCharCount(var5);
               }

               var2.append(var0.substring(var6, var4));
            }
         } while(var3 > 0);

         return var2.toString();
      }
   }

   static String doWriteReverse(char[] var0, int var1, int var2, int var3) {
      return writeReverse(new String(var0, var1, var2 - var1), var3);
   }

   static String writeReordered(Bidi var0, int var1) {
      char[] var5 = var0.text;
      int var3 = var0.countRuns();
      if ((var0.reorderingOptions & 1) != 0) {
         var1 |= 4;
         var1 &= -9;
      }

      if ((var0.reorderingOptions & 2) != 0) {
         var1 |= 8;
         var1 &= -5;
      }

      if (var0.reorderingMode != 4 && var0.reorderingMode != 5 && var0.reorderingMode != 6 && var0.reorderingMode != 3) {
         var1 &= -5;
      }

      StringBuilder var4 = new StringBuilder((var1 & 4) != 0 ? var0.length * 2 : var0.length);
      int var2;
      BidiRun var6;
      byte[] var10;
      if ((var1 & 16) == 0) {
         if ((var1 & 4) == 0) {
            for(var2 = 0; var2 < var3; ++var2) {
               var6 = var0.getVisualRun(var2);
               if (var6.isEvenRun()) {
                  var4.append(doWriteForward(var5, var6.start, var6.limit, var1 & -3));
               } else {
                  var4.append(doWriteReverse(var5, var6.start, var6.limit, var1));
               }
            }
         } else {
            var10 = var0.dirProps;

            for(var2 = 0; var2 < var3; ++var2) {
               BidiRun var9 = var0.getVisualRun(var2);
               boolean var8 = false;
               int var11 = var0.runs[var2].insertRemove;
               if (var11 < 0) {
                  var11 = 0;
               }

               char var7;
               if (var9.isEvenRun()) {
                  if (var0.isInverse() && var10[var9.start] != 0) {
                     var11 |= 1;
                  }

                  if ((var11 & 1) != 0) {
                     var7 = 8206;
                  } else if ((var11 & 4) != 0) {
                     var7 = 8207;
                  } else {
                     var7 = 0;
                  }

                  if (var7 != 0) {
                     var4.append(var7);
                  }

                  var4.append(doWriteForward(var5, var9.start, var9.limit, var1 & -3));
                  if (var0.isInverse() && var10[var9.limit - 1] != 0) {
                     var11 |= 2;
                  }

                  if ((var11 & 2) != 0) {
                     var7 = 8206;
                  } else if ((var11 & 8) != 0) {
                     var7 = 8207;
                  } else {
                     var7 = 0;
                  }

                  if (var7 != 0) {
                     var4.append(var7);
                  }
               } else {
                  if (var0.isInverse() && !var0.testDirPropFlagAt(8194, var9.limit - 1)) {
                     var11 |= 4;
                  }

                  if ((var11 & 1) != 0) {
                     var7 = 8206;
                  } else if ((var11 & 4) != 0) {
                     var7 = 8207;
                  } else {
                     var7 = 0;
                  }

                  if (var7 != 0) {
                     var4.append(var7);
                  }

                  var4.append(doWriteReverse(var5, var9.start, var9.limit, var1));
                  if (var0.isInverse() && (8194 & Bidi.DirPropFlag(var10[var9.start])) == 0) {
                     var11 |= 8;
                  }

                  if ((var11 & 2) != 0) {
                     var7 = 8206;
                  } else if ((var11 & 8) != 0) {
                     var7 = 8207;
                  } else {
                     var7 = 0;
                  }

                  if (var7 != 0) {
                     var4.append(var7);
                  }
               }
            }
         }
      } else if ((var1 & 4) == 0) {
         var2 = var3;

         while(true) {
            --var2;
            if (var2 < 0) {
               break;
            }

            var6 = var0.getVisualRun(var2);
            if (var6.isEvenRun()) {
               var4.append(doWriteReverse(var5, var6.start, var6.limit, var1 & -3));
            } else {
               var4.append(doWriteForward(var5, var6.start, var6.limit, var1));
            }
         }
      } else {
         var10 = var0.dirProps;
         var2 = var3;

         while(true) {
            --var2;
            if (var2 < 0) {
               break;
            }

            BidiRun var12 = var0.getVisualRun(var2);
            if (var12.isEvenRun()) {
               if (var10[var12.limit - 1] != 0) {
                  var4.append('\u200e');
               }

               var4.append(doWriteReverse(var5, var12.start, var12.limit, var1 & -3));
               if (var10[var12.start] != 0) {
                  var4.append('\u200e');
               }
            } else {
               if ((8194 & Bidi.DirPropFlag(var10[var12.start])) == 0) {
                  var4.append('\u200f');
               }

               var4.append(doWriteForward(var5, var12.start, var12.limit, var1));
               if ((8194 & Bidi.DirPropFlag(var10[var12.limit - 1])) == 0) {
                  var4.append('\u200f');
               }
            }
         }
      }

      return var4.toString();
   }
}
