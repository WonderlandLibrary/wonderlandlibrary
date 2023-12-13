package com.sun.javafx.iio.bmp;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import java.io.IOException;
import java.io.InputStream;

public final class BMPImageLoaderFactory implements ImageLoaderFactory {
   private static final BMPImageLoaderFactory theInstance = new BMPImageLoaderFactory();

   public static ImageLoaderFactory getInstance() {
      return theInstance;
   }

   public ImageFormatDescription getFormatDescription() {
      return BMPDescriptor.theInstance;
   }

   public ImageLoader createImageLoader(InputStream var1) throws IOException {
      return new BMPImageLoader(var1);
   }
}
