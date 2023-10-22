package com.sun.javafx.scene.control.behavior;

import java.util.Collections;
import javafx.scene.control.FocusModel;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;

public class ListCellBehavior extends CellBehaviorBase {
   public ListCellBehavior(ListCell var1) {
      super(var1, Collections.emptyList());
   }

   protected MultipleSelectionModel getSelectionModel() {
      return this.getCellContainer().getSelectionModel();
   }

   protected FocusModel getFocusModel() {
      return this.getCellContainer().getFocusModel();
   }

   protected ListView getCellContainer() {
      return ((ListCell)this.getControl()).getListView();
   }

   protected void edit(ListCell var1) {
      int var2 = var1 == null ? -1 : var1.getIndex();
      this.getCellContainer().edit(var2);
   }
}
