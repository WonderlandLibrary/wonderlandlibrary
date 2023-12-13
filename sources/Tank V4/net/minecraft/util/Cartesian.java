package net.minecraft.util;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Cartesian {
   private static Object[] toArray(Class var0, Iterable var1) {
      ArrayList var2 = Lists.newArrayList();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Object var3 = (Object)var4.next();
         var2.add(var3);
      }

      return var2.toArray(createArray(var0, var2.size()));
   }

   private static Iterable arraysAsLists(Iterable var0) {
      return Iterables.transform(var0, new Cartesian.GetList((Cartesian.GetList)null));
   }

   static Object[] access$0(Class var0, int var1) {
      return createArray(var0, var1);
   }

   private static Object[] createArray(Class var0, int var1) {
      return (Object[])Array.newInstance(var0, var1);
   }

   public static Iterable cartesianProduct(Class var0, Iterable var1) {
      return new Cartesian.Product(var0, (Iterable[])toArray(Iterable.class, var1), (Cartesian.Product)null);
   }

   public static Iterable cartesianProduct(Iterable var0) {
      return arraysAsLists(cartesianProduct(Object.class, var0));
   }

   static class GetList implements Function {
      public Object apply(Object var1) {
         return this.apply((Object[])var1);
      }

      private GetList() {
      }

      public List apply(Object[] var1) {
         return Arrays.asList(var1);
      }

      GetList(Cartesian.GetList var1) {
         this();
      }
   }

   static class Product implements Iterable {
      private final Class clazz;
      private final Iterable[] iterables;

      Product(Class var1, Iterable[] var2, Cartesian.Product var3) {
         this(var1, var2);
      }

      public Iterator iterator() {
         return (Iterator)(this.iterables.length <= 0 ? Collections.singletonList(Cartesian.access$0(this.clazz, 0)).iterator() : new Cartesian.Product.ProductIterator(this.clazz, this.iterables, (Cartesian.Product.ProductIterator)null));
      }

      private Product(Class var1, Iterable[] var2) {
         this.clazz = var1;
         this.iterables = var2;
      }

      static class ProductIterator extends UnmodifiableIterator {
         private final Object[] results;
         private int index;
         private final Iterator[] iterators;
         private final Iterable[] iterables;

         public Object next() {
            return this.next();
         }

         ProductIterator(Class var1, Iterable[] var2, Cartesian.Product.ProductIterator var3) {
            this(var1, var2);
         }

         public Object[] next() {
            if (this != false) {
               throw new NoSuchElementException();
            } else {
               while(this.index < this.iterators.length) {
                  this.results[this.index] = this.iterators[this.index].next();
                  ++this.index;
               }

               return (Object[])this.results.clone();
            }
         }

         private void endOfData() {
            this.index = -1;
            Arrays.fill(this.iterators, (Object)null);
            Arrays.fill(this.results, (Object)null);
         }

         private ProductIterator(Class var1, Iterable[] var2) {
            this.index = -2;
            this.iterables = var2;
            this.iterators = (Iterator[])Cartesian.access$0(Iterator.class, this.iterables.length);

            for(int var3 = 0; var3 < this.iterables.length; ++var3) {
               this.iterators[var3] = var2[var3].iterator();
            }

            this.results = Cartesian.access$0(var1, this.iterators.length);
         }
      }
   }
}
