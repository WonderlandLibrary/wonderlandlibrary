package space.lunaclient.luna.impl.elements.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;

@ElementInfo(name="Haste", category=Category.PLAYER)
public class Haste
  extends Element
{
  public Haste() {}
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    if (isToggled())
    {
      if (Minecraft.playerController.curBlockDamageMP > 0.81F) {
        Minecraft.playerController.curBlockDamageMP = 1.0F;
      }
      Minecraft.playerController.blockHitDelay = 0;
    }
  }
}
