package com.sun.javafx.event;

import javafx.event.Event;
import javafx.event.EventDispatcher;

public final class EventDispatchTreeImpl implements EventDispatchTree {
   private static final int CAPACITY_GROWTH_FACTOR = 8;
   private static final int NULL_INDEX = -1;
   private EventDispatcher[] dispatchers;
   private int[] nextChildren;
   private int[] nextSiblings;
   private int reservedCount;
   private int rootIndex = -1;
   private int tailFirstIndex = -1;
   private int tailLastIndex = -1;
   private boolean expandTailFirstPath;

   public void reset() {
      for(int var1 = 0; var1 < this.reservedCount; ++var1) {
         this.dispatchers[var1] = null;
      }

      this.reservedCount = 0;
      this.rootIndex = -1;
      this.tailFirstIndex = -1;
      this.tailLastIndex = -1;
   }

   public EventDispatchTree createTree() {
      return new EventDispatchTreeImpl();
   }

   public EventDispatchTree mergeTree(EventDispatchTree var1) {
      if (this.tailFirstIndex != -1) {
         if (this.rootIndex != -1) {
            this.expandTailFirstPath = true;
            this.expandTail(this.rootIndex);
         } else {
            this.rootIndex = this.tailFirstIndex;
         }

         this.tailFirstIndex = -1;
         this.tailLastIndex = -1;
      }

      EventDispatchTreeImpl var2 = (EventDispatchTreeImpl)var1;
      int var3 = var2.rootIndex != -1 ? var2.rootIndex : var2.tailFirstIndex;
      if (this.rootIndex == -1) {
         this.rootIndex = this.copyTreeLevel(var2, var3);
      } else {
         this.mergeTreeLevel(var2, this.rootIndex, var3);
      }

      return this;
   }

   public EventDispatchTree append(EventDispatcher var1) {
      this.ensureCapacity(this.reservedCount + 1);
      this.dispatchers[this.reservedCount] = var1;
      this.nextSiblings[this.reservedCount] = -1;
      this.nextChildren[this.reservedCount] = -1;
      if (this.tailFirstIndex == -1) {
         this.tailFirstIndex = this.reservedCount;
      } else {
         this.nextChildren[this.tailLastIndex] = this.reservedCount;
      }

      this.tailLastIndex = this.reservedCount++;
      return this;
   }

   public EventDispatchTree prepend(EventDispatcher var1) {
      this.ensureCapacity(this.reservedCount + 1);
      this.dispatchers[this.reservedCount] = var1;
      this.nextSiblings[this.reservedCount] = -1;
      this.nextChildren[this.reservedCount] = this.rootIndex;
      this.rootIndex = this.reservedCount++;
      return this;
   }

   public Event dispatchEvent(Event var1) {
      if (this.rootIndex == -1) {
         if (this.tailFirstIndex == -1) {
            return var1;
         }

         this.rootIndex = this.tailFirstIndex;
         this.tailFirstIndex = -1;
         this.tailLastIndex = -1;
      }

      int var2 = this.reservedCount;
      int var3 = this.rootIndex;
      int var4 = this.tailFirstIndex;
      int var5 = this.tailLastIndex;
      Event var6 = null;
      int var7 = this.rootIndex;

      do {
         this.rootIndex = this.nextChildren[var7];
         Event var8 = this.dispatchers[var7].dispatchEvent(var1, this);
         if (var8 != null) {
            var6 = var6 != null ? var1 : var8;
         }

         var7 = this.nextSiblings[var7];
      } while(var7 != -1);

      this.reservedCount = var2;
      this.rootIndex = var3;
      this.tailFirstIndex = var4;
      this.tailLastIndex = var5;
      return var6;
   }

   public String toString() {
      int var1 = this.rootIndex != -1 ? this.rootIndex : this.tailFirstIndex;
      if (var1 == -1) {
         return "()";
      } else {
         StringBuilder var2 = new StringBuilder();
         this.appendTreeLevel(var2, var1);
         return var2.toString();
      }
   }

   private void ensureCapacity(int var1) {
      int var2 = var1 + 8 - 1 & -8;
      if (var2 != 0) {
         if (this.dispatchers == null || this.dispatchers.length < var2) {
            EventDispatcher[] var3 = new EventDispatcher[var2];
            int[] var4 = new int[var2];
            int[] var5 = new int[var2];
            if (this.reservedCount > 0) {
               System.arraycopy(this.dispatchers, 0, var3, 0, this.reservedCount);
               System.arraycopy(this.nextChildren, 0, var4, 0, this.reservedCount);
               System.arraycopy(this.nextSiblings, 0, var5, 0, this.reservedCount);
            }

            this.dispatchers = var3;
            this.nextChildren = var4;
            this.nextSiblings = var5;
         }

      }
   }

   private void expandTail(int var1) {
      for(int var2 = var1; var2 != -1; var2 = this.nextSiblings[var2]) {
         if (this.nextChildren[var2] != -1) {
            this.expandTail(this.nextChildren[var2]);
         } else if (this.expandTailFirstPath) {
            this.nextChildren[var2] = this.tailFirstIndex;
            this.expandTailFirstPath = false;
         } else {
            int var3 = this.copyTreeLevel(this, this.tailFirstIndex);
            this.nextChildren[var2] = var3;
         }
      }

   }

   private void mergeTreeLevel(EventDispatchTreeImpl var1, int var2, int var3) {
      for(int var4 = var3; var4 != -1; var4 = var1.nextSiblings[var4]) {
         EventDispatcher var5 = var1.dispatchers[var4];
         int var6 = var2;

         int var7;
         for(var7 = var2; var6 != -1 && var5 != this.dispatchers[var6]; var6 = this.nextSiblings[var6]) {
            var7 = var6;
         }

         int var8;
         if (var6 == -1) {
            var8 = this.copySubtree(var1, var4);
            this.nextSiblings[var7] = var8;
            this.nextSiblings[var8] = -1;
         } else {
            var8 = this.nextChildren[var6];
            int var9 = getChildIndex(var1, var4);
            if (var8 != -1) {
               this.mergeTreeLevel(var1, var8, var9);
            } else {
               var8 = this.copyTreeLevel(var1, var9);
               this.nextChildren[var6] = var8;
            }
         }
      }

   }

   private int copyTreeLevel(EventDispatchTreeImpl var1, int var2) {
      if (var2 == -1) {
         return -1;
      } else {
         int var4 = this.copySubtree(var1, var2);
         int var5 = var4;

         for(int var3 = var1.nextSiblings[var2]; var3 != -1; var3 = var1.nextSiblings[var3]) {
            int var6 = this.copySubtree(var1, var3);
            this.nextSiblings[var5] = var6;
            var5 = var6;
         }

         this.nextSiblings[var5] = -1;
         return var4;
      }
   }

   private int copySubtree(EventDispatchTreeImpl var1, int var2) {
      this.ensureCapacity(this.reservedCount + 1);
      int var3 = this.reservedCount++;
      int var4 = this.copyTreeLevel(var1, getChildIndex(var1, var2));
      this.dispatchers[var3] = var1.dispatchers[var2];
      this.nextChildren[var3] = var4;
      return var3;
   }

   private void appendTreeLevel(StringBuilder var1, int var2) {
      var1.append('(');
      this.appendSubtree(var1, var2);

      for(int var3 = this.nextSiblings[var2]; var3 != -1; var3 = this.nextSiblings[var3]) {
         var1.append(",");
         this.appendSubtree(var1, var3);
      }

      var1.append(')');
   }

   private void appendSubtree(StringBuilder var1, int var2) {
      var1.append(this.dispatchers[var2]);
      int var3 = getChildIndex(this, var2);
      if (var3 != -1) {
         var1.append("->");
         this.appendTreeLevel(var1, var3);
      }

   }

   private static int getChildIndex(EventDispatchTreeImpl var0, int var1) {
      int var2 = var0.nextChildren[var1];
      if (var2 == -1 && var1 != var0.tailLastIndex) {
         var2 = var0.tailFirstIndex;
      }

      return var2;
   }
}
