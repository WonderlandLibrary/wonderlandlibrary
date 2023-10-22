package com.sun.javafx.css.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

final class CSSLexer {
   static final int STRING = 10;
   static final int IDENT = 11;
   static final int FUNCTION = 12;
   static final int NUMBER = 13;
   static final int CM = 14;
   static final int EMS = 15;
   static final int EXS = 16;
   static final int IN = 17;
   static final int MM = 18;
   static final int PC = 19;
   static final int PT = 20;
   static final int PX = 21;
   static final int PERCENTAGE = 22;
   static final int DEG = 23;
   static final int GRAD = 24;
   static final int RAD = 25;
   static final int TURN = 26;
   static final int GREATER = 27;
   static final int LBRACE = 28;
   static final int RBRACE = 29;
   static final int SEMI = 30;
   static final int COLON = 31;
   static final int SOLIDUS = 32;
   static final int STAR = 33;
   static final int LPAREN = 34;
   static final int RPAREN = 35;
   static final int COMMA = 36;
   static final int HASH = 37;
   static final int DOT = 38;
   static final int IMPORTANT_SYM = 39;
   static final int WS = 40;
   static final int NL = 41;
   static final int FONT_FACE = 42;
   static final int URL = 43;
   static final int IMPORT = 44;
   static final int SECONDS = 45;
   static final int MS = 46;
   static final int AT_KEYWORD = 47;
   private final Recognizer A = (var0) -> {
      return var0 == 97 || var0 == 65;
   };
   private final Recognizer B = (var0) -> {
      return var0 == 98 || var0 == 66;
   };
   private final Recognizer C = (var0) -> {
      return var0 == 99 || var0 == 67;
   };
   private final Recognizer D = (var0) -> {
      return var0 == 100 || var0 == 68;
   };
   private final Recognizer E = (var0) -> {
      return var0 == 101 || var0 == 69;
   };
   private final Recognizer F = (var0) -> {
      return var0 == 102 || var0 == 70;
   };
   private final Recognizer G = (var0) -> {
      return var0 == 103 || var0 == 71;
   };
   private final Recognizer H = (var0) -> {
      return var0 == 104 || var0 == 72;
   };
   private final Recognizer I = (var0) -> {
      return var0 == 105 || var0 == 73;
   };
   private final Recognizer J = (var0) -> {
      return var0 == 106 || var0 == 74;
   };
   private final Recognizer K = (var0) -> {
      return var0 == 107 || var0 == 75;
   };
   private final Recognizer L = (var0) -> {
      return var0 == 108 || var0 == 76;
   };
   private final Recognizer M = (var0) -> {
      return var0 == 109 || var0 == 77;
   };
   private final Recognizer N = (var0) -> {
      return var0 == 110 || var0 == 78;
   };
   private final Recognizer O = (var0) -> {
      return var0 == 111 || var0 == 79;
   };
   private final Recognizer P = (var0) -> {
      return var0 == 112 || var0 == 80;
   };
   private final Recognizer Q = (var0) -> {
      return var0 == 113 || var0 == 81;
   };
   private final Recognizer R = (var0) -> {
      return var0 == 114 || var0 == 82;
   };
   private final Recognizer S = (var0) -> {
      return var0 == 115 || var0 == 83;
   };
   private final Recognizer T = (var0) -> {
      return var0 == 116 || var0 == 84;
   };
   private final Recognizer U = (var0) -> {
      return var0 == 117 || var0 == 85;
   };
   private final Recognizer V = (var0) -> {
      return var0 == 118 || var0 == 86;
   };
   private final Recognizer W = (var0) -> {
      return var0 == 119 || var0 == 87;
   };
   private final Recognizer X = (var0) -> {
      return var0 == 120 || var0 == 88;
   };
   private final Recognizer Y = (var0) -> {
      return var0 == 121 || var0 == 89;
   };
   private final Recognizer Z = (var0) -> {
      return var0 == 122 || var0 == 90;
   };
   private final Recognizer ALPHA = (var0) -> {
      return 97 <= var0 && var0 <= 122 || 65 <= var0 && var0 <= 90;
   };
   private final Recognizer NON_ASCII = (var0) -> {
      return 128 <= var0 && var0 <= 65535;
   };
   private final Recognizer DOT_CHAR = (var0) -> {
      return var0 == 46;
   };
   private final Recognizer GREATER_CHAR = (var0) -> {
      return var0 == 62;
   };
   private final Recognizer LBRACE_CHAR = (var0) -> {
      return var0 == 123;
   };
   private final Recognizer RBRACE_CHAR = (var0) -> {
      return var0 == 125;
   };
   private final Recognizer SEMI_CHAR = (var0) -> {
      return var0 == 59;
   };
   private final Recognizer COLON_CHAR = (var0) -> {
      return var0 == 58;
   };
   private final Recognizer SOLIDUS_CHAR = (var0) -> {
      return var0 == 47;
   };
   private final Recognizer MINUS_CHAR = (var0) -> {
      return var0 == 45;
   };
   private final Recognizer PLUS_CHAR = (var0) -> {
      return var0 == 43;
   };
   private final Recognizer STAR_CHAR = (var0) -> {
      return var0 == 42;
   };
   private final Recognizer LPAREN_CHAR = (var0) -> {
      return var0 == 40;
   };
   private final Recognizer RPAREN_CHAR = (var0) -> {
      return var0 == 41;
   };
   private final Recognizer COMMA_CHAR = (var0) -> {
      return var0 == 44;
   };
   private final Recognizer UNDERSCORE_CHAR = (var0) -> {
      return var0 == 95;
   };
   private final Recognizer HASH_CHAR = (var0) -> {
      return var0 == 35;
   };
   private final Recognizer WS_CHARS = (var0) -> {
      return var0 == 32 || var0 == 9 || var0 == 13 || var0 == 10 || var0 == 12;
   };
   private final Recognizer NL_CHARS = (var0) -> {
      return var0 == 13 || var0 == 10;
   };
   private final Recognizer DIGIT = (var0) -> {
      return 48 <= var0 && var0 <= 57;
   };
   private final Recognizer HEX_DIGIT = (var0) -> {
      return 48 <= var0 && var0 <= 57 || 97 <= var0 && var0 <= 102 || 65 <= var0 && var0 <= 70;
   };
   final LexerState initState = new LexerState("initState", (Recognizer)null, new Recognizer[0]) {
      public boolean accepts(int var1) {
         return true;
      }
   };
   final LexerState hashState;
   final LexerState minusState;
   final LexerState plusState;
   final LexerState dotState;
   final LexerState nmStartState;
   final LexerState nmCharState;
   final LexerState hashNameCharState;
   final LexerState lparenState;
   final LexerState leadingDigitsState;
   final LexerState decimalMarkState;
   final LexerState trailingDigitsState;
   final LexerState unitsState;
   private int pos;
   private int offset;
   private int line;
   private int lastc;
   private int ch;
   private boolean charNotConsumed;
   private Reader reader;
   private Token token;
   private final Map stateMap;
   private LexerState currentState;
   private final StringBuilder text;

