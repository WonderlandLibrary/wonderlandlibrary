package javafx.scene.input;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class KeyCodeCombinationBuilder implements Builder {
   private KeyCombination.ModifierValue alt;
   private KeyCode code;
   private KeyCombination.ModifierValue control;
   private KeyCombination.ModifierValue meta;
   private KeyCombination.ModifierValue shift;
   private KeyCombination.ModifierValue shortcut;

   protected KeyCodeCombinationBuilder() {
   }

   public static KeyCodeCombinationBuilder create() {
      return new KeyCodeCombinationBuilder();
   }

   public KeyCodeCombinationBuilder alt(KeyCombination.ModifierValue var1) {
      this.alt = var1;
      return this;
   }

   public KeyCodeCombinationBuilder code(KeyCode var1) {
      this.code = var1;
      return this;
   }

   public KeyCodeCombinationBuilder control(KeyCombination.ModifierValue var1) {
      this.control = var1;
      return this;
   }

   public KeyCodeCombinationBuilder meta(KeyCombination.ModifierValue var1) {
      this.meta = var1;
      return this;
   }

   public KeyCodeCombinationBuilder shift(KeyCombination.ModifierValue var1) {
      this.shift = var1;
      return this;
   }

   public KeyCodeCombinationBuilder shortcut(KeyCombination.ModifierValue var1) {
      this.shortcut = var1;
      return this;
   }

   public KeyCodeCombination build() {
      KeyCodeCombination var1 = new KeyCodeCombination(this.code, this.shift, this.control, this.alt, this.meta, this.shortcut);
      return var1;
   }
}
