package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.HeaderIterator;
import org.apache.http.ParseException;
import org.apache.http.TokenIterator;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class BasicTokenIterator implements TokenIterator {
   public static final String HTTP_SEPARATORS = " ,;=()<>@:\\\"/[]?{}\t";
   protected final HeaderIterator headerIt;
   protected String currentHeader;
   protected String currentToken;
   protected int searchPos;

   public BasicTokenIterator(HeaderIterator var1) {
      this.headerIt = (HeaderIterator)Args.notNull(var1, "Header iterator");
      this.searchPos = this.findNext(-1);
   }

   public boolean hasNext() {
      return this.currentToken != null;
   }

   public String nextToken() throws NoSuchElementException, ParseException {
      if (this.currentToken == null) {
         throw new NoSuchElementException("Iteration already finished.");
      } else {
         String var1 = this.currentToken;
         this.searchPos = this.findNext(this.searchPos);
         return var1;
      }
   }

   public final Object next() throws NoSuchElementException, ParseException {
      return this.nextToken();
   }

   public final void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException("Removing tokens is not supported.");
   }

   protected int findNext(int var1) throws ParseException {
      int var2;
      if (var1 < 0) {
         if (!this.headerIt.hasNext()) {
            return -1;
         }

         this.currentHeader = this.headerIt.nextHeader().getValue();
         var2 = 0;
      } else {
         var2 = this.findTokenSeparator(var1);
      }

      int var3 = this.findTokenStart(var2);
      if (var3 < 0) {
         this.currentToken = null;
         return -1;
      } else {
         int var4 = this.findTokenEnd(var3);
         this.currentToken = this.createToken(this.currentHeader, var3, var4);
         return var4;
      }
   }

   protected String createToken(String var1, int var2, int var3) {
      return var1.substring(var2, var3);
   }

   protected int findTokenStart(int var1) {
      int var2 = Args.notNegative(var1, "Search position");
      boolean var3 = false;

      while(!var3 && this.currentHeader != null) {
         int var4 = this.currentHeader.length();

         while(!var3 && var2 < var4) {
            char var5 = this.currentHeader.charAt(var2);
            if (this == var5 && this == var5) {
               if (this.currentHeader.charAt(var2) == 0) {
                  throw new ParseException("Invalid character before token (pos " + var2 + "): " + this.currentHeader);
               }

               var3 = true;
            } else {
               ++var2;
            }
         }

         if (!var3) {
            if (this.headerIt.hasNext()) {
               this.currentHeader = this.headerIt.nextHeader().getValue();
               var2 = 0;
            } else {
               this.currentHeader = null;
            }
         }
      }

      return var3 ? var2 : -1;
   }

   protected int findTokenSeparator(int var1) {
      int var2 = Args.notNegative(var1, "Search position");
      boolean var3 = false;
      int var4 = this.currentHeader.length();

      while(!var3 && var2 < var4) {
         char var5 = this.currentHeader.charAt(var2);
         if (this == var5) {
            var3 = true;
         } else {
            if (this == var5) {
               if (var5 != 0) {
                  throw new ParseException("Tokens without separator (pos " + var2 + "): " + this.currentHeader);
               }

               throw new ParseException("Invalid character after token (pos " + var2 + "): " + this.currentHeader);
            }

            ++var2;
         }
      }

      return var2;
   }

   protected int findTokenEnd(int var1) {
      Args.notNegative(var1, "Search position");
      int var2 = this.currentHeader.length();

      int var3;
      for(var3 = var1 + 1; var3 < var2 && this.currentHeader.charAt(var3) != 0; ++var3) {
      }

      return var3;
   }
}
