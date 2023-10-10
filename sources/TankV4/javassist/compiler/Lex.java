package javassist.compiler;

public class Lex implements TokenId {
   private int lastChar = -1;
   private StringBuffer textBuffer = new StringBuffer();
   private Token currentToken = new Token();
   private Token lookAheadTokens = null;
   private String input;
   private int position;
   private int maxlen;
   private int lineNumber;
   private static final int[] equalOps = new int[]{350, 0, 0, 0, 351, 352, 0, 0, 0, 353, 354, 0, 355, 0, 356, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 357, 358, 359, 0};
   private static final KeywordTable ktable = new KeywordTable();

   public Lex(String var1) {
      this.input = var1;
      this.position = 0;
      this.maxlen = var1.length();
      this.lineNumber = 0;
   }

   public int get() {
      if (this.lookAheadTokens == null) {
         return this.get(this.currentToken);
      } else {
         Token var1;
         this.currentToken = var1 = this.lookAheadTokens;
         this.lookAheadTokens = this.lookAheadTokens.next;
         return var1.tokenId;
      }
   }

   public int lookAhead() {
      return this.lookAhead(0);
   }

   public int lookAhead(int var1) {
      Token var2 = this.lookAheadTokens;
      if (var2 == null) {
         this.lookAheadTokens = var2 = this.currentToken;
         var2.next = null;
         this.get(var2);
      }

      for(; var1-- > 0; var2 = var2.next) {
         if (var2.next == null) {
            Token var3;
            var2.next = var3 = new Token();
            this.get(var3);
         }
      }

      this.currentToken = var2;
      return var2.tokenId;
   }

   public String getString() {
      return this.currentToken.textValue;
   }

   public long getLong() {
      return this.currentToken.longValue;
   }

   public double getDouble() {
      return this.currentToken.doubleValue;
   }

   private int get(Token var1) {
      int var2;
      do {
         var2 = this.readLine(var1);
      } while(var2 == 10);

      var1.tokenId = var2;
      return var2;
   }

   private int readLine(Token var1) {
      int var2 = this.getNextNonWhiteChar();
      if (var2 < 0) {
         return var2;
      } else if (var2 == 10) {
         ++this.lineNumber;
         return 10;
      } else if (var2 == 39) {
         return this.readCharConst(var1);
      } else if (var2 == 34) {
         return this.readStringL(var1);
      } else if (48 <= var2 && var2 <= 57) {
         return this.readNumber(var2, var1);
      } else if (var2 == 46) {
         var2 = this.getc();
         if (48 <= var2 && var2 <= 57) {
            StringBuffer var3 = this.textBuffer;
            var3.setLength(0);
            var3.append('.');
            return this.readDouble(var3, var2, var1);
         } else {
            this.ungetc(var2);
            return this.readSeparator(46);
         }
      } else {
         return Character.isJavaIdentifierStart((char)var2) ? this.readIdentifier(var2, var1) : this.readSeparator(var2);
      }
   }

   private int getNextNonWhiteChar() {
      // $FF: Couldn't be decompiled
   }

   private int readCharConst(Token var1) {
      int var3 = 0;

      int var2;
      while((var2 = this.getc()) != 39) {
         if (var2 == 92) {
            var3 = this.readEscapeChar();
         } else {
            if (var2 < 32) {
               if (var2 == 10) {
                  ++this.lineNumber;
               }

               return 500;
            }

            var3 = var2;
         }
      }

      var1.longValue = (long)var3;
      return 401;
   }

   private int readEscapeChar() {
      int var1 = this.getc();
      if (var1 == 110) {
         var1 = 10;
      } else if (var1 == 116) {
         var1 = 9;
      } else if (var1 == 114) {
         var1 = 13;
      } else if (var1 == 102) {
         var1 = 12;
      } else if (var1 == 10) {
         ++this.lineNumber;
      }

      return var1;
   }

   private int readStringL(Token param1) {
      // $FF: Couldn't be decompiled
   }

   private int readNumber(int var1, Token var2) {
      long var3 = 0L;
      int var5 = this.getc();
      if (var1 == 48) {
         if (var5 == 88 || var5 == 120) {
            while(true) {
               while(true) {
                  while(true) {
                     var1 = this.getc();
                     if (48 > var1 || var1 > 57) {
                        if (65 > var1 || var1 > 70) {
                           if (97 > var1 || var1 > 102) {
                              var2.longValue = var3;
                              if (var1 != 76 && var1 != 108) {
                                 this.ungetc(var1);
                                 return 402;
                              }

                              return 403;
                           }

                           var3 = var3 * 16L + (long)(var1 - 97 + 10);
                        } else {
                           var3 = var3 * 16L + (long)(var1 - 65 + 10);
                        }
                     } else {
                        var3 = var3 * 16L + (long)(var1 - 48);
                     }
                  }
               }
            }
         }

         if (48 <= var5 && var5 <= 55) {
            var3 = (long)(var5 - 48);

            while(true) {
               var1 = this.getc();
               if (48 > var1 || var1 > 55) {
                  var2.longValue = var3;
                  if (var1 != 76 && var1 != 108) {
                     this.ungetc(var1);
                     return 402;
                  }

                  return 403;
               }

               var3 = var3 * 8L + (long)(var1 - 48);
            }
         }
      }

      for(var3 = (long)(var1 - 48); 48 <= var5 && var5 <= 57; var5 = this.getc()) {
         var3 = var3 * 10L + (long)var5 - 48L;
      }

      var2.longValue = var3;
      if (var5 != 70 && var5 != 102) {
         if (var5 != 69 && var5 != 101 && var5 != 68 && var5 != 100 && var5 != 46) {
            if (var5 != 76 && var5 != 108) {
               this.ungetc(var5);
               return 402;
            } else {
               return 403;
            }
         } else {
            StringBuffer var6 = this.textBuffer;
            var6.setLength(0);
            var6.append(var3);
            return this.readDouble(var6, var5, var2);
         }
      } else {
         var2.doubleValue = (double)var3;
         return 404;
      }
   }

