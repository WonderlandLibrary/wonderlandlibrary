package javafx.scene.layout;

import com.sun.javafx.util.WeakReferenceQueue;
import java.util.Iterator;
import javafx.scene.Parent;

public abstract class ConstraintsBase {
   public static final double CONSTRAIN_TO_PREF = Double.NEGATIVE_INFINITY;
   WeakReferenceQueue impl_nodes = new WeakReferenceQueue();

   ConstraintsBase() {
   }

   void add(Parent var1) {
      this.impl_nodes.add(var1);
   }

   void remove(Parent var1) {
      this.impl_nodes.remove(var1);
   }

   protected void requestLayout() {
      Iterator var1 = this.impl_nodes.iterator();

      while(var1.hasNext()) {
         ((Parent)var1.next()).requestLayout();
      }

   }
}
