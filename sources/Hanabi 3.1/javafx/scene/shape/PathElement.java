package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;
import com.sun.javafx.util.WeakReferenceQueue;
import java.util.Iterator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.scene.Node;

public abstract class PathElement {
   WeakReferenceQueue impl_nodes = new WeakReferenceQueue();
   private BooleanProperty absolute;

   void addNode(Node var1) {
      this.impl_nodes.add(var1);
   }

   void removeNode(Node var1) {
      this.impl_nodes.remove(var1);
   }

   void u() {
      Iterator var1 = this.impl_nodes.iterator();

      while(var1.hasNext()) {
         ((Path)var1.next()).markPathDirty();
      }

   }

   abstract void addTo(NGPath var1);

   /** @deprecated */
   @Deprecated
   public abstract void impl_addTo(Path2D var1);

   public final void setAbsolute(boolean var1) {
      this.absoluteProperty().set(var1);
   }

   public final boolean isAbsolute() {
      return this.absolute == null || this.absolute.get();
   }

   public final BooleanProperty absoluteProperty() {
      if (this.absolute == null) {
         this.absolute = new BooleanPropertyBase(true) {
            protected void invalidated() {
               PathElement.this.u();
            }

            public Object getBean() {
               return PathElement.this;
            }

            public String getName() {
               return "absolute";
            }
         };
      }

      return this.absolute;
   }
}
