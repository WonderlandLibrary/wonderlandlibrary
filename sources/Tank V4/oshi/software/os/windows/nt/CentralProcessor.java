package oshi.software.os.windows.nt;

import oshi.hardware.Processor;

public class CentralProcessor implements Processor {
   private String _vendor;
   private String _name;
   private String _identifier;

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
      return this._identifier;
   }

   public void setIdentifier(String var1) {
      this._identifier = var1;
   }

   public boolean isCpu64bit() {
      throw new UnsupportedOperationException();
   }

   public void setCpu64(boolean var1) {
      throw new UnsupportedOperationException();
   }

   public String getStepping() {
      throw new UnsupportedOperationException();
   }

   public void setStepping(String var1) {
      throw new UnsupportedOperationException();
   }

   public String getModel() {
      throw new UnsupportedOperationException();
   }

   public void setModel(String var1) {
      throw new UnsupportedOperationException();
   }

   public String getFamily() {
      throw new UnsupportedOperationException();
   }

   public void setFamily(String var1) {
      throw new UnsupportedOperationException();
   }

   public float getLoad() {
      throw new UnsupportedOperationException();
   }

   public String toString() {
      return this._name;
   }
}
