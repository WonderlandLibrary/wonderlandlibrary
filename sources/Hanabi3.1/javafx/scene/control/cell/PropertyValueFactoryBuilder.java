package javafx.scene.control.cell;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class PropertyValueFactoryBuilder implements Builder {
   private String property;

   protected PropertyValueFactoryBuilder() {
   }

   public static PropertyValueFactoryBuilder create() {
      return new PropertyValueFactoryBuilder();
   }

   public PropertyValueFactoryBuilder property(String var1) {
      this.property = var1;
      return this;
   }

   public PropertyValueFactory build() {
      PropertyValueFactory var1 = new PropertyValueFactory(this.property);
      return var1;
   }
}
