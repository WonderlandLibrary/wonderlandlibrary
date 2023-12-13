package com.ibm.icu.impl.data;

import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.UTF16;
import java.io.IOException;

public class TokenIterator {
   private ResourceReader reader;
   private String line;
   private StringBuffer buf;
   private boolean done;
   private int pos;
   private int lastpos;

   public TokenIterator(ResourceReader var1) {
      this.reader = var1;
      this.line = null;
      this.done = false;
      this.buf = new StringBuffer();
      this.pos = this.lastpos = -1;
   }

   public String next() throws IOException {
      if (this.done) {
         return null;
      } else {
         while(true) {
            if (this.line == null) {
               this.line = this.reader.readLineSkippingComments();
               if (this.line == null) {
                  this.done = true;
                  return null;
               }

               this.pos = 0;
            }

            this.buf.setLength(0);
            this.lastpos = this.pos;
            this.pos = this.nextToken(this.pos);
            if (this.pos >= 0) {
               return this.buf.toString();
            }

            this.line = null;
         }
      }
   }

   public int getLineNumber() {
      return this.reader.getLineNumber();
   }

   public String describePosition() {
      return this.reader.describePosition() + ':' + (this.lastpos + 1);
   }

   private int nextToken(int var1) {
      var1 = PatternProps.skipWhiteSpace(this.line, var1);
      if (var1 == this.line.length()) {
         return -1;
      } else {
         int var2 = var1;
         char var3 = this.line.charAt(var1++);
         char var4 = 0;
         switch(var3) {
         case '"':
         case '\'':
            var4 = var3;
            break;
         case '#':
            return -1;
         default:
            this.buf.append(var3);
         }

         int[] var5 = null;

         while(true) {
            while(var1 < this.line.length()) {
               var3 = this.line.charAt(var1);
               if (var3 != '\\') {
                  if (var4 != 0 && var3 == var4 || var4 == 0 && PatternProps.isWhiteSpace(var3)) {
                     ++var1;
                     return var1;
                  }

                  if (var4 == 0 && var3 == '#') {
                     return var1;
                  }

                  this.buf.append(var3);
                  ++var1;
               } else {
                  if (var5 == null) {
                     var5 = new int[1];
                  }

                  var5[0] = var1 + 1;
                  int var6 = Utility.unescapeAt(this.line, var5);
                  if (var6 < 0) {
                     throw new RuntimeException("Invalid escape at " + this.reader.describePosition() + ':' + var1);
                  }

                  UTF16.append(this.buf, var6);
                  var1 = var5[0];
               }
            }

            if (var4 != 0) {
               throw new RuntimeException("Unterminated quote at " + this.reader.describePosition() + ':' + var2);
            }

            return var1;
         }
      }
   }
}
