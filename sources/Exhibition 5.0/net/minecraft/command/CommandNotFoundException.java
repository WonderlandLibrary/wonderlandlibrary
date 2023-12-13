// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

public class CommandNotFoundException extends CommandException
{
    private static final long serialVersionUID = 1L;
    private static final String __OBFID = "CL_00001191";
    
    public CommandNotFoundException() {
        this("commands.generic.notFound", new Object[0]);
    }
    
    public CommandNotFoundException(final String p_i1363_1_, final Object... p_i1363_2_) {
        super(p_i1363_1_, p_i1363_2_);
    }
}
