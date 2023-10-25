/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 */
package com.sun.javafx.runtime.async;

import com.sun.javafx.runtime.async.AsyncOperation;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.runtime.async.BackgroundExecutor;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javafx.application.Platform;

public abstract class AbstractAsyncOperation<V>
implements AsyncOperation,
Callable<V> {
    protected final FutureTask<V> future;
    protected final AsyncOperationListener listener;
    private int progressGranularity = 100;
    private int progressMax;
    private int lastProgress;
    private int progressIncrement;
    private int nextProgress;
    private int bytesRead;

    protected AbstractAsyncOperation(final AsyncOperationListener<V> asyncOperationListener) {
        this.listener = asyncOperationListener;
        Callable<Object> callable = () -> this.call();
        final Runnable runnable = new Runnable(){

            @Override
            public void run() {
                if (AbstractAsyncOperation.this.future.isCancelled()) {
                    asyncOperationListener.onCancel();
                } else {
                    try {
                        asyncOperationListener.onCompletion(AbstractAsyncOperation.this.future.get());
                    }
                    catch (InterruptedException interruptedException) {
                        asyncOperationListener.onCancel();
                    }
                    catch (ExecutionException executionException) {
                        asyncOperationListener.onException(executionException);
                    }
                }
            }
        };
        this.future = new FutureTask<V>(callable){

            @Override
            protected void done() {
                try {
                    Platform.runLater((Runnable)runnable);
                }
                finally {
                    super.done();
                }
            }
        };
    }

    @Override
    public boolean isCancelled() {
        return this.future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return this.future.isDone();
    }

    @Override
    public void cancel() {
        this.future.cancel(true);
    }

    @Override
    public void start() {
        BackgroundExecutor.getExecutor().execute(this.future);
    }

    protected void notifyProgress() {
        int n = this.lastProgress;
        int n2 = this.progressMax;
        Platform.runLater(() -> this.listener.onProgress(n, n2));
    }

    protected void addProgress(int n) {
        this.bytesRead += n;
        if (this.bytesRead > this.nextProgress) {
            this.lastProgress = this.bytesRead;
            this.notifyProgress();
            this.nextProgress = (this.lastProgress / this.progressIncrement + 1) * this.progressIncrement;
        }
    }

    protected int getProgressMax() {
        return this.progressMax;
    }

    protected void setProgressMax(int n) {
        if (n == 0) {
            this.progressIncrement = this.progressGranularity;
        } else if (n == -1) {
            this.progressIncrement = this.progressGranularity;
        } else {
            this.progressMax = n;
            this.progressIncrement = n / this.progressGranularity;
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

    protected void setProgressGranularity(int n) {
        this.progressGranularity = n;
        this.progressIncrement = this.progressMax / n;
        this.nextProgress = (this.lastProgress / this.progressIncrement + 1) * this.progressIncrement;
        this.notifyProgress();
    }
}

