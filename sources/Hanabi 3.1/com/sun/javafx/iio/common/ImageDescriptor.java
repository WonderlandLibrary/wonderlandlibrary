package com.sun.javafx.iio.common;

import com.sun.javafx.iio.ImageFormatDescription;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ImageDescriptor implements ImageFormatDescription {
   private final String formatName;
   private final List extensions;
   private final List signatures;

   public ImageDescriptor(String var1, String[] var2, ImageFormatDescription.Signature[] var3) {
      this.formatName = var1;
      this.extensions = Collections.unmodifiableList(Arrays.asList(var2));
      this.signatures = Collections.unmodifiableList(Arrays.asList(var3));
   }

   public String getFormatName() {
      return this.formatName;
   }

   public List getExtensions() {
      return this.extensions;
   }

   public List getSignatures() {
      return this.signatures;
   }
}
