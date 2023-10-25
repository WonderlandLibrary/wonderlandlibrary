/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.IOException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.status.StatusLogger;

public abstract class AbstractAction
implements Action {
    protected static final Logger LOGGER = StatusLogger.getLogger();
    private boolean complete = false;
    private boolean interrupted = false;

    protected AbstractAction() {
    }

    @Override
    public abstract boolean execute() throws IOException;

    @Override
    public synchronized void run() {
        if (!this.interrupted) {
            try {
                this.execute();
            }
            catch (IOException | RuntimeException ex) {
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

    protected void reportException(Exception ex) {
        LOGGER.warn("Exception reported by action '{}'", this.getClass(), ex);
    }
}

