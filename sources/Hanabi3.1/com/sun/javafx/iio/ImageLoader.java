package com.sun.javafx.iio;

import java.io.IOException;

public interface ImageLoader {
   ImageFormatDescription getFormatDescription();

   void dispose();

   void addListener(ImageLoadListener var1);

   void removeListener(ImageLoadListener var1);

   ImageFrame load(int var1, int var2, int var3, boolean var4, boolean var5) throws IOException;
}
