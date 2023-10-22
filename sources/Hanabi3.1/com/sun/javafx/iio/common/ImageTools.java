package com.sun.javafx.iio.common;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class ImageTools {
   public static final int PROGRESS_INTERVAL = 5;

   public static int readFully(InputStream var0, byte[] var1, int var2, int var3) throws IOException {
      if (var3 < 0) {
         throw new IndexOutOfBoundsException();
      } else {
         int var4 = var3;
         if (var2 >= 0 && var3 >= 0 && var2 + var3 <= var1.length && var2 + var3 >= 0) {
            while(var3 > 0) {
               int var5 = var0.read(var1, var2, var3);
               if (var5 == -1) {
                  throw new EOFException();
               }

               var2 += var5;
               var3 -= var5;
            }

            return var4;
         } else {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > b.length!");
         }
      }
   }

   public static int readFully(InputStream var0, byte[] var1) throws IOException {
      return readFully(var0, var1, 0, var1.length);
   }

   public static void skipFully(InputStream var0, long var1) throws IOException {
      while(var1 > 0L) {
         long var3 = var0.skip(var1);
         if (var3 <= 0L) {
            if (var0.read() == -1) {
               throw new EOFException();
            }

            --var1;
         } else {
            var1 -= var3;
         }
      }

   }

   public static ImageStorage.ImageType getConvertedType(ImageStorage.ImageType var0) {
      ImageStorage.ImageType var1;
      switch (var0) {
         case GRAY:
            var1 = ImageStorage.ImageType.GRAY;
            break;
         case GRAY_ALPHA:
         case GRAY_ALPHA_PRE:
         case PALETTE_ALPHA:
         case PALETTE_ALPHA_PRE:
         case PALETTE_TRANS:
         case RGBA:
            var1 = ImageStorage.ImageType.RGBA_PRE;
            break;
         case PALETTE:
         case RGB:
            var1 = ImageStorage.ImageType.RGB;
            break;
         case RGBA_PRE:
            var1 = ImageStorage.ImageType.RGBA_PRE;
            break;
         default:
            throw new IllegalArgumentException("Unsupported ImageType " + var0);
      }

      return var1;
   }

   public static byte[] createImageArray(ImageStorage.ImageType var0, int var1, int var2) {
      boolean var3 = false;
      byte var4;
      switch (var0) {
         case GRAY:
         case PALETTE_ALPHA:
         case PALETTE_ALPHA_PRE:
         case PALETTE:
            var4 = 1;
            break;
         case GRAY_ALPHA:
         case GRAY_ALPHA_PRE:
            var4 = 2;
            break;
         case PALETTE_TRANS:
         default:
            throw new IllegalArgumentException("Unsupported ImageType " + var0);
         case RGBA:
         case RGBA_PRE:
            var4 = 4;
            break;
         case RGB:
            var4 = 3;
      }

      return new byte[var1 * var2 * var4];
   }

   public static ImageFrame convertImageFrame(ImageFrame var0) {
      ImageStorage.ImageType var2 = var0.getImageType();
      ImageStorage.ImageType var3 = getConvertedType(var2);
      ImageFrame var1;
      if (var3 == var2) {
         var1 = var0;
      } else {
         Object var4 = null;
         Buffer var5 = var0.getImageData();
         if (!(var5 instanceof ByteBuffer)) {
            throw new IllegalArgumentException("!(frame.getImageData() instanceof ByteBuffer)");
         }

         ByteBuffer var6 = (ByteBuffer)var5;
         byte[] var17;
         if (var6.hasArray()) {
            var17 = var6.array();
         } else {
            var17 = new byte[var6.capacity()];
            var6.get(var17);
         }

         int var7 = var0.getWidth();
         int var8 = var0.getHeight();
         int var9 = var0.getStride();
         byte[] var10 = createImageArray(var3, var7, var8);
         ByteBuffer var11 = ByteBuffer.wrap(var10);
         int var12 = var10.length / var8;
         byte[][] var13 = var0.getPalette();
         ImageMetadata var14 = var0.getMetadata();
         int var15 = var14.transparentIndex != null ? var14.transparentIndex : 0;
         convert(var7, var8, var2, var17, 0, var9, var10, 0, var12, var13, var15, false);
         ImageMetadata var16 = new ImageMetadata(var14.gamma, var14.blackIsZero, (Integer)null, var14.backgroundColor, (Integer)null, var14.delayTime, var14.loopCount, var14.imageWidth, var14.imageHeight, var14.imageLeftPosition, var14.imageTopPosition, var14.disposalMethod);
         var1 = new ImageFrame(var3, var11, var7, var8, var12, (byte[][])null, var16);
      }

      return var1;
   }

   public static byte[] convert(int var0, int var1, ImageStorage.ImageType var2, byte[] var3, int var4, int var5, byte[] var6, int var7, int var8, byte[][] var9, int var10, boolean var11) {
      int var12;
      int var13;
      int var14;
      int var15;
      if (var2 != ImageStorage.ImageType.GRAY && var2 != ImageStorage.ImageType.RGB && var2 != ImageStorage.ImageType.RGBA_PRE) {
         int var16;
         int var17;
         int var18;
         int var19;
         if (var2 != ImageStorage.ImageType.GRAY_ALPHA && var2 != ImageStorage.ImageType.GRAY_ALPHA_PRE) {
            byte[] var27;
            byte[] var28;
            byte[] var30;
            int var31;
            if (var2 == ImageStorage.ImageType.PALETTE) {
               var13 = var7;
               var27 = var9[0];
               var28 = var9[1];
               var30 = var9[2];
               var17 = var4;
               var18 = var7;

               for(var19 = 0; var19 < var0; ++var19) {
                  var31 = var3[var17++] & 255;
                  var6[var18++] = var27[var31];
                  var6[var18++] = var28[var31];
                  var6[var18++] = var30[var31];
                  var13 += var8;
               }
            } else {
               int var21;
               int var22;
               byte[] var29;
               if (var2 == ImageStorage.ImageType.PALETTE_ALPHA) {
                  var27 = var9[0];
                  var28 = var9[1];
                  var30 = var9[2];
                  var29 = var9[3];
                  var18 = var4;
                  var19 = var7;

                  for(var31 = 0; var31 < var0; ++var31) {
                     var21 = var3[var18++] & 255;
                     var22 = var27[var21];
                     byte var23 = var28[var21];
                     byte var24 = var30[var21];
                     int var25 = var29[var21] & 255;
                     float var26 = (float)var25 / 255.0F;
                     var6[var19++] = (byte)((int)(var26 * (float)(var22 & 255)));
                     var6[var19++] = (byte)((int)(var26 * (float)(var23 & 255)));
                     var6[var19++] = (byte)((int)(var26 * (float)(var24 & 255)));
                     var6[var19++] = (byte)var25;
                  }

                  int var10000 = var4 + var5;
                  var13 = var7 + var8;
               } else if (var2 == ImageStorage.ImageType.PALETTE_ALPHA_PRE) {
                  var12 = var4;
                  var13 = var7;
                  var27 = var9[0];
                  var28 = var9[1];
                  var30 = var9[2];
                  var29 = var9[3];

                  for(var18 = 0; var18 < var1; ++var18) {
                     var19 = var12;
                     var31 = var13;

                     for(var21 = 0; var21 < var0; ++var21) {
                        var22 = var3[var19++] & 255;
                        var6[var31++] = var27[var22];
                        var6[var31++] = var28[var22];
                        var6[var31++] = var30[var22];
                        var6[var31++] = var29[var22];
                     }

                     var12 += var5;
                     var13 += var8;
                  }
               } else if (var2 == ImageStorage.ImageType.PALETTE_TRANS) {
                  var12 = var4;
                  var13 = var7;

                  for(var14 = 0; var14 < var1; ++var14) {
                     var15 = var12;
                     var16 = var13;
                     var29 = var9[0];
                     byte[] var32 = var9[1];
                     byte[] var34 = var9[2];

                     for(var31 = 0; var31 < var0; ++var31) {
                        var21 = var3[var15++] & 255;
                        if (var21 == var10) {
                           if (var11) {
                              var16 += 4;
                           } else {
                              var6[var16++] = 0;
                              var6[var16++] = 0;
                              var6[var16++] = 0;
                              var6[var16++] = 0;
                           }
                        } else {
                           var6[var16++] = var29[var21];
                           var6[var16++] = var32[var21];
                           var6[var16++] = var34[var21];
                           var6[var16++] = -1;
                        }
                     }

                     var12 += var5;
                     var13 += var8;
                  }
               } else {
                  if (var2 != ImageStorage.ImageType.RGBA) {
                     throw new UnsupportedOperationException("Unsupported ImageType " + var2);
                  }

                  var12 = var4;
                  var13 = var7;

                  for(var14 = 0; var14 < var1; ++var14) {
                     var15 = var12;
                     var16 = var13;

                     for(var17 = 0; var17 < var0; ++var17) {
                        byte var33 = var3[var15++];
                        byte var35 = var3[var15++];
                        byte var37 = var3[var15++];
                        var21 = var3[var15++] & 255;
                        float var36 = (float)var21 / 255.0F;
                        var6[var16++] = (byte)((int)(var36 * (float)(var33 & 255)));
                        var6[var16++] = (byte)((int)(var36 * (float)(var35 & 255)));
                        var6[var16++] = (byte)((int)(var36 * (float)(var37 & 255)));
                        var6[var16++] = (byte)var21;
                     }

                     var12 += var5;
                     var13 += var8;
                  }
               }
            }
         } else {
            var12 = var4;
            var13 = var7;
            if (var2 == ImageStorage.ImageType.GRAY_ALPHA) {
               for(var14 = 0; var14 < var1; ++var14) {
                  var15 = var12;
                  var16 = var13;

                  for(var17 = 0; var17 < var0; ++var17) {
                     var18 = var3[var15++];
                     var19 = var3[var15++] & 255;
                     float var20 = (float)var19 / 255.0F;
                     var18 = (byte)((int)(var20 * (float)(var18 & 255)));
                     var6[var16++] = (byte)var18;
                     var6[var16++] = (byte)var18;
                     var6[var16++] = (byte)var18;
                     var6[var16++] = (byte)var19;
                  }

                  var12 += var5;
                  var13 += var8;
               }
            } else {
               for(var14 = 0; var14 < var1; ++var14) {
                  var15 = var12;
                  var16 = var13;

                  for(var17 = 0; var17 < var0; ++var17) {
                     var18 = var3[var15++];
                     var6[var16++] = (byte)var18;
                     var6[var16++] = (byte)var18;
                     var6[var16++] = (byte)var18;
                     var6[var16++] = var3[var15++];
                  }

                  var12 += var5;
                  var13 += var8;
               }
            }
         }
      } else if (var3 != var6) {
         var12 = var0;
         if (var2 == ImageStorage.ImageType.RGB) {
            var12 = var0 * 3;
         } else if (var2 == ImageStorage.ImageType.RGBA_PRE) {
            var12 = var0 * 4;
         }

         if (var1 == 1) {
            System.arraycopy(var3, var4, var6, var7, var12);
         } else {
            var13 = var4;
            var14 = var7;

            for(var15 = 0; var15 < var1; ++var15) {
               System.arraycopy(var3, var13, var6, var14, var12);
               var13 += var5;
               var14 += var8;
            }
         }
      }

      return var6;
   }

   public static String getScaledImageName(String var0) {
      StringBuilder var1 = new StringBuilder();
      int var2 = var0.lastIndexOf(47);
      String var3 = var2 < 0 ? var0 : var0.substring(var2 + 1);
      int var4 = var3.lastIndexOf(".");
      if (var4 < 0) {
         var4 = var3.length();
      }

      if (var2 >= 0) {
         var1.append(var0.substring(0, var2 + 1));
      }

      var1.append(var3.substring(0, var4));
      var1.append("@2x");
      var1.append(var3.substring(var4));
      return var1.toString();
   }

   public static InputStream createInputStream(String var0) throws IOException {
      Object var1 = null;

      try {
         File var2 = new File(var0);
         if (var2.exists()) {
            var1 = new FileInputStream(var2);
         }
      } catch (Exception var3) {
      }

      if (var1 == null) {
         URL var4 = new URL(var0);
         var1 = var4.openStream();
      }

      return (InputStream)var1;
   }

   private static void computeUpdatedPixels(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int[] var9, int var10) {
      boolean var11 = false;
      int var12 = -1;
      int var13 = -1;
      int var14 = -1;

      for(int var15 = 0; var15 < var7; ++var15) {
         int var16 = var6 + var15 * var8;
         if (var16 >= var0 && (var16 - var0) % var5 == 0) {
            if (var16 >= var0 + var1) {
               break;
            }

            int var17 = var2 + (var16 - var0) / var5;
            if (var17 >= var3) {
               if (var17 > var4) {
                  break;
               }

               if (!var11) {
                  var12 = var17;
                  var11 = true;
               } else if (var13 == -1) {
                  var13 = var17;
               }

               var14 = var17;
            }
         }
      }

      var9[var10] = var12;
      if (!var11) {
         var9[var10 + 2] = 0;
      } else {
         var9[var10 + 2] = var14 - var12 + 1;
      }

      var9[var10 + 4] = Math.max(var13 - var12, 1);
   }

   public static int[] computeUpdatedPixels(Rectangle var0, Point2D var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13) {
      int[] var14 = new int[6];
      computeUpdatedPixels(var0.x, var0.width, (int)(var1.x + 0.5F), var2, var4, var6, var8, var10, var12, var14, 0);
      computeUpdatedPixels(var0.y, var0.height, (int)(var1.y + 0.5F), var3, var5, var7, var9, var11, var13, var14, 1);
      return var14;
   }

   public static int[] computeDimensions(int var0, int var1, int var2, int var3, boolean var4) {
      int var5 = var2 < 0 ? 0 : var2;
      int var6 = var3 < 0 ? 0 : var3;
      if (var5 == 0 && var6 == 0) {
         var5 = var0;
         var6 = var1;
      } else if (var5 != var0 || var6 != var1) {
         if (var4) {
            if (var5 == 0) {
               var5 = (int)((float)var0 * (float)var6 / (float)var1);
            } else if (var6 == 0) {
               var6 = (int)((float)var1 * (float)var5 / (float)var0);
            } else {
               float var7 = Math.min((float)var5 / (float)var0, (float)var6 / (float)var1);
               var5 = (int)((float)var0 * var7);
               var6 = (int)((float)var1 * var7);
            }
         } else {
            if (var6 == 0) {
               var6 = var1;
            }

            if (var5 == 0) {
               var5 = var0;
            }
         }

         if (var5 == 0) {
            var5 = 1;
         }

         if (var6 == 0) {
            var6 = 1;
         }
      }

      return new int[]{var5, var6};
   }

   public static ImageFrame scaleImageFrame(ImageFrame var0, int var1, int var2, boolean var3) {
      int var4 = ImageStorage.getNumBands(var0.getImageType());
      ByteBuffer var5 = scaleImage((ByteBuffer)var0.getImageData(), var0.getWidth(), var0.getHeight(), var4, var1, var2, var3);
      return new ImageFrame(var0.getImageType(), var5, var1, var2, var1 * var4, (byte[][])null, var0.getMetadata());
   }

   public static ByteBuffer scaleImage(ByteBuffer var0, int var1, int var2, int var3, int var4, int var5, boolean var6) {
      PushbroomScaler var7 = ScalerFactory.createScaler(var1, var2, var3, var4, var5, var6);
      int var8 = var1 * var3;
      byte[] var9;
      int var10;
      if (var0.hasArray()) {
         var9 = var0.array();

         for(var10 = 0; var10 != var2; ++var10) {
            var7.putSourceScanline(var9, var10 * var8);
         }
      } else {
         var9 = new byte[var8];

         for(var10 = 0; var10 != var2; ++var10) {
            var0.get(var9);
            var7.putSourceScanline(var9, 0);
         }
      }

      return var7.getDestination();
   }
}
