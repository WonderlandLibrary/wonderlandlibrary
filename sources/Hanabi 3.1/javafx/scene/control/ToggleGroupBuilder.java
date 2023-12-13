package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ToggleGroupBuilder implements Builder {
   private boolean __set;
   private Collection toggles;

   protected ToggleGroupBuilder() {
   }

   public static ToggleGroupBuilder create() {
      return new ToggleGroupBuilder();
   }

   public void applyTo(ToggleGroup var1) {
      if (this.__set) {
         var1.getToggles().addAll(this.toggles);
      }

   }

   public ToggleGroupBuilder toggles(Collection var1) {
      this.toggles = var1;
      this.__set = true;
      return this;
   }

   public ToggleGroupBuilder toggles(Toggle... var1) {
      return this.toggles((Collection)Arrays.asList(var1));
   }

   public ToggleGroup build() {
      ToggleGroup var1 = new ToggleGroup();
      this.applyTo(var1);
      return var1;
   }
}
