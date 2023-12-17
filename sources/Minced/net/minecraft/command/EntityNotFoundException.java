// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

public class EntityNotFoundException extends CommandException
{
    public EntityNotFoundException(final String p_i47332_1_) {
        this("commands.generic.entity.notFound", new Object[] { p_i47332_1_ });
    }
    
    public EntityNotFoundException(final String message, final Object... args) {
        super(message, args);
    }
    
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
