package javafx.scene.control;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.util.Builder;
import javafx.util.Callback;

/** @deprecated */
@Deprecated
public class ListViewBuilder extends ControlBuilder implements Builder {
   private int __set;
   private Callback cellFactory;
   private boolean editable;
   private FocusModel focusModel;
   private ObservableList items;
   private EventHandler onEditCancel;
   private EventHandler onEditCommit;
   private EventHandler onEditStart;
   private Orientation orientation;
   private MultipleSelectionModel selectionModel;

   protected ListViewBuilder() {
   }

   public static ListViewBuilder create() {
      return new ListViewBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(ListView var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setCellFactory(this.cellFactory);
               break;
            case 1:
               var1.setEditable(this.editable);
               break;
            case 2:
               var1.setFocusModel(this.focusModel);
               break;
            case 3:
               var1.setItems(this.items);
               break;
            case 4:
               var1.setOnEditCancel(this.onEditCancel);
               break;
            case 5:
               var1.setOnEditCommit(this.onEditCommit);
               break;
            case 6:
               var1.setOnEditStart(this.onEditStart);
               break;
            case 7:
               var1.setOrientation(this.orientation);
               break;
            case 8:
               var1.setSelectionModel(this.selectionModel);
         }
      }

   }

   public ListViewBuilder cellFactory(Callback var1) {
      this.cellFactory = var1;
      this.__set(0);
      return this;
   }

   public ListViewBuilder editable(boolean var1) {
      this.editable = var1;
      this.__set(1);
      return this;
   }

   public ListViewBuilder focusModel(FocusModel var1) {
      this.focusModel = var1;
      this.__set(2);
      return this;
   }

   public ListViewBuilder items(ObservableList var1) {
      this.items = var1;
      this.__set(3);
      return this;
   }

   public ListViewBuilder onEditCancel(EventHandler var1) {
      this.onEditCancel = var1;
      this.__set(4);
      return this;
   }

   public ListViewBuilder onEditCommit(EventHandler var1) {
      this.onEditCommit = var1;
      this.__set(5);
      return this;
   }

   public ListViewBuilder onEditStart(EventHandler var1) {
      this.onEditStart = var1;
      this.__set(6);
      return this;
   }

   public ListViewBuilder orientation(Orientation var1) {
      this.orientation = var1;
      this.__set(7);
      return this;
   }

   public ListViewBuilder selectionModel(MultipleSelectionModel var1) {
      this.selectionModel = var1;
      this.__set(8);
      return this;
   }

   public ListView build() {
      ListView var1 = new ListView();
      this.applyTo(var1);
      return var1;
   }
}
