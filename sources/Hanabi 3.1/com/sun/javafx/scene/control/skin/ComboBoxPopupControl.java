package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import com.sun.javafx.scene.traversal.Algorithm;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import com.sun.javafx.scene.traversal.TraversalContext;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.css.Styleable;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.scene.control.Skinnable;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

public abstract class ComboBoxPopupControl extends ComboBoxBaseSkin {
   protected PopupControl popup;
   public static final String COMBO_BOX_STYLE_CLASS = "combo-box-popup";
   private boolean popupNeedsReconfiguring = true;
   private final ComboBoxBase comboBoxBase;
   private TextField textField;
   private EventHandler textFieldMouseEventHandler = (var1x) -> {
      ComboBoxBase var2 = (ComboBoxBase)this.getSkinnable();
      if (!var1x.getTarget().equals(var2)) {
         var2.fireEvent(var1x.copyFor(var2, var2));
         var1x.consume();
      }

   };
   private EventHandler textFieldDragEventHandler = (var1x) -> {
      ComboBoxBase var2 = (ComboBoxBase)this.getSkinnable();
      if (!var1x.getTarget().equals(var2)) {
         var2.fireEvent(var1x.copyFor(var2, var2));
         var1x.consume();
      }

   };
   private String initialTextFieldValue = null;

   public ComboBoxPopupControl(ComboBoxBase var1, ComboBoxBaseBehavior var2) {
      super(var1, var2);
      this.comboBoxBase = var1;
      this.textField = this.getEditor() != null ? this.getEditableInputNode() : null;
      if (this.textField != null) {
         this.getChildren().add(this.textField);
      }

      var1.focusedProperty().addListener((var1x, var2x, var3) -> {
         if (this.getEditor() != null) {
            ((FakeFocusTextField)this.textField).setFakeFocus(var3);
            if (!var3) {
               this.setTextFromTextFieldIntoComboBoxValue();
            }
         }

      });
      var1.addEventFilter(KeyEvent.ANY, (var1x) -> {
         if (this.textField != null && this.getEditor() != null) {
            if (var1x.getTarget().equals(this.textField)) {
               return;
            }

            switch (var1x.getCode()) {
               case ESCAPE:
               case F10:
                  break;
               case ENTER:
                  this.handleKeyEvent(var1x, true);
                  break;
               default:
                  this.textField.fireEvent(var1x.copyFor(this.textField, this.textField));
                  var1x.consume();
            }
         } else {
            this.handleKeyEvent(var1x, false);
         }

      });
      if (var1.getOnInputMethodTextChanged() == null) {
         var1.setOnInputMethodTextChanged((var2x) -> {
            if (this.textField != null && this.getEditor() != null && var1.getScene().getFocusOwner() == var1 && this.textField.getOnInputMethodTextChanged() != null) {
               this.textField.getOnInputMethodTextChanged().handle(var2x);
            }

         });
      }

      var1.setImpl_traversalEngine(new ParentTraversalEngine(var1, new Algorithm() {
         public Node select(Node var1, Direction var2, TraversalContext var3) {
            return null;
         }

         public Node selectFirst(TraversalContext var1) {
            return null;
         }

         public Node selectLast(TraversalContext var1) {
            return null;
         }
      }));
      this.updateEditable();
   }

   protected abstract Node getPopupContent();

   protected PopupControl getPopup() {
      if (this.popup == null) {
         this.createPopup();
      }

      return this.popup;
   }

   public void show() {
      if (this.getSkinnable() == null) {
         throw new IllegalStateException("ComboBox is null");
      } else {
         Node var1 = this.getPopupContent();
         if (var1 == null) {
            throw new IllegalStateException("Popup node is null");
         } else if (!this.getPopup().isShowing()) {
            this.positionAndShowPopup();
         }
      }
   }

   public void hide() {
      if (this.popup != null && this.popup.isShowing()) {
         this.popup.hide();
      }

   }

   private Point2D getPrefPopupPosition() {
      return com.sun.javafx.util.Utils.pointRelativeTo(this.getSkinnable(), this.getPopupContent(), HPos.CENTER, VPos.BOTTOM, 0.0, 0.0, true);
   }

   private void positionAndShowPopup() {
      PopupControl var1 = this.getPopup();
      var1.getScene().setNodeOrientation(((ComboBoxBase)this.getSkinnable()).getEffectiveNodeOrientation());
      Node var2 = this.getPopupContent();
      this.sizePopup();
      Point2D var3 = this.getPrefPopupPosition();
      this.popupNeedsReconfiguring = true;
      this.reconfigurePopup();
      ComboBoxBase var4 = (ComboBoxBase)this.getSkinnable();
      var1.show(var4.getScene().getWindow(), this.snapPosition(var3.getX()), this.snapPosition(var3.getY()));
      var2.requestFocus();
      this.sizePopup();
   }

