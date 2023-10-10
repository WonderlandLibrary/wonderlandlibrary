// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.status;

import org.apache.logging.log4j.Level;
import java.util.EventListener;
import java.io.Closeable;

public interface StatusListener extends Closeable, EventListener
{
    void log(final StatusData data);
    
    Level getStatusLevel();
}
