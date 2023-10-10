package com.google.common.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class SynchronizedEventSubscriber extends EventSubscriber {
   public SynchronizedEventSubscriber(Object var1, Method var2) {
      super(var1, var2);
   }

   public void handleEvent(Object var1) throws InvocationTargetException {
      synchronized(this){}
      super.handleEvent(var1);
   }
}
