package com.sun.prism;

public enum PixelFormat {
   INT_ARGB_PRE(PixelFormat.DataType.INT, 1, true, false),
   BYTE_BGRA_PRE(PixelFormat.DataType.BYTE, 4, true, false),
   BYTE_RGB(PixelFormat.DataType.BYTE, 3, true, true),
   BYTE_GRAY(PixelFormat.DataType.BYTE, 1, true, true),
   BYTE_ALPHA(PixelFormat.DataType.BYTE, 1, false, false),
   MULTI_YCbCr_420(PixelFormat.DataType.BYTE, 1, false, true),
   BYTE_APPLE_422(PixelFormat.DataType.BYTE, 2, false, true),
   FLOAT_XYZW(PixelFormat.DataType.FLOAT, 4, false, true);

   public static final int YCBCR_PLANE_LUMA = 0;
   public static final int YCBCR_PLANE_CHROMARED = 1;
   public static final int YCBCR_PLANE_CHROMABLUE = 2;
   public static final int YCBCR_PLANE_ALPHA = 3;
   private DataType dataType;
   private int elemsPerPixelUnit;
   private boolean rgb;
   private boolean opaque;

   private PixelFormat(DataType var3, int var4, boolean var5, boolean var6) {
      this.dataType = var3;
      this.elemsPerPixelUnit = var4;
      this.rgb = var5;
      this.opaque = var6;
   }

   public DataType getDataType() {
      return this.dataType;
   }

   public int getBytesPerPixelUnit() {
      return this.elemsPerPixelUnit * this.dataType.getSizeInBytes();
   }

   public int getElemsPerPixelUnit() {
      return this.elemsPerPixelUnit;
   }

   public boolean isRGB() {
      return this.rgb;
   }

   public boolean isOpaque() {
      return this.opaque;
   }

   public static enum DataType {
      BYTE(1),
      INT(4),
      FLOAT(4);

      private int sizeInBytes;

      private DataType(int var3) {
         this.sizeInBytes = var3;
      }

      public int getSizeInBytes() {
         return this.sizeInBytes;
      }
   }
}
