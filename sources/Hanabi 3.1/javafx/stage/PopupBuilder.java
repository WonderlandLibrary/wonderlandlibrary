package javafx.stage;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.Node;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class PopupBuilder extends PopupWindowBuilder implements Builder {
   private boolean __set;
   private Collection content;

   protected PopupBuilder() {
   }

   public static PopupBuilder create() {
      return new PopupBuilder();
   }

   public void applyTo(Popup var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.getContent().addAll(this.content);
      }

   }

   public PopupBuilder content(Collection var1) {
      this.content = var1;
      this.__set = true;
      return this;
   }

   public PopupBuilder content(Node... var1) {
      return this.content((Collection)Arrays.asList(var1));
   }

   public Popup build() {
      Popup var1 = new Popup();
      this.applyTo(var1);
      return var1;
   }
}
