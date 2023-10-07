// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

public class CommandException extends Exception
{
    private static final long serialVersionUID = 1L;
    private final Object[] errorObjects;
    private static final String __OBFID = "CL_00001187";
    
    public CommandException(final String p_i1359_1_, final Object... p_i1359_2_) {
        super(p_i1359_1_);
        this.errorObjects = p_i1359_2_;
    }
    
    public Object[] getErrorOjbects() {
        return this.errorObjects;
    }
}
