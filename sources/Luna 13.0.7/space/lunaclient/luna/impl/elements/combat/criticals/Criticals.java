package space.lunaclient.luna.impl.elements.combat.criticals;

import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.combat.criticals.structure.Jump;
import space.lunaclient.luna.impl.elements.combat.criticals.structure.Motion;
import space.lunaclient.luna.impl.elements.combat.criticals.structure.Packets;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.impl.managers.EventManager;

@ElementInfo(name="Criticals", category=Category.COMBAT)
public class Criticals
  extends Element
{
  @ModeSetting(name="Mode", currentOption="Packets", options={"Packets", "Motion", "Jump"}, locked=false)
  public static Setting mode;
  private Packets packets = new Packets();
  private Motion motion = new Motion();
  private Jump jump = new Jump();
  
  public Criticals() {}
  
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
    if (mode.getValString().equalsIgnoreCase("Motion"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.motion);
    }
    else if (mode.getValString().equalsIgnoreCase("Packets"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.packets);
    }
    else if (mode.getValString().equalsIgnoreCase("Jump"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.jump);
    }
    super.onEnable();
  }
  
  public void onDisable()
  {
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.packets);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.motion);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.jump);
    super.onDisable();
  }
}
