// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.command;

import java.util.Map;
import java.util.List;
import net.minecraft.util.BlockPos;

public interface ICommandManager
{
    int executeCommand(final ICommandSender p0, final String p1);
    
    List getTabCompletionOptions(final ICommandSender p0, final String p1, final BlockPos p2);
    
    List getPossibleCommands(final ICommandSender p0);
    
    Map getCommands();
}
