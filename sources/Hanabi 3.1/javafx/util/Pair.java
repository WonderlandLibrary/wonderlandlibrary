package javafx.util;

import java.io.Serializable;
import javafx.beans.NamedArg;

public class Pair implements Serializable {
   private Object key;
   private Object value;

   public Object getKey() {
      return this.key;
   }

   public Object getValue() {
      return this.value;
   }

   public Pair(@NamedArg("key") Object var1, @NamedArg("value") Object var2) {
      this.key = var1;
      this.value = var2;
   }

   public String toString() {
      return this.key + "=" + this.value;
   }

   public int hashCode() {
      return this.key.hashCode() * 13 + (this.value == null ? 0 : this.value.hashCode());
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Pair)) {
         return false;
      } else {
         Pair var2 = (Pair)var1;
         if (this.key != null) {
            if (!this.key.equals(var2.key)) {
               return false;
            }
         } else if (var2.key != null) {
            return false;
         }

         if (this.value != null) {
            if (!this.value.equals(var2.value)) {
               return false;
            }
         } else if (var2.value != null) {
            return false;
         }

         return true;
      }
   }
}
