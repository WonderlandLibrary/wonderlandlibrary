package space.lunaclient.luna.impl.elements.examples.nomodes;

import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventMotion;
import space.lunaclient.luna.impl.events.EventMove;
import space.lunaclient.luna.impl.events.EventRender2D;
import space.lunaclient.luna.impl.events.EventRender3D;
import space.lunaclient.luna.impl.events.EventUpdate;

@ElementInfo(name="Basic", category=Category.PLAYER, description="Element Description here")
public class Basic
  extends Element
{
  public Basic() {}
  
  public void onEnable() {}
  
  public void onDisable() {}
  
  @EventRegister
  public void onRender3D(EventRender3D event) {}
  
  @EventRegister
  public void onRender2D(EventRender2D event) {}
  
  @EventRegister
  public void onMotion(EventMotion event) {}
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    net.minecraft.util.Timer.timerSpeed = 4.2F;
  }
  
  @EventRegister
  public void onMove(EventMove event) {}
}
