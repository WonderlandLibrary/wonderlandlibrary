package space.lunaclient.luna.impl.elements.player;

import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.impl.irc.IrcChatLine;
import space.lunaclient.luna.impl.irc.IrcManager;
import space.lunaclient.luna.util.PlayerUtils;

@ElementInfo(name="IRC", category=Category.PLAYER, description="Communicate with other Luna users by typing @<your message>")
public class IRC
  extends Element
{
  public IRC() {}
  
  public void onEnable()
  {
    if (!Luna.INSTANCE.isLoading)
    {
      PlayerUtils.tellPlayer("Use '@<msg>' to chat", true);
      PlayerUtils.tellPlayer("You will be know as: §8" + Luna.INSTANCE.IRC_MANAGER.getNick(), true);
    }
    super.onEnable();
  }
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    if (isToggled()) {
      for (IrcChatLine irc : Luna.INSTANCE.IRC_MANAGER.getUnreadLines())
      {
        PlayerUtils.tellPlayer("§8[§7" + irc.getSender() + "§8]§7 " + irc.getLine(), false);
        irc.setRead(true);
      }
    }
  }
}
