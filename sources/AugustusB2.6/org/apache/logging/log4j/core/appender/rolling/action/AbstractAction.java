// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling.action;

import org.apache.logging.log4j.status.StatusLogger;
import java.io.IOException;
import org.apache.logging.log4j.Logger;

public abstract class AbstractAction implements Action
{
    protected static final Logger LOGGER;
    private boolean complete;
    private boolean interrupted;
    
    protected AbstractAction() {
        this.complete = false;
        this.interrupted = false;
    }
    
    @Override
    public abstract boolean execute() throws IOException;
    
    @Override
    public synchronized void run() {
        if (!this.interrupted) {
            try {
                this.execute();
            }
            catch (RuntimeException | IOException ex3) {
                final Exception ex2;
                final Exception ex = ex2;
                this.reportException(ex);
            }
            catch (Error e) {
                this.reportException(new RuntimeException(e));
            }
            this.complete = true;
            this.interrupted = true;
        }
    }
    
    @Override
    public synchronized void close() {
        this.interrupted = true;
    }
    
    @Override
    public boolean isComplete() {
        return this.complete;
    }
    
    public boolean isInterrupted() {
        return this.interrupted;
    }
    
    protected void reportException(final Exception ex) {
        AbstractAction.LOGGER.warn("Exception reported by action '{}'", this.getClass(), ex);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
