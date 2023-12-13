package javafx.scene.control;

import javafx.event.EventHandler;
import javafx.util.Builder;
import javafx.util.Callback;

/** @deprecated */
@Deprecated
public class TreeViewBuilder extends ControlBuilder implements Builder {
   private int __set;
   private Callback cellFactory;
   private boolean editable;
   private FocusModel focusModel;
   private EventHandler onEditCancel;
   private EventHandler onEditCommit;
   private EventHandler onEditStart;
   private TreeItem root;
   private MultipleSelectionModel selectionModel;
   private boolean showRoot;

   protected TreeViewBuilder() {
   }

   public static TreeViewBuilder create() {
      return new TreeViewBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(TreeView var1) {
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
               var1.setOnEditCancel(this.onEditCancel);
               break;
            case 4:
               var1.setOnEditCommit(this.onEditCommit);
               break;
            case 5:
               var1.setOnEditStart(this.onEditStart);
               break;
            case 6:
               var1.setRoot(this.root);
               break;
            case 7:
               var1.setSelectionModel(this.selectionModel);
               break;
            case 8:
               var1.setShowRoot(this.showRoot);
         }
      }

   }

   public TreeViewBuilder cellFactory(Callback var1) {
      this.cellFactory = var1;
      this.__set(0);
      return this;
   }

   public TreeViewBuilder editable(boolean var1) {
      this.editable = var1;
      this.__set(1);
      return this;
   }

   public TreeViewBuilder focusModel(FocusModel var1) {
      this.focusModel = var1;
      this.__set(2);
      return this;
   }

   public TreeViewBuilder onEditCancel(EventHandler var1) {
      this.onEditCancel = var1;
      this.__set(3);
      return this;
   }

   public TreeViewBuilder onEditCommit(EventHandler var1) {
      this.onEditCommit = var1;
      this.__set(4);
      return this;
   }

   public TreeViewBuilder onEditStart(EventHandler var1) {
      this.onEditStart = var1;
      this.__set(5);
      return this;
   }

   public TreeViewBuilder root(TreeItem var1) {
      this.root = var1;
      this.__set(6);
      return this;
   }

   public TreeViewBuilder selectionModel(MultipleSelectionModel var1) {
      this.selectionModel = var1;
      this.__set(7);
      return this;
   }

   public TreeViewBuilder showRoot(boolean var1) {
      this.showRoot = var1;
      this.__set(8);
      return this;
   }

   public TreeView build() {
      TreeView var1 = new TreeView();
      this.applyTo(var1);
      return var1;
   }
}
