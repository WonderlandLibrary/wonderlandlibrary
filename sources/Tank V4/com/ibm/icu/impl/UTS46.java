package com.ibm.icu.impl;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.text.IDNA;
import com.ibm.icu.text.Normalizer2;
import java.io.InputStream;
import java.util.EnumSet;

public final class UTS46 extends IDNA {
   private static final Normalizer2 uts46Norm2;
   final int options;
   private static final EnumSet severeErrors;
   private static final byte[] asciiData;
   private static final int L_MASK;
   private static final int R_AL_MASK;
   private static final int L_R_AL_MASK;
   private static final int R_AL_AN_MASK;
   private static final int EN_AN_MASK;
   private static final int R_AL_EN_AN_MASK;
   private static final int L_EN_MASK;
   private static final int ES_CS_ET_ON_BN_NSM_MASK;
   private static final int L_EN_ES_CS_ET_ON_BN_NSM_MASK;
   private static final int R_AL_AN_EN_ES_CS_ET_ON_BN_NSM_MASK;
   private static int U_GC_M_MASK;

   public UTS46(int var1) {
      this.options = var1;
   }

   public StringBuilder labelToASCII(CharSequence var1, StringBuilder var2, IDNA.Info var3) {
      return this.process(var1, true, true, var2, var3);
   }

   public StringBuilder labelToUnicode(CharSequence var1, StringBuilder var2, IDNA.Info var3) {
      return this.process(var1, true, false, var2, var3);
   }

   public StringBuilder nameToASCII(CharSequence param1, StringBuilder param2, IDNA.Info param3) {
      // $FF: Couldn't be decompiled
   }

   public StringBuilder nameToUnicode(CharSequence var1, StringBuilder var2, IDNA.Info var3) {
      return this.process(var1, false, false, var2, var3);
   }

   private StringBuilder process(CharSequence var1, boolean var2, boolean var3, StringBuilder var4, IDNA.Info var5) {
      if (var4 == var1) {
         throw new IllegalArgumentException();
      } else {
         var4.delete(0, Integer.MAX_VALUE);
         resetInfo(var5);
         int var6 = var1.length();
         if (var6 == 0) {
            if (var3) {
               addError(var5, IDNA.Error.EMPTY_LABEL);
            }

            return var4;
         } else {
            boolean var7 = (this.options & 2) != 0;
            int var8 = 0;
            int var9 = 0;

            while(true) {
               if (var9 == var6) {
                  if (var3) {
                     if (var9 - var8 > 63) {
                        addLabelError(var5, IDNA.Error.LABEL_TOO_LONG);
                     }

                     if (!var2 && var9 >= 254 && (var9 > 254 || var8 < var9)) {
                        addError(var5, IDNA.Error.DOMAIN_NAME_TOO_LONG);
                     }
                  }

                  promoteAndResetLabelErrors(var5);
                  return var4;
               }

               char var10 = var1.charAt(var9);
               if (var10 > 127) {
                  break;
               }

               byte var11 = asciiData[var10];
               if (var11 > 0) {
                  var4.append((char)(var10 + 32));
               } else {
                  if (var11 < 0 && var7) {
                     break;
                  }

                  var4.append(var10);
                  if (var10 == '-') {
                     if (var9 == var8 + 3 && var1.charAt(var9 - 1) == '-') {
                        ++var9;
                        break;
                     }

                     if (var9 == var8) {
                        addLabelError(var5, IDNA.Error.LEADING_HYPHEN);
                     }

                     if (var9 + 1 == var6 || var1.charAt(var9 + 1) == '.') {
                        addLabelError(var5, IDNA.Error.TRAILING_HYPHEN);
                     }
                  } else if (var10 == '.') {
                     if (var2) {
                        ++var9;
                        break;
                     }

                     if (var3) {
                        if (var9 == var8 && var9 < var6 - 1) {
                           addLabelError(var5, IDNA.Error.EMPTY_LABEL);
                        } else if (var9 - var8 > 63) {
                           addLabelError(var5, IDNA.Error.LABEL_TOO_LONG);
                        }
                     }

                     promoteAndResetLabelErrors(var5);
                     var8 = var9 + 1;
                  }
               }

               ++var9;
            }

            promoteAndResetLabelErrors(var5);
            this.processUnicode(var1, var8, var9, var2, var3, var4, var5);
            if (isBiDi(var5) && !hasCertainErrors(var5, severeErrors) && (!isOkBiDi(var5) || var8 > 0 && var4 < var8)) {
               addError(var5, IDNA.Error.BIDI);
            }

            return var4;
         }
      }
   }

