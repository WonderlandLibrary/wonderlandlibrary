package com.google.common.eventbus;

public interface SubscriberExceptionHandler {
   void handleException(Throwable var1, SubscriberExceptionContext var2);
}
