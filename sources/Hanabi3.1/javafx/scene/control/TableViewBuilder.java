package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.util.Builder;
import javafx.util.Callback;

/** @deprecated */
@Deprecated
public class TableViewBuilder extends ControlBuilder implements Builder {
   private int __set;
   private Callback columnResizePolicy;
   private Collection columns;
   private boolean editable;
   private TableView.TableViewFocusModel focusModel;
   private ObservableList items;
   private Node placeholder;
   private Callback rowFactory;
   private TableView.TableViewSelectionModel selectionModel;
   private Collection sortOrder;
   private boolean tableMenuButtonVisible;

   protected TableViewBuilder() {
   }

   public static TableViewBuilder create() {
      return new TableViewBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(TableView var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setColumnResizePolicy(this.columnResizePolicy);
               break;
            case 1:
               var1.getColumns().addAll(this.columns);
               break;
            case 2:
               var1.setEditable(this.editable);
               break;
            case 3:
               var1.setFocusModel(this.focusModel);
               break;
            case 4:
               var1.setItems(this.items);
               break;
            case 5:
               var1.setPlaceholder(this.placeholder);
               break;
            case 6:
               var1.setRowFactory(this.rowFactory);
               break;
            case 7:
               var1.setSelectionModel(this.selectionModel);
               break;
            case 8:
               var1.getSortOrder().addAll(this.sortOrder);
               break;
            case 9:
               var1.setTableMenuButtonVisible(this.tableMenuButtonVisible);
         }
      }

   }

   public TableViewBuilder columnResizePolicy(Callback var1) {
      this.columnResizePolicy = var1;
      this.__set(0);
      return this;
   }

   public TableViewBuilder columns(Collection var1) {
      this.columns = var1;
      this.__set(1);
      return this;
   }

   public TableViewBuilder columns(TableColumn... var1) {
      return this.columns((Collection)Arrays.asList(var1));
   }

   public TableViewBuilder editable(boolean var1) {
      this.editable = var1;
      this.__set(2);
      return this;
   }

   public TableViewBuilder focusModel(TableView.TableViewFocusModel var1) {
      this.focusModel = var1;
      this.__set(3);
      return this;
   }

   public TableViewBuilder items(ObservableList var1) {
      this.items = var1;
      this.__set(4);
      return this;
   }

   public TableViewBuilder placeholder(Node var1) {
      this.placeholder = var1;
      this.__set(5);
      return this;
   }

   public TableViewBuilder rowFactory(Callback var1) {
      this.rowFactory = var1;
      this.__set(6);
      return this;
   }

   public TableViewBuilder selectionModel(TableView.TableViewSelectionModel var1) {
      this.selectionModel = var1;
      this.__set(7);
      return this;
   }

   public TableViewBuilder sortOrder(Collection var1) {
      this.sortOrder = var1;
      this.__set(8);
      return this;
   }

   public TableViewBuilder sortOrder(TableColumn... var1) {
      return this.sortOrder((Collection)Arrays.asList(var1));
   }

   public TableViewBuilder tableMenuButtonVisible(boolean var1) {
      this.tableMenuButtonVisible = var1;
      this.__set(9);
      return this;
   }

   public TableView build() {
      TableView var1 = new TableView();
      this.applyTo(var1);
      return var1;
   }
}
