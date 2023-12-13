package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ComboBox extends ComboBoxBase {
   private ObjectProperty items;
   private ObjectProperty converter;
   private ObjectProperty cellFactory;
   private ObjectProperty buttonCell;
   private ObjectProperty selectionModel;
   private IntegerProperty visibleRowCount;
   private TextField textField;
   private ReadOnlyObjectWrapper editor;
   private ObjectProperty placeholder;
   private ChangeListener selectedItemListener;
   private static final String DEFAULT_STYLE_CLASS = "combo-box";
   private boolean wasSetAllCalled;
   private int previousItemCount;

   private static StringConverter defaultStringConverter() {
      return new StringConverter() {
         public String toString(Object var1) {
            return var1 == null ? null : var1.toString();
         }

         public Object fromString(String var1) {
            return var1;
         }
      };
   }

   public ComboBox() {
      this(FXCollections.observableArrayList());
   }

   public ComboBox(ObservableList var1) {
      this.items = new SimpleObjectProperty(this, "items");
      this.converter = new SimpleObjectProperty(this, "converter", defaultStringConverter());
      this.cellFactory = new SimpleObjectProperty(this, "cellFactory");
      this.buttonCell = new SimpleObjectProperty(this, "buttonCell");
      this.selectionModel = new SimpleObjectProperty(this, "selectionModel") {
         private SingleSelectionModel oldSM = null;

         protected void invalidated() {
            if (this.oldSM != null) {
               this.oldSM.selectedItemProperty().removeListener(ComboBox.this.selectedItemListener);
            }

            SingleSelectionModel var1 = (SingleSelectionModel)this.get();
            this.oldSM = var1;
            if (var1 != null) {
               var1.selectedItemProperty().addListener(ComboBox.this.selectedItemListener);
            }

         }
      };
      this.visibleRowCount = new SimpleIntegerProperty(this, "visibleRowCount", 10);
      this.selectedItemListener = new ChangeListener() {
         public void changed(ObservableValue var1, Object var2, Object var3) {
            if (!ComboBox.this.wasSetAllCalled || var3 != null) {
               ComboBox.this.updateValue(var3);
            }

            ComboBox.this.wasSetAllCalled = false;
         }
      };
      this.wasSetAllCalled = false;
      this.previousItemCount = -1;
      this.getStyleClass().add("combo-box");
      this.setAccessibleRole(AccessibleRole.COMBO_BOX);
      this.setItems(var1);
      this.setSelectionModel(new ComboBoxSelectionModel(this));
      this.valueProperty().addListener((var1x, var2, var3) -> {
         if (this.getItems() != null) {
            SingleSelectionModel var4 = this.getSelectionModel();
            int var5 = this.getItems().indexOf(var3);
            if (var5 == -1) {
               var4.setSelectedItem(var3);
            } else {
               Object var6 = var4.getSelectedItem();
               if (var6 == null || !var6.equals(this.getValue())) {
                  var4.clearAndSelect(var5);
               }
            }

         }
      });
      this.editableProperty().addListener((var1x) -> {
         this.getSelectionModel().clearSelection();
      });
   }

   public final void setItems(ObservableList var1) {
      this.itemsProperty().set(var1);
   }

   public final ObservableList getItems() {
      return (ObservableList)this.items.get();
   }

   public ObjectProperty itemsProperty() {
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

   public final void setCellFactory(Callback var1) {
      this.cellFactoryProperty().set(var1);
   }

   public final Callback getCellFactory() {
      return (Callback)this.cellFactoryProperty().get();
   }

   public ObjectProperty cellFactoryProperty() {
      return this.cellFactory;
   }

   public ObjectProperty buttonCellProperty() {
      return this.buttonCell;
   }

   public final void setButtonCell(ListCell var1) {
      this.buttonCellProperty().set(var1);
   }

   public final ListCell getButtonCell() {
      return (ListCell)this.buttonCellProperty().get();
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

   public final void setVisibleRowCount(int var1) {
      this.visibleRowCount.set(var1);
   }

   public final int getVisibleRowCount() {
      return this.visibleRowCount.get();
   }

   public final IntegerProperty visibleRowCountProperty() {
      return this.visibleRowCount;
   }

   public final TextField getEditor() {
      return (TextField)this.editorProperty().get();
   }

   public final ReadOnlyObjectProperty editorProperty() {
      if (this.editor == null) {
         this.editor = new ReadOnlyObjectWrapper(this, "editor");
         this.textField = new ComboBoxPopupControl.FakeFocusTextField();
         this.editor.set(this.textField);
      }

      return this.editor.getReadOnlyProperty();
   }

   public final ObjectProperty placeholderProperty() {
      if (this.placeholder == null) {
         this.placeholder = new SimpleObjectProperty(this, "placeholder");
      }

      return this.placeholder;
   }

   public final void setPlaceholder(Node var1) {
      this.placeholderProperty().set(var1);
   }

   public final Node getPlaceholder() {
      return this.placeholder == null ? null : (Node)this.placeholder.get();
   }

   protected Skin createDefaultSkin() {
      return new ComboBoxListViewSkin(this);
   }

   private void updateValue(Object var1) {
      if (!this.valueProperty().isBound()) {
         this.setValue(var1);
      }

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
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   static class ComboBoxSelectionModel extends SingleSelectionModel {
      private final ComboBox comboBox;
      private final ListChangeListener itemsContentObserver = new ListChangeListener() {
         public void onChanged(ListChangeListener.Change var1) {
            int var2;
            if (ComboBoxSelectionModel.this.comboBox.getItems() != null && !ComboBoxSelectionModel.this.comboBox.getItems().isEmpty()) {
               if (ComboBoxSelectionModel.this.getSelectedIndex() == -1 && ComboBoxSelectionModel.this.getSelectedItem() != null) {
                  var2 = ComboBoxSelectionModel.this.comboBox.getItems().indexOf(ComboBoxSelectionModel.this.getSelectedItem());
                  if (var2 != -1) {
                     ComboBoxSelectionModel.this.setSelectedIndex(var2);
                  }
               }
            } else {
               ComboBoxSelectionModel.this.setSelectedIndex(-1);
            }

            var2 = 0;

            while(true) {
               do {
                  do {
                     if (!var1.next()) {
                        if (var2 != 0) {
                           ComboBoxSelectionModel.this.clearAndSelect(ComboBoxSelectionModel.this.getSelectedIndex() + var2);
                        }

                        ComboBoxSelectionModel.this.comboBox.previousItemCount = ComboBoxSelectionModel.this.getItemCount();
                        return;
                     }

                     ComboBoxSelectionModel.this.comboBox.wasSetAllCalled = ComboBoxSelectionModel.this.comboBox.previousItemCount == var1.getRemovedSize();
                  } while(var1.wasReplaced());
               } while(!var1.wasAdded() && !var1.wasRemoved());

               if (var1.getFrom() <= ComboBoxSelectionModel.this.getSelectedIndex() && ComboBoxSelectionModel.this.getSelectedIndex() != -1) {
                  var2 += var1.wasAdded() ? var1.getAddedSize() : -var1.getRemovedSize();
               }
            }
         }
      };
      private final InvalidationListener itemsObserver;
      private WeakListChangeListener weakItemsContentObserver;

      public ComboBoxSelectionModel(ComboBox var1) {
         this.weakItemsContentObserver = new WeakListChangeListener(this.itemsContentObserver);
         if (var1 == null) {
            throw new NullPointerException("ComboBox can not be null");
         } else {
            this.comboBox = var1;
            this.selectedIndexProperty().addListener((var1x) -> {
               this.setSelectedItem(this.getModelItem(this.getSelectedIndex()));
            });
            this.itemsObserver = new InvalidationListener() {
               private WeakReference weakItemsRef;

               {
                  this.weakItemsRef = new WeakReference(ComboBoxSelectionModel.this.comboBox.getItems());
               }

               public void invalidated(Observable var1) {
                  ObservableList var2 = (ObservableList)this.weakItemsRef.get();
                  this.weakItemsRef = new WeakReference(ComboBoxSelectionModel.this.comboBox.getItems());
                  ComboBoxSelectionModel.this.updateItemsObserver(var2, ComboBoxSelectionModel.this.comboBox.getItems());
               }
            };
            this.comboBox.itemsProperty().addListener(new WeakInvalidationListener(this.itemsObserver));
            if (this.comboBox.getItems() != null) {
               this.comboBox.getItems().addListener(this.weakItemsContentObserver);
            }

         }
      }

      private void updateItemsObserver(ObservableList var1, ObservableList var2) {
         if (var1 != null) {
            var1.removeListener(this.weakItemsContentObserver);
         }

         if (var2 != null) {
            var2.addListener(this.weakItemsContentObserver);
         }

         int var3 = -1;
         if (var2 != null) {
            Object var4 = this.comboBox.getValue();
            if (var4 != null) {
               var3 = var2.indexOf(var4);
            }
         }

         this.setSelectedIndex(var3);
      }

      protected Object getModelItem(int var1) {
         ObservableList var2 = this.comboBox.getItems();
         if (var2 == null) {
            return null;
         } else {
            return var1 >= 0 && var1 < var2.size() ? var2.get(var1) : null;
         }
      }

      protected int getItemCount() {
         ObservableList var1 = this.comboBox.getItems();
         return var1 == null ? 0 : var1.size();
      }
   }
}
