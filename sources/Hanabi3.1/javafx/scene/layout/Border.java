package javafx.scene.layout;

import com.sun.javafx.UnmodifiableArrayList;
import com.sun.javafx.css.SubCssMetaData;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.scene.layout.region.BorderImageSlices;
import com.sun.javafx.scene.layout.region.BorderImageWidthConverter;
import com.sun.javafx.scene.layout.region.CornerRadiiConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderPaintConverter;
import com.sun.javafx.scene.layout.region.LayeredBorderStyleConverter;
import com.sun.javafx.scene.layout.region.Margins;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import com.sun.javafx.scene.layout.region.RepeatStructConverter;
import com.sun.javafx.scene.layout.region.SliceSequenceConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.css.CssMetaData;
import javafx.geometry.Insets;

public final class Border {
   static final CssMetaData BORDER_COLOR = new SubCssMetaData("-fx-border-color", LayeredBorderPaintConverter.getInstance());
   static final CssMetaData BORDER_STYLE = new SubCssMetaData("-fx-border-style", LayeredBorderStyleConverter.getInstance());
   static final CssMetaData BORDER_WIDTH = new SubCssMetaData("-fx-border-width", Margins.SequenceConverter.getInstance());
   static final CssMetaData BORDER_RADIUS = new SubCssMetaData("-fx-border-radius", CornerRadiiConverter.getInstance());
   static final CssMetaData BORDER_INSETS = new SubCssMetaData("-fx-border-insets", InsetsConverter.SequenceConverter.getInstance());
   static final CssMetaData BORDER_IMAGE_SOURCE = new SubCssMetaData("-fx-border-image-source", URLConverter.SequenceConverter.getInstance());
   static final CssMetaData BORDER_IMAGE_REPEAT;
   static final CssMetaData BORDER_IMAGE_SLICE;
   static final CssMetaData BORDER_IMAGE_WIDTH;
   static final CssMetaData BORDER_IMAGE_INSETS;
   private static final List STYLEABLES;
   public static final Border EMPTY;
   final List strokes;
   final List images;
   final Insets outsets;
   final Insets insets;
   private final int hash;

   public static List getClassCssMetaData() {
      return STYLEABLES;
   }

   public final List getStrokes() {
      return this.strokes;
   }

   public final List getImages() {
      return this.images;
   }

   public final Insets getOutsets() {
      return this.outsets;
   }

   public final Insets getInsets() {
      return this.insets;
   }

   public final boolean isEmpty() {
      return this.strokes.isEmpty() && this.images.isEmpty();
   }

   public Border(@NamedArg("strokes") BorderStroke... var1) {
      this((BorderStroke[])var1, (BorderImage[])null);
   }

   public Border(@NamedArg("images") BorderImage... var1) {
      this((BorderStroke[])null, (BorderImage[])var1);
   }

   public Border(@NamedArg("strokes") List var1, @NamedArg("images") List var2) {
      this(var1 == null ? null : (BorderStroke[])var1.toArray(new BorderStroke[var1.size()]), var2 == null ? null : (BorderImage[])var2.toArray(new BorderImage[var2.size()]));
   }

