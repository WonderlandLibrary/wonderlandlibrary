package com.sun.javafx.iio;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.iio.bmp.BMPImageLoaderFactory;
import com.sun.javafx.iio.common.ImageTools;
import com.sun.javafx.iio.gif.GIFImageLoaderFactory;
import com.sun.javafx.iio.ios.IosImageLoaderFactory;
import com.sun.javafx.iio.jpeg.JPEGImageLoaderFactory;
import com.sun.javafx.iio.png.PNGImageLoaderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ImageStorage {
   private static final HashMap loaderFactoriesBySignature;
   private static final ImageLoaderFactory[] loaderFactories;
   private static final boolean isIOS = PlatformUtil.isIOS();
   private static int maxSignatureLength;

   public static ImageFormatDescription[] getSupportedDescriptions() {
      ImageFormatDescription[] var0 = new ImageFormatDescription[loaderFactories.length];

      for(int var1 = 0; var1 < loaderFactories.length; ++var1) {
         var0[var1] = loaderFactories[var1].getFormatDescription();
      }

      return var0;
   }

   public static int getNumBands(ImageType var0) {
      boolean var1 = true;
      byte var2;
      switch (var0) {
         case GRAY:
         case PALETTE:
         case PALETTE_ALPHA:
         case PALETTE_ALPHA_PRE:
         case PALETTE_TRANS:
            var2 = 1;
            break;
         case GRAY_ALPHA:
         case GRAY_ALPHA_PRE:
            var2 = 2;
            break;
         case RGB:
            var2 = 3;
            break;
         case RGBA:
         case RGBA_PRE:
            var2 = 4;
            break;
         default:
            throw new IllegalArgumentException("Unknown ImageType " + var0);
      }

      return var2;
   }

   public static void addImageLoaderFactory(ImageLoaderFactory var0) {
      ImageFormatDescription var1 = var0.getFormatDescription();
      Iterator var2 = var1.getSignatures().iterator();

      while(var2.hasNext()) {
         ImageFormatDescription.Signature var3 = (ImageFormatDescription.Signature)var2.next();
         loaderFactoriesBySignature.put(var3, var0);
      }

      Class var6 = ImageStorage.class;
      synchronized(ImageStorage.class) {
         maxSignatureLength = -1;
      }
   }

   public static ImageFrame[] loadAll(InputStream var0, ImageLoadListener var1, int var2, int var3, boolean var4, float var5, boolean var6) throws ImageStorageException {
      ImageLoader var7 = null;

      try {
         if (isIOS) {
            var7 = IosImageLoaderFactory.getInstance().createImageLoader(var0);
         } else {
            var7 = getLoaderBySignature(var0, var1);
         }
      } catch (IOException var9) {
         throw new ImageStorageException(var9.getMessage(), var9);
      }

      ImageFrame[] var8 = null;
      if (var7 != null) {
         var8 = loadAll(var7, var2, var3, var4, var5, var6);
         return var8;
      } else {
         throw new ImageStorageException("No loader for image data");
      }
   }

   public static ImageFrame[] loadAll(String var0, ImageLoadListener var1, int var2, int var3, boolean var4, float var5, boolean var6) throws ImageStorageException {
      if (var0 != null && !var0.isEmpty()) {
         ImageFrame[] var7 = null;
         InputStream var8 = null;
         ImageLoader var9 = null;

         try {
            float var10 = 1.0F;

            try {
               if (var5 >= 1.5F) {
                  try {
                     String var11 = ImageTools.getScaledImageName(var0);
                     var8 = ImageTools.createInputStream(var11);
                     var10 = 2.0F;
                  } catch (IOException var20) {
                  }
               }

               if (var8 == null) {
                  var8 = ImageTools.createInputStream(var0);
               }

               if (isIOS) {
                  var9 = IosImageLoaderFactory.getInstance().createImageLoader(var8);
               } else {
                  var9 = getLoaderBySignature(var8, var1);
               }
            } catch (IOException var21) {
               throw new ImageStorageException(var21.getMessage(), var21);
            }

            if (var9 == null) {
               throw new ImageStorageException("No loader for image data");
            }

            var7 = loadAll(var9, var2, var3, var4, var10, var6);
         } finally {
            try {
               if (var8 != null) {
                  var8.close();
               }
            } catch (IOException var19) {
            }

         }

         return var7;
      } else {
         throw new ImageStorageException("URL can't be null or empty");
      }
   }

   private static synchronized int getMaxSignatureLength() {
      if (maxSignatureLength < 0) {
         maxSignatureLength = 0;
         Iterator var0 = loaderFactoriesBySignature.keySet().iterator();

         while(var0.hasNext()) {
            ImageFormatDescription.Signature var1 = (ImageFormatDescription.Signature)var0.next();
            int var2 = var1.getLength();
            if (maxSignatureLength < var2) {
               maxSignatureLength = var2;
            }
         }
      }

      return maxSignatureLength;
   }

   private static ImageFrame[] loadAll(ImageLoader var0, int var1, int var2, boolean var3, float var4, boolean var5) throws ImageStorageException {
      ImageFrame[] var6 = null;
      ArrayList var7 = new ArrayList();
      int var8 = 0;
      ImageFrame var9 = null;

      while(true) {
         try {
            var9 = var0.load(var8++, var1, var2, var3, var5);
         } catch (Exception var11) {
            if (var8 > 1) {
               break;
            }

            throw new ImageStorageException(var11.getMessage(), var11);
         }

         if (var9 == null) {
            break;
         }

         var9.setPixelScale(var4);
         var7.add(var9);
      }

      int var10 = var7.size();
      if (var10 > 0) {
         var6 = new ImageFrame[var10];
         var7.toArray(var6);
      }

      return var6;
   }

   private static ImageLoader getLoaderBySignature(InputStream var0, ImageLoadListener var1) throws IOException {
      byte[] var2 = new byte[getMaxSignatureLength()];
      ImageTools.readFully(var0, var2);
      Iterator var3 = loaderFactoriesBySignature.entrySet().iterator();

      Map.Entry var4;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var4 = (Map.Entry)var3.next();
      } while(!((ImageFormatDescription.Signature)var4.getKey()).matches(var2));

      ByteArrayInputStream var5 = new ByteArrayInputStream(var2);
      SequenceInputStream var6 = new SequenceInputStream(var5, var0);
      ImageLoader var7 = ((ImageLoaderFactory)var4.getValue()).createImageLoader(var6);
      if (var1 != null) {
         var7.addListener(var1);
      }

      return var7;
   }

   private ImageStorage() {
   }

   static {
      if (isIOS) {
         loaderFactories = new ImageLoaderFactory[]{IosImageLoaderFactory.getInstance()};
      } else {
         loaderFactories = new ImageLoaderFactory[]{GIFImageLoaderFactory.getInstance(), JPEGImageLoaderFactory.getInstance(), PNGImageLoaderFactory.getInstance(), BMPImageLoaderFactory.getInstance()};
      }

      loaderFactoriesBySignature = new HashMap(loaderFactories.length);

      for(int var0 = 0; var0 < loaderFactories.length; ++var0) {
         addImageLoaderFactory(loaderFactories[var0]);
      }

   }

   public static enum ImageType {
      GRAY,
      GRAY_ALPHA,
      GRAY_ALPHA_PRE,
      PALETTE,
      PALETTE_ALPHA,
      PALETTE_ALPHA_PRE,
      PALETTE_TRANS,
      RGB,
      RGBA,
      RGBA_PRE;
   }
}