   private StringBuilder processUnicode(CharSequence var1, int var2, int var3, boolean var4, boolean var5, StringBuilder var6, IDNA.Info var7) {
      if (var3 == 0) {
         uts46Norm2.normalize(var1, var6);
      } else {
         uts46Norm2.normalizeSecondAndAppend(var6, var1.subSequence(var3, var1.length()));
      }

      boolean var8 = var5 ? (this.options & 16) == 0 : (this.options & 32) == 0;
      int var9 = var6.length();
      int var10 = var2;

      while(true) {
         while(var10 < var9) {
            char var11 = var6.charAt(var10);
            if (var11 == '.' && !var4) {
               int var12 = var10 - var2;
               int var13 = this.processLabel(var6, var2, var12, var5, var7);
               promoteAndResetLabelErrors(var7);
               var9 += var13 - var12;
               var10 = var2 += var13 + 1;
            } else if (223 > var11 || var11 > 8205 || var11 != 223 && var11 != 962 && var11 < 8204) {
               ++var10;
            } else {
               setTransitionalDifferent(var7);
               if (var8) {
                  var9 = this.mapDevChars(var6, var2, var10);
                  var8 = false;
               } else {
                  ++var10;
               }
            }
         }

         if (0 == var2 || var2 < var10) {
            this.processLabel(var6, var2, var10 - var2, var5, var7);
            promoteAndResetLabelErrors(var7);
         }

         return var6;
      }
   }

   private int mapDevChars(StringBuilder var1, int var2, int var3) {
      int var4 = var1.length();
      boolean var5 = false;
      int var6 = var3;

      while(var6 < var4) {
         char var7 = var1.charAt(var6);
         switch(var7) {
         case 'ß':
            var5 = true;
            var1.setCharAt(var6++, 's');
            var1.insert(var6++, 's');
            ++var4;
            break;
         case 'ς':
            var5 = true;
            var1.setCharAt(var6++, 'σ');
            break;
         case '\u200c':
         case '\u200d':
            var5 = true;
            var1.delete(var6, var6 + 1);
            --var4;
            break;
         default:
            ++var6;
         }
      }

      if (var5) {
         String var8 = uts46Norm2.normalize(var1.subSequence(var2, var1.length()));
         var1.replace(var2, Integer.MAX_VALUE, var8);
         return var1.length();
      } else {
         return var4;
      }
   }

   private static int replaceLabel(StringBuilder var0, int var1, int var2, CharSequence var3, int var4) {
      if (var3 != var0) {
         var0.delete(var1, var1 + var2).insert(var1, var3);
      }

      return var4;
   }

   private int processLabel(StringBuilder param1, int param2, int param3, boolean param4, IDNA.Info param5) {
      // $FF: Couldn't be decompiled
   }

   private int markBadACELabel(StringBuilder var1, int var2, int var3, boolean var4, IDNA.Info var5) {
      boolean var6 = (this.options & 2) != 0;
      boolean var7 = true;
      boolean var8 = true;
      int var9 = var2 + 4;
      int var10 = var2 + var3;

      do {
         char var11 = var1.charAt(var9);
         if (var11 <= 127) {
            if (var11 == '.') {
               addLabelError(var5, IDNA.Error.LABEL_HAS_DOT);
               var1.setCharAt(var9, '�');
               var8 = false;
               var7 = false;
            } else if (asciiData[var11] < 0) {
               var8 = false;
               if (var6) {
                  var1.setCharAt(var9, '�');
                  var7 = false;
               }
            }
         } else {
            var8 = false;
            var7 = false;
         }

         ++var9;
      } while(var9 < var10);

      if (var8) {
         var1.insert(var2 + var3, '�');
         ++var3;
      } else if (var4 && var7 && var3 > 63) {
         addLabelError(var5, IDNA.Error.LABEL_TOO_LONG);
      }

      return var3;
   }

   private void checkLabelBiDi(CharSequence var1, int var2, int var3, IDNA.Info var4) {
      int var5 = Character.codePointAt(var1, var2);
      int var6 = var2 + Character.charCount(var5);
      int var7 = U_MASK(UBiDiProps.INSTANCE.getClass(var5));
      if ((var7 & ~L_R_AL_MASK) != 0) {
         setNotOkBiDi(var4);
      }

      int var9 = var2 + var3;

      int var8;
      int var10;
      while(true) {
         if (var6 >= var9) {
            var8 = var7;
            break;
         }

         var5 = Character.codePointBefore(var1, var9);
         var9 -= Character.charCount(var5);
         var10 = UBiDiProps.INSTANCE.getClass(var5);
         if (var10 != 17) {
            var8 = U_MASK(var10);
            break;
         }
      }

      label50: {
         if ((var7 & L_MASK) != 0) {
            if ((var8 & ~L_EN_MASK) == 0) {
               break label50;
            }
         } else if ((var8 & ~R_AL_EN_AN_MASK) == 0) {
            break label50;
         }

         setNotOkBiDi(var4);
      }

      for(var10 = 0; var6 < var9; var10 |= U_MASK(UBiDiProps.INSTANCE.getClass(var5))) {
         var5 = Character.codePointAt(var1, var6);
         var6 += Character.charCount(var5);
      }

      if ((var7 & L_MASK) != 0) {
         if ((var10 & ~L_EN_ES_CS_ET_ON_BN_NSM_MASK) != 0) {
            setNotOkBiDi(var4);
         }
      } else {
         if ((var10 & ~R_AL_AN_EN_ES_CS_ET_ON_BN_NSM_MASK) != 0) {
            setNotOkBiDi(var4);
         }

         if ((var10 & EN_AN_MASK) == EN_AN_MASK) {
            setNotOkBiDi(var4);
         }
      }

      if (((var7 | var10 | var8) & R_AL_AN_MASK) != 0) {
         setBiDi(var4);
      }

   }

