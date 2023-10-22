package com.sun.javafx.iio.ios;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import java.io.IOException;
import java.io.InputStream;

public class IosImageLoaderFactory implements ImageLoaderFactory {
   private static IosImageLoaderFactory theInstance;

   private IosImageLoaderFactory() {
   }

   public static final synchronized IosImageLoaderFactory getInstance() {
      if (theInstance == null) {
         theInstance = new IosImageLoaderFactory();
      }

      return theInstance;
   }

   public ImageFormatDescription getFormatDescription() {
      return IosDescriptor.getInstance();
   }

   public ImageLoader createImageLoader(InputStream var1) throws IOException {
      return new IosImageLoader(var1, IosDescriptor.getInstance());
   }

   public ImageLoader createImageLoader(String var1) throws IOException {
      return new IosImageLoader(var1, IosDescriptor.getInstance());
   }
}
