package org.apache.http.protocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
final class ChainBuilder {
   private final LinkedList list = new LinkedList();
   private final Map uniqueClasses = new HashMap();

   public ChainBuilder() {
   }

   private void ensureUnique(Object var1) {
      Object var2 = this.uniqueClasses.remove(var1.getClass());
      if (var2 != null) {
         this.list.remove(var2);
      }

      this.uniqueClasses.put(var1.getClass(), var1);
   }

   public ChainBuilder addFirst(Object var1) {
      if (var1 == null) {
         return this;
      } else {
         this.ensureUnique(var1);
         this.list.addFirst(var1);
         return this;
      }
   }

   public ChainBuilder addLast(Object var1) {
      if (var1 == null) {
         return this;
      } else {
         this.ensureUnique(var1);
         this.list.addLast(var1);
         return this;
      }
   }

   public ChainBuilder addAllFirst(Collection var1) {
      if (var1 == null) {
         return this;
      } else {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            this.addFirst(var3);
         }

         return this;
      }
   }

   public ChainBuilder addAllFirst(Object... var1) {
      if (var1 == null) {
         return this;
      } else {
         Object[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Object var5 = var2[var4];
            this.addFirst(var5);
         }

         return this;
      }
   }

   public ChainBuilder addAllLast(Collection var1) {
      if (var1 == null) {
         return this;
      } else {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            this.addLast(var3);
         }

         return this;
      }
   }

   public ChainBuilder addAllLast(Object... var1) {
      if (var1 == null) {
         return this;
      } else {
         Object[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Object var5 = var2[var4];
            this.addLast(var5);
         }

         return this;
      }
   }

   public LinkedList build() {
      return new LinkedList(this.list);
   }
}
