package io.netty.util;

import java.util.IdentityHashMap;
import java.util.Map;

public abstract class Recycler {
   private final ThreadLocal threadLocal = new ThreadLocal(this) {
      final Recycler this$0;

      {
         this.this$0 = var1;
      }

      protected Recycler.Stack initialValue() {
         return new Recycler.Stack(this.this$0, Thread.currentThread());
      }

      protected Object initialValue() {
         return this.initialValue();
      }
   };

   public final Object get() {
      Recycler.Stack var1 = (Recycler.Stack)this.threadLocal.get();
      Object var2 = var1.pop();
      if (var2 == null) {
         var2 = this.newObject(var1);
      }

      return var2;
   }

   public final boolean recycle(Object var1, Recycler.Handle var2) {
      Recycler.Stack var3 = (Recycler.Stack)var2;
      if (var3.parent != this) {
         return false;
      } else if (Thread.currentThread() != var3.thread) {
         return false;
      } else {
         var3.push(var1);
         return true;
      }
   }

   protected abstract Object newObject(Recycler.Handle var1);

   static final class Stack implements Recycler.Handle {
      private static final int INITIAL_CAPACITY = 256;
      final Recycler parent;
      final Thread thread;
      private Object[] elements;
      private int size;
      private final Map map = new IdentityHashMap(256);

      Stack(Recycler var1, Thread var2) {
         this.parent = var1;
         this.thread = var2;
         this.elements = newArray(256);
      }

      Object pop() {
         int var1 = this.size;
         if (var1 == 0) {
            return null;
         } else {
            --var1;
            Object var2 = this.elements[var1];
            this.elements[var1] = null;
            this.map.remove(var2);
            this.size = var1;
            return var2;
         }
      }

      void push(Object var1) {
         if (this.map.put(var1, Boolean.TRUE) != null) {
            throw new IllegalStateException("recycled already");
         } else {
            int var2 = this.size;
            if (var2 == this.elements.length) {
               Object[] var3 = newArray(var2 << 1);
               System.arraycopy(this.elements, 0, var3, 0, var2);
               this.elements = var3;
            }

            this.elements[var2] = var1;
            this.size = var2 + 1;
         }
      }

      private static Object[] newArray(int var0) {
         return (Object[])(new Object[var0]);
      }
   }

   public interface Handle {
   }
}
