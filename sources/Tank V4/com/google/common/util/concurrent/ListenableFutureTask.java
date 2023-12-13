package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import javax.annotation.Nullable;

public class ListenableFutureTask extends FutureTask implements ListenableFuture {
   private final ExecutionList executionList = new ExecutionList();

   public static ListenableFutureTask create(Callable var0) {
      return new ListenableFutureTask(var0);
   }

   public static ListenableFutureTask create(Runnable var0, @Nullable Object var1) {
      return new ListenableFutureTask(var0, var1);
   }

   ListenableFutureTask(Callable var1) {
      super(var1);
   }

   ListenableFutureTask(Runnable var1, @Nullable Object var2) {
      super(var1, var2);
   }

   public void addListener(Runnable var1, Executor var2) {
      this.executionList.add(var1, var2);
   }

   protected void done() {
      this.executionList.execute();
   }
}
