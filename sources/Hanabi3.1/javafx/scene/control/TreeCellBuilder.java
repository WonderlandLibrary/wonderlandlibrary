package javafx.scene.control;

import javafx.scene.Node;

/** @deprecated */
@Deprecated
public class TreeCellBuilder extends IndexedCellBuilder {
   private boolean __set;
   private Node disclosureNode;

   protected TreeCellBuilder() {
   }

   public static TreeCellBuilder create() {
      return new TreeCellBuilder();
   }

   public void applyTo(TreeCell var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setDisclosureNode(this.disclosureNode);
      }

   }

   public TreeCellBuilder disclosureNode(Node var1) {
      this.disclosureNode = var1;
      this.__set = true;
      return this;
   }

   public TreeCell build() {
      TreeCell var1 = new TreeCell();
      this.applyTo(var1);
      return var1;
   }
}