   public static CSSLexer getInstance() {
      return CSSLexer.InstanceHolder.INSTANCE;
   }

   private Map createStateMap() {
      HashMap var1 = new HashMap();
      var1.put(this.initState, new LexerState[]{this.hashState, this.minusState, this.nmStartState, this.plusState, this.minusState, this.leadingDigitsState, this.dotState});
      var1.put(this.minusState, new LexerState[]{this.nmStartState, this.leadingDigitsState, this.decimalMarkState});
      var1.put(this.hashState, new LexerState[]{this.hashNameCharState});
      var1.put(this.hashNameCharState, new LexerState[]{this.hashNameCharState});
      var1.put(this.nmStartState, new LexerState[]{this.nmCharState});
      var1.put(this.nmCharState, new LexerState[]{this.nmCharState, this.lparenState});
      var1.put(this.plusState, new LexerState[]{this.leadingDigitsState, this.decimalMarkState});
      var1.put(this.leadingDigitsState, new LexerState[]{this.leadingDigitsState, this.decimalMarkState, this.unitsState});
      var1.put(this.dotState, new LexerState[]{this.trailingDigitsState});
      var1.put(this.decimalMarkState, new LexerState[]{this.trailingDigitsState});
      var1.put(this.trailingDigitsState, new LexerState[]{this.trailingDigitsState, this.unitsState});
      var1.put(this.unitsState, new LexerState[]{this.unitsState});
      return var1;
   }

