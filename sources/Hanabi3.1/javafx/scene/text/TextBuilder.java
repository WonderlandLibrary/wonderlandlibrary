package javafx.scene.text;

import javafx.geometry.VPos;
import javafx.scene.shape.ShapeBuilder;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class TextBuilder extends ShapeBuilder implements Builder {
   private int __set;
   private TextBoundsType boundsType;
   private Font font;
   private FontSmoothingType fontSmoothingType;
   private boolean impl_caretBias;
   private int impl_caretPosition;
   private int impl_selectionEnd;
   private int impl_selectionStart;
   private boolean strikethrough;
   private String text;
   private TextAlignment textAlignment;
   private VPos textOrigin;
   private boolean underline;
   private double wrappingWidth;
   private double x;
   private double y;

   protected TextBuilder() {
   }

   public static TextBuilder create() {
      return new TextBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(Text var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setBoundsType(this.boundsType);
               break;
            case 1:
               var1.setFont(this.font);
               break;
            case 2:
               var1.setFontSmoothingType(this.fontSmoothingType);
               break;
            case 3:
               var1.setImpl_caretBias(this.impl_caretBias);
               break;
            case 4:
               var1.setImpl_caretPosition(this.impl_caretPosition);
            case 5:
            case 7:
            default:
               break;
            case 6:
               var1.setImpl_selectionEnd(this.impl_selectionEnd);
               break;
            case 8:
               var1.setImpl_selectionStart(this.impl_selectionStart);
               break;
            case 9:
               var1.setStrikethrough(this.strikethrough);
               break;
            case 10:
               var1.setText(this.text);
               break;
            case 11:
               var1.setTextAlignment(this.textAlignment);
               break;
            case 12:
               var1.setTextOrigin(this.textOrigin);
               break;
            case 13:
               var1.setUnderline(this.underline);
               break;
            case 14:
               var1.setWrappingWidth(this.wrappingWidth);
               break;
            case 15:
               var1.setX(this.x);
               break;
            case 16:
               var1.setY(this.y);
         }
      }

   }

   public TextBuilder boundsType(TextBoundsType var1) {
      this.boundsType = var1;
      this.__set(0);
      return this;
   }

   public TextBuilder font(Font var1) {
      this.font = var1;
      this.__set(1);
      return this;
   }

   public TextBuilder fontSmoothingType(FontSmoothingType var1) {
      this.fontSmoothingType = var1;
      this.__set(2);
      return this;
   }

   /** @deprecated */
   @Deprecated
   public TextBuilder impl_caretBias(boolean var1) {
      this.impl_caretBias = var1;
      this.__set(3);
      return this;
   }

   /** @deprecated */
   @Deprecated
   public TextBuilder impl_caretPosition(int var1) {
      this.impl_caretPosition = var1;
      this.__set(4);
      return this;
   }

   /** @deprecated */
   @Deprecated
   public TextBuilder impl_selectionEnd(int var1) {
      this.impl_selectionEnd = var1;
      this.__set(6);
      return this;
   }

   /** @deprecated */
   @Deprecated
   public TextBuilder impl_selectionStart(int var1) {
      this.impl_selectionStart = var1;
      this.__set(8);
      return this;
   }

   public TextBuilder strikethrough(boolean var1) {
      this.strikethrough = var1;
      this.__set(9);
      return this;
   }

   public TextBuilder text(String var1) {
      this.text = var1;
      this.__set(10);
      return this;
   }

   public TextBuilder textAlignment(TextAlignment var1) {
      this.textAlignment = var1;
      this.__set(11);
      return this;
   }

   public TextBuilder textOrigin(VPos var1) {
      this.textOrigin = var1;
      this.__set(12);
      return this;
   }

   public TextBuilder underline(boolean var1) {
      this.underline = var1;
      this.__set(13);
      return this;
   }

   public TextBuilder wrappingWidth(double var1) {
      this.wrappingWidth = var1;
      this.__set(14);
      return this;
   }

   public TextBuilder x(double var1) {
      this.x = var1;
      this.__set(15);
      return this;
   }

   public TextBuilder y(double var1) {
      this.y = var1;
      this.__set(16);
      return this;
   }

   public Text build() {
      Text var1 = new Text();
      this.applyTo(var1);
      return var1;
   }
}
