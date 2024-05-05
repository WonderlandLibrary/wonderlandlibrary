package space.lunaclient.luna.impl.elements.player.autoarmor;

import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.player.autoarmor.structure.Normal;
import space.lunaclient.luna.impl.elements.player.autoarmor.structure.OpenINV;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.impl.managers.EventManager;

@ElementInfo(name="AutoArmor", category=Category.PLAYER, description="Allows you to auto equip armor.")
public class AutoArmor
  extends Element
{
  @ModeSetting(name="Mode", currentOption="Normal", options={"Normal", "OpenINV"}, locked=false)
  public static Setting mode;
  private Normal normal = new Normal();
  private OpenINV openINV = new OpenINV();
  
  public AutoArmor() {}
  
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
    if (mode.getValString().equalsIgnoreCase("Normal"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.normal);
    }
    else if (mode.getValString().equalsIgnoreCase("OpenINV"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.openINV);
    }
    super.onEnable();
  }
  
  public void onDisable()
  {
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.normal);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.openINV);
    super.onDisable();
  }
}
