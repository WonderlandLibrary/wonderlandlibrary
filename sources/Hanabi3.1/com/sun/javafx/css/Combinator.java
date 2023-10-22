package com.sun.javafx.css;

public enum Combinator {
   CHILD {
      public String toString() {
         return ">";
      }
   },
   DESCENDANT {
      public String toString() {
         return " ";
      }
   };

   private Combinator() {
   }

   // $FF: synthetic method
   Combinator(Object var3) {
      this();
   }
}
