package com.sun.javafx.scene.layout.region;

import com.sun.javafx.css.StyleConverterImpl;
import javafx.css.ParsedValue;
import javafx.scene.text.Font;

public final class SliceSequenceConverter extends StyleConverterImpl {
   private static final SliceSequenceConverter BORDER_IMAGE_SLICE_SEQUENCE_CONVERTER = new SliceSequenceConverter();

   public static SliceSequenceConverter getInstance() {
      return BORDER_IMAGE_SLICE_SEQUENCE_CONVERTER;
   }

   public BorderImageSlices[] convert(ParsedValue var1, Font var2) {
      ParsedValue[] var3 = (ParsedValue[])var1.getValue();
      BorderImageSlices[] var4 = new BorderImageSlices[var3.length];

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var4[var5] = BorderImageSliceConverter.getInstance().convert(var3[var5], var2);
      }

      return var4;
   }

   public String toString() {
      return "BorderImageSliceSequenceConverter";
   }
}
