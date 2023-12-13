package optifine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class HttpUtils {
   public static final String SERVER_URL = "http://s.optifine.net";
   public static final String POST_URL = "http://optifine.net";

   public static byte[] get(String var0) throws IOException {
      HttpURLConnection var1 = null;
      URL var3 = new URL(var0);
      var1 = (HttpURLConnection)var3.openConnection(Minecraft.getMinecraft().getProxy());
      var1.setDoInput(true);
      var1.setDoOutput(false);
      var1.connect();
      if (var1.getResponseCode() / 100 != 2) {
         if (var1.getErrorStream() != null) {
            Config.readAll(var1.getErrorStream());
         }

         throw new IOException("HTTP response: " + var1.getResponseCode());
      } else {
         InputStream var4 = var1.getInputStream();
         byte[] var5 = new byte[var1.getContentLength()];
         int var6 = 0;

         do {
            int var7 = var4.read(var5, var6, var5.length - var6);
            if (var7 < 0) {
               throw new IOException("Input stream closed: " + var0);
            }

            var6 += var7;
         } while(var6 < var5.length);

         if (var1 != null) {
            var1.disconnect();
         }

         return var5;
      }
   }

   public static String post(String var0, Map var1, byte[] var2) throws IOException {
      HttpURLConnection var3 = null;
      URL var5 = new URL(var0);
      var3 = (HttpURLConnection)var5.openConnection(Minecraft.getMinecraft().getProxy());
      var3.setRequestMethod("POST");
      if (var1 != null) {
         Iterator var7 = var1.keySet().iterator();

         while(var7.hasNext()) {
            Object var6 = var7.next();
            String var8 = "" + var1.get(var6);
            var3.setRequestProperty((String)var6, var8);
         }
      }

      var3.setRequestProperty("Content-Type", "text/plain");
      var3.setRequestProperty("Content-Length", "" + var2.length);
      var3.setRequestProperty("Content-Language", "en-US");
      var3.setUseCaches(false);
      var3.setDoInput(true);
      var3.setDoOutput(true);
      OutputStream var13 = var3.getOutputStream();
      var13.write(var2);
      var13.flush();
      var13.close();
      InputStream var14 = var3.getInputStream();
      InputStreamReader var15 = new InputStreamReader(var14, "ASCII");
      BufferedReader var9 = new BufferedReader(var15);
      StringBuffer var10 = new StringBuffer();

      String var11;
      while((var11 = var9.readLine()) != null) {
         var10.append(var11);
         var10.append('\r');
      }

      var9.close();
      String var4 = var10.toString();
      if (var3 != null) {
         var3.disconnect();
      }

      return var4;
   }
}
