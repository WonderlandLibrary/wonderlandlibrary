package io.netty.util.concurrent;

final class MultithreadEventExecutorGroup$PowerOfTwoEventExecutorChooser implements MultithreadEventExecutorGroup$EventExecutorChooser {
   final MultithreadEventExecutorGroup this$0;

   private MultithreadEventExecutorGroup$PowerOfTwoEventExecutorChooser(MultithreadEventExecutorGroup var1) {
      this.this$0 = var1;
   }

   public EventExecutor next() {
      return MultithreadEventExecutorGroup.access$300(this.this$0)[MultithreadEventExecutorGroup.access$500(this.this$0).getAndIncrement() & MultithreadEventExecutorGroup.access$300(this.this$0).length - 1];
   }

   MultithreadEventExecutorGroup$PowerOfTwoEventExecutorChooser(MultithreadEventExecutorGroup var1, Object var2) {
      this(var1);
   }
}
