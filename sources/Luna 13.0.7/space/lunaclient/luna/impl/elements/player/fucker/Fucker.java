package space.lunaclient.luna.impl.elements.player.fucker;

import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.player.fucker.structure.Beds;
import space.lunaclient.luna.impl.elements.player.fucker.structure.Cakes;
import space.lunaclient.luna.impl.elements.player.fucker.structure.Snow;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.impl.managers.EventManager;

@ElementInfo(name="Fucker", category=Category.PLAYER)
public class Fucker
  extends Element
{
  @ModeSetting(name="Mode", currentOption="Beds", options={"Beds", "Cakes", "Snow"}, locked=false)
  public static Setting mode;
  private Beds beds = new Beds();
  private Snow snow = new Snow();
  private Cakes cakes = new Cakes();
  
  public Fucker() {}
  
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
    if (mode.getValString().equalsIgnoreCase("Beds"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.beds);
    }
    else if (mode.getValString().equalsIgnoreCase("Cakes"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.cakes);
    }
    else if (mode.getValString().equalsIgnoreCase("Snow"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.snow);
    }
    super.onEnable();
  }
  
  public void onDisable()
  {
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.beds);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.snow);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.cakes);
    super.onDisable();
  }
}
