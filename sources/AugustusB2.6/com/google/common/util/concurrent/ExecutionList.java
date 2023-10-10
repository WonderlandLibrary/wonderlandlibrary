// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.logging.Level;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import com.google.errorprone.annotations.concurrent.GuardedBy;
import javax.annotation.CheckForNull;
import java.util.logging.Logger;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public final class ExecutionList
{
    private static final Logger log;
    @CheckForNull
    @GuardedBy("this")
    private RunnableExecutorPair runnables;
    @GuardedBy("this")
    private boolean executed;
    
    public void add(final Runnable runnable, final Executor executor) {
        Preconditions.checkNotNull(runnable, (Object)"Runnable was null.");
        Preconditions.checkNotNull(executor, (Object)"Executor was null.");
        synchronized (this) {
            if (!this.executed) {
                this.runnables = new RunnableExecutorPair(runnable, executor, this.runnables);
                return;
            }
        }
        executeListener(runnable, executor);
    }
    
    public void execute() {
        RunnableExecutorPair list;
        synchronized (this) {
            if (this.executed) {
                return;
            }
            this.executed = true;
            list = this.runnables;
            this.runnables = null;
        }
        RunnableExecutorPair reversedList;
        RunnableExecutorPair tmp;
        for (reversedList = null; list != null; list = list.next, tmp.next = reversedList, reversedList = tmp) {
            tmp = list;
        }
        while (reversedList != null) {
            executeListener(reversedList.runnable, reversedList.executor);
            reversedList = reversedList.next;
        }
    }
    
    private static void executeListener(final Runnable runnable, final Executor executor) {
        try {
            executor.execute(runnable);
        }
        catch (RuntimeException e) {
            final Logger log = ExecutionList.log;
            final Level severe = Level.SEVERE;
            final String value = String.valueOf(runnable);
            final String value2 = String.valueOf(executor);
            log.log(severe, new StringBuilder(57 + String.valueOf(value).length() + String.valueOf(value2).length()).append("RuntimeException while executing runnable ").append(value).append(" with executor ").append(value2).toString(), e);
        }
    }
    
    static {
        log = Logger.getLogger(ExecutionList.class.getName());
    }
    
    private static final class RunnableExecutorPair
    {
        final Runnable runnable;
        final Executor executor;
        @CheckForNull
        RunnableExecutorPair next;
        
        RunnableExecutorPair(final Runnable runnable, final Executor executor, @CheckForNull final RunnableExecutorPair next) {
            this.runnable = runnable;
            this.executor = executor;
            this.next = next;
        }
    }
}
