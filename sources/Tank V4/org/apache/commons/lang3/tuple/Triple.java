package org.apache.commons.lang3.tuple;

import java.io.Serializable;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

public abstract class Triple implements Comparable, Serializable {
   private static final long serialVersionUID = 1L;

   public static Triple of(Object var0, Object var1, Object var2) {
      return new ImmutableTriple(var0, var1, var2);
   }

   public abstract Object getLeft();

   public abstract Object getMiddle();

   public abstract Object getRight();

   public int compareTo(Triple var1) {
      return (new CompareToBuilder()).append(this.getLeft(), var1.getLeft()).append(this.getMiddle(), var1.getMiddle()).append(this.getRight(), var1.getRight()).toComparison();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof Triple)) {
         return false;
      } else {
         Triple var2 = (Triple)var1;
         return ObjectUtils.equals(this.getLeft(), var2.getLeft()) && ObjectUtils.equals(this.getMiddle(), var2.getMiddle()) && ObjectUtils.equals(this.getRight(), var2.getRight());
      }
   }

   public int hashCode() {
      return (this.getLeft() == null ? 0 : this.getLeft().hashCode()) ^ (this.getMiddle() == null ? 0 : this.getMiddle().hashCode()) ^ (this.getRight() == null ? 0 : this.getRight().hashCode());
   }

   public String toString() {
      return "" + '(' + this.getLeft() + ',' + this.getMiddle() + ',' + this.getRight() + ')';
   }

   public String toString(String var1) {
      return String.format(var1, this.getLeft(), this.getMiddle(), this.getRight());
   }

   public int compareTo(Object var1) {
      return this.compareTo((Triple)var1);
   }
}
