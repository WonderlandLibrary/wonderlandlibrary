package team.massacre.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FilesReader {
   public static void search(String pattern, File folder, List<String> result) {
      File[] var3 = folder.listFiles();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         File f = var3[var5];
         if (f.isDirectory()) {
            search(pattern, f, result);
         }

         if (f.isFile() && f.getName().matches(pattern)) {
            result.add(f.getName().replace(".cfg", ""));
         }
      }

   }

   public static String readFile(File file) {
      BufferedReader br = null;

      try {
         br = new BufferedReader(new FileReader(file));
      } catch (FileNotFoundException var5) {
         var5.printStackTrace();
      }

      String st = "";

      try {
         if ((st = br.readLine()) == null) {
            return "";
         }
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      return st;
   }
}
