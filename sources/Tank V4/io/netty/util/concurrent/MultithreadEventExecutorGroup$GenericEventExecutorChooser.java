package io.netty.util.concurrent;

final class MultithreadEventExecutorGroup$GenericEventExecutorChooser implements MultithreadEventExecutorGroup$EventExecutorChooser {
   final MultithreadEventExecutorGroup this$0;

   private MultithreadEventExecutorGroup$GenericEventExecutorChooser(MultithreadEventExecutorGroup var1) {
      this.this$0 = var1;
   }

   public EventExecutor next() {
      return MultithreadEventExecutorGroup.access$300(this.this$0)[Math.abs(MultithreadEventExecutorGroup.access$500(this.this$0).getAndIncrement() % MultithreadEventExecutorGroup.access$300(this.this$0).length)];
   }

   MultithreadEventExecutorGroup$GenericEventExecutorChooser(MultithreadEventExecutorGroup var1, Object var2) {
      this(var1);
   }
}