   CSSLexer() {
      this.hashState = new LexerState("hashState", this.HASH_CHAR, new Recognizer[0]);
      this.minusState = new LexerState("minusState", this.MINUS_CHAR, new Recognizer[0]);
      this.plusState = new LexerState("plusState", this.PLUS_CHAR, new Recognizer[0]);
      this.dotState = new LexerState(38, "dotState", this.DOT_CHAR, new Recognizer[0]);
      this.nmStartState = new LexerState(11, "nmStartState", this.UNDERSCORE_CHAR, new Recognizer[]{this.ALPHA});
      this.nmCharState = new LexerState(11, "nmCharState", this.UNDERSCORE_CHAR, new Recognizer[]{this.ALPHA, this.DIGIT, this.MINUS_CHAR});
      this.hashNameCharState = new LexerState(37, "hashNameCharState", this.UNDERSCORE_CHAR, new Recognizer[]{this.ALPHA, this.DIGIT, this.MINUS_CHAR});
      this.lparenState = new LexerState(12, "lparenState", this.LPAREN_CHAR, new Recognizer[0]) {
         public int getType() {
            if (CSSLexer.this.text.indexOf("url(") == 0) {
               try {
                  return CSSLexer.this.consumeUrl();
               } catch (IOException var2) {
                  return 0;
               }
            } else {
               return super.getType();
            }
         }
      };
      this.leadingDigitsState = new LexerState(13, "leadingDigitsState", this.DIGIT, new Recognizer[0]);
      this.decimalMarkState = new LexerState("decimalMarkState", this.DOT_CHAR, new Recognizer[0]);
      this.trailingDigitsState = new LexerState(13, "trailingDigitsState", this.DIGIT, new Recognizer[0]);
      this.unitsState = new UnitsState();
      this.pos = 0;
      this.offset = 0;
      this.line = 1;
      this.lastc = -1;
      this.charNotConsumed = false;
      this.stateMap = this.createStateMap();
      this.text = new StringBuilder(64);
      this.currentState = this.initState;
   }

   public void setReader(Reader var1) {
      this.reader = var1;
      this.lastc = -1;
      this.pos = this.offset = 0;
      this.line = 1;
      this.currentState = this.initState;
      this.token = null;

      try {
         this.ch = this.readChar();
      } catch (IOException var3) {
         this.token = Token.EOF_TOKEN;
      }

   }

   private Token scanImportant() throws IOException {
      Recognizer[] var1 = new Recognizer[]{this.I, this.M, this.P, this.O, this.R, this.T, this.A, this.N, this.T};
      int var2 = 0;
      this.text.append((char)this.ch);
      this.ch = this.readChar();

      while(true) {
         switch (this.ch) {
            case -1:
               this.token = Token.EOF_TOKEN;
               return this.token;
            case 9:
            case 10:
            case 12:
            case 13:
            case 32:
               this.ch = this.readChar();
               break;
            case 47:
               this.ch = this.readChar();
               if (this.ch == 42) {
                  this.skipComment();
               } else {
                  if (this.ch != 47) {
                     this.text.append('/').append((char)this.ch);
                     int var3 = this.offset;
                     this.offset = this.pos;
                     return new Token(0, this.text.toString(), this.line, var3);
                  }

                  this.skipEOL();
               }
               break;
            default:
               boolean var5;
               for(var5 = true; var5 && var2 < var1.length; this.ch = this.readChar()) {
                  var5 = var1[var2++].recognize(this.ch);
                  this.text.append((char)this.ch);
               }

               int var4;
               if (var5) {
                  var4 = this.offset;
                  this.offset = this.pos - 1;
                  return new Token(39, "!important", this.line, var4);
               }

               while(this.ch != 59 && this.ch != 125 && this.ch != -1) {
                  this.ch = this.readChar();
               }

               if (this.ch != -1) {
                  var4 = this.offset;
                  this.offset = this.pos - 1;
                  return new Token(1, this.text.toString(), this.line, var4);
               }

               return Token.EOF_TOKEN;
         }
      }
   }

