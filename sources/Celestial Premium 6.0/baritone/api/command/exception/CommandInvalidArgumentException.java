/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command.exception;

import baritone.api.command.argument.ICommandArgument;
import baritone.api.command.exception.CommandErrorMessageException;

public abstract class CommandInvalidArgumentException
extends CommandErrorMessageException {
    public final ICommandArgument arg;

    protected CommandInvalidArgumentException(ICommandArgument arg, String message) {
        super(CommandInvalidArgumentException.formatMessage(arg, message));
        this.arg = arg;
    }

    protected CommandInvalidArgumentException(ICommandArgument arg, String message, Throwable cause) {
        super(CommandInvalidArgumentException.formatMessage(arg, message), cause);
        this.arg = arg;
    }

    private static String formatMessage(ICommandArgument arg, String message) {
        return String.format("Error at argument #%s: %s", arg.getIndex() == -1 ? "<unknown>" : Integer.toString(arg.getIndex() + 1), message);
    }
}

