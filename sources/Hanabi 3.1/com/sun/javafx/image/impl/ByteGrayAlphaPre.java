package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;

public class ByteGrayAlphaPre {
   public static final BytePixelGetter getter;
   public static final BytePixelSetter setter;
   public static final BytePixelAccessor accessor;

   public static ByteToBytePixelConverter ToByteBgraPreConverter() {
      return ByteGrayAlpha.ToByteBgraSameConv.premul;
   }

   static {
      getter = ByteGrayAlpha.Accessor.premul;
      setter = ByteGrayAlpha.Accessor.premul;
      accessor = ByteGrayAlpha.Accessor.premul;
   }
}
