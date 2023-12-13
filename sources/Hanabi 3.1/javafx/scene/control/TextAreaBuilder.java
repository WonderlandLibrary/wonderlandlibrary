package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class TextAreaBuilder extends TextInputControlBuilder implements Builder {
   private int __set;
   private Collection paragraphs;
   private int prefColumnCount;
   private int prefRowCount;
   private String promptText;
   private double scrollLeft;
   private double scrollTop;
   private boolean wrapText;

   protected TextAreaBuilder() {
   }

   public static TextAreaBuilder create() {
      return new TextAreaBuilder();
   }

   public void applyTo(TextArea var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.getParagraphs().addAll(this.paragraphs);
      }

      if ((var2 & 2) != 0) {
         var1.setPrefColumnCount(this.prefColumnCount);
      }

      if ((var2 & 4) != 0) {
         var1.setPrefRowCount(this.prefRowCount);
      }

      if ((var2 & 8) != 0) {
         var1.setPromptText(this.promptText);
      }

      if ((var2 & 16) != 0) {
         var1.setScrollLeft(this.scrollLeft);
      }

      if ((var2 & 32) != 0) {
         var1.setScrollTop(this.scrollTop);
      }

      if ((var2 & 64) != 0) {
         var1.setWrapText(this.wrapText);
      }

   }

   public TextAreaBuilder paragraphs(Collection var1) {
      this.paragraphs = var1;
      this.__set |= 1;
      return this;
   }

   public TextAreaBuilder paragraphs(CharSequence... var1) {
      return this.paragraphs((Collection)Arrays.asList(var1));
   }

   public TextAreaBuilder prefColumnCount(int var1) {
      this.prefColumnCount = var1;
      this.__set |= 2;
      return this;
   }

   public TextAreaBuilder prefRowCount(int var1) {
      this.prefRowCount = var1;
      this.__set |= 4;
      return this;
   }

   public TextAreaBuilder promptText(String var1) {
      this.promptText = var1;
      this.__set |= 8;
      return this;
   }

   public TextAreaBuilder scrollLeft(double var1) {
      this.scrollLeft = var1;
      this.__set |= 16;
      return this;
   }

   public TextAreaBuilder scrollTop(double var1) {
      this.scrollTop = var1;
      this.__set |= 32;
      return this;
   }

   public TextAreaBuilder wrapText(boolean var1) {
      this.wrapText = var1;
      this.__set |= 64;
      return this;
   }

   public TextArea build() {
      TextArea var1 = new TextArea();
      this.applyTo(var1);
      return var1;
   }
}
