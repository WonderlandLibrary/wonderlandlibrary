package javafx.scene.control;

import javafx.scene.Node;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class TitledPaneBuilder extends LabeledBuilder implements Builder {
   private int __set;
   private boolean animated;
   private boolean collapsible;
   private Node content;
   private boolean expanded;

   protected TitledPaneBuilder() {
   }

   public static TitledPaneBuilder create() {
      return new TitledPaneBuilder();
   }

   public void applyTo(TitledPane var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAnimated(this.animated);
      }

      if ((var2 & 2) != 0) {
         var1.setCollapsible(this.collapsible);
      }

      if ((var2 & 4) != 0) {
         var1.setContent(this.content);
      }

      if ((var2 & 8) != 0) {
         var1.setExpanded(this.expanded);
      }

   }

   public TitledPaneBuilder animated(boolean var1) {
      this.animated = var1;
      this.__set |= 1;
      return this;
   }

   public TitledPaneBuilder collapsible(boolean var1) {
      this.collapsible = var1;
      this.__set |= 2;
      return this;
   }

   public TitledPaneBuilder content(Node var1) {
      this.content = var1;
      this.__set |= 4;
      return this;
   }

   public TitledPaneBuilder expanded(boolean var1) {
      this.expanded = var1;
      this.__set |= 8;
      return this;
   }

   public TitledPane build() {
      TitledPane var1 = new TitledPane();
      this.applyTo(var1);
      return var1;
   }
}
