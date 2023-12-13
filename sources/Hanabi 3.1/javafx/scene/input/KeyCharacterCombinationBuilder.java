package javafx.scene.input;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class KeyCharacterCombinationBuilder implements Builder {
   private KeyCombination.ModifierValue alt;
   private String character;
   private KeyCombination.ModifierValue control;
   private KeyCombination.ModifierValue meta;
   private KeyCombination.ModifierValue shift;
   private KeyCombination.ModifierValue shortcut;

   protected KeyCharacterCombinationBuilder() {
   }

   public static KeyCharacterCombinationBuilder create() {
      return new KeyCharacterCombinationBuilder();
   }

   public KeyCharacterCombinationBuilder alt(KeyCombination.ModifierValue var1) {
      this.alt = var1;
      return this;
   }

   public KeyCharacterCombinationBuilder character(String var1) {
      this.character = var1;
      return this;
   }

   public KeyCharacterCombinationBuilder control(KeyCombination.ModifierValue var1) {
      this.control = var1;
      return this;
   }

   public KeyCharacterCombinationBuilder meta(KeyCombination.ModifierValue var1) {
      this.meta = var1;
      return this;
   }

   public KeyCharacterCombinationBuilder shift(KeyCombination.ModifierValue var1) {
      this.shift = var1;
      return this;
   }

   public KeyCharacterCombinationBuilder shortcut(KeyCombination.ModifierValue var1) {
      this.shortcut = var1;
      return this;
   }

   public KeyCharacterCombination build() {
      KeyCharacterCombination var1 = new KeyCharacterCombination(this.character, this.shift, this.control, this.alt, this.meta, this.shortcut);
      return var1;
   }
}
