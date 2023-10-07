// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

public class NumberInvalidException extends CommandException
{
    private static final long serialVersionUID = 1L;
    private static final String __OBFID = "CL_00001188";
    
    public NumberInvalidException() {
        this("commands.generic.num.invalid", new Object[0]);
    }
    
    public NumberInvalidException(final String p_i1360_1_, final Object... p_i1360_2_) {
        super(p_i1360_1_, p_i1360_2_);
    }
}
