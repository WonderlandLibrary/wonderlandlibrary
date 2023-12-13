package javafx.scene.control;

import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.scene.control.skin.ContextMenuSkin;
import com.sun.javafx.util.Utils;
import java.util.Iterator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Window;

@IDProperty("id")
public class ContextMenu extends PopupControl {
   private ObjectProperty onAction;
   private final ObservableList items;
   /** @deprecated */
   @Deprecated
   private final BooleanProperty impl_showRelativeToWindow;
   private static final String DEFAULT_STYLE_CLASS = "context-menu";

   public ContextMenu() {
      this.onAction = new ObjectPropertyBase() {
         protected void invalidated() {
            ContextMenu.this.setEventHandler(ActionEvent.ACTION, (EventHandler)this.get());
         }

         public Object getBean() {
            return ContextMenu.this;
         }

         public String getName() {
            return "onAction";
         }
      };
      this.items = new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            while(var1.next()) {
               Iterator var2 = var1.getRemoved().iterator();

               MenuItem var3;
               while(var2.hasNext()) {
                  var3 = (MenuItem)var2.next();
                  var3.setParentPopup((ContextMenu)null);
               }

               for(var2 = var1.getAddedSubList().iterator(); var2.hasNext(); var3.setParentPopup(ContextMenu.this)) {
                  var3 = (MenuItem)var2.next();
                  if (var3.getParentPopup() != null) {
                     var3.getParentPopup().getItems().remove(var3);
                  }
               }
            }

         }
      };
      this.impl_showRelativeToWindow = new SimpleBooleanProperty(false);
      this.getStyleClass().setAll((Object[])("context-menu"));
      this.setAutoHide(true);
      this.setConsumeAutoHidingEvents(false);
   }

   public ContextMenu(MenuItem... var1) {
      this();
      this.items.addAll(var1);
   }

   public final void setOnAction(EventHandler var1) {
      this.onActionProperty().set(var1);
   }

   public final EventHandler getOnAction() {
      return (EventHandler)this.onActionProperty().get();
   }

   public final ObjectProperty onActionProperty() {
      return this.onAction;
   }

   public final ObservableList getItems() {
      return this.items;
   }

   public final boolean isImpl_showRelativeToWindow() {
      return this.impl_showRelativeToWindow.get();
   }

   public final void setImpl_showRelativeToWindow(boolean var1) {
      this.impl_showRelativeToWindow.set(var1);
   }

   public final BooleanProperty impl_showRelativeToWindowProperty() {
      return this.impl_showRelativeToWindow;
   }

   public void show(Node var1, Side var2, double var3, double var5) {
      if (var1 != null) {
         if (this.getItems().size() != 0) {
            this.getScene().setNodeOrientation(var1.getEffectiveNodeOrientation());
            HPos var7 = var2 == Side.LEFT ? HPos.LEFT : (var2 == Side.RIGHT ? HPos.RIGHT : HPos.CENTER);
            VPos var8 = var2 == Side.TOP ? VPos.TOP : (var2 == Side.BOTTOM ? VPos.BOTTOM : VPos.CENTER);
            Point2D var9 = Utils.pointRelativeTo(var1, this.prefWidth(-1.0), this.prefHeight(-1.0), var7, var8, var3, var5, true);
            this.doShow(var1, var9.getX(), var9.getY());
         }
      }
   }

   public void show(Node var1, double var2, double var4) {
      if (var1 != null) {
         if (this.getItems().size() != 0) {
            this.getScene().setNodeOrientation(var1.getEffectiveNodeOrientation());
            this.doShow(var1, var2, var4);
         }
      }
   }

   private void doShow(Node var1, double var2, double var4) {
      Event.fireEvent(this, new Event(Menu.ON_SHOWING));
      if (this.isImpl_showRelativeToWindow()) {
         Scene var6 = var1 == null ? null : var1.getScene();
         Window var7 = var6 == null ? null : var6.getWindow();
         if (var7 == null) {
            return;
         }

         super.show(var7, var2, var4);
      } else {
         super.show(var1, var2, var4);
      }

      Event.fireEvent(this, new Event(Menu.ON_SHOWN));
   }

   public void hide() {
      if (this.isShowing()) {
         Event.fireEvent(this, new Event(Menu.ON_HIDING));
         super.hide();
         Event.fireEvent(this, new Event(Menu.ON_HIDDEN));
      }
   }

   protected Skin createDefaultSkin() {
      return new ContextMenuSkin(this);
   }
}
