package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

@Beta
public final class MinMaxPriorityQueue extends AbstractQueue {
   private final MinMaxPriorityQueue.Heap minHeap;
   private final MinMaxPriorityQueue.Heap maxHeap;
   @VisibleForTesting
   final int maximumSize;
   private Object[] queue;
   private int size;
   private int modCount;
   private static final int EVEN_POWERS_OF_TWO = 1431655765;
   private static final int ODD_POWERS_OF_TWO = -1431655766;
   private static final int DEFAULT_CAPACITY = 11;

   public static MinMaxPriorityQueue create() {
      return (new MinMaxPriorityQueue.Builder(Ordering.natural())).create();
   }

   public static MinMaxPriorityQueue create(Iterable var0) {
      return (new MinMaxPriorityQueue.Builder(Ordering.natural())).create(var0);
   }

   public static MinMaxPriorityQueue.Builder orderedBy(Comparator var0) {
      return new MinMaxPriorityQueue.Builder(var0);
   }

   public static MinMaxPriorityQueue.Builder expectedSize(int var0) {
      return (new MinMaxPriorityQueue.Builder(Ordering.natural())).expectedSize(var0);
   }

   public static MinMaxPriorityQueue.Builder maximumSize(int var0) {
      return (new MinMaxPriorityQueue.Builder(Ordering.natural())).maximumSize(var0);
   }

   private MinMaxPriorityQueue(MinMaxPriorityQueue.Builder var1, int var2) {
      Ordering var3 = MinMaxPriorityQueue.Builder.access$200(var1);
      this.minHeap = new MinMaxPriorityQueue.Heap(this, var3);
      this.maxHeap = new MinMaxPriorityQueue.Heap(this, var3.reverse());
      this.minHeap.otherHeap = this.maxHeap;
      this.maxHeap.otherHeap = this.minHeap;
      this.maximumSize = MinMaxPriorityQueue.Builder.access$300(var1);
      this.queue = new Object[var2];
   }

   public int size() {
      return this.size;
   }

   public boolean add(Object var1) {
      this.offer(var1);
      return true;
   }

   public boolean addAll(Collection var1) {
      boolean var2 = false;

      for(Iterator var3 = var1.iterator(); var3.hasNext(); var2 = true) {
         Object var4 = var3.next();
         this.offer(var4);
      }

      return var2;
   }

   public boolean offer(Object var1) {
      Preconditions.checkNotNull(var1);
      ++this.modCount;
      int var2 = this.size++;
      this.growIfNeeded();
      this.heapForIndex(var2).bubbleUp(var2, var1);
      return this.size <= this.maximumSize || this.pollLast() != var1;
   }

   public Object poll() {
      return this.isEmpty() ? null : this.removeAndGet(0);
   }

   Object elementData(int var1) {
      return this.queue[var1];
   }

   public Object peek() {
      return this.isEmpty() ? null : this.elementData(0);
   }

   private int getMaxElementIndex() {
      switch(this.size) {
      case 1:
         return 0;
      case 2:
         return 1;
      default:
         return this.maxHeap.compareElements(1, 2) <= 0 ? 1 : 2;
      }
   }

   public Object pollFirst() {
      return this.poll();
   }

   public Object removeFirst() {
      return this.remove();
   }

   public Object peekFirst() {
      return this.peek();
   }

   public Object pollLast() {
      return this.isEmpty() ? null : this.removeAndGet(this.getMaxElementIndex());
   }

   public Object removeLast() {
      if (this.isEmpty()) {
         throw new NoSuchElementException();
      } else {
         return this.removeAndGet(this.getMaxElementIndex());
      }
   }

   public Object peekLast() {
      return this.isEmpty() ? null : this.elementData(this.getMaxElementIndex());
   }

