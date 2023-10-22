package com.sun.javafx.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Iterator;

public class WeakReferenceQueue {
   private final ReferenceQueue garbage = new ReferenceQueue();
   private Object strongRef = new Object();
   private ListEntry head;
   int size;

   public WeakReferenceQueue() {
      this.head = new ListEntry(this.strongRef, this.garbage);
      this.size = 0;
   }

   public void add(Object var1) {
      this.cleanup();
      ++this.size;
      (new ListEntry(var1, this.garbage)).insert(this.head.prev);
   }

   public void remove(Object var1) {
      this.cleanup();

      for(ListEntry var2 = this.head.next; var2 != this.head; var2 = var2.next) {
         Object var3 = var2.get();
         if (var3 == var1) {
            --this.size;
            var2.remove();
            return;
         }
      }

   }

   public void cleanup() {
      ListEntry var1;
      while((var1 = (ListEntry)this.garbage.poll()) != null) {
         --this.size;
         var1.remove();
      }

   }

   public Iterator iterator() {
      return new Iterator() {
         private ListEntry index;
         private Object next;

         {
            this.index = WeakReferenceQueue.this.head;
            this.next = null;
         }

         public boolean hasNext() {
            this.next = null;

            while(this.next == null) {
               ListEntry var1 = this.index.prev;
               if (var1 == WeakReferenceQueue.this.head) {
                  break;
               }

               this.next = var1.get();
               if (this.next == null) {
                  --WeakReferenceQueue.this.size;
                  var1.remove();
               }
            }

            return this.next != null;
         }

         public Object next() {
            this.hasNext();
            this.index = this.index.prev;
            return this.next;
         }

         public void remove() {
            if (this.index != WeakReferenceQueue.this.head) {
               ListEntry var1 = this.index.next;
               --WeakReferenceQueue.this.size;
               this.index.remove();
               this.index = var1;
            }

         }
      };
   }

   private static class ListEntry extends WeakReference {
      ListEntry prev = this;
      ListEntry next = this;

      public ListEntry(Object var1, ReferenceQueue var2) {
         super(var1, var2);
      }

      public void insert(ListEntry var1) {
         this.prev = var1;
         this.next = var1.next;
         var1.next = this;
         this.next.prev = this;
      }

      public void remove() {
         this.prev.next = this.next;
         this.next.prev = this.prev;
         this.next = this;
         this.prev = this;
      }
   }
}
