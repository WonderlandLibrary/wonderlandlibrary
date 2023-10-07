// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j;

import org.apache.log4j.spi.LoggingEvent;
import java.util.Vector;

public class VectorAppender extends AppenderSkeleton
{
    public Vector vector;
    
    public VectorAppender() {
        this.vector = new Vector();
    }
    
    @Override
    public void activateOptions() {
    }
    
    public void append(final LoggingEvent event) {
        try {
            Thread.sleep(100L);
        }
        catch (final Exception ex) {}
        this.vector.addElement(event);
    }
    
    @Override
    public synchronized void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
    }
    
    public Vector getVector() {
        return this.vector;
    }
    
    public boolean isClosed() {
        return this.closed;
    }
    
    @Override
    public boolean requiresLayout() {
        return false;
    }
}
