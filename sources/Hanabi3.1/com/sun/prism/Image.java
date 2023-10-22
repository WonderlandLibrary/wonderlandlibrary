package com.sun.prism;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.ByteToIntPixelConverter;
import com.sun.javafx.image.IntPixelGetter;
import com.sun.javafx.image.IntPixelSetter;
import com.sun.javafx.image.IntToBytePixelConverter;
import com.sun.javafx.image.IntToIntPixelConverter;
import com.sun.javafx.image.PixelConverter;
import com.sun.javafx.image.PixelGetter;
import com.sun.javafx.image.PixelSetter;
import com.sun.javafx.image.PixelUtils;
import com.sun.javafx.image.impl.ByteBgra;
import com.sun.javafx.image.impl.ByteBgraPre;
import com.sun.javafx.image.impl.ByteGray;
import com.sun.javafx.image.impl.ByteGrayAlpha;
import com.sun.javafx.image.impl.ByteGrayAlphaPre;
import com.sun.javafx.image.impl.ByteRgb;
import com.sun.javafx.image.impl.ByteRgba;
import com.sun.javafx.tk.PlatformImage;
import com.sun.prism.impl.BufferUtil;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

public class Image implements PlatformImage {
   static final WritablePixelFormat FX_ByteBgraPre_FORMAT = javafx.scene.image.PixelFormat.getByteBgraPreInstance();
   static final WritablePixelFormat FX_IntArgbPre_FORMAT = javafx.scene.image.PixelFormat.getIntArgbPreInstance();
   static final javafx.scene.image.PixelFormat FX_ByteRgb_FORMAT = javafx.scene.image.PixelFormat.getByteRgbInstance();
   private final Buffer pixelBuffer;
   private final int minX;
   private final int minY;
   private final int width;
   private final int height;
   private final int scanlineStride;
   private final PixelFormat pixelFormat;
   private final float pixelScale;
   int[] serial;
   private Accessor pixelaccessor;
   static javafx.scene.image.PixelFormat FX_ByteGray_FORMAT;

   public static Image fromIntArgbPreData(int[] var0, int var1, int var2) {
      return new Image(PixelFormat.INT_ARGB_PRE, var0, var1, var2);
   }

   public static Image fromIntArgbPreData(IntBuffer var0, int var1, int var2) {
      return new Image(PixelFormat.INT_ARGB_PRE, var0, var1, var2);
   }

   public static Image fromIntArgbPreData(IntBuffer var0, int var1, int var2, int var3) {
      return new Image(PixelFormat.INT_ARGB_PRE, var0, var1, var2, 0, 0, var3);
   }

   public static Image fromIntArgbPreData(IntBuffer var0, int var1, int var2, int var3, float var4) {
      return new Image(PixelFormat.INT_ARGB_PRE, var0, var1, var2, 0, 0, var3, var4);
   }

   public static Image fromByteBgraPreData(byte[] var0, int var1, int var2) {
      return new Image(PixelFormat.BYTE_BGRA_PRE, var0, var1, var2);
   }

   public static Image fromByteBgraPreData(byte[] var0, int var1, int var2, float var3) {
      return new Image(PixelFormat.BYTE_BGRA_PRE, ByteBuffer.wrap(var0), var1, var2, 0, 0, 0, var3);
   }

   public static Image fromByteBgraPreData(ByteBuffer var0, int var1, int var2) {
      return new Image(PixelFormat.BYTE_BGRA_PRE, var0, var1, var2);
   }

   public static Image fromByteBgraPreData(ByteBuffer var0, int var1, int var2, int var3) {
      return new Image(PixelFormat.BYTE_BGRA_PRE, var0, var1, var2, 0, 0, var3);
   }

   public static Image fromByteBgraPreData(ByteBuffer var0, int var1, int var2, int var3, float var4) {
      return new Image(PixelFormat.BYTE_BGRA_PRE, var0, var1, var2, 0, 0, var3, var4);
   }

   public static Image fromByteRgbData(byte[] var0, int var1, int var2) {
      return new Image(PixelFormat.BYTE_RGB, var0, var1, var2);
   }