   public Border(@NamedArg("strokes") BorderStroke[] var1, @NamedArg("images") BorderImage[] var2) {
      double var3 = 0.0;
      double var5 = 0.0;
      double var7 = 0.0;
      double var9 = 0.0;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      double var17 = 0.0;
      int var20;
      int var21;
      double var23;
      double var25;
      double var27;
      double var29;
      double var31;
      double var33;
      double var35;
      double var37;
      if (var1 != null && var1.length != 0) {
         BorderStroke[] var19 = new BorderStroke[var1.length];
         var20 = 0;

         for(var21 = 0; var21 < var1.length; ++var21) {
            BorderStroke var22 = var1[var21];
            if (var22 != null) {
               var19[var20++] = var22;
               var23 = var22.innerEdge.getTop();
               var25 = var22.innerEdge.getRight();
               var27 = var22.innerEdge.getBottom();
               var29 = var22.innerEdge.getLeft();
               var3 = var3 >= var23 ? var3 : var23;
               var5 = var5 >= var25 ? var5 : var25;
               var7 = var7 >= var27 ? var7 : var27;
               var9 = var9 >= var29 ? var9 : var29;
               var31 = var22.outerEdge.getTop();
               var33 = var22.outerEdge.getRight();
               var35 = var22.outerEdge.getBottom();
               var37 = var22.outerEdge.getLeft();
               var11 = var11 >= var31 ? var11 : var31;
               var13 = var13 >= var33 ? var13 : var33;
               var15 = var15 >= var35 ? var15 : var35;
               var17 = var17 >= var37 ? var17 : var37;
            }
         }

         this.strokes = new UnmodifiableArrayList(var19, var20);
      } else {
         this.strokes = Collections.emptyList();
      }

      if (var2 != null && var2.length != 0) {
         BorderImage[] var39 = new BorderImage[var2.length];
         var20 = 0;

         for(var21 = 0; var21 < var2.length; ++var21) {
            BorderImage var41 = var2[var21];
            if (var41 != null) {
               var39[var20++] = var41;
               var23 = var41.innerEdge.getTop();
               var25 = var41.innerEdge.getRight();
               var27 = var41.innerEdge.getBottom();
               var29 = var41.innerEdge.getLeft();
               var3 = var3 >= var23 ? var3 : var23;
               var5 = var5 >= var25 ? var5 : var25;
               var7 = var7 >= var27 ? var7 : var27;
               var9 = var9 >= var29 ? var9 : var29;
               var31 = var41.outerEdge.getTop();
               var33 = var41.outerEdge.getRight();
               var35 = var41.outerEdge.getBottom();
               var37 = var41.outerEdge.getLeft();
               var11 = var11 >= var31 ? var11 : var31;
               var13 = var13 >= var33 ? var13 : var33;
               var15 = var15 >= var35 ? var15 : var35;
               var17 = var17 >= var37 ? var17 : var37;
            }
         }

         this.images = new UnmodifiableArrayList(var39, var20);
      } else {
         this.images = Collections.emptyList();
      }

      this.outsets = new Insets(var11, var13, var15, var17);
      this.insets = new Insets(var3, var5, var7, var9);
      int var40 = this.strokes.hashCode();
      var40 = 31 * var40 + this.images.hashCode();
      this.hash = var40;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         Border var2 = (Border)var1;
         if (this.hash != var2.hash) {
            return false;
         } else if (!this.images.equals(var2.images)) {
            return false;
         } else {
            return this.strokes.equals(var2.strokes);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hash;
   }

   static {
      BORDER_IMAGE_REPEAT = new SubCssMetaData("-fx-border-image-repeat", RepeatStructConverter.getInstance(), new RepeatStruct[]{new RepeatStruct(BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT)});
      BORDER_IMAGE_SLICE = new SubCssMetaData("-fx-border-image-slice", SliceSequenceConverter.getInstance(), new BorderImageSlices[]{BorderImageSlices.DEFAULT});
      BORDER_IMAGE_WIDTH = new SubCssMetaData("-fx-border-image-width", BorderImageWidthConverter.getInstance(), new BorderWidths[]{BorderWidths.DEFAULT});
      BORDER_IMAGE_INSETS = new SubCssMetaData("-fx-border-image-insets", InsetsConverter.SequenceConverter.getInstance(), new Insets[]{Insets.EMPTY});
      STYLEABLES = Collections.unmodifiableList(Arrays.asList(BORDER_COLOR, BORDER_STYLE, BORDER_WIDTH, BORDER_RADIUS, BORDER_INSETS, BORDER_IMAGE_SOURCE, BORDER_IMAGE_REPEAT, BORDER_IMAGE_SLICE, BORDER_IMAGE_WIDTH, BORDER_IMAGE_INSETS));
      EMPTY = new Border((BorderStroke[])null, (BorderImage[])null);
   }
}
