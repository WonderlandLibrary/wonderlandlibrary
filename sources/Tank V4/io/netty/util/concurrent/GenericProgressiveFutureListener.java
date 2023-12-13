package io.netty.util.concurrent;

public interface GenericProgressiveFutureListener extends GenericFutureListener {
   void operationProgressed(ProgressiveFuture var1, long var2, long var4) throws Exception;
}
