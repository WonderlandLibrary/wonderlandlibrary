package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class BasicHeaderIterator implements HeaderIterator {
   protected final Header[] allHeaders;
   protected int currentIndex;
   protected String headerName;

   public BasicHeaderIterator(Header[] var1, String var2) {
      this.allHeaders = (Header[])Args.notNull(var1, "Header array");
      this.headerName = var2;
      this.currentIndex = this.findNext(-1);
   }

   protected int findNext(int var1) {
      int var2 = var1;
      if (var1 < -1) {
         return -1;
      } else {
         int var3 = this.allHeaders.length - 1;

         boolean var4;
         for(var4 = false; !var4 && var2 < var3; var4 = this.filterHeader(var2)) {
            ++var2;
         }

         return var4 ? var2 : -1;
      }
   }

   protected boolean filterHeader(int var1) {
      return this.headerName == null || this.headerName.equalsIgnoreCase(this.allHeaders[var1].getName());
   }

   public boolean hasNext() {
      return this.currentIndex >= 0;
   }

   public Header nextHeader() throws NoSuchElementException {
      int var1 = this.currentIndex;
      if (var1 < 0) {
         throw new NoSuchElementException("Iteration already finished.");
      } else {
         this.currentIndex = this.findNext(var1);
         return this.allHeaders[var1];
      }
   }

   public final Object next() throws NoSuchElementException {
      return this.nextHeader();
   }

   public void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException("Removing headers is not supported.");
   }
}
