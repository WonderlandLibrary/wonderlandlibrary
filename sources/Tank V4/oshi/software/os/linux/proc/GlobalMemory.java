package oshi.software.os.linux.proc;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import oshi.hardware.Memory;

public class GlobalMemory implements Memory {
   private long totalMemory = 0L;

   public long getAvailable() {
      long var1 = 0L;
      Scanner var3 = null;

      try {
         var3 = new Scanner(new FileReader("/proc/meminfo"));
      } catch (FileNotFoundException var6) {
         return var1;
      }

      var3.useDelimiter("\n");

      while(var3.hasNext()) {
         String var4 = var3.next();
         if (var4.startsWith("MemFree:") || var4.startsWith("MemAvailable:")) {
            String[] var5 = var4.split("\\s+");
            var1 = new Long(var5[1]);
            if (var5[2].equals("kB")) {
               var1 *= 1024L;
            }

            if (var5[0].equals("MemAvailable:")) {
               break;
            }
         }
      }

      var3.close();
      return var1;
   }

   public long getTotal() {
      if (this.totalMemory == 0L) {
         Scanner var1 = null;

         try {
            var1 = new Scanner(new FileReader("/proc/meminfo"));
         } catch (FileNotFoundException var4) {
            this.totalMemory = 0L;
            return this.totalMemory;
         }

         var1.useDelimiter("\n");

         while(var1.hasNext()) {
            String var2 = var1.next();
            if (var2.startsWith("MemTotal:")) {
               String[] var3 = var2.split("\\s+");
               this.totalMemory = new Long(var3[1]);
               if (var3[2].equals("kB")) {
                  this.totalMemory *= 1024L;
               }
               break;
            }
         }

         var1.close();
      }

      return this.totalMemory;
   }
}
