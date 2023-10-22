package javafx.scene;

import java.util.Collection;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;

@DefaultProperty("children")
public class Group extends Parent {
   private BooleanProperty autoSizeChildren;

   public Group() {
   }

   public Group(Node... var1) {
      this.getChildren().addAll(var1);
   }

   public Group(Collection var1) {
      this.getChildren().addAll(var1);
   }

   public final void setAutoSizeChildren(boolean var1) {
      this.autoSizeChildrenProperty().set(var1);
   }

   public final boolean isAutoSizeChildren() {
      return this.autoSizeChildren == null ? true : this.autoSizeChildren.get();
   }

   public final BooleanProperty autoSizeChildrenProperty() {
      if (this.autoSizeChildren == null) {
         this.autoSizeChildren = new BooleanPropertyBase(true) {
            protected void invalidated() {
               Group.this.requestLayout();
            }

            public Object getBean() {
               return Group.this;
            }

            public String getName() {
               return "autoSizeChildren";
            }
         };
      }

      return this.autoSizeChildren;
   }

   public ObservableList getChildren() {
      return super.getChildren();
   }

   /** @deprecated */
   @Deprecated
   protected Bounds impl_computeLayoutBounds() {
      this.layout();
      return super.impl_computeLayoutBounds();
   }

   public double prefWidth(double var1) {
      if (this.isAutoSizeChildren()) {
         this.layout();
      }

      double var3 = this.getLayoutBounds().getWidth();
      return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
   }

   public double prefHeight(double var1) {
      if (this.isAutoSizeChildren()) {
         this.layout();
      }

      double var3 = this.getLayoutBounds().getHeight();
      return !Double.isNaN(var3) && !(var3 < 0.0) ? var3 : 0.0;
   }

   public double minHeight(double var1) {
      return this.prefHeight(var1);
   }

   public double minWidth(double var1) {
      return this.prefWidth(var1);
   }

   protected void layoutChildren() {
      if (this.isAutoSizeChildren()) {
         super.layoutChildren();
      }

   }
}
