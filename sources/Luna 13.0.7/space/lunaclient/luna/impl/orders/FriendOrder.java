package space.lunaclient.luna.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.order.Order;
import space.lunaclient.luna.impl.managers.FriendManager;
import space.lunaclient.luna.util.PlayerUtils;

public class FriendOrder
  implements Order
{
  public FriendOrder() {}
  
  private boolean handleFriend(String command, String name)
  {
    return handleFriend(command, name, name);
  }
  
  private boolean handleFriend(String command, String name, String nickname)
  {
    switch (command.toLowerCase())
    {
    case "add": 
      Luna.INSTANCE.FRIEND_MANAGER.addFriend(name, nickname);
      PlayerUtils.tellPlayer("Added \"" + name + "\".", false);
      return true;
    case "remove": 
      Luna.INSTANCE.FRIEND_MANAGER.deleteFriend(name);
      PlayerUtils.tellPlayer("Deleted \"" + name + "\".", false);
      return true;
    }
    return false;
  }
  
  public boolean run(String[] args)
  {
    if (args.length == 3)
    {
      String command = args[1];
      String name = args[2];
      return handleFriend(command, name);
    }
    if (args.length == 4)
    {
      String command = args[1];
      String name = args[2];
      String nickname = args[3];
      return handleFriend(command, name, nickname);
    }
    return false;
  }
  
  public String usage()
  {
    return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "friend <add/remove> <name> <nick>" + ChatFormatting.GRAY + " ]";
  }
}
