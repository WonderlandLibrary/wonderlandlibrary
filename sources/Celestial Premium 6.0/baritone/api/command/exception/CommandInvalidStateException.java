/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command.exception;

import baritone.api.command.exception.CommandErrorMessageException;

public class CommandInvalidStateException
extends CommandErrorMessageException {
    public CommandInvalidStateException(String reason) {
        super(reason);
    }
}

