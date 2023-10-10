package optifine;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.minecraft.client.ClientBrandRetriever;

public class VersionCheckThread extends Thread {
   public void run() {
      HttpURLConnection var1 = null;

      try {
         Config.dbg("Checking for new version");
         URL var2 = new URL("http://optifine.net/version/1.8.8/HD_U.txt");
         var1 = (HttpURLConnection)var2.openConnection();
         if (Config.getGameSettings().snooperEnabled) {
            var1.setRequestProperty("OF-MC-Version", "1.8.8");
            var1.setRequestProperty("OF-MC-Brand", ClientBrandRetriever.getClientModName());
            var1.setRequestProperty("OF-Edition", "HD_U");
            var1.setRequestProperty("OF-Release", "H8");
            var1.setRequestProperty("OF-Java-Version", System.getProperty("java.version"));
            var1.setRequestProperty("OF-CpuCount", "" + Config.getAvailableProcessors());
            var1.setRequestProperty("OF-OpenGL-Version", Config.openGlVersion);
            var1.setRequestProperty("OF-OpenGL-Vendor", Config.openGlVendor);
         }

         var1.setDoInput(true);
         var1.setDoOutput(false);
         var1.connect();
         InputStream var3 = var1.getInputStream();
         String var4 = Config.readInputStream(var3);
         var3.close();
         String[] var5 = Config.tokenize(var4, "\n\r");
         if (var5.length >= 1) {
            String var6 = var5[0].trim();
            Config.dbg("Version found: " + var6);
            if (Config.compareRelease(var6, "H8") <= 0) {
               if (var1 != null) {
                  var1.disconnect();
               }

               return;
            }

            Config.setNewRelease(var6);
            if (var1 != null) {
               var1.disconnect();
            }

            return;
         }

         if (var1 != null) {
            var1.disconnect();
         }
      } catch (Exception var8) {
         Config.dbg(var8.getClass().getName() + ": " + var8.getMessage());
      }

   }
}
