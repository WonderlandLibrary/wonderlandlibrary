package com.sun.javafx.css;

import javafx.css.StyleOrigin;

public final class CalculatedValue {
   public static final CalculatedValue SKIP = new CalculatedValue(new int[0], (StyleOrigin)null, false);
   private final Object value;
   private final StyleOrigin origin;
   private final boolean relative;

   public CalculatedValue(Object var1, StyleOrigin var2, boolean var3) {
      this.value = var1;
      this.origin = var2;
      this.relative = var3;
   }

   public Object getValue() {
      return this.value;
   }

   public StyleOrigin getOrigin() {
      return this.origin;
   }

   public boolean isRelative() {
      return this.relative;
   }

   public String toString() {
      return '{' + String.valueOf(this.value) + ", " + this.origin + ", " + this.relative + '}';
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         CalculatedValue var2 = (CalculatedValue)var1;
         if (this.relative != var2.relative) {
            return false;
         } else if (this.origin != var2.origin) {
            return false;
         } else {
            if (this.value == null) {
               if (var2.value != null) {
                  return false;
               }
            } else if (!this.value.equals(var2.value)) {
               return false;
            }

            return true;
         }
      }
   }
}
