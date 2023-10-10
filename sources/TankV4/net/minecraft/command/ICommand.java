package net.minecraft.command;

import java.util.List;
import net.minecraft.util.BlockPos;

public interface ICommand extends Comparable {
   String getCommandUsage(ICommandSender var1);

   String getCommandName();

   List addTabCompletionOptions(ICommandSender var1, String[] var2, BlockPos var3);

   List getCommandAliases();

   void processCommand(ICommandSender var1, String[] var2) throws CommandException;

   boolean canCommandSenderUseCommand(ICommandSender var1);

   boolean isUsernameIndex(String[] var1, int var2);
}
