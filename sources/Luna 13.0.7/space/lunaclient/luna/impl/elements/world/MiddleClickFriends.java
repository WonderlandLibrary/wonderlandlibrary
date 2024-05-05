package space.lunaclient.luna.impl.elements.world;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventMiddleClick;
import space.lunaclient.luna.impl.managers.FriendManager;
import space.lunaclient.luna.util.PlayerUtils;

@ElementInfo(name="MCF", category=Category.WORLD, description="Allows you to middle click on players to add them as friends.")
public class MiddleClickFriends
  extends Element
{
  public MiddleClickFriends() {}
  
  public void onEnable()
  {
    super.onEnable();
  }
  
  public void onDisable()
  {
    super.onDisable();
  }
  
  @EventRegister
  public void onMiddle(EventMiddleClick e)
  {
    if ((mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) && ((mc.objectMouseOver.entityHit instanceof EntityPlayer)))
    {
      String s = mc.objectMouseOver.entityHit.getName();
      if (!FriendManager.isFriend(s))
      {
        INSTANCE.FRIEND_MANAGER.addFriend(s, s);
        PlayerUtils.tellPlayer("Added \"" + s + "\".", false);
      }
      else
      {
        INSTANCE.FRIEND_MANAGER.deleteFriend(s);
        PlayerUtils.tellPlayer("Deleted \"" + s + "\".", false);
      }
    }
  }
}
