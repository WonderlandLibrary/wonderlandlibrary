package org.lwjgl.opengl;

public final class DisplayMode {
   private final int width;
   private final int height;
   private final int bpp;
   private final int freq;
   private final boolean fullscreen;

   public DisplayMode(int var1, int var2) {
      this(var1, var2, 0, 0, false);
   }

   DisplayMode(int var1, int var2, int var3, int var4) {
      this(var1, var2, var3, var4, true);
   }

   private DisplayMode(int var1, int var2, int var3, int var4, boolean var5) {
      this.width = var1;
      this.height = var2;
      this.bpp = var3;
      this.freq = var4;
      this.fullscreen = var5;
   }

   public boolean isFullscreenCapable() {
      return this.fullscreen;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getBitsPerPixel() {
      return this.bpp;
   }

   public int getFrequency() {
      return this.freq;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof DisplayMode) {
         DisplayMode var2 = (DisplayMode)var1;
         return var2.width == this.width && var2.height == this.height && var2.bpp == this.bpp && var2.freq == this.freq;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.width ^ this.height ^ this.freq ^ this.bpp;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(32);
      var1.append(this.width);
      var1.append(" x ");
      var1.append(this.height);
      var1.append(" x ");
      var1.append(this.bpp);
      var1.append(" @");
      var1.append(this.freq);
      var1.append("Hz");
      return var1.toString();
   }
}
