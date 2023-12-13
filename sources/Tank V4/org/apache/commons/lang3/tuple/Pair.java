package org.apache.commons.lang3.tuple;

import java.io.Serializable;
import java.util.Map.Entry;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

public abstract class Pair implements Entry, Comparable, Serializable {
   private static final long serialVersionUID = 4954918890077093841L;

   public static Pair of(Object var0, Object var1) {
      return new ImmutablePair(var0, var1);
   }

   public abstract Object getLeft();

   public abstract Object getRight();

   public final Object getKey() {
      return this.getLeft();
   }

   public Object getValue() {
      return this.getRight();
   }

   public int compareTo(Pair var1) {
      return (new CompareToBuilder()).append(this.getLeft(), var1.getLeft()).append(this.getRight(), var1.getRight()).toComparison();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Entry)) {
         return false;
      } else {
         Entry var2 = (Entry)var1;
         return ObjectUtils.equals(this.getKey(), var2.getKey()) && ObjectUtils.equals(this.getValue(), var2.getValue());
      }
   }

   public int hashCode() {
      return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^ (this.getValue() == null ? 0 : this.getValue().hashCode());
   }

   public String toString() {
      return "" + '(' + this.getLeft() + ',' + this.getRight() + ')';
   }

   public String toString(String var1) {
      return String.format(var1, this.getLeft(), this.getRight());
   }

   public int compareTo(Object var1) {
      return this.compareTo((Pair)var1);
   }
}
