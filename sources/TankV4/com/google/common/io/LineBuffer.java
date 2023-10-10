package com.google.common.io;

import java.io.IOException;

abstract class LineBuffer {
   private StringBuilder line = new StringBuilder();
   private boolean sawReturn;

   protected void add(char[] var1, int var2, int var3) throws IOException {
      int var4 = var2;
      if (this.sawReturn && var3 > 0 && var1[var2] == '\n') {
         var4 = var2 + 1;
      }

      int var5 = var4;

      for(int var6 = var2 + var3; var4 < var6; ++var4) {
         switch(var1[var4]) {
         case '\n':
            this.line.append(var1, var5, var4 - var5);
            this.finishLine(true);
            var5 = var4 + 1;
            break;
         case '\r':
            this.line.append(var1, var5, var4 - var5);
            this.sawReturn = true;
            if (var4 + 1 < var6 && var1[var4 + 1] == '\n') {
               ++var4;
            }

            var5 = var4 + 1;
         }
      }

      this.line.append(var1, var5, var2 + var3 - var5);
   }

   protected void finish() throws IOException {
      if (this.sawReturn || this.line.length() > 0) {
         this.finishLine(false);
      }

   }

   protected abstract void handleLine(String var1, String var2) throws IOException;
}
