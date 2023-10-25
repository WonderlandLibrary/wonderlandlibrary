package me.rhys.base.command;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerChatEvent;
import me.rhys.base.util.ArrayUtil;
import me.rhys.base.util.container.Container;
import net.minecraft.client.entity.EntityPlayerSP;

public class CommandFactory extends Container<Command> {
  private final char prefix;
  
  public CommandFactory(char prefix) {
    this.prefix = prefix;
  }
  
  @EventTarget
  void playerChat(PlayerChatEvent event) {
    EntityPlayerSP player = event.getPlayer();
    if (player == null)
      return; 
    String message = event.getMessage();
    if (message == null)
      return; 
    if (message.charAt(0) != this.prefix)
      return; 
    event.setCancelled(true);
    message = message.substring(1);
    String label = message.split(" ")[0];
    String[] args = ArrayUtil.offset(message.split(" "), 1);
    if (label.isEmpty()) {
      player.sendMessage("Please provide a valid command");
      return;
    } 
    Command command = (Command)find(c -> c.isAlias(label));
    if (command == null) {
      player.sendMessage("Could not find a \"" + label + "\" command.");
      return;
    } 
    if (!command.handle(player, label, args))
      player.sendMessage(command.getUsage()); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\command\CommandFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */