package com.ibm.icu.impl;

import java.util.Comparator;
import java.util.Iterator;

public class IterableComparator implements Comparator {
   private final Comparator comparator;
   private final int shorterFirst;
   private static final IterableComparator NOCOMPARATOR = new IterableComparator();

   public IterableComparator() {
      this((Comparator)null, true);
   }

   public IterableComparator(Comparator var1) {
      this(var1, true);
   }

   public IterableComparator(Comparator var1, boolean var2) {
      this.comparator = var1;
      this.shorterFirst = var2 ? 1 : -1;
   }

   public int compare(Iterable var1, Iterable var2) {
      if (var1 == null) {
         return var2 == null ? 0 : -this.shorterFirst;
      } else if (var2 == null) {
         return this.shorterFirst;
      } else {
         Iterator var3 = var1.iterator();
         Iterator var4 = var2.iterator();

         while(var3.hasNext()) {
            if (!var4.hasNext()) {
               return this.shorterFirst;
            }

            Object var5 = var3.next();
            Object var6 = var4.next();
            int var7 = this.comparator != null ? this.comparator.compare(var5, var6) : ((Comparable)var5).compareTo(var6);
            if (var7 != 0) {
               return var7;
            }
         }

         return var4.hasNext() ? -this.shorterFirst : 0;
      }
   }

   public static int compareIterables(Iterable var0, Iterable var1) {
      return NOCOMPARATOR.compare(var0, var1);
   }

   public int compare(Object var1, Object var2) {
      return this.compare((Iterable)var1, (Iterable)var2);
   }
}
