package javafx.scene.layout;

import javafx.geometry.Pos;

/** @deprecated */
@Deprecated
public class StackPaneBuilder extends PaneBuilder {
   private boolean __set;
   private Pos alignment;

   protected StackPaneBuilder() {
   }

   public static StackPaneBuilder create() {
      return new StackPaneBuilder();
   }

   public void applyTo(StackPane var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setAlignment(this.alignment);
      }

   }

   public StackPaneBuilder alignment(Pos var1) {
      this.alignment = var1;
      this.__set = true;
      return this;
   }

   public StackPane build() {
      StackPane var1 = new StackPane();
      this.applyTo(var1);
      return var1;
   }
}
