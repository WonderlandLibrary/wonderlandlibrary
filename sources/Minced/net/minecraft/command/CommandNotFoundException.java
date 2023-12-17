// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

public class CommandNotFoundException extends CommandException
{
    public CommandNotFoundException() {
        this("commands.generic.notFound", new Object[0]);
    }
    
    public CommandNotFoundException(final String message, final Object... args) {
        super(message, args);
    }
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
