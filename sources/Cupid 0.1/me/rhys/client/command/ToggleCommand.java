package me.rhys.client.command;

import me.rhys.base.Lite;
import me.rhys.base.command.Command;
import me.rhys.base.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;

public class ToggleCommand extends Command {
  public ToggleCommand(String label, String usage, String description, String... aliases) {
    super(label, usage, description, aliases);
  }
  
  public boolean handle(EntityPlayerSP player, String label, String[] args) {
    if (args != null) {
      Module module = Lite.MODULE_FACTORY.findByName(args[0]);
      if (module != null) {
        module.toggle();
        player.sendMessage(module.getData().getName() + " toggled");
      } else {
        player.sendMessage("\"" + args[0] + "\" not recognized as a module");
      } 
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\command\ToggleCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */