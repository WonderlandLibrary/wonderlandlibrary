package javafx.scene.control;

/** @deprecated */
@Deprecated
public class TableRowBuilder extends IndexedCellBuilder {
   protected TableRowBuilder() {
   }

   public static TableRowBuilder create() {
      return new TableRowBuilder();
   }

   public TableRow build() {
      TableRow var1 = new TableRow();
      this.applyTo(var1);
      return var1;
   }
}
