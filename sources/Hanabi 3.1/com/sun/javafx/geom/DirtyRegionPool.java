package com.sun.javafx.geom;

import java.util.Deque;
import java.util.LinkedList;

public final class DirtyRegionPool {
   private static final int POOL_SIZE_MIN = 4;
   private static final int EXPIRATION_TIME = 3000;
   private static final int COUNT_BETWEEN_EXPIRATION_CHECK = 90;
   private final int containerSize;
   private int clearCounter = 90;
   private final Deque fixed;
   private final Deque unlocked;
   private final Deque locked;

   public DirtyRegionPool(int var1) {
      this.containerSize = var1;
      this.fixed = new LinkedList();
      this.unlocked = new LinkedList();
      this.locked = new LinkedList();

      for(int var2 = 0; var2 < 4; ++var2) {
         this.fixed.add(new DirtyRegionContainer(var1));
      }

   }

   public DirtyRegionContainer checkOut() {
      this.clearExpired();
      if (!this.fixed.isEmpty()) {
         return (DirtyRegionContainer)this.fixed.pop();
      } else if (!this.unlocked.isEmpty()) {
         PoolItem var2 = (PoolItem)this.unlocked.pop();
         this.locked.push(var2);
         return var2.container;
      } else {
         DirtyRegionContainer var1 = new DirtyRegionContainer(this.containerSize);
         this.locked.push(new PoolItem((DirtyRegionContainer)null, -1L));
         return var1;
      }
   }

   public void checkIn(DirtyRegionContainer var1) {
      var1.reset();
      if (this.locked.isEmpty()) {
         this.fixed.push(var1);
      } else {
         PoolItem var2 = (PoolItem)this.locked.pop();
         var2.container = var1;
         var2.timeStamp = System.currentTimeMillis();
         this.unlocked.push(var2);
      }

   }

   private void clearExpired() {
      if (!this.unlocked.isEmpty()) {
         if (this.clearCounter-- == 0) {
            this.clearCounter = 90;
            PoolItem var1 = (PoolItem)this.unlocked.peekLast();

            for(long var2 = System.currentTimeMillis(); var1 != null && var1.timeStamp + 3000L < var2; var1 = (PoolItem)this.unlocked.peekLast()) {
               this.unlocked.removeLast();
            }
         }

      }
   }

   private class PoolItem {
      DirtyRegionContainer container;
      long timeStamp;

      public PoolItem(DirtyRegionContainer var2, long var3) {
         this.container = var2;
         this.timeStamp = var3;
      }
   }
}
