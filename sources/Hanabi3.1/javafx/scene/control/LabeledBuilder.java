package javafx.scene.control;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/** @deprecated */
@Deprecated
public abstract class LabeledBuilder extends ControlBuilder {
   private int __set;
   private Pos alignment;
   private ContentDisplay contentDisplay;
   private String ellipsisString;
   private Font font;
   private Node graphic;
   private double graphicTextGap;
   private boolean mnemonicParsing;
   private String text;
   private TextAlignment textAlignment;
   private Paint textFill;
   private OverrunStyle textOverrun;
   private boolean underline;
   private boolean wrapText;

   protected LabeledBuilder() {
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(Labeled var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setAlignment(this.alignment);
               break;
            case 1:
               var1.setContentDisplay(this.contentDisplay);
               break;
            case 2:
               var1.setEllipsisString(this.ellipsisString);
               break;
            case 3:
               var1.setFont(this.font);
               break;
            case 4:
               var1.setGraphic(this.graphic);
               break;
            case 5:
               var1.setGraphicTextGap(this.graphicTextGap);
               break;
            case 6:
               var1.setMnemonicParsing(this.mnemonicParsing);
               break;
            case 7:
               var1.setText(this.text);
               break;
            case 8:
               var1.setTextAlignment(this.textAlignment);
               break;
            case 9:
               var1.setTextFill(this.textFill);
               break;
            case 10:
               var1.setTextOverrun(this.textOverrun);
               break;
            case 11:
               var1.setUnderline(this.underline);
               break;
            case 12:
               var1.setWrapText(this.wrapText);
         }
      }

   }

   public LabeledBuilder alignment(Pos var1) {
      this.alignment = var1;
      this.__set(0);
      return this;
   }

   public LabeledBuilder contentDisplay(ContentDisplay var1) {
      this.contentDisplay = var1;
      this.__set(1);
      return this;
   }

   public LabeledBuilder ellipsisString(String var1) {
      this.ellipsisString = var1;
      this.__set(2);
      return this;
   }

   public LabeledBuilder font(Font var1) {
      this.font = var1;
      this.__set(3);
      return this;
   }

   public LabeledBuilder graphic(Node var1) {
      this.graphic = var1;
      this.__set(4);
      return this;
   }

   public LabeledBuilder graphicTextGap(double var1) {
      this.graphicTextGap = var1;
      this.__set(5);
      return this;
   }

   public LabeledBuilder mnemonicParsing(boolean var1) {
      this.mnemonicParsing = var1;
      this.__set(6);
      return this;
   }

   public LabeledBuilder text(String var1) {
      this.text = var1;
      this.__set(7);
      return this;
   }

   public LabeledBuilder textAlignment(TextAlignment var1) {
      this.textAlignment = var1;
      this.__set(8);
      return this;
   }

   public LabeledBuilder textFill(Paint var1) {
      this.textFill = var1;
      this.__set(9);
      return this;
   }

   public LabeledBuilder textOverrun(OverrunStyle var1) {
      this.textOverrun = var1;
      this.__set(10);
      return this;
   }

   public LabeledBuilder underline(boolean var1) {
      this.underline = var1;
      this.__set(11);
      return this;
   }

   public LabeledBuilder wrapText(boolean var1) {
      this.wrapText = var1;
      this.__set(12);
      return this;
   }
}
