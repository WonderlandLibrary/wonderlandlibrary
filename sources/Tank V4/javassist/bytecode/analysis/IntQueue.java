package javassist.bytecode.analysis;

import java.util.NoSuchElementException;

class IntQueue {
   private IntQueue.Entry head;
   private IntQueue.Entry tail;

   void add(int var1) {
      IntQueue.Entry var2 = new IntQueue.Entry(var1);
      if (this.tail != null) {
         IntQueue.Entry.access$102(this.tail, var2);
      }

      this.tail = var2;
      if (this.head == null) {
         this.head = var2;
      }

   }

   boolean isEmpty() {
      return this.head == null;
   }

   int take() {
      if (this.head == null) {
         throw new NoSuchElementException();
      } else {
         int var1 = IntQueue.Entry.access$200(this.head);
         this.head = IntQueue.Entry.access$100(this.head);
         if (this.head == null) {
            this.tail = null;
         }

         return var1;
      }
   }

   private static class Entry {
      private IntQueue.Entry next;
      private int value;

      private Entry(int var1) {
         this.value = var1;
      }

      Entry(int var1, Object var2) {
         this(var1);
      }

      static IntQueue.Entry access$102(IntQueue.Entry var0, IntQueue.Entry var1) {
         return var0.next = var1;
      }

      static int access$200(IntQueue.Entry var0) {
         return var0.value;
      }

      static IntQueue.Entry access$100(IntQueue.Entry var0) {
         return var0.next;
      }
   }
}
