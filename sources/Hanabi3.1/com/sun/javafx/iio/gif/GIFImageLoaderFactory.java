package com.sun.javafx.iio.gif;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import java.io.IOException;
import java.io.InputStream;

public class GIFImageLoaderFactory implements ImageLoaderFactory {
   private static final GIFImageLoaderFactory theInstance = new GIFImageLoaderFactory();

   private GIFImageLoaderFactory() {
   }

   public static final ImageLoaderFactory getInstance() {
      return theInstance;
   }

   public ImageFormatDescription getFormatDescription() {
      return GIFDescriptor.getInstance();
   }

   public ImageLoader createImageLoader(InputStream var1) throws IOException {
      return new GIFImageLoader2(var1);
   }
}
