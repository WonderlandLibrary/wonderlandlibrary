package com.google.common.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class SynchronizedEventSubscriber extends EventSubscriber {
  public SynchronizedEventSubscriber(Object target, Method method) {
    super(target, method);
  }
  
  public void handleEvent(Object event) throws InvocationTargetException {
    synchronized (this) {
      super.handleEvent(event);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\eventbus\SynchronizedEventSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */