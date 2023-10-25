/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk;

import com.sun.javafx.tk.CompletionListener;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class RenderJob
extends FutureTask {
    private CompletionListener listener;
    private Object futureReturn;

    public RenderJob(Runnable runnable) {
        super(runnable, null);
    }

    public RenderJob(Runnable runnable, CompletionListener completionListener) {
        super(runnable, null);
        this.setCompletionListener(completionListener);
    }

    public CompletionListener getCompletionListener() {
        return this.listener;
    }

    public void setCompletionListener(CompletionListener completionListener) {
        this.listener = completionListener;
    }

    @Override
    public void run() {
        if (!super.runAndReset()) {
            try {
                Object v = super.get();
                System.err.println("RenderJob.run: failed no exception: " + v);
            }
            catch (CancellationException cancellationException) {
                System.err.println("RenderJob.run: task cancelled");
            }
            catch (ExecutionException executionException) {
                System.err.println("RenderJob.run: internal exception");
                executionException.getCause().printStackTrace();
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else if (this.listener != null) {
            try {
                this.listener.done(this);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    @Override
    public Object get() {
        return this.futureReturn;
    }

    public void setFutureReturn(Object object) {
        this.futureReturn = object;
    }
}

