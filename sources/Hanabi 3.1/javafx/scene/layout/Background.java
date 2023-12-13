package javafx.scene.layout;

import com.sun.javafx.UnmodifiableArrayList;
import com.sun.javafx.css.SubCssMetaData;
import com.sun.javafx.css.converters.InsetsConverter;
import com.sun.javafx.css.converters.PaintConverter;
import com.sun.javafx.css.converters.URLConverter;
import com.sun.javafx.scene.layout.region.CornerRadiiConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundPositionConverter;
import com.sun.javafx.scene.layout.region.LayeredBackgroundSizeConverter;
import com.sun.javafx.scene.layout.region.RepeatStruct;
import com.sun.javafx.scene.layout.region.RepeatStructConverter;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.Image;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.css.CssMetaData;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public final class Background {
   static final CssMetaData BACKGROUND_COLOR;
   static final CssMetaData BACKGROUND_RADIUS;
   static final CssMetaData BACKGROUND_INSETS;
   static final CssMetaData BACKGROUND_IMAGE;
   static final CssMetaData BACKGROUND_REPEAT;
   static final CssMetaData BACKGROUND_POSITION;
   static final CssMetaData BACKGROUND_SIZE;
   private static final List STYLEABLES;
   public static final Background EMPTY;
   final List fills;
   final List images;
   final Insets outsets;
   private final boolean hasOpaqueFill;
   private final double opaqueFillTop;
   private final double opaqueFillRight;
   private final double opaqueFillBottom;
   private final double opaqueFillLeft;
   final boolean hasPercentageBasedOpaqueFills;
   final boolean hasPercentageBasedFills;
   private final int hash;

   public static List getClassCssMetaData() {
      return STYLEABLES;
   }

   public final List getFills() {
      return this.fills;
   }

   public final List getImages() {
      return this.images;
   }

   public final Insets getOutsets() {
      return this.outsets;
   }

   public final boolean isEmpty() {
      return this.fills.isEmpty() && this.images.isEmpty();
   }

   public Background(@NamedArg("fills") BackgroundFill... var1) {
      this((BackgroundFill[])var1, (BackgroundImage[])null);
   }

   public Background(@NamedArg("images") BackgroundImage... var1) {
      this((BackgroundFill[])null, (BackgroundImage[])var1);
   }

   public Background(@NamedArg("fills") List var1, @NamedArg("images") List var2) {
      this(var1 == null ? null : (BackgroundFill[])var1.toArray(new BackgroundFill[var1.size()]), var2 == null ? null : (BackgroundImage[])var2.toArray(new BackgroundImage[var2.size()]));
   }

   public Background(@NamedArg("fills") BackgroundFill[] var1, @NamedArg("images") BackgroundImage[] var2) {
      double var3 = 0.0;
      double var5 = 0.0;
      double var7 = 0.0;
      double var9 = 0.0;
      boolean var11 = false;
      boolean var12 = false;
      boolean var13 = false;
      int var15;
      int var16;
      if (var1 != null && var1.length != 0) {
         BackgroundFill[] var14 = new BackgroundFill[var1.length];
         var15 = 0;

         for(var16 = 0; var16 < var1.length; ++var16) {
            BackgroundFill var17 = var1[var16];
            if (var17 != null) {
               var14[var15++] = var17;
               Insets var18 = var17.getInsets();
               double var19 = var18.getTop();
               double var21 = var18.getRight();
               double var23 = var18.getBottom();
               double var25 = var18.getLeft();
               var3 = var3 <= var19 ? var3 : var19;
               var5 = var5 <= var21 ? var5 : var21;
               var7 = var7 <= var23 ? var7 : var23;
               var9 = var9 <= var25 ? var9 : var25;
               boolean var27 = var17.getRadii().hasPercentBasedRadii;
               var12 |= var27;
               if (var17.fill.isOpaque()) {
                  var13 = true;
                  if (var27) {
                     var11 = true;
                  }
               }
            }
         }

         this.fills = new UnmodifiableArrayList(var14, var15);
      } else {
         this.fills = Collections.emptyList();
      }

      this.hasPercentageBasedFills = var12;
      this.outsets = new Insets(Math.max(0.0, -var3), Math.max(0.0, -var5), Math.max(0.0, -var7), Math.max(0.0, -var9));
      if (var2 != null && var2.length != 0) {
         BackgroundImage[] var28 = new BackgroundImage[var2.length];
         var15 = 0;

         for(var16 = 0; var16 < var2.length; ++var16) {
            BackgroundImage var31 = var2[var16];
            if (var31 != null) {
               var28[var15++] = var31;
            }
         }

         this.images = new UnmodifiableArrayList(var28, var15);
      } else {
         this.images = Collections.emptyList();
      }

      this.hasOpaqueFill = var13;
      if (var11) {
         this.opaqueFillTop = Double.NaN;
         this.opaqueFillRight = Double.NaN;
         this.opaqueFillBottom = Double.NaN;
         this.opaqueFillLeft = Double.NaN;
      } else {
         double[] var29 = new double[4];
         this.computeOpaqueInsets(1.0, 1.0, true, var29);
         this.opaqueFillTop = var29[0];
         this.opaqueFillRight = var29[1];
         this.opaqueFillBottom = var29[2];
         this.opaqueFillLeft = var29[3];
      }

      this.hasPercentageBasedOpaqueFills = var11;
      int var30 = this.fills.hashCode();
      var30 = 31 * var30 + this.images.hashCode();
      this.hash = var30;
   }

   public boolean isFillPercentageBased() {
      return this.hasPercentageBasedFills;
   }

   void computeOpaqueInsets(double var1, double var3, double[] var5) {
      this.computeOpaqueInsets(var1, var3, false, var5);
   }

   private void computeOpaqueInsets(double var1, double var3, boolean var5, double[] var6) {
      double var7 = Double.NaN;
      double var9 = Double.NaN;
      double var11 = Double.NaN;
      double var13 = Double.NaN;
      double var28;
      double var30;
      double var32;
      double var34;
      double var36;
      if (this.hasOpaqueFill) {
         if (!var5 && !this.hasPercentageBasedOpaqueFills) {
            var7 = this.opaqueFillTop;
            var9 = this.opaqueFillRight;
            var11 = this.opaqueFillBottom;
            var13 = this.opaqueFillLeft;
         } else {
            int var15 = 0;

            for(int var16 = this.fills.size(); var15 < var16; ++var15) {
               BackgroundFill var17 = (BackgroundFill)this.fills.get(var15);
               Insets var18 = var17.getInsets();
               double var19 = var18.getTop();
               double var21 = var18.getRight();
               double var23 = var18.getBottom();
               double var25 = var18.getLeft();
               if (var17.fill.isOpaque()) {
                  CornerRadii var27 = var17.getRadii();
                  var28 = var27.isTopLeftHorizontalRadiusAsPercentage() ? var1 * var27.getTopLeftHorizontalRadius() : var27.getTopLeftHorizontalRadius();
                  var30 = var27.isTopLeftVerticalRadiusAsPercentage() ? var3 * var27.getTopLeftVerticalRadius() : var27.getTopLeftVerticalRadius();
                  var32 = var27.isTopRightVerticalRadiusAsPercentage() ? var3 * var27.getTopRightVerticalRadius() : var27.getTopRightVerticalRadius();
                  var34 = var27.isTopRightHorizontalRadiusAsPercentage() ? var1 * var27.getTopRightHorizontalRadius() : var27.getTopRightHorizontalRadius();
                  var36 = var27.isBottomRightHorizontalRadiusAsPercentage() ? var1 * var27.getBottomRightHorizontalRadius() : var27.getBottomRightHorizontalRadius();
                  double var38 = var27.isBottomRightVerticalRadiusAsPercentage() ? var3 * var27.getBottomRightVerticalRadius() : var27.getBottomRightVerticalRadius();
                  double var40 = var27.isBottomLeftVerticalRadiusAsPercentage() ? var3 * var27.getBottomLeftVerticalRadius() : var27.getBottomLeftVerticalRadius();
                  double var42 = var27.isBottomLeftHorizontalRadiusAsPercentage() ? var1 * var27.getBottomLeftHorizontalRadius() : var27.getBottomLeftHorizontalRadius();
                  double var44 = var19 + Math.max(var30, var32) / 2.0;
                  double var46 = var21 + Math.max(var34, var36) / 2.0;
                  double var48 = var23 + Math.max(var40, var38) / 2.0;
                  double var50 = var25 + Math.max(var28, var42) / 2.0;
                  if (Double.isNaN(var7)) {
                     var7 = var44;
                     var9 = var46;
                     var11 = var48;
                     var13 = var50;
                  } else {
                     boolean var52 = var44 >= var7;
                     boolean var53 = var46 >= var9;
                     boolean var54 = var48 >= var11;
                     boolean var55 = var50 >= var13;
                     if (!var52 || !var53 || !var54 || !var55) {
                        if (!var52 && !var53 && !var54 && !var55) {
                           var7 = var19;
                           var9 = var21;
                           var11 = var23;
                           var13 = var25;
                        } else if (var50 == var13 && var46 == var9) {
                           var7 = Math.min(var44, var7);
                           var11 = Math.min(var48, var11);
                        } else if (var44 == var7 && var48 == var11) {
                           var13 = Math.min(var50, var13);
                           var9 = Math.min(var46, var9);
                        }
                     }
                  }
               }
            }
         }
      }

      Toolkit.ImageAccessor var56 = Toolkit.getImageAccessor();
      Iterator var57 = this.images.iterator();

      while(var57.hasNext()) {
         BackgroundImage var58 = (BackgroundImage)var57.next();
         if (var58.opaque == null) {
            PlatformImage var59 = (PlatformImage)var56.getImageProperty(var58.image).get();
            if (var59 == null || !(var59 instanceof Image)) {
               continue;
            }

            var58.opaque = ((Image)var59).isOpaque();
         }

         if (var58.opaque) {
            if (var58.size.cover || var58.size.height == -1.0 && var58.size.width == -1.0 && var58.size.widthAsPercentage && var58.size.heightAsPercentage) {
               var7 = Double.isNaN(var7) ? 0.0 : Math.min(0.0, var7);
               var9 = Double.isNaN(var9) ? 0.0 : Math.min(0.0, var9);
               var11 = Double.isNaN(var11) ? 0.0 : Math.min(0.0, var11);
               var13 = Double.isNaN(var13) ? 0.0 : Math.min(0.0, var13);
               break;
            }

            if (var58.repeatX != BackgroundRepeat.SPACE && var58.repeatY != BackgroundRepeat.SPACE) {
               boolean var60 = var58.repeatX == BackgroundRepeat.REPEAT || var58.repeatX == BackgroundRepeat.ROUND;
               boolean var61 = var58.repeatY == BackgroundRepeat.REPEAT || var58.repeatY == BackgroundRepeat.ROUND;
               if (var60 && var61) {
                  var7 = Double.isNaN(var7) ? 0.0 : Math.min(0.0, var7);
                  var9 = Double.isNaN(var9) ? 0.0 : Math.min(0.0, var9);
                  var11 = Double.isNaN(var11) ? 0.0 : Math.min(0.0, var11);
                  var13 = Double.isNaN(var13) ? 0.0 : Math.min(0.0, var13);
                  break;
               }

               double var20 = var58.size.widthAsPercentage ? var58.size.width * var1 : var58.size.width;
               double var22 = var58.size.heightAsPercentage ? var58.size.height * var3 : var58.size.height;
               double var24 = var58.image.getWidth();
               double var26 = var58.image.getHeight();
               if (var58.size.contain) {
                  var32 = var1 / var24;
                  var34 = var3 / var26;
                  var36 = Math.min(var32, var34);
                  var28 = Math.ceil(var36 * var24);
                  var30 = Math.ceil(var36 * var26);
               } else if (var58.size.width >= 0.0 && var58.size.height >= 0.0) {
                  var28 = var20;
                  var30 = var22;
               } else if (var20 >= 0.0) {
                  var28 = var20;
                  var32 = var20 / var24;
                  var30 = var26 * var32;
               } else if (var22 >= 0.0) {
                  var30 = var22;
                  var32 = var22 / var26;
                  var28 = var24 * var32;
               } else {
                  var28 = var24;
                  var30 = var26;
               }

               var7 = Double.isNaN(var7) ? 0.0 : Math.min(0.0, var7);
               var9 = Double.isNaN(var9) ? var1 - var28 : Math.min(var1 - var28, var9);
               var11 = Double.isNaN(var11) ? var3 - var30 : Math.min(var3 - var30, var11);
               var13 = Double.isNaN(var13) ? 0.0 : Math.min(0.0, var13);
            } else {
               var58.opaque = false;
            }
         }
      }

      var6[0] = var7;
      var6[1] = var9;
      var6[2] = var11;
      var6[3] = var13;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         Background var2 = (Background)var1;
         if (this.hash != var2.hash) {
            return false;
         } else if (!this.fills.equals(var2.fills)) {
            return false;
         } else {
            return this.images.equals(var2.images);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hash;
   }

   static {
      BACKGROUND_COLOR = new SubCssMetaData("-fx-background-color", PaintConverter.SequenceConverter.getInstance(), new Paint[]{Color.TRANSPARENT});
      BACKGROUND_RADIUS = new SubCssMetaData("-fx-background-radius", CornerRadiiConverter.getInstance(), new CornerRadii[]{CornerRadii.EMPTY});
      BACKGROUND_INSETS = new SubCssMetaData("-fx-background-insets", InsetsConverter.SequenceConverter.getInstance(), new Insets[]{Insets.EMPTY});
      BACKGROUND_IMAGE = new SubCssMetaData("-fx-background-image", URLConverter.SequenceConverter.getInstance());
      BACKGROUND_REPEAT = new SubCssMetaData("-fx-background-repeat", RepeatStructConverter.getInstance(), new RepeatStruct[]{new RepeatStruct(BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT)});
      BACKGROUND_POSITION = new SubCssMetaData("-fx-background-position", LayeredBackgroundPositionConverter.getInstance(), new BackgroundPosition[]{BackgroundPosition.DEFAULT});
      BACKGROUND_SIZE = new SubCssMetaData("-fx-background-size", LayeredBackgroundSizeConverter.getInstance(), new BackgroundSize[]{BackgroundSize.DEFAULT});
      STYLEABLES = Collections.unmodifiableList(Arrays.asList(BACKGROUND_COLOR, BACKGROUND_INSETS, BACKGROUND_RADIUS, BACKGROUND_IMAGE, BACKGROUND_REPEAT, BACKGROUND_POSITION, BACKGROUND_SIZE));
      EMPTY = new Background((BackgroundFill[])null, (BackgroundImage[])null);
   }
}
