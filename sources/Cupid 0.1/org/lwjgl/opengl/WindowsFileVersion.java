package org.lwjgl.opengl;

final class WindowsFileVersion {
  private final int product_version_ms;
  
  private final int product_version_ls;
  
  WindowsFileVersion(int product_version_ms, int product_version_ls) {
    this.product_version_ms = product_version_ms;
    this.product_version_ls = product_version_ls;
  }
  
  public String toString() {
    int f1 = this.product_version_ms >> 16 & 0xFFFF;
    int f2 = this.product_version_ms & 0xFFFF;
    int f3 = this.product_version_ls >> 16 & 0xFFFF;
    int f4 = this.product_version_ls & 0xFFFF;
    return f1 + "." + f2 + "." + f3 + "." + f4;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\WindowsFileVersion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */