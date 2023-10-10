package io.netty.util;

class HashedWheelTimer$HashedWheelTimeout$1 implements Runnable {
   final HashedWheelTimer.HashedWheelTimeout this$0;

   HashedWheelTimer$HashedWheelTimeout$1(HashedWheelTimer.HashedWheelTimeout var1) {
      this.this$0 = var1;
   }

   public void run() {
      HashedWheelTimer$HashedWheelBucket var1 = this.this$0.bucket;
      if (var1 != null) {
         var1.remove(this.this$0);
      }

   }
}
