package javafx.scene.control;

/** @deprecated */
@Deprecated
public class ListCellBuilder extends IndexedCellBuilder {
   protected ListCellBuilder() {
   }

   public static ListCellBuilder create() {
      return new ListCellBuilder();
   }

   public ListCell build() {
      ListCell var1 = new ListCell();
      this.applyTo(var1);
      return var1;
   }
}