   public static Image fromByteRgbData(ByteBuffer var0, int var1, int var2) {
      return new Image(PixelFormat.BYTE_RGB, var0, var1, var2);
   }

   public static Image fromByteRgbData(ByteBuffer var0, int var1, int var2, int var3) {
      return new Image(PixelFormat.BYTE_RGB, var0, var1, var2, 0, 0, var3);
   }

   public static Image fromByteRgbData(ByteBuffer var0, int var1, int var2, int var3, float var4) {
      return new Image(PixelFormat.BYTE_RGB, var0, var1, var2, 0, 0, var3, var4);
   }

   public static Image fromByteGrayData(byte[] var0, int var1, int var2) {
      return new Image(PixelFormat.BYTE_GRAY, var0, var1, var2);
   }

   public static Image fromByteGrayData(ByteBuffer var0, int var1, int var2) {
      return new Image(PixelFormat.BYTE_GRAY, var0, var1, var2);
   }

   public static Image fromByteGrayData(ByteBuffer var0, int var1, int var2, int var3) {
      return new Image(PixelFormat.BYTE_GRAY, var0, var1, var2, 0, 0, var3);
   }

   public static Image fromByteGrayData(ByteBuffer var0, int var1, int var2, int var3, float var4) {
      return new Image(PixelFormat.BYTE_GRAY, var0, var1, var2, 0, 0, var3, var4);
   }

   public static Image fromByteAlphaData(byte[] var0, int var1, int var2) {
      return new Image(PixelFormat.BYTE_ALPHA, var0, var1, var2);
   }

   public static Image fromByteAlphaData(ByteBuffer var0, int var1, int var2) {
      return new Image(PixelFormat.BYTE_ALPHA, var0, var1, var2);
   }

   public static Image fromByteAlphaData(ByteBuffer var0, int var1, int var2, int var3) {
      return new Image(PixelFormat.BYTE_ALPHA, var0, var1, var2, 0, 0, var3);
   }

   public static Image fromByteApple422Data(byte[] var0, int var1, int var2) {
      return new Image(PixelFormat.BYTE_APPLE_422, var0, var1, var2);
   }

   public static Image fromByteApple422Data(ByteBuffer var0, int var1, int var2) {
      return new Image(PixelFormat.BYTE_APPLE_422, var0, var1, var2);
   }

   public static Image fromByteApple422Data(ByteBuffer var0, int var1, int var2, int var3) {
      return new Image(PixelFormat.BYTE_APPLE_422, var0, var1, var2, 0, 0, var3);
   }

   public static Image fromFloatMapData(FloatBuffer var0, int var1, int var2) {
      return new Image(PixelFormat.FLOAT_XYZW, var0, var1, var2);
   }

   public static Image convertImageFrame(ImageFrame var0) {
      ByteBuffer var1 = (ByteBuffer)var0.getImageData();
      ImageStorage.ImageType var2 = var0.getImageType();
      int var3 = var0.getWidth();
      int var4 = var0.getHeight();
      int var5 = var0.getStride();
      float var6 = var0.getPixelScale();
      switch (var2) {
         case GRAY:
            return fromByteGrayData(var1, var3, var4, var5, var6);
         case RGB:
            return fromByteRgbData(var1, var3, var4, var5, var6);
         case RGBA:
            ByteBgra.ToByteBgraPreConverter().convert((Buffer)var1, 0, var5, (Buffer)var1, 0, var5, var3, var4);
         case RGBA_PRE:
            ByteRgba.ToByteBgraConverter().convert((Buffer)var1, 0, var5, (Buffer)var1, 0, var5, var3, var4);
            return fromByteBgraPreData(var1, var3, var4, var5, var6);
         case GRAY_ALPHA:
            ByteGrayAlpha.ToByteGrayAlphaPreConverter().convert((Buffer)var1, 0, var5, (Buffer)var1, 0, var5, var3, var4);
         case GRAY_ALPHA_PRE:
            if (var5 != var3 * 2) {
               throw new AssertionError("Bad stride for GRAY_ALPHA");
            }

            byte[] var7 = new byte[var3 * var4 * 4];
            ByteGrayAlphaPre.ToByteBgraPreConverter().convert((ByteBuffer)var1, 0, var5, (byte[])var7, 0, var3 * 4, var3, var4);
            return fromByteBgraPreData(var7, var3, var4, var6);
         default:
            throw new RuntimeException("Unknown image type: " + var2);
      }
   }