   @VisibleForTesting
   MinMaxPriorityQueue.MoveDesc removeAt(int var1) {
      Preconditions.checkPositionIndex(var1, this.size);
      ++this.modCount;
      --this.size;
      if (this.size == var1) {
         this.queue[this.size] = null;
         return null;
      } else {
         Object var2 = this.elementData(this.size);
         int var3 = this.heapForIndex(this.size).getCorrectLastElement(var2);
         Object var4 = this.elementData(this.size);
         this.queue[this.size] = null;
         MinMaxPriorityQueue.MoveDesc var5 = this.fillHole(var1, var4);
         if (var3 < var1) {
            return var5 == null ? new MinMaxPriorityQueue.MoveDesc(var2, var4) : new MinMaxPriorityQueue.MoveDesc(var2, var5.replaced);
         } else {
            return var5;
         }
      }
   }

   private MinMaxPriorityQueue.MoveDesc fillHole(int var1, Object var2) {
      MinMaxPriorityQueue.Heap var3 = this.heapForIndex(var1);
      int var4 = var3.fillHoleAt(var1);
      int var5 = var3.bubbleUpAlternatingLevels(var4, var2);
      if (var5 == var4) {
         return var3.tryCrossOverAndBubbleUp(var1, var4, var2);
      } else {
         return var5 < var1 ? new MinMaxPriorityQueue.MoveDesc(var2, this.elementData(var1)) : null;
      }
   }

   private Object removeAndGet(int var1) {
      Object var2 = this.elementData(var1);
      this.removeAt(var1);
      return var2;
   }

   private MinMaxPriorityQueue.Heap heapForIndex(int var1) {
      return var1 > 0 ? this.minHeap : this.maxHeap;
   }

   @VisibleForTesting
   boolean isIntact() {
      for(int var1 = 1; var1 < this.size; ++var1) {
         if (!MinMaxPriorityQueue.Heap.access$400(this.heapForIndex(var1), var1)) {
            return false;
         }
      }

      return true;
   }

   public Iterator iterator() {
      return new MinMaxPriorityQueue.QueueIterator(this);
   }

   public void clear() {
      for(int var1 = 0; var1 < this.size; ++var1) {
         this.queue[var1] = null;
      }

      this.size = 0;
   }

   public Object[] toArray() {
      Object[] var1 = new Object[this.size];
      System.arraycopy(this.queue, 0, var1, 0, this.size);
      return var1;
   }

   public Comparator comparator() {
      return this.minHeap.ordering;
   }

   @VisibleForTesting
   int capacity() {
      return this.queue.length;
   }

   @VisibleForTesting
   static int initialQueueSize(int var0, int var1, Iterable var2) {
      int var3 = var0 == -1 ? 11 : var0;
      if (var2 instanceof Collection) {
         int var4 = ((Collection)var2).size();
         var3 = Math.max(var3, var4);
      }

      return capAtMaximumSize(var3, var1);
   }

   private void growIfNeeded() {
      if (this.size > this.queue.length) {
         int var1 = this.calculateNewCapacity();
         Object[] var2 = new Object[var1];
         System.arraycopy(this.queue, 0, var2, 0, this.queue.length);
         this.queue = var2;
      }

   }

   private int calculateNewCapacity() {
      int var1 = this.queue.length;
      int var2 = var1 < 64 ? (var1 + 1) * 2 : IntMath.checkedMultiply(var1 / 2, 3);
      return capAtMaximumSize(var2, this.maximumSize);
   }

   private static int capAtMaximumSize(int var0, int var1) {
      return Math.min(var0 - 1, var1) + 1;
   }

   MinMaxPriorityQueue(MinMaxPriorityQueue.Builder var1, int var2, Object var3) {
      this(var1, var2);
   }

   static Object[] access$500(MinMaxPriorityQueue var0) {
      return var0.queue;
   }

   static int access$600(MinMaxPriorityQueue var0) {
      return var0.size;
   }

   static int access$700(MinMaxPriorityQueue var0) {
      return var0.modCount;
   }

