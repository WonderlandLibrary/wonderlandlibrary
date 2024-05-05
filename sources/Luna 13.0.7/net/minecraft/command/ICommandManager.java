package net.minecraft.command;

import java.util.List;
import java.util.Map;
import net.minecraft.util.BlockPos;

public abstract interface ICommandManager
{
  public abstract int executeCommand(ICommandSender paramICommandSender, String paramString);
  
  public abstract List getTabCompletionOptions(ICommandSender paramICommandSender, String paramString, BlockPos paramBlockPos);
  
  public abstract List getPossibleCommands(ICommandSender paramICommandSender);
  
  public abstract Map getCommands();
}
