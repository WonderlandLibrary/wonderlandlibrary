package com.sun.javafx.iio.jpeg;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import java.io.IOException;
import java.io.InputStream;

public class JPEGImageLoaderFactory implements ImageLoaderFactory {
   private static final JPEGImageLoaderFactory theInstance = new JPEGImageLoaderFactory();

   private JPEGImageLoaderFactory() {
   }

   public static final ImageLoaderFactory getInstance() {
      return theInstance;
   }

   public ImageFormatDescription getFormatDescription() {
      return JPEGDescriptor.getInstance();
   }

   public ImageLoader createImageLoader(InputStream var1) throws IOException {
      return new JPEGImageLoader(var1);
   }
}
