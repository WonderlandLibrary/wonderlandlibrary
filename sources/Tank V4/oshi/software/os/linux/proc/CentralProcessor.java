package oshi.software.os.linux.proc;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import oshi.hardware.Processor;
import oshi.util.FormatUtil;

public class CentralProcessor implements Processor {
   private String _vendor;
   private String _name;
   private String _identifier = null;
   private String _stepping;
   private String _model;
   private String _family;
   private boolean _cpu64;

   public String getVendor() {
      return this._vendor;
   }

   public void setVendor(String var1) {
      this._vendor = var1;
   }

   public String getName() {
      return this._name;
   }

   public void setName(String var1) {
      this._name = var1;
   }

   public String getIdentifier() {
      if (this._identifier == null) {
         StringBuilder var1 = new StringBuilder();
         if (this.getVendor().contentEquals("GenuineIntel")) {
            var1.append(this.isCpu64bit() ? "Intel64" : "x86");
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

   public boolean isCpu64bit() {
      return this._cpu64;
   }

   public void setCpu64(boolean var1) {
      this._cpu64 = var1;
   }

   public String getStepping() {
      return this._stepping;
   }

   public void setStepping(String var1) {
      this._stepping = var1;
   }

   public String getModel() {
      return this._model;
   }

   public void setModel(String var1) {
      this._model = var1;
   }

   public String getFamily() {
      return this._family;
   }

   public void setFamily(String var1) {
      this._family = var1;
   }

   public float getLoad() {
      Scanner var1 = null;

      try {
         var1 = new Scanner(new FileReader("/proc/stat"));
      } catch (FileNotFoundException var8) {
         System.err.println("Problem with: /proc/stat");
         System.err.println(var8.getMessage());
         return -1.0F;
      }

      var1.useDelimiter("\n");
      String[] var2 = var1.next().split(" ");
      ArrayList var3 = new ArrayList();
      String[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         if (var7.matches("-?\\d+(\\.\\d+)?")) {
            var3.add(Float.valueOf(var7));
         }
      }

      float var9 = ((Float)var3.get(0) + (Float)var3.get(2)) * 100.0F / ((Float)var3.get(0) + (Float)var3.get(2) + (Float)var3.get(3));
      return FormatUtil.round(var9, 2);
   }

   public String toString() {
      return this.getName();
   }
}