   private int consumeUrl() throws IOException {
      this.text.delete(0, this.text.length());

      while(this.WS_CHARS.recognize(this.ch)) {
         this.ch = this.readChar();
      }

      if (this.ch == -1) {
         return -1;
      } else {
         int var1;
         if (this.ch != 39 && this.ch != 34) {
            this.text.append((char)this.ch);
            this.ch = this.readChar();

            label125:
            while(true) {
               while(!this.WS_CHARS.recognize(this.ch)) {
                  if (this.ch == 41) {
                     this.ch = this.readChar();
                     return 43;
                  }

                  if (this.ch == -1) {
                     return 43;
                  }

                  if (this.ch == 92) {
                     this.ch = this.readChar();
                     if (this.NL_CHARS.recognize(this.ch)) {
                        while(this.NL_CHARS.recognize(this.ch)) {
                           this.ch = this.readChar();
                        }
                     } else if (this.ch != -1) {
                        this.text.append((char)this.ch);
                        this.ch = this.readChar();
                     }
                  } else {
                     if (this.ch == 39 || this.ch == 34 || this.ch == 40) {
                        break label125;
                     }

                     this.text.append((char)this.ch);
                     this.ch = this.readChar();
                  }
               }

               this.ch = this.readChar();
            }
         } else {
            var1 = this.ch;
            this.ch = this.readChar();

            label98:
            while(true) {
               while(true) {
                  while(this.ch != var1 && this.ch != -1 && !this.NL_CHARS.recognize(this.ch)) {
                     if (this.ch == 92) {
                        this.ch = this.readChar();
                        if (this.NL_CHARS.recognize(this.ch)) {
                           while(this.NL_CHARS.recognize(this.ch)) {
                              this.ch = this.readChar();
                           }
                        } else if (this.ch != -1) {
                           this.text.append((char)this.ch);
                           this.ch = this.readChar();
                        }
                     } else {
                        this.text.append((char)this.ch);
                        this.ch = this.readChar();
                     }
                  }

                  if (this.ch == var1) {
                     for(this.ch = this.readChar(); this.WS_CHARS.recognize(this.ch); this.ch = this.readChar()) {
                     }

                     if (this.ch == 41) {
                        this.ch = this.readChar();
                        return 43;
                     }

                     if (this.ch == -1) {
                        return 43;
                     }
                  }
                  break label98;
               }
            }
         }

         while(true) {
            var1 = this.ch;
            if (this.ch == -1) {
               return -1;
            }

            if (this.ch == 41 && var1 != 92) {
               this.ch = this.readChar();
               return 0;
            }

            var1 = this.ch;
            this.ch = this.readChar();
         }
      }
   }

   private void skipComment() throws IOException {
      while(true) {
         if (this.ch != -1) {
            if (this.ch != 42) {
               this.ch = this.readChar();
               continue;
            }

            this.ch = this.readChar();
            if (this.ch != 47) {
               continue;
            }

            this.offset = this.pos;
            this.ch = this.readChar();
         }

         return;
      }
   }

   private void skipEOL() throws IOException {
      int var1 = this.ch;

      while(this.ch != -1) {
         this.ch = this.readChar();
         if (this.ch == 10 || var1 == 13 && this.ch != 10) {
            break;
         }
      }

   }

   private int readChar() throws IOException {
      int var1 = this.reader.read();
      if (this.lastc != 10 && (this.lastc != 13 || var1 == 10)) {
         ++this.pos;
      } else {
         this.pos = 1;
         this.offset = 0;
         ++this.line;
      }

      this.lastc = var1;
      return var1;
   }

   public Token nextToken() {
      Token var1 = null;
      if (this.token != null) {
         var1 = this.token;
         if (this.token.getType() != -1) {
            this.token = null;
         }
      } else {
         do {
            var1 = this.getToken();
         } while(var1 != null && Token.SKIP_TOKEN.equals(var1));
      }

      this.text.delete(0, this.text.length());
      this.currentState = this.initState;
      return var1;
   }

