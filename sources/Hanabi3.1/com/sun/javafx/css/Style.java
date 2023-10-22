package com.sun.javafx.css;

public final class Style {
   private final Selector selector;
   private final Declaration declaration;

   public Selector getSelector() {
      return this.selector;
   }

   public Declaration getDeclaration() {
      return this.declaration;
   }

   public Style(Selector var1, Declaration var2) {
      this.selector = var1;
      this.declaration = var2;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         Style var2 = (Style)var1;
         if (this.selector == var2.selector || this.selector != null && this.selector.equals(var2.selector)) {
            return this.declaration == var2.declaration || this.declaration != null && this.declaration.equals(var2.declaration);
         } else {
            return false;
         }
      }
   }

   public int hashCode() {
      int var1 = 3;
      var1 = 83 * var1 + (this.selector != null ? this.selector.hashCode() : 0);
      var1 = 83 * var1 + (this.declaration != null ? this.declaration.hashCode() : 0);
      return var1;
   }

   public String toString() {
      StringBuilder var1 = (new StringBuilder()).append(String.valueOf(this.selector)).append(" { ").append(String.valueOf(this.declaration)).append(" } ");
      return var1.toString();
   }
}
