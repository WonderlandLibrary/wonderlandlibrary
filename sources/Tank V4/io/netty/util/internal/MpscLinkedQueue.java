package io.netty.util.internal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

final class MpscLinkedQueue extends MpscLinkedQueueTailRef implements Queue {
   private static final long serialVersionUID = -1878402552271506449L;
   long p00;
   long p01;
   long p02;
   long p03;
   long p04;
   long p05;
   long p06;
   long p07;
   long p30;
   long p31;
   long p32;
   long p33;
   long p34;
   long p35;
   long p36;
   long p37;

   MpscLinkedQueue() {
      MpscLinkedQueue.DefaultNode var1 = new MpscLinkedQueue.DefaultNode((Object)null);
      this.setHeadRef(var1);
      this.setTailRef(var1);
   }

   private MpscLinkedQueueNode peekNode() {
      MpscLinkedQueueNode var1;
      do {
         var1 = this.headRef();
         MpscLinkedQueueNode var2 = var1.next();
         if (var2 != null) {
            return var2;
         }
      } while(var1 != this.tailRef());

      return null;
   }

   public Object poll() {
      MpscLinkedQueueNode var1 = this.peekNode();
      if (var1 == null) {
         return null;
      } else {
         MpscLinkedQueueNode var2 = this.headRef();
         this.lazySetHeadRef(var1);
         var2.unlink();
         return var1.clearMaybe();
      }
   }

   public Object peek() {
      MpscLinkedQueueNode var1 = this.peekNode();
      return var1 == null ? null : var1.value();
   }

   public int size() {
      int var1 = 0;

      for(MpscLinkedQueueNode var2 = this.peekNode(); var2 != null; var2 = var2.next()) {
         ++var1;
      }

      return var1;
   }

   public boolean isEmpty() {
      return this.peekNode() == null;
   }

   public Iterator iterator() {
      return new Iterator(this) {
         private MpscLinkedQueueNode node;
         final MpscLinkedQueue this$0;

         {
            this.this$0 = var1;
            this.node = MpscLinkedQueue.access$000(this.this$0);
         }

         public boolean hasNext() {
            return this.node != null;
         }

         public Object next() {
            MpscLinkedQueueNode var1 = this.node;
            if (var1 == null) {
               throw new NoSuchElementException();
            } else {
               Object var2 = var1.value();
               this.node = var1.next();
               return var2;
            }
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public boolean add(Object var1) {
      if (var1 == null) {
         return true;
      } else {
         throw new IllegalStateException("queue full");
      }
   }

   public Object remove() {
      Object var1 = this.poll();
      if (var1 != null) {
         return var1;
      } else {
         throw new NoSuchElementException();
      }
   }

   public Object element() {
      Object var1 = this.peek();
      if (var1 != null) {
         return var1;
      } else {
         throw new NoSuchElementException();
      }
   }

   public Object[] toArray() {
      Object[] var1 = new Object[this.size()];
      Iterator var2 = this.iterator();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (!var2.hasNext()) {
            return Arrays.copyOf(var1, var3);
         }

         var1[var3] = var2.next();
      }

      return var1;
   }

   public Object[] toArray(Object[] var1) {
      int var2 = this.size();
      Object[] var3;
      if (var1.length >= var2) {
         var3 = var1;
      } else {
         var3 = (Object[])((Object[])Array.newInstance(var1.getClass().getComponentType(), var2));
      }

      Iterator var4 = this.iterator();

      for(int var5 = 0; var5 < var3.length; ++var5) {
         if (!var4.hasNext()) {
            if (var1 == var3) {
               var3[var5] = null;
               return var3;
            }

            if (var1.length < var5) {
               return Arrays.copyOf(var3, var5);
            }

            System.arraycopy(var3, 0, var1, 0, var5);
            if (var1.length > var5) {
               var1[var5] = null;
            }

            return var1;
         }

         var3[var5] = var4.next();
      }

      return var3;
   }

   public boolean remove(Object var1) {
      throw new UnsupportedOperationException();
   }

   public boolean containsAll(Collection var1) {
      Iterator var2 = var1.iterator();

      Object var3;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         var3 = var2.next();
      } while(var3 != null);

      return false;
   }

   public boolean addAll(Collection var1) {
      if (var1 == null) {
         throw new NullPointerException("c");
      } else if (var1 == this) {
         throw new IllegalArgumentException("c == this");
      } else {
         boolean var2 = false;

         for(Iterator var3 = var1.iterator(); var3.hasNext(); var2 = true) {
            Object var4 = var3.next();
            this.add(var4);
         }

         return var2;
      }
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      while(this.poll() != null) {
      }

   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         var1.writeObject(var3);
      }

      var1.writeObject((Object)null);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      MpscLinkedQueue.DefaultNode var2 = new MpscLinkedQueue.DefaultNode((Object)null);
      this.setHeadRef(var2);
      this.setTailRef(var2);

      while(true) {
         Object var3 = var1.readObject();
         if (var3 == null) {
            return;
         }

         this.add(var3);
      }
   }

   static MpscLinkedQueueNode access$000(MpscLinkedQueue var0) {
      return var0.peekNode();
   }

   private static final class DefaultNode extends MpscLinkedQueueNode {
      private Object value;

      DefaultNode(Object var1) {
         this.value = var1;
      }

      public Object value() {
         return this.value;
      }

      protected Object clearMaybe() {
         Object var1 = this.value;
         this.value = null;
         return var1;
      }
   }
}