   private void checkLabelContextO(CharSequence var1, int var2, int var3, IDNA.Info var4) {
      int var5 = var2 + var3 - 1;
      byte var6 = 0;

      label89:
      for(int var7 = var2; var7 <= var5; ++var7) {
         char var8 = var1.charAt(var7);
         if (var8 >= 183) {
            if (var8 <= 1785) {
               if (var8 == 183) {
                  if (var2 >= var7 || var1.charAt(var7 - 1) != 'l' || var7 >= var5 || var1.charAt(var7 + 1) != 'l') {
                     addLabelError(var4, IDNA.Error.CONTEXTO_PUNCTUATION);
                  }
               } else if (var8 == 885) {
                  if (var7 >= var5 || 14 != UScript.getScript(Character.codePointAt(var1, var7 + 1))) {
                     addLabelError(var4, IDNA.Error.CONTEXTO_PUNCTUATION);
                  }
               } else if (var8 != 1523 && var8 != 1524) {
                  if (1632 <= var8) {
                     if (var8 <= 1641) {
                        if (var6 > 0) {
                           addLabelError(var4, IDNA.Error.CONTEXTO_DIGITS);
                        }

                        var6 = -1;
                     } else if (1776 <= var8) {
                        if (var6 < 0) {
                           addLabelError(var4, IDNA.Error.CONTEXTO_DIGITS);
                        }

                        var6 = 1;
                     }
                  }
               } else if (var2 >= var7 || 19 != UScript.getScript(Character.codePointBefore(var1, var7))) {
                  addLabelError(var4, IDNA.Error.CONTEXTO_PUNCTUATION);
               }
            } else if (var8 == 12539) {
               int var11;
               for(int var9 = var2; var9 <= var5; var9 += Character.charCount(var11)) {
                  var11 = Character.codePointAt(var1, var9);
                  int var10 = UScript.getScript(var11);
                  if (var10 == 20 || var10 == 22 || var10 == 17) {
                     continue label89;
                  }
               }

               addLabelError(var4, IDNA.Error.CONTEXTO_PUNCTUATION);
            }
         }
      }

   }

   private static int U_MASK(int var0) {
      return 1 << var0;
   }

   private static int U_GET_GC_MASK(int var0) {
      return 1 << UCharacter.getType(var0);
   }

   static {
      uts46Norm2 = Normalizer2.getInstance((InputStream)null, "uts46", Normalizer2.Mode.COMPOSE);
      severeErrors = EnumSet.of(IDNA.Error.LEADING_COMBINING_MARK, IDNA.Error.DISALLOWED, IDNA.Error.PUNYCODE, IDNA.Error.LABEL_HAS_DOT, IDNA.Error.INVALID_ACE_LABEL);
      asciiData = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, -1, -1, -1};
      L_MASK = U_MASK(0);
      R_AL_MASK = U_MASK(1) | U_MASK(13);
      L_R_AL_MASK = L_MASK | R_AL_MASK;
      R_AL_AN_MASK = R_AL_MASK | U_MASK(5);
      EN_AN_MASK = U_MASK(2) | U_MASK(5);
      R_AL_EN_AN_MASK = R_AL_MASK | EN_AN_MASK;
      L_EN_MASK = L_MASK | U_MASK(2);
      ES_CS_ET_ON_BN_NSM_MASK = U_MASK(3) | U_MASK(6) | U_MASK(4) | U_MASK(10) | U_MASK(18) | U_MASK(17);
      L_EN_ES_CS_ET_ON_BN_NSM_MASK = L_EN_MASK | ES_CS_ET_ON_BN_NSM_MASK;
      R_AL_AN_EN_ES_CS_ET_ON_BN_NSM_MASK = R_AL_MASK | EN_AN_MASK | ES_CS_ET_ON_BN_NSM_MASK;
      U_GC_M_MASK = U_MASK(6) | U_MASK(7) | U_MASK(8);
   }
}
