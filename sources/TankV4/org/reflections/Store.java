package org.reflections;

import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Store {
   private transient boolean concurrent;
   private final Map storeMap = new HashMap();

   protected Store() {
      this.concurrent = false;
   }

   public Store(Configuration var1) {
      this.concurrent = var1.getExecutorService() != null;
   }

   public Set keySet() {
      return this.storeMap.keySet();
   }

   public Multimap getOrCreate(String var1) {
      Object var2 = (Multimap)this.storeMap.get(var1);
      if (var2 == null) {
         SetMultimap var3 = Multimaps.newSetMultimap(new HashMap(), new Supplier(this) {
            final Store this$0;

            {
               this.this$0 = var1;
            }

            public Set get() {
               return Sets.newSetFromMap(new ConcurrentHashMap());
            }

            public Object get() {
               return this.get();
            }
         });
         var2 = this.concurrent ? Multimaps.synchronizedSetMultimap(var3) : var3;
         this.storeMap.put(var1, var2);
      }

      return (Multimap)var2;
   }

   public Multimap get(String var1) {
      Multimap var2 = (Multimap)this.storeMap.get(var1);
      if (var2 == null) {
         throw new ReflectionsException("Scanner " + var1 + " was not configured");
      } else {
         return var2;
      }
   }

   public Iterable get(String var1, String... var2) {
      return this.get(var1, (Iterable)Arrays.asList(var2));
   }

   public Iterable get(String var1, Iterable var2) {
      Multimap var3 = this.get(var1);
      Store.IterableChain var4 = new Store.IterableChain();
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         Store.IterableChain.access$100(var4, var3.get(var6));
      }

      return var4;
   }

   private Iterable getAllIncluding(String var1, Iterable var2, Store.IterableChain var3) {
      Store.IterableChain.access$100(var3, var2);
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         Iterable var6 = this.get(var1, var5);
         if (var6.iterator().hasNext()) {
            this.getAllIncluding(var1, var6, var3);
         }
      }

      return var3;
   }

   public Iterable getAll(String var1, String var2) {
      return this.getAllIncluding(var1, this.get(var1, var2), new Store.IterableChain());
   }

   public Iterable getAll(String var1, Iterable var2) {
      return this.getAllIncluding(var1, this.get(var1, var2), new Store.IterableChain());
   }

   private static class IterableChain implements Iterable {
      private final List chain;

      private IterableChain() {
         this.chain = Lists.newArrayList();
      }

      private void addAll(Iterable var1) {
         this.chain.add(var1);
      }

      public Iterator iterator() {
         return Iterables.concat((Iterable)this.chain).iterator();
      }

      IterableChain(Object var1) {
         this();
      }

      static void access$100(Store.IterableChain var0, Iterable var1) {
         var0.addAll(var1);
      }
   }
}