   private Token getToken() {
      try {
         while(true) {
            this.charNotConsumed = false;
            LexerState[] var1 = this.currentState != null ? (LexerState[])this.stateMap.get(this.currentState) : null;
            int var2 = var1 != null ? var1.length : 0;
            LexerState var3 = null;

            int var4;
            for(var4 = 0; var4 < var2 && var3 == null; ++var4) {
               LexerState var5 = var1[var4];
               if (var5.accepts(this.ch)) {
                  var3 = var5;
               }
            }

            if (var3 != null) {
               this.currentState = var3;
               this.text.append((char)this.ch);
               this.ch = this.readChar();
            } else {
               var4 = this.currentState != null ? this.currentState.getType() : 0;
               if (var4 == 0 && this.currentState.equals(this.initState)) {
                  Token var10;
                  switch (this.ch) {
                     case -1:
                        this.token = Token.EOF_TOKEN;
                        return this.token;
                     case 9:
                     case 12:
                     case 32:
                        this.token = new Token(40, Character.toString((char)this.ch), this.line, this.offset);
                        this.offset = this.pos;
                        break;
                     case 10:
                        this.token = new Token(41, "\\n", this.line, this.offset);
                        break;
                     case 13:
                        this.token = new Token(41, "\\r", this.line, this.offset);
                        this.ch = this.readChar();
                        if (this.ch != 10) {
                           var10 = this.token;
                           this.token = this.ch == -1 ? Token.EOF_TOKEN : null;
                           return var10;
                        }

                        this.token = new Token(41, "\\r\\n", this.line, this.offset);
                        break;
                     case 33:
                        var10 = this.scanImportant();
                        return var10;
                     case 34:
                     case 39:
                        this.text.append((char)this.ch);
                        var4 = this.ch;

                        while((this.ch = this.readChar()) != -1) {
                           this.text.append((char)this.ch);
                           if (this.ch == var4) {
                              break;
                           }
                        }

                        if (this.ch != -1) {
                           this.token = new Token(10, this.text.toString(), this.line, this.offset);
                           this.offset = this.pos;
                        } else {
                           this.token = new Token(0, this.text.toString(), this.line, this.offset);
                           this.offset = this.pos;
                        }
                        break;
                     case 40:
                        this.token = new Token(34, "(", this.line, this.offset);
                        this.offset = this.pos;
                        break;
                     case 41:
                        this.token = new Token(35, ")", this.line, this.offset);
                        this.offset = this.pos;
                        break;
                     case 42:
                        this.token = new Token(33, "*", this.line, this.offset);
                        this.offset = this.pos;
                        break;
                     case 44:
                        this.token = new Token(36, ",", this.line, this.offset);
                        this.offset = this.pos;
                        break;
                     case 46:
                        this.token = new Token(38, ".", this.line, this.offset);
                        this.offset = this.pos;
                        break;
                     case 47:
                        this.ch = this.readChar();
                        if (this.ch == 42) {
                           this.skipComment();
                           if (this.ch != -1) {
                              continue;
                           }

                           this.token = Token.EOF_TOKEN;
                           return this.token;
                        }

                        if (this.ch == 47) {
                           this.skipEOL();
                           if (this.ch != -1) {
                              continue;
                           }

                           this.token = Token.EOF_TOKEN;
                           return this.token;
                        }

                        this.token = new Token(32, "/", this.line, this.offset);
                        this.offset = this.pos;
                        this.charNotConsumed = true;
                        break;
                     case 58:
                        this.token = new Token(31, ":", this.line, this.offset);
                        this.offset = this.pos;
                        break;
                     case 59:
                        this.token = new Token(30, ";", this.line, this.offset);
                        this.offset = this.pos;
                        break;
                     case 62:
                        this.token = new Token(27, ">", this.line, this.offset);
                        this.offset = this.pos;
                        break;
                     case 64:
                        this.token = new Token(47, "@", this.line, this.offset);
                        this.offset = this.pos;
                        break;
                     case 123:
                        this.token = new Token(28, "{", this.line, this.offset);
                        this.offset = this.pos;
                        break;
                     case 125:
                        this.token = new Token(29, "}", this.line, this.offset);
                        this.offset = this.pos;
                        break;
                     default:
                        this.token = new Token(0, Character.toString((char)this.ch), this.line, this.offset);
                        this.offset = this.pos;
                  }

                  if (this.token == null) {
                     this.token = new Token(0, (String)null, this.line, this.offset);
                     this.offset = this.pos;
                  } else if (this.token.getType() == -1) {
                     return this.token;
                  }

                  if (this.ch != -1 && !this.charNotConsumed) {
                     this.ch = this.readChar();
                  }

                  Token var9 = this.token;
                  this.token = null;
                  return var9;
               }

               String var8 = this.text.toString();
               Token var6 = new Token(var4, var8, this.line, this.offset);
               this.offset = this.pos - 1;
               return var6;
            }
         }
      } catch (IOException var7) {
         this.token = Token.EOF_TOKEN;
         return this.token;
      }
   }

