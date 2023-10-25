package net.minecraft.command;

import java.util.List;
import java.util.Map;
import net.minecraft.util.BlockPos;

public interface ICommandManager {
  int executeCommand(ICommandSender paramICommandSender, String paramString);
  
  List<String> getTabCompletionOptions(ICommandSender paramICommandSender, String paramString, BlockPos paramBlockPos);
  
  List<ICommand> getPossibleCommands(ICommandSender paramICommandSender);
  
  Map<String, ICommand> getCommands();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\command\ICommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */