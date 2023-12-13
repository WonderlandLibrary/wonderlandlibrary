package oshi.software.os.mac.local;

import java.util.Iterator;
import oshi.hardware.Memory;
import oshi.util.ExecutingCommand;

public class GlobalMemory implements Memory {
   private long totalMemory = 0L;

   public long getAvailable() {
      long var1 = 0L;
      Iterator var3 = ExecutingCommand.runNative("vm_stat").iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         String[] var5;
         if (var4.startsWith("Pages free:")) {
            var5 = var4.split(":\\s+");
            var1 += new Long(var5[1].replace(".", ""));
         } else if (var4.startsWith("Pages speculative:")) {
            var5 = var4.split(":\\s+");
            var1 += new Long(var5[1].replace(".", ""));
         }
      }

      var1 *= 4096L;
      return var1;
   }

   public long getTotal() {
      if (this.totalMemory == 0L) {
         this.totalMemory = new Long(ExecutingCommand.getFirstAnswer("sysctl -n hw.memsize"));
      }

      return this.totalMemory;
   }
}
