package io.netty.util;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/** @deprecated */
@Deprecated
public class UniqueName implements Comparable {
   private static final AtomicInteger nextId = new AtomicInteger();
   private final int id;
   private final String name;

   public UniqueName(ConcurrentMap var1, String var2, Object... var3) {
      if (var1 == null) {
         throw new NullPointerException("map");
      } else if (var2 == null) {
         throw new NullPointerException("name");
      } else {
         if (var3 != null && var3.length > 0) {
            this.validateArgs(var3);
         }

         if (var1.putIfAbsent(var2, Boolean.TRUE) != null) {
            throw new IllegalArgumentException(String.format("'%s' is already in use", var2));
         } else {
            this.id = nextId.incrementAndGet();
            this.name = var2;
         }
      }
   }

   protected void validateArgs(Object... var1) {
   }

   public final String name() {
      return this.name;
   }

   public final int id() {
      return this.id;
   }

   public final int hashCode() {
      return super.hashCode();
   }

   public final boolean equals(Object var1) {
      return super.equals(var1);
   }

   public int compareTo(UniqueName var1) {
      if (this == var1) {
         return 0;
      } else {
         int var2 = this.name.compareTo(var1.name);
         return var2 != 0 ? var2 : Integer.valueOf(this.id).compareTo(var1.id);
      }
   }

   public String toString() {
      return this.name();
   }

   public int compareTo(Object var1) {
      return this.compareTo((UniqueName)var1);
   }
}
