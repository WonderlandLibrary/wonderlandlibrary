package space.lunaclient.luna.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.HashMap;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.order.Order;
import space.lunaclient.luna.impl.managers.CommandManager;
import space.lunaclient.luna.util.PlayerUtils;

public class HelpOrder
  implements Order
{
  public HelpOrder() {}
  
  public boolean run(String[] args)
  {
    PlayerUtils.tellPlayer("Here is a list of all the orders:", false);
    for (Order command : Luna.INSTANCE.COMMAND_MANAGER.getCommands().values()) {
      PlayerUtils.tellPlayer(command.usage(), false);
    }
    return true;
  }
  
  public String usage()
  {
    return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "help" + ChatFormatting.GRAY + " ]";
  }
}
