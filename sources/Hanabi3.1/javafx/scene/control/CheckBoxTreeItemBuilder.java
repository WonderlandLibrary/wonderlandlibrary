package javafx.scene.control;

/** @deprecated */
@Deprecated
public class CheckBoxTreeItemBuilder extends TreeItemBuilder {
   protected CheckBoxTreeItemBuilder() {
   }

   public static CheckBoxTreeItemBuilder create() {
      return new CheckBoxTreeItemBuilder();
   }

   public CheckBoxTreeItem build() {
      CheckBoxTreeItem var1 = new CheckBoxTreeItem();
      this.applyTo(var1);
      return var1;
   }
}