   private class UnitsState extends LexerState {
      private final Recognizer[][] units;
      private int unitsMask;
      private int index;

      UnitsState() {
         super(-1, "UnitsState", (Recognizer)null);
         this.units = new Recognizer[][]{{CSSLexer.this.C, CSSLexer.this.M}, {CSSLexer.this.D, CSSLexer.this.E, CSSLexer.this.G}, {CSSLexer.this.E, CSSLexer.this.M}, {CSSLexer.this.E, CSSLexer.this.X}, {CSSLexer.this.G, CSSLexer.this.R, CSSLexer.this.A, CSSLexer.this.D}, {CSSLexer.this.I, CSSLexer.this.N}, {CSSLexer.this.M, CSSLexer.this.M}, {CSSLexer.this.M, CSSLexer.this.S}, {CSSLexer.this.P, CSSLexer.this.C}, {CSSLexer.this.P, CSSLexer.this.T}, {CSSLexer.this.P, CSSLexer.this.X}, {CSSLexer.this.R, CSSLexer.this.A, CSSLexer.this.D}, {CSSLexer.this.S}, {CSSLexer.this.T, CSSLexer.this.U, CSSLexer.this.R, CSSLexer.this.N}, {(var0) -> {
            return var0 == 37;
         }}};
         this.unitsMask = 32767;
         this.index = -1;
      }

      public int getType() {
         boolean var1 = false;
         byte var2;
         switch (this.unitsMask) {
            case 1:
               var2 = 14;
               break;
            case 2:
               var2 = 23;
               break;
            case 4:
               var2 = 15;
               break;
            case 8:
               var2 = 16;
               break;
            case 16:
               var2 = 24;
               break;
            case 32:
               var2 = 17;
               break;
            case 64:
               var2 = 18;
               break;
            case 128:
               var2 = 46;
               break;
            case 256:
               var2 = 19;
               break;
            case 512:
               var2 = 20;
               break;
            case 1024:
               var2 = 21;
               break;
            case 2048:
               var2 = 25;
               break;
            case 4096:
               var2 = 45;
               break;
            case 8192:
               var2 = 26;
               break;
            case 16384:
               var2 = 22;
               break;
            default:
               var2 = 0;
         }

         this.unitsMask = 32767;
         this.index = -1;
         return var2;
      }

      public boolean accepts(int var1) {
         if (!CSSLexer.this.ALPHA.recognize(var1) && var1 != 37) {
            return false;
         } else if (this.unitsMask == 0) {
            return true;
         } else {
            ++this.index;

            for(int var2 = 0; var2 < this.units.length; ++var2) {
               int var3 = 1 << var2;
               if ((this.unitsMask & var3) != 0 && (this.index >= this.units[var2].length || !this.units[var2][this.index].recognize(var1))) {
                  this.unitsMask &= ~var3;
               }
            }

            return true;
         }
      }
   }

   private static class InstanceHolder {
      static final CSSLexer INSTANCE = new CSSLexer();
   }
}
