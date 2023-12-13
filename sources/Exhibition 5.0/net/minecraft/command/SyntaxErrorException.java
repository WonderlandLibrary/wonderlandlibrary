// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

public class SyntaxErrorException extends CommandException
{
    private static final long serialVersionUID = 1L;
    private static final String __OBFID = "CL_00001189";
    
    public SyntaxErrorException() {
        this("commands.generic.snytax", new Object[0]);
    }
    
    public SyntaxErrorException(final String p_i1361_1_, final Object... p_i1361_2_) {
        super(p_i1361_1_, p_i1361_2_);
    }
}
