package javafx.scene.control;

/** @deprecated */
@Deprecated
public class TableCellBuilder extends CellBuilder {
   protected TableCellBuilder() {
   }

   public static TableCellBuilder create() {
      return new TableCellBuilder();
   }

   public TableCell build() {
      TableCell var1 = new TableCell();
      this.applyTo(var1);
      return var1;
   }
}
