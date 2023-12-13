package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LineIterator implements Iterator {
   private final BufferedReader bufferedReader;
   private String cachedLine;
   private boolean finished = false;

   public LineIterator(Reader var1) throws IllegalArgumentException {
      if (var1 == null) {
         throw new IllegalArgumentException("Reader must not be null");
      } else {
         if (var1 instanceof BufferedReader) {
            this.bufferedReader = (BufferedReader)var1;
         } else {
            this.bufferedReader = new BufferedReader(var1);
         }

      }
   }

   protected boolean isValidLine(String var1) {
      return true;
   }

   public String next() {
      return this.nextLine();
   }

   public String nextLine() {
      if (this != null) {
         throw new NoSuchElementException("No more lines");
      } else {
         String var1 = this.cachedLine;
         this.cachedLine = null;
         return var1;
      }
   }

   public void close() {
      this.finished = true;
      IOUtils.closeQuietly((Reader)this.bufferedReader);
      this.cachedLine = null;
   }

   public void remove() {
      throw new UnsupportedOperationException("Remove unsupported on LineIterator");
   }

   public static void closeQuietly(LineIterator var0) {
      if (var0 != null) {
         var0.close();
      }

   }

   public Object next() {
      return this.next();
   }
}
