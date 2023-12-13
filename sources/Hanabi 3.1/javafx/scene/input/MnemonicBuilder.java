package javafx.scene.input;

import javafx.scene.Node;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class MnemonicBuilder implements Builder {
   private KeyCombination keyCombination;
   private Node node;

   protected MnemonicBuilder() {
   }

   public static MnemonicBuilder create() {
      return new MnemonicBuilder();
   }

   public MnemonicBuilder keyCombination(KeyCombination var1) {
      this.keyCombination = var1;
      return this;
   }

   public MnemonicBuilder node(Node var1) {
      this.node = var1;
      return this;
   }

   public Mnemonic build() {
      Mnemonic var1 = new Mnemonic(this.node, this.keyCombination);
      return var1;
   }
}
