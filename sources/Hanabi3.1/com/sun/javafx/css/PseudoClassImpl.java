package com.sun.javafx.css;

import javafx.css.PseudoClass;

final class PseudoClassImpl extends PseudoClass {
   private final String pseudoClassName;
   private final int index;

   PseudoClassImpl(String var1, int var2) {
      this.pseudoClassName = var1;
      this.index = var2;
   }

   public String getPseudoClassName() {
      return this.pseudoClassName;
   }

   public String toString() {
      return this.pseudoClassName;
   }

   public int getIndex() {
      return this.index;
   }
}
