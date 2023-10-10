package org.lwjgl.opengl;

public final class PixelFormat implements PixelFormatLWJGL {
   private int bpp;
   private int alpha;
   private int depth;
   private int stencil;
   private int samples;
   private int colorSamples;
   private int num_aux_buffers;
   private int accum_bpp;
   private int accum_alpha;
   private boolean stereo;
   private boolean floating_point;
   private boolean floating_point_packed;
   private boolean sRGB;

   public PixelFormat() {
      this(0, 8, 0);
   }

   public PixelFormat(int var1, int var2, int var3) {
      this(var1, var2, var3, 0);
   }

   public PixelFormat(int var1, int var2, int var3, int var4) {
      this(0, var1, var2, var3, var4);
   }

   public PixelFormat(int var1, int var2, int var3, int var4, int var5) {
      this(var1, var2, var3, var4, var5, 0, 0, 0, false);
   }

   public PixelFormat(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, false);
   }

   public PixelFormat(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9, boolean var10) {
      this.bpp = var1;
      this.alpha = var2;
      this.depth = var3;
      this.stencil = var4;
      this.samples = var5;
      this.num_aux_buffers = var6;
      this.accum_bpp = var7;
      this.accum_alpha = var8;
      this.stereo = var9;
      this.floating_point = var10;
      this.floating_point_packed = false;
      this.sRGB = false;
   }

   private PixelFormat(PixelFormat var1) {
      this.bpp = var1.bpp;
      this.alpha = var1.alpha;
      this.depth = var1.depth;
      this.stencil = var1.stencil;
      this.samples = var1.samples;
      this.colorSamples = var1.colorSamples;
      this.num_aux_buffers = var1.num_aux_buffers;
      this.accum_bpp = var1.accum_bpp;
      this.accum_alpha = var1.accum_alpha;
      this.stereo = var1.stereo;
      this.floating_point = var1.floating_point;
      this.floating_point_packed = var1.floating_point_packed;
      this.sRGB = var1.sRGB;
   }

   public int getBitsPerPixel() {
      return this.bpp;
   }

   public PixelFormat withBitsPerPixel(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Invalid number of bits per pixel specified: " + var1);
      } else {
         PixelFormat var2 = new PixelFormat(this);
         var2.bpp = var1;
         return var2;
      }
   }

   public int getAlphaBits() {
      return this.alpha;
   }

   public PixelFormat withAlphaBits(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Invalid number of alpha bits specified: " + var1);
      } else {
         PixelFormat var2 = new PixelFormat(this);
         var2.alpha = var1;
         return var2;
      }
   }

   public int getDepthBits() {
      return this.depth;
   }

   public PixelFormat withDepthBits(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Invalid number of depth bits specified: " + var1);
      } else {
         PixelFormat var2 = new PixelFormat(this);
         var2.depth = var1;
         return var2;
      }
   }

   public int getStencilBits() {
      return this.stencil;
   }

   public PixelFormat withStencilBits(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Invalid number of stencil bits specified: " + var1);
      } else {
         PixelFormat var2 = new PixelFormat(this);
         var2.stencil = var1;
         return var2;
      }
   }

   public int getSamples() {
      return this.samples;
   }

   public PixelFormat withSamples(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Invalid number of samples specified: " + var1);
      } else {
         PixelFormat var2 = new PixelFormat(this);
         var2.samples = var1;
         return var2;
      }
   }

   public PixelFormat withCoverageSamples(int var1) {
      return this.withCoverageSamples(var1, this.samples);
   }

   public PixelFormat withCoverageSamples(int var1, int var2) {
      if (var2 >= 0 && var1 >= 0 && (var2 != 0 || 0 >= var1) && var2 >= var1) {
         PixelFormat var3 = new PixelFormat(this);
         var3.samples = var2;
         var3.colorSamples = var1;
         return var3;
      } else {
         throw new IllegalArgumentException("Invalid number of coverage samples specified: " + var2 + " - " + var1);
      }
   }

   public int getAuxBuffers() {
      return this.num_aux_buffers;
   }

   public PixelFormat withAuxBuffers(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Invalid number of auxiliary buffers specified: " + var1);
      } else {
         PixelFormat var2 = new PixelFormat(this);
         var2.num_aux_buffers = var1;
         return var2;
      }
   }

   public int getAccumulationBitsPerPixel() {
      return this.accum_bpp;
   }

   public PixelFormat withAccumulationBitsPerPixel(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Invalid number of bits per pixel in the accumulation buffer specified: " + var1);
      } else {
         PixelFormat var2 = new PixelFormat(this);
         var2.accum_bpp = var1;
         return var2;
      }
   }

   public int getAccumulationAlpha() {
      return this.accum_alpha;
   }

   public PixelFormat withAccumulationAlpha(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Invalid number of alpha bits in the accumulation buffer specified: " + var1);
      } else {
         PixelFormat var2 = new PixelFormat(this);
         var2.accum_alpha = var1;
         return var2;
      }
   }

   public boolean isStereo() {
      return this.stereo;
   }

   public PixelFormat withStereo(boolean var1) {
      PixelFormat var2 = new PixelFormat(this);
      var2.stereo = var1;
      return var2;
   }

   public boolean isFloatingPoint() {
      return this.floating_point;
   }

   public PixelFormat withFloatingPoint(boolean var1) {
      PixelFormat var2 = new PixelFormat(this);
      var2.floating_point = var1;
      if (var1) {
         var2.floating_point_packed = false;
      }

      return var2;
   }

   public PixelFormat withFloatingPointPacked(boolean var1) {
      PixelFormat var2 = new PixelFormat(this);
      var2.floating_point_packed = var1;
      if (var1) {
         var2.floating_point = false;
      }

      return var2;
   }

   public boolean isSRGB() {
      return this.sRGB;
   }

   public PixelFormat withSRGB(boolean var1) {
      PixelFormat var2 = new PixelFormat(this);
      var2.sRGB = var1;
      return var2;
   }
}
