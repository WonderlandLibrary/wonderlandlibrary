/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.core.appender.rolling.helper.AbstractAction;
import org.apache.logging.log4j.core.appender.rolling.helper.Action;

public class CompositeAction
extends AbstractAction {
    private final Action[] actions;
    private final boolean stopOnError;

    public CompositeAction(List<Action> actions, boolean stopOnError) {
        this.actions = new Action[actions.size()];
        actions.toArray(this.actions);
        this.stopOnError = stopOnError;
    }

    @Override
    public void run() {
        try {
            this.execute();
        }
        catch (IOException ex) {
            LOGGER.warn("Exception during file rollover.", (Throwable)ex);
        }
    }

    @Override
    public boolean execute() throws IOException {
        if (this.stopOnError) {
            for (Action action : this.actions) {
                if (action.execute()) continue;
                return false;
            }
            return true;
        }
        boolean status = true;
        IOException exception = null;
        for (Action action : this.actions) {
            try {
                status &= action.execute();
            }
            catch (IOException ex) {
                status = false;
                if (exception != null) continue;
                exception = ex;
            }
        }
        if (exception != null) {
            throw exception;
        }
        return status;
    }
}

