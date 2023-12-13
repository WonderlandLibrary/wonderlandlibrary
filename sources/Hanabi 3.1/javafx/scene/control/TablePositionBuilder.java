package javafx.scene.control;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class TablePositionBuilder implements Builder {
   private int row;
   private TableColumn tableColumn;
   private TableView tableView;

   protected TablePositionBuilder() {
   }

   public static TablePositionBuilder create() {
      return new TablePositionBuilder();
   }

   public TablePositionBuilder row(int var1) {
      this.row = var1;
      return this;
   }

   public TablePositionBuilder tableColumn(TableColumn var1) {
      this.tableColumn = var1;
      return this;
   }

   public TablePositionBuilder tableView(TableView var1) {
      this.tableView = var1;
      return this;
   }

   public TablePosition build() {
      TablePosition var1 = new TablePosition(this.tableView, this.row, this.tableColumn);
      return var1;
   }
}
