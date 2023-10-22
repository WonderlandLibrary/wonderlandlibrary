package com.sun.javafx.runtime.async;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javafx.application.Platform;

public abstract class AbstractAsyncOperation implements AsyncOperation, Callable {
   protected final FutureTask future;
   protected final AsyncOperationListener listener;
   private int progressGranularity = 100;
   private int progressMax;
   private int lastProgress;
   private int progressIncrement;
   private int nextProgress;
   private int bytesRead;

   protected AbstractAsyncOperation(final AsyncOperationListener var1) {
      this.listener = var1;
      Callable var2 = () -> {
         return this.call();
      };
      final Runnable var3 = new Runnable() {
         public void run() {
            if (AbstractAsyncOperation.this.future.isCancelled()) {
               var1.onCancel();
            } else {
               try {
                  var1.onCompletion(AbstractAsyncOperation.this.future.get());
               } catch (InterruptedException var2) {
                  var1.onCancel();
               } catch (ExecutionException var3) {
                  var1.onException(var3);
               }
            }

         }
      };
      this.future = new FutureTask(var2) {
         protected void done() {
            try {
               Platform.runLater(var3);
            } finally {
               super.done();
            }

         }
      };
   }

   public boolean isCancelled() {
      return this.future.isCancelled();
   }

   public boolean isDone() {
      return this.future.isDone();
   }

   public void cancel() {
      this.future.cancel(true);
   }

   public void start() {
      BackgroundExecutor.getExecutor().execute(this.future);
   }

   protected void notifyProgress() {
      int var1 = this.lastProgress;
      int var2 = this.progressMax;
      Platform.runLater(() -> {
         this.listener.onProgress(var1, var2);
      });
   }

   protected void addProgress(int var1) {
      this.bytesRead += var1;
      if (this.bytesRead > this.nextProgress) {
         this.lastProgress = this.bytesRead;
         this.notifyProgress();
         this.nextProgress = (this.lastProgress / this.progressIncrement + 1) * this.progressIncrement;
      }

   }

   protected int getProgressMax() {
      return this.progressMax;
   }

   protected void setProgressMax(int var1) {
      if (var1 == 0) {
         this.progressIncrement = this.progressGranularity;
      } else if (var1 == -1) {
         this.progressIncrement = this.progressGranularity;
      } else {
         this.progressMax = var1;
         this.progressIncrement = var1 / this.progressGranularity;
         if (this.progressIncrement < 1) {
            this.progressIncrement = 1;
         }
      }

      this.nextProgress = (this.lastProgress / this.progressIncrement + 1) * this.progressIncrement;
      this.notifyProgress();
   }

   protected int getProgressGranularity() {
      return this.progressGranularity;
   }

   protected void setProgressGranularity(int var1) {
      this.progressGranularity = var1;
      this.progressIncrement = this.progressMax / var1;
      this.nextProgress = (this.lastProgress / this.progressIncrement + 1) * this.progressIncrement;
      this.notifyProgress();
   }
}
