package javafx.scene.control;

import javafx.scene.Node;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class LabelBuilder extends LabeledBuilder implements Builder {
   private boolean __set;
   private Node labelFor;

   protected LabelBuilder() {
   }

   public static LabelBuilder create() {
      return new LabelBuilder();
   }

   public void applyTo(Label var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setLabelFor(this.labelFor);
      }

   }

   public LabelBuilder labelFor(Node var1) {
      this.labelFor = var1;
      this.__set = true;
      return this;
   }

   public Label build() {
      Label var1 = new Label();
      this.applyTo(var1);
      return var1;
   }
}