   private Image(PixelFormat var1, int[] var2, int var3, int var4) {
      this(var1, IntBuffer.wrap(var2), var3, var4, 0, 0, 0, 1.0F);
   }

   private Image(PixelFormat var1, byte[] var2, int var3, int var4) {
      this(var1, ByteBuffer.wrap(var2), var3, var4, 0, 0, 0, 1.0F);
   }

   private Image(PixelFormat var1, Buffer var2, int var3, int var4) {
      this(var1, var2, var3, var4, 0, 0, 0, 1.0F);
   }

   private Image(PixelFormat var1, Buffer var2, int var3, int var4, int var5, int var6, int var7) {
      this(var1, var2, var3, var4, var5, var6, var7, 1.0F);
   }

   private Image(PixelFormat var1, Buffer var2, int var3, int var4, int var5, int var6, int var7, float var8) {
      this.serial = new int[1];
      if (var1 == PixelFormat.MULTI_YCbCr_420) {
         throw new IllegalArgumentException("Format not supported " + var1.name());
      } else {
         if (var7 == 0) {
            var7 = var3 * var1.getBytesPerPixelUnit();
         }

         if (var2 == null) {
            throw new IllegalArgumentException("Pixel buffer must be non-null");
         } else if (var3 > 0 && var4 > 0) {
            if (var5 >= 0 && var6 >= 0) {
               if ((var5 + var3) * var1.getBytesPerPixelUnit() > var7) {
                  throw new IllegalArgumentException("Image scanlineStride is too small");
               } else if (var7 % var1.getBytesPerPixelUnit() != 0) {
                  throw new IllegalArgumentException("Image scanlineStride must be a multiple of the pixel stride");
               } else {
                  this.pixelFormat = var1;
                  this.pixelBuffer = var2;
                  this.width = var3;
                  this.height = var4;
                  this.minX = var5;
                  this.minY = var6;
                  this.scanlineStride = var7;
                  this.pixelScale = var8;
               }
            } else {
               throw new IllegalArgumentException("Image minX and minY must be >= 0");
            }
         } else {
            throw new IllegalArgumentException("Image dimensions must be > 0");
         }
      }
   }

   public PixelFormat getPixelFormat() {
      return this.pixelFormat;
   }

   public PixelFormat.DataType getDataType() {
      return this.pixelFormat.getDataType();
   }

   public int getBytesPerPixelUnit() {
      return this.pixelFormat.getBytesPerPixelUnit();
   }

   public Buffer getPixelBuffer() {
      return this.pixelBuffer;
   }

   public int getMinX() {
      return this.minX;
   }

