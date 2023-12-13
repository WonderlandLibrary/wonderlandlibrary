package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class ForwardingListeningExecutorService extends ForwardingExecutorService implements ListeningExecutorService {
   protected ForwardingListeningExecutorService() {
   }

   protected abstract ListeningExecutorService delegate();

   public ListenableFuture submit(Callable var1) {
      return this.delegate().submit(var1);
   }

   public ListenableFuture submit(Runnable var1) {
      return this.delegate().submit(var1);
   }

   public ListenableFuture submit(Runnable var1, Object var2) {
      return this.delegate().submit(var1, var2);
   }

   public Future submit(Runnable var1, Object var2) {
      return this.submit(var1, var2);
   }

   public Future submit(Runnable var1) {
      return this.submit(var1);
   }

   public Future submit(Callable var1) {
      return this.submit(var1);
   }

   protected ExecutorService delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }
}
