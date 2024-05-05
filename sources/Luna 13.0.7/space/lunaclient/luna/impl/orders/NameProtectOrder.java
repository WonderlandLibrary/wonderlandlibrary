package space.lunaclient.luna.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import space.lunaclient.luna.api.order.Order;
import space.lunaclient.luna.util.PlayerUtils;

public class NameProtectOrder
  implements Order
{
  public NameProtectOrder() {}
  
  public boolean run(String[] args)
  {
    if (args.length < 1) {
      PlayerUtils.tellPlayer("More args required!", false);
    }
    if (args.length > 1)
    {
      if (args[1].length() < 2) {
        PlayerUtils.tellPlayer("Invalid name, has to be over 2 letters/numbers.", false);
      }
      if (args[1].length() > 2)
      {
        if (args.length > 2)
        {
          space.lunaclient.luna.impl.elements.render.NameProtect.name = args[1].replace("&", "§") + " " + args[2].replace("&", "§");
          PlayerUtils.tellPlayer("Name changed to: " + args[1] + " " + args[2], false);
        }
        else
        {
          space.lunaclient.luna.impl.elements.render.NameProtect.name = args[1].replace("&", "§");
          PlayerUtils.tellPlayer("Name changed to: " + args[1], false);
        }
        if (args.length > 2) {
          PlayerUtils.tellPlayer("Too many args!", false);
        }
      }
      return true;
    }
    return true;
  }
  
  public String usage()
  {
    return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "nameprotect <new name>" + ChatFormatting.GRAY + " ]";
  }
}
