package me.rhys.client.command;

import me.rhys.base.command.Command;
import net.minecraft.client.entity.EntityPlayerSP;

public class VClip extends Command {
  public VClip(String label, String description, String... aliases) {
    super(label, description, aliases);
  }
  
  public boolean handle(EntityPlayerSP player, String label, String[] args) {
    if (args != null) {
      try {
        double value = Double.parseDouble(args[0]);
        if (Math.abs(value) > 8.0D)
          player.sendMessage("Your value is too high, you might get set back by the server!"); 
        player.setPositionAndUpdate(player.posX, player.posY + value, player.posZ);
      } catch (NumberFormatException ignored) {
        player.sendMessage("Please enter a valid number!");
      } 
    } else {
      player.sendMessage("Please supply a number!");
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\command\VClip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */