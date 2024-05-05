package space.lunaclient.luna.impl.elements.player.nofall;

import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.player.nofall.structure.AAC;
import space.lunaclient.luna.impl.elements.player.nofall.structure.CubeCraft;
import space.lunaclient.luna.impl.elements.player.nofall.structure.Normal;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.impl.managers.EventManager;

@ElementInfo(name="NoFall", category=Category.PLAYER)
public class NoFall
  extends Element
{
  @ModeSetting(name="Mode", currentOption="Normal", options={"Normal", "CubeCraft", "AAC"}, locked=false)
  public Setting mode;
  private Normal normal = new Normal();
  private CubeCraft cubeCraft = new CubeCraft();
  private AAC aac = new AAC();
  
  public NoFall() {}
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    if ((isToggled()) && 
      (!getMode().contains(this.mode.getValString())))
    {
      toggle();
      toggle();
    }
  }
  
  public void onEnable()
  {
    if (this.mode.getValString().equalsIgnoreCase("Normal"))
    {
      setMode(this.mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.normal);
    }
    else if (this.mode.getValString().equalsIgnoreCase("CubeCraft"))
    {
      setMode(this.mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.cubeCraft);
    }
    else if (this.mode.getValString().equalsIgnoreCase("AAC"))
    {
      setMode(this.mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.aac);
    }
    super.onEnable();
  }
  
  public void onDisable()
  {
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.normal);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.cubeCraft);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.aac);
    super.onDisable();
  }
}
