package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import java.lang.reflect.Method;

public class SubscriberExceptionContext {
   private final EventBus eventBus;
   private final Object event;
   private final Object subscriber;
   private final Method subscriberMethod;

   SubscriberExceptionContext(EventBus var1, Object var2, Object var3, Method var4) {
      this.eventBus = (EventBus)Preconditions.checkNotNull(var1);
      this.event = Preconditions.checkNotNull(var2);
      this.subscriber = Preconditions.checkNotNull(var3);
      this.subscriberMethod = (Method)Preconditions.checkNotNull(var4);
   }

   public EventBus getEventBus() {
      return this.eventBus;
   }

   public Object getEvent() {
      return this.event;
   }

   public Object getSubscriber() {
      return this.subscriber;
   }

   public Method getSubscriberMethod() {
      return this.subscriberMethod;
   }
}
