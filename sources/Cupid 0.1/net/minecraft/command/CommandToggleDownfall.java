package net.minecraft.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandToggleDownfall extends CommandBase {
  public String getCommandName() {
    return "toggledownfall";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.downfall.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args) throws CommandException {
    toggleDownfall();
    notifyOperators(sender, this, "commands.downfall.success", new Object[0]);
  }
  
  protected void toggleDownfall() {
    WorldInfo worldinfo = (MinecraftServer.getServer()).worldServers[0].getWorldInfo();
    worldinfo.setRaining(!worldinfo.isRaining());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\command\CommandToggleDownfall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */