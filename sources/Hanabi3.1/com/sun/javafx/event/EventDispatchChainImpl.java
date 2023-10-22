package com.sun.javafx.event;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;

public class EventDispatchChainImpl implements EventDispatchChain {
   private static final int CAPACITY_GROWTH_FACTOR = 8;
   private EventDispatcher[] dispatchers;
   private int[] nextLinks;
   private int reservedCount;
   private int activeCount;
   private int headIndex;
   private int tailIndex;

   public void reset() {
      for(int var1 = 0; var1 < this.reservedCount; ++var1) {
         this.dispatchers[var1] = null;
      }

      this.reservedCount = 0;
      this.activeCount = 0;
      this.headIndex = 0;
      this.tailIndex = 0;
   }

   public EventDispatchChain append(EventDispatcher var1) {
      this.ensureCapacity(this.reservedCount + 1);
      if (this.activeCount == 0) {
         this.insertFirst(var1);
         return this;
      } else {
         this.dispatchers[this.reservedCount] = var1;
         this.nextLinks[this.tailIndex] = this.reservedCount;
         this.tailIndex = this.reservedCount;
         ++this.activeCount;
         ++this.reservedCount;
         return this;
      }
   }

   public EventDispatchChain prepend(EventDispatcher var1) {
      this.ensureCapacity(this.reservedCount + 1);
      if (this.activeCount == 0) {
         this.insertFirst(var1);
         return this;
      } else {
         this.dispatchers[this.reservedCount] = var1;
         this.nextLinks[this.reservedCount] = this.headIndex;
         this.headIndex = this.reservedCount;
         ++this.activeCount;
         ++this.reservedCount;
         return this;
      }
   }

   public Event dispatchEvent(Event var1) {
      if (this.activeCount == 0) {
         return var1;
      } else {
         int var2 = this.headIndex;
         int var3 = this.tailIndex;
         int var4 = this.activeCount;
         int var5 = this.reservedCount;
         EventDispatcher var6 = this.dispatchers[this.headIndex];
         this.headIndex = this.nextLinks[this.headIndex];
         --this.activeCount;
         Event var7 = var6.dispatchEvent(var1, this);
         this.headIndex = var2;
         this.tailIndex = var3;
         this.activeCount = var4;
         this.reservedCount = var5;
         return var7;
      }
   }

   private void insertFirst(EventDispatcher var1) {
      this.dispatchers[this.reservedCount] = var1;
      this.headIndex = this.reservedCount;
      this.tailIndex = this.reservedCount;
      this.activeCount = 1;
      ++this.reservedCount;
   }

   private void ensureCapacity(int var1) {
      int var2 = var1 + 8 - 1 & -8;
      if (var2 != 0) {
         if (this.dispatchers == null || this.dispatchers.length < var2) {
            EventDispatcher[] var3 = new EventDispatcher[var2];
            int[] var4 = new int[var2];
            if (this.reservedCount > 0) {
               System.arraycopy(this.dispatchers, 0, var3, 0, this.reservedCount);
               System.arraycopy(this.nextLinks, 0, var4, 0, this.reservedCount);
            }

            this.dispatchers = var3;
            this.nextLinks = var4;
         }

      }
   }
}
