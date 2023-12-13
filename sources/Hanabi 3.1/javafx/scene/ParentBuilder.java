package javafx.scene;

import java.util.Arrays;
import java.util.Collection;

/** @deprecated */
@Deprecated
public abstract class ParentBuilder extends NodeBuilder {
   private int __set;
   private Collection stylesheets;

   protected ParentBuilder() {
   }

   public void applyTo(Parent var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 2) != 0) {
         var1.getStylesheets().addAll(this.stylesheets);
      }

   }

   public ParentBuilder stylesheets(Collection var1) {
      this.stylesheets = var1;
      this.__set |= 2;
      return this;
   }

   public ParentBuilder stylesheets(String... var1) {
      return this.stylesheets((Collection)Arrays.asList(var1));
   }
}
