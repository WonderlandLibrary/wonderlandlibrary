package javafx.scene.control;

import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/** @deprecated */
@Deprecated
public class TooltipBuilder extends PopupControlBuilder {
   private int __set;
   private ContentDisplay contentDisplay;
   private Font font;
   private Node graphic;
   private double graphicTextGap;
   private String text;
   private TextAlignment textAlignment;
   private OverrunStyle textOverrun;
   private boolean wrapText;

   protected TooltipBuilder() {
   }

   public static TooltipBuilder create() {
      return new TooltipBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(Tooltip var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setContentDisplay(this.contentDisplay);
               break;
            case 1:
               var1.setFont(this.font);
               break;
            case 2:
               var1.setGraphic(this.graphic);
               break;
            case 3:
               var1.setGraphicTextGap(this.graphicTextGap);
               break;
            case 4:
               var1.setText(this.text);
               break;
            case 5:
               var1.setTextAlignment(this.textAlignment);
               break;
            case 6:
               var1.setTextOverrun(this.textOverrun);
               break;
            case 7:
               var1.setWrapText(this.wrapText);
         }
      }

   }

   public TooltipBuilder contentDisplay(ContentDisplay var1) {
      this.contentDisplay = var1;
      this.__set(0);
      return this;
   }

   public TooltipBuilder font(Font var1) {
      this.font = var1;
      this.__set(1);
      return this;
   }

   public TooltipBuilder graphic(Node var1) {
      this.graphic = var1;
      this.__set(2);
      return this;
   }

   public TooltipBuilder graphicTextGap(double var1) {
      this.graphicTextGap = var1;
      this.__set(3);
      return this;
   }

   public TooltipBuilder text(String var1) {
      this.text = var1;
      this.__set(4);
      return this;
   }

   public TooltipBuilder textAlignment(TextAlignment var1) {
      this.textAlignment = var1;
      this.__set(5);
      return this;
   }

   public TooltipBuilder textOverrun(OverrunStyle var1) {
      this.textOverrun = var1;
      this.__set(6);
      return this;
   }

   public TooltipBuilder wrapText(boolean var1) {
      this.wrapText = var1;
      this.__set(7);
      return this;
   }

   public Tooltip build() {
      Tooltip var1 = new Tooltip();
      this.applyTo(var1);
      return var1;
   }
}
