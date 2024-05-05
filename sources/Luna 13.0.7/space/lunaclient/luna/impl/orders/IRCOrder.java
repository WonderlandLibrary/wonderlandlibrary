package space.lunaclient.luna.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Arrays;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.order.Order;
import space.lunaclient.luna.impl.irc.IrcManager;
import space.lunaclient.luna.util.PlayerUtils;

public class IRCOrder
  implements Order
{
  public IRCOrder() {}
  
  public boolean run(String[] args)
  {
    IrcManager irc = new IrcManager(Luna.INSTANCE.IRC_MANAGER.getNick());
    if (args.length == 2)
    {
      if (args[1].equalsIgnoreCase("reconnect"))
      {
        PlayerUtils.tellPlayer("Trying to reconnect...", false);
        Luna.INSTANCE.IRC_MANAGER.disconnect();
        Luna.INSTANCE.IRC_MANAGER.connect();
        PlayerUtils.tellPlayer(Luna.INSTANCE.IRC_MANAGER.isConnected() ? "Reconnected." : "Failed to reconnect, try again.", false);
        return true;
      }
      if (args[1].equalsIgnoreCase("users"))
      {
        PlayerUtils.tellPlayer(
          "(" + Luna.INSTANCE.IRC_MANAGER.getUsers("#LunaClient").length + ") Usernames: " + ChatFormatting.GRAY + Arrays.toString(Luna.INSTANCE.IRC_MANAGER.getUsers("#LunaClient")).replace("[", "§7").replace("]", "§7"), false);
        
        return true;
      }
    }
    return false;
  }
  
  public String usage()
  {
    return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "irc <reconnect/users>" + ChatFormatting.GRAY + " ]";
  }
}
