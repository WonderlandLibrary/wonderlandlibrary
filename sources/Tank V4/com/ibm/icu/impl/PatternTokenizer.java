package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;

public class PatternTokenizer {
   private UnicodeSet ignorableCharacters = new UnicodeSet();
   private UnicodeSet syntaxCharacters = new UnicodeSet();
   private UnicodeSet extraQuotingCharacters = new UnicodeSet();
   private UnicodeSet escapeCharacters = new UnicodeSet();
   private boolean usingSlash = false;
   private boolean usingQuote = false;
   private transient UnicodeSet needingQuoteCharacters = null;
   private int start;
   private int limit;
   private String pattern;
   public static final char SINGLE_QUOTE = '\'';
   public static final char BACK_SLASH = '\\';
   private static int NO_QUOTE = -1;
   private static int IN_QUOTE = -2;
   public static final int DONE = 0;
   public static final int SYNTAX = 1;
   public static final int LITERAL = 2;
   public static final int BROKEN_QUOTE = 3;
   public static final int BROKEN_ESCAPE = 4;
   public static final int UNKNOWN = 5;
   private static final int AFTER_QUOTE = -1;
   private static final int NONE = 0;
   private static final int START_QUOTE = 1;
   private static final int NORMAL_QUOTE = 2;
   private static final int SLASH_START = 3;
   private static final int HEX = 4;

   public UnicodeSet getIgnorableCharacters() {
      return (UnicodeSet)this.ignorableCharacters.clone();
   }

   public PatternTokenizer setIgnorableCharacters(UnicodeSet var1) {
      this.ignorableCharacters = (UnicodeSet)var1.clone();
      this.needingQuoteCharacters = null;
      return this;
   }

   public UnicodeSet getSyntaxCharacters() {
      return (UnicodeSet)this.syntaxCharacters.clone();
   }

   public UnicodeSet getExtraQuotingCharacters() {
      return (UnicodeSet)this.extraQuotingCharacters.clone();
   }

   public PatternTokenizer setSyntaxCharacters(UnicodeSet var1) {
      this.syntaxCharacters = (UnicodeSet)var1.clone();
      this.needingQuoteCharacters = null;
      return this;
   }

   public PatternTokenizer setExtraQuotingCharacters(UnicodeSet var1) {
      this.extraQuotingCharacters = (UnicodeSet)var1.clone();
      this.needingQuoteCharacters = null;
      return this;
   }

   public UnicodeSet getEscapeCharacters() {
      return (UnicodeSet)this.escapeCharacters.clone();
   }

   public PatternTokenizer setEscapeCharacters(UnicodeSet var1) {
      this.escapeCharacters = (UnicodeSet)var1.clone();
      return this;
   }

   public boolean isUsingQuote() {
      return this.usingQuote;
   }

   public PatternTokenizer setUsingQuote(boolean var1) {
      this.usingQuote = var1;
      this.needingQuoteCharacters = null;
      return this;
   }

   public boolean isUsingSlash() {
      return this.usingSlash;
   }

   public PatternTokenizer setUsingSlash(boolean var1) {
      this.usingSlash = var1;
      this.needingQuoteCharacters = null;
      return this;
   }

   public int getLimit() {
      return this.limit;
   }

   public PatternTokenizer setLimit(int var1) {
      this.limit = var1;
      return this;
   }

   public int getStart() {
      return this.start;
   }

   public PatternTokenizer setStart(int var1) {
      this.start = var1;
      return this;
   }

   public PatternTokenizer setPattern(CharSequence var1) {
      return this.setPattern(var1.toString());
   }

