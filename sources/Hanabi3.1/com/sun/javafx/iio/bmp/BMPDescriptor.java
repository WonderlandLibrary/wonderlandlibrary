package com.sun.javafx.iio.bmp;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.common.ImageDescriptor;

final class BMPDescriptor extends ImageDescriptor {
   static final String formatName = "BMP";
   static final String[] extensions = new String[]{"bmp"};
   static final ImageFormatDescription.Signature[] signatures = new ImageFormatDescription.Signature[]{new ImageFormatDescription.Signature(new byte[]{66, 77})};
   static final ImageDescriptor theInstance = new BMPDescriptor();

   private BMPDescriptor() {
      super("BMP", extensions, signatures);
   }
}
