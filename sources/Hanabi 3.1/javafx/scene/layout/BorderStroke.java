package javafx.scene.layout;

import javafx.beans.NamedArg;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;

public class BorderStroke {
   public static final BorderWidths THIN = new BorderWidths(1.0);
   public static final BorderWidths MEDIUM = new BorderWidths(3.0);
   public static final BorderWidths THICK = new BorderWidths(5.0);
   public static final BorderWidths DEFAULT_WIDTHS;
   final Paint topStroke;
   final Paint rightStroke;
   final Paint bottomStroke;
   final Paint leftStroke;
   final BorderStrokeStyle topStyle;
   final BorderStrokeStyle rightStyle;
   final BorderStrokeStyle bottomStyle;
   final BorderStrokeStyle leftStyle;
   final BorderWidths widths;
   final Insets insets;
   final Insets innerEdge;
   final Insets outerEdge;
   private final CornerRadii radii;
   private final boolean strokeUniform;
   private final int hash;

   public final Paint getTopStroke() {
      return this.topStroke;
   }

   public final Paint getRightStroke() {
      return this.rightStroke;
   }

   public final Paint getBottomStroke() {
      return this.bottomStroke;
   }

   public final Paint getLeftStroke() {
      return this.leftStroke;
   }

   public final BorderStrokeStyle getTopStyle() {
      return this.topStyle;
   }

   public final BorderStrokeStyle getRightStyle() {
      return this.rightStyle;
   }

   public final BorderStrokeStyle getBottomStyle() {
      return this.bottomStyle;
   }

   public final BorderStrokeStyle getLeftStyle() {
      return this.leftStyle;
   }

   public final BorderWidths getWidths() {
      return this.widths;
   }

   public final Insets getInsets() {
      return this.insets;
   }

   public final CornerRadii getRadii() {
      return this.radii;
   }

   public final boolean isStrokeUniform() {
      return this.strokeUniform;
   }

   public BorderStroke(@NamedArg("stroke") Paint var1, @NamedArg("style") BorderStrokeStyle var2, @NamedArg("radii") CornerRadii var3, @NamedArg("widths") BorderWidths var4) {
      this.leftStroke = this.topStroke = this.rightStroke = this.bottomStroke = (Paint)(var1 == null ? Color.BLACK : var1);
      this.topStyle = this.rightStyle = this.bottomStyle = this.leftStyle = var2 == null ? BorderStrokeStyle.NONE : var2;
      this.radii = var3 == null ? CornerRadii.EMPTY : var3;
      this.widths = var4 == null ? DEFAULT_WIDTHS : var4;
      this.insets = Insets.EMPTY;
      this.strokeUniform = this.widths.left == this.widths.top && this.widths.left == this.widths.right && this.widths.left == this.widths.bottom;
      this.innerEdge = new Insets(this.computeInside(this.topStyle.getType(), this.widths.getTop()), this.computeInside(this.rightStyle.getType(), this.widths.getRight()), this.computeInside(this.bottomStyle.getType(), this.widths.getBottom()), this.computeInside(this.leftStyle.getType(), this.widths.getLeft()));
      this.outerEdge = new Insets(Math.max(0.0, this.computeOutside(this.topStyle.getType(), this.widths.getTop())), Math.max(0.0, this.computeOutside(this.rightStyle.getType(), this.widths.getRight())), Math.max(0.0, this.computeOutside(this.bottomStyle.getType(), this.widths.getBottom())), Math.max(0.0, this.computeOutside(this.leftStyle.getType(), this.widths.getLeft())));
      this.hash = this.preComputeHash();
   }

   public BorderStroke(@NamedArg("stroke") Paint var1, @NamedArg("style") BorderStrokeStyle var2, @NamedArg("radii") CornerRadii var3, @NamedArg("widths") BorderWidths var4, @NamedArg("insets") Insets var5) {
      this(var1, var1, var1, var1, var2, var2, var2, var2, var3, var4, var5);
   }

