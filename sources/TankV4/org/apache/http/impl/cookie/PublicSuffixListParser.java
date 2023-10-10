package org.apache.http.impl.cookie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import org.apache.http.annotation.Immutable;

@Immutable
public class PublicSuffixListParser {
   private static final int MAX_LINE_LEN = 256;
   private final PublicSuffixFilter filter;

   PublicSuffixListParser(PublicSuffixFilter var1) {
      this.filter = var1;
   }

   public void parse(Reader var1) throws IOException {
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();
      BufferedReader var4 = new BufferedReader(var1);
      StringBuilder var5 = new StringBuilder(256);
      boolean var6 = true;

      while(var6) {
         var6 = this.readLine(var4, var5);
         String var7 = var5.toString();
         if (var7.length() != 0 && !var7.startsWith("//")) {
            if (var7.startsWith(".")) {
               var7 = var7.substring(1);
            }

            boolean var8 = var7.startsWith("!");
            if (var8) {
               var7 = var7.substring(1);
            }

            if (var8) {
               var3.add(var7);
            } else {
               var2.add(var7);
            }
         }
      }

      this.filter.setPublicSuffixes(var2);
      this.filter.setExceptions(var3);
   }

   private boolean readLine(Reader var1, StringBuilder var2) throws IOException {
      var2.setLength(0);
      boolean var4 = false;

      int var3;
      while((var3 = var1.read()) != -1) {
         char var5 = (char)var3;
         if (var5 == '\n') {
            break;
         }

         if (Character.isWhitespace(var5)) {
            var4 = true;
         }

         if (!var4) {
            var2.append(var5);
         }

         if (var2.length() > 256) {
            throw new IOException("Line too long");
         }
      }

      return var3 != -1;
   }
}
