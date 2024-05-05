package space.lunaclient.luna.impl.elements.movement.highjump;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.movement.highjump.structure.Border;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.impl.managers.EventManager;

@ElementInfo(name="Phase", category=Category.MOVEMENT)
public class Phase
  extends Element
{
  @ModeSetting(name="Mode", currentOption="Border", options={"Border"}, locked=false)
  public static Setting mode;
  private Border border = new Border();
  
  public Phase() {}
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    if ((isToggled()) && 
      (!getMode().contains(mode.getValString())))
    {
      toggle();
      toggle();
    }
  }
  
  public void onEnable()
  {
    if (mode.getValString().equalsIgnoreCase("Border"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.border);
    }
    super.onEnable();
  }
  
  public void onDisable()
  {
    Minecraft.thePlayer.setSpeed(0.0D);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.border);
    super.onDisable();
  }
}
