package space.lunaclient.luna.impl.orders;

import com.mojang.realmsclient.gui.ChatFormatting;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.order.Order;
import space.lunaclient.luna.impl.managers.ElementManager;
import space.lunaclient.luna.util.PlayerUtils;

public class ToggleOrder
  implements Order
{
  public ToggleOrder() {}
  
  public boolean run(String[] args)
  {
    if (args.length == 2)
    {
      try
      {
        Element m = Luna.INSTANCE.ELEMENT_MANAGER.getElement(args[1]);
        if (args[1].equalsIgnoreCase(m.getName())) {
          m.toggle();
        }
        PlayerUtils.tellPlayer(m.getName() + (m.isToggled() ? ChatFormatting.GREEN + " on." : new StringBuilder().append(ChatFormatting.RED).append(" off.").toString()), false);
      }
      catch (Exception e)
      {
        PlayerUtils.tellPlayer("Module not found.", false);
      }
      return true;
    }
    return false;
  }
  
  public String usage()
  {
    return "USAGE: " + ChatFormatting.GRAY + "[ " + ChatFormatting.WHITE + "toggle <element>" + ChatFormatting.GRAY + " ]";
  }
}
