// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Map;
import java.util.List;
import net.minecraft.util.math.BlockPos;

public interface ICommandManager
{
    int executeCommand(final ICommandSender p0, final String p1);
    
    List<String> getTabCompletions(final ICommandSender p0, final String p1, final BlockPos p2);
    
    List<ICommand> getPossibleCommands(final ICommandSender p0);
    
    Map<String, ICommand> getCommands();
}
