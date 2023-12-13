package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

@Beta
public class AsyncEventBus extends EventBus {
   private final Executor executor;
   private final ConcurrentLinkedQueue eventsToDispatch = new ConcurrentLinkedQueue();

   public AsyncEventBus(String var1, Executor var2) {
      super(var1);
      this.executor = (Executor)Preconditions.checkNotNull(var2);
   }

   public AsyncEventBus(Executor var1, SubscriberExceptionHandler var2) {
      super(var2);
      this.executor = (Executor)Preconditions.checkNotNull(var1);
   }

   public AsyncEventBus(Executor var1) {
      super("default");
      this.executor = (Executor)Preconditions.checkNotNull(var1);
   }

   void enqueueEvent(Object var1, EventSubscriber var2) {
      this.eventsToDispatch.offer(new EventBus.EventWithSubscriber(var1, var2));
   }

   protected void dispatchQueuedEvents() {
      while(true) {
         EventBus.EventWithSubscriber var1 = (EventBus.EventWithSubscriber)this.eventsToDispatch.poll();
         if (var1 == null) {
            return;
         }

         this.dispatch(var1.event, var1.subscriber);
      }
   }

   void dispatch(Object var1, EventSubscriber var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      this.executor.execute(new Runnable(this, var1, var2) {
         final Object val$event;
         final EventSubscriber val$subscriber;
         final AsyncEventBus this$0;

         {
            this.this$0 = var1;
            this.val$event = var2;
            this.val$subscriber = var3;
         }

         public void run() {
            AsyncEventBus.access$001(this.this$0, this.val$event, this.val$subscriber);
         }
      });
   }

   static void access$001(AsyncEventBus var0, Object var1, EventSubscriber var2) {
      var0.dispatch(var1, var2);
   }
}
