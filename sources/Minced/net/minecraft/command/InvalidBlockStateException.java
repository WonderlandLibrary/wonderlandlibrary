// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

public class InvalidBlockStateException extends CommandException
{
    public InvalidBlockStateException() {
        this("commands.generic.blockstate.invalid", new Object[0]);
    }
    
    public InvalidBlockStateException(final String message, final Object... objects) {
        super(message, objects);
    }
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
