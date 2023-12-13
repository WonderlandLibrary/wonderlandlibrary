package javafx.scene.control;

import javafx.scene.ParentBuilder;

/** @deprecated */
@Deprecated
public abstract class ControlBuilder extends ParentBuilder {
   private int __set;
   private ContextMenu contextMenu;
   private double maxHeight;
   private double maxWidth;
   private double minHeight;
   private double minWidth;
   private double prefHeight;
   private double prefWidth;
   private Skin skin;
   private Tooltip tooltip;

   protected ControlBuilder() {
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(Control var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setContextMenu(this.contextMenu);
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
               var1.setTooltip(this.tooltip);
         }
      }

   }

   public ControlBuilder contextMenu(ContextMenu var1) {
      this.contextMenu = var1;
      this.__set(0);
      return this;
   }

   public ControlBuilder maxHeight(double var1) {
      this.maxHeight = var1;
      this.__set(1);
      return this;
   }

   public ControlBuilder maxWidth(double var1) {
      this.maxWidth = var1;
      this.__set(2);
      return this;
   }

   public ControlBuilder minHeight(double var1) {
      this.minHeight = var1;
      this.__set(3);
      return this;
   }

   public ControlBuilder minWidth(double var1) {
      this.minWidth = var1;
      this.__set(4);
      return this;
   }

   public ControlBuilder prefHeight(double var1) {
      this.prefHeight = var1;
      this.__set(5);
      return this;
   }

   public ControlBuilder prefWidth(double var1) {
      this.prefWidth = var1;
      this.__set(6);
      return this;
   }

   public ControlBuilder skin(Skin var1) {
      this.skin = var1;
      this.__set(7);
      return this;
   }

   public ControlBuilder tooltip(Tooltip var1) {
      this.tooltip = var1;
      this.__set(8);
      return this;
   }
}
