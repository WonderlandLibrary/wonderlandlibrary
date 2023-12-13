package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ChoiceBoxSkin;
import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.util.StringConverter;

@DefaultProperty("items")
public class ChoiceBox extends Control {
   public static final EventType ON_SHOWING;
   public static final EventType ON_SHOWN;
   public static final EventType ON_HIDING;
   public static final EventType ON_HIDDEN;
   private ObjectProperty selectionModel;
   private ChangeListener selectedItemListener;
   private ReadOnlyBooleanWrapper showing;
   private ObjectProperty items;
   private final ListChangeListener itemsListener;
   private ObjectProperty converter;
   private ObjectProperty value;
   private ObjectProperty onAction;
   private ObjectProperty onShowing;
   private ObjectProperty onShown;
   private ObjectProperty onHiding;
   private ObjectProperty onHidden;
   private static final PseudoClass SHOWING_PSEUDOCLASS_STATE;

   public ChoiceBox() {
      this(FXCollections.observableArrayList());
   }

   public ChoiceBox(ObservableList var1) {
      this.selectionModel = new SimpleObjectProperty(this, "selectionModel") {
         private SelectionModel oldSM = null;

         protected void invalidated() {
            if (this.oldSM != null) {
               this.oldSM.selectedItemProperty().removeListener(ChoiceBox.this.selectedItemListener);
            }

            SelectionModel var1 = (SelectionModel)this.get();
            this.oldSM = var1;
            if (var1 != null) {
               var1.selectedItemProperty().addListener(ChoiceBox.this.selectedItemListener);
            }

         }
      };
      this.selectedItemListener = (var1x, var2, var3) -> {
         if (!this.valueProperty().isBound()) {
            this.setValue(var3);
         }

      };
      this.showing = new ReadOnlyBooleanWrapper() {
         protected void invalidated() {
            ChoiceBox.this.pseudoClassStateChanged(ChoiceBox.SHOWING_PSEUDOCLASS_STATE, this.get());
            ChoiceBox.this.notifyAccessibleAttributeChanged(AccessibleAttribute.EXPANDED);
         }

         public Object getBean() {
            return ChoiceBox.this;
         }

         public String getName() {
            return "showing";
         }
      };
      this.items = new ObjectPropertyBase() {
         ObservableList old;

         protected void invalidated() {
            ObservableList var1 = (ObservableList)this.get();
            if (this.old != var1) {
               if (this.old != null) {
                  this.old.removeListener(ChoiceBox.this.itemsListener);
               }

               if (var1 != null) {
                  var1.addListener(ChoiceBox.this.itemsListener);
               }

               SingleSelectionModel var2 = ChoiceBox.this.getSelectionModel();
               if (var2 != null) {
                  if (var1 != null && var1.isEmpty()) {
                     var2.clearSelection();
                  } else if (var2.getSelectedIndex() == -1 && var2.getSelectedItem() != null) {
                     int var3 = ChoiceBox.this.getItems().indexOf(var2.getSelectedItem());
                     if (var3 != -1) {
                        var2.setSelectedIndex(var3);
                     }
                  } else {
                     var2.clearSelection();
                  }
               }

               this.old = var1;
            }

         }

         public Object getBean() {
            return ChoiceBox.this;
         }

         public String getName() {
            return "items";
         }
      };
      this.itemsListener = (var1x) -> {
         SingleSelectionModel var2 = this.getSelectionModel();
         if (var2 != null) {
            if (this.getItems() != null && !this.getItems().isEmpty()) {
               int var3 = this.getItems().indexOf(var2.getSelectedItem());
               var2.setSelectedIndex(var3);
            } else {
               var2.clearSelection();
            }
         }

         if (var2 != null) {
            Object var4 = var2.getSelectedItem();

            while(var1x.next()) {
               if (var4 != null && var1x.getRemoved().contains(var4)) {
                  var2.clearSelection();
                  break;
               }
            }
         }

      };
      this.converter = new SimpleObjectProperty(this, "converter", (Object)null);
      this.value = new SimpleObjectProperty(this, "value") {
         protected void invalidated() {
            super.invalidated();
            ChoiceBox.this.fireEvent(new ActionEvent());
            SingleSelectionModel var1 = ChoiceBox.this.getSelectionModel();
            if (var1 != null) {
               var1.select(super.getValue());
            }

            ChoiceBox.this.notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
         }
      };
      this.onAction = new ObjectPropertyBase() {
         protected void invalidated() {
            ChoiceBox.this.setEventHandler(ActionEvent.ACTION, (EventHandler)this.get());
         }

         public Object getBean() {
            return ChoiceBox.this;
         }

         public String getName() {
            return "onAction";
         }
      };
      this.onShowing = new ObjectPropertyBase() {
         protected void invalidated() {
            ChoiceBox.this.setEventHandler(ChoiceBox.ON_SHOWING, (EventHandler)this.get());
         }

         public Object getBean() {
            return ChoiceBox.this;
         }

         public String getName() {
            return "onShowing";
         }
      };
      this.onShown = new ObjectPropertyBase() {
         protected void invalidated() {
            ChoiceBox.this.setEventHandler(ChoiceBox.ON_SHOWN, (EventHandler)this.get());
         }

         public Object getBean() {
            return ChoiceBox.this;
         }

         public String getName() {
            return "onShown";
         }
      };
      this.onHiding = new ObjectPropertyBase() {
         protected void invalidated() {
            ChoiceBox.this.setEventHandler(ChoiceBox.ON_HIDING, (EventHandler)this.get());
         }

         public Object getBean() {
            return ChoiceBox.this;
         }

         public String getName() {
            return "onHiding";
         }
      };
      this.onHidden = new ObjectPropertyBase() {
         protected void invalidated() {
            ChoiceBox.this.setEventHandler(ChoiceBox.ON_HIDDEN, (EventHandler)this.get());
         }

         public Object getBean() {
            return ChoiceBox.this;
         }

         public String getName() {
            return "onHidden";
         }
      };
      this.getStyleClass().setAll((Object[])("choice-box"));
      this.setAccessibleRole(AccessibleRole.COMBO_BOX);
      this.setItems(var1);
      this.setSelectionModel(new ChoiceBoxSelectionModel(this));
      this.valueProperty().addListener((var1x, var2, var3) -> {
         if (this.getItems() != null) {
            int var4 = this.getItems().indexOf(var3);
            if (var4 > -1) {
               this.getSelectionModel().select(var4);
            }

         }
      });
   }

