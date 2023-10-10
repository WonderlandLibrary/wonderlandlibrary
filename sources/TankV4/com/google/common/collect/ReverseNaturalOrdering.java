package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Iterator;

@GwtCompatible(
   serializable = true
)
final class ReverseNaturalOrdering extends Ordering implements Serializable {
   static final ReverseNaturalOrdering INSTANCE = new ReverseNaturalOrdering();
   private static final long serialVersionUID = 0L;

   public int compare(Comparable var1, Comparable var2) {
      Preconditions.checkNotNull(var1);
      return var1 == var2 ? 0 : var2.compareTo(var1);
   }

   public Ordering reverse() {
      return Ordering.natural();
   }

   public Comparable min(Comparable var1, Comparable var2) {
      return (Comparable)NaturalOrdering.INSTANCE.max(var1, var2);
   }

   public Comparable min(Comparable var1, Comparable var2, Comparable var3, Comparable... var4) {
      return (Comparable)NaturalOrdering.INSTANCE.max(var1, var2, var3, var4);
   }

   public Comparable min(Iterator var1) {
      return (Comparable)NaturalOrdering.INSTANCE.max(var1);
   }

   public Comparable min(Iterable var1) {
      return (Comparable)NaturalOrdering.INSTANCE.max(var1);
   }

   public Comparable max(Comparable var1, Comparable var2) {
      return (Comparable)NaturalOrdering.INSTANCE.min(var1, var2);
   }

   public Comparable max(Comparable var1, Comparable var2, Comparable var3, Comparable... var4) {
      return (Comparable)NaturalOrdering.INSTANCE.min(var1, var2, var3, var4);
   }

   public Comparable max(Iterator var1) {
      return (Comparable)NaturalOrdering.INSTANCE.min(var1);
   }

   public Comparable max(Iterable var1) {
      return (Comparable)NaturalOrdering.INSTANCE.min(var1);
   }

   private Object readResolve() {
      return INSTANCE;
   }

   public String toString() {
      return "Ordering.natural().reverse()";
   }

   private ReverseNaturalOrdering() {
   }

   public Object max(Object var1, Object var2, Object var3, Object[] var4) {
      return this.max((Comparable)var1, (Comparable)var2, (Comparable)var3, (Comparable[])var4);
   }

   public Object max(Object var1, Object var2) {
      return this.max((Comparable)var1, (Comparable)var2);
   }

   public Object max(Iterable var1) {
      return this.max(var1);
   }

   public Object max(Iterator var1) {
      return this.max(var1);
   }

   public Object min(Object var1, Object var2, Object var3, Object[] var4) {
      return this.min((Comparable)var1, (Comparable)var2, (Comparable)var3, (Comparable[])var4);
   }

   public Object min(Object var1, Object var2) {
      return this.min((Comparable)var1, (Comparable)var2);
   }

   public Object min(Iterable var1) {
      return this.min(var1);
   }

   public Object min(Iterator var1) {
      return this.min(var1);
   }

   public int compare(Object var1, Object var2) {
      return this.compare((Comparable)var1, (Comparable)var2);
   }
}