   public PatternTokenizer setPattern(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Inconsistent arguments");
      } else {
         this.start = 0;
         this.limit = var1.length();
         this.pattern = var1;
         return this;
      }
   }

   public String quoteLiteral(CharSequence var1) {
      return this.quoteLiteral(var1.toString());
   }

   public String quoteLiteral(String var1) {
      if (this.needingQuoteCharacters == null) {
         this.needingQuoteCharacters = (new UnicodeSet()).addAll(this.syntaxCharacters).addAll(this.ignorableCharacters).addAll(this.extraQuotingCharacters);
         if (this.usingSlash) {
            this.needingQuoteCharacters.add(92);
         }

         if (this.usingQuote) {
            this.needingQuoteCharacters.add(39);
         }
      }

      StringBuffer var2 = new StringBuffer();
      int var3 = NO_QUOTE;

      int var4;
      for(int var5 = 0; var5 < var1.length(); var5 += UTF16.getCharCount(var4)) {
         var4 = UTF16.charAt(var1, var5);
         if (this.escapeCharacters.contains(var4)) {
            if (var3 == IN_QUOTE) {
               var2.append('\'');
               var3 = NO_QUOTE;
            }

            this.appendEscaped(var2, var4);
         } else if (this.needingQuoteCharacters.contains(var4)) {
            if (var3 == IN_QUOTE) {
               UTF16.append(var2, var4);
               if (this.usingQuote && var4 == 39) {
                  var2.append('\'');
               }
            } else if (this.usingSlash) {
               var2.append('\\');
               UTF16.append(var2, var4);
            } else if (this.usingQuote) {
               if (var4 == 39) {
                  var2.append('\'');
                  var2.append('\'');
               } else {
                  var2.append('\'');
                  UTF16.append(var2, var4);
                  var3 = IN_QUOTE;
               }
            } else {
               this.appendEscaped(var2, var4);
            }
         } else {
            if (var3 == IN_QUOTE) {
               var2.append('\'');
               var3 = NO_QUOTE;
            }

            UTF16.append(var2, var4);
         }
      }

      if (var3 == IN_QUOTE) {
         var2.append('\'');
      }

      return var2.toString();
   }

   private void appendEscaped(StringBuffer var1, int var2) {
      if (var2 <= 65535) {
         var1.append("\\u").append(Utility.hex((long)var2, 4));
      } else {
         var1.append("\\U").append(Utility.hex((long)var2, 8));
      }

   }

   public String normalize() {
      int var1 = this.start;
      StringBuffer var2 = new StringBuffer();
      StringBuffer var3 = new StringBuffer();

      while(true) {
         var3.setLength(0);
         int var4 = this.next(var3);
         if (var4 == 0) {
            this.start = var1;
            return var2.toString();
         }

         if (var4 != 1) {
            var2.append(this.quoteLiteral((CharSequence)var3));
         } else {
            var2.append(var3);
         }
      }
   }

   public int next(StringBuffer var1) {
      if (this.start >= this.limit) {
         return 0;
      } else {
         byte var2 = 5;
         int var3 = 5;
         byte var4 = 0;
         int var5 = 0;
         int var6 = 0;

         int var7;
         for(int var8 = this.start; var8 < this.limit; var8 += UTF16.getCharCount(var7)) {
            var7 = UTF16.charAt(this.pattern, var8);
            label81:
            switch(var4) {
            case -1:
               if (var7 == var3) {
                  UTF16.append(var1, var7);
                  var4 = 2;
                  continue;
               }

               var4 = 0;
            case 0:
            default:
               break;
            case 1:
               if (var7 == var3) {
                  UTF16.append(var1, var7);
                  var4 = 0;
               } else {
                  UTF16.append(var1, var7);
                  var4 = 2;
               }
               continue;
            case 2:
               if (var7 == var3) {
                  var4 = -1;
               } else {
                  UTF16.append(var1, var7);
               }
               continue;
            case 3:
               switch(var7) {
               case 85:
                  var4 = 4;
                  var5 = 8;
                  var6 = 0;
                  continue;
               case 117:
                  var4 = 4;
                  var5 = 4;
                  var6 = 0;
                  continue;
               default:
                  if (this.usingSlash) {
                     UTF16.append(var1, var7);
                     var4 = 0;
                     continue;
                  }

                  var1.append('\\');
                  var4 = 0;
                  break label81;
               }
            case 4:
               var6 <<= 4;
               var6 += var7;
               switch(var7) {
               case 48:
               case 49:
               case 50:
               case 51:
               case 52:
               case 53:
               case 54:
               case 55:
               case 56:
               case 57:
                  var6 -= 48;
                  break;
               case 58:
               case 59:
               case 60:
               case 61:
               case 62:
               case 63:
               case 64:
               case 71:
               case 72:
               case 73:
               case 74:
               case 75:
               case 76:
               case 77:
               case 78:
               case 79:
               case 80:
               case 81:
               case 82:
               case 83:
               case 84:
               case 85:
               case 86:
               case 87:
               case 88:
               case 89:
               case 90:
               case 91:
               case 92:
               case 93:
               case 94:
               case 95:
               case 96:
               default:
                  this.start = var8;
                  return 4;
               case 65:
               case 66:
               case 67:
               case 68:
               case 69:
               case 70:
                  var6 -= 55;
                  break;
               case 97:
               case 98:
               case 99:
               case 100:
               case 101:
               case 102:
                  var6 -= 87;
               }

               --var5;
               if (var5 == 0) {
                  var4 = 0;
                  UTF16.append(var1, var6);
               }
               continue;
            }

            if (!this.ignorableCharacters.contains(var7)) {
               if (this.syntaxCharacters.contains(var7)) {
                  if (var2 == 5) {
                     UTF16.append(var1, var7);
                     this.start = var8 + UTF16.getCharCount(var7);
                     return 1;
                  }

                  this.start = var8;
                  return var2;
               }

               var2 = 2;
               if (var7 == 92) {
                  var4 = 3;
               } else if (this.usingQuote && var7 == 39) {
                  var3 = var7;
                  var4 = 1;
               } else {
                  UTF16.append(var1, var7);
               }
            }
         }

         this.start = this.limit;
         switch(var4) {
         case 1:
         case 2:
            var2 = 3;
            break;
         case 3:
            if (this.usingSlash) {
               var2 = 4;
            } else {
               var1.append('\\');
            }
            break;
         case 4:
            var2 = 4;
         }

         return var2;
      }
   }
}
