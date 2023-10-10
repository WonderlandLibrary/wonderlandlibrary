// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.ExceptionHandler;

abstract class AbstractAsyncExceptionHandler<T> implements ExceptionHandler<T>
{
    public void handleEventException(final Throwable throwable, final long sequence, final T event) {
        try {
            System.err.print("AsyncLogger error handling event seq=");
            System.err.print(sequence);
            System.err.print(", value='");
            try {
                System.err.print(event);
            }
            catch (Throwable t) {
                System.err.print("ERROR calling toString() on ");
                System.err.print(event.getClass().getName());
                System.err.print(": ");
                System.err.print(t.getClass().getName());
                System.err.print(": ");
                System.err.print(t.getMessage());
            }
            System.err.print("': ");
            System.err.print(throwable.getClass().getName());
            System.err.print(": ");
            System.err.println(throwable.getMessage());
            throwable.printStackTrace();
        }
        catch (Throwable t2) {}
    }
    
    public void handleOnStartException(final Throwable throwable) {
        System.err.println("AsyncLogger error starting:");
        throwable.printStackTrace();
    }
    
    public void handleOnShutdownException(final Throwable throwable) {
        System.err.println("AsyncLogger error shutting down:");
        throwable.printStackTrace();
    }
}
