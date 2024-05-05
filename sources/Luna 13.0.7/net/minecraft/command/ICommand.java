package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;

public abstract interface ICommand
  extends Comparable
{
  public abstract String getCommandName();
  
  public abstract String getCommandUsage(ICommandSender paramICommandSender);
  
  public abstract List getCommandAliases();
  
  public abstract void processCommand(ICommandSender paramICommandSender, String[] paramArrayOfString)
    throws CommandException;
  
  public abstract boolean canCommandSenderUseCommand(ICommandSender paramICommandSender);
  
  public abstract List addTabCompletionOptions(ICommandSender paramICommandSender, String[] paramArrayOfString, BlockPos paramBlockPos);
  
  public abstract boolean isUsernameIndex(String[] paramArrayOfString, int paramInt);
}