   private void sizePopup() {
      Node var1 = this.getPopupContent();
      if (var1 instanceof Region) {
         Region var2 = (Region)var1;
         double var3 = this.snapSize(var2.prefHeight(0.0));
         double var5 = this.snapSize(var2.minHeight(0.0));
         double var7 = this.snapSize(var2.maxHeight(0.0));
         double var9 = this.snapSize(Math.min(Math.max(var3, var5), Math.max(var5, var7)));
         double var11 = this.snapSize(var2.prefWidth(var9));
         double var13 = this.snapSize(var2.minWidth(var9));
         double var15 = this.snapSize(var2.maxWidth(var9));
         double var17 = this.snapSize(Math.min(Math.max(var11, var13), Math.max(var13, var15)));
         var1.resize(var17, var9);
      } else {
         var1.autosize();
      }

   }

   private void createPopup() {
      this.popup = new PopupControl() {
         {
            this.setSkin(new Skin() {
               public Skinnable getSkinnable() {
                  return ComboBoxPopupControl.this.getSkinnable();
               }

               public Node getNode() {
                  return ComboBoxPopupControl.this.getPopupContent();
               }

               public void dispose() {
               }
            });
         }

         public Styleable getStyleableParent() {
            return ComboBoxPopupControl.this.getSkinnable();
         }
      };
      this.popup.getStyleClass().add("combo-box-popup");
      this.popup.setConsumeAutoHidingEvents(false);
      this.popup.setAutoHide(true);
      this.popup.setAutoFix(true);
      this.popup.setHideOnEscape(true);
      this.popup.setOnAutoHide((var1x) -> {
         ((ComboBoxBaseBehavior)this.getBehavior()).onAutoHide();
      });
      this.popup.addEventHandler(MouseEvent.MOUSE_CLICKED, (var1x) -> {
         ((ComboBoxBaseBehavior)this.getBehavior()).onAutoHide();
      });
      this.popup.addEventHandler(WindowEvent.WINDOW_HIDDEN, (var1x) -> {
         ((ComboBoxBase)this.getSkinnable()).notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_NODE);
      });
      InvalidationListener var1 = (var1x) -> {
         this.popupNeedsReconfiguring = true;
         this.reconfigurePopup();
      };
      ((ComboBoxBase)this.getSkinnable()).layoutXProperty().addListener(var1);
      ((ComboBoxBase)this.getSkinnable()).layoutYProperty().addListener(var1);
      ((ComboBoxBase)this.getSkinnable()).widthProperty().addListener(var1);
      ((ComboBoxBase)this.getSkinnable()).heightProperty().addListener(var1);
      ((ComboBoxBase)this.getSkinnable()).sceneProperty().addListener((var1x) -> {
         if (((ObservableValue)var1x).getValue() == null) {
            this.hide();
         }

      });
   }

   void reconfigurePopup() {
      if (this.popup != null) {
         boolean var1 = this.popup.isShowing();
         if (var1) {
            if (this.popupNeedsReconfiguring) {
               this.popupNeedsReconfiguring = false;
               Point2D var2 = this.getPrefPopupPosition();
               Node var3 = this.getPopupContent();
               double var4 = var3.prefWidth(-1.0);
               double var6 = var3.prefHeight(-1.0);
               if (var2.getX() > -1.0) {
                  this.popup.setAnchorX(var2.getX());
               }

               if (var2.getY() > -1.0) {
                  this.popup.setAnchorY(var2.getY());
               }

               if (var4 > -1.0) {
                  this.popup.setMinWidth(var4);
               }

               if (var6 > -1.0) {
                  this.popup.setMinHeight(var6);
               }

               Bounds var8 = var3.getLayoutBounds();
               double var9 = var8.getWidth();
               double var11 = var8.getHeight();
               double var13 = var9 < var4 ? var4 : var9;
               double var15 = var11 < var6 ? var6 : var11;
               if (var13 != var9 || var15 != var11) {
                  var3.resize(var13, var15);
                  if (var3 instanceof Region) {
                     ((Region)var3).setMinSize(var13, var15);
                     ((Region)var3).setPrefSize(var13, var15);
                  }
               }

            }
         }
      }
   }

   protected abstract TextField getEditor();

   protected abstract StringConverter getConverter();

   protected TextField getEditableInputNode() {
      if (this.textField == null && this.getEditor() != null) {
         this.textField = this.getEditor();
         this.textField.setFocusTraversable(false);
         this.textField.promptTextProperty().bind(this.comboBoxBase.promptTextProperty());
         this.textField.tooltipProperty().bind(this.comboBoxBase.tooltipProperty());
         this.initialTextFieldValue = this.textField.getText();
      }

      return this.textField;
   }

   protected void setTextFromTextFieldIntoComboBoxValue() {
      if (this.getEditor() != null) {
         StringConverter var1 = this.getConverter();
         if (var1 != null) {
            Object var2 = this.comboBoxBase.getValue();
            Object var3 = var2;
            String var4 = this.textField.getText();
            if (var2 != null || var4 != null && !var4.isEmpty()) {
               try {
                  var3 = var1.fromString(var4);
               } catch (Exception var6) {
               }
            } else {
               var3 = null;
            }

            if ((var3 != null || var2 != null) && (var3 == null || !var3.equals(var2))) {
               this.comboBoxBase.setValue(var3);
            }

            this.updateDisplayNode();
         }
      }

   }

   protected void updateDisplayNode() {
      if (this.textField != null && this.getEditor() != null) {
         Object var1 = this.comboBoxBase.getValue();
         StringConverter var2 = this.getConverter();
         if (this.initialTextFieldValue != null && !this.initialTextFieldValue.isEmpty()) {
            this.textField.setText(this.initialTextFieldValue);
            this.initialTextFieldValue = null;
         } else {
            String var3 = var2.toString(var1);
            if (var1 != null && var3 != null) {
               if (!var3.equals(this.textField.getText())) {
                  this.textField.setText(var3);
               }
            } else {
               this.textField.setText("");
            }
         }
      }

   }

   private void handleKeyEvent(KeyEvent var1, boolean var2) {
      if (var1.getCode() == KeyCode.ENTER) {
         this.setTextFromTextFieldIntoComboBoxValue();
         if (var2 && this.comboBoxBase.getOnAction() != null) {
            var1.consume();
         } else {
            this.forwardToParent(var1);
         }
      } else if (var1.getCode() == KeyCode.F4) {
         if (var1.getEventType() == KeyEvent.KEY_RELEASED) {
            if (this.comboBoxBase.isShowing()) {
               this.comboBoxBase.hide();
            } else {
               this.comboBoxBase.show();
            }
         }

         var1.consume();
      }

   }

   private void forwardToParent(KeyEvent var1) {
      if (this.comboBoxBase.getParent() != null) {
         this.comboBoxBase.getParent().fireEvent(var1);
      }

   }

   protected void updateEditable() {
      final TextField var1 = this.getEditor();
      if (this.getEditor() == null) {
         if (this.textField != null) {
            this.textField.removeEventFilter(MouseEvent.DRAG_DETECTED, this.textFieldMouseEventHandler);
            this.textField.removeEventFilter(DragEvent.ANY, this.textFieldDragEventHandler);
            this.comboBoxBase.setInputMethodRequests((InputMethodRequests)null);
         }
      } else if (var1 != null) {
         var1.addEventFilter(MouseEvent.DRAG_DETECTED, this.textFieldMouseEventHandler);
         var1.addEventFilter(DragEvent.ANY, this.textFieldDragEventHandler);
         this.comboBoxBase.setInputMethodRequests(new ExtendedInputMethodRequests() {
            public Point2D getTextLocation(int var1x) {
               return var1.getInputMethodRequests().getTextLocation(var1x);
            }

            public int getLocationOffset(int var1x, int var2) {
               return var1.getInputMethodRequests().getLocationOffset(var1x, var2);
            }

            public void cancelLatestCommittedText() {
               var1.getInputMethodRequests().cancelLatestCommittedText();
            }

            public String getSelectedText() {
               return var1.getInputMethodRequests().getSelectedText();
            }

            public int getInsertPositionOffset() {
               return ((ExtendedInputMethodRequests)var1.getInputMethodRequests()).getInsertPositionOffset();
            }

            public String getCommittedText(int var1x, int var2) {
               return ((ExtendedInputMethodRequests)var1.getInputMethodRequests()).getCommittedText(var1x, var2);
            }

            public int getCommittedTextLength() {
               return ((ExtendedInputMethodRequests)var1.getInputMethodRequests()).getCommittedTextLength();
            }
         });
      }

      this.textField = var1;
   }

   public static final class FakeFocusTextField extends TextField {
      public void requestFocus() {
         if (this.getParent() != null) {
            this.getParent().requestFocus();
         }

      }

      public void setFakeFocus(boolean var1) {
         this.setFocused(var1);
      }

      public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
         switch (var1) {
            case FOCUS_ITEM:
               return this.getParent();
            default:
               return super.queryAccessibleAttribute(var1, var2);
         }
      }
   }
}
