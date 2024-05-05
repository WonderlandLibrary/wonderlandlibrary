package space.lunaclient.luna.impl.elements.combat.antibot;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import space.lunaclient.luna.Luna;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.BooleanSetting;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.combat.antibot.structure.Advanced;
import space.lunaclient.luna.impl.elements.combat.antibot.structure.GWEN;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.impl.managers.EventManager;

@ElementInfo(name="AntiBot", category=Category.COMBAT)
public class AntiBot
  extends Element
{
  public static List<EntityPlayer> invalid = new ArrayList();
  @ModeSetting(name="Mode", currentOption="Advanced", options={"GWEN", "Advanced"}, locked=false)
  public static Setting mode;
  @BooleanSetting(name="Remove", booleanValue=true)
  public static Setting remove;
  @BooleanSetting(name="Notify", booleanValue=true)
  public static Setting notify;
  private GWEN mineplex = new GWEN();
  private Advanced advanced = new Advanced();
  
  public AntiBot() {}
  
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
    if (mode.getValString().equalsIgnoreCase("Advanced"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.advanced);
    }
    else if (mode.getValString().equalsIgnoreCase("GWEN"))
    {
      setMode(mode.getValString());
      Luna.INSTANCE.EVENT_MANAGER.register(this.mineplex);
    }
    super.onEnable();
  }
  
  public void onDisable()
  {
    Advanced.invalidID.clear();
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.mineplex);
    Luna.INSTANCE.EVENT_MANAGER.unregister(this.advanced);
    super.onDisable();
  }
}