   public final void setSelectionModel(SingleSelectionModel var1) {
      this.selectionModel.set(var1);
   }

   public final SingleSelectionModel getSelectionModel() {
      return (SingleSelectionModel)this.selectionModel.get();
   }

   public final ObjectProperty selectionModelProperty() {
      return this.selectionModel;
   }

   public final boolean isShowing() {
      return this.showing.get();
   }

   public final ReadOnlyBooleanProperty showingProperty() {
      return this.showing.getReadOnlyProperty();
   }

   private void setShowing(boolean var1) {
      Event.fireEvent(this, var1 ? new Event(ComboBoxBase.ON_SHOWING) : new Event(ComboBoxBase.ON_HIDING));
      this.showing.set(var1);
      Event.fireEvent(this, var1 ? new Event(ComboBoxBase.ON_SHOWN) : new Event(ComboBoxBase.ON_HIDDEN));
   }

   public final void setItems(ObservableList var1) {
      this.items.set(var1);
   }

   public final ObservableList getItems() {
      return (ObservableList)this.items.get();
   }

   public final ObjectProperty itemsProperty() {
      return this.items;
   }

   public ObjectProperty converterProperty() {
      return this.converter;
   }

   public final void setConverter(StringConverter var1) {
      this.converterProperty().set(var1);
   }

   public final StringConverter getConverter() {
      return (StringConverter)this.converterProperty().get();
   }

   public ObjectProperty valueProperty() {
      return this.value;
   }

   public final void setValue(Object var1) {
      this.valueProperty().set(var1);
   }

   public final Object getValue() {
      return this.valueProperty().get();
   }

   public final ObjectProperty onActionProperty() {
      return this.onAction;
   }

   public final void setOnAction(EventHandler var1) {
      this.onActionProperty().set(var1);
   }

   public final EventHandler getOnAction() {
      return (EventHandler)this.onActionProperty().get();
   }

   public final ObjectProperty onShowingProperty() {
      return this.onShowing;
   }

   public final void setOnShowing(EventHandler var1) {
      this.onShowingProperty().set(var1);
   }

   public final EventHandler getOnShowing() {
      return (EventHandler)this.onShowingProperty().get();
   }

   public final ObjectProperty onShownProperty() {
      return this.onShown;
   }

