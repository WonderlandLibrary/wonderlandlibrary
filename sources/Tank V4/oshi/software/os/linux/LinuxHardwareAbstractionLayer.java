package oshi.software.os.linux;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Memory;
import oshi.hardware.Processor;
import oshi.software.os.linux.proc.CentralProcessor;
import oshi.software.os.linux.proc.GlobalMemory;

public class LinuxHardwareAbstractionLayer implements HardwareAbstractionLayer {
   private static final String SEPARATOR = "\\s+:\\s";
   private Processor[] _processors = null;
   private Memory _memory = null;

   public Memory getMemory() {
      if (this._memory == null) {
         this._memory = new GlobalMemory();
      }

      return this._memory;
   }

   public Processor[] getProcessors() {
      if (this._processors == null) {
         ArrayList var1 = new ArrayList();
         Scanner var2 = null;

         try {
            var2 = new Scanner(new FileReader("/proc/cpuinfo"));
         } catch (FileNotFoundException var11) {
            System.err.println("Problem with: /proc/cpuinfo");
            System.err.println(var11.getMessage());
            return null;
         }

         var2.useDelimiter("\n");
         CentralProcessor var3 = null;

         while(true) {
            while(var2.hasNext()) {
               String var4 = var2.next();
               if (var4.equals("")) {
                  if (var3 != null) {
                     var1.add(var3);
                  }

                  var3 = null;
               } else {
                  if (var3 == null) {
                     var3 = new CentralProcessor();
                  }

                  if (var4.startsWith("model name\t")) {
                     var3.setName(var4.split("\\s+:\\s")[1]);
                  } else if (!var4.startsWith("flags\t")) {
                     if (var4.startsWith("cpu family\t")) {
                        var3.setFamily(var4.split("\\s+:\\s")[1]);
                     } else if (var4.startsWith("model\t")) {
                        var3.setModel(var4.split("\\s+:\\s")[1]);
                     } else if (var4.startsWith("stepping\t")) {
                        var3.setStepping(var4.split("\\s+:\\s")[1]);
                     } else if (var4.startsWith("vendor_id")) {
                        var3.setVendor(var4.split("\\s+:\\s")[1]);
                     }
                  } else {
                     String[] var5 = var4.split("\\s+:\\s")[1].split(" ");
                     boolean var6 = false;
                     String[] var7 = var5;
                     int var8 = var5.length;

                     for(int var9 = 0; var9 < var8; ++var9) {
                        String var10 = var7[var9];
                        if (var10.equalsIgnoreCase("LM")) {
                           var6 = true;
                           break;
                        }
                     }

                     var3.setCpu64(var6);
                  }
               }
            }

            var2.close();
            if (var3 != null) {
               var1.add(var3);
            }

            this._processors = (Processor[])var1.toArray(new Processor[0]);
            break;
         }
      }

      return this._processors;
   }
}
