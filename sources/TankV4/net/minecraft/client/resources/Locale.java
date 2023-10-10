package net.minecraft.client.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class Locale {
   private boolean unicode;
   Map properties = Maps.newHashMap();
   private static final Pattern pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
   private static final Splitter splitter = Splitter.on('=').limit(2);

   private String translateKeyPrivate(String var1) {
      String var2 = (String)this.properties.get(var1);
      return var2 == null ? var1 : var2;
   }

   public synchronized void loadLocaleDataFiles(IResourceManager var1, List var2) {
      this.properties.clear();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         String var3 = (String)var4.next();
         String var5 = String.format("lang/%s.lang", var3);
         Iterator var7 = var1.getResourceDomains().iterator();

         while(var7.hasNext()) {
            String var6 = (String)var7.next();

            try {
               this.loadLocaleData(var1.getAllResources(new ResourceLocation(var6, var5)));
            } catch (IOException var10) {
            }
         }
      }

      this.checkUnicode();
   }

   private void loadLocaleData(InputStream var1) throws IOException {
      Iterator var3 = IOUtils.readLines(var1, Charsets.UTF_8).iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         if (!var2.isEmpty() && var2.charAt(0) != '#') {
            String[] var4 = (String[])Iterables.toArray(splitter.split(var2), String.class);
            if (var4 != null && var4.length == 2) {
               String var5 = var4[0];
               String var6 = pattern.matcher(var4[1]).replaceAll("%$1s");
               this.properties.put(var5, var6);
            }
         }
      }

   }

   public boolean isUnicode() {
      return this.unicode;
   }

   public String formatMessage(String var1, Object[] var2) {
      String var3 = this.translateKeyPrivate(var1);

      try {
         return String.format(var3, var2);
      } catch (IllegalFormatException var5) {
         return "Format error: " + var3;
      }
   }

   private void checkUnicode() {
      this.unicode = false;
      int var1 = 0;
      int var2 = 0;
      Iterator var4 = this.properties.values().iterator();

      while(var4.hasNext()) {
         String var3 = (String)var4.next();
         int var5 = var3.length();
         var2 += var5;

         for(int var6 = 0; var6 < var5; ++var6) {
            if (var3.charAt(var6) >= 256) {
               ++var1;
            }
         }
      }

      float var7 = (float)var1 / (float)var2;
      this.unicode = (double)var7 > 0.1D;
   }

   private void loadLocaleData(List var1) throws IOException {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         IResource var2 = (IResource)var3.next();
         InputStream var4 = var2.getInputStream();
         this.loadLocaleData(var4);
         IOUtils.closeQuietly(var4);
      }

   }
}
