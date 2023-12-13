package javafx.scene.control;

/** @deprecated */
@Deprecated
public class IndexedCellBuilder extends CellBuilder {
   protected IndexedCellBuilder() {
   }

   public static IndexedCellBuilder create() {
      return new IndexedCellBuilder();
   }

   public IndexedCell build() {
      IndexedCell var1 = new IndexedCell();
      this.applyTo(var1);
      return var1;
   }
}
