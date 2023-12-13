package net.minecraft.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class StringTranslate {
   private static final Splitter equalSignSplitter = Splitter.on('=').limit(2);
   private static final Pattern numericVariablePattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
   private long lastUpdateTimeInMilliseconds;
   private final Map languageList = Maps.newHashMap();
   private static StringTranslate instance = new StringTranslate();

   public StringTranslate() {
      try {
         InputStream var1 = StringTranslate.class.getResourceAsStream("/assets/minecraft/lang/en_US.lang");
         Iterator var3 = IOUtils.readLines(var1, Charsets.UTF_8).iterator();

         while(var3.hasNext()) {
            String var2 = (String)var3.next();
            if (!var2.isEmpty() && var2.charAt(0) != '#') {
               String[] var4 = (String[])Iterables.toArray(equalSignSplitter.split(var2), String.class);
               if (var4 != null && var4.length == 2) {
                  String var5 = var4[0];
                  String var6 = numericVariablePattern.matcher(var4[1]).replaceAll("%$1s");
                  this.languageList.put(var5, var6);
               }
            }
         }

         this.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
      } catch (IOException var7) {
      }

   }

   public long getLastUpdateTimeInMilliseconds() {
      return this.lastUpdateTimeInMilliseconds;
   }

   private String tryTranslateKey(String var1) {
      String var2 = (String)this.languageList.get(var1);
      return var2 == null ? var1 : var2;
   }

   public static synchronized void replaceWith(Map var0) {
      instance.languageList.clear();
      instance.languageList.putAll(var0);
      instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
   }

   public synchronized String translateKey(String var1) {
      return this.tryTranslateKey(var1);
   }

   public synchronized boolean isKeyTranslated(String var1) {
      return this.languageList.containsKey(var1);
   }

   public synchronized String translateKeyFormat(String var1, Object... var2) {
      String var3 = this.tryTranslateKey(var1);

      try {
         return String.format(var3, var2);
      } catch (IllegalFormatException var5) {
         return "Format error: " + var3;
      }
   }

   static StringTranslate getInstance() {
      return instance;
   }
}
