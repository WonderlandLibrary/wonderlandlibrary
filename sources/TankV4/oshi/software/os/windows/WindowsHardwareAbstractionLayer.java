package oshi.software.os.windows;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import java.util.ArrayList;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Memory;
import oshi.hardware.Processor;
import oshi.software.os.windows.nt.CentralProcessor;
import oshi.software.os.windows.nt.GlobalMemory;

public class WindowsHardwareAbstractionLayer implements HardwareAbstractionLayer {
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
         String var1 = "HARDWARE\\DESCRIPTION\\System\\CentralProcessor";
         ArrayList var2 = new ArrayList();
         String[] var3 = Advapi32Util.registryGetKeys(WinReg.HKEY_LOCAL_MACHINE, "HARDWARE\\DESCRIPTION\\System\\CentralProcessor");
         String[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            String var8 = "HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\" + var7;
            CentralProcessor var9 = new CentralProcessor();
            var9.setIdentifier(Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, var8, "Identifier"));
            var9.setName(Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, var8, "ProcessorNameString"));
            var9.setVendor(Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, var8, "VendorIdentifier"));
            var2.add(var9);
         }

         this._processors = (Processor[])var2.toArray(new Processor[0]);
      }

      return this._processors;
   }
}
