package oshi.software.os.linux;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystemVersion;
import oshi.software.os.linux.proc.OSVersionInfoEx;

public class LinuxOperatingSystem implements OperatingSystem {
   private OperatingSystemVersion _version = null;
   private String _family = null;

   public String getFamily() {
      if (this._family == null) {
         Scanner var1;
         try {
            var1 = new Scanner(new FileReader("/etc/os-release"));
         } catch (FileNotFoundException var3) {
            return "";
         }

         var1.useDelimiter("\n");

         while(var1.hasNext()) {
            String[] var2 = var1.next().split("=");
            if (var2[0].equals("NAME")) {
               this._family = var2[1].replaceAll("^\"|\"$", "");
               break;
            }
         }

         var1.close();
      }

      return this._family;
   }

   public String getManufacturer() {
      return "GNU/Linux";
   }

   public OperatingSystemVersion getVersion() {
      if (this._version == null) {
         this._version = new OSVersionInfoEx();
      }

      return this._version;
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
