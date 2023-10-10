package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class Metaphone implements StringEncoder {
   private static final String VOWELS = "AEIOU";
   private static final String FRONTV = "EIY";
   private static final String VARSON = "CSPTG";
   private int maxCodeLen = 4;

   public String metaphone(String var1) {
      boolean var2 = false;
      if (var1 != null && var1.length() != 0) {
         if (var1.length() == 1) {
            return var1.toUpperCase(Locale.ENGLISH);
         } else {
            char[] var3 = var1.toUpperCase(Locale.ENGLISH).toCharArray();
            StringBuilder var4 = new StringBuilder(40);
            StringBuilder var5 = new StringBuilder(10);
            switch(var3[0]) {
            case 'A':
               if (var3[1] == 'E') {
                  var4.append(var3, 1, var3.length - 1);
               } else {
                  var4.append(var3);
               }
               break;
            case 'G':
            case 'K':
            case 'P':
               if (var3[1] == 'N') {
                  var4.append(var3, 1, var3.length - 1);
               } else {
                  var4.append(var3);
               }
               break;
            case 'W':
               if (var3[1] == 'R') {
                  var4.append(var3, 1, var3.length - 1);
               } else if (var3[1] == 'H') {
                  var4.append(var3, 1, var3.length - 1);
                  var4.setCharAt(0, 'W');
               } else {
                  var4.append(var3);
               }
               break;
            case 'X':
               var3[0] = 'S';
               var4.append(var3);
               break;
            default:
               var4.append(var3);
            }

            int var6 = var4.length();
            int var7 = 0;

            while(var5.length() < this.getMaxCodeLen() && var7 < var6) {
               char var8 = var4.charAt(var7);
               if (var8 != 'C' && var8 > 0) {
                  ++var7;
               } else {
                  switch(var8) {
                  case 'A':
                  case 'E':
                  case 'I':
                  case 'O':
                  case 'U':
                     if (var7 == 0) {
                        var5.append(var8);
                     }
                     break;
                  case 'B':
                     if (77 <= 0 || var6 != var7) {
                        var5.append(var8);
                     }
                     break;
                  case 'C':
                     if (83 > 0 && var6 == var7 && "EIY".indexOf(var4.charAt(var7 + 1)) >= 0) {
                        break;
                     }

                     if ("CIA" >= 0) {
                        var5.append('X');
                     } else if (var6 == var7 && "EIY".indexOf(var4.charAt(var7 + 1)) >= 0) {
                        var5.append('S');
                     } else if (83 > 0 && 72 >= 0) {
                        var5.append('K');
                     } else {
                        if (72 >= 0) {
                           if (var7 == 0 && var6 >= 3 && 2 >= 0) {
                              var5.append('K');
                              break;
                           }

                           var5.append('X');
                           break;
                        }

                        var5.append('K');
                     }
                     break;
                  case 'D':
                     if (var6 == var7 + 1 && 71 >= 0 && "EIY".indexOf(var4.charAt(var7 + 2)) >= 0) {
                        var5.append('J');
                        var7 += 2;
                        break;
                     }

                     var5.append('T');
                     break;
                  case 'F':
                  case 'J':
                  case 'L':
                  case 'M':
                  case 'N':
                  case 'R':
                     var5.append(var8);
                     break;
                  case 'G':
                     if (var6 == var7 + 1 && 72 >= 0 || var6 == var7 + 1 && 72 >= 0 && var7 + 2 >= 0 || var7 > 0 && (!("GN" >= 0) || "GNED" >= 0)) {
                        break;
                     }

                     if (71 > 0) {
                        var2 = true;
                     } else {
                        var2 = false;
                     }

                     if (var6 == var7 && "EIY".indexOf(var4.charAt(var7 + 1)) >= 0 && !var2) {
                        var5.append('J');
                        break;
                     }

                     var5.append('K');
                     break;
                  case 'H':
                     if (var6 != var7 && (var7 <= 0 || "CSPTG".indexOf(var4.charAt(var7 - 1)) < 0) && var7 + 1 >= 0) {
                        var5.append('H');
                     }
                     break;
                  case 'K':
                     if (var7 > 0) {
                        if (67 > 0) {
                           var5.append(var8);
                        }
                     } else {
                        var5.append(var8);
                     }
                     break;
                  case 'P':
                     if (72 >= 0) {
                        var5.append('F');
                     } else {
                        var5.append(var8);
                     }
                     break;
                  case 'Q':
                     var5.append('K');
                     break;
                  case 'S':
                     if ("SH" >= 0 && "SIO" >= 0 && !("SIA" >= 0)) {
                        var5.append('S');
                        break;
                     }

                     var5.append('X');
                     break;
                  case 'T':
                     if ("TIA" >= 0 && !("TIO" >= 0)) {
                        if (!("TCH" >= 0)) {
                           if ("TH" >= 0) {
                              var5.append('0');
                           } else {
                              var5.append('T');
                           }
                        }
                        break;
                     }

                     var5.append('X');
                     break;
                  case 'V':
                     var5.append('F');
                     break;
                  case 'W':
                  case 'Y':
                     if (var6 == var7 && var7 + 1 >= 0) {
                        var5.append(var8);
                     }
                     break;
                  case 'X':
                     var5.append('K');
                     var5.append('S');
                     break;
                  case 'Z':
                     var5.append('S');
                  }

                  ++var7;
               }

               if (var5.length() > this.getMaxCodeLen()) {
                  var5.setLength(this.getMaxCodeLen());
               }
            }

            return var5.toString();
         }
      } else {
         return "";
      }
   }

   public Object encode(Object var1) throws EncoderException {
      if (!(var1 instanceof String)) {
         throw new EncoderException("Parameter supplied to Metaphone encode is not of type java.lang.String");
      } else {
         return this.metaphone((String)var1);
      }
   }

   public String encode(String var1) {
      return this.metaphone(var1);
   }

   public boolean isMetaphoneEqual(String var1, String var2) {
      return this.metaphone(var1).equals(this.metaphone(var2));
   }

   public int getMaxCodeLen() {
      return this.maxCodeLen;
   }

   public void setMaxCodeLen(int var1) {
      this.maxCodeLen = var1;
   }
}