   public final void setOnShown(EventHandler var1) {
      this.onShownProperty().set(var1);
   }

   public final EventHandler getOnShown() {
      return (EventHandler)this.onShownProperty().get();
   }

   public final ObjectProperty onHidingProperty() {
      return this.onHiding;
   }

   public final void setOnHiding(EventHandler var1) {
      this.onHidingProperty().set(var1);
   }

   public final EventHandler getOnHiding() {
      return (EventHandler)this.onHidingProperty().get();
   }

   public final ObjectProperty onHiddenProperty() {
      return this.onHidden;
   }

   public final void setOnHidden(EventHandler var1) {
      this.onHiddenProperty().set(var1);
   }

   public final EventHandler getOnHidden() {
      return (EventHandler)this.onHiddenProperty().get();
   }

   public void show() {
      if (!this.isDisabled()) {
         this.setShowing(true);
      }

   }

   public void hide() {
      this.setShowing(false);
   }

   protected Skin createDefaultSkin() {
      return new ChoiceBoxSkin(this);
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case TEXT:
            String var3 = this.getAccessibleText();
            if (var3 != null && !var3.isEmpty()) {
               return var3;
            } else {
               Object var4 = super.queryAccessibleAttribute(var1, var2);
               if (var4 != null) {
                  return var4;
               } else {
                  StringConverter var5 = this.getConverter();
                  if (var5 == null) {
                     return this.getValue() != null ? this.getValue().toString() : "";
                  }

                  return var5.toString(this.getValue());
               }
            }
         case EXPANDED:
            return this.isShowing();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case COLLAPSE:
            this.hide();
            break;
         case EXPAND:
            this.show();
            break;
         default:
            super.executeAccessibleAction(var1);
      }

   }

   static {
      ON_SHOWING = new EventType(Event.ANY, "CHOICE_BOX_ON_SHOWING");
      ON_SHOWN = new EventType(Event.ANY, "CHOICE_BOX_ON_SHOWN");
      ON_HIDING = new EventType(Event.ANY, "CHOICE_BOX_ON_HIDING");
      ON_HIDDEN = new EventType(Event.ANY, "CHOICE_BOX_ON_HIDDEN");
      SHOWING_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("showing");
   }

   static class ChoiceBoxSelectionModel extends SingleSelectionModel {
      private final ChoiceBox choiceBox;

      public ChoiceBoxSelectionModel(ChoiceBox var1) {
         if (var1 == null) {
            throw new NullPointerException("ChoiceBox can not be null");
         } else {
            this.choiceBox = var1;
            ListChangeListener var2 = (var1x) -> {
               if (this.choiceBox.getItems() != null && !this.choiceBox.getItems().isEmpty()) {
                  if (this.getSelectedIndex() == -1 && this.getSelectedItem() != null) {
                     int var2 = this.choiceBox.getItems().indexOf(this.getSelectedItem());
                     if (var2 != -1) {
                        this.setSelectedIndex(var2);
                     }
                  }
               } else {
                  this.setSelectedIndex(-1);
               }

            };
            if (this.choiceBox.getItems() != null) {
               this.choiceBox.getItems().addListener(var2);
            }

            ChangeListener var3 = (var2x, var3x, var4) -> {
               if (var3x != null) {
                  var3x.removeListener(var2);
               }

               if (var4 != null) {
                  var4.addListener(var2);
               }

               this.setSelectedIndex(-1);
               if (this.getSelectedItem() != null) {
                  int var5 = this.choiceBox.getItems().indexOf(this.getSelectedItem());
                  if (var5 != -1) {
                     this.setSelectedIndex(var5);
                  }
               }

            };
            this.choiceBox.itemsProperty().addListener(var3);
         }
      }

      protected Object getModelItem(int var1) {
         ObservableList var2 = this.choiceBox.getItems();
         if (var2 == null) {
            return null;
         } else {
            return var1 >= 0 && var1 < var2.size() ? var2.get(var1) : null;
         }
      }

      protected int getItemCount() {
         ObservableList var1 = this.choiceBox.getItems();
         return var1 == null ? 0 : var1.size();
      }

      public void select(int var1) {
         Object var2 = this.getModelItem(var1);
         if (var2 instanceof Separator) {
            ++var1;
            this.select(var1);
         } else {
            super.select(var1);
         }

         if (this.choiceBox.isShowing()) {
            this.choiceBox.hide();
         }

      }
   }
}
