package javafx.scene.layout;

import javafx.beans.DefaultProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;

@DefaultProperty("children")
public class Pane extends Region {
   static void setConstraint(Node var0, Object var1, Object var2) {
      if (var2 == null) {
         var0.getProperties().remove(var1);
      } else {
         var0.getProperties().put(var1, var2);
      }

      if (var0.getParent() != null) {
         var0.getParent().requestLayout();
      }

   }

   static Object getConstraint(Node var0, Object var1) {
      if (var0.hasProperties()) {
         Object var2 = var0.getProperties().get(var1);
         if (var2 != null) {
            return var2;
         }
      }

      return null;
   }

   public Pane() {
   }

   public Pane(Node... var1) {
      this.getChildren().addAll(var1);
   }

   public ObservableList getChildren() {
      return super.getChildren();
   }
}
