// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

public class PlayerNotFoundException extends CommandException
{
    private static final long serialVersionUID = 1L;
    private static final String __OBFID = "CL_00001190";
    
    public PlayerNotFoundException() {
        this("commands.generic.player.notFound", new Object[0]);
    }
    
    public PlayerNotFoundException(final String p_i1362_1_, final Object... p_i1362_2_) {
        super(p_i1362_1_, p_i1362_2_);
    }
}
