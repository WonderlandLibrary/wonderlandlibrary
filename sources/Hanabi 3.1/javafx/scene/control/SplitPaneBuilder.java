package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class SplitPaneBuilder extends ControlBuilder implements Builder {
   private int __set;
   private double[] dividerPositions;
   private Collection items;
   private Orientation orientation;

   protected SplitPaneBuilder() {
   }

   public static SplitPaneBuilder create() {
      return new SplitPaneBuilder();
   }

   public void applyTo(SplitPane var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setDividerPositions(this.dividerPositions);
      }

      if ((var2 & 2) != 0) {
         var1.getItems().addAll(this.items);
      }

      if ((var2 & 4) != 0) {
         var1.setOrientation(this.orientation);
      }

   }

   public SplitPaneBuilder dividerPositions(double[] var1) {
      this.dividerPositions = var1;
      this.__set |= 1;
      return this;
   }

   public SplitPaneBuilder items(Collection var1) {
      this.items = var1;
      this.__set |= 2;
      return this;
   }

   public SplitPaneBuilder items(Node... var1) {
      return this.items((Collection)Arrays.asList(var1));
   }

   public SplitPaneBuilder orientation(Orientation var1) {
      this.orientation = var1;
      this.__set |= 4;
      return this;
   }

   public SplitPane build() {
      SplitPane var1 = new SplitPane();
      this.applyTo(var1);
      return var1;
   }
}
