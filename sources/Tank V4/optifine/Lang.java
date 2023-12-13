package optifine;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class Lang {
   private static final Splitter splitter = Splitter.on('=').limit(2);
   private static final Pattern pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");

   public static void resourcesReloaded() {
      Map var0 = I18n.getLocaleProperties();
      ArrayList var1 = new ArrayList();
      String var2 = "optifine/lang/";
      String var3 = "en_US";
      String var4 = ".lang";
      var1.add(var2 + var3 + var4);
      if (!Config.getGameSettings().language.equals(var3)) {
         var1.add(var2 + Config.getGameSettings().language + var4);
      }

      String[] var5 = (String[])var1.toArray(new String[var1.size()]);
      loadResources(Config.getDefaultResourcePack(), var5, var0);
      IResourcePack[] var6 = Config.getResourcePacks();

      for(int var7 = 0; var7 < var6.length; ++var7) {
         IResourcePack var8 = var6[var7];
         loadResources(var8, var5, var0);
      }

   }

   private static void loadResources(IResourcePack var0, String[] var1, Map var2) {
      try {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = var1[var3];
            ResourceLocation var5 = new ResourceLocation(var4);
            if (var0.resourceExists(var5)) {
               InputStream var6 = var0.getInputStream(var5);
               if (var6 != null) {
                  loadLocaleData(var6, var2);
               }
            }
         }
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   public static void loadLocaleData(InputStream var0, Map var1) throws IOException {
      Iterator var3 = IOUtils.readLines(var0, Charsets.UTF_8).iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         if (!var2.isEmpty() && var2.charAt(0) != '#') {
            String[] var4 = (String[])Iterables.toArray(splitter.split(var2), String.class);
            if (var4 != null && var4.length == 2) {
               String var5 = var4[0];
               String var6 = pattern.matcher(var4[1]).replaceAll("%$1s");
               var1.put(var5, var6);
            }
         }
      }

   }

   public static String get(String var0) {
      return I18n.format(var0);
   }

   public static String get(String var0, String var1) {
      String var2 = I18n.format(var0);
      return var2 != null && !var2.equals(var0) ? var2 : var1;
   }

   public static String getOn() {
      return I18n.format("options.on");
   }

   public static String getOff() {
      return I18n.format("options.off");
   }

   public static String getFast() {
      return I18n.format("options.graphics.fast");
   }

   public static String getFancy() {
      return I18n.format("options.graphics.fancy");
   }

   public static String getDefault() {
      return I18n.format("generator.default");
   }
}
