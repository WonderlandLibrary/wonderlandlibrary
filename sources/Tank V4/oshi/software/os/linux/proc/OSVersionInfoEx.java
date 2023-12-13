package oshi.software.os.linux.proc;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import oshi.software.os.OperatingSystemVersion;

public class OSVersionInfoEx implements OperatingSystemVersion {
   private String _version = null;
   private String _codeName = null;
   private String version = null;

   public OSVersionInfoEx() {
      Scanner var1 = null;

      try {
         var1 = new Scanner(new FileReader("/etc/os-release"));
      } catch (FileNotFoundException var4) {
         return;
      }

      var1.useDelimiter("\n");

      while(var1.hasNext()) {
         String[] var2 = var1.next().split("=");
         if (var2[0].equals("VERSION_ID")) {
            this.setVersion(var2[1].replaceAll("^\"|\"$", ""));
         }

         if (var2[0].equals("VERSION")) {
            var2[1] = var2[1].replaceAll("^\"|\"$", "");
            String[] var3 = var2[1].split("[()]");
            if (var3.length <= 1) {
               var3 = var2[1].split(", ");
            }

            if (var3.length > 1) {
               this.setCodeName(var3[1]);
            } else {
               this.setCodeName(var2[1]);
            }
         }
      }

      var1.close();
   }

   public String getCodeName() {
      return this._codeName;
   }

   public String getVersion() {
      return this._version;
   }

   public void setCodeName(String var1) {
      this._codeName = var1;
   }

   public void setVersion(String var1) {
      this._version = var1;
   }

   public String toString() {
      if (this.version == null) {
         this.version = this.getVersion() + " (" + this.getCodeName() + ")";
      }

      return this.version;
   }
}
