package org.lwjgl.opengl;

final class WindowsFileVersion {
   private final int product_version_ms;
   private final int product_version_ls;

   WindowsFileVersion(int var1, int var2) {
      this.product_version_ms = var1;
      this.product_version_ls = var2;
   }

   public String toString() {
      int var1 = this.product_version_ms >> 16 & '\uffff';
      int var2 = this.product_version_ms & '\uffff';
      int var3 = this.product_version_ls >> 16 & '\uffff';
      int var4 = this.product_version_ls & '\uffff';
      return var1 + "." + var2 + "." + var3 + "." + var4;
   }
}
