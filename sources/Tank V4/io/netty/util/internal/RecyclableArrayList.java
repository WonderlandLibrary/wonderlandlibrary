package io.netty.util.internal;

import io.netty.util.Recycler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

public final class RecyclableArrayList extends ArrayList {
   private static final long serialVersionUID = -8605125654176467947L;
   private static final int DEFAULT_INITIAL_CAPACITY = 8;
   private static final Recycler RECYCLER = new Recycler() {
      protected RecyclableArrayList newObject(Recycler.Handle var1) {
         return new RecyclableArrayList(var1);
      }

      protected Object newObject(Recycler.Handle var1) {
         return this.newObject(var1);
      }
   };
   private final Recycler.Handle handle;

   public static RecyclableArrayList newInstance() {
      return newInstance(8);
   }

   public static RecyclableArrayList newInstance(int var0) {
      RecyclableArrayList var1 = (RecyclableArrayList)RECYCLER.get();
      var1.ensureCapacity(var0);
      return var1;
   }

   private RecyclableArrayList(Recycler.Handle var1) {
      this(var1, 8);
   }

   private RecyclableArrayList(Recycler.Handle var1, int var2) {
      super(var2);
      this.handle = var1;
   }

   public boolean addAll(Collection var1) {
      checkNullElements(var1);
      return super.addAll(var1);
   }

   public boolean addAll(int var1, Collection var2) {
      checkNullElements(var2);
      return super.addAll(var1, var2);
   }

   private static void checkNullElements(Collection var0) {
      if (var0 instanceof RandomAccess && var0 instanceof List) {
         List var4 = (List)var0;
         int var5 = var4.size();

         for(int var3 = 0; var3 < var5; ++var3) {
            if (var4.get(var3) == null) {
               throw new IllegalArgumentException("c contains null values");
            }
         }
      } else {
         Iterator var1 = var0.iterator();

         while(var1.hasNext()) {
            Object var2 = var1.next();
            if (var2 == null) {
               throw new IllegalArgumentException("c contains null values");
            }
         }
      }

   }

   public boolean add(Object var1) {
      if (var1 == null) {
         throw new NullPointerException("element");
      } else {
         return super.add(var1);
      }
   }

   public void add(int var1, Object var2) {
      if (var2 == null) {
         throw new NullPointerException("element");
      } else {
         super.add(var1, var2);
      }
   }

   public Object set(int var1, Object var2) {
      if (var2 == null) {
         throw new NullPointerException("element");
      } else {
         return super.set(var1, var2);
      }
   }

   public boolean recycle() {
      this.clear();
      return RECYCLER.recycle(this, this.handle);
   }

   RecyclableArrayList(Recycler.Handle var1, Object var2) {
      this(var1);
   }
}
