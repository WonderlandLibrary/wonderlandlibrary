package io.netty.util.concurrent;

import java.util.EventListener;

public interface GenericFutureListener extends EventListener {
   void operationComplete(Future var1) throws Exception;
}
