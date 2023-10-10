package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import javax.annotation.Nullable;

class MultiReader extends Reader {
   private final Iterator it;
   private Reader current;

   MultiReader(Iterator var1) throws IOException {
      this.it = var1;
      this.advance();
   }

   private void advance() throws IOException {
      this.close();
      if (this.it.hasNext()) {
         this.current = ((CharSource)this.it.next()).openStream();
      }

   }

   public int read(@Nullable char[] var1, int var2, int var3) throws IOException {
      if (this.current == null) {
         return -1;
      } else {
         int var4 = this.current.read(var1, var2, var3);
         if (var4 == -1) {
            this.advance();
            return this.read(var1, var2, var3);
         } else {
            return var4;
         }
      }
   }

   public long skip(long var1) throws IOException {
      Preconditions.checkArgument(var1 >= 0L, "n is negative");
      if (var1 > 0L) {
         while(this.current != null) {
            long var3 = this.current.skip(var1);
            if (var3 > 0L) {
               return var3;
            }

            this.advance();
         }
      }

      return 0L;
   }

   public boolean ready() throws IOException {
      return this.current != null && this.current.ready();
   }

   public void close() throws IOException {
      if (this.current != null) {
         this.current.close();
         this.current = null;
      }

   }
}
