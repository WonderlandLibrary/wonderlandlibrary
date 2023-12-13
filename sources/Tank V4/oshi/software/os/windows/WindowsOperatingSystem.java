package oshi.software.os.windows;

import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystemVersion;
import oshi.software.os.windows.nt.OSVersionInfoEx;

public class WindowsOperatingSystem implements OperatingSystem {
   private OperatingSystemVersion _version = null;

   public OperatingSystemVersion getVersion() {
      if (this._version == null) {
         this._version = new OSVersionInfoEx();
      }

      return this._version;
   }

   public String getFamily() {
      return "Windows";
   }

   public String getManufacturer() {
      return "Microsoft";
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.getManufacturer());
      var1.append(" ");
      var1.append(this.getFamily());
      var1.append(" ");
      var1.append(this.getVersion().toString());
      return var1.toString();
   }
}
