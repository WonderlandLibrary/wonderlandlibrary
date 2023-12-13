package oshi.software.os.mac;

import java.util.ArrayList;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Memory;
import oshi.hardware.Processor;
import oshi.software.os.mac.local.CentralProcessor;
import oshi.software.os.mac.local.GlobalMemory;
import oshi.util.ExecutingCommand;

public class MacHardwareAbstractionLayer implements HardwareAbstractionLayer {
   private Processor[] _processors;
   private Memory _memory;

   public Processor[] getProcessors() {
      if (this._processors == null) {
         ArrayList var1 = new ArrayList();
         int var2 = new Integer(ExecutingCommand.getFirstAnswer("sysctl -n hw.logicalcpu"));

         for(int var3 = 0; var3 < var2; ++var3) {
            var1.add(new CentralProcessor());
         }

         this._processors = (Processor[])var1.toArray(new Processor[0]);
      }

      return this._processors;
   }

   public Memory getMemory() {
      if (this._memory == null) {
         this._memory = new GlobalMemory();
      }

      return this._memory;
   }
}
