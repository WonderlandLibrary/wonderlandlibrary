package space.lunaclient.luna.impl.elements.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.FoodStats;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.world.Scaffold;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.impl.managers.ElementManager;

@ElementInfo(name="Sprint", category=Category.MOVEMENT)
public class Sprint
  extends Element
{
  public Sprint() {}
  
  public void onEnable()
  {
    super.onEnable();
  }
  
  public void onDisable()
  {
    super.onDisable();
  }
  
  private boolean canSprint()
  {
    return (Minecraft.thePlayer.getFoodStats().getFoodLevel() > 6 ? 1 : 0) & (!Luna.INSTANCE.ELEMENT_MANAGER.getElement(Scaffold.class).isToggled() ? 1 : 0);
  }
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    Minecraft.thePlayer.setSprinting(canSprint() & Minecraft.thePlayer.isMoving());
    if (!Scaffold.mode.getValString().equalsIgnoreCase("CubeCraft")) {
      Minecraft.thePlayer.setSprinting(true);
    }
  }
}
