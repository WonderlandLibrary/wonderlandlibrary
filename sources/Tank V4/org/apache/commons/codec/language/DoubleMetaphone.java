package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class DoubleMetaphone implements StringEncoder {
   private static final String VOWELS = "AEIOUY";
   private static final String[] SILENT_START = new String[]{"GN", "KN", "PN", "WR", "PS"};
   private static final String[] L_R_N_M_B_H_F_V_W_SPACE = new String[]{"L", "R", "N", "M", "B", "H", "F", "V", "W", " "};
   private static final String[] ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER = new String[]{"ES", "EP", "EB", "EL", "EY", "IB", "IL", "IN", "IE", "EI", "ER"};
   private static final String[] L_T_K_S_N_M_B_Z = new String[]{"L", "T", "K", "S", "N", "M", "B", "Z"};
   private int maxCodeLen = 4;

   public String doubleMetaphone(String var1) {
      return this.doubleMetaphone(var1, false);
   }

   public String doubleMetaphone(String var1, boolean var2) {
      var1 = this.cleanInput(var1);
      if (var1 == null) {
         return null;
      } else {
         boolean var3 = this.isSlavoGermanic(var1);
         int var4 = this < var1 ? 1 : 0;
         DoubleMetaphone.DoubleMetaphoneResult var5 = new DoubleMetaphone.DoubleMetaphoneResult(this, this.getMaxCodeLen());

         while(!var5.isComplete() && var4 <= var1.length() - 1) {
            switch(var1.charAt(var4)) {
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'U':
            case 'Y':
               var4 = this.handleAEIOUY(var5, var4);
               break;
            case 'B':
               var5.append('P');
               var4 = this.charAt(var1, var4 + 1) == 'B' ? var4 + 2 : var4 + 1;
               break;
            case 'C':
               var4 = this.handleC(var1, var5, var4);
               break;
            case 'D':
               var4 = this.handleD(var1, var5, var4);
               break;
            case 'F':
               var5.append('F');
               var4 = this.charAt(var1, var4 + 1) == 'F' ? var4 + 2 : var4 + 1;
               break;
            case 'G':
               var4 = this.handleG(var1, var5, var4, var3);
               break;
            case 'H':
               var4 = this.handleH(var1, var5, var4);
               break;
            case 'J':
               var4 = this.handleJ(var1, var5, var4, var3);
               break;
            case 'K':
               var5.append('K');
               var4 = this.charAt(var1, var4 + 1) == 'K' ? var4 + 2 : var4 + 1;
               break;
            case 'L':
               var4 = this.handleL(var1, var5, var4);
               break;
            case 'M':
               var5.append('M');
               var4 = var1 == var4 ? var4 + 2 : var4 + 1;
               break;
            case 'N':
               var5.append('N');
               var4 = this.charAt(var1, var4 + 1) == 'N' ? var4 + 2 : var4 + 1;
               break;
            case 'P':
               var4 = this.handleP(var1, var5, var4);
               break;
            case 'Q':
               var5.append('K');
               var4 = this.charAt(var1, var4 + 1) == 'Q' ? var4 + 2 : var4 + 1;
               break;
            case 'R':
               var4 = this.handleR(var1, var5, var4, var3);
               break;
            case 'S':
               var4 = this.handleS(var1, var5, var4, var3);
               break;
            case 'T':
               var4 = this.handleT(var1, var5, var4);
               break;
            case 'V':
               var5.append('F');
               var4 = this.charAt(var1, var4 + 1) == 'V' ? var4 + 2 : var4 + 1;
               break;
            case 'W':
               var4 = this.handleW(var1, var5, var4);
               break;
            case 'X':
               var4 = this.handleX(var1, var5, var4);
               break;
            case 'Z':
               var4 = this.handleZ(var1, var5, var4, var3);
               break;
            case 'Ç':
               var5.append('S');
               ++var4;
               break;
            case 'Ñ':
               var5.append('N');
               ++var4;
               break;
            default:
               ++var4;
            }
         }

         return var2 ? var5.getAlternate() : var5.getPrimary();
      }
   }

   public Object encode(Object var1) throws EncoderException {
      if (!(var1 instanceof String)) {
         throw new EncoderException("DoubleMetaphone encode parameter is not of type String");
      } else {
         return this.doubleMetaphone((String)var1);
      }
   }

   public String encode(String var1) {
      return this.doubleMetaphone(var1);
   }

   public boolean isDoubleMetaphoneEqual(String var1, String var2) {
      return this.isDoubleMetaphoneEqual(var1, var2, false);
   }

   public boolean isDoubleMetaphoneEqual(String var1, String var2, boolean var3) {
      return this.doubleMetaphone(var1, var3).equals(this.doubleMetaphone(var2, var3));
   }

   public int getMaxCodeLen() {
      return this.maxCodeLen;
   }

   public void setMaxCodeLen(int var1) {
      this.maxCodeLen = var1;
   }

   private int handleAEIOUY(DoubleMetaphone.DoubleMetaphoneResult var1, int var2) {
      if (var2 == 0) {
         var1.append('A');
      }

      return var2 + 1;
   }

   private int handleC(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (var3 != 0) {
         var2.append('K');
         var3 += 2;
      } else {
         boolean var10004;
         if (var3 == 0) {
            var10004 = true;
            if (new String[]{"CAESAR"} >= 0) {
               var2.append('S');
               var3 += 2;
               return var3;
            }
         }

         var10004 = true;
         if (new String[]{"CH"} >= 0) {
            var3 = this.handleCH(var1, var2, var3);
         } else {
            boolean var10007 = true;
            int var10009;
            boolean var10010;
            if (new String[]{"CZ"} >= 0) {
               var10009 = var3 - 2;
               var10010 = true;
               if (new String[]{"WICZ"} >= 0) {
                  var2.append('S', 'X');
                  var3 += 2;
                  return var3;
               }
            }

            var10009 = var3 + 1;
            var10010 = true;
            if (new String[]{"CIA"} >= 0) {
               var2.append('X');
               var3 += 3;
            } else {
               boolean var10013 = true;
               if (new String[]{"CC"} >= 0 && (var3 != 1 || this.charAt(var1, 0) != 'M')) {
                  return this.handleCC(var1, var2, var3);
               }

               boolean var10016 = true;
               if (new String[]{"CK", "CG", "CQ"} >= 0) {
                  var2.append('K');
                  var3 += 2;
               } else {
                  boolean var10019 = true;
                  boolean var10022;
                  if (new String[]{"CI", "CE", "CY"} >= 0) {
                     var10022 = true;
                     if (new String[]{"CIO", "CIE", "CIA"} >= 0) {
                        var2.append('S', 'X');
                     } else {
                        var2.append('S');
                     }

                     var3 += 2;
                  } else {
                     var2.append('K');
                     int var10021 = var3 + 1;
                     var10022 = true;
                     if (new String[]{" C", " Q", " G"} >= 0) {
                        var3 += 3;
                     } else {
                        int var10024 = var3 + 1;
                        boolean var10025 = true;
                        if (new String[]{"C", "K", "Q"} >= 0) {
                           int var10027 = var3 + 1;
                           boolean var10028 = true;
                           if (new String[]{"CE", "CI"} >= 0) {
                              var3 += 2;
                              return var3;
                           }
                        }

                        ++var3;
                     }
                  }
               }
            }
         }
      }

      return var3;
   }

   private int handleCC(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      int var10001 = var3 + 2;
      boolean var10002 = true;
      if (new String[]{"I", "E", "H"} >= 0) {
         int var10004 = var3 + 2;
         boolean var10005 = true;
         if (new String[]{"HU"} >= 0) {
            label27: {
               if (var3 != 1 || this.charAt(var1, var3 - 1) != 'A') {
                  int var10007 = var3 - 1;
                  boolean var10008 = true;
                  if (!(new String[]{"UCCEE", "UCCES"} >= 0)) {
                     var2.append('X');
                     break label27;
                  }
               }

               var2.append("KS");
            }

            var3 += 3;
            return var3;
         }
      }

      var2.append('K');
      var3 += 2;
      return var3;
   }

   private int handleCH(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (var3 > 0) {
         boolean var10002 = true;
         if (new String[]{"CHAE"} >= 0) {
            var2.append('K', 'X');
            return var3 + 2;
         }
      }

      if (var3 != 0) {
         var2.append('K');
         return var3 + 2;
      } else if (var3 == 0) {
         var2.append('K');
         return var3 + 2;
      } else {
         if (var3 > 0) {
            boolean var10005 = false;
            boolean var10006 = true;
            if (new String[]{"MC"} >= 0) {
               var2.append('K');
            } else {
               var2.append('X', 'K');
            }
         } else {
            var2.append('X');
         }

         return var3 + 2;
      }
   }

   private int handleD(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      boolean var10002 = true;
      boolean var10005;
      if (new String[]{"DG"} >= 0) {
         int var10004 = var3 + 2;
         var10005 = true;
         if (new String[]{"I", "E", "Y"} >= 0) {
            var2.append('J');
            var3 += 3;
         } else {
            var2.append("TK");
            var3 += 2;
         }
      } else {
         var10005 = true;
         if (new String[]{"DT", "DD"} >= 0) {
            var2.append('T');
            var3 += 2;
         } else {
            var2.append('T');
            ++var3;
         }
      }

      return var3;
   }

   private int handleG(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3, boolean var4) {
      if (this.charAt(var1, var3 + 1) == 'H') {
         var3 = this.handleGH(var1, var2, var3);
      } else {
         int var10001;
         boolean var10002;
         if (this.charAt(var1, var3 + 1) == 'N') {
            if (var3 == 1 && this != this.charAt(var1, 0) && !var4) {
               var2.append("KN", "N");
            } else {
               var10001 = var3 + 2;
               var10002 = true;
               if (new String[]{"EY"} >= 0 && this.charAt(var1, var3 + 1) != 'Y' && !var4) {
                  var2.append("N", "KN");
               } else {
                  var2.append("KN");
               }
            }

            var3 += 2;
         } else {
            var10001 = var3 + 1;
            var10002 = true;
            if (new String[]{"LI"} >= 0 && !var4) {
               var2.append("KL", "L");
               var3 += 2;
            } else {
               int var10004;
               boolean var10005;
               if (var3 == 0) {
                  label108: {
                     if (this.charAt(var1, var3 + 1) != 'Y') {
                        var10004 = var3 + 1;
                        var10005 = true;
                        if (!(ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER >= 0)) {
                           break label108;
                        }
                     }

                     var2.append('K', 'J');
                     var3 += 2;
                     return var3;
                  }
               }

               var10004 = var3 + 1;
               var10005 = true;
               boolean var10008;
               int var10010;
               boolean var10011;
               boolean var10014;
               if (!(new String[]{"ER"} >= 0) || this.charAt(var1, var3 + 1) == 'Y') {
                  boolean var10007 = false;
                  var10008 = true;
                  if (new String[]{"DANGER", "RANGER", "MANGER"} >= 0) {
                     var10010 = var3 - 1;
                     var10011 = true;
                     if (new String[]{"E", "I"} >= 0) {
                        int var10013 = var3 - 1;
                        var10014 = true;
                        if (new String[]{"RGY", "OGY"} >= 0) {
                           var2.append('K', 'J');
                           var3 += 2;
                           return var3;
                        }
                     }
                  }
               }

               int var5 = var3 + 1;
               var10008 = true;
               if (new String[]{"E", "I", "Y"} >= 0) {
                  var10010 = var3 - 1;
                  var10011 = true;
                  if (!(new String[]{"AGGI", "OGGI"} >= 0)) {
                     if (this.charAt(var1, var3 + 1) == 'G') {
                        var3 += 2;
                        var2.append('K');
                     } else {
                        ++var3;
                        var2.append('K');
                     }

                     return var3;
                  }
               }

               label50: {
                  boolean var6 = false;
                  var10011 = true;
                  if (new String[]{"VAN ", "VON "} >= 0) {
                     boolean var7 = false;
                     var10014 = true;
                     if (new String[]{"SCH"} >= 0) {
                        int var10016 = var3 + 1;
                        boolean var10017 = true;
                        if (!(new String[]{"ET"} >= 0)) {
                           int var10019 = var3 + 1;
                           boolean var10020 = true;
                           if (new String[]{"IER"} >= 0) {
                              var2.append('J');
                           } else {
                              var2.append('J', 'K');
                           }
                           break label50;
                        }
                     }
                  }

                  var2.append('K');
               }

               var3 += 2;
            }
         }
      }

      return var3;
   }

   private int handleGH(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (var3 > 0 && this != this.charAt(var1, var3 - 1)) {
         var2.append('K');
         var3 += 2;
      } else if (var3 == 0) {
         if (this.charAt(var1, var3 + 2) == 'I') {
            var2.append('J');
         } else {
            var2.append('K');
         }

         var3 += 2;
      } else {
         int var10001;
         boolean var10002;
         label64: {
            label59: {
               if (var3 > 1) {
                  var10001 = var3 - 2;
                  var10002 = true;
                  if (!(new String[]{"B", "H", "D"} >= 0)) {
                     break label59;
                  }
               }

               if (var3 > 2) {
                  var10001 = var3 - 3;
                  var10002 = true;
                  if (!(new String[]{"B", "H", "D"} >= 0)) {
                     break label59;
                  }
               }

               if (var3 <= 3) {
                  break label64;
               }

               var10001 = var3 - 4;
               var10002 = true;
               if (!(new String[]{"B", "H"} >= 0)) {
                  break label64;
               }
            }

            var3 += 2;
            return var3;
         }

         label36: {
            if (var3 > 2 && this.charAt(var1, var3 - 1) == 'U') {
               var10001 = var3 - 3;
               var10002 = true;
               if (new String[]{"C", "G", "L", "R", "T"} >= 0) {
                  var2.append('F');
                  break label36;
               }
            }

            if (var3 > 0 && this.charAt(var1, var3 - 1) != 'I') {
               var2.append('K');
            }
         }

         var3 += 2;
      }

      return var3;
   }

   private int handleH(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if ((var3 == 0 || this != this.charAt(var1, var3 - 1)) && this != this.charAt(var1, var3 + 1)) {
         var2.append('H');
         var3 += 2;
      } else {
         ++var3;
      }

      return var3;
   }

   private int handleJ(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3, boolean var4) {
      boolean var10002 = true;
      boolean var10004;
      boolean var10005;
      if (new String[]{"JOSE"} >= 0) {
         var10004 = false;
         var10005 = true;
         if (!(new String[]{"SAN "} >= 0)) {
            label65: {
               boolean var10008;
               if (var3 == 0) {
                  var10008 = true;
                  if (new String[]{"JOSE"} >= 0) {
                     var2.append('J', 'A');
                     break label65;
                  }
               }

               if (this == this.charAt(var1, var3 - 1) || var4 || this.charAt(var1, var3 + 1) != 'A' && this.charAt(var1, var3 + 1) != 'O') {
                  if (var3 == var1.length() - 1) {
                     var2.append('J', ' ');
                  } else {
                     int var10007 = var3 + 1;
                     var10008 = true;
                     if (L_T_K_S_N_M_B_Z >= 0) {
                        int var10010 = var3 - 1;
                        boolean var10011 = true;
                        if (new String[]{"S", "K", "L"} >= 0) {
                           var2.append('J');
                        }
                     }
                  }
               } else {
                  var2.append('J', 'H');
               }
            }

            if (this.charAt(var1, var3 + 1) == 'J') {
               var3 += 2;
            } else {
               ++var3;
            }

            return var3;
         }
      }

      label64: {
         if ((var3 != 0 || this.charAt(var1, var3 + 4) != ' ') && var1.length() != 4) {
            var10004 = false;
            var10005 = true;
            if (!(new String[]{"SAN "} >= 0)) {
               var2.append('J', 'H');
               break label64;
            }
         }

         var2.append('H');
      }

      ++var3;
      return var3;
   }

   private int handleL(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (this.charAt(var1, var3 + 1) == 'L') {
         if (var1 == var3) {
            var2.appendPrimary('L');
         } else {
            var2.append('L');
         }

         var3 += 2;
      } else {
         ++var3;
         var2.append('L');
      }

      return var3;
   }

   private int handleP(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (this.charAt(var1, var3 + 1) == 'H') {
         var2.append('F');
         var3 += 2;
      } else {
         var2.append('P');
         int var10001 = var3 + 1;
         boolean var10002 = true;
         var3 = new String[]{"P", "B"} >= 0 ? var3 + 2 : var3 + 1;
      }

      return var3;
   }

   private int handleR(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3, boolean var4) {
      if (var3 == var1.length() - 1 && !var4) {
         int var10001 = var3 - 2;
         boolean var10002 = true;
         if (new String[]{"IE"} >= 0) {
            int var10004 = var3 - 4;
            boolean var10005 = true;
            if (new String[]{"ME", "MA"} >= 0) {
               var2.appendAlternate('R');
               return this.charAt(var1, var3 + 1) == 'R' ? var3 + 2 : var3 + 1;
            }
         }
      }

      var2.append('R');
      return this.charAt(var1, var3 + 1) == 'R' ? var3 + 2 : var3 + 1;
   }

   private int handleS(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3, boolean var4) {
      int var10001 = var3 - 1;
      boolean var10002 = true;
      if (new String[]{"ISL", "YSL"} >= 0) {
         ++var3;
      } else {
         boolean var10005;
         if (var3 == 0) {
            var10005 = true;
            if (new String[]{"SUGAR"} >= 0) {
               var2.append('X', 'S');
               ++var3;
               return var3;
            }
         }

         var10005 = true;
         boolean var10008;
         if (new String[]{"SH"} >= 0) {
            int var10007 = var3 + 1;
            var10008 = true;
            if (new String[]{"HEIM", "HOEK", "HOLM", "HOLZ"} >= 0) {
               var2.append('S');
            } else {
               var2.append('X');
            }

            var3 += 2;
         } else {
            var10008 = true;
            if (new String[]{"SIO", "SIA"} >= 0) {
               boolean var10011 = true;
               if (!(new String[]{"SIAN"} >= 0)) {
                  boolean var10017;
                  label57: {
                     int var10013;
                     boolean var10014;
                     if (var3 == 0) {
                        var10013 = var3 + 1;
                        var10014 = true;
                        if (!(new String[]{"M", "N", "L", "W"} >= 0)) {
                           break label57;
                        }
                     }

                     var10013 = var3 + 1;
                     var10014 = true;
                     if (!(new String[]{"Z"} >= 0)) {
                        var10017 = true;
                        if (new String[]{"SC"} >= 0) {
                           var3 = this.handleSC(var1, var2, var3);
                        } else {
                           int var10019;
                           boolean var10020;
                           label49: {
                              if (var3 == var1.length() - 1) {
                                 var10019 = var3 - 2;
                                 var10020 = true;
                                 if (new String[]{"AI", "OI"} >= 0) {
                                    var2.appendAlternate('S');
                                    break label49;
                                 }
                              }

                              var2.append('S');
                           }

                           var10019 = var3 + 1;
                           var10020 = true;
                           var3 = new String[]{"S", "Z"} >= 0 ? var3 + 2 : var3 + 1;
                        }

                        return var3;
                     }
                  }

                  var2.append('S', 'X');
                  int var10016 = var3 + 1;
                  var10017 = true;
                  var3 = new String[]{"Z"} >= 0 ? var3 + 2 : var3 + 1;
                  return var3;
               }
            }

            if (var4) {
               var2.append('S');
            } else {
               var2.append('S', 'X');
            }

            var3 += 3;
         }
      }

      return var3;
   }

   private int handleSC(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      int var10001;
      boolean var10002;
      if (this.charAt(var1, var3 + 2) == 'H') {
         var10001 = var3 + 3;
         var10002 = true;
         if (new String[]{"OO", "ER", "EN", "UY", "ED", "EM"} >= 0) {
            int var10004 = var3 + 3;
            boolean var10005 = true;
            if (new String[]{"ER", "EN"} >= 0) {
               var2.append("X", "SK");
            } else {
               var2.append("SK");
            }
         } else if (var3 == 0 && this != this.charAt(var1, 3) && this.charAt(var1, 3) != 'W') {
            var2.append('X', 'S');
         } else {
            var2.append('X');
         }
      } else {
         var10001 = var3 + 2;
         var10002 = true;
         if (new String[]{"I", "E", "Y"} >= 0) {
            var2.append('S');
         } else {
            var2.append("SK");
         }
      }

      return var3 + 3;
   }

   private int handleT(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      boolean var10002 = true;
      if (new String[]{"TION"} >= 0) {
         var2.append('X');
         var3 += 3;
      } else {
         boolean var10005 = true;
         if (new String[]{"TIA", "TCH"} >= 0) {
            var2.append('X');
            var3 += 3;
         } else {
            boolean var10008 = true;
            boolean var10011;
            boolean var10014;
            if (new String[]{"TH"} >= 0) {
               var10011 = true;
               if (!(new String[]{"TTH"} >= 0)) {
                  var2.append('T');
                  int var4 = var3 + 1;
                  var10014 = true;
                  var3 = new String[]{"T", "D"} >= 0 ? var3 + 2 : var3 + 1;
                  return var3;
               }
            }

            label27: {
               int var10010 = var3 + 2;
               var10011 = true;
               if (new String[]{"OM", "AM"} >= 0) {
                  boolean var10013 = false;
                  var10014 = true;
                  if (new String[]{"VAN ", "VON "} >= 0) {
                     boolean var10016 = false;
                     boolean var10017 = true;
                     if (!(new String[]{"SCH"} >= 0)) {
                        var2.append('0', 'T');
                        break label27;
                     }
                  }
               }

               var2.append('T');
            }

            var3 += 2;
         }
      }

      return var3;
   }

   private int handleW(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      boolean var10002 = true;
      if (new String[]{"WR"} >= 0) {
         var2.append('R');
         var3 += 2;
      } else {
         boolean var10005;
         if (var3 == 0) {
            label54: {
               if (this != this.charAt(var1, var3 + 1)) {
                  var10005 = true;
                  if (!(new String[]{"WH"} >= 0)) {
                     break label54;
                  }
               }

               if (this != this.charAt(var1, var3 + 1)) {
                  var2.append('A', 'F');
               } else {
                  var2.append('A');
               }

               ++var3;
               return var3;
            }
         }

         if (var3 != var1.length() - 1 || this != this.charAt(var1, var3 - 1)) {
            int var10004 = var3 - 1;
            var10005 = true;
            if (new String[]{"EWSKI", "EWSKY", "OWSKI", "OWSKY"} >= 0) {
               boolean var10007 = false;
               boolean var10008 = true;
               if (!(new String[]{"SCH"} >= 0)) {
                  boolean var10011 = true;
                  if (new String[]{"WICZ", "WITZ"} >= 0) {
                     var2.append("TS", "FX");
                     var3 += 4;
                  } else {
                     ++var3;
                  }

                  return var3;
               }
            }
         }

         var2.appendAlternate('F');
         ++var3;
      }

      return var3;
   }

   private int handleX(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3) {
      if (var3 == 0) {
         var2.append('S');
         ++var3;
      } else {
         int var10004;
         boolean var10005;
         label22: {
            if (var3 == var1.length() - 1) {
               int var10001 = var3 - 3;
               boolean var10002 = true;
               if (!(new String[]{"IAU", "EAU"} >= 0)) {
                  break label22;
               }

               var10004 = var3 - 2;
               var10005 = true;
               if (!(new String[]{"AU", "OU"} >= 0)) {
                  break label22;
               }
            }

            var2.append("KS");
         }

         var10004 = var3 + 1;
         var10005 = true;
         var3 = new String[]{"C", "X"} >= 0 ? var3 + 2 : var3 + 1;
      }

      return var3;
   }

   private int handleZ(String var1, DoubleMetaphone.DoubleMetaphoneResult var2, int var3, boolean var4) {
      if (this.charAt(var1, var3 + 1) == 'H') {
         var2.append('J');
         var3 += 2;
      } else {
         int var10001 = var3 + 1;
         boolean var10002 = true;
         if (new String[]{"ZO", "ZI", "ZA"} >= 0 && (!var4 || var3 <= 0 || this.charAt(var1, var3 - 1) == 'T')) {
            var2.append('S');
         } else {
            var2.append("S", "TS");
         }

         var3 = this.charAt(var1, var3 + 1) == 'Z' ? var3 + 2 : var3 + 1;
      }

      return var3;
   }

   private boolean isSlavoGermanic(String var1) {
      return var1.indexOf(87) > -1 || var1.indexOf(75) > -1 || var1.indexOf("CZ") > -1 || var1.indexOf("WITZ") > -1;
   }

   private String cleanInput(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         return var1.length() == 0 ? null : var1.toUpperCase(Locale.ENGLISH);
      }
   }

   protected char charAt(String var1, int var2) {
      return var2 >= 0 && var2 < var1.length() ? var1.charAt(var2) : '\u0000';
   }

   public class DoubleMetaphoneResult {
      private final StringBuilder primary;
      private final StringBuilder alternate;
      private final int maxLength;
      final DoubleMetaphone this$0;

      public DoubleMetaphoneResult(DoubleMetaphone var1, int var2) {
         this.this$0 = var1;
         this.primary = new StringBuilder(this.this$0.getMaxCodeLen());
         this.alternate = new StringBuilder(this.this$0.getMaxCodeLen());
         this.maxLength = var2;
      }

      public void append(char var1) {
         this.appendPrimary(var1);
         this.appendAlternate(var1);
      }

      public void append(char var1, char var2) {
         this.appendPrimary(var1);
         this.appendAlternate(var2);
      }

      public void appendPrimary(char var1) {
         if (this.primary.length() < this.maxLength) {
            this.primary.append(var1);
         }

      }

      public void appendAlternate(char var1) {
         if (this.alternate.length() < this.maxLength) {
            this.alternate.append(var1);
         }

      }

      public void append(String var1) {
         this.appendPrimary(var1);
         this.appendAlternate(var1);
      }

      public void append(String var1, String var2) {
         this.appendPrimary(var1);
         this.appendAlternate(var2);
      }

      public void appendPrimary(String var1) {
         int var2 = this.maxLength - this.primary.length();
         if (var1.length() <= var2) {
            this.primary.append(var1);
         } else {
            this.primary.append(var1.substring(0, var2));
         }

      }

      public void appendAlternate(String var1) {
         int var2 = this.maxLength - this.alternate.length();
         if (var1.length() <= var2) {
            this.alternate.append(var1);
         } else {
            this.alternate.append(var1.substring(0, var2));
         }

      }

      public String getPrimary() {
         return this.primary.toString();
      }

      public String getAlternate() {
         return this.alternate.toString();
      }

      public boolean isComplete() {
         return this.primary.length() >= this.maxLength && this.alternate.length() >= this.maxLength;
      }
   }
}