   public int getMinY() {
      return this.minY;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public int getScanlineStride() {
      return this.scanlineStride;
   }

   public float getPixelScale() {
      return this.pixelScale;
   }

   public int getRowLength() {
      return this.scanlineStride / this.pixelFormat.getBytesPerPixelUnit();
   }

   public boolean isTightlyPacked() {
      return this.minX == 0 && this.minY == 0 && this.width == this.getRowLength();
   }

   public Image createSubImage(int var1, int var2, int var3, int var4) {
      if (var3 > 0 && var4 > 0) {
         if (var1 >= 0 && var2 >= 0) {
            if (var1 + var3 > this.width) {
               throw new IllegalArgumentException("Subimage minX+width must be <= width of parent image");
            } else if (var2 + var4 > this.height) {
               throw new IllegalArgumentException("Subimage minY+height must be <= height of parent image");
            } else {
               Image var5 = new Image(this.pixelFormat, this.pixelBuffer, var3, var4, this.minX + var1, this.minY + var2, this.scanlineStride);
               var5.serial = this.serial;
               return var5;
            }
         } else {
            throw new IllegalArgumentException("Subimage minX and minY must be >= 0");
         }
      } else {
         throw new IllegalArgumentException("Subimage dimensions must be > 0");
      }
   }

   public Image createPackedCopy() {
      int var1 = this.width * this.pixelFormat.getBytesPerPixelUnit();
      Buffer var2 = createPackedBuffer(this.pixelBuffer, this.pixelFormat, this.minX, this.minY, this.width, this.height, this.scanlineStride);
      return new Image(this.pixelFormat, var2, this.width, this.height, 0, 0, var1);
   }

   public Image createPackedCopyIfNeeded() {
      int var1 = this.width * this.pixelFormat.getBytesPerPixelUnit();
      return var1 == this.scanlineStride && this.minX == 0 && this.minY == 0 ? this : this.createPackedCopy();
   }

   public static Buffer createPackedBuffer(Buffer var0, PixelFormat var1, int var2, int var3, int var4, int var5, int var6) {
      if (var6 % var1.getBytesPerPixelUnit() != 0) {
         throw new IllegalArgumentException("Image scanlineStride must be a multiple of the pixel stride");
      } else if (var1 == PixelFormat.MULTI_YCbCr_420) {
         throw new IllegalArgumentException("Format unsupported " + var1);
      } else {
         int var7 = var1.getElemsPerPixelUnit();
         int var8 = var6 / var1.getBytesPerPixelUnit();
         int var9 = var8 * var7;
         int var10 = var4 * var7;
         int var11 = var10 * var5;
         int var12 = var2 * var7 + var3 * var9;
         int var13 = 0;
         Object var14;
         switch (var1.getDataType()) {
            case BYTE:
               ByteBuffer var15 = (ByteBuffer)var0;
               ByteBuffer var16 = BufferUtil.newByteBuffer(var11);

               for(int var22 = 0; var22 < var5; ++var22) {
                  var15.limit(var12 + var10);
                  var15.position(var12);
                  var16.limit(var13 + var10);
                  var16.position(var13);
                  var16.put(var15);
                  var12 += var9;
                  var13 += var10;
               }

               var14 = var16;
               break;
            case INT:
               IntBuffer var17 = (IntBuffer)var0;
               IntBuffer var18 = BufferUtil.newIntBuffer(var11);

               for(int var23 = 0; var23 < var5; ++var23) {
                  var17.limit(var12 + var10);
                  var17.position(var12);
                  var18.limit(var13 + var10);
                  var18.position(var13);
                  var18.put(var17);
                  var12 += var9;
                  var13 += var10;
               }

               var14 = var18;
               break;
            case FLOAT:
               FloatBuffer var19 = (FloatBuffer)var0;
               FloatBuffer var20 = BufferUtil.newFloatBuffer(var11);

               for(int var21 = 0; var21 < var5; ++var21) {
                  var19.limit(var12 + var10);
                  var19.position(var12);
                  var20.limit(var13 + var10);
                  var20.position(var13);
                  var20.put(var19);
                  var12 += var9;
                  var13 += var10;
               }

               var14 = var20;
               break;
            default:
               throw new InternalError("Unknown data type");
         }

         var0.limit(var0.capacity());
         var0.rewind();
         ((Buffer)var14).limit(((Buffer)var14).capacity());
         ((Buffer)var14).rewind();
         return (Buffer)var14;
      }
   }

   public Image iconify(ByteBuffer var1, int var2, int var3) {
      if (this.pixelFormat == PixelFormat.MULTI_YCbCr_420) {
         throw new IllegalArgumentException("Format not supported " + this.pixelFormat);
      } else {
         int var4 = this.getBytesPerPixelUnit();
         int var5 = var2 * var4;
         ByteToIntPixelConverter var6;
         if (var4 == 1) {
            var6 = ByteGray.ToIntArgbPreConverter();
         } else if (this.pixelFormat == PixelFormat.BYTE_BGRA_PRE) {
            var6 = ByteBgraPre.ToIntArgbPreConverter();
         } else {
            var6 = ByteRgb.ToIntArgbPreConverter();
         }

         int[] var7 = new int[var2 * var3];
         var6.convert((ByteBuffer)var1, 0, var5, (int[])var7, 0, var2, var2, var3);
         return new Image(PixelFormat.INT_ARGB_PRE, var7, var2, var3);
      }
   }

   public String toString() {
      return super.toString() + " [format=" + this.pixelFormat + " width=" + this.width + " height=" + this.height + " scanlineStride=" + this.scanlineStride + " minX=" + this.minX + " minY=" + this.minY + " pixelBuffer=" + this.pixelBuffer + " bpp=" + this.getBytesPerPixelUnit() + "]";
   }

   public int getSerial() {
      return this.serial[0];
   }

   public Image promoteByteRgbToByteBgra() {
      ByteBuffer var1 = (ByteBuffer)this.pixelBuffer;
      ByteBuffer var2 = ByteBuffer.allocate(this.width * this.height * 4);
      int var3 = this.minY * this.scanlineStride + this.minX * 3;
      ByteRgb.ToByteBgraPreConverter().convert((Buffer)var1, var3, this.scanlineStride, (Buffer)var2, 0, this.width * 4, this.width, this.height);
      return new Image(PixelFormat.BYTE_BGRA_PRE, var2, this.width, this.height, 0, 0, this.width * 4, this.getPixelScale());
   }

   private Accessor getPixelAccessor() {
      if (this.pixelaccessor == null) {
         switch (this.getPixelFormat()) {
            case BYTE_ALPHA:
            case BYTE_APPLE_422:
            case FLOAT_XYZW:
            case MULTI_YCbCr_420:
            default:
               this.pixelaccessor = new UnsupportedAccess();
               break;
            case BYTE_GRAY:
               this.pixelaccessor = new ByteAccess(getGrayFXPixelFormat(), ByteGray.getter, (PixelSetter)null, (ByteBuffer)this.pixelBuffer, 1);
               break;
            case BYTE_RGB:
               this.pixelaccessor = new ByteRgbAccess((ByteBuffer)this.pixelBuffer);
               break;
            case BYTE_BGRA_PRE:
               this.pixelaccessor = new ByteAccess(FX_ByteBgraPre_FORMAT, (ByteBuffer)this.pixelBuffer, 4);
               break;
            case INT_ARGB_PRE:
               this.pixelaccessor = new IntAccess(FX_IntArgbPre_FORMAT, (IntBuffer)this.pixelBuffer);
         }
      }

      if (this.pixelaccessor != null && this.pixelScale != 1.0F) {
         this.pixelaccessor = new ScaledAccessor(this.pixelaccessor, this.pixelScale);
      }

      return this.pixelaccessor;
   }

   public javafx.scene.image.PixelFormat getPlatformPixelFormat() {
      return this.getPixelAccessor().getPlatformPixelFormat();
   }

   public boolean isWritable() {
      return this.getPixelAccessor().isWritable();
   }

   public PlatformImage promoteToWritableImage() {
      return this.getPixelAccessor().promoteToWritableImage();
   }

   public int getArgb(int var1, int var2) {
      return this.getPixelAccessor().getArgb(var1, var2);
   }

   public void setArgb(int var1, int var2, int var3) {
      this.getPixelAccessor().setArgb(var1, var2, var3);
      int var10002 = this.serial[0]++;
   }

   public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, Buffer var6, int var7) {
      this.getPixelAccessor().getPixels(var1, var2, var3, var4, var5, var6, var7);
   }

