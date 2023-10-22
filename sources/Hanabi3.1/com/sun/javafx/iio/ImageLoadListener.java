package com.sun.javafx.iio;

public interface ImageLoadListener {
   void imageLoadProgress(ImageLoader var1, float var2);

   void imageLoadWarning(ImageLoader var1, String var2);

   void imageLoadMetaData(ImageLoader var1, ImageMetadata var2);
}
