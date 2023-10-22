package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.TwoLevelFocusPopupBehavior;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.Skin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

public class ContextMenuSkin implements Skin {
   private ContextMenu popupMenu;
   private final Region root;
   private TwoLevelFocusPopupBehavior tlFocus;
   private final EventHandler keyListener = new EventHandler() {
      public void handle(KeyEvent var1) {
         if (var1.getEventType() == KeyEvent.KEY_PRESSED) {
            if (ContextMenuSkin.this.root.isFocused()) {
               KeyCode var2 = var1.getCode();
               switch (var2) {
                  case ENTER:
                  case SPACE:
                     ContextMenuSkin.this.popupMenu.hide();
                     return;
                  default:
               }
            }
         }
      }
   };

   public ContextMenuSkin(final ContextMenu var1) {
      this.popupMenu = var1;
      var1.addEventHandler(Menu.ON_SHOWN, new EventHandler() {
         public void handle(Event var1x) {
            Node var2 = var1.getSkin().getNode();
            if (var2 != null) {
               var2.requestFocus();
               if (var2 instanceof ContextMenuContent) {
                  VBox var3 = ((ContextMenuContent)var2).getItemsContainer();
                  var3.notifyAccessibleAttributeChanged(AccessibleAttribute.VISIBLE);
               }
            }

            ContextMenuSkin.this.root.addEventHandler(KeyEvent.KEY_PRESSED, ContextMenuSkin.this.keyListener);
         }
      });
      var1.addEventHandler(Menu.ON_HIDDEN, new EventHandler() {
         public void handle(Event var1x) {
            Node var2 = var1.getSkin().getNode();
            if (var2 != null) {
               var2.requestFocus();
            }

            ContextMenuSkin.this.root.removeEventHandler(KeyEvent.KEY_PRESSED, ContextMenuSkin.this.keyListener);
         }
      });
      var1.addEventFilter(WindowEvent.WINDOW_HIDING, new EventHandler() {
         public void handle(Event var1x) {
            Node var2 = var1.getSkin().getNode();
            if (var2 instanceof ContextMenuContent) {
               VBox var3 = ((ContextMenuContent)var2).getItemsContainer();
               var3.notifyAccessibleAttributeChanged(AccessibleAttribute.VISIBLE);
            }

         }
      });
      if (BehaviorSkinBase.IS_TOUCH_SUPPORTED && var1.getStyleClass().contains("text-input-context-menu")) {
         this.root = new EmbeddedTextContextMenuContent(var1);
      } else {
         this.root = new ContextMenuContent(var1);
      }

      this.root.idProperty().bind(var1.idProperty());
      this.root.styleProperty().bind(var1.styleProperty());
      this.root.getStyleClass().addAll(var1.getStyleClass());
      if (Utils.isTwoLevelFocus()) {
         this.tlFocus = new TwoLevelFocusPopupBehavior(var1);
      }

   }

   public ContextMenu getSkinnable() {
      return this.popupMenu;
   }

   public Node getNode() {
      return this.root;
   }

   public void dispose() {
      this.root.idProperty().unbind();
      this.root.styleProperty().unbind();
      if (this.tlFocus != null) {
         this.tlFocus.dispose();
      }

   }
}
