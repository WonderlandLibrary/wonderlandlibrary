/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command.exception;

import baritone.api.command.exception.ICommandException;

public abstract class CommandException
extends Exception
implements ICommandException {
    protected CommandException(String reason) {
        super(reason);
    }

    protected CommandException(String reason, Throwable cause) {
        super(reason, cause);
    }
}