   public BorderStroke(@NamedArg("topStroke") Paint var1, @NamedArg("rightStroke") Paint var2, @NamedArg("bottomStroke") Paint var3, @NamedArg("leftStroke") Paint var4, @NamedArg("topStyle") BorderStrokeStyle var5, @NamedArg("rightStyle") BorderStrokeStyle var6, @NamedArg("bottomStyle") BorderStrokeStyle var7, @NamedArg("leftStyle") BorderStrokeStyle var8, @NamedArg("radii") CornerRadii var9, @NamedArg("widths") BorderWidths var10, @NamedArg("insets") Insets var11) {
      this.topStroke = (Paint)(var1 == null ? Color.BLACK : var1);
      this.rightStroke = var2 == null ? this.topStroke : var2;
      this.bottomStroke = var3 == null ? this.topStroke : var3;
      this.leftStroke = var4 == null ? this.rightStroke : var4;
      this.topStyle = var5 == null ? BorderStrokeStyle.NONE : var5;
      this.rightStyle = var6 == null ? this.topStyle : var6;
      this.bottomStyle = var7 == null ? this.topStyle : var7;
      this.leftStyle = var8 == null ? this.rightStyle : var8;
      this.radii = var9 == null ? CornerRadii.EMPTY : var9;
      this.widths = var10 == null ? DEFAULT_WIDTHS : var10;
      this.insets = var11 == null ? Insets.EMPTY : var11;
      boolean var12 = this.leftStroke.equals(this.topStroke) && this.leftStroke.equals(this.rightStroke) && this.leftStroke.equals(this.bottomStroke);
      boolean var13 = this.widths.left == this.widths.top && this.widths.left == this.widths.right && this.widths.left == this.widths.bottom;
      boolean var14 = this.leftStyle.equals(this.topStyle) && this.leftStyle.equals(this.rightStyle) && this.leftStyle.equals(this.bottomStyle);
      this.strokeUniform = var12 && var13 && var14;
      this.innerEdge = new Insets(this.insets.getTop() + this.computeInside(this.topStyle.getType(), this.widths.getTop()), this.insets.getRight() + this.computeInside(this.rightStyle.getType(), this.widths.getRight()), this.insets.getBottom() + this.computeInside(this.bottomStyle.getType(), this.widths.getBottom()), this.insets.getLeft() + this.computeInside(this.leftStyle.getType(), this.widths.getLeft()));
      this.outerEdge = new Insets(Math.max(0.0, this.computeOutside(this.topStyle.getType(), this.widths.getTop()) - this.insets.getTop()), Math.max(0.0, this.computeOutside(this.rightStyle.getType(), this.widths.getRight()) - this.insets.getRight()), Math.max(0.0, this.computeOutside(this.bottomStyle.getType(), this.widths.getBottom()) - this.insets.getBottom()), Math.max(0.0, this.computeOutside(this.leftStyle.getType(), this.widths.getLeft()) - this.insets.getLeft()));
      this.hash = this.preComputeHash();
   }

   private int preComputeHash() {
      int var1 = this.topStroke.hashCode();
      var1 = 31 * var1 + this.rightStroke.hashCode();
      var1 = 31 * var1 + this.bottomStroke.hashCode();
      var1 = 31 * var1 + this.leftStroke.hashCode();
      var1 = 31 * var1 + this.topStyle.hashCode();
      var1 = 31 * var1 + this.rightStyle.hashCode();
      var1 = 31 * var1 + this.bottomStyle.hashCode();
      var1 = 31 * var1 + this.leftStyle.hashCode();
      var1 = 31 * var1 + this.widths.hashCode();
      var1 = 31 * var1 + this.radii.hashCode();
      var1 = 31 * var1 + this.insets.hashCode();
      return var1;
   }

   private double computeInside(StrokeType var1, double var2) {
      if (var1 == StrokeType.OUTSIDE) {
         return 0.0;
      } else if (var1 == StrokeType.CENTERED) {
         return var2 / 2.0;
      } else if (var1 == StrokeType.INSIDE) {
         return var2;
      } else {
         throw new AssertionError("Unexpected Stroke Type");
      }
   }

   private double computeOutside(StrokeType var1, double var2) {
      if (var1 == StrokeType.OUTSIDE) {
         return var2;
      } else if (var1 == StrokeType.CENTERED) {
         return var2 / 2.0;
      } else if (var1 == StrokeType.INSIDE) {
         return 0.0;
      } else {
         throw new AssertionError("Unexpected Stroke Type");
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         BorderStroke var2 = (BorderStroke)var1;
         if (this.hash != var2.hash) {
            return false;
         } else if (!this.bottomStroke.equals(var2.bottomStroke)) {
            return false;
         } else if (!this.bottomStyle.equals(var2.bottomStyle)) {
            return false;
         } else if (!this.leftStroke.equals(var2.leftStroke)) {
            return false;
         } else if (!this.leftStyle.equals(var2.leftStyle)) {
            return false;
         } else if (!this.radii.equals(var2.radii)) {
            return false;
         } else if (!this.rightStroke.equals(var2.rightStroke)) {
            return false;
         } else if (!this.rightStyle.equals(var2.rightStyle)) {
            return false;
         } else if (!this.topStroke.equals(var2.topStroke)) {
            return false;
         } else if (!this.topStyle.equals(var2.topStyle)) {
            return false;
         } else if (!this.widths.equals(var2.widths)) {
            return false;
         } else {
            return this.insets.equals(var2.insets);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.hash;
   }

   static {
      DEFAULT_WIDTHS = THIN;
   }
}
