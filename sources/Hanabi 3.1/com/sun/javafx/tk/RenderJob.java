package com.sun.javafx.tk;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class RenderJob extends FutureTask {
   private CompletionListener listener;
   private Object futureReturn;

   public RenderJob(Runnable var1) {
      super(var1, (Object)null);
   }

   public RenderJob(Runnable var1, CompletionListener var2) {
      super(var1, (Object)null);
      this.setCompletionListener(var2);
   }

   public CompletionListener getCompletionListener() {
      return this.listener;
   }

   public void setCompletionListener(CompletionListener var1) {
      this.listener = var1;
   }

   public void run() {
      if (!super.runAndReset()) {
         try {
            Object var1 = super.get();
            System.err.println("RenderJob.run: failed no exception: " + var1);
         } catch (CancellationException var3) {
            System.err.println("RenderJob.run: task cancelled");
         } catch (ExecutionException var4) {
            System.err.println("RenderJob.run: internal exception");
            var4.getCause().printStackTrace();
         } catch (Throwable var5) {
            var5.printStackTrace();
         }
      } else if (this.listener != null) {
         try {
            this.listener.done(this);
         } catch (Throwable var2) {
            var2.printStackTrace();
         }
      }

   }

   public Object get() {
      return this.futureReturn;
   }

   public void setFutureReturn(Object var1) {
      this.futureReturn = var1;
   }
}
