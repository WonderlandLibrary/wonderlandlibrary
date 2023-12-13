package my.NewSnake.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class FileUtils {
   public static File getConfigFile(String var0) {
      File var1 = new File(getConfigDir(), String.format("%s.txt", var0));
      if (!var1.exists()) {
         try {
            var1.createNewFile();
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      return var1;
   }

   public static File getConfigDir() {
      File var0 = new File(ClientUtils.mc().mcDataDir, "Save-DataTankV4");
      if (!var0.exists()) {
         var0.mkdir();
      }

      return var0;
   }

   public static List read(File var0) {
      ArrayList var1 = new ArrayList();

      try {
         BufferedReader var2 = new BufferedReader(new InputStreamReader(new FileInputStream(var0), "UTF8"));

         String var3;
         while((var3 = var2.readLine()) != null) {
            var1.add(var3);
         }

         var2.close();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return var1;
   }

   public static void write(File var0, List var1, boolean var2) {
      try {
         BufferedWriter var3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var0), "UTF-8"));
         Iterator var5 = var1.iterator();

         while(var5.hasNext()) {
            String var4 = (String)var5.next();
            var3.write(String.valueOf(var4) + System.getProperty("line.separator"));
         }

         var3.close();
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
