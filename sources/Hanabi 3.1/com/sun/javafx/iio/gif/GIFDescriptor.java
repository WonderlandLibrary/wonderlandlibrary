package com.sun.javafx.iio.gif;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.common.ImageDescriptor;

public class GIFDescriptor extends ImageDescriptor {
   private static final String formatName = "GIF";
   private static final String[] extensions = new String[]{"gif"};
   private static final ImageFormatDescription.Signature[] signatures = new ImageFormatDescription.Signature[]{new ImageFormatDescription.Signature(new byte[]{71, 73, 70, 56, 55, 97}), new ImageFormatDescription.Signature(new byte[]{71, 73, 70, 56, 57, 97})};
   private static ImageDescriptor theInstance = null;

   private GIFDescriptor() {
      super("GIF", extensions, signatures);
   }

   public static synchronized ImageDescriptor getInstance() {
      if (theInstance == null) {
         theInstance = new GIFDescriptor();
      }

      return theInstance;
   }
}
