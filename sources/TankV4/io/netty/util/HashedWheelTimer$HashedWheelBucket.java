package io.netty.util;

import java.util.Set;

final class HashedWheelTimer$HashedWheelBucket {
   private HashedWheelTimer.HashedWheelTimeout head;
   private HashedWheelTimer.HashedWheelTimeout tail;
   static final boolean $assertionsDisabled = !HashedWheelTimer.class.desiredAssertionStatus();

   private HashedWheelTimer$HashedWheelBucket() {
   }

   public void addTimeout(HashedWheelTimer.HashedWheelTimeout var1) {
      if (!$assertionsDisabled && var1.bucket != null) {
         throw new AssertionError();
      } else {
         var1.bucket = this;
         if (this.head == null) {
            this.head = this.tail = var1;
         } else {
            this.tail.next = var1;
            var1.prev = this.tail;
            this.tail = var1;
         }

      }
   }

   public void expireTimeouts(long var1) {
      HashedWheelTimer.HashedWheelTimeout var5;
      for(HashedWheelTimer.HashedWheelTimeout var3 = this.head; var3 != null; var3 = var5) {
         boolean var4 = false;
         if (var3.remainingRounds <= 0L) {
            if (HashedWheelTimer.HashedWheelTimeout.access$800(var3) > var1) {
               throw new IllegalStateException(String.format("timeout.deadline (%d) > deadline (%d)", HashedWheelTimer.HashedWheelTimeout.access$800(var3), var1));
            }

            var3.expire();
            var4 = true;
         } else if (var3.isCancelled()) {
            var4 = true;
         } else {
            --var3.remainingRounds;
         }

         var5 = var3.next;
         if (var4) {
            this.remove(var3);
         }
      }

   }

   public void remove(HashedWheelTimer.HashedWheelTimeout var1) {
      HashedWheelTimer.HashedWheelTimeout var2 = var1.next;
      if (var1.prev != null) {
         var1.prev.next = var2;
      }

      if (var1.next != null) {
         var1.next.prev = var1.prev;
      }

      if (var1 == this.head) {
         if (var1 == this.tail) {
            this.tail = null;
            this.head = null;
         } else {
            this.head = var2;
         }
      } else if (var1 == this.tail) {
         this.tail = var1.prev;
      }

      var1.prev = null;
      var1.next = null;
      var1.bucket = null;
   }

   public void clearTimeouts(Set var1) {
      while(true) {
         HashedWheelTimer.HashedWheelTimeout var2 = this.pollTimeout();
         if (var2 == null) {
            return;
         }

         if (!var2.isExpired() && !var2.isCancelled()) {
            var1.add(var2);
         }
      }
   }

   private HashedWheelTimer.HashedWheelTimeout pollTimeout() {
      HashedWheelTimer.HashedWheelTimeout var1 = this.head;
      if (var1 == null) {
         return null;
      } else {
         HashedWheelTimer.HashedWheelTimeout var2 = var1.next;
         if (var2 == null) {
            this.tail = this.head = null;
         } else {
            this.head = var2;
            var2.prev = null;
         }

         var1.next = null;
         var1.prev = null;
         var1.bucket = null;
         return var1;
      }
   }

   HashedWheelTimer$HashedWheelBucket(HashedWheelTimer$1 var1) {
      this();
   }
}