   private class QueueIterator implements Iterator {
      private int cursor;
      private int expectedModCount;
      private Queue forgetMeNot;
      private List skipMe;
      private Object lastFromForgetMeNot;
      private boolean canRemove;
      final MinMaxPriorityQueue this$0;

      private QueueIterator(MinMaxPriorityQueue var1) {
         this.this$0 = var1;
         this.cursor = -1;
         this.expectedModCount = MinMaxPriorityQueue.access$700(this.this$0);
      }

      public boolean hasNext() {
         this.checkModCount();
         return this.nextNotInSkipMe(this.cursor + 1) < this.this$0.size() || this.forgetMeNot != null && !this.forgetMeNot.isEmpty();
      }

      public Object next() {
         this.checkModCount();
         int var1 = this.nextNotInSkipMe(this.cursor + 1);
         if (var1 < this.this$0.size()) {
            this.cursor = var1;
            this.canRemove = true;
            return this.this$0.elementData(this.cursor);
         } else {
            if (this.forgetMeNot != null) {
               this.cursor = this.this$0.size();
               this.lastFromForgetMeNot = this.forgetMeNot.poll();
               if (this.lastFromForgetMeNot != null) {
                  this.canRemove = true;
                  return this.lastFromForgetMeNot;
               }
            }

            throw new NoSuchElementException("iterator moved past last element in queue.");
         }
      }

      public void remove() {
         CollectPreconditions.checkRemove(this.canRemove);
         this.checkModCount();
         this.canRemove = false;
         ++this.expectedModCount;
         if (this.cursor < this.this$0.size()) {
            MinMaxPriorityQueue.MoveDesc var1 = this.this$0.removeAt(this.cursor);
            if (var1 != null) {
               if (this.forgetMeNot == null) {
                  this.forgetMeNot = new ArrayDeque();
                  this.skipMe = new ArrayList(3);
               }

               this.forgetMeNot.add(var1.toTrickle);
               this.skipMe.add(var1.replaced);
            }

            --this.cursor;
         } else {
            Preconditions.checkState(this.removeExact(this.lastFromForgetMeNot));
            this.lastFromForgetMeNot = null;
         }

      }

      boolean removeExact(Object var1) {
         for(int var2 = 0; var2 < MinMaxPriorityQueue.access$600(this.this$0); ++var2) {
            if (MinMaxPriorityQueue.access$500(this.this$0)[var2] == var1) {
               this.this$0.removeAt(var2);
               return true;
            }
         }

         return false;
      }

      void checkModCount() {
         if (MinMaxPriorityQueue.access$700(this.this$0) != this.expectedModCount) {
            throw new ConcurrentModificationException();
         }
      }

      private int nextNotInSkipMe(int var1) {
         if (this.skipMe != null) {
            while(var1 < this.this$0.size()) {
               List var10001 = this.skipMe;
               if (this.this$0.elementData(var1) == false) {
                  break;
               }

               ++var1;
            }
         }

         return var1;
      }

      QueueIterator(MinMaxPriorityQueue var1, Object var2) {
         this(var1);
      }
   }

   private class Heap {
      final Ordering ordering;
      MinMaxPriorityQueue.Heap otherHeap;
      final MinMaxPriorityQueue this$0;

      Heap(MinMaxPriorityQueue var1, Ordering var2) {
         this.this$0 = var1;
         this.ordering = var2;
      }

      int compareElements(int var1, int var2) {
         return this.ordering.compare(this.this$0.elementData(var1), this.this$0.elementData(var2));
      }

      MinMaxPriorityQueue.MoveDesc tryCrossOverAndBubbleUp(int var1, int var2, Object var3) {
         int var4 = this.crossOver(var2, var3);
         if (var4 == var2) {
            return null;
         } else {
            Object var5;
            if (var4 < var1) {
               var5 = this.this$0.elementData(var1);
            } else {
               var5 = this.this$0.elementData(this.getParentIndex(var1));
            }

            return this.otherHeap.bubbleUpAlternatingLevels(var4, var3) < var1 ? new MinMaxPriorityQueue.MoveDesc(var3, var5) : null;
         }
      }

