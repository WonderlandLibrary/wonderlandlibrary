package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.stage.PopupWindowBuilder;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class PopupControlBuilder extends PopupWindowBuilder implements Builder {
   private int __set;
   private String id;
   private double maxHeight;
   private double maxWidth;
   private double minHeight;
   private double minWidth;
   private double prefHeight;
   private double prefWidth;
   private Skin skin;
   private String style;
   private Collection styleClass;

   protected PopupControlBuilder() {
   }

   public static PopupControlBuilder create() {
      return new PopupControlBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(PopupControl var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setId(this.id);
               break;
            case 1:
               var1.setMaxHeight(this.maxHeight);
               break;
            case 2:
               var1.setMaxWidth(this.maxWidth);
               break;
            case 3:
               var1.setMinHeight(this.minHeight);
               break;
            case 4:
               var1.setMinWidth(this.minWidth);
               break;
            case 5:
               var1.setPrefHeight(this.prefHeight);
               break;
            case 6:
               var1.setPrefWidth(this.prefWidth);
               break;
            case 7:
               var1.setSkin(this.skin);
               break;
            case 8:
               var1.setStyle(this.style);
               break;
            case 9:
               var1.getStyleClass().addAll(this.styleClass);
         }
      }

   }

   public PopupControlBuilder id(String var1) {
      this.id = var1;
      this.__set(0);
      return this;
   }

   public PopupControlBuilder maxHeight(double var1) {
      this.maxHeight = var1;
      this.__set(1);
      return this;
   }

   public PopupControlBuilder maxWidth(double var1) {
      this.maxWidth = var1;
      this.__set(2);
      return this;
   }

   public PopupControlBuilder minHeight(double var1) {
      this.minHeight = var1;
      this.__set(3);
      return this;
   }

   public PopupControlBuilder minWidth(double var1) {
      this.minWidth = var1;
      this.__set(4);
      return this;
   }

   public PopupControlBuilder prefHeight(double var1) {
      this.prefHeight = var1;
      this.__set(5);
      return this;
   }

   public PopupControlBuilder prefWidth(double var1) {
      this.prefWidth = var1;
      this.__set(6);
      return this;
   }

   public PopupControlBuilder skin(Skin var1) {
      this.skin = var1;
      this.__set(7);
      return this;
   }

   public PopupControlBuilder style(String var1) {
      this.style = var1;
      this.__set(8);
      return this;
   }

   public PopupControlBuilder styleClass(Collection var1) {
      this.styleClass = var1;
      this.__set(9);
      return this;
   }

   public PopupControlBuilder styleClass(String... var1) {
      return this.styleClass((Collection)Arrays.asList(var1));
   }

   public PopupControl build() {
      PopupControl var1 = new PopupControl();
      this.applyTo(var1);
      return var1;
   }
}
