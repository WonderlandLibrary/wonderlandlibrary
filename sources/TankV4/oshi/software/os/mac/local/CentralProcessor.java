package oshi.software.os.mac.local;

import java.util.ArrayList;
import oshi.hardware.Processor;
import oshi.util.ExecutingCommand;

public class CentralProcessor implements Processor {
   private String _vendor;
   private String _name;
   private String _identifier = null;
   private String _stepping;
   private String _model;
   private String _family;
   private Boolean _cpu64;

   public String getVendor() {
      if (this._vendor == null) {
         this._vendor = ExecutingCommand.getFirstAnswer("sysctl -n machdep.cpu.vendor");
      }

      return this._vendor;
   }

   public void setVendor(String var1) {
      this._vendor = var1;
   }

   public String getName() {
      if (this._name == null) {
         this._name = ExecutingCommand.getFirstAnswer("sysctl -n machdep.cpu.brand_string");
      }

      return this._name;
   }

   public void setName(String var1) {
      this._name = var1;
   }

   public String getIdentifier() {
      if (this._identifier == null) {
         StringBuilder var1 = new StringBuilder();
         if (this.getVendor().contentEquals("GenuineIntel")) {
            var1.append(this == null ? "Intel64" : "x86");
         } else {
            var1.append(this.getVendor());
         }

         var1.append(" Family ");
         var1.append(this.getFamily());
         var1.append(" Model ");
         var1.append(this.getModel());
         var1.append(" Stepping ");
         var1.append(this.getStepping());
         this._identifier = var1.toString();
      }

      return this._identifier;
   }

   public void setIdentifier(String var1) {
      this._identifier = var1;
   }

   public void setCpu64(boolean var1) {
      this._cpu64 = var1;
   }

   public String getStepping() {
      if (this._stepping == null) {
         this._stepping = ExecutingCommand.getFirstAnswer("sysctl -n machdep.cpu.stepping");
      }

      return this._stepping;
   }

   public void setStepping(String var1) {
      this._stepping = var1;
   }

   public String getModel() {
      if (this._model == null) {
         this._model = ExecutingCommand.getFirstAnswer("sysctl -n machdep.cpu.model");
      }

      return this._model;
   }

   public void setModel(String var1) {
      this._model = var1;
   }

   public String getFamily() {
      if (this._family == null) {
         this._family = ExecutingCommand.getFirstAnswer("sysctl -n machdep.cpu.family");
      }

      return this._family;
   }

   public void setFamily(String var1) {
      this._family = var1;
   }

   public float getLoad() {
      ArrayList var1 = ExecutingCommand.runNative("top -l 1 -R -F -n1");
      String[] var2 = ((String)var1.get(3)).split(" ");
      return 100.0F - Float.valueOf(var2[6].replace("%", ""));
   }

   public String toString() {
      return this.getName();
   }
}