      void bubbleUp(int var1, Object var2) {
         int var3 = this.crossOverUp(var1, var2);
         MinMaxPriorityQueue.Heap var4;
         if (var3 == var1) {
            var4 = this;
         } else {
            var1 = var3;
            var4 = this.otherHeap;
         }

         var4.bubbleUpAlternatingLevels(var1, var2);
      }

      int bubbleUpAlternatingLevels(int var1, Object var2) {
         while(true) {
            if (var1 > 2) {
               int var3 = this.getGrandparentIndex(var1);
               Object var4 = this.this$0.elementData(var3);
               if (this.ordering.compare(var4, var2) > 0) {
                  MinMaxPriorityQueue.access$500(this.this$0)[var1] = var4;
                  var1 = var3;
                  continue;
               }
            }

            MinMaxPriorityQueue.access$500(this.this$0)[var1] = var2;
            return var1;
         }
      }

      int findMin(int var1, int var2) {
         if (var1 >= MinMaxPriorityQueue.access$600(this.this$0)) {
            return -1;
         } else {
            Preconditions.checkState(var1 > 0);
            int var3 = Math.min(var1, MinMaxPriorityQueue.access$600(this.this$0) - var2) + var2;
            int var4 = var1;

            for(int var5 = var1 + 1; var5 < var3; ++var5) {
               if (this.compareElements(var5, var4) < 0) {
                  var4 = var5;
               }
            }

            return var4;
         }
      }

      int findMinChild(int var1) {
         return this.findMin(this.getLeftChildIndex(var1), 2);
      }

      int findMinGrandChild(int var1) {
         int var2 = this.getLeftChildIndex(var1);
         return var2 < 0 ? -1 : this.findMin(this.getLeftChildIndex(var2), 4);
      }

      int crossOverUp(int var1, Object var2) {
         if (var1 == 0) {
            MinMaxPriorityQueue.access$500(this.this$0)[0] = var2;
            return 0;
         } else {
            int var3 = this.getParentIndex(var1);
            Object var4 = this.this$0.elementData(var3);
            if (var3 != 0) {
               int var5 = this.getParentIndex(var3);
               int var6 = this.getRightChildIndex(var5);
               if (var6 != var3 && this.getLeftChildIndex(var6) >= MinMaxPriorityQueue.access$600(this.this$0)) {
                  Object var7 = this.this$0.elementData(var6);
                  if (this.ordering.compare(var7, var4) < 0) {
                     var3 = var6;
                     var4 = var7;
                  }
               }
            }

            if (this.ordering.compare(var4, var2) < 0) {
               MinMaxPriorityQueue.access$500(this.this$0)[var1] = var4;
               MinMaxPriorityQueue.access$500(this.this$0)[var3] = var2;
               return var3;
            } else {
               MinMaxPriorityQueue.access$500(this.this$0)[var1] = var2;
               return var1;
            }
         }
      }

      int getCorrectLastElement(Object var1) {
         int var2 = this.getParentIndex(MinMaxPriorityQueue.access$600(this.this$0));
         if (var2 != 0) {
            int var3 = this.getParentIndex(var2);
            int var4 = this.getRightChildIndex(var3);
            if (var4 != var2 && this.getLeftChildIndex(var4) >= MinMaxPriorityQueue.access$600(this.this$0)) {
               Object var5 = this.this$0.elementData(var4);
               if (this.ordering.compare(var5, var1) < 0) {
                  MinMaxPriorityQueue.access$500(this.this$0)[var4] = var1;
                  MinMaxPriorityQueue.access$500(this.this$0)[MinMaxPriorityQueue.access$600(this.this$0)] = var5;
                  return var4;
               }
            }
         }

         return MinMaxPriorityQueue.access$600(this.this$0);
      }

