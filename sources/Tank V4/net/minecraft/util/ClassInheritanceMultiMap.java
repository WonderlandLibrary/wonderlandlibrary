package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassInheritanceMultiMap extends AbstractSet {
   private final Map map = Maps.newHashMap();
   private final Set knownKeys = Sets.newIdentityHashSet();
   private final Class baseClass;
   private static final Set field_181158_a = Sets.newHashSet();
   private final List field_181745_e = Lists.newArrayList();

   public boolean add(Object var1) {
      Iterator var3 = this.knownKeys.iterator();

      while(var3.hasNext()) {
         Class var2 = (Class)var3.next();
         if (var2.isAssignableFrom(var1.getClass())) {
            this.func_181743_a(var1, var2);
         }
      }

      return true;
   }

   public Iterable getByClass(Class var1) {
      return new Iterable(this, var1) {
         final ClassInheritanceMultiMap this$0;
         private final Class val$clazz;

         public Iterator iterator() {
            List var1 = (List)ClassInheritanceMultiMap.access$0(this.this$0).get(this.this$0.func_181157_b(this.val$clazz));
            if (var1 == null) {
               return Iterators.emptyIterator();
            } else {
               Iterator var2 = var1.iterator();
               return Iterators.filter(var2, this.val$clazz);
            }
         }

         {
            this.this$0 = var1;
            this.val$clazz = var2;
         }
      };
   }

   private void func_181743_a(Object var1, Class var2) {
      List var3 = (List)this.map.get(var2);
      if (var3 == null) {
         this.map.put(var2, Lists.newArrayList(var1));
      } else {
         var3.add(var1);
      }

   }

   protected void createLookup(Class var1) {
      field_181158_a.add(var1);
      Iterator var3 = this.field_181745_e.iterator();

      while(var3.hasNext()) {
         Object var2 = (Object)var3.next();
         if (var1.isAssignableFrom(var2.getClass())) {
            this.func_181743_a(var2, var1);
         }
      }

      this.knownKeys.add(var1);
   }

   public boolean remove(Object var1) {
      Object var2 = var1;
      boolean var3 = false;
      Iterator var5 = this.knownKeys.iterator();

      while(var5.hasNext()) {
         Class var4 = (Class)var5.next();
         if (var4.isAssignableFrom(var2.getClass())) {
            List var6 = (List)this.map.get(var4);
            if (var6 != null && var6.remove(var2)) {
               var3 = true;
            }
         }
      }

      return var3;
   }

   public boolean contains(Object var1) {
      return Iterators.contains(this.getByClass(var1.getClass()).iterator(), var1);
   }

   public Iterator iterator() {
      return this.field_181745_e.isEmpty() ? Iterators.emptyIterator() : Iterators.unmodifiableIterator(this.field_181745_e.iterator());
   }

   public ClassInheritanceMultiMap(Class var1) {
      this.baseClass = var1;
      this.knownKeys.add(var1);
      this.map.put(var1, this.field_181745_e);
      Iterator var3 = field_181158_a.iterator();

      while(var3.hasNext()) {
         Class var2 = (Class)var3.next();
         this.createLookup(var2);
      }

   }

   public int size() {
      return this.field_181745_e.size();
   }

   static Map access$0(ClassInheritanceMultiMap var0) {
      return var0.map;
   }

   protected Class func_181157_b(Class var1) {
      if (this.baseClass.isAssignableFrom(var1)) {
         if (!this.knownKeys.contains(var1)) {
            this.createLookup(var1);
         }

         return var1;
      } else {
         throw new IllegalArgumentException("Don't know how to search for " + var1);
      }
   }
}
