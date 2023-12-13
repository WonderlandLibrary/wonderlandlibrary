package org.apache.commons.compress.compressors;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class FileNameUtil {
   private final Map compressSuffix = new HashMap();
   private final Map uncompressSuffix;
   private final int longestCompressedSuffix;
   private final int shortestCompressedSuffix;
   private final int longestUncompressedSuffix;
   private final int shortestUncompressedSuffix;
   private final String defaultExtension;

   public FileNameUtil(Map var1, String var2) {
      this.uncompressSuffix = Collections.unmodifiableMap(var1);
      int var3 = Integer.MIN_VALUE;
      int var4 = Integer.MAX_VALUE;
      int var5 = Integer.MIN_VALUE;
      int var6 = Integer.MAX_VALUE;
      Iterator var7 = var1.entrySet().iterator();

      while(var7.hasNext()) {
         Entry var8 = (Entry)var7.next();
         int var9 = ((String)var8.getKey()).length();
         if (var9 > var3) {
            var3 = var9;
         }

         if (var9 < var4) {
            var4 = var9;
         }

         String var10 = (String)var8.getValue();
         int var11 = var10.length();
         if (var11 > 0) {
            if (!this.compressSuffix.containsKey(var10)) {
               this.compressSuffix.put(var10, var8.getKey());
            }

            if (var11 > var5) {
               var5 = var11;
            }

            if (var11 < var6) {
               var6 = var11;
            }
         }
      }

      this.longestCompressedSuffix = var3;
      this.longestUncompressedSuffix = var5;
      this.shortestCompressedSuffix = var4;
      this.shortestUncompressedSuffix = var6;
      this.defaultExtension = var2;
   }

   public boolean isCompressedFilename(String var1) {
      String var2 = var1.toLowerCase(Locale.ENGLISH);
      int var3 = var2.length();

      for(int var4 = this.shortestCompressedSuffix; var4 <= this.longestCompressedSuffix && var4 < var3; ++var4) {
         if (this.uncompressSuffix.containsKey(var2.substring(var3 - var4))) {
            return true;
         }
      }

      return false;
   }

   public String getUncompressedFilename(String var1) {
      String var2 = var1.toLowerCase(Locale.ENGLISH);
      int var3 = var2.length();

      for(int var4 = this.shortestCompressedSuffix; var4 <= this.longestCompressedSuffix && var4 < var3; ++var4) {
         String var5 = (String)this.uncompressSuffix.get(var2.substring(var3 - var4));
         if (var5 != null) {
            return var1.substring(0, var3 - var4) + var5;
         }
      }

      return var1;
   }

   public String getCompressedFilename(String var1) {
      String var2 = var1.toLowerCase(Locale.ENGLISH);
      int var3 = var2.length();

      for(int var4 = this.shortestUncompressedSuffix; var4 <= this.longestUncompressedSuffix && var4 < var3; ++var4) {
         String var5 = (String)this.compressSuffix.get(var2.substring(var3 - var4));
         if (var5 != null) {
            return var1.substring(0, var3 - var4) + var5;
         }
      }

      return var1 + this.defaultExtension;
   }
}