   public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, byte[] var6, int var7, int var8) {
      this.getPixelAccessor().getPixels(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, int[] var6, int var7, int var8) {
      this.getPixelAccessor().getPixels(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat var5, Buffer var6, int var7) {
      this.getPixelAccessor().setPixels(var1, var2, var3, var4, var5, var6, var7);
      int var10002 = this.serial[0]++;
   }

   public void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat var5, byte[] var6, int var7, int var8) {
      this.getPixelAccessor().setPixels(var1, var2, var3, var4, var5, var6, var7, var8);
      int var10002 = this.serial[0]++;
   }

   public void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat var5, int[] var6, int var7, int var8) {
      this.getPixelAccessor().setPixels(var1, var2, var3, var4, var5, var6, var7, var8);
      int var10002 = this.serial[0]++;
   }

   public void setPixels(int var1, int var2, int var3, int var4, PixelReader var5, int var6, int var7) {
      this.getPixelAccessor().setPixels(var1, var2, var3, var4, var5, var6, var7);
      int var10002 = this.serial[0]++;
   }

   public boolean isOpaque() {
      return this.pixelFormat.isOpaque();
   }

   static PixelSetter getSetterIfWritable(javafx.scene.image.PixelFormat var0) {
      return var0 instanceof WritablePixelFormat ? PixelUtils.getSetter((WritablePixelFormat)var0) : null;
   }

   static javafx.scene.image.PixelFormat getGrayFXPixelFormat() {
      if (FX_ByteGray_FORMAT == null) {
         int[] var0 = new int[256];
         int var1 = -16777216;

         for(int var2 = 0; var2 < 256; ++var2) {
            var0[var2] = var1;
            var1 += 65793;
         }

         FX_ByteGray_FORMAT = javafx.scene.image.PixelFormat.createByteIndexedPremultipliedInstance(var0);
      }

      return FX_ByteGray_FORMAT;
   }

   class ByteRgbAccess extends ByteAccess {
      public ByteRgbAccess(ByteBuffer var2) {
         super(Image.FX_ByteRgb_FORMAT, var2, 3);
      }

      public PlatformImage promoteToWritableImage() {
         return Image.this.promoteByteRgbToByteBgra();
      }
   }

   class UnsupportedAccess extends ByteAccess {
      private UnsupportedAccess() {
         super((javafx.scene.image.PixelFormat)null, (PixelGetter)null, (PixelSetter)null, (ByteBuffer)null, 0);
      }

      // $FF: synthetic method
      UnsupportedAccess(Object var2) {
         this();
      }
   }

   class IntAccess extends BaseAccessor {
      IntAccess(javafx.scene.image.PixelFormat var2, IntBuffer var3) {
         super(var2, var3, 1);
      }

      public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, byte[] var6, int var7, int var8) {
         BytePixelSetter var9 = PixelUtils.getByteSetter(var5);
         IntToBytePixelConverter var10 = PixelUtils.getI2BConverter(this.getGetter(), var9);
         var10.convert((IntBuffer)this.getBuffer(), this.getIndex(var1, var2), this.scanlineElems, var6, var7, var8, var3, var4);
      }

      public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, int[] var6, int var7, int var8) {
         IntPixelSetter var9 = PixelUtils.getIntSetter(var5);
         IntToIntPixelConverter var10 = PixelUtils.getI2IConverter(this.getGetter(), var9);
         var10.convert((IntBuffer)this.getBuffer(), this.getIndex(var1, var2), this.scanlineElems, var6, var7, var8, var3, var4);
      }

      public void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat var5, byte[] var6, int var7, int var8) {
         BytePixelGetter var9 = PixelUtils.getByteGetter(var5);
         ByteToIntPixelConverter var10 = PixelUtils.getB2IConverter(var9, this.getSetter());
         var10.convert(var6, var7, var8, (IntBuffer)this.getBuffer(), this.getIndex(var1, var2), this.scanlineElems, var3, var4);
      }

      public void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat var5, int[] var6, int var7, int var8) {
         IntPixelGetter var9 = PixelUtils.getIntGetter(var5);
         IntToIntPixelConverter var10 = PixelUtils.getI2IConverter(var9, this.getSetter());
         var10.convert(var6, var7, var8, (IntBuffer)this.getBuffer(), this.getIndex(var1, var2), this.scanlineElems, var3, var4);
      }

      public void setPixels(int var1, int var2, int var3, int var4, PixelReader var5, int var6, int var7) {
         IntBuffer var8 = ((IntBuffer)this.theBuffer).duplicate();
         var8.position(var8.position() + this.getIndex(var1, var2));
         var5.getPixels(var6, var7, var3, var4, (WritablePixelFormat)this.theFormat, var8, this.scanlineElems);
      }
   }

   class ByteAccess extends BaseAccessor {
      ByteAccess(javafx.scene.image.PixelFormat var2, PixelGetter var3, PixelSetter var4, ByteBuffer var5, int var6) {
         super(var2, var3, var4, var5, var6);
      }

      ByteAccess(javafx.scene.image.PixelFormat var2, ByteBuffer var3, int var4) {
         super(var2, var3, var4);
      }

      public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, byte[] var6, int var7, int var8) {
         BytePixelSetter var9 = PixelUtils.getByteSetter(var5);
         ByteToBytePixelConverter var10 = PixelUtils.getB2BConverter(this.getGetter(), var9);
         var10.convert((ByteBuffer)this.getBuffer(), this.getIndex(var1, var2), this.scanlineElems, var6, var7, var8, var3, var4);
      }

      public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, int[] var6, int var7, int var8) {
         IntPixelSetter var9 = PixelUtils.getIntSetter(var5);
         ByteToIntPixelConverter var10 = PixelUtils.getB2IConverter(this.getGetter(), var9);
         var10.convert((ByteBuffer)this.getBuffer(), this.getIndex(var1, var2), this.scanlineElems, var6, var7, var8, var3, var4);
      }

      public void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat var5, byte[] var6, int var7, int var8) {
         BytePixelGetter var9 = PixelUtils.getByteGetter(var5);
         ByteToBytePixelConverter var10 = PixelUtils.getB2BConverter(var9, this.getSetter());
         var10.convert(var6, var7, var8, (ByteBuffer)this.getBuffer(), this.getIndex(var1, var2), this.scanlineElems, var3, var4);
      }

      public void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat var5, int[] var6, int var7, int var8) {
         IntPixelGetter var9 = PixelUtils.getIntGetter(var5);
         IntToBytePixelConverter var10 = PixelUtils.getI2BConverter(var9, this.getSetter());
         var10.convert(var6, var7, var8, (ByteBuffer)this.getBuffer(), this.getIndex(var1, var2), this.scanlineElems, var3, var4);
      }

      public void setPixels(int var1, int var2, int var3, int var4, PixelReader var5, int var6, int var7) {
         ByteBuffer var8 = ((ByteBuffer)this.theBuffer).duplicate();
         var8.position(var8.position() + this.getIndex(var1, var2));
         var5.getPixels(var6, var7, var3, var4, (WritablePixelFormat)this.theFormat, var8, this.scanlineElems);
      }
   }

   abstract class BaseAccessor extends Accessor {
      javafx.scene.image.PixelFormat theFormat;
      PixelGetter theGetter;
      PixelSetter theSetter;
      Buffer theBuffer;
      int pixelElems;
      int scanlineElems;
      int offsetElems;

      BaseAccessor(javafx.scene.image.PixelFormat var2, Buffer var3, int var4) {
         this(var2, PixelUtils.getGetter(var2), Image.getSetterIfWritable(var2), var3, var4);
      }

      BaseAccessor(javafx.scene.image.PixelFormat var2, PixelGetter var3, PixelSetter var4, Buffer var5, int var6) {
         super();
         this.theFormat = var2;
         this.theGetter = var3;
         this.theSetter = var4;
         this.theBuffer = var5;
         this.pixelElems = var6;
         this.scanlineElems = Image.this.scanlineStride / Image.this.pixelFormat.getDataType().getSizeInBytes();
         this.offsetElems = Image.this.minY * this.scanlineElems + Image.this.minX * var6;
      }

      public int getIndex(int var1, int var2) {
         if (var1 >= 0 && var2 >= 0 && var1 < Image.this.width && var2 < Image.this.height) {
            return this.offsetElems + var2 * this.scanlineElems + var1 * this.pixelElems;
         } else {
            throw new IndexOutOfBoundsException(var1 + ", " + var2);
         }
      }

      public Buffer getBuffer() {
         return this.theBuffer;
      }

      public PixelGetter getGetter() {
         if (this.theGetter == null) {
            throw new UnsupportedOperationException("Unsupported Image type");
         } else {
            return this.theGetter;
         }
      }

      public PixelSetter getSetter() {
         if (this.theSetter == null) {
            throw new UnsupportedOperationException("Unsupported Image type");
         } else {
            return this.theSetter;
         }
      }

      public javafx.scene.image.PixelFormat getPlatformPixelFormat() {
         return this.theFormat;
      }

      public boolean isWritable() {
         return this.theSetter != null;
      }

      public PlatformImage promoteToWritableImage() {
         return Image.this;
      }

      public int getArgb(int var1, int var2) {
         return this.getGetter().getArgb(this.getBuffer(), this.getIndex(var1, var2));
      }

      public void setArgb(int var1, int var2, int var3) {
         this.getSetter().setArgb(this.getBuffer(), this.getIndex(var1, var2), var3);
      }

      public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, Buffer var6, int var7) {
         PixelSetter var8 = PixelUtils.getSetter(var5);
         PixelConverter var9 = PixelUtils.getConverter(this.getGetter(), var8);
         int var10 = var6.position();
         var9.convert(this.getBuffer(), this.getIndex(var1, var2), this.scanlineElems, var6, var10, var7, var3, var4);
      }

      public void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat var5, Buffer var6, int var7) {
         PixelGetter var8 = PixelUtils.getGetter(var5);
         PixelConverter var9 = PixelUtils.getConverter(var8, this.getSetter());
         int var10 = var6.position();
         var9.convert(var6, var10, var7, this.getBuffer(), this.getIndex(var1, var2), this.scanlineElems, var3, var4);
      }
   }

   class ScaledAccessor extends Accessor {
      Accessor theDelegate;
      float pixelScale;

      ScaledAccessor(Accessor var2, float var3) {
         super();
         this.theDelegate = var2;
         this.pixelScale = var3;
      }

      private int scale(int var1) {
         return (int)(((float)var1 + 0.5F) * this.pixelScale);
      }

      public int getArgb(int var1, int var2) {
         return this.theDelegate.getArgb(this.scale(var1), this.scale(var2));
      }

      public void setArgb(int var1, int var2, int var3) {
         throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
      }

      public javafx.scene.image.PixelFormat getPlatformPixelFormat() {
         return this.theDelegate.getPlatformPixelFormat();
      }

      public boolean isWritable() {
         return this.theDelegate.isWritable();
      }

      public PlatformImage promoteToWritableImage() {
         throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
      }

      public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, Buffer var6, int var7) {
         PixelSetter var8 = PixelUtils.getSetter(var5);
         int var9 = var6.position();
         int var10 = var8.getNumElements();

         for(int var11 = 0; var11 < var4; ++var11) {
            int var12 = this.scale(var2 + var11);
            int var13 = var9;

            for(int var14 = 0; var14 < var3; ++var14) {
               int var15 = this.scale(var1 + var14);
               var8.setArgb(var6, var13, this.theDelegate.getArgb(var15, var12));
               var13 += var10;
            }

            var9 += var7;
         }

      }

      public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, byte[] var6, int var7, int var8) {
         ByteBuffer var9 = ByteBuffer.wrap(var6);
         var9.position(var7);
         this.getPixels(var1, var2, var3, var4, var5, var9, var8);
      }

      public void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, int[] var6, int var7, int var8) {
         IntBuffer var9 = IntBuffer.wrap(var6);
         var9.position(var7);
         this.getPixels(var1, var2, var3, var4, var5, var9, var8);
      }

      public void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat var5, Buffer var6, int var7) {
         throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
      }

      public void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat var5, byte[] var6, int var7, int var8) {
         throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
      }

      public void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat var5, int[] var6, int var7, int var8) {
         throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
      }

      public void setPixels(int var1, int var2, int var3, int var4, PixelReader var5, int var6, int var7) {
         throw new UnsupportedOperationException("Pixel setting for scaled images not supported yet");
      }
   }

   abstract class Accessor {
      public abstract int getArgb(int var1, int var2);

      public abstract void setArgb(int var1, int var2, int var3);

      public abstract javafx.scene.image.PixelFormat getPlatformPixelFormat();

      public abstract boolean isWritable();

      public abstract PlatformImage promoteToWritableImage();

      public abstract void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, Buffer var6, int var7);

      public abstract void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, byte[] var6, int var7, int var8);

      public abstract void getPixels(int var1, int var2, int var3, int var4, WritablePixelFormat var5, int[] var6, int var7, int var8);

      public abstract void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat var5, Buffer var6, int var7);

      public abstract void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat var5, byte[] var6, int var7, int var8);

      public abstract void setPixels(int var1, int var2, int var3, int var4, javafx.scene.image.PixelFormat var5, int[] var6, int var7, int var8);

      public abstract void setPixels(int var1, int var2, int var3, int var4, PixelReader var5, int var6, int var7);
   }
}
