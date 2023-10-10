package oshi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ExecutingCommand {
   public static ArrayList runNative(String var0) {
      Process var1 = null;

      try {
         var1 = Runtime.getRuntime().exec(var0);
         var1.waitFor();
      } catch (IOException var6) {
         return null;
      } catch (InterruptedException var7) {
         return null;
      }

      BufferedReader var2 = new BufferedReader(new InputStreamReader(var1.getInputStream()));
      String var3 = "";
      ArrayList var4 = new ArrayList();

      try {
         while((var3 = var2.readLine()) != null) {
            var4.add(var3);
         }

         return var4;
      } catch (IOException var8) {
         return null;
      }
   }

   public static String getFirstAnswer(String var0) {
      ArrayList var1 = runNative(var0);
      return var1 != null ? (String)var1.get(0) : null;
   }
}