   private int readDouble(StringBuffer var1, int var2, Token var3) {
      if (var2 != 69 && var2 != 101 && var2 != 68 && var2 != 100) {
         var1.append((char)var2);

         while(true) {
            var2 = this.getc();
            if (48 > var2 || var2 > 57) {
               break;
            }

            var1.append((char)var2);
         }
      }

      if (var2 == 69 || var2 == 101) {
         var1.append((char)var2);
         var2 = this.getc();
         if (var2 == 43 || var2 == 45) {
            var1.append((char)var2);
            var2 = this.getc();
         }

         while(48 <= var2 && var2 <= 57) {
            var1.append((char)var2);
            var2 = this.getc();
         }
      }

      try {
         var3.doubleValue = Double.parseDouble(var1.toString());
      } catch (NumberFormatException var5) {
         return 500;
      }

      if (var2 != 70 && var2 != 102) {
         if (var2 != 68 && var2 != 100) {
            this.ungetc(var2);
         }

         return 405;
      } else {
         return 404;
      }
   }

   private int readSeparator(int var1) {
      int var2;
      if (33 <= var1 && var1 <= 63) {
         int var4 = equalOps[var1 - 33];
         if (var4 == 0) {
            return var1;
         }

         var2 = this.getc();
         if (var1 == var2) {
            int var3;
            switch(var1) {
            case 38:
               return 369;
            case 43:
               return 362;
            case 45:
               return 363;
            case 60:
               var3 = this.getc();
               if (var3 == 61) {
                  return 365;
               }

               this.ungetc(var3);
               return 364;
            case 61:
               return 358;
            case 62:
               var3 = this.getc();
               if (var3 == 61) {
                  return 367;
               }

               if (var3 == 62) {
                  var3 = this.getc();
                  if (var3 == 61) {
                     return 371;
                  }

                  this.ungetc(var3);
                  return 370;
               }

               this.ungetc(var3);
               return 366;
            }
         } else if (var2 == 61) {
            return var4;
         }
      } else if (var1 == 94) {
         var2 = this.getc();
         if (var2 == 61) {
            return 360;
         }
      } else {
         if (var1 != 124) {
            return var1;
         }

         var2 = this.getc();
         if (var2 == 61) {
            return 361;
         }

         if (var2 == 124) {
            return 368;
         }
      }

      this.ungetc(var2);
      return var1;
   }

   private int readIdentifier(int var1, Token var2) {
      StringBuffer var3 = this.textBuffer;
      var3.setLength(0);

      do {
         var3.append((char)var1);
         var1 = this.getc();
      } while(Character.isJavaIdentifierPart((char)var1));

      this.ungetc(var1);
      String var4 = var3.toString();
      int var5 = ktable.lookup(var4);
      if (var5 >= 0) {
         return var5;
      } else {
         var2.textValue = var4;
         return 400;
      }
   }

   private static boolean isDigit(int var0) {
      return 48 <= var0 && var0 <= 57;
   }

   private void ungetc(int var1) {
      this.lastChar = var1;
   }

   public String getTextAround() {
      int var1 = this.position - 10;
      if (var1 < 0) {
         var1 = 0;
      }

      int var2 = this.position + 10;
      if (var2 > this.maxlen) {
         var2 = this.maxlen;
      }

      return this.input.substring(var1, var2);
   }

   private int getc() {
      if (this.lastChar < 0) {
         return this.position < this.maxlen ? this.input.charAt(this.position++) : -1;
      } else {
         int var1 = this.lastChar;
         this.lastChar = -1;
         return var1;
      }
   }

   static {
      ktable.append("abstract", 300);
      ktable.append("boolean", 301);
      ktable.append("break", 302);
      ktable.append("byte", 303);
      ktable.append("case", 304);
      ktable.append("catch", 305);
      ktable.append("char", 306);
      ktable.append("class", 307);
      ktable.append("const", 308);
      ktable.append("continue", 309);
      ktable.append("default", 310);
      ktable.append("do", 311);
      ktable.append("double", 312);
      ktable.append("else", 313);
      ktable.append("extends", 314);
      ktable.append("false", 411);
      ktable.append("final", 315);
      ktable.append("finally", 316);
      ktable.append("float", 317);
      ktable.append("for", 318);
      ktable.append("goto", 319);
      ktable.append("if", 320);
      ktable.append("implements", 321);
      ktable.append("import", 322);
      ktable.append("instanceof", 323);
      ktable.append("int", 324);
      ktable.append("interface", 325);
      ktable.append("long", 326);
      ktable.append("native", 327);
      ktable.append("new", 328);
      ktable.append("null", 412);
      ktable.append("package", 329);
      ktable.append("private", 330);
      ktable.append("protected", 331);
      ktable.append("public", 332);
      ktable.append("return", 333);
      ktable.append("short", 334);
      ktable.append("static", 335);
      ktable.append("strictfp", 347);
      ktable.append("super", 336);
      ktable.append("switch", 337);
      ktable.append("synchronized", 338);
      ktable.append("this", 339);
      ktable.append("throw", 340);
      ktable.append("throws", 341);
      ktable.append("transient", 342);
      ktable.append("true", 410);
      ktable.append("try", 343);
      ktable.append("void", 344);
      ktable.append("volatile", 345);
      ktable.append("while", 346);
   }
}
