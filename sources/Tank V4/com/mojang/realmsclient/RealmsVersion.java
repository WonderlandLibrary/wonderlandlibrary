package com.mojang.realmsclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RealmsVersion {
   private static String version;

   public static String getVersion() {
      if (version != null) {
         return version;
      } else {
         BufferedReader var0 = null;

         String var2;
         try {
            InputStream var1 = RealmsVersion.class.getResourceAsStream("/version");
            var0 = new BufferedReader(new InputStreamReader(var1));
            version = var0.readLine();
            var0.close();
            var2 = version;
         } catch (Exception var8) {
            if (var0 != null) {
               try {
                  var0.close();
               } catch (IOException var6) {
               }
            }

            return null;
         }

         if (var0 != null) {
            try {
               var0.close();
            } catch (IOException var7) {
            }
         }

         return var2;
      }
   }
}
