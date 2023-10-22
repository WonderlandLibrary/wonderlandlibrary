package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.EventHandler;

/** @deprecated */
@Deprecated
public class ContextMenuBuilder extends PopupControlBuilder {
   private int __set;
   private boolean impl_showRelativeToWindow;
   private Collection items;
   private EventHandler onAction;

   protected ContextMenuBuilder() {
   }

   public static ContextMenuBuilder create() {
      return new ContextMenuBuilder();
   }

   public void applyTo(ContextMenu var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setImpl_showRelativeToWindow(this.impl_showRelativeToWindow);
      }

      if ((var2 & 2) != 0) {
         var1.getItems().addAll(this.items);
      }

      if ((var2 & 4) != 0) {
         var1.setOnAction(this.onAction);
      }

   }

   /** @deprecated */
   @Deprecated
   public ContextMenuBuilder impl_showRelativeToWindow(boolean var1) {
      this.impl_showRelativeToWindow = var1;
      this.__set |= 1;
      return this;
   }

   public ContextMenuBuilder items(Collection var1) {
      this.items = var1;
      this.__set |= 2;
      return this;
   }

   public ContextMenuBuilder items(MenuItem... var1) {
      return this.items((Collection)Arrays.asList(var1));
   }

   public ContextMenuBuilder onAction(EventHandler var1) {
      this.onAction = var1;
      this.__set |= 4;
      return this;
   }

   public ContextMenu build() {
      ContextMenu var1 = new ContextMenu();
      this.applyTo(var1);
      return var1;
   }
}
