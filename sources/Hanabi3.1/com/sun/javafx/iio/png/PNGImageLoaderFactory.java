package com.sun.javafx.iio.png;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import java.io.IOException;
import java.io.InputStream;

public class PNGImageLoaderFactory implements ImageLoaderFactory {
   private static final PNGImageLoaderFactory theInstance = new PNGImageLoaderFactory();

   private PNGImageLoaderFactory() {
   }

   public static final ImageLoaderFactory getInstance() {
      return theInstance;
   }

   public ImageFormatDescription getFormatDescription() {
      return PNGDescriptor.getInstance();
   }

   public ImageLoader createImageLoader(InputStream var1) throws IOException {
      return new PNGImageLoader2(var1);
   }
}