      int crossOver(int var1, Object var2) {
         int var3 = this.findMinChild(var1);
         if (var3 > 0 && this.ordering.compare(this.this$0.elementData(var3), var2) < 0) {
            MinMaxPriorityQueue.access$500(this.this$0)[var1] = this.this$0.elementData(var3);
            MinMaxPriorityQueue.access$500(this.this$0)[var3] = var2;
            return var3;
         } else {
            return this.crossOverUp(var1, var2);
         }
      }

      int fillHoleAt(int var1) {
         int var2;
         while((var2 = this.findMinGrandChild(var1)) > 0) {
            MinMaxPriorityQueue.access$500(this.this$0)[var1] = this.this$0.elementData(var2);
            var1 = var2;
         }

         return var1;
      }

      private boolean verifyIndex(int var1) {
         if (this.getLeftChildIndex(var1) < MinMaxPriorityQueue.access$600(this.this$0) && this.compareElements(var1, this.getLeftChildIndex(var1)) > 0) {
            return false;
         } else if (this.getRightChildIndex(var1) < MinMaxPriorityQueue.access$600(this.this$0) && this.compareElements(var1, this.getRightChildIndex(var1)) > 0) {
            return false;
         } else if (var1 > 0 && this.compareElements(var1, this.getParentIndex(var1)) > 0) {
            return false;
         } else {
            return var1 <= 2 || this.compareElements(this.getGrandparentIndex(var1), var1) <= 0;
         }
      }

      private int getLeftChildIndex(int var1) {
         return var1 * 2 + 1;
      }

      private int getRightChildIndex(int var1) {
         return var1 * 2 + 2;
      }

      private int getParentIndex(int var1) {
         return (var1 - 1) / 2;
      }

      private int getGrandparentIndex(int var1) {
         return this.getParentIndex(this.getParentIndex(var1));
      }

      static boolean access$400(MinMaxPriorityQueue.Heap var0, int var1) {
         return var0.verifyIndex(var1);
      }
   }

   static class MoveDesc {
      final Object toTrickle;
      final Object replaced;

      MoveDesc(Object var1, Object var2) {
         this.toTrickle = var1;
         this.replaced = var2;
      }
   }

   @Beta
   public static final class Builder {
      private static final int UNSET_EXPECTED_SIZE = -1;
      private final Comparator comparator;
      private int expectedSize;
      private int maximumSize;

      private Builder(Comparator var1) {
         this.expectedSize = -1;
         this.maximumSize = Integer.MAX_VALUE;
         this.comparator = (Comparator)Preconditions.checkNotNull(var1);
      }

      public MinMaxPriorityQueue.Builder expectedSize(int var1) {
         Preconditions.checkArgument(var1 >= 0);
         this.expectedSize = var1;
         return this;
      }

      public MinMaxPriorityQueue.Builder maximumSize(int var1) {
         Preconditions.checkArgument(var1 > 0);
         this.maximumSize = var1;
         return this;
      }

      public MinMaxPriorityQueue create() {
         return this.create(Collections.emptySet());
      }

      public MinMaxPriorityQueue create(Iterable var1) {
         MinMaxPriorityQueue var2 = new MinMaxPriorityQueue(this, MinMaxPriorityQueue.initialQueueSize(this.expectedSize, this.maximumSize, var1));
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            var2.offer(var4);
         }

         return var2;
      }

      private Ordering ordering() {
         return Ordering.from(this.comparator);
      }

      Builder(Comparator var1, Object var2) {
         this(var1);
      }

      static Ordering access$200(MinMaxPriorityQueue.Builder var0) {
         return var0.ordering();
      }

      static int access$300(MinMaxPriorityQueue.Builder var0) {
         return var0.maximumSize;
      }
   }
}
