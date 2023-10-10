// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.COM.COMUtils;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Ole32;
import java.util.concurrent.ExecutorService;

public class ComThread
{
    private static ThreadLocal<Boolean> isCOMThread;
    ExecutorService executor;
    Runnable firstTask;
    boolean requiresInitialisation;
    long timeoutMilliseconds;
    Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    
    public ComThread(final String threadName, final long timeoutMilliseconds, final Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this(threadName, timeoutMilliseconds, uncaughtExceptionHandler, 0);
    }
    
    public ComThread(final String threadName, final long timeoutMilliseconds, final Thread.UncaughtExceptionHandler uncaughtExceptionHandler, final int coinitialiseExFlag) {
        this.requiresInitialisation = true;
        this.timeoutMilliseconds = timeoutMilliseconds;
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        this.firstTask = new Runnable() {
            @Override
            public void run() {
                try {
                    final WinNT.HRESULT hr = Ole32.INSTANCE.CoInitializeEx(null, coinitialiseExFlag);
                    ComThread.isCOMThread.set(true);
                    COMUtils.checkRC(hr);
                    ComThread.this.requiresInitialisation = false;
                }
                catch (Throwable t) {
                    ComThread.this.uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), t);
                }
            }
        };
        this.executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable r) {
                if (!ComThread.this.requiresInitialisation) {
                    throw new RuntimeException("ComThread executor has a problem.");
                }
                final Thread thread = new Thread(r, threadName);
                thread.setDaemon(true);
                thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(final Thread t, final Throwable e) {
                        ComThread.this.requiresInitialisation = true;
                        ComThread.this.uncaughtExceptionHandler.uncaughtException(t, e);
                    }
                });
                return thread;
            }
        });
    }
    
    public void terminate(final long timeoutMilliseconds) {
        try {
            this.executor.submit(new Runnable() {
                @Override
                public void run() {
                    Ole32.INSTANCE.CoUninitialize();
                }
            }).get(timeoutMilliseconds, TimeUnit.MILLISECONDS);
            this.executor.shutdown();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e2) {
            e2.printStackTrace();
        }
        catch (TimeoutException e3) {
            this.executor.shutdownNow();
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        if (!this.executor.isShutdown()) {
            this.terminate(100L);
        }
    }
    
    static void setComThread(final boolean value) {
        ComThread.isCOMThread.set(value);
    }
    
    public <T> T execute(final Callable<T> task) throws TimeoutException, InterruptedException, ExecutionException {
        Boolean comThread = ComThread.isCOMThread.get();
        if (comThread == null) {
            comThread = false;
        }
        if (comThread) {
            try {
                return task.call();
            }
            catch (Exception ex) {
                throw new ExecutionException(ex);
            }
        }
        if (this.requiresInitialisation) {
            this.executor.execute(this.firstTask);
        }
        return this.executor.submit(task).get(this.timeoutMilliseconds, TimeUnit.MILLISECONDS);
    }
    
    static {
        ComThread.isCOMThread = new ThreadLocal<Boolean>();
    }
}
