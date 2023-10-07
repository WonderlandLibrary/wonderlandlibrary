// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.appender.nosql;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractNoSqlConnection<W, T extends NoSqlObject<W>> implements NoSqlConnection<W, T>
{
    private final AtomicBoolean closed;
    
    public AbstractNoSqlConnection() {
        this.closed = new AtomicBoolean();
    }
    
    @Override
    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            this.closeImpl();
        }
    }
    
    protected abstract void closeImpl();
    
    @Override
    public boolean isClosed() {
        return this.closed.get();
    }
}
