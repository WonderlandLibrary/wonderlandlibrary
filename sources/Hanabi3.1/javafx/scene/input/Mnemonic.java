package javafx.scene.input;

import javafx.beans.NamedArg;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class Mnemonic {
   private KeyCombination keyCombination;
   private Node node;

   public Mnemonic(@NamedArg("node") Node var1, @NamedArg("keyCombination") KeyCombination var2) {
      this.node = var1;
      this.keyCombination = var2;
   }

   public KeyCombination getKeyCombination() {
      return this.keyCombination;
   }

   public void setKeyCombination(KeyCombination var1) {
      this.keyCombination = var1;
   }

   public Node getNode() {
      return this.node;
   }

   public void setNode(Node var1) {
      this.node = var1;
   }

   public void fire() {
      if (this.node != null) {
         this.node.fireEvent(new ActionEvent());
      }

   }
}
