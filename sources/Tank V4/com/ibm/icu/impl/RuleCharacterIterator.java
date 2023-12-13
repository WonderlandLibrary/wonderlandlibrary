package com.ibm.icu.impl;

import com.ibm.icu.text.SymbolTable;
import com.ibm.icu.text.UTF16;
import java.text.ParsePosition;

public class RuleCharacterIterator {
   private String text;
   private ParsePosition pos;
   private SymbolTable sym;
   private char[] buf;
   private int bufPos;
   private boolean isEscaped;
   public static final int DONE = -1;
   public static final int PARSE_VARIABLES = 1;
   public static final int PARSE_ESCAPES = 2;
   public static final int SKIP_WHITESPACE = 4;

   public RuleCharacterIterator(String var1, SymbolTable var2, ParsePosition var3) {
      if (var1 != null && var3.getIndex() <= var1.length()) {
         this.text = var1;
         this.sym = var2;
         this.pos = var3;
         this.buf = null;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public boolean atEnd() {
      return this.buf == null && this.pos.getIndex() == this.text.length();
   }

   public int next(int var1) {
      boolean var2 = true;
      this.isEscaped = false;

      int var4;
      while(true) {
         var4 = this._current();
         this._advance(UTF16.getCharCount(var4));
         if (var4 == 36 && this.buf == null && (var1 & 1) != 0 && this.sym != null) {
            String var5 = this.sym.parseReference(this.text, this.pos, this.text.length());
            if (var5 == null) {
               break;
            }

            this.bufPos = 0;
            this.buf = this.sym.lookup(var5);
            if (this.buf == null) {
               throw new IllegalArgumentException("Undefined variable: " + var5);
            }

            if (this.buf.length == 0) {
               this.buf = null;
            }
         } else if ((var1 & 4) == 0 || !PatternProps.isWhiteSpace(var4)) {
            if (var4 == 92 && (var1 & 2) != 0) {
               int[] var3 = new int[]{0};
               var4 = Utility.unescapeAt(this.lookahead(), var3);
               this.jumpahead(var3[0]);
               this.isEscaped = true;
               if (var4 < 0) {
                  throw new IllegalArgumentException("Invalid escape");
               }
            }
            break;
         }
      }

      return var4;
   }

   public boolean isEscaped() {
      return this.isEscaped;
   }

   public boolean inVariable() {
      return this.buf != null;
   }

   public Object getPos(Object var1) {
      if (var1 == null) {
         return new Object[]{this.buf, new int[]{this.pos.getIndex(), this.bufPos}};
      } else {
         Object[] var2 = (Object[])((Object[])var1);
         var2[0] = this.buf;
         int[] var3 = (int[])((int[])var2[1]);
         var3[0] = this.pos.getIndex();
         var3[1] = this.bufPos;
         return var1;
      }
   }

   public void setPos(Object var1) {
      Object[] var2 = (Object[])((Object[])var1);
      this.buf = (char[])((char[])var2[0]);
      int[] var3 = (int[])((int[])var2[1]);
      this.pos.setIndex(var3[0]);
      this.bufPos = var3[1];
   }

   public void skipIgnored(int var1) {
      if ((var1 & 4) != 0) {
         while(true) {
            int var2 = this._current();
            if (!PatternProps.isWhiteSpace(var2)) {
               break;
            }

            this._advance(UTF16.getCharCount(var2));
         }
      }

   }

   public String lookahead() {
      return this.buf != null ? new String(this.buf, this.bufPos, this.buf.length - this.bufPos) : this.text.substring(this.pos.getIndex());
   }

   public void jumpahead(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException();
      } else {
         if (this.buf != null) {
            this.bufPos += var1;
            if (this.bufPos > this.buf.length) {
               throw new IllegalArgumentException();
            }

            if (this.bufPos == this.buf.length) {
               this.buf = null;
            }
         } else {
            int var2 = this.pos.getIndex() + var1;
            this.pos.setIndex(var2);
            if (var2 > this.text.length()) {
               throw new IllegalArgumentException();
            }
         }

      }
   }

   public String toString() {
      int var1 = this.pos.getIndex();
      return this.text.substring(0, var1) + '|' + this.text.substring(var1);
   }

   private int _current() {
      if (this.buf != null) {
         return UTF16.charAt(this.buf, 0, this.buf.length, this.bufPos);
      } else {
         int var1 = this.pos.getIndex();
         return var1 < this.text.length() ? UTF16.charAt(this.text, var1) : -1;
      }
   }

   private void _advance(int var1) {
      if (this.buf != null) {
         this.bufPos += var1;
         if (this.bufPos == this.buf.length) {
            this.buf = null;
         }
      } else {
         this.pos.setIndex(this.pos.getIndex() + var1);
         if (this.pos.getIndex() > this.text.length()) {
            this.pos.setIndex(this.text.length());
         }
      }

   }
}
